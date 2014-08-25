/*
 * Copyright (C) XBUP Project
 *
 * This application or library is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * This application or library is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along this application.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.xbup.tool.editor.module.picture_editor.panel;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Transparency;
import java.awt.event.ActionEvent;
import java.awt.event.InputMethodListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.PixelGrabber;
import java.awt.image.RenderedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import org.xbup.lib.core.block.XBBasicBlockType;
import org.xbup.lib.core.block.XBBlockType;
import org.xbup.lib.core.block.XBFixedBlockType;
import org.xbup.lib.core.block.declaration.XBBlockDecl;
import org.xbup.lib.core.block.declaration.XBContext;
import org.xbup.lib.core.block.declaration.XBDeclaration;
import org.xbup.lib.core.block.declaration.XBFormatDecl;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.basic.convert.XBTEncapsulator;
import org.xbup.lib.core.parser.token.event.convert.XBTEventListenerToListener;
import org.xbup.lib.core.parser.token.event.convert.XBTListenerToEventListener;
import org.xbup.lib.core.parser.token.event.convert.XBTToXBEventConvertor;
import org.xbup.lib.core.parser.token.pull.XBPullReader;
import org.xbup.lib.core.parser.token.pull.convert.XBToXBTPullConvertor;
import org.xbup.lib.core.serial.XBSerializable;
import org.xbup.lib.core.serial.child.XBTChildOutputSerialHandler;
import org.xbup.lib.core.serial.child.XBTChildInputSerialHandler;
import org.xbup.lib.core.serial.child.XBTChildListenerSerialHandler;
import org.xbup.lib.core.serial.child.XBTChildProviderSerialHandler;
import org.xbup.lib.core.serial.child.XBTChildSerializable;
import org.xbup.lib.core.serial.sequence.XBSerialSequence;
import org.xbup.lib.core.serial.sequence.XBTSequenceSerialHandler;
import org.xbup.lib.core.serial.sequence.XBTSequenceSerializable;
import org.xbup.lib.core.stream.file.XBFileOutputStream;
import org.xbup.lib.core.ubnumber.UBNatural;
import org.xbup.lib.core.ubnumber.type.UBNat32;
import org.xbup.lib.visual.picture.XBBufferedImage;
import org.xbup.tool.editor.module.picture_editor.PictureFileType;
import org.xbup.tool.editor.module.picture_editor.XBPictureEditorFrame;
import org.xbup.tool.editor.module.picture_editor.dialog.FontDialog;
import org.xbup.tool.editor.module.picture_editor.dialog.ImageResizeDialog;
import org.xbup.tool.editor.module.picture_editor.dialog.ToolColorDialog;
import org.xbup.tool.editor.base.api.ApplicationFilePanel;
import org.xbup.tool.editor.base.api.FileType;

/**
 * XBPEditor image panel.
 *
 * @version 0.1.23 2014/03/04
 * @author XBUP Project (http://xbup.org)
 */
public class ImagePanel extends javax.swing.JPanel implements ApplicationFilePanel {

    public static int PNG_FILEMODE = 1;
    public static int XB_FILEMODE = 2;

    final UndoManager undo;
    private String fileName;
    private String ext;
    private FileType fileType;
    private boolean modified = false;
    private Image image;
    private Image scaledImage;
    private Graphics grph;
    private double scaleRatio;
    private Object highlight;
    private Color selectionColor;
    private Color toolColor;
    private Font defaultFont;
    private Color[] defaultColors;
    private InputMethodListener caretListener;
    private int toolMode;
    private PropertyChangeListener propertyChangeListener;

    /**
     * Creates new form ImagePanel
     */
    public ImagePanel() {
        initComponents();
        scaleRatio = 1;
        toolMode = 0;
        undo = new UndoManager();
        fileName = "";
        highlight = null;
        toolColor = Color.BLACK;
        selectionColor = Color.YELLOW;

        // if the document is ever edited, assume that it needs to be saved
/*        imageArea.getDocument().addDocumentListener(new DocumentListener() {
         public void changedUpdate(DocumentEvent e) { setModified(true); }
         public void insertUpdate(DocumentEvent e) { setModified(true); }
         public void removeUpdate(DocumentEvent e) { setModified(true); }
         }); */
        // Listen for undo and redo events
/*        imageArea.getDocument().addUndoableEditListener(new UndoableEditListener() {

         public void undoableEditHappened(UndoableEditEvent evt) {
         undo.addEdit(evt.getEdit());
         firePropertyChange("undoAvailable", false, true);
         firePropertyChange("redoAvailable", false, true);
         }
         }); */
        // Create an undo action and add it to the text component
        imageArea.getActionMap().put("Undo", new AbstractAction("Undo") {

            @Override
            public void actionPerformed(ActionEvent evt) {
                try {
                    if (undo.canUndo()) {
                        undo.undo();
                    }
                } catch (CannotUndoException ex) {
                    Logger.getLogger(ImagePanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        // Bind the undo action to ctl-Z
        imageArea.getInputMap().put(KeyStroke.getKeyStroke("control Z"), "Undo");

        // Create a redo action and add it to the text component
        imageArea.getActionMap().put("Redo", new AbstractAction("Redo") {

            @Override
            public void actionPerformed(ActionEvent evt) {
                try {
                    if (undo.canRedo()) {
                        undo.redo();
                    }
                } catch (CannotRedoException ex) {
                    Logger.getLogger(ImagePanel.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        // Bind the redo action to ctl-Y
        imageArea.getInputMap().put(KeyStroke.getKeyStroke("control Y"), "Redo");
    }

    public Icon getIcon() {
        return imageArea.getIcon();
    }

    public void performCopy() {
//        imageArea.copy();
    }

    public void performCut() {
//        imageArea.cut();
    }

    public void performDelete() {
//        imageArea.getInputContext().dispatchEvent(new KeyEvent(this, KeyEvent.KEY_PRESSED, 0, 0, KeyEvent.VK_DELETE, KeyEvent.CHAR_UNDEFINED));
    }

    public void performPaste() {
//        imageArea.paste();
    }

    public void performSelectAll() {
//        imageArea.selectAll();
    }

    public void printFile() {
        PrinterJob job = PrinterJob.getPrinterJob();
        if (job.printDialog()) {
            try {
//                PrintJob myJob = imageArea.getToolkit().getPrintJob(null, fileName, null);
//                if (myJob != null) {
                job.setPrintable(new Printable() {

                    @Override
                    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
                        imageArea.print(graphics); // TODO: Rescale on page
                        if (pageIndex == 0) {
                            return Printable.PAGE_EXISTS;
                        }
                        return Printable.NO_SUCH_PAGE;
                    }
                });
                job.print();
//                }
            } catch (PrinterException ex) {
                Logger.getLogger(ImagePanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void setCurrentFont(Font font) {
        imageArea.setFont(font);
    }

    public void showColorDialog(ToolColorDialog dlg) {
        ToolColorPanel colorPanel = dlg.getColorPanel();
        // colorPanel.setTextFindColor(getSelectionColor());
        dlg.setVisible(true);
        if (dlg.getOption() == JOptionPane.OK_OPTION) {
            // setSelectionColor(colorPanel.getTextFindColor());
        }
    }

    public void showFontDialog(FontDialog dlg) {
//        SimpleAttributeSet a = new SimpleAttributeSet();
//        StyleConstants.setFontFamily(a, "Monospaced");
//        StyleConstants.setFontSize(a, 12);
//        dlg.setAttributes(a);
        dlg.setStoredFont(imageArea.getFont());
        dlg.setVisible(true);
        if (dlg.getOption() == JOptionPane.OK_OPTION) {
            imageArea.setFont(dlg.getStoredFont());
//            textArea.setForeground(dlg.getMyColor());
        }
    }

    public Color getSelectionColor() {
        return selectionColor;
    }

    public void setSelectionColor(Color color) {
        selectionColor = color;
        if (highlight != null) {
            // TODO: Replace higlighter with the same with new color
//            ((DefaultHighlighter) highlight).getHighlights()[0].
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        imageArea = new javax.swing.JLabel();

        setAutoscrolls(true);
        setInheritsPopupMenu(true);
        setName("Form"); // NOI18N
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.LINE_AXIS));

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        imageArea.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        imageArea.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        imageArea.setAlignmentY(0.0F);
        imageArea.setAutoscrolls(true);
        imageArea.setCursor(new java.awt.Cursor(java.awt.Cursor.CROSSHAIR_CURSOR));
        imageArea.setIconTextGap(0);
        imageArea.setName("imageArea"); // NOI18N
        imageArea.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                imageAreaMouseClicked(evt);
            }
        });
        imageArea.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                imageAreaMouseDragged(evt);
            }
        });
        jScrollPane1.setViewportView(imageArea);

        add(jScrollPane1);
    }// </editor-fold>//GEN-END:initComponents

    private void imageAreaMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_imageAreaMouseDragged
        if (toolMode == 0) {
            int x = (int) (evt.getX() / scaleRatio);
            int y = (int) (evt.getY() / scaleRatio);
            if (grph != null) {
                grph.fillOval(x - 5, y - 5, 10, 10);
//                image.flush();
                scaledImage.flush();
                imageArea.repaint();
            } else {
                System.out.println("Graphics is null!");
            }
//            evt.getComponent().repaint();
            evt.consume();
        }
    }//GEN-LAST:event_imageAreaMouseDragged

    private void imageAreaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_imageAreaMouseClicked
        if (evt.getButton() == java.awt.event.MouseEvent.BUTTON1) {
            if (toolMode == 0) {
                int x = (int) (evt.getX() / scaleRatio);
                int y = (int) (evt.getY() / scaleRatio);
                if (grph != null) {
                    grph.fillOval(x - 5, y - 5, 10, 10);
//                    image.flush();
                    scaledImage.flush();
                    repaint();
                } else {
                    System.out.println("Graphics is null!");
                }
//                evt.getComponent().repaint();
                evt.consume();
            }
        }
    }//GEN-LAST:event_imageAreaMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel imageArea;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables

    /**
     * @return the modified
     */
    @Override
    public boolean isModified() {
        return modified;
    }

    /**
     * @param modified the modified to set
     */
    public void setModified(boolean modified) {
        /*        if (highlight != null) {
         imageArea.getHighlighter().removeHighlight(highlight);
         highlight = null;
         }
         boolean oldValue = this.modified;
         this.modified = modified;
         firePropertyChange("modified", oldValue, this.modified); */
    }

    public boolean isEditEnabled() {
        return false;
//        return imageArea.isEditable();
    }

    public boolean isPasteEnabled() {
        return false;
//        return imageArea.isEditable();
    }

    @Override
    public void loadFromFile() {
        if (XBPictureEditorFrame.XBPFILETYPE.equals(fileType.getFileTypeId())) {
            XBTChildInputSerialHandler handler = new XBTChildProviderSerialHandler();
            try {
                handler.attachXBTPullProvider(new XBToXBTPullConvertor(new XBPullReader(new FileInputStream(getFileName()))));
                getStubXBTDataSerializator().serializeFromXB(handler);
            } catch (XBProcessingException | IOException ex) {
                Logger.getLogger(ImagePanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            image = toBufferedImage(Toolkit.getDefaultToolkit().getImage(getFileName()));
        }
        scaledImage = image;
        grph = image.getGraphics();
        grph.setColor(toolColor);
        imageArea.setIcon(new ImageIcon(image));
        scale(scaleRatio);
    }

    @Override
    public void saveToFile() {
        File file = new File(getFileName());
        if (XBPictureEditorFrame.XBPFILETYPE.equals(fileType.getFileTypeId())) {
            try {
                XBFileOutputStream output = new XBFileOutputStream(file);
                XBTChildOutputSerialHandler handler = new XBTChildListenerSerialHandler();
                handler.attachXBTEventListener(null);
                XBTEncapsulator encapsulator = new XBTEncapsulator(new StubContext(getStubXBTDataSerializator()));
                encapsulator.attachXBTListener(new XBTEventListenerToListener(new XBTToXBEventConvertor(output)));
                handler.attachXBTEventListener(new XBTListenerToEventListener(encapsulator));
                getStubXBTDataSerializator().serializeToXB(handler);
                output.close();
            } catch (XBProcessingException ex) {
                Logger.getLogger(ImagePanel.class.getName()).log(Level.SEVERE, null, ex);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(ImagePanel.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(ImagePanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                if (fileType instanceof PictureFileType) {
                    ext = ((PictureFileType) fileType).getExt();
                }
                if (ext == null) {
                    ext = "PNG";
                }
                ImageIO.write((RenderedImage) image, ext, file);
            } catch (IOException ex) {
                Logger.getLogger(ImagePanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void newFile() {
        image = toBufferedImage(createImage(100, 100));
        scaledImage = image;
        imageArea.setIcon(new ImageIcon(image));
        grph = image.getGraphics();
        grph.setColor(Color.WHITE);
        grph.fillRect(0, 0, imageArea.getIcon().getIconWidth(), imageArea.getIcon().getIconHeight());
        grph.setColor(toolColor);
        setModified(false);
    }

    /**
     * @return the fileName
     */
    @Override
    public String getFileName() {
        return fileName;
    }

    /**
     * @param fileName the fileName to set
     */
    @Override
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public UndoManager getUndo() {
        return undo;
    }

    public void setPopupMenu(JPopupMenu menu) {
        imageArea.setComponentPopupMenu(menu);
    }

    /**
     * @return the defaultFont
     */
    public Font getDefaultFont() {
        return defaultFont;
    }

    public void scale(double ratio) {
        scaleRatio = ratio;
        int width = (int) (image.getWidth(null) * ratio);
        int height = (int) (image.getHeight(null) * ratio);
        scaledImage = image.getScaledInstance(width, height, Image.SCALE_DEFAULT);
        ((ImageIcon) imageArea.getIcon()).setImage(scaledImage);
        imageArea.setSize(width, height);
        imageArea.repaint();
    }

    /**
     * @return the ext
     */
    public String getExt() {
        return ext;
    }

    /**
     * @param ext the ext to set
     */
    public void setExt(String ext) {
        this.ext = ext;
    }

    // This method returns a buffered image with the contents of an image
    // From: http://www.exampledepot.com/egs/java.awt.image/Image2Buf.html
    public static BufferedImage toBufferedImage(Image image) {
        if (image instanceof BufferedImage) {
            return (BufferedImage) image;
        }

        // This code ensures that all the pixels in the image are loaded
        image = new ImageIcon(image).getImage();

        // Determine if the image has transparent pixels; for this method's
        // implementation, see e661 Determining If an Image Has Transparent Pixels
        boolean hasAlpha = hasAlpha(image);

        // Create a buffered image with a format that's compatible with the screen
        BufferedImage bimage = null;
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        try {
            // Determine the type of transparency of the new buffered image
            int transparency = Transparency.OPAQUE;
            if (hasAlpha) {
                transparency = Transparency.BITMASK;
            }

            // Create the buffered image
            GraphicsDevice gs = ge.getDefaultScreenDevice();
            GraphicsConfiguration gc = gs.getDefaultConfiguration();
            int width = image.getWidth(null);
            int height = image.getHeight(null);
            if ((width == -1) || (height == -1)) {
                // Something gone wrong

            }
            bimage = gc.createCompatibleImage(width, height, transparency);
        } catch (HeadlessException e) {
            // The system does not have a screen
        }

        if (bimage == null) {
            // Create a buffered image using the default color model
            int type = BufferedImage.TYPE_INT_RGB;
            if (hasAlpha) {
                type = BufferedImage.TYPE_INT_ARGB;
            }
            bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
        }

        // Copy image to buffered image
        Graphics g = bimage.createGraphics();

        // Paint the image onto the buffered image
        g.drawImage(image, 0, 0, null);
        g.dispose();

        return bimage;
    }

    public static boolean hasAlpha(Image image) {
        // If buffered image, the color model is readily available
        if (image instanceof BufferedImage) {
            BufferedImage bimage = (BufferedImage) image;
            return bimage.getColorModel().hasAlpha();
        }

        // Use a pixel grabber to retrieve the image's color model;
        // grabbing a single pixel is usually sufficient
        PixelGrabber pg = new PixelGrabber(image, 0, 0, 1, 1, false);
        try {
            pg.grabPixels();
        } catch (InterruptedException e) {
        }

        // Get the image's color model
        ColorModel cm = pg.getColorModel();
        if (cm == null) {
            return false;
        }
        return cm.hasAlpha();
    }

    @Override
    public Point getMousePosition() {
        return imageArea.getMousePosition();
    }

    public void attachCaretListener(MouseMotionListener listener) {
        imageArea.addMouseMotionListener(listener);
    }

    public void setToolColor(Color toolColor) {
        this.toolColor = toolColor;
        if (grph != null) {
            grph.setColor(toolColor);
        }
    }

    public void showResizeDialog(ImageResizeDialog dlg) {
        dlg.setResolution(new Point(image.getWidth(null), image.getHeight(null)));
        dlg.setVisible(true);
        if (dlg.getOption() == JOptionPane.OK_OPTION) {
            Point point = dlg.getResolution();
            int width = (int) (point.getX());
            int height = (int) (point.getY());
            image = toBufferedImage(image.getScaledInstance(width, height, Image.SCALE_DEFAULT));
            grph = image.getGraphics();
            grph.setColor(toolColor);
            scale(scaleRatio);
//            scaledImage.flush();
//            imageArea.repaint();
/*            ((ImageIcon) imageArea.getIcon()).setImage(scaledImage);
             imageArea.setSize(width, height);
             imageArea.repaint(); */
//            textArea.setForeground(dlg.getMyColor());
        }
    }

    public XBTChildSerializable getXBTDataSerializator() {
        return new XBTChildSerializable() {
            @Override
            public void serializeFromXB(XBTChildInputSerialHandler serial) throws XBProcessingException, IOException {
                XBBufferedImage srcImage = new XBBufferedImage();
                srcImage.serializeFromXB(serial);
                image = toBufferedImage(srcImage.getImage());
                scaledImage = image;
                grph = image.getGraphics();
                grph.setColor(toolColor);
                imageArea.setIcon(new ImageIcon(image));
                scale(scaleRatio);
            }

            @Override
            public void serializeToXB(XBTChildOutputSerialHandler serial) throws XBProcessingException, IOException {
                XBBufferedImage bufferedImage = new XBBufferedImage(toBufferedImage(image));
                bufferedImage.serializeToXB(serial);
            }
        };
    }

    // TODO: This is ugly stub for loaging files skipping definition
    public XBTChildSerializable getStubXBTDataSerializator() {
        return new XBTChildSerializable() {
            @Override
            public void serializeFromXB(XBTChildInputSerialHandler serial) throws XBProcessingException, IOException {
                serial.getType();
                serial.nextAttribute();
                serial.nextAttribute();
                serial.nextAttribute();
                serial.nextChild(new XBTChildSerializable() {
                    @Override
                    public void serializeFromXB(XBTChildInputSerialHandler serial) throws XBProcessingException, IOException {
                        serial.getType();
                        serial.nextAttribute();
                        serial.nextAttribute();
                        serial.nextAttribute();
                        serial.nextAttribute();
                        serial.nextAttribute();
                        serial.nextAttribute();
                        serial.end();
                    }

                    @Override
                    public void serializeToXB(XBTChildOutputSerialHandler serializationHandler) throws XBProcessingException, IOException {
                        throw new IllegalStateException();
                    }
                });

                serial.nextChild(new XBTChildSerializable() {
                    @Override
                    public void serializeFromXB(XBTChildInputSerialHandler serializationHandler) throws XBProcessingException, IOException {
                        XBBufferedImage srcImage = new XBBufferedImage();
                        srcImage.serializeFromXB(serializationHandler);
                        image = toBufferedImage(srcImage.getImage());
                        scaledImage = image;
                        grph = image.getGraphics();
                        grph.setColor(toolColor);
                        imageArea.setIcon(new ImageIcon(image));
                        scale(scaleRatio);
                    }

                    @Override
                    public void serializeToXB(XBTChildOutputSerialHandler serial) throws XBProcessingException, IOException {
                        throw new IllegalStateException();
                    }
                });

                serial.end();
            }

            @Override
            public void serializeToXB(XBTChildOutputSerialHandler serial) throws XBProcessingException, IOException {
                XBBufferedImage bufferedImage = new XBBufferedImage(toBufferedImage(image));
                bufferedImage.serializeToXB(serial);
            }
        };
    }

    @Override
    public String getPanelName() {
        return "Image Panel";
    }

    @Override
    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }

    @Override
    public void setPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        this.propertyChangeListener = propertyChangeListener;
    }

    @Override
    public String getWindowTitle(String frameTitle) {
        if (!"".equals(fileName)) {
            int pos;
            int newpos = 0;
            do {
                pos = newpos;
                newpos = fileName.indexOf(File.separatorChar, pos) + 1;
            } while (newpos > 0);
            return fileName.substring(pos) + " - " + frameTitle;
        }

        return frameTitle;
    }

    private static class StubContext extends XBContext {

        private final XBSerializable source;

        public StubContext(XBSerializable source) {
            this.source = source;
        }

        @Override
        public XBDeclaration getDeclaration() {
            XBDeclaration decl = new StubDeclaration();
            decl.setGroupsReserved(3);
            decl.setRootNode(source);

            return decl;
        }

        @Override
        public XBBlockType getBlockType(XBBlockDecl type) {
            return new XBBlockType() {

                @Override
                public UBNatural getGroupID() {
                    return new UBNat32(1);
                }

                @Override
                public UBNatural getBlockID() {
                    return new UBNat32(0);
                }
            };
        }
    }

    private static class StubDeclaration extends XBDeclaration {

        public StubDeclaration() {
        }

        @Override
        public XBFormatDecl getFormat() {
            long[] path = {1, 4, 0, 1};
            return new StubXBFormatDecl(path);
        }
    }

    private static class StubXBFormatDecl extends XBFormatDecl implements XBTSequenceSerializable {

        public StubXBFormatDecl(long[] path) {
            super(path);
        }

        @Override
        public void serializeXB(XBTSequenceSerialHandler serializationHandler) throws XBProcessingException, IOException {
            XBSerialSequence seq = new XBSerialSequence(new XBFixedBlockType(XBBasicBlockType.FORMAT_DECLARATION));
            seq.join(new UBNat32(2));
            seq.join(getCatalogPath());

            serializationHandler.sequenceXB(seq);
        }
    }
}
