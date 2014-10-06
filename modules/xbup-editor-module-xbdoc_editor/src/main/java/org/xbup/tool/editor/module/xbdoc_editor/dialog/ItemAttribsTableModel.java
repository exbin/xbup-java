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
package org.xbup.tool.editor.module.xbdoc_editor.dialog;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import org.xbup.lib.core.ubnumber.UBNatural;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * Attributes list table model for item editing.
 *
 * @version 0.1.24 2014/10/07
 * @author XBUP Project (http://xbup.org)
 */
public class ItemAttribsTableModel extends AbstractTableModel {

    private final ResourceBundle resourceBundle;
    private List<UBNatural> attributes;

    private final String[] columns;
    private Class[] types = new Class[]{
        java.lang.Integer.class, java.lang.Integer.class
    };
    private final boolean[] canEdit = new boolean[]{false, true};
    private List<TableModelListener> tableModelListeners = new ArrayList<>();

    public ItemAttribsTableModel() {
        resourceBundle = java.util.ResourceBundle.getBundle("org/xbup/tool/xbeditor/module/xbdoceditor/dialog/resources/ItemModifyDialog");
        columns = new String[]{resourceBundle.getString("itemmod_itemorder"), resourceBundle.getString("itemmod_itemvalue")};
        attributes = new ArrayList<>();
    }

    @Override
    public int getRowCount() {
        return attributes.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columns[columnIndex];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return getTypes()[columnIndex];
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return canEdit[columnIndex];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex == 1) {
            return ((UBNat32) getAttribs().get(rowIndex)).getInt();
        } else {
            return rowIndex;
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (columnIndex == 1) {
            ((UBNat32) attributes.get(rowIndex)).setValue((Integer) aValue);
        } else {
            throw new IllegalStateException();
        }
    }

    public List<UBNatural> getAttribs() {
        return attributes;
    }

    public void setAttribs(List<UBNatural> attributes) {
        this.attributes = attributes;
    }

    public Class[] getTypes() {
        return types;
    }

    public void setTypes(Class[] types) {
        this.types = types;
    }

    @Override
    public void addTableModelListener(TableModelListener listener) {
        tableModelListeners.add(listener);
    }

    @Override
    public void removeTableModelListener(TableModelListener listener) {
        tableModelListeners.remove(listener);
    }

    public int getAttribute(int index) {
        if (index >= attributes.size()) {
            return 0;
        }

        UBNat32 attribute = (UBNat32) attributes.get(index);
        return attribute != null ? attribute.getInt() : 0;
    }
}
