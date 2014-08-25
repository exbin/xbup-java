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
package org.xbup.tool.editor.module.text_editor.panel;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.SystemColor;
import java.awt.event.KeyEvent;
import java.awt.print.PrinterException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Document;
import javax.swing.text.Highlighter.Highlight;
import javax.swing.undo.UndoableEdit;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.serial.child.XBChildInputSerialHandler;
import org.xbup.lib.core.serial.child.XBChildListenerSerialHandler;
import org.xbup.lib.core.serial.child.XBChildOutputSerialHandler;
import org.xbup.lib.core.serial.child.XBChildProviderSerialHandler;
import org.xbup.lib.core.serial.child.XBChildSerializable;
import org.xbup.lib.core.stream.file.XBFileInputStream;
import org.xbup.lib.core.stream.file.XBFileOutputStream;
import org.xbup.lib.core.ubnumber.type.UBNat32;
import org.xbup.tool.editor.module.text_editor.XBTextEditorFrame;
import org.xbup.tool.editor.module.text_editor.dialog.FindDialog;
import org.xbup.tool.editor.module.text_editor.dialog.FontDialog;
import org.xbup.tool.editor.base.api.ActivePanelUndoable;
import org.xbup.tool.editor.base.api.ApplicationFilePanel;
import org.xbup.tool.editor.base.api.FileType;
import org.xbup.tool.editor.base.api.MainFrameManagement;

/**
 * Text editor panel.
 *
 * @version 0.1 wr23.0 2014/03/06
 * @author XBUP Project (http://xbup.org)
 */
public class TextPanel extends javax.swing.JPanel implements ApplicationFilePanel, ActivePanelUndoable {

    final TextPanelCompoundUndoManager undoManagement;
    private String fileName;
    private FileType fileType;
    private boolean modified = false;
    private Object highlight;
    private Color foundTextBackgroundColor;
    private Charset charset;
    private Font defaultFont;
    private Color[] defaultColors;
    private PropertyChangeListener propertyChangeListener;
    private MainFrameManagement mainFrameManagement;

    /**
     * Creates new form TextPanel
     */
    public TextPanel() {
        initComponents();

        undoManagement = new TextPanelCompoundUndoManager();
        fileName = "";
        highlight = null;
        foundTextBackgroundColor = Color.YELLOW;
        charset = Charset.forName(TextEncodingPanel.ENCODING_UTF8);
        defaultFont = textArea.getFont();
        defaultColors = new Color[5];
        defaultColors[0] = new Color(textArea.getForeground().getRGB());
        defaultColors[1] = new Color(SystemColor.text.getRGB()); // Patch on wrong value in textArea.getBackground()
        defaultColors[2] = new Color(textArea.getSelectedTextColor().getRGB());
        defaultColors[3] = new Color(textArea.getSelectionColor().getRGB());
        defaultColors[4] = foundTextBackgroundColor;

        // if the document is ever edited, assume that it needs to be saved
        textArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void changedUpdate(DocumentEvent e) {
                setModified(true);
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                setModified(true);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                setModified(true);
            }
        });

        // Listen for undoManagement and redo events
        textArea.getDocument().addUndoableEditListener(new UndoableEditListener() {
            @Override
            public void undoableEditHappened(UndoableEditEvent evt) {
                undoManagement.undoableEditHappened(evt);

                if (mainFrameManagement != null) {
                    mainFrameManagement.refreshUndo();
                }
            }
        });

        addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (propertyChangeListener != null) {
                    propertyChangeListener.propertyChange(evt);
                }
            }
        });
    }

    public boolean changeLineWrap() {
        textArea.setLineWrap(!textArea.getLineWrap());
        return textArea.getLineWrap();
    }

    public void findText(FindDialog dialog) {
        String text = textArea.getText();
        int pos = textArea.getCaretPosition();
        if (highlight != null) {
            if (((Highlight) highlight).getStartOffset() == pos) {
                pos++;
            }
            textArea.getHighlighter().removeHighlight(highlight);
        } else if (dialog.getSearchFromStart()) {
            pos = 0;
        }
        String findText = dialog.getFindText();
        pos = text.indexOf(findText, pos);
        if (pos >= 0) {
            try {
                int toPos;
                if (dialog.getShallReplace()) {
                    String replaceText = dialog.getReplaceText();
                    textArea.replaceRange(replaceText, pos, pos + findText.length());
                    toPos = pos + replaceText.length();
                } else {
                    toPos = pos + findText.length();
                }
                highlight = textArea.getHighlighter().addHighlight(pos, toPos, new DefaultHighlighter.DefaultHighlightPainter(foundTextBackgroundColor));
                textArea.setCaretPosition(pos);
                return;
            } catch (BadLocationException ex) {
                Logger.getLogger(TextPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        JOptionPane.showMessageDialog(null, "String was not found", "Find text", JOptionPane.INFORMATION_MESSAGE); // getFrame
        highlight = null;
    }

    public Color[] getCurrentColors() {
        Color[] colors = new Color[5];
        colors[0] = textArea.getForeground();
        colors[1] = textArea.getBackground();
        colors[2] = textArea.getSelectedTextColor();
        colors[3] = textArea.getSelectionColor();
        colors[4] = getFoundTextBackgroundColor();
        return colors;
    }

    public Color[] getDefaultColors() {
        return defaultColors;
    }

    public void setCurrentColors(Color[] colors) {
        if (colors[0] != null) {
            textArea.setForeground(colors[0]);
        }
        if (colors[1] != null) {
            textArea.setBackground(colors[1]);
        }
        if (colors[2] != null) {
            textArea.setSelectedTextColor(colors[2]);
        }
        if (colors[3] != null) {
            textArea.setSelectionColor(colors[3]);
        }
        if (colors[4] != null) {
            setFoundTextBackgroundColor(colors[4]);
        }
    }

    public Document getDocument() {
        return textArea.getDocument();
    }

    public int getLineCount() {
        return textArea.getLineCount();
    }

    public String getText() {
        return textArea.getText();
    }

    public void gotoLine(int line) {
        try {
            textArea.setCaretPosition(textArea.getLineStartOffset(line - 1));
        } catch (BadLocationException ex) {
            Logger.getLogger(TextPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void gotoRelative(int charPos) {
        textArea.setCaretPosition(textArea.getCaretPosition() + charPos - 1);
    }

    public void performCopy() {
        textArea.copy();
    }

    public void performCut() {
        textArea.cut();
    }

    public void performDelete() {
        textArea.getInputContext().dispatchEvent(new KeyEvent(this, KeyEvent.KEY_PRESSED, 0, 0, KeyEvent.VK_DELETE, KeyEvent.CHAR_UNDEFINED));
    }

    public void performPaste() {
        textArea.paste();
    }

    public void performSelectAll() {
        textArea.selectAll();
    }

    public void printFile() {
        try {
            textArea.print();
        } catch (PrinterException ex) {
            Logger.getLogger(TextPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setCurrentFont(Font font) {
        textArea.setFont(font);
    }

    public Font getCurrentFont() {
        return textArea.getFont();
    }

    public void showFontDialog(FontDialog dlg) {
        dlg.setStoredFont(textArea.getFont());
        dlg.setVisible(true);
        if (dlg.getOption() == JOptionPane.OK_OPTION) {
            textArea.setFont(dlg.getStoredFont());
        }
    }

    public Color getFoundTextBackgroundColor() {
        return foundTextBackgroundColor;
    }

    public void setFoundTextBackgroundColor(Color color) {
        foundTextBackgroundColor = color;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        textAreaScrollPane = new javax.swing.JScrollPane();
        textArea = new javax.swing.JTextArea();

        setInheritsPopupMenu(true);
        setName("Form"); // NOI18N
        setLayout(new java.awt.BorderLayout());

        textAreaScrollPane.setName("textAreaScrollPane"); // NOI18N

        textArea.setColumns(20);
        textArea.setRows(5);
        textArea.setName("textArea"); // NOI18N
        textAreaScrollPane.setViewportView(textArea);

        add(textAreaScrollPane, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextArea textArea;
    private javax.swing.JScrollPane textAreaScrollPane;
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
        if (highlight != null) {
            textArea.getHighlighter().removeHighlight(highlight);
            highlight = null;
        }
        boolean oldValue = this.modified;
        this.modified = modified;
        firePropertyChange("modified", oldValue, this.modified);
    }

    @Override
    public void loadFromFile() {
        File file = new File(getFileName());
        try {
            XBFileInputStream fileStream = new XBFileInputStream(file);
            if (XBTextEditorFrame.XBTFILETYPE.equals(fileType.getFileTypeId())) { // Process XB header
                XBChildInputSerialHandler handler = new XBChildProviderSerialHandler();
                handler.attachXBPullProvider(fileStream);
                new XBL0TextPanelSerializable().serializeFromXB(handler);
            }
            setModified(false);
        } catch (XBProcessingException ex) {
            Logger.getLogger(TextPanel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(TextPanel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TextPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void saveToFile() {
        File file = new File(getFileName());
        try {
            XBFileOutputStream fileStream = new XBFileOutputStream(file);
            if (XBTextEditorFrame.XBTFILETYPE.equals(fileType.getFileTypeId())) { // Process XB header
                XBChildListenerSerialHandler handler = new XBChildListenerSerialHandler();
                handler.attachXBEventListener(fileStream);
                new XBL0TextPanelSerializable().serializeToXB(handler);
            }
            setModified(false);
        } catch (XBProcessingException ex) {
            Logger.getLogger(TextPanel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(TextPanel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(TextPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void newFile() {
        textArea.setText("");
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

    public UndoableEdit getUndo() {
        return undoManagement;
    }

    public void setPopupMenu(JPopupMenu menu) {
        textArea.setComponentPopupMenu(menu);
    }

    public Point getCaretPosition() {
        int line;
        int caretPosition = textArea.getCaretPosition();
        javax.swing.text.Element root = textArea.getDocument().getDefaultRootElement();
        line = root.getElementIndex(caretPosition);
        try {
            return new Point(caretPosition - textArea.getLineStartOffset(line) + 1, line + 1);
        } catch (BadLocationException ex) {
            Logger.getLogger(TextPanel.class.getName()).log(Level.SEVERE, null, ex);
            return new Point(0, 0);
        }
    }

    public void attachCaretListener(ChangeListener listener) {
        textArea.getCaret().addChangeListener(listener);
    }

    /**
     * @return the charset
     */
    public Charset getCharset() {
        return charset;
    }

    /**
     * @param charset the charset to set
     */
    public void setCharset(Charset charset) {
        this.charset = charset;
    }

    /**
     * @return the defaultFont
     */
    public Font getDefaultFont() {
        return defaultFont;
    }

    public void setText(String text) {
        textArea.setText(text);
    }

    @Override
    public String getPanelName() {
        return "Text Panel";
    }

    @Override
    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }

    @Override
    public Boolean canUndo() {
        return getUndo().canUndo();
    }

    @Override
    public Boolean canRedo() {
        return getUndo().canRedo();
    }

    @Override
    public void setPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        this.propertyChangeListener = propertyChangeListener;
    }

    @Override
    public void performUndo() {
        getUndo().undo();
    }

    @Override
    public void performRedo() {
        getUndo().redo();
    }

    public void setMainFrameManagement(MainFrameManagement mainFrameManagement) {
        this.mainFrameManagement = mainFrameManagement;
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

    private class XBL0TextPanelSerializable implements XBChildSerializable {

        @Override
        public void serializeFromXB(XBChildInputSerialHandler serial) throws XBProcessingException, IOException {
            // TODO: Check it later
            serial.nextAttribute();
            serial.nextAttribute();
            serial.nextAttribute();
            serial.nextAttribute();
            serial.nextChild(new XBL0TextPanelSerializableH1());
            serial.nextChild(new XBL0TextPanelSerializableEnc());

            InputStream data = serial.nextData();
            int gotChars;
            char[] buffer = new char[32];
            StringBuilder dataBuilder = new StringBuilder();
            BufferedReader rdr = new BufferedReader(new InputStreamReader(data, charset));
            while ((gotChars = rdr.read(buffer)) != -1) {
                dataBuilder.append(buffer, 0, gotChars);
            }
            textArea.setText(dataBuilder.toString());

            serial.end();
        }

        @Override
        public void serializeToXB(XBChildOutputSerialHandler serial) throws XBProcessingException, IOException {
            serial.addAttribute(new UBNat32(0));
            serial.addAttribute(new UBNat32(1));
            serial.addAttribute(new UBNat32(1));
            serial.addAttribute(new UBNat32(0));
            serial.addChild(new XBL0TextPanelSerializableH1());
            serial.addChild(new XBL0TextPanelSerializableEnc());

            // TODO optimalize
            ByteArrayOutputStream data = new ByteArrayOutputStream();
            String text = textArea.getText();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(data, charset));
            int fileLength = text.length();
            int offset = 0;
            while (offset < fileLength) {
                int length = Math.min(1024, fileLength - offset);
                writer.write(text, offset, length);
                offset += length;
            }
            writer.close();

            serial.addData(new ByteArrayInputStream(data.toByteArray()));
            serial.end();
        }
    }

    private class XBL0TextPanelSerializableH1 implements XBChildSerializable {

        @Override
        public void serializeFromXB(XBChildInputSerialHandler serial) throws XBProcessingException, IOException {
            if (serial.nextAttribute().getInt() != 0) {
                throw new XBProcessingException("Unexpected attribute value");
            }
            if (serial.nextAttribute().getInt() != 2) {
                throw new XBProcessingException("Unexpected attribute value");
            }
            if (serial.nextAttribute().getInt() != 2) {
                throw new XBProcessingException("Unexpected attribute value");
            }

            if (serial.nextAttribute().getInt() != 5) {
                throw new XBProcessingException("Unexpected attribute value");
            }
            if (serial.nextAttribute().getInt() != 1) {
                throw new XBProcessingException("Unexpected attribute value");
            }
            if (serial.nextAttribute().getInt() != 3) {
                throw new XBProcessingException("Unexpected attribute value");
            }
            if (serial.nextAttribute().getInt() != 1) {
                throw new XBProcessingException("Unexpected attribute value");
            }
            if (serial.nextAttribute().getInt() != 2) {
                throw new XBProcessingException("Unexpected attribute value");
            }
            if (serial.nextAttribute().getInt() != 0) {
                throw new XBProcessingException("Unexpected attribute value");
            }

            if (serial.nextAttribute().getInt() != 0) {
                throw new XBProcessingException("Unexpected attribute value");
            }
            serial.end();
        }

        @Override
        public void serializeToXB(XBChildOutputSerialHandler serial) throws XBProcessingException, IOException {
            serial.addAttribute(new UBNat32(0));
            serial.addAttribute(new UBNat32(2));
            serial.addAttribute(new UBNat32(2));

            serial.addAttribute(new UBNat32(5));
            serial.addAttribute(new UBNat32(1));
            serial.addAttribute(new UBNat32(3));
            serial.addAttribute(new UBNat32(1));
            serial.addAttribute(new UBNat32(2));
            serial.addAttribute(new UBNat32(0));

            serial.addAttribute(new UBNat32(0));
            serial.end();
        }
    }

    private class XBL0TextPanelSerializableEnc implements XBChildSerializable {

        @Override
        public void serializeFromXB(XBChildInputSerialHandler serial) throws XBProcessingException, IOException {
            serial.nextAttribute();
            serial.nextAttribute();
            serial.nextAttribute();
            serial.end();
        }

        @Override
        public void serializeToXB(XBChildOutputSerialHandler serial) throws XBProcessingException, IOException {
            serial.addAttribute(new UBNat32(1));
            serial.addAttribute(new UBNat32(0));
            serial.addAttribute(new UBNat32(2));
            serial.end();
        }
    }
}
