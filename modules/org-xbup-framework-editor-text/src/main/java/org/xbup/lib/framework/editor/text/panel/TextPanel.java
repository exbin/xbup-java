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
package org.xbup.lib.framework.editor.text.panel;

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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.event.ChangeEvent;
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
import org.xbup.lib.core.block.declaration.XBDeclaration;
import org.xbup.lib.core.block.declaration.local.XBLFormatDecl;
import org.xbup.lib.core.catalog.XBPCatalog;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.basic.convert.XBTTypeUndeclaringFilter;
import org.xbup.lib.core.parser.token.event.XBEventWriter;
import org.xbup.lib.core.parser.token.event.convert.XBTEventListenerToListener;
import org.xbup.lib.core.parser.token.event.convert.XBTListenerToEventListener;
import org.xbup.lib.core.parser.token.event.convert.XBTToXBEventConvertor;
import org.xbup.lib.core.parser.token.pull.XBPullReader;
import org.xbup.lib.core.parser.token.pull.convert.XBTPullTypeDeclaringFilter;
import org.xbup.lib.core.parser.token.pull.convert.XBToXBTPullConvertor;
import org.xbup.lib.core.serial.XBPSerialReader;
import org.xbup.lib.core.serial.XBPSerialWriter;
import org.xbup.lib.core.type.XBEncodingText;
import org.xbup.lib.framework.editor.text.EditorTextModule;
import org.xbup.lib.framework.editor.text.TextStatusApi;
import org.xbup.lib.framework.gui.editor.api.XBEditorProvider;
import org.xbup.lib.framework.gui.file.api.FileType;
import org.xbup.lib.framework.editor.text.dialog.FindTextDialog;
import org.xbup.lib.framework.editor.text.dialog.FontDialog;

/**
 * Text editor panel.
 *
 * @version 0.2.0 2016/01/23
 * @author XBUP Project (http://xbup.org)
 */
public class TextPanel extends javax.swing.JPanel implements XBEditorProvider {

    private final TextPanelCompoundUndoManager undoManagement = new TextPanelCompoundUndoManager();
    private String fileName;
    private FileType fileType;
    private boolean modified = false;
    private Object highlight;
    private Color foundTextBackgroundColor;
    private Charset charset;
    private Font defaultFont;
    private Color[] defaultColors;
    private PropertyChangeListener propertyChangeListener;
    private CharsetChangeListener charsetChangeListener = null;
    private TextStatusApi textStatus = null;

    public TextPanel() {
        initComponents();
        init();
    }

    private void init() {
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

        // Listener for undoManagement and redo events
        textArea.getDocument().addUndoableEditListener(new UndoableEditListener() {
            @Override
            public void undoableEditHappened(UndoableEditEvent evt) {
                undoManagement.undoableEditHappened(evt);

//                if (mainFrameManagement != null) {
//                    mainFrameManagement.refreshUndo();
//                }
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

    public void findText(FindTextDialog dialog) {
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

    public void setNoBorder() {
        textAreaScrollPane.setBorder(null);
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
        if (dlg.getDialogOption() == JOptionPane.OK_OPTION) {
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

    @Override
    public boolean isModified() {
        return modified;
    }

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
        switch (fileType.getFileTypeId()) {
            case EditorTextModule.XBT_FILE_TYPE: {
                try {
                    XBPCatalog catalog = new XBPCatalog();
                    catalog.addFormatDecl(getContextFormatDecl());
                    XBLFormatDecl formatDecl = new XBLFormatDecl(XBEncodingText.XBUP_FORMATREV_CATALOGPATH);
                    XBEncodingText encodingText = new XBEncodingText();
                    XBDeclaration declaration = new XBDeclaration(formatDecl, encodingText);
                    XBTPullTypeDeclaringFilter typeProcessing = new XBTPullTypeDeclaringFilter(catalog);
                    typeProcessing.attachXBTPullProvider(new XBToXBTPullConvertor(new XBPullReader(new FileInputStream(getFileName()))));
                    XBPSerialReader reader = new XBPSerialReader(typeProcessing);
                    reader.read(declaration);
                    changeCharset(encodingText.getCharset());
                    textArea.setText(encodingText.getValue());
                } catch (XBProcessingException | IOException ex) {
                    Logger.getLogger(TextPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            }
            case EditorTextModule.TXT_FILE_TYPE: {
                try {
                    FileInputStream fileStream = new FileInputStream(file);
                    int gotChars;
                    char[] buffer = new char[32];
                    StringBuilder data = new StringBuilder();
                    BufferedReader rdr = new BufferedReader(new InputStreamReader(fileStream, charset));
                    while ((gotChars = rdr.read(buffer)) != -1) {
                        data.append(buffer, 0, gotChars);
                    }
                    textArea.setText(data.toString());
                } catch (IOException ex) {
                    Logger.getLogger(TextPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            }
        }

        setModified(false);
    }

    @Override
    public void saveToFile() {
        File file = new File(getFileName());
        switch (fileType.getFileTypeId()) {
            case EditorTextModule.XBT_FILE_TYPE: {
                try {
                    XBEncodingText encodingString = new XBEncodingText();
                    encodingString.setValue(textArea.getText());
                    encodingString.setCharset(charset);

                    try (FileOutputStream output = new FileOutputStream(file)) {
                        XBPCatalog catalog = new XBPCatalog();
                        catalog.addFormatDecl(getContextFormatDecl());
                        XBLFormatDecl formatDecl = new XBLFormatDecl(XBEncodingText.XBUP_FORMATREV_CATALOGPATH);
                        XBDeclaration declaration = new XBDeclaration(formatDecl, encodingString);
                        declaration.realignReservation(catalog);
                        XBTTypeUndeclaringFilter typeProcessing = new XBTTypeUndeclaringFilter(catalog);
                        typeProcessing.attachXBTListener(new XBTEventListenerToListener(new XBTToXBEventConvertor(new XBEventWriter(output))));
                        XBPSerialWriter writer = new XBPSerialWriter(new XBTListenerToEventListener(typeProcessing));
                        writer.write(declaration);
                    }
                } catch (XBProcessingException | IOException ex) {
                    Logger.getLogger(TextPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            }
            default: // TODO detect extension
            case EditorTextModule.TXT_FILE_TYPE: {
                try {
                    try (FileOutputStream output = new FileOutputStream(file)) {
                        String text = textArea.getText();
                        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output, charset))) {
                            int fileLength = text.length();
                            int offset = 0;
                            while (offset < fileLength) {
                                int length = Math.min(1024, fileLength - offset);
                                writer.write(text, offset, length);
                                offset += length;
                            }
                        }
                    }
                } catch (IOException ex) {
                    Logger.getLogger(TextPanel.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            }
        }

        setModified(false);
    }

    /**
     * Returns local format declaration when catalog or service is not
     * available.
     *
     * @return local format declaration
     */
    public XBLFormatDecl getContextFormatDecl() {
        /*XBLFormatDef formatDef = new XBLFormatDef();
         List<XBFormatParam> groups = formatDef.getFormatParams();
         XBLGroupDecl stringGroup = new XBLGroupDecl(new XBLGroupDef());
         List<XBGroupParam> stringBlocks = stringGroup.getGroupDef().getGroupParams();
         stringBlocks.add(new XBGroupParamConsist(new XBLBlockDecl(new long[]{1, 3, 1, 2, 0, 0})));
         stringBlocks.add(new XBGroupParamConsist(new XBLBlockDecl(new long[]{1, 3, 1, 1, 1, 0})));
         stringBlocks.add(new XBGroupParamConsist(new XBLBlockDecl(new long[]{1, 3, 1, 2, 2, 0})));
         stringBlocks.add(new XBGroupParamConsist(new XBLBlockDecl(new long[]{1, 3, 1, 2, 3, 0})));
         stringBlocks.add(new XBGroupParamConsist(new XBLBlockDecl(new long[]{1, 3, 1, 2, 4, 0})));
         ((XBLGroupDef) stringGroup.getGroupDef()).provideRevision();
         groups.add(new XBFormatParamConsist(stringGroup));
         formatDef.realignRevision();

         XBLFormatDecl formatDecl = new XBLFormatDecl(formatDef);
         formatDecl.setCatalogPath(XBEncodingText.XBUP_FORMATREV_CATALOGPATH);
         return formatDecl;*/

        XBPSerialReader reader = new XBPSerialReader(ClassLoader.class.getResourceAsStream("/org/xbup/tool/editor/module/text_editor/resources/xbt_format_decl.xb"));
        XBLFormatDecl formatDecl = new XBLFormatDecl();
        try {
            reader.read(formatDecl);
        } catch (XBProcessingException | IOException ex) {
            return null;
        }
        return formatDecl;
    }

    @Override
    public void newFile() {
        textArea.setText("");
        setModified(false);
    }

    @Override
    public String getFileName() {
        return fileName;
    }

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

    public Charset getCharset() {
        return charset;
    }

    public void setCharset(Charset charset) {
        this.charset = charset;
    }

    public Font getDefaultFont() {
        return defaultFont;
    }

    public void setText(String text) {
        textArea.setText(text);
    }

    @Override
    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }

    public Boolean canUndo() {
        return getUndo().canUndo();
    }

    public Boolean canRedo() {
        return getUndo().canRedo();
    }

    @Override
    public void setPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        this.propertyChangeListener = propertyChangeListener;
    }

    public void performUndo() {
        getUndo().undo();
    }

    public void performRedo() {
        getUndo().redo();
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

    public void setCharsetChangeListener(CharsetChangeListener charsetChangeListener) {
        this.charsetChangeListener = charsetChangeListener;
    }

    private void changeCharset(Charset charset) {
        this.charset = charset;
        if (charsetChangeListener != null) {
            charsetChangeListener.charsetChanged();
        }
    }

    @Override
    public JPanel getPanel() {
        return this;
    }

    public void registerTextStatus(TextStatusApi textStatusApi) {
        this.textStatus = textStatusApi;
        attachCaretListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                Point pos = getCaretPosition();
                String textPosition = Long.toString((long) pos.getX()) + ":" + Long.toString((long) pos.getY());
                textStatus.setTextPosition(textPosition);
            }
        });
        setCharsetChangeListener(new TextPanel.CharsetChangeListener() {

            @Override
            public void charsetChanged() {
                textStatus.setEncoding(getCharset().name());
            }
        });
    }

    public interface CharsetChangeListener {

        public void charsetChanged();
    }
}
