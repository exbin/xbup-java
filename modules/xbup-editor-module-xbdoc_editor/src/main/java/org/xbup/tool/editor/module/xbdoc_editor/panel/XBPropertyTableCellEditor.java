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
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;
import javax.swing.JTable;
import javax.swing.event.CellEditorListener;
import javax.swing.table.TableCellEditor;
import org.xbup.lib.core.catalog.XBACatalog;
import org.xbup.lib.parser_tree.XBTTreeNode;

/**
 * Property Table Cell Renderer.
 *
 * @version 0.1.24 2014/11/10
 * @author XBUP Project (http://xbup.org)
 */
public class XBPropertyTableCellEditor implements TableCellEditor {

    private final List<CellEditorListener> cellEditorListeners;
    private XBACatalog catalog;
    private XBTTreeNode node;

    public XBPropertyTableCellEditor(XBACatalog catalog, XBTTreeNode node) {
        this.catalog = catalog;
        this.node = node;
        cellEditorListeners = new ArrayList<>();
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        return new XBPropertyTableCellPanel(catalog, node);
    }

    @Override
    public Object getCellEditorValue() {
        return "";
    }

    @Override
    public boolean isCellEditable(EventObject anEvent) {
        return true;
    }

    @Override
    public boolean shouldSelectCell(EventObject anEvent) {
        return false;
    }

    @Override
    public boolean stopCellEditing() {
        return true;
    }

    @Override
    public void cancelCellEditing() {
    }

    @Override
    public void addCellEditorListener(CellEditorListener l) {
        cellEditorListeners.add(l);
    }

    @Override
    public void removeCellEditorListener(CellEditorListener l) {
        cellEditorListeners.remove(l);
    }

    public void setCatalog(XBACatalog catalog) {
        this.catalog = catalog;
    }

    public void setNode(XBTTreeNode node) {
        this.node = node;
    }
}
