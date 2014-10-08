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
package org.xbup.tool.editor.module.xbdoc_editor.panel;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Frame;
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
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import org.xbup.lib.core.block.XBBlockDataMode;
import org.xbup.lib.core.block.XBBlockType;
import org.xbup.lib.core.block.XBFBlockType;
import org.xbup.lib.core.block.declaration.catalog.XBCBlockDecl;
import org.xbup.lib.core.block.param.XBParamDecl;
import org.xbup.lib.core.catalog.XBACatalog;
import org.xbup.lib.core.catalog.base.XBCBlockSpec;
import org.xbup.lib.core.catalog.base.service.XBCXNameService;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.param.XBParamConvertor;
import org.xbup.lib.core.parser.param.XBParamListener;
import org.xbup.lib.parser_tree.XBTTreeDocument;
import org.xbup.lib.parser_tree.XBTTreeNode;
import org.xbup.lib.parser_tree.XBTTreeWriter;
import org.xbup.lib.core.ubnumber.type.UBNat32;
import org.xbup.lib.operation.XBTCommand;
import org.xbup.lib.operation.basic.XBTModAttrBlockCommand;
import org.xbup.lib.operation.basic.XBTModDataBlockCommand;
import org.xbup.lib.operation.basic.XBTModifyBlockCommand;
import org.xbup.lib.operation.undo.XBTLinearUndo;
import org.xbup.lib.plugin.XBPluginRepository;
import org.xbup.tool.editor.module.xbdoc_editor.XBDocEditorFrame;
import org.xbup.tool.editor.module.xbdoc_editor.dialog.ItemModifyDialog;
import org.xbup.tool.editor.module.xbdoc_editor.dialog.ItemPropertiesDialog;
import org.xbup.tool.editor.module.text_editor.dialog.FindDialog;
import org.xbup.tool.editor.module.text_editor.dialog.FontDialog;
import org.xbup.tool.editor.module.text_editor.panel.TextPanel;
import org.xbup.tool.editor.base.api.ActivePanelActionHandling;
import org.xbup.tool.editor.base.api.ActivePanelUndoable;
import org.xbup.tool.editor.base.api.ApplicationFilePanel;
import org.xbup.tool.editor.base.api.FileType;

/**
 * Panel with XBUP document visualization.
 *
 * @version 0.1.23 2013/09/23
 * @author XBUP Project (http://xbup.org)
 */
public class XBDocumentPanel extends javax.swing.JPanel implements ApplicationFilePanel, ActivePanelUndoable, ActivePanelActionHandling {

    private XBTTreeDocument mainDoc;
    private FileType fileType;
    private XBACatalog catalog;
    private boolean splitMode = false;

    private PanelMode mode;

    private XBDocTreePanel treePanel;
    private XBDocHexPanel hexPanel;
    private TextPanel textPanel;

    private XBPropertyPanel propertyPanel;
    private XBPluginRepository pluginRepository;
    private PropertyChangeListener propertyChangeListener;
    private final XBDocEditorFrame mainFrame;

    public XBDocumentPanel(XBDocEditorFrame mainFrame, XBACatalog catalog) {
        this.mainFrame = mainFrame;
        mode = PanelMode.TREE;
        this.catalog = catalog;
        mainDoc = new XBTTreeDocument(catalog);

        initComponents();

        treePanel = new XBDocTreePanel(mainFrame, mainDoc, catalog, popupMenu);
        hexPanel = new XBDocHexPanel(mainDoc);
        textPanel = new TextPanel();

        treePanel.setPopupMenu(popupMenu);
        textPanel.setPopupMenu(popupMenu);

        mainPanel.add(treePanel,"tree");
        mainPanel.add(hexPanel,"hex");
        mainPanel.add(textPanel,"text");

        treePanel.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                firePropertyChange(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
                if (propertyChangeListener != null) {
                    propertyChangeListener.propertyChange(evt);
                }
                if (propertyPanel.isEnabled()) {
                    propertyPanel.setActiveNode(treePanel.getSelectedItem());
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

        propertyPanel = new XBPropertyPanel(catalog);
        jSplitPane1.setRightComponent(propertyPanel);
        changeSplitMode();
        splitMode = true;
        //updateItem();
    }

    /** Updating selected item available operations status, like add, edit, delete */
    public void updateItem() {
        treePanel.updateItemStatus();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        popupMenu = new javax.swing.JPopupMenu();
        popupItemViewMenuItem = new javax.swing.JMenuItem();
        popupItemCopyMenuItem = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        popupItemPropertiesMenuItem = new javax.swing.JMenuItem();
        mainPanel = new javax.swing.JPanel();
        jSplitPane1 = new javax.swing.JSplitPane();
        splitPanel = new javax.swing.JPanel();

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/xbup/tool/xbeditor/module/xbdoceditor/panel/resources/XBDocumentPanel"); // NOI18N
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

        mainPanel.setLayout(new java.awt.CardLayout());
        add(mainPanel, "main");

        jSplitPane1.setBorder(null);
        jSplitPane1.setDividerLocation(0);
        jSplitPane1.setDividerSize(8);
        jSplitPane1.setResizeWeight(1.0);
        jSplitPane1.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                jSplitPane1ComponentResized(evt);
            }
        });

        splitPanel.setLayout(new java.awt.CardLayout());
        jSplitPane1.setLeftComponent(splitPanel);

        add(jSplitPane1, "split");
    }// </editor-fold>//GEN-END:initComponents

    private void jSplitPane1ComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jSplitPane1ComponentResized
        if (jSplitPane1.getDividerLocation() < 2) {
            jSplitPane1.setDividerLocation(getWidth()-400);
        }
    }//GEN-LAST:event_jSplitPane1ComponentResized

    private void popupItemPropertiesMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popupItemPropertiesMenuItemActionPerformed
        actionItemProperties();
    }//GEN-LAST:event_popupItemPropertiesMenuItemActionPerformed

    private void popupItemViewMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popupItemViewMenuItemActionPerformed
        performModify();
    }//GEN-LAST:event_popupItemViewMenuItemActionPerformed

    private void popupItemCopyMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popupItemCopyMenuItemActionPerformed
        performCopy();
    }//GEN-LAST:event_popupItemCopyMenuItemActionPerformed

    public XBTTreeNode getSelectedItem() {
        return treePanel.getSelectedItem();
    }

    public XBTTreeDocument getDoc() {
        return mainDoc;
    }

    public void reportStructureChange(XBTTreeNode node) {
        treePanel.reportStructureChange(node);
    }

    public boolean isEditEnabled() {
        return treePanel.isEditEnabled();
    }

    public boolean isAddEnabled() {
        return treePanel.isAddEnabled();
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
//        if (mainDoc != null) {
            treePanel.setCatalog(catalog);
            mainDoc.setCatalog(catalog);
            mainDoc.processSpec();
            propertyPanel.setCatalog(catalog);
//        }
    }

    public void performCut() {
        performCopy();
        performDelete();
    }

    public void performCopy() {
        if (mode == PanelMode.TREE) {
            treePanel.performCopy();
        } else if (mode == PanelMode.TEXT) {
            textPanel.performCopy();
        }
    }

    public void performPaste() {
        if (mode == PanelMode.TREE) {
            treePanel.performPaste();
        } else if (mode == PanelMode.TEXT) {
            textPanel.performPaste();
        }
    }

    public void performSelectAll() {
        if (mode == PanelMode.TREE) {
            treePanel.performSelectAll();
        } else if (mode == PanelMode.TEXT) {
            textPanel.performSelectAll();
        } else if (mode == PanelMode.HEX) {
            hexPanel.performSelectAll();
        }
    }

    @Override
    public void performUndo() {
        try {
            if (mode == PanelMode.TREE) {
                treePanel.performUndo();
                updateItem();
            } else if (mode == PanelMode.TEXT) {
                getTreeUndo().performUndo();
            }
        } catch (Exception ex) {
            Logger.getLogger(XBDocumentPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void performRedo() {
        try {
            if (mode == PanelMode.TREE) {
                treePanel.performRedo();
                updateItem();
            } else if (mode == PanelMode.TEXT) {
                getTreeUndo().performRedo();
            }
        } catch (Exception ex) {
            Logger.getLogger(XBDocumentPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void performAdd() {
        treePanel.performAdd();
    }

    public void performDelete() {
        treePanel.performDelete();
    }

    public void setMode(PanelMode mode) {
        if (this.mode != mode) {
            switch (this.mode) {
                case TREE: break;
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
                    } catch (IOException ex) {
                        Logger.getLogger(XBDocumentPanel.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                }
                default: throw new InternalError("Unknown mode");
            }
            switch (mode) {
                case TREE: break;
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
                default: throw new InternalError("Unknown mode");
            }
            this.mode = mode;

            mainFrame.getEditFindAction().setEnabled(mode != PanelMode.TREE);
            mainFrame.getEditFindAgainAction().setEnabled(mode == PanelMode.TEXT);
            mainFrame.getEditGotoAction().setEnabled(mode == PanelMode.TEXT);
            mainFrame.getEditReplaceAction().setEnabled(mode == PanelMode.TEXT);
            mainFrame.getItemAddAction().setEnabled(false);
            mainFrame.getItemModifyAction().setEnabled(false);

            showPanel();
            updateActionStatus(null);
        }
    }

    public void showPanel() {
        JPanel cardPanel;
        if (splitMode) { cardPanel = splitPanel; } else {
            cardPanel = mainPanel;
        }
        switch (getMode()) {
            case TREE: {
                ((CardLayout) cardPanel.getLayout()).show(cardPanel, "tree");
                break;
            }
            case TEXT: {
                ((CardLayout) cardPanel.getLayout()).show(cardPanel, "text");
                break;
            }
            case HEX: {
                ((CardLayout) cardPanel.getLayout()).show(cardPanel, "hex");
                break;
            }
        }
    }

    private StringBuffer nodeAsText(XBTTreeNode node, String prefix) {
        StringBuffer result = new StringBuffer();
        result.append(prefix);
        if (node.getDataMode() == XBBlockDataMode.DATA_BLOCK) {
            result.append("[");
            byte[] data = node.getDataArray();
            for (int i = 0; i < data.length; i++) {
                byte b = data[i];
                result.append(getHex(b));
            }
            result.append("]\n");
        } else {
            result.append("<").append(getCaption(node));
            if (node.getAttributes()!=null) {
                if (node.getAttributesCount()>2) {
                    Iterator it = node.getAttributes().iterator();
                    int i = 1;
                    for (;it.hasNext();) {
                        UBNat32 attr = (UBNat32) it.next();
                        result.append(" ").append(i).append("=\"").append(attr.getLong()).append("\"");
                        i++;
                    }
                }
            }

            if (node.getChildren()!=null) {
                result.append(">\n");
                for (Iterator it = node.getChildren().iterator(); it.hasNext();) {
                    XBTTreeNode elem = (XBTTreeNode) it.next();
                    result.append(nodeAsText(elem, prefix + "  "));
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
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuItem popupItemCopyMenuItem;
    private javax.swing.JMenuItem popupItemPropertiesMenuItem;
    private javax.swing.JMenuItem popupItemViewMenuItem;
    private javax.swing.JPopupMenu popupMenu;
    private javax.swing.JPanel splitPanel;
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

    public XBTLinearUndo getTreeUndo() {
        return treePanel.getTreeUndo();
    }

    @Override
    public void loadFromFile() {
        try {
            getDoc().fromFileUB();
            getDoc().processSpec();
            reportStructureChange((XBTTreeNode) getDoc().getRootBlock());
            performSelectAll();
            getTreeUndo().clear();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(XBDocEditorFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XBDocEditorFrame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (XBProcessingException ex) {
            JOptionPane.showMessageDialog(getFrame(), ex.getMessage(), "Parse Exception", JOptionPane.ERROR_MESSAGE);
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

    public void showFontDialog(FontDialog dlg) {
        textPanel.showFontDialog(dlg);
    }

    @Override
    public void saveToFile() {
        try {
            getDoc().toFileUB();
            getTreeUndo().setSyncPoint();
            getDoc().setModified(false);
        } catch (IOException ex) {
            Logger.getLogger(XBDocumentPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void newFile() {
        getTreeUndo().clear();
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

    public void findText(FindDialog findDialog) {
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
        XBTCommand undoStep;
        try {
            ItemModifyDialog dialog = new ItemModifyDialog(getFrame(), true);
            dialog.setCatalog(catalog);
            dialog.setPluginRepository(pluginRepository);
            dialog.setLocationRelativeTo(dialog.getParent());
            XBTTreeNode newNode = dialog.runDialog(node);
            if (newNode != null) {
                if (node.getDataMode() == XBBlockDataMode.DATA_BLOCK) {
                    undoStep = new XBTModDataBlockCommand(node, newNode);
                } else if (newNode.getChildCount()>0) {
                    undoStep = new XBTModifyBlockCommand(node, newNode);
                } else {
                    undoStep = new XBTModAttrBlockCommand(node, newNode);
                }
                getTreeUndo().performStep(undoStep);
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
            mainPanel.add(treePanel,"tree");
            mainPanel.add(hexPanel,"hex");
            mainPanel.add(textPanel,"text");
        } else {
            ((CardLayout) getLayout()).show(this, "split");
            splitPanel.add(treePanel,"tree");
            splitPanel.add(hexPanel,"hex");
            splitPanel.add(textPanel,"text");
        }
        showPanel();
    }

    public boolean isSplitMode() {
        return splitMode;
    }

    /**
     * @return the mode
     */
    public PanelMode getMode() {
        return mode;
    }

    /**
     * @return the pluginRepository
     */
    public XBPluginRepository getPluginRepository() {
        return pluginRepository;
    }

    /**
     * @param pluginRepository the pluginRepository to set
     */
    public void setPluginRepository(XBPluginRepository pluginRepository) {
        this.pluginRepository = pluginRepository;
    }

    public String getHex(byte b) {
        byte low = (byte) (b & 0xf);
        byte hi = (byte) (b >> 0x8);
        return (Integer.toHexString(hi)+Integer.toHexString(low)).toUpperCase();
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
                XBCBlockSpec blockSpec = blockDecl.getBlockSpec().getParent();
                return nameService.getDefaultCaption(blockSpec);
            }
        }
        return "Unknown" + " (" + Integer.toString(((XBFBlockType) blockType).getGroupID().getInt()) + ", "+ Integer.toString(((XBFBlockType) blockType).getBlockID().getInt())+")";
    }

    public void testParamList() {
        XBParamConvertor convertor = new XBParamConvertor(new XBParamListener() {

            @Override
            public void beginXBParam(XBParamDecl type) throws XBProcessingException, IOException {
                System.out.println("Param begin");
            }

            @Override
            public void blockXBParam() throws XBProcessingException, IOException {
                System.out.println("Param block");
            }

            @Override
            public void listXBParam() throws XBProcessingException, IOException {
                System.out.println("Param list");
            }

            @Override
            public void endXBParam() throws XBProcessingException, IOException {
                System.out.println("Param end");
            }
        }, catalog);

        XBTTreeNode node = getSelectedItem();
        if (node != null) {
            try {
                XBTTreeWriter writer = new XBTTreeWriter(node);
                writer.produceXBT(convertor);
            } catch (XBProcessingException | IOException ex) {
                Logger.getLogger(XBDocumentPanel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public String getPanelName() {
        return "XBDocPanel";
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
    public Boolean canUndo() {
        if (getTreeUndo() == null) {
            return false;
        }
        return getTreeUndo().canUndo();
    }

    @Override
    public Boolean canRedo() {
        if (getTreeUndo() == null) {
            return false;
        }
        return getTreeUndo().canRedo();
    }

    @Override
    public boolean updateActionStatus(Component component) {
        switch (mode) {
            case TREE: return treePanel.updateActionStatus(component);
            case TEXT: {
                return false;
            }
            case HEX: {
                return false;
            }
        }

        return false;
    }

    @Override
    public void releaseActionStatus() {
        switch (mode) {
            case TREE: {
                treePanel.releaseActionStatus();
                break;
            }
        }
    }

    @Override
    public boolean performAction(String eventName, ActionEvent event) {
        switch (mode) {
            case TREE: return treePanel.performAction(eventName, event);
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
            } while (newpos>0);
            return getDoc().getFileName().substring(pos) + " - " + frameTitle;
        }

        return frameTitle;
    }

    public Font getCurrentFont() {
        return textPanel.getCurrentFont();
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
        ItemPropertiesDialog dialog = new ItemPropertiesDialog(getFrame(),true);
        dialog.setCatalog(catalog);
        dialog.runDialog(getSelectedItem());
    }

    private Frame getFrame() {
        Component component = SwingUtilities.getWindowAncestor(this);
        while (!(component == null || component instanceof Frame)) {
            component = component.getParent();
        }
        return (Frame) component;
    }
}
