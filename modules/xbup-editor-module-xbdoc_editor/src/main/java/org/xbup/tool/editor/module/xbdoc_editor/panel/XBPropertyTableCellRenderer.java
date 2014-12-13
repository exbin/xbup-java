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

import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import org.xbup.lib.core.catalog.XBACatalog;
import org.xbup.lib.parser_tree.XBTTreeNode;

/**
 * Property Table Cell Renderer.
 *
 * @version 0.1.24 2014/12/13
 * @author XBUP Project (http://xbup.org)
 */
public class XBPropertyTableCellRenderer implements TableCellRenderer {

    private XBACatalog catalog;
    private XBTTreeNode node;

    public XBPropertyTableCellRenderer(XBACatalog catalog, XBTTreeNode node) {
        this.catalog = catalog;
        this.node = node;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        XBPropertyTableCellPanel cellPanel = new XBPropertyTableCellPanel(catalog, node);
        cellPanel.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
        cellPanel.getCellComponent().setBorder(null);
        return cellPanel;
    }

    public void setCatalog(XBACatalog catalog) {
        this.catalog = catalog;
    }

    public void setNode(XBTTreeNode node) {
        this.node = node;
    }
}
