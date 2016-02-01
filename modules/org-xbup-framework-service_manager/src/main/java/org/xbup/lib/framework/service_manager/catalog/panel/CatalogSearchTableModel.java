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

/**
 * Table model for catalog specifications.
 *
 * @version 0.1.24 2014/12/10
 * @author XBUP Project (http://xbup.org)
 */
public class CatalogSearchTableModel extends AbstractTableModel {

    private CatalogSearchTableItem searchConditions = new CatalogSearchTableItem();

    public CatalogSearchTableModel() {
        searchConditions = new CatalogSearchTableItem();
    }

    @Override
    public int getRowCount() {
        return 1;
    }

    @Override
    public int getColumnCount() {
        return CatalogItemsTableModel.columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0: {
                return searchConditions.getName();
            }
            case 1: {
                return searchConditions.getStringId();
            }
            case 2: {
                return searchConditions.getType();
            }
            case 3: {
                return searchConditions.getDescription();
            }
        }
        return "";
    }

    @Override
    public String getColumnName(int columnIndex) {
        return CatalogItemsTableModel.columnNames[columnIndex];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return CatalogItemsTableModel.columnClasses[columnIndex];
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0: {
                searchConditions.setName((String) aValue);
                break;
            }
            case 1: {
                searchConditions.setStringId((String) aValue);
                break;
            }
            case 2: {
                searchConditions.setType((String) aValue);
                break;
            }
            case 3: {
                searchConditions.setDescription((String) aValue);
                break;
            }
        }
    }

    public CatalogSearchTableItem getSearchConditions() {
        return searchConditions;
    }

    public void setSearchConditions(CatalogSearchTableItem searchConditions) {
        this.searchConditions = searchConditions;
    }

    public class CatalogSearchTableItem {

        private String name;
        private String description;
        private String stringId;
        private String type;

        public CatalogSearchTableItem() {
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getStringId() {
            return stringId;
        }

        public void setStringId(String stringId) {
            this.stringId = stringId;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
