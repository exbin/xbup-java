/*
 * Copyright (C) ExBin Project
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
package org.exbin.framework.editor.xbup.panel;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import org.xbup.lib.core.block.XBBlockDataMode;
import org.xbup.lib.core.block.XBBlockType;
import org.xbup.lib.core.block.XBFBlockType;
import org.xbup.lib.core.block.XBTBlock;
import org.xbup.lib.core.block.declaration.catalog.XBCBlockDecl;
import org.xbup.lib.core.catalog.XBACatalog;
import org.xbup.lib.core.catalog.XBCatalog;
import org.xbup.lib.core.catalog.base.XBCBlockSpec;
import org.xbup.lib.core.catalog.base.service.XBCXNameService;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.token.XBAttribute;
import org.xbup.lib.core.type.XBData;
import org.exbin.framework.editor.text.dialog.FindTextDialog;
import org.exbin.framework.editor.text.dialog.TextFontDialog;
import org.exbin.framework.editor.text.panel.TextPanel;
import org.exbin.framework.editor.xbup.dialog.BlockPropertiesDialog;
import org.exbin.framework.editor.xbup.dialog.ModifyBlockDialog;
import org.exbin.framework.gui.editor.api.XBEditorProvider;
import org.exbin.framework.gui.file.api.FileType;
import org.exbin.framework.gui.menu.api.ClipboardActionsUpdateListener;
import org.exbin.framework.gui.menu.api.ComponentClipboardHandler;
import org.xbup.lib.framework.gui.utils.WindowUtils;
import org.xbup.lib.operation.Operation;
import org.xbup.lib.operation.OperationEvent;
import org.xbup.lib.operation.OperationListener;
import org.xbup.lib.operation.XBTDocCommand;
import org.xbup.lib.operation.XBTDocOperation;
import org.xbup.lib.operation.basic.XBTExtAreaOperation;
import org.xbup.lib.operation.basic.XBTModifyBlockOperation;
import org.xbup.lib.operation.basic.command.XBTChangeBlockCommand;
import org.xbup.lib.operation.basic.command.XBTModifyBlockCommand;
import org.xbup.lib.operation.undo.XBUndoHandler;
import org.xbup.lib.parser_tree.XBTTreeDocument;
import org.xbup.lib.parser_tree.XBTTreeNode;
import org.xbup.lib.plugin.XBPluginRepository;

/**
 * Panel with XBUP document visualization.
 *
 * @version 0.2.0 2016/02/29
 * @author ExBin Project (http://exbin.org)
 */
public class XBDocumentPanel extends javax.swing.JPanel implements XBEditorProvider, ComponentClipboardHandler {

    private final TreeDocument mainDoc;
    private FileType fileType;
    private XBACatalog catalog;
    private boolean splitMode = false;

    private PanelMode mode = PanelMode.TREE;

    private final XBDocTreePanel treePanel;
    private final XBDocHexPanel hexPanel;
    private final TextPanel textPanel;

    private XBPropertyPanel propertyPanel;
    private XBPluginRepository pluginRepository;
    private PropertyChangeListener propertyChangeListener;
    private ClipboardActionsUpdateListener clipboardActionsUpdateListener;

    public XBDocumentPanel(XBACatalog catalog, XBUndoHandler undoHandler) {
        this.catalog = catalog;
        mainDoc = new TreeDocument(catalog);

        initComponents();

        propertyPanel = new XBPropertyPanel(catalog);
        mainSplitPane.setRightComponent(propertyPanel);

        treePanel = new XBDocTreePanel(mainDoc, catalog, undoHandler, popupMenu);
        hexPanel = new XBDocHexPanel(mainDoc);
        textPanel = new TextPanel();
        textPanel.setNoBorder();

        treePanel.setPopupMenu(popupMenu);
        textPanel.setPopupMenu(popupMenu);

        ((JPanel) mainTabbedPane.getComponentAt(0)).add(treePanel, java.awt.BorderLayout.CENTER);

        treePanel.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                firePropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
                if (propertyChangeListener != null) {
                    propertyChangeListener.propertyChange(evt);
                }
            }
        });

        treePanel.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                if (propertyPanel.isEnabled()) {
                    propertyPanel.setActiveNode(treePanel.getSelectedItem());
                }
            }
        });

        super.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (propertyChangeListener != null) {
                    propertyChangeListener.propertyChange(evt);
                }
            }
        });

        changeSplitMode();
        splitMode = true;
        //updateItem();
    }

    public void postWindowOpened() {
        mainSplitPane.setDividerLocation(getWidth() - 300 > 0 ? getWidth() - 300 : getWidth() / 3);
    }

    /**
     * Updating selected item available operations status, like add, edit,
     * delete.
     */
    public void updateItem() {
        treePanel.updateItemStatus();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        popupMenu = new javax.swing.JPopupMenu();
        popupItemViewMenuItem = new javax.swing.JMenuItem();
        popupItemCopyMenuItem = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        popupItemPropertiesMenuItem = new javax.swing.JMenuItem();
        mainTabbedPane = new javax.swing.JTabbedPane();
        treeTabPanel = new javax.swing.JPanel();
        sourceTabPanel = new javax.swing.JPanel();
        hexTabPanel = new javax.swing.JPanel();
        mainSplitPane = new javax.swing.JSplitPane();
        splitTabbedPane = new javax.swing.JTabbedPane();
        treeTabPanel1 = new javax.swing.JPanel();
        sourceTabPanel1 = new javax.swing.JPanel();
        hexTabPanel1 = new javax.swing.JPanel();

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/xbup/lib/framework/editor/xbup/panel/resources/XBDocumentPanel"); // NOI18N
        popupItemViewMenuItem.setText(bundle.getString("popupItemViewMenuItem.text")); // NOI18N
        popupItemViewMenuItem.setToolTipText(bundle.getString("popupItemViewMenuItem.toolTipText")); // NOI18N
        popupItemViewMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popupItemViewMenuItemActionPerformed(evt);
            }
        });
        popupMenu.add(popupItemViewMenuItem);

        popupItemCopyMenuItem.setText(bundle.getString("popupItemCopyMenuItem.text")); // NOI18N
        popupItemCopyMenuItem.setToolTipText(bundle.getString("popupItemCopyMenuItem.toolTipText")); // NOI18N
        popupItemCopyMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popupItemCopyMenuItemActionPerformed(evt);
            }
        });
        popupMenu.add(popupItemCopyMenuItem);
        popupMenu.add(jSeparator1);

        popupItemPropertiesMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_ENTER, java.awt.event.InputEvent.ALT_MASK));
        popupItemPropertiesMenuItem.setText(bundle.getString("popupItemPropertiesMenuItem.text")); // NOI18N
        popupItemPropertiesMenuItem.setToolTipText(bundle.getString("popupItemPropertiesMenuItem.toolTipText")); // NOI18N
        popupItemPropertiesMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popupItemPropertiesMenuItemActionPerformed(evt);
            }
        });
        popupMenu.add(popupItemPropertiesMenuItem);

        setLayout(new java.awt.CardLayout());

        mainTabbedPane.setTabPlacement(javax.swing.JTabbedPane.BOTTOM);
        mainTabbedPane.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                mainTabbedPaneStateChanged(evt);
            }
        });

        treeTabPanel.setLayout(new java.awt.BorderLayout());
        mainTabbedPane.addTab("Tree", treeTabPanel);

        sourceTabPanel.setLayout(new java.awt.BorderLayout());
        mainTabbedPane.addTab("Source", sourceTabPanel);

        hexTabPanel.setLayout(new java.awt.BorderLayout());
        mainTabbedPane.addTab("Hex", hexTabPanel);

        add(mainTabbedPane, "main");

        mainSplitPane.setBorder(null);
        mainSplitPane.setDividerSize(8);
        mainSplitPane.setResizeWeight(1.0);

        splitTabbedPane.setTabPlacement(javax.swing.JTabbedPane.BOTTOM);
        splitTabbedPane.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                splitTabbedPaneStateChanged(evt);
            }
        });

        treeTabPanel1.setLayout(new java.awt.BorderLayout());
        splitTabbedPane.addTab("Tree", treeTabPanel1);

        sourceTabPanel1.setLayout(new java.awt.BorderLayout());
        splitTabbedPane.addTab("Source", sourceTabPanel1);

        hexTabPanel1.setLayout(new java.awt.BorderLayout());
        splitTabbedPane.addTab("Hex", hexTabPanel1);

        mainSplitPane.setLeftComponent(splitTabbedPane);

        add(mainSplitPane, "split");
    }// </editor-fold>//GEN-END:initComponents

    private void popupItemPropertiesMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popupItemPropertiesMenuItemActionPerformed
        actionItemProperties();
    }//GEN-LAST:event_popupItemPropertiesMenuItemActionPerformed

    private void popupItemViewMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popupItemViewMenuItemActionPerformed
        performModify();
    }//GEN-LAST:event_popupItemViewMenuItemActionPerformed

    private void popupItemCopyMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popupItemCopyMenuItemActionPerformed
        performCopy();
    }//GEN-LAST:event_popupItemCopyMenuItemActionPerformed

    private void mainTabbedPaneStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_mainTabbedPaneStateChanged
        setMode(PanelMode.values()[mainTabbedPane.getSelectedIndex()]);
    }//GEN-LAST:event_mainTabbedPaneStateChanged

    private void splitTabbedPaneStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_splitTabbedPaneStateChanged
        setMode(PanelMode.values()[splitTabbedPane.getSelectedIndex()]);
    }//GEN-LAST:event_splitTabbedPaneStateChanged

    public XBTTreeNode getSelectedItem() {
        return treePanel.getSelectedItem();
    }

    public XBTTreeDocument getDoc() {
        return mainDoc;
    }

    public void reportStructureChange(XBTBlock block) {
        treePanel.reportStructureChange(block);
    }

    public boolean isEditEnabled() {
        switch (mode) {
            case TREE:
                return treePanel.isEditEnabled();
            case TEXT:
                return false;
            case HEX:
                return false;
            default:
                return false;
        }
    }

    public boolean isAddEnabled() {
        switch (mode) {
            case TREE:
                return treePanel.isAddEnabled();
            case TEXT:
                return false;
            case HEX:
                return false;
            default:
                return false;
        }
    }

    public boolean isPasteEnabled() {
        return treePanel.isPasteEnabled();
    }

    public void addUpdateListener(ActionListener tml) {
        treePanel.addUpdateListener(tml);
    }

    public void removeUpdateListener(ActionListener tml) {
        treePanel.removeUpdateListener(tml);
    }

    public XBACatalog getCatalog() {
        return catalog;
    }

    public void setCatalog(XBACatalog catalog) {
        this.catalog = catalog;
        treePanel.setCatalog(catalog);
        mainDoc.setCatalog(catalog);
        mainDoc.processSpec();
        propertyPanel.setCatalog(catalog);
    }

    @Override
    public void performCut() {
        switch (mode) {
            case TREE:
                treePanel.performCut();
                break;
            case TEXT:
                textPanel.performCut();
                break;
            case HEX:
                hexPanel.performCut();
            default:
                break;
        }
    }

    @Override
    public void performCopy() {
        switch (mode) {
            case TREE:
                treePanel.performCopy();
                break;
            case TEXT:
                textPanel.performCopy();
                break;
            case HEX:
                hexPanel.performCopy();
            default:
                break;
        }
    }

    @Override
    public void performPaste() {
        switch (mode) {
            case TREE:
                treePanel.performPaste();
                break;
            case TEXT:
                textPanel.performPaste();
                break;
            case HEX:
                hexPanel.performPaste();
                break;
            default:
                break;
        }
    }

    @Override
    public void performSelectAll() {
        switch (mode) {
            case TREE:
                treePanel.performSelectAll();
                break;
            case TEXT:
                textPanel.performSelectAll();
                break;
            case HEX:
                hexPanel.performSelectAll();
                break;
            default:
                break;
        }
    }

    public void performAdd() {
        treePanel.performAdd();
    }

    @Override
    public void performDelete() {
        switch (mode) {
            case TREE:
                treePanel.performDelete();
                break;
            case TEXT:
                textPanel.performDelete();
                break;
            case HEX:
                hexPanel.performDelete();
                break;
            default:
                break;
        }
    }

    public void setMode(PanelMode mode) {
        if (this.mode != mode) {
            switch (this.mode) {
                case TREE:
                    break;
                case TEXT: {
                    break;
                }
                case HEX: {
                    // TODO: Replace stupid buffer copy later
                    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                    try {
                        hexPanel.saveToStream(buffer);
                        mainDoc.fromStreamUB(new ByteArrayInputStream(buffer.toByteArray()));
                    } catch (XBProcessingException ex) {
                        Logger.getLogger(XBDocumentPanel.class.getName()).log(Level.SEVERE, null, ex);
                        JOptionPane.showMessageDialog(WindowUtils.getFrame(this), ex.getMessage(), "Parsing Exception", JOptionPane.ERROR_MESSAGE);
                    } catch (IOException ex) {
                        Logger.getLogger(XBDocumentPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                }
                default:
                    throw new InternalError("Unknown mode");
            }
            switch (mode) {
                case TREE:
                    break;
                case TEXT: {
                    String text = "<!XBUP version=\"0.1\">\n";
                    if (mainDoc.getRootBlock() != null) {
                        text += nodeAsText((XBTTreeNode) mainDoc.getRootBlock(), "").toString();
                    }
                    textPanel.setText(text);
                    break;
                }
                case HEX: {
                    // TODO: Replace stupid buffer copy later
                    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                    try {
                        mainDoc.toStreamUB(buffer);
                    } catch (IOException ex) {
                        Logger.getLogger(XBDocumentPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    hexPanel.loadFromStream(new ByteArrayInputStream(buffer.toByteArray()), buffer.size());
                    break;
                }
                default:
                    throw new InternalError("Unknown mode");
            }
            this.mode = mode;

//            mainFrame.getEditFindAction().setEnabled(mode != PanelMode.TREE);
//            mainFrame.getEditFindAgainAction().setEnabled(mode == PanelMode.TEXT);
//            mainFrame.getEditGotoAction().setEnabled(mode == PanelMode.TEXT);
//            mainFrame.getEditReplaceAction().setEnabled(mode == PanelMode.TEXT);
//            mainFrame.getItemAddAction().setEnabled(false);
//            mainFrame.getItemModifyAction().setEnabled(false);
            showPanel();
            updateItem();
            updateActionStatus(null);
            if (clipboardActionsUpdateListener != null) {
                clipboardActionsUpdateListener.stateChanged();
            }
        }
    }

    public void showPanel() {
        JTabbedPane tabPane;
        if (splitMode) {
            tabPane = splitTabbedPane;
        } else {
            tabPane = mainTabbedPane;
        }

        int index = getMode().ordinal();
        tabPane.setSelectedIndex(index);
        ((JPanel) tabPane.getComponentAt(index)).add(getPanel(index));
    }

    private StringBuffer nodeAsText(XBTTreeNode node, String prefix) {
        StringBuffer result = new StringBuffer();
        result.append(prefix);
        if (node.getDataMode() == XBBlockDataMode.DATA_BLOCK) {
            result.append("[");
            for (long i = 0; i < node.getDataSize(); i++) {
                byte b = node.getBlockData().getByte(i);
                result.append(getHex(b));
            }
            result.append("]\n");
        } else {
            result.append("<").append(getCaption(node));
            if (node.getAttributesCount() > 2) {
                XBAttribute[] attributes = node.getAttributes();
                for (int i = 0; i < attributes.length; i++) {
                    XBAttribute attribute = attributes[i];
                    result.append(" ").append(i + 1).append("=\"").append(attribute.getNaturalLong()).append("\"");
                }
            }

            if (node.getChildren() != null) {
                result.append(">\n");
                XBTBlock[] children = node.getChildren();
                for (XBTBlock child : children) {
                    result.append(nodeAsText((XBTTreeNode) child, prefix + "  "));
                }
                result.append(prefix);
                result.append("</").append(getCaption(node)).append(">\n");
            } else {
                result.append("/>\n");
            }
        }
        return result;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel hexTabPanel;
    private javax.swing.JPanel hexTabPanel1;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JSplitPane mainSplitPane;
    private javax.swing.JTabbedPane mainTabbedPane;
    private javax.swing.JMenuItem popupItemCopyMenuItem;
    private javax.swing.JMenuItem popupItemPropertiesMenuItem;
    private javax.swing.JMenuItem popupItemViewMenuItem;
    private javax.swing.JPopupMenu popupMenu;
    private javax.swing.JPanel sourceTabPanel;
    private javax.swing.JPanel sourceTabPanel1;
    private javax.swing.JTabbedPane splitTabbedPane;
    private javax.swing.JPanel treeTabPanel;
    private javax.swing.JPanel treeTabPanel1;
    // End of variables declaration//GEN-END:variables

    public void setEditEnabled(boolean editEnabled) {
        treePanel.setEditEnabled(editEnabled);
    }

    public void setAddEnabled(boolean addEnabled) {
        treePanel.setAddEnabled(addEnabled);
    }

    public void updateUndoAvailable() {
        firePropertyChange("undoAvailable", false, true);
        firePropertyChange("redoAvailable", false, true);
    }

    public XBUndoHandler getUndoHandler() {
        return treePanel.getUndoHandler();
    }

    @Override
    public void loadFromFile() {
        try {
            getDoc().fromFileUB();
            getDoc().processSpec();
            reportStructureChange((XBTTreeNode) getDoc().getRootBlock());
            performSelectAll();
            getUndoHandler().clear();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(XBDocumentPanel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XBDocumentPanel.class.getName()).log(Level.SEVERE, null, ex);
        } catch (XBProcessingException ex) {
            Logger.getLogger(XBDocumentPanel.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(WindowUtils.getFrame(this), ex.getMessage(), "Parsing Exception", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * @return the popupMenu
     */
    public JPopupMenu getPopupMenu() {
        return popupMenu;
    }

    public Color[] getDefaultColors() {
        return textPanel.getDefaultColors();
    }

    public void setCurrentColors(Color[] colors) {
        textPanel.setCurrentColors(colors);
    }

    public Font getDefaultFont() {
        return textPanel.getDefaultFont();
    }

    public void setCurrentFont(Font deriveFont) {
        textPanel.setCurrentFont(deriveFont);
    }

    @Override
    public String getFileName() {
        return getDoc().getFileName();
    }

    @Override
    public void setFileName(String fileName) {
        getDoc().setFileName(fileName);
    }

    @Override
    public boolean isModified() {
        return getDoc().wasModified();
    }

    public boolean changeLineWrap() {
        return textPanel.changeLineWrap();
    }

    public void showFontDialog(TextFontDialog dlg) {
        textPanel.showFontDialog(dlg);
    }

    @Override
    public void saveToFile() {
        try {
            getDoc().toFileUB();
            getUndoHandler().setSyncPoint();
            getDoc().setModified(false);
        } catch (IOException ex) {
            Logger.getLogger(XBDocumentPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void newFile() {
        getUndoHandler().clear();
        getDoc().clear();
        reportStructureChange(null);
        updateItem();
    }

    public void printFile() {
        textPanel.printFile();
    }

    public int getLineCount() {
        return textPanel.getLineCount();
    }

    public void gotoRelative(int charPos) {
        textPanel.gotoRelative(charPos);
    }

    public void gotoLine(int line) {
        textPanel.gotoLine(line);
    }

    public void findText(FindTextDialog findDialog) {
        textPanel.findText(findDialog);
    }

    public void setCharset(Charset charset) {
        textPanel.setCharset(charset);
    }

    public Color[] getCurrentColors() {
        return textPanel.getCurrentColors();
    }

    public void setFileMode(int i) {
        // TODO textPanel.setFileMode(getMode().ordinal());
    }

    public void performModify() {
        XBTTreeNode node = getSelectedItem();
        XBTDocCommand undoStep;
        try {
            ModifyBlockDialog dialog = new ModifyBlockDialog(WindowUtils.getFrame(this), true);
            dialog.setCatalog(catalog);
            dialog.setPluginRepository(pluginRepository);
            dialog.setLocationRelativeTo(dialog.getParent());
            XBTTreeNode newNode = dialog.runDialog(node, mainDoc);
            if (dialog.getDialogOption() == JOptionPane.OK_OPTION) {
                if (node.getParent() == null) {
                    undoStep = new XBTChangeBlockCommand(mainDoc);
                    long position = node.getBlockIndex();
                    XBTModifyBlockOperation modifyOperation = new XBTModifyBlockOperation(mainDoc, position, newNode);
                    ((XBTChangeBlockCommand) undoStep).appendOperation(modifyOperation);
                    XBData extendedArea = new XBData();
                    dialog.saveExtendedArea(extendedArea.getDataOutputStream());
                    XBTExtAreaOperation extOperation = new XBTExtAreaOperation(mainDoc, extendedArea);
                    ((XBTChangeBlockCommand) undoStep).appendOperation(extOperation);
                } else {
                    undoStep = new XBTModifyBlockCommand(mainDoc, node, newNode);
                }
                // TODO: Optimized diff command later
//                if (node.getDataMode() == XBBlockDataMode.DATA_BLOCK) {
//                    undoStep = new XBTModDataBlockCommand(node, newNode);
//                } else if (newNode.getChildrenCount() > 0) {
//                } else {
//                    undoStep = new XBTModAttrBlockCommand(node, newNode);
//                }
                getUndoHandler().execute(undoStep);

                mainDoc.processSpec();
                reportStructureChange(node);
                getDoc().setModified(true);
            }
        } catch (Exception ex) {
            Logger.getLogger(XBDocumentPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setSplitMode(boolean mode) {
        if (mode != splitMode) {
            changeSplitMode();
            splitMode = mode;
        }
    }

    private void changeSplitMode() {
        if (splitMode) {
            ((CardLayout) getLayout()).show(this, "main");
            int selectedIndex = splitTabbedPane.getSelectedIndex();
            ((JPanel) mainTabbedPane.getComponentAt(selectedIndex)).add(getPanel(selectedIndex));
            mainTabbedPane.setSelectedIndex(selectedIndex);
        } else {
            ((CardLayout) getLayout()).show(this, "split");
            int selectedIndex = mainTabbedPane.getSelectedIndex();
            ((JPanel) splitTabbedPane.getComponentAt(selectedIndex)).add(getPanel(selectedIndex));
            splitTabbedPane.setSelectedIndex(selectedIndex);
        }
        splitMode = !splitMode;
        showPanel();
    }

    private Component getPanel(int index) {
        switch (index) {
            case 0:
                return treePanel;
            case 1:
                return textPanel;
            case 2:
                return hexPanel;
        }

        return null;
    }

//    public ActivePanelActionHandling getActivePanel() {
//        int selectedIndex = mainTabbedPane.getSelectedIndex();
//        return (ActivePanelActionHandling) getPanel(selectedIndex);
//    }
    public boolean isSplitMode() {
        return splitMode;
    }

    public PanelMode getMode() {
        return mode;
    }

    public XBPluginRepository getPluginRepository() {
        return pluginRepository;
    }

    public void setPluginRepository(XBPluginRepository pluginRepository) {
        this.pluginRepository = pluginRepository;
        propertyPanel.setPluginRepository(pluginRepository);
    }

    public String getHex(byte b) {
        byte low = (byte) (b & 0xf);
        byte hi = (byte) (b >> 0x8);
        return (Integer.toHexString(hi) + Integer.toHexString(low)).toUpperCase();
    }

    private String getCaption(XBTTreeNode node) {
        if (node.getDataMode() == XBBlockDataMode.DATA_BLOCK) {
            return "Data Block";
        }
        XBBlockType blockType = node.getBlockType();
        if (catalog != null) {
            XBCXNameService nameService = (XBCXNameService) catalog.getCatalogService(XBCXNameService.class);
            XBCBlockDecl blockDecl = (XBCBlockDecl) node.getBlockDecl();
            if (blockDecl != null) {
                XBCBlockSpec blockSpec = blockDecl.getBlockSpecRev().getParent();
                return nameService.getDefaultText(blockSpec);
            }
        }
        return "Unknown" + " (" + Integer.toString(((XBFBlockType) blockType).getGroupID().getInt()) + ", " + Integer.toString(((XBFBlockType) blockType).getBlockID().getInt()) + ")";
    }

    @Override
    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }

    @Override
    public void setPropertyChangeListener(PropertyChangeListener propertyChangeListener) {
        this.propertyChangeListener = propertyChangeListener;
    }

    public boolean updateActionStatus(Component component) {
        switch (mode) {
            case TREE:
//                return treePanel.updateActionStatus(component);
            case TEXT: {
                return false;
            }
            case HEX: {
                return false;
            }
        }

        return false;
    }

    public void releaseActionStatus() {
        switch (mode) {
            case TREE: {
//                treePanel.releaseActionStatus();
                break;
            }
        }
    }

    public boolean performAction(String eventName, ActionEvent event) {
        switch (mode) {
            case TREE:
//                return treePanel.performAction(eventName, event);
        }

        return false;
    }

    @Override
    public String getWindowTitle(String frameTitle) {
        if (!"".equals(getDoc().getFileName())) {
            int pos;
            int newpos = 0;
            do {
                pos = newpos;
                newpos = getDoc().getFileName().indexOf(File.separatorChar, pos) + 1;
            } while (newpos > 0);
            return getDoc().getFileName().substring(pos) + " - " + frameTitle;
        }

        return frameTitle;
    }

    public Font getCurrentFont() {
        return textPanel.getCurrentFont();
    }

    @Override
    public JPanel getPanel() {
        return this;
    }

    @Override
    public boolean isSelection() {
        switch (mode) {
            case TREE:
                return treePanel.isSelection();
            case TEXT:
                return textPanel.isSelection();
            case HEX:
                // TODO
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean isEditable() {
        switch (mode) {
            case TREE:
                return treePanel.isEditEnabled();
            case TEXT:
                return true;
            case HEX:
                return hexPanel.isEditEnabled();
            default:
                return false;
        }
    }

    @Override
    public boolean canSelectAll() {
        switch (mode) {
            case TREE:
                // TODO Multiple selection in tree
                return true;
            case TEXT:
                return true;
            case HEX:
                return true;
            default:
                return false;
        }
    }

    @Override
    public boolean canPaste() {
        switch (mode) {
            case TREE:
                return treePanel.isPasteEnabled();
            case TEXT:
                // TODO Allow to paste text only
                return true;
            case HEX:
                return hexPanel.isPasteEnabled();
            default:
                return false;
        }
    }

    @Override
    public void setUpdateListener(ClipboardActionsUpdateListener updateListener) {
        clipboardActionsUpdateListener = updateListener;
        treePanel.setUpdateListener(updateListener);
        textPanel.setUpdateListener(updateListener);
    }

    public enum PanelMode {
        TREE,
        TEXT,
        HEX
    }

    public void setPopupMenu(JPopupMenu popupMenu) {
        this.popupMenu = popupMenu;
        treePanel.setPopupMenu(popupMenu);
        textPanel.setPopupMenu(popupMenu);
    }

    public void actionItemProperties() {
        BlockPropertiesDialog dialog = new BlockPropertiesDialog(WindowUtils.getFrame(this), true);
        dialog.setCatalog(catalog);
        dialog.runDialog(getSelectedItem());
    }

    private class TreeDocument extends XBTTreeDocument implements OperationListener {

        public TreeDocument(XBCatalog catalog) {
            super(catalog);
        }

        @Override
        public void notifyChange(OperationEvent event) {
            Operation operation = event.getOperation();
            // TODO Consolidate
            mainDoc.processSpec();
            reportStructureChange(null);
            // getDoc().setModified(true);
            updateItem();
            updateActionStatus(null);
            if (clipboardActionsUpdateListener != null) {
                clipboardActionsUpdateListener.stateChanged();
            }

            if (operation instanceof XBTDocOperation) {
                setMode(PanelMode.TREE);
            } else {
                // TODO
            }
        }
    }
}
