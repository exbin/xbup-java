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
package org.exbin.framework.gui.service.catalog.panel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import org.xbup.lib.core.catalog.XBACatalog;
import org.xbup.lib.core.catalog.base.XBCItem;
import org.xbup.lib.core.catalog.base.XBCNode;
import org.xbup.lib.core.catalog.base.service.XBCXNameService;
import org.exbin.framework.gui.service.catalog.dialog.CatalogSelectNodeDialog;
import org.xbup.lib.framework.gui.utils.WindowUtils;

/**
 * Catalog Parent Cell Panel.
 *
 * @version 0.2.0 2016/02/01
 * @author ExBin Project (http://exbin.org)
 */
public class CatalogParentPropertyTableCellPanel extends PropertyTableCellPanel {

    private XBACatalog catalog;
    private XBCNode parent;

    public CatalogParentPropertyTableCellPanel(XBACatalog catalog) {
        super();
        this.catalog = catalog;
        setEditorAction(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                performEditorAction();
            }
        });
    }

    public void performEditorAction() {
        if (parent == null) {
            JOptionPane.showMessageDialog(this, "You cannot move root node", "Editing not allowed", JOptionPane.ERROR_MESSAGE);
            return;
        }

        CatalogSelectNodeDialog nodeDialog = new CatalogSelectNodeDialog(WindowUtils.getFrame(this), true, catalog, parent);
        nodeDialog.setLocationRelativeTo(this);
        nodeDialog.setVisible(true);

        if (nodeDialog.getDialogOption() == JOptionPane.OK_OPTION) {
            parent = nodeDialog.getNode();
            setNodeLabel();
        }
    }

    public void setCatalogItem(XBCItem catalogItem) {
        parent = (XBCNode) catalogItem.getParent();
        setNodeLabel();
    }

    private void setNodeLabel() {
        XBCXNameService nameService = (XBCXNameService) catalog.getCatalogService(XBCXNameService.class);
        String targetCaption = parent == null ? null : nameService.getItemNamePath(parent);
        if (targetCaption == null) {
            targetCaption = "";
        } else {
            targetCaption += " ";
        }

        if (parent != null) {
            targetCaption += "(" + Long.toString(parent.getId()) + ")";
        }

        setPropertyText(targetCaption);
    }

    public XBCNode getParentNode() {
        return parent;
    }

    public XBACatalog getCatalog() {
        return catalog;
    }

    public void setCatalog(XBACatalog catalog) {
        this.catalog = catalog;
    }
}
