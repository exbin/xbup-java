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
package org.xbup.lib.framework.service_manager.catalog.panel;

import javax.swing.table.AbstractTableModel;
import org.xbup.lib.core.catalog.base.XBCBlockCons;
import org.xbup.lib.core.catalog.base.XBCBlockJoin;
import org.xbup.lib.core.catalog.base.XBCBlockListCons;
import org.xbup.lib.core.catalog.base.XBCBlockListJoin;
import org.xbup.lib.core.catalog.base.XBCConsDef;
import org.xbup.lib.core.catalog.base.XBCJoinDef;
import org.xbup.lib.core.catalog.base.XBCSpecDef;

/**
 * Table model for catalog definition bindings.
 *
 * @version 0.2.0 2016/02/01
 * @author XBUP Project (http://xbup.org)
 */
public class CatalogDefsDetailTableModel extends AbstractTableModel {

    private CatalogDefsTableItem item = null;

    private final String[] columnNames = new String[]{"Property", "Value"};
    private final String[] rowProperties = new String[]{"Name", "Description", "Type", "Type Revision", "Operation", "StringId"};
    private final Class[] columnClasses = new Class[]{
        java.lang.String.class, java.lang.Object.class
    };

    public CatalogDefsDetailTableModel() {
    }

    @Override
    public int getRowCount() {
        if (item == null) {
            return 0;
        }
        return 6;
    }

    @Override
    public int getColumnCount() {
        return columnClasses.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (item == null) {
            return null;
        }

        switch (columnIndex) {
            case 0:
                return rowProperties[rowIndex];
            case 1: {
                switch (rowIndex) {
                    case 0:
                        return item.getName();
                    case 1:
                        return item.getDescription();
                    case 2:
                        return item.getType();
                    case 3:
                        return item.getTargetRevision();
                    case 4:
                        return item.getOperation();
                    case 5:
                        return item.getStringId();
                }

            }

            default:
                return "";
        }
    }

    public CatalogDefsTableItem getItem() {
        return item;
    }

    public void setItem(CatalogDefsTableItem item) {
        this.item = item;
        fireTableDataChanged();
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columnNames[columnIndex];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return columnClasses[columnIndex];
    }

    public String getOperation(XBCSpecDef specDef) {
        CatalogDefOperationType operation;
        if (specDef instanceof XBCBlockJoin) {
            operation = specDef.getTarget() == null
                    ? CatalogDefOperationType.ATTRIBUTE : CatalogDefOperationType.JOIN;
        } else if (specDef instanceof XBCBlockCons) {
            operation = specDef.getTarget() == null
                    ? CatalogDefOperationType.ANY : CatalogDefOperationType.CONSIST;
        } else if (specDef instanceof XBCBlockListJoin) {
            operation = specDef.getTarget() == null
                    ? CatalogDefOperationType.ATTRIBUTE_LIST : CatalogDefOperationType.JOIN_LIST;
        } else if (specDef instanceof XBCBlockListCons) {
            operation = specDef.getTarget() == null
                    ? CatalogDefOperationType.ANY_LIST : CatalogDefOperationType.CONSIST_LIST;
        } else if (specDef instanceof XBCJoinDef) {
            operation = CatalogDefOperationType.JOIN;
        } else if (specDef instanceof XBCConsDef) {
            operation = CatalogDefOperationType.CONSIST;
        } else {
            return "Unknown";
        }

        return operation.getCaption();
    }
}
