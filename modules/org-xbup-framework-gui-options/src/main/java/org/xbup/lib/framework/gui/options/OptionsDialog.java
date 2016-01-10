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
package org.xbup.lib.framework.gui.options;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.AbstractPreferences;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import javax.swing.JPanel;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import org.xbup.lib.framework.gui.api.XBApplication;
import org.xbup.lib.framework.gui.options.panel.AppearanceOptionsPanel;
import org.xbup.lib.framework.gui.options.panel.MainOptionsPanel;
import org.xbup.lib.framework.gui.options.panel.OptionsPanel;
import org.xbup.lib.framework.gui.options.panel.OptionsPanel.ModifiedOptionListener;
import org.xbup.lib.framework.gui.options.panel.OptionsPanel.PathItem;
import org.xbup.lib.framework.gui.utils.WindowUtils;
import org.xbup.lib.framework.gui.frame.api.ApplicationFrameHandler;

/**
 * Dialog for application options and preferences setting.
 *
 * @version 0.2.0 2015/11/05
 * @author XBUP Project (http://xbup.org)
 */
public class OptionsDialog extends javax.swing.JDialog {

    private Preferences preferences = null;
    private java.util.ResourceBundle resourceBundle;
    private Map<String, JPanel> optionPanels;
    private JPanel currentOptionsPanel = null;
    private ModifiedOptionListener modifiedOptionListener;

    private boolean modified;
    private OptionsMutableTreeNode top;
    private XBApplication appEditor;
    private final ApplicationFrameHandler frame;
    private MainOptionsPanel mainOptionsPanel;
    private AppearanceOptionsPanel appearanceOptionsPanel;

    public OptionsDialog(java.awt.Frame parent, boolean modal, ApplicationFrameHandler frame) {
        super(parent, modal);
        this.frame = frame;
        init();
    }

    private void init() {
        resourceBundle = java.util.ResourceBundle.getBundle("org/xbup/lib/framework/gui/options/resources/OptionsDialog");
        initComponents();

        optionPanels = new HashMap<>();
        modified = false;
        modifiedOptionListener = new ModifiedOptionListener() {

            @Override
            public void wasModified() {
                setModified(true);
            }
        };

        addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("modified".equals(evt.getPropertyName())) {
                    modified = true;
                }
            }
        });

        // Create menu tree
        top = new OptionsMutableTreeNode(resourceBundle.getString("options_options"), "options");
        createNodes(top);
        optionsTree.setModel(new DefaultTreeModel(top, true));
        optionsTree.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                if (e.getPath() != null) {
                    String caption;
                    OptionsMutableTreeNode node = ((OptionsMutableTreeNode) optionsTree.getLastSelectedPathComponent());
                    if (node == null) {
                        caption = null;
                        optionsAreaTitleLabel.setText("");
                    } else {
                        caption = node.getName();
                        optionsAreaTitleLabel.setText(" " + (String) node.getUserObject());
                    }
                    if (currentOptionsPanel != null) {
                        optionsAreaScrollPane.remove(currentOptionsPanel);
                    }
                    if (caption != null) {
                        currentOptionsPanel = optionPanels.get(caption);
                        optionsAreaScrollPane.setViewportView(currentOptionsPanel);
                    } else {
                        currentOptionsPanel = null;
                        optionsAreaScrollPane.setViewportView(null);
                    }
                }
            }
        });

        mainOptionsPanel = new MainOptionsPanel(frame);
        addOptionsPanel(mainOptionsPanel);
        appearanceOptionsPanel = new AppearanceOptionsPanel(frame);
        addOptionsPanel(appearanceOptionsPanel);

        optionsTree.setSelectionRow(0);

        // Expand all nodes
        expandJTree(optionsTree, -1);

        WindowUtils.initWindow(this);
        WindowUtils.assignGlobalKeyListener(this, null, cancelButton);
    }

    public void loadPreferences(Preferences preferences) {
        for (Iterator optionPanelsIterator = optionPanels.values().iterator(); optionPanelsIterator.hasNext();) {
            Object optionPanel = optionPanelsIterator.next();
            if (optionPanel instanceof OptionsPanel) {
                ((OptionsPanel) optionPanel).loadFromPreferences(preferences);
            }
        }
    }

    public void savePreferences(Preferences preferences) {
        try {
            for (Iterator optionPanelsIterator = optionPanels.values().iterator(); optionPanelsIterator.hasNext();) {
                Object optionPanel = optionPanelsIterator.next();
                if (optionPanel instanceof OptionsPanel) {
                    ((OptionsPanel) optionPanel).saveToPreferences(preferences);
                }
            }

            preferences.flush();
        } catch (BackingStoreException ex) {
            Logger.getLogger(OptionsDialog.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void applyPreferencesChanges() {
        for (Iterator optionPanelsIterator = optionPanels.values().iterator(); optionPanelsIterator.hasNext();) {
            Object optionPanel = optionPanelsIterator.next();
            if (optionPanel instanceof OptionsPanel) {
                ((OptionsPanel) optionPanel).applyPreferencesChanges();
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

        optionsSplitPane = new javax.swing.JSplitPane();
        optionsTreeScrollPane = new javax.swing.JScrollPane();
        optionsTree = new javax.swing.JTree();
        optionsTreePanel = new javax.swing.JPanel();
        optionsAreaScrollPane = new javax.swing.JScrollPane();
        optionsAreaTitlePanel = new javax.swing.JPanel();
        optionsAreaTitleLabel = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        cancelButton = new javax.swing.JButton();
        applyButton = new javax.swing.JButton();
        saveButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/xbup/lib/framework/gui/options/resources/OptionsDialog"); // NOI18N
        setTitle(bundle.getString("OptionsDialog.title")); // NOI18N

        optionsSplitPane.setDividerLocation(130);
        optionsSplitPane.setName("optionsSplitPane"); // NOI18N

        optionsTreeScrollPane.setName("optionsTreeScrollPane"); // NOI18N

        optionsTree.setName("optionsTree"); // NOI18N
        optionsTreeScrollPane.setViewportView(optionsTree);

        optionsSplitPane.setLeftComponent(optionsTreeScrollPane);

        optionsTreePanel.setName("optionsTreePanel"); // NOI18N
        optionsTreePanel.setLayout(new java.awt.BorderLayout());

        optionsAreaScrollPane.setName("optionsAreaScrollPane"); // NOI18N
        optionsTreePanel.add(optionsAreaScrollPane, java.awt.BorderLayout.CENTER);

        optionsAreaTitlePanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        optionsAreaTitlePanel.setName("optionsAreaTitlePanel"); // NOI18N
        optionsAreaTitlePanel.setLayout(new java.awt.BorderLayout());

        optionsAreaTitleLabel.setBackground(javax.swing.UIManager.getDefaults().getColor("EditorPane.selectionBackground"));
        optionsAreaTitleLabel.setForeground(javax.swing.UIManager.getDefaults().getColor("EditorPane.background"));
        optionsAreaTitleLabel.setText(bundle.getString("options_options")); // NOI18N
        optionsAreaTitleLabel.setName("optionsAreaTitleLabel"); // NOI18N
        optionsAreaTitleLabel.setOpaque(true);
        optionsAreaTitleLabel.setVerifyInputWhenFocusTarget(false);
        optionsAreaTitlePanel.add(optionsAreaTitleLabel, java.awt.BorderLayout.NORTH);

        optionsTreePanel.add(optionsAreaTitlePanel, java.awt.BorderLayout.NORTH);

        optionsSplitPane.setRightComponent(optionsTreePanel);

        jSeparator1.setName("jSeparator1"); // NOI18N

        cancelButton.setText(bundle.getString("cancelButton.text")); // NOI18N
        cancelButton.setName("cancelButton"); // NOI18N
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        applyButton.setText(bundle.getString("applyButton.text")); // NOI18N
        applyButton.setName("applyButton"); // NOI18N
        applyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                applyButtonActionPerformed(evt);
            }
        });

        saveButton.setText(bundle.getString("saveButton.text")); // NOI18N
        saveButton.setName("saveButton"); // NOI18N
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(648, Short.MAX_VALUE)
                .addComponent(saveButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(applyButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cancelButton)
                .addContainerGap())
            .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 591, Short.MAX_VALUE)
            .addComponent(optionsSplitPane, javax.swing.GroupLayout.DEFAULT_SIZE, 591, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(optionsSplitPane, javax.swing.GroupLayout.DEFAULT_SIZE, 432, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelButton)
                    .addComponent(applyButton)
                    .addComponent(saveButton))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        setVisible(false);
        dispose();
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void applyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_applyButtonActionPerformed
        applyPreferencesChanges();

        setModified(false);
    }//GEN-LAST:event_applyButtonActionPerformed

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        savePreferences(preferences);
        dispose();
    }//GEN-LAST:event_saveButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        OptionsDialog optionsDialog = new OptionsDialog(new javax.swing.JFrame(), true, null);
        optionsDialog.setPreferences(optionsDialog.new OptionsPreferences());
        WindowUtils.invokeWindow(optionsDialog);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton applyButton;
    private javax.swing.JButton cancelButton;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JScrollPane optionsAreaScrollPane;
    private javax.swing.JLabel optionsAreaTitleLabel;
    private javax.swing.JPanel optionsAreaTitlePanel;
    private javax.swing.JSplitPane optionsSplitPane;
    private javax.swing.JTree optionsTree;
    private javax.swing.JPanel optionsTreePanel;
    private javax.swing.JScrollPane optionsTreeScrollPane;
    private javax.swing.JButton saveButton;
    // End of variables declaration//GEN-END:variables

    private void createNodes(DefaultMutableTreeNode top) {
        DefaultMutableTreeNode item;
        item = new OptionsMutableTreeNode(resourceBundle.getString("options_apperance"), "apperance");
        top.add(item);
    }

    /**
     * @param modified the modified to set
     */
    public void setModified(boolean modified) {
        this.modified = modified;
        applyButton.setEnabled(modified);
    }

    public void addOptionsPanel(OptionsPanel optionPanel) {
        String panelKey;
        if (optionPanel.getPath() == null) {
            panelKey = "options";
        } else {
            panelKey = optionPanel.getPath().get(optionPanel.getPath().size() - 1).getName();
            estabilishPath(optionPanel.getPath());
        }
        optionPanels.put(panelKey, (JPanel) optionPanel);
        optionPanel.setModifiedOptionListener(modifiedOptionListener);
        optionsTree.setSelectionRow(0);
    }

    public void extendMainOptionsPanel(OptionsPanel panel) {
        mainOptionsPanel.addExtendedPanel(panel);
    }

    public void extendAppearanceOptionsPanel(OptionsPanel panel) {
        appearanceOptionsPanel.addExtendedPanel(panel);
    }

    private void estabilishPath(List<PathItem> path) {
        OptionsMutableTreeNode node = top;
        for (PathItem pathItem : path) {
            int childIndex = 0;
            OptionsMutableTreeNode child = null;
            if (node == null) {
                return;
            }

            while ((childIndex >= 0) && (childIndex < node.getChildCount())) {
                child = (OptionsMutableTreeNode) node.getChildAt(childIndex);
                if (child.getName().equals(pathItem.getName())) {
                    break;
                } else {
                    childIndex++;
                }
            }

            if (childIndex == node.getChildCount()) {
                OptionsMutableTreeNode newNode = new OptionsMutableTreeNode(pathItem.getCaption(), pathItem.getName());
                node.add(newNode);
                node = newNode;
            } else {
                node = child;
            }
        }

        optionsTree.setModel(new DefaultTreeModel(top, true));
        for (int i = 0; i < optionsTree.getRowCount(); i++) {
            optionsTree.expandRow(i);
        }
    }

    public void setPreferences(Preferences preferences) {
        this.preferences = preferences;
    }

    public XBApplication getAppEditor() {
        return appEditor;
    }

    public void setAppEditor(XBApplication appEditor) {
        this.appEditor = appEditor;
        setIconImage(appEditor.getApplicationIcon());
    }

    private class OptionsMutableTreeNode extends DefaultMutableTreeNode {

        private final String name;

        public OptionsMutableTreeNode(Object userObject, String name) {
            super(userObject);
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    private class OptionsPreferences extends AbstractPreferences {

        private final Map<String, String> spiValues;

        public OptionsPreferences() {
            super(null, "");
            spiValues = new HashMap<>();
        }

        @Override
        protected void putSpi(String key, String value) {
            spiValues.put(key, value);
        }

        @Override
        protected String getSpi(String key) {
            return spiValues.get(key);
        }

        @Override
        protected void removeSpi(String key) {
            spiValues.remove(key);
        }

        @Override
        protected void removeNodeSpi() throws BackingStoreException {
            throw new UnsupportedOperationException("Can't remove the root!");
        }

        @Override
        protected String[] keysSpi() throws BackingStoreException {
            return (String[]) spiValues.keySet().toArray(new String[0]);
        }

        @Override
        protected String[] childrenNamesSpi() throws BackingStoreException {
            return new String[0];
        }

        @Override
        protected AbstractPreferences childSpi(String name) {
            return null;
        }

        @Override
        protected void syncSpi() throws BackingStoreException {
        }

        @Override
        protected void flushSpi() throws BackingStoreException {
        }
    };

    /**
     * Expands all nodes in a JTree.
     *
     * @param tree The JTree to expand.
     * @param depth The depth to which the tree should be expanded. Zero will
     * just expand the root node, a negative value will fully expand the tree,
     * and a positive value will recursively expand the tree to that depth.
     */
    public static void expandJTree(javax.swing.JTree tree, int depth) {
        javax.swing.tree.TreeModel model = tree.getModel();
        expandJTreeNode(tree, model, model.getRoot(), 0, depth);
    } // expandJTree()

    /**
     * Expands a given node in a JTree.
     *
     * @param tree The JTree to expand.
     * @param model The TreeModel for tree.
     * @param node The node within tree to expand.
     * @param row The displayed row in tree that represents node.
     * @param depth The depth to which the tree should be expanded. Zero will
     * just expand node, a negative value will fully expand the tree, and a
     * positive value will recursively expand the tree to that depth relative to
     * node.
     * @return row
     */
    public static int expandJTreeNode(javax.swing.JTree tree,
            javax.swing.tree.TreeModel model,
            Object node, int row, int depth) {
        if (node != null && !model.isLeaf(node)) {
            tree.expandRow(row);
            if (depth != 0) {
                for (int index = 0;
                        row + 1 < tree.getRowCount()
                        && index < model.getChildCount(node);
                        index++) {
                    row++;
                    Object child = model.getChild(node, index);
                    if (child == null) {
                        break;
                    }
                    javax.swing.tree.TreePath path;
                    while ((path = tree.getPathForRow(row)) != null
                            && path.getLastPathComponent() != child) {
                        row++;
                    }
                    if (path == null) {
                        break;
                    }
                    row = expandJTreeNode(tree, model, child, row, depth - 1);
                }
            }
        }
        return row;
    } // expandJTreeNode()

    @Override
    public void setVisible(boolean visibility) {
        if (visibility) {
            loadPreferences(preferences);
        }

        super.setVisible(visibility);
    }
}
