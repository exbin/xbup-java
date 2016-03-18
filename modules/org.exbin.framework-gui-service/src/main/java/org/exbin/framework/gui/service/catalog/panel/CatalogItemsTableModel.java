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

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import org.xbup.lib.catalog.entity.manager.XBItemWithDetail;
import org.xbup.lib.catalog.entity.service.XBEItemService;
import org.xbup.lib.core.catalog.XBACatalog;
import org.xbup.lib.core.catalog.base.XBCItem;
import org.xbup.lib.core.catalog.base.service.XBCItemService;
import org.exbin.framework.gui.service.catalog.panel.CatalogSearchTableModel.CatalogSearchTableItem;

/**
 * Table model for catalog specifications.
 *
 * @version 0.1.24 2014/12/12
 * @author ExBin Project (http://exbin.org)
 */
public class CatalogItemsTableModel extends AbstractTableModel {

    private XBCItemService itemService;

    final static String[] columnNames = new String[]{"Name", "StringId", "Type", "Description"};
    final static Class[] columnClasses = new Class[]{
        java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
    };
    final static int pageSize = 20;

    private final List<List<XBItemWithDetail>> itemPages = new ArrayList<>();
    private String filterCondition = "";
    private int itemCount = 0;
    private String specType = null;

    public CatalogItemsTableModel() {
    }

    @Override
    public int getRowCount() {
        return itemCount;
    }

    public XBItemWithDetail getRow(int rowIndex) {
        int pageIndex = rowIndex / pageSize;
        int rowInPage = rowIndex % pageSize;

        List<XBItemWithDetail> page = itemPages.get(pageIndex);
        if (page == null) {
            page = ((XBEItemService) itemService).findAllPaged(pageIndex * pageSize, pageSize, filterCondition, null, specType);
            itemPages.set(pageIndex, page);
        }

        return page.get(rowInPage);
    }

    public void performLoad() {
        itemCount = ((XBEItemService) itemService).findAllPagedCount(filterCondition, specType);
        itemPages.clear();
        for (int i = 0; i <= itemCount / pageSize; i++) {
            itemPages.add(null);
        }

        fireTableDataChanged();
    }

    public void performSearch(CatalogSearchTableItem filter) {
        if (filter != null) {
            StringBuilder builder = new StringBuilder();
            if (!(filter.getName() == null || filter.getName().isEmpty())) {
                builder.append("LOWER(name.text) LIKE '%").append(filter.getName().toLowerCase().replace("'", "''")).append("%'");
            }
            if (!(filter.getDescription() == null || filter.getDescription().isEmpty())) {
                if (builder.length() > 0) {
                    builder.append(" AND ");
                }
                builder.append("LOWER(desc.text) LIKE '%").append(filter.getDescription().toLowerCase().replace("'", "''")).append("%'");
            }
            if (!(filter.getStringId() == null || filter.getStringId().isEmpty())) {
                if (builder.length() > 0) {
                    builder.append(" AND ");
                }
                builder.append("LOWER(stri.text) LIKE '%").append(filter.getStringId().toLowerCase().replace("'", "''")).append("%'");
            }
            if (!(filter.getType() == null || filter.getType().isEmpty())) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            filterCondition = builder.toString();
        } else {
            filterCondition = "";
        }

        performLoad();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0: {
                return getRow(rowIndex).getName();
            }
            case 1: {
                return getRow(rowIndex).getStringId();
            }
            case 2: {
                return getRow(rowIndex).getType();
            }
            case 3: {
                return getRow(rowIndex).getDescription();
            }
        }
        return "";
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columnNames[columnIndex];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return columnClasses[columnIndex];
    }

    public XBCItem getItem(int index) {
        return getRow(index).getItem();
    }

    public int getIndexOfItem(XBItemWithDetail item) {
        return itemPages.indexOf(item);
    }

    public void setCatalog(XBACatalog catalog) {
        itemService = catalog == null ? null : (XBCItemService) catalog.getCatalogService(XBCItemService.class);
    }

    void setSpecType(CatalogSpecItemType catalogSpecType) {
        switch (catalogSpecType) {
            case FORMAT: {
                specType = "XBFormatSpec";
                break;
            }
            case GROUP: {
                specType = "XBGroupSpec";
                break;
            }
            case BLOCK: {
                specType = "XBBlockSpec";
                break;
            }
            case NODE: {
                specType = "XBNode";
                break;
            }
        }
    }
}
