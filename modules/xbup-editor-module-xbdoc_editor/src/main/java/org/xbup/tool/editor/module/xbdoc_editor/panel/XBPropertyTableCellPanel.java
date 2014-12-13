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

import org.xbup.tool.editor.module.xbdoc_editor.panel.cell.PropertyTableCellPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComponent;
import org.xbup.lib.core.catalog.XBACatalog;
import org.xbup.lib.parser_tree.XBTTreeNode;
import org.xbup.tool.editor.base.api.utils.WindowUtils;
import org.xbup.tool.editor.module.xbdoc_editor.dialog.XBPropertyDialog;

/**
 * Properties table cell panel.
 *
 * @version 0.1.24 2014/12/13
 * @author XBUP Project (http://xbup.org)
 */
public class XBPropertyTableCellPanel extends PropertyTableCellPanel {

    private XBACatalog catalog;
    private XBTTreeNode node;

    public XBPropertyTableCellPanel(JComponent cellComponent, XBACatalog catalog, XBTTreeNode node) {
        super(cellComponent);

        this.catalog = catalog;
        this.node = node;
        init();
    }

    public XBPropertyTableCellPanel(XBACatalog catalog, XBTTreeNode node) {
        super();

        this.catalog = catalog;
        this.node = node;
        init();
    }

    private void init() {
        setEditorAction(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                performEditorAction();
            }
        });
    }

    public void performEditorAction() {
        XBPropertyDialog propertyDialog = new XBPropertyDialog(WindowUtils.getFrame(this), true);
        propertyDialog.setCatalog(catalog);
        propertyDialog.setLocationRelativeTo(propertyDialog.getParent());

        // TODO Fix parameters processing
        /*XBTTreeNode newNode = (XBTTreeNode) node.getParam(catalog, getParamIndex());
         propertyDialog.setRootBlock(newNode);
         propertyDialog.getDocumentPanel().reportStructureChange(newNode);
         propertyDialog.getDocumentPanel().updateItem(); */
        propertyDialog.setVisible(true);
    }

    public XBACatalog getCatalog() {
        return catalog;
    }

    public void setCatalog(XBACatalog catalog) {
        this.catalog = catalog;
    }

    public XBTTreeNode getNode() {
        return node;
    }

    public void setNode(XBTTreeNode node) {
        this.node = node;
    }
}
