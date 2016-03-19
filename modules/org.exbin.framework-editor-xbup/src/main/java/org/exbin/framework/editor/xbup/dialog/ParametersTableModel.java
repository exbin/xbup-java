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
package org.exbin.framework.editor.xbup.dialog;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.swing.table.AbstractTableModel;
import org.exbin.framework.gui.utils.ActionUtils;

/**
 * Parameters list table model for item editing.
 *
 * @version 0.1.24 2015/01/10
 * @author ExBin Project (http://exbin.org)
 */
public class ParametersTableModel extends AbstractTableModel {

    private final ResourceBundle resourceBundle;
    private List<ParametersTableItem> parameters;

    private final String[] columnNames;
    private Class[] columnTypes = new Class[]{
        java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class
    };
    private final boolean[] columnsEditable = new boolean[]{false, false, false, true};

    public ParametersTableModel() {
        resourceBundle = ActionUtils.getResourceBundleByClass(ModifyBlockDialog.class);
        columnNames = new String[]{resourceBundle.getString("parametersTableModel.itemOrder"), resourceBundle.getString("parametersTableModel.itemName"), resourceBundle.getString("parametersTableModel.itemType"), resourceBundle.getString("parametersTableModel.itemValue")};
        parameters = new ArrayList<>();
    }

    @Override
    public int getRowCount() {
        return parameters.size();
    }

    public ParametersTableItem getRow(int index) {
        return parameters.get(index);
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columnNames[columnIndex];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return getTypes()[columnIndex];
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnsEditable[columnIndex];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return rowIndex;
            case 1:
                return getParameter(rowIndex).getValueName();
            case 2:
                return getParameter(rowIndex).getTypeName();
            case 3:
                return "";
            default:
                return "";
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (rowIndex < getRowCount()) {
            if (columnIndex == 3) {
                // ((UBNat32) parameters.get(rowIndex)).setValue((Integer) aValue);
            } else {
                throw new IllegalStateException();
            }
        }
    }

    public List<ParametersTableItem> getParameters() {
        return parameters;
    }

    public void setParameters(List<ParametersTableItem> attributes) {
        this.parameters = attributes;
    }

    public Class[] getTypes() {
        return columnTypes;
    }

    public void setTypes(Class[] types) {
        this.columnTypes = types;
    }

    public ParametersTableItem getParameter(int index) {
        if (index >= parameters.size()) {
            return null;
        }

        return parameters.get(index);
    }
    
    public void clear() {
        parameters.clear();
    }
    
    public void addRow(ParametersTableItem item) {
        parameters.add(item);
    }

    public boolean isEmpty() {
        return parameters == null || parameters.isEmpty();
    }
}
