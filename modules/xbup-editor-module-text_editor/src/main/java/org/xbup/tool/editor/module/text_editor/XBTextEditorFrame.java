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
package org.xbup.tool.editor.module.text_editor;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javax.swing.ButtonGroup;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSeparator;
import javax.swing.JToolBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;
import org.xbup.tool.editor.module.text_editor.dialog.EncodingDialog;
import org.xbup.tool.editor.module.text_editor.dialog.FindDialog;
import org.xbup.tool.editor.module.text_editor.dialog.FontDialog;
import org.xbup.tool.editor.module.text_editor.dialog.GotoDialog;
import org.xbup.tool.editor.module.text_editor.dialog.PropertiesDialog;
import org.xbup.tool.editor.module.text_editor.dialog.TextColorDialog;
import org.xbup.tool.editor.module.text_editor.panel.TextAppearancePanelFrame;
import org.xbup.tool.editor.module.text_editor.panel.TextColorPanelFrame;
import org.xbup.tool.editor.module.text_editor.panel.TextEncodingPanel;
import org.xbup.tool.editor.module.text_editor.panel.TextEncodingPanelFrame;
import org.xbup.tool.editor.module.text_editor.panel.TextFontPanelFrame;
import org.xbup.tool.editor.module.text_editor.panel.TextPanel;
import org.xbup.tool.editor.base.api.FileType;
import org.xbup.tool.editor.base.api.MainFrameManagement;

/**
 * XBTEditor Main Frame.
 *
 * @version 0.1.24 2014/10/03
 * @author XBUP Project (http://xbup.org)
 */
public class XBTextEditorFrame extends javax.swing.JFrame implements TextColorPanelFrame, TextFontPanelFrame, TextEncodingPanelFrame, TextAppearancePanelFrame {

    private FindDialog findDialog = null;
    private GotoDialog gotoDialog = null;
    private TextPanel activePanel;
    private List<String> encodings = null;
    private ButtonGroup encodingButtonGroup;
    private ActionListener encodingActionListener;
    private ChangeListener caretChangeListener;
    private ResourceBundle resourceBundle;
    private final String DIALOG_MENU_SUFIX = "...";
    private MainFrameManagement mainFrameManagement;

    public static final String XBT_FILE_TYPE = "XBTextEditor.XBTFileType";
    public static final String TXT_FILE_TYPE = "XBTextEditor.TXTFileType";

    /**
     * Creates new form XBTextEditorFrame
     */
    public XBTextEditorFrame() {
        resourceBundle = ResourceBundle.getBundle("org/xbup/tool/editor/module/text_editor/resources/XBTextEditorFrame");

        activePanel = new TextPanel();

        encodingButtonGroup = new ButtonGroup();
        encodingActionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setDocumentCharset(Charset.forName(((JRadioButtonMenuItem) e.getSource()).getText()));
            }
        };

        initComponents();

        ((CardLayout) statusPanel.getLayout()).show(statusPanel,"default");

        mainPanel.add(activePanel, java.awt.BorderLayout.CENTER);

        // Caret position listener
        caretChangeListener = new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                Point pos = getActivePanel().getCaretPosition();
                documentCursorPositionTextField.setText(Long.toString((long) pos.getX()) +":"+ Long.toString((long) pos.getY()));
            }
        };

        activePanel.attachCaretListener(caretChangeListener);
        activePanel.setPopupMenu(mainPopupMenu);
     }

    /**
     * @return the activePanel
     */
    public TextPanel getActivePanel() {
        return activePanel;
    }

    /**
     * @param mainFrameManagement the mainFrameManagement to set
     */
    public void setMainFrameManagement(MainFrameManagement mainFrameManagement) {
        this.mainFrameManagement = mainFrameManagement;

        activePanel.setMainFrameManagement(mainFrameManagement);
    }

    public JPopupMenu getPopupMenu() {
        return mainPopupMenu;
    }

    @Override
    public Color[] getCurrentTextColors() {
        return activePanel.getCurrentColors();
    }

    @Override
    public Color[] getDefaultTextColors() {
        return activePanel.getDefaultColors();
    }

    @Override
    public void setCurrentTextColors(Color[] arrayFromColors) {
        activePanel.setCurrentColors(arrayFromColors);
    }

    @Override
    public Font getCurrentFont() {
        return activePanel.getCurrentFont();
    }

    @Override
    public Font getDefaultFont() {
        return activePanel.getDefaultFont();
    }

    @Override
    public void setCurrentFont(Font font) {
        activePanel.setCurrentFont(font);
    }

    public JToolBar getToolBar() {
        return toolBar;
    }

    private void encodingsRebuild() {
        String encodingToolTip = resourceBundle.getString("set_encoding") + " ";
        for (int i = toolsEncodingMenu.getItemCount() - 2; i > 1; i--) {
            toolsEncodingMenu.remove(i);
        }
        if (encodings.size() > 0) {
            int selectedEncoding = encodings.indexOf(getSelectedEncoding());
            if (selectedEncoding < 0) {
                setSelectedEncoding(TextEncodingPanel.ENCODING_UTF8);
                utfEncodingRadioButtonMenuItem.setSelected(true);
            }
            toolsEncodingMenu.add(new JSeparator(), 1);
            for (int index = 0; index < encodings.size(); index++) {
                String encoding = encodings.get(index);
                JRadioButtonMenuItem item = new JRadioButtonMenuItem(encoding, false);
                item.addActionListener(encodingActionListener);
                item.setToolTipText(encodingToolTip + encoding);
                mainFrameManagement.initMenuItem(item);
                toolsEncodingMenu.add(item, index + 2);
                encodingButtonGroup.add(item);
                if (index == selectedEncoding) {
                    item.setSelected(true);
                }
            }
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

        mainPopupMenu = new javax.swing.JPopupMenu();
        statusBar = new javax.swing.JPanel();
        javax.swing.JSeparator statusPanelSeparator = new javax.swing.JSeparator();
        statusPanel = new javax.swing.JPanel();
        documentStatusPanel = new javax.swing.JPanel();
        documentCursorPositionTextField = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        documentEncodingTextField = new javax.swing.JTextField();
        mainPanel = new javax.swing.JPanel();
        toolBar = new javax.swing.JToolBar();
        findToolButton = new javax.swing.JButton();
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        filePropertiesMenuItem = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        filePrintMenuItem = new javax.swing.JMenuItem();
        editMenu = new javax.swing.JMenu();
        jSeparator2 = new javax.swing.JSeparator();
        editGotoMenuItem = new javax.swing.JMenuItem();
        jSeparator12 = new javax.swing.JSeparator();
        editFindMenuItem = new javax.swing.JMenuItem();
        editFindAgainMenuItem = new javax.swing.JMenuItem();
        editFindReplaceMenuItem = new javax.swing.JMenuItem();
        viewMenu = new javax.swing.JMenu();
        jSeparator6 = new javax.swing.JSeparator();
        viewWordWrapCheckBoxMenuItem = new javax.swing.JCheckBoxMenuItem();
        optionsMenu = new javax.swing.JMenu();
        optionsFontMenuItem = new javax.swing.JMenuItem();
        optionsColorsMenuItem = new javax.swing.JMenuItem();
        toolsEncodingMenu = new javax.swing.JMenu();
        utfEncodingRadioButtonMenuItem = new javax.swing.JRadioButtonMenuItem();
        jSeparator13 = new javax.swing.JSeparator();
        manageEncodingsMenuItem = new javax.swing.JMenuItem();

        mainPopupMenu.setName("mainPopupMenu"); // NOI18N

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        statusBar.setName("statusBar"); // NOI18N
        statusBar.setLayout(new java.awt.BorderLayout());

        statusPanelSeparator.setName("statusPanelSeparator"); // NOI18N
        statusBar.add(statusPanelSeparator, java.awt.BorderLayout.NORTH);

        statusPanel.setName("statusPanel"); // NOI18N
        statusPanel.setPreferredSize(new java.awt.Dimension(649, 26));
        statusPanel.setRequestFocusEnabled(false);
        statusPanel.setLayout(new java.awt.CardLayout());

        documentStatusPanel.setName("documentStatusPanel"); // NOI18N

        documentCursorPositionTextField.setEditable(false);
        documentCursorPositionTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        documentCursorPositionTextField.setText("1:1"); // NOI18N
        documentCursorPositionTextField.setToolTipText("Current position of cursor CHAR : LINE");
        documentCursorPositionTextField.setName("documentCursorPositionTextField"); // NOI18N

        jLabel1.setText("Encoding");
        jLabel1.setName("jLabel1"); // NOI18N

        documentEncodingTextField.setEditable(false);
        documentEncodingTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        documentEncodingTextField.setText("UTF-8"); // NOI18N
        documentEncodingTextField.setToolTipText("Set load/save encoding");
        documentEncodingTextField.setName("documentEncodingTextField"); // NOI18N

        javax.swing.GroupLayout documentStatusPanelLayout = new javax.swing.GroupLayout(documentStatusPanel);
        documentStatusPanel.setLayout(documentStatusPanelLayout);
        documentStatusPanelLayout.setHorizontalGroup(
            documentStatusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(documentStatusPanelLayout.createSequentialGroup()
                .addComponent(documentCursorPositionTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 372, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(documentEncodingTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        documentStatusPanelLayout.setVerticalGroup(
            documentStatusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(documentStatusPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(documentCursorPositionTextField, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
                .addComponent(documentEncodingTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel1))
        );

        statusPanel.add(documentStatusPanel, "default");

        statusBar.add(statusPanel, java.awt.BorderLayout.SOUTH);

        getContentPane().add(statusBar, java.awt.BorderLayout.EAST);

        mainPanel.setName("mainPanel"); // NOI18N
        mainPanel.setLayout(new java.awt.BorderLayout());

        toolBar.setRollover(true);
        toolBar.setName("toolBar"); // NOI18N

        findToolButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/org/xbup/tool/editor/module/text_editor/resources/images/actions/Find16.gif"))); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/xbup/tool/editor/module/text_editor/resources/XBTextEditorFrame"); // NOI18N
        findToolButton.setText(bundle.getString("XBTextEditorFrame.findToolButton.text")); // NOI18N
        findToolButton.setFocusable(false);
        findToolButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        findToolButton.setName("findToolButton"); // NOI18N
        findToolButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        findToolButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                findToolButtonActionPerformed(evt);
            }
        });
        toolBar.add(findToolButton);

        mainPanel.add(toolBar, java.awt.BorderLayout.CENTER);

        getContentPane().add(mainPanel, java.awt.BorderLayout.PAGE_START);

        menuBar.setName("menuBar"); // NOI18N

        fileMenu.setText("File");
        fileMenu.setName("fileMenu"); // NOI18N

        filePropertiesMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ENTER, java.awt.event.InputEvent.ALT_MASK));
        filePropertiesMenuItem.setText(bundle.getString("actionFileProperties.Action.text")); // NOI18N
        filePropertiesMenuItem.setToolTipText(bundle.getString("actionFileProperties.Action.shortDescription")); // NOI18N
        filePropertiesMenuItem.setName("filePropertiesMenuItem"); // NOI18N
        filePropertiesMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                filePropertiesMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(filePropertiesMenuItem);

        jSeparator1.setName("jSeparator1"); // NOI18N
        fileMenu.add(jSeparator1);

        filePrintMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_MASK));
        filePrintMenuItem.setText(bundle.getString("actionFilePrint.Action.text")); // NOI18N
        filePrintMenuItem.setToolTipText(bundle.getString("actionFilePrint.Action.shortDescription")); // NOI18N
        filePrintMenuItem.setName("filePrintMenuItem"); // NOI18N
        filePrintMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                filePrintMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(filePrintMenuItem);

        menuBar.add(fileMenu);

        editMenu.setText("Edit");
        editMenu.setName("editMenu"); // NOI18N
        editMenu.setRolloverEnabled(true);

        jSeparator2.setName("jSeparator2"); // NOI18N
        editMenu.add(jSeparator2);

        editGotoMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_G, java.awt.event.InputEvent.CTRL_MASK));
        editGotoMenuItem.setText(bundle.getString("actionEditGoto.Action.text")); // NOI18N
        editGotoMenuItem.setToolTipText(bundle.getString("actionEditGoto.Action.shortDescription")); // NOI18N
        editGotoMenuItem.setName("editGotoMenuItem"); // NOI18N
        editGotoMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editGotoMenuItemActionPerformed(evt);
            }
        });
        editMenu.add(editGotoMenuItem);

        jSeparator12.setName("jSeparator12"); // NOI18N
        editMenu.add(jSeparator12);

        editFindMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F, java.awt.event.InputEvent.CTRL_MASK));
        editFindMenuItem.setText(bundle.getString("actionEditFind.Action.text")); // NOI18N
        editFindMenuItem.setToolTipText(bundle.getString("actionEditFind.Action.shortDescription")); // NOI18N
        editFindMenuItem.setName("editFindMenuItem"); // NOI18N
        editFindMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editFindMenuItemActionPerformed(evt);
            }
        });
        editMenu.add(editFindMenuItem);

        editFindAgainMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F3, 0));
        editFindAgainMenuItem.setText(bundle.getString("actionFindAgain.Action.text")); // NOI18N
        editFindAgainMenuItem.setToolTipText(bundle.getString("actionFindAgain.Action.shortDescription")); // NOI18N
        editFindAgainMenuItem.setName("editFindAgainMenuItem"); // NOI18N
        editFindAgainMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editFindAgainMenuItemActionPerformed(evt);
            }
        });
        editMenu.add(editFindAgainMenuItem);

        editFindReplaceMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_H, java.awt.event.InputEvent.CTRL_MASK));
        editFindReplaceMenuItem.setText(bundle.getString("actionEditReplace.Action.text")); // NOI18N
        editFindReplaceMenuItem.setToolTipText(bundle.getString("actionEditReplace.Action.shortDescription")); // NOI18N
        editFindReplaceMenuItem.setName("editFindReplaceMenuItem"); // NOI18N
        editFindReplaceMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editFindReplaceMenuItemActionPerformed(evt);
            }
        });
        editMenu.add(editFindReplaceMenuItem);

        menuBar.add(editMenu);

        viewMenu.setText("View");
        viewMenu.setName("viewMenu"); // NOI18N

        jSeparator6.setName("jSeparator6"); // NOI18N
        viewMenu.add(jSeparator6);

        viewWordWrapCheckBoxMenuItem.setText(bundle.getString("viewWordWrapCheckBoxMenuItem.text")); // NOI18N
        viewWordWrapCheckBoxMenuItem.setToolTipText(bundle.getString("viewWordWrapCheckBoxMenuItem.toolTipText")); // NOI18N
        viewWordWrapCheckBoxMenuItem.setName("viewWordWrapCheckBoxMenuItem"); // NOI18N
        viewWordWrapCheckBoxMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewWordWrapCheckBoxMenuItemActionPerformed(evt);
            }
        });
        viewMenu.add(viewWordWrapCheckBoxMenuItem);

        menuBar.add(viewMenu);

        optionsMenu.setText("Options");
        optionsMenu.setName("optionsMenu"); // NOI18N

        optionsFontMenuItem.setText("Set font");
        optionsFontMenuItem.setToolTipText("Set font of text view");
        optionsFontMenuItem.setName("optionsFontMenuItem"); // NOI18N
        optionsFontMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                optionsFontMenuItemActionPerformed(evt);
            }
        });
        optionsMenu.add(optionsFontMenuItem);

        optionsColorsMenuItem.setText("Set colors");
        optionsColorsMenuItem.setToolTipText("Select text view colors");
        optionsColorsMenuItem.setName("optionsColorsMenuItem"); // NOI18N
        optionsColorsMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                optionsColorsMenuItemActionPerformed(evt);
            }
        });
        optionsMenu.add(optionsColorsMenuItem);

        toolsEncodingMenu.setText("Encoding");
        toolsEncodingMenu.setToolTipText("Set load/save encoding");
        toolsEncodingMenu.setName("toolsEncodingMenu"); // NOI18N

        encodingButtonGroup.add(utfEncodingRadioButtonMenuItem);
        utfEncodingRadioButtonMenuItem.setSelected(true);
        utfEncodingRadioButtonMenuItem.setText("UTF-8 (default)");
        utfEncodingRadioButtonMenuItem.setToolTipText("Set encoding UTF-8");
        utfEncodingRadioButtonMenuItem.setName("utfEncodingRadioButtonMenuItem"); // NOI18N
        utfEncodingRadioButtonMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                utfEncodingRadioButtonMenuItemActionPerformed(evt);
            }
        });
        toolsEncodingMenu.add(utfEncodingRadioButtonMenuItem);

        jSeparator13.setName("jSeparator13"); // NOI18N
        toolsEncodingMenu.add(jSeparator13);

        manageEncodingsMenuItem.setText(bundle.getString("manageEncodingsMenuItem.text")); // NOI18N
        manageEncodingsMenuItem.setToolTipText(bundle.getString("manageEncodingsMenuItem.toolTipText")); // NOI18N
        manageEncodingsMenuItem.setName("manageEncodingsMenuItem"); // NOI18N
        manageEncodingsMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                manageEncodingsMenuItemActionPerformed(evt);
            }
        });
        toolsEncodingMenu.add(manageEncodingsMenuItem);

        optionsMenu.add(toolsEncodingMenu);

        menuBar.add(optionsMenu);

        for (JMenuItem item : Arrays.asList(editFindMenuItem, editFindReplaceMenuItem, editGotoMenuItem, filePrintMenuItem, filePropertiesMenuItem, manageEncodingsMenuItem, optionsColorsMenuItem, optionsFontMenuItem)) {
            item.setText(item.getText()+DIALOG_MENU_SUFIX);
        }

        setJMenuBar(menuBar);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void viewWordWrapCheckBoxMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewWordWrapCheckBoxMenuItemActionPerformed
        viewWordWrapCheckBoxMenuItem.setSelected(getActivePanel().changeLineWrap());
    }//GEN-LAST:event_viewWordWrapCheckBoxMenuItemActionPerformed

    private void optionsFontMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_optionsFontMenuItemActionPerformed
        FontDialog dlg = new FontDialog(getFrame(), true);
        dlg.setIconImage(mainFrameManagement.getFrameIcon());
        dlg.setLocationRelativeTo(dlg.getParent());
        getActivePanel().showFontDialog(dlg);
    }//GEN-LAST:event_optionsFontMenuItemActionPerformed

    private void optionsColorsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_optionsColorsMenuItemActionPerformed
        TextColorDialog dlg = new TextColorDialog(getFrame(), this, true);
        dlg.setIconImage(mainFrameManagement.getFrameIcon());
        dlg.setLocationRelativeTo(dlg.getParent());
        dlg.showDialog();
    }//GEN-LAST:event_optionsColorsMenuItemActionPerformed

    private void utfEncodingRadioButtonMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_utfEncodingRadioButtonMenuItemActionPerformed
        setDocumentCharset(Charset.forName("UTF-8"));
    }//GEN-LAST:event_utfEncodingRadioButtonMenuItemActionPerformed

    private void manageEncodingsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_manageEncodingsMenuItemActionPerformed
        EncodingDialog dlg = new EncodingDialog(getFrame(), this, true);
        dlg.setIconImage(mainFrameManagement.getFrameIcon());
        TextEncodingPanel panel = dlg.getEncodingPanel();
        panel.setEncodingList(new ArrayList<String>(encodings));
        dlg.setLocationRelativeTo(dlg.getParent());
        dlg.setVisible(true);
        if (dlg.getOption() == JOptionPane.OK_OPTION) {
            encodings = panel.getEncodingList();
            encodingsRebuild();
        }
    }//GEN-LAST:event_manageEncodingsMenuItemActionPerformed

    private void editFindMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editFindMenuItemActionPerformed
        initFindDialog();
        findDialog.setShallReplace(false);
        findDialog.setSelected();
        findDialog.setLocationRelativeTo(findDialog.getParent());
        findDialog.setVisible(true);
        if (findDialog.getResultOption() == JOptionPane.OK_OPTION) {
            getActivePanel().findText(findDialog);
        }
    }//GEN-LAST:event_editFindMenuItemActionPerformed

    private void editFindReplaceMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editFindReplaceMenuItemActionPerformed
        initFindDialog();
        findDialog.setShallReplace(true);
        findDialog.setSelected();
        findDialog.setLocationRelativeTo(findDialog.getParent());
        findDialog.setVisible(true);
        if (findDialog.getResultOption() == JOptionPane.OK_OPTION) {
            getActivePanel().findText(findDialog);
        }
    }//GEN-LAST:event_editFindReplaceMenuItemActionPerformed

    private void filePrintMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_filePrintMenuItemActionPerformed
        getActivePanel().printFile();
    }//GEN-LAST:event_filePrintMenuItemActionPerformed

    private void editGotoMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editGotoMenuItemActionPerformed
        initGotoDialog();
        gotoDialog.setMaxLine(getActivePanel().getLineCount());
        gotoDialog.setCharPos(1);
        gotoDialog.setLocationRelativeTo(gotoDialog.getParent());
        gotoDialog.setVisible(true);
        if (gotoDialog.getResultOption() == JOptionPane.OK_OPTION) {
            getActivePanel().gotoLine(gotoDialog.getLine());
            getActivePanel().gotoRelative(gotoDialog.getCharPos());
        }
    }//GEN-LAST:event_editGotoMenuItemActionPerformed

    private void editFindAgainMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editFindAgainMenuItemActionPerformed
        initFindDialog();
        findDialog.setShallReplace(false);
        getActivePanel().findText(findDialog);
    }//GEN-LAST:event_editFindAgainMenuItemActionPerformed

    private void filePropertiesMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_filePropertiesMenuItemActionPerformed
        PropertiesDialog dialog = new PropertiesDialog(getFrame(), true);
        dialog.setIconImage(mainFrameManagement.getFrameIcon());
        dialog.setDocument(getActivePanel());
        dialog.setLocationRelativeTo(dialog.getParent());
        dialog.setVisible(true);
    }//GEN-LAST:event_filePropertiesMenuItemActionPerformed

    private void findToolButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_findToolButtonActionPerformed
        editFindMenuItemActionPerformed(evt);
    }//GEN-LAST:event_findToolButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(XBTextEditorFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(XBTextEditorFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(XBTextEditorFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(XBTextEditorFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new XBTextEditorFrame().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField documentCursorPositionTextField;
    private javax.swing.JTextField documentEncodingTextField;
    private javax.swing.JPanel documentStatusPanel;
    private javax.swing.JMenuItem editFindAgainMenuItem;
    private javax.swing.JMenuItem editFindMenuItem;
    private javax.swing.JMenuItem editFindReplaceMenuItem;
    private javax.swing.JMenuItem editGotoMenuItem;
    public javax.swing.JMenu editMenu;
    public javax.swing.JMenu fileMenu;
    private javax.swing.JMenuItem filePrintMenuItem;
    private javax.swing.JMenuItem filePropertiesMenuItem;
    private javax.swing.JButton findToolButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator12;
    private javax.swing.JSeparator jSeparator13;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JPopupMenu mainPopupMenu;
    private javax.swing.JMenuItem manageEncodingsMenuItem;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenuItem optionsColorsMenuItem;
    private javax.swing.JMenuItem optionsFontMenuItem;
    public javax.swing.JMenu optionsMenu;
    private javax.swing.JPanel statusBar;
    private javax.swing.JPanel statusPanel;
    private javax.swing.JToolBar toolBar;
    private javax.swing.JMenu toolsEncodingMenu;
    private javax.swing.JRadioButtonMenuItem utfEncodingRadioButtonMenuItem;
    public javax.swing.JMenu viewMenu;
    private javax.swing.JCheckBoxMenuItem viewWordWrapCheckBoxMenuItem;
    // End of variables declaration//GEN-END:variables

    public void setDocumentCharset(Charset charset) {
        getActivePanel().setCharset(charset);
        documentEncodingTextField.setText(charset.name());
        documentEncodingTextField.repaint();
    }

    public void initFindDialog() {
        if (findDialog == null) {
            findDialog = new FindDialog(getFrame(),true);
            findDialog.setIconImage(mainFrameManagement.getFrameIcon());
        }
    }

    public void initGotoDialog() {
        if (gotoDialog == null) {
            gotoDialog = new GotoDialog(getFrame(), true);
            gotoDialog.setIconImage(mainFrameManagement.getFrameIcon());
        }
    }

    /**
     * Gets the extension part of file name.
     *
     * @param file Source file
     * @return extension part of file name
     */
    public static String getExtension(File file) {
        String ext = null;
        String str = file.getName();
        int i = str.lastIndexOf('.');

        if (i > 0 &&  i < str.length() - 1) {
            ext = str.substring(i+1).toLowerCase();
        }
        return ext;
    }

    @Override
    public List<String> getCurrentEncodingList() {
        return encodings;
    }

    @Override
    public void setEncodingList(List<String> encodings) {
        this.encodings = encodings;
        encodingsRebuild();
    }

    @Override
    public String getSelectedEncoding() {
        return activePanel.getCharset().name();
    }

    @Override
    public void setSelectedEncoding(String encoding) {
        if (encoding != null) {
            setDocumentCharset(Charset.forName(encoding));
        }
    }

    @Override
    public boolean getWordWrapMode() {
        return viewWordWrapCheckBoxMenuItem.isSelected();
    }

    @Override
    public void setWordWrapMode(boolean mode) {
        if (viewWordWrapCheckBoxMenuItem.isSelected() != mode) {
            viewWordWrapCheckBoxMenuItem.doClick();
        }
    }

    public class XBTFileType extends FileFilter implements FileType {

        @Override
        public boolean accept(File f) {
            if (f.isDirectory()) {
                return true;
            }
            String extension = getExtension(f);
            if (extension != null) {
                if (extension.length()<3) {
                    return false;
                }
                return "xbt".contains(extension.substring(0,3));
            }
            return false;
        }

        @Override
        public String getDescription() {
            return resourceBundle.getString("filter_file_xbt");
        }

        @Override
        public String getFileTypeId() {
            return XBT_FILE_TYPE;
        }
    }

    public FileType newXBTFileType() {
        return new XBTFileType();
    }

    public class TXTFileType extends FileFilter implements FileType {

        @Override
        public boolean accept(File f) {
            if (f.isDirectory()) {
                return true;
            }
            String extension = getExtension(f);
            if (extension != null) {
                return "txt".equals(extension);
            }
            return false;
        }

        @Override
        public String getDescription() {
            return resourceBundle.getString("filter_file_txt");
        }

        @Override
        public String getFileTypeId() {
            return TXT_FILE_TYPE;
        }
    }

    public TXTFileType newTXTFileType() {
        return new TXTFileType();
    }

    public JPanel getStatusPanel() {
        return statusPanel;
    }

    protected javax.swing.JFrame getFrame() {
        return this;
    }
}
