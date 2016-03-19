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

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.swing.table.AbstractTableModel;
import org.exbin.framework.gui.utils.ActionUtils;

/**
 * Parameters list table model for item editing.
 *
 * @version 0.2.0 2016/02/01
 * @author ExBin Project (http://exbin.org)
 */
public class XBPropertyTableModel extends AbstractTableModel {

    private final ResourceBundle resourceBundle = ActionUtils.getResourceBundleByClass(XBPropertyTablePanel.class);
    private List<XBPropertyTableItem> parameters;

    private final String[] columnNames;
    private Class[] columnTypes = new Class[]{
        java.lang.String.class, java.lang.Object.class
    };
    private final boolean[] columnsEditable = new boolean[]{false, true};

    public XBPropertyTableModel() {
        columnNames = new String[]{"Property", "Value"};
        parameters = new ArrayList<>();
    }

    @Override
    public int getRowCount() {
        return parameters.size();
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
                return getRow(rowIndex).getValueName();
            case 1:
                return null;
            default:
                return "";
        }
    }

    public XBPropertyTableItem getRow(int rowIndex) {
        return parameters.get(rowIndex);
    }

    public void removeRow(int rowIndex) {
        parameters.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }

    public void addRow(XBPropertyTableItem rowData) {
        parameters.add(rowData);
        fireTableRowsInserted(parameters.size() - 1, parameters.size() - 1);
    }

    public List<XBPropertyTableItem> getParameters() {
        return parameters;
    }

    public void setParameters(List<XBPropertyTableItem> attributes) {
        this.parameters = attributes;
    }

    public Class[] getTypes() {
        return columnTypes;
    }

    public void setTypes(Class[] types) {
        this.columnTypes = types;
    }
}
