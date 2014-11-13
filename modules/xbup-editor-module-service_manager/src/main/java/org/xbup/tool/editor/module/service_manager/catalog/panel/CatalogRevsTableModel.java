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
package org.xbup.tool.editor.module.service_manager.catalog.panel;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import org.xbup.lib.core.catalog.XBCatalog;
import org.xbup.lib.core.catalog.base.XBCRev;
import org.xbup.lib.core.catalog.base.XBCSpec;
import org.xbup.lib.core.catalog.base.service.XBCRevService;
import org.xbup.lib.core.catalog.base.service.XBCXDescService;
import org.xbup.lib.core.catalog.base.service.XBCXNameService;

/**
 * Table model for catalog revisions.
 *
 * @version 0.1.24 2014/11/13
 * @author XBUP Project (http://xbup.org)
 */
public class CatalogRevsTableModel extends AbstractTableModel {

    private XBCatalog catalog;
    private XBCSpec spec;

    private final String[] columnNames = new String[]{"XBIndex", "Name", "Description", "XBLimit"};
    private final Class[] classes = new Class[]{
        java.lang.Long.class, java.lang.String.class, java.lang.String.class, java.lang.Long.class
    };
    private final boolean[] columnsEditable = new boolean[]{false, false, false, true};
    private final List<CatalogRevsTableItem> revs = new ArrayList<>();

    public CatalogRevsTableModel(XBCatalog catalog) {
        this.catalog = catalog;
    }

    @Override
    public int getRowCount() {
        return revs.size();
    }

    @Override
    public int getColumnCount() {
        return classes.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return revs.get(rowIndex).getXbIndex();
            case 1:
                return revs.get(rowIndex).getName();
            case 2:
                return revs.get(rowIndex).getDescription();
            case 3:
                return revs.get(rowIndex).getLimit();
        }

        return null;

    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnsEditable[columnIndex];
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columnNames[columnIndex];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return classes[columnIndex];
    }

    public XBCSpec getSpec() {
        return spec;
    }

    public void setSpec(XBCSpec spec) {
        this.spec = spec;
        reloadItems();
    }

    private void reloadItems() {
        revs.clear();
        if (spec != null && catalog != null) {
            XBCRevService revService = (XBCRevService) catalog.getCatalogService(XBCRevService.class);
            XBCXNameService nameService = (XBCXNameService) catalog.getCatalogService(XBCXNameService.class);
            XBCXDescService descService = (XBCXDescService) catalog.getCatalogService(XBCXDescService.class);
            List<XBCRev> revsList = revService.getRevs(spec);
            for (XBCRev rev : revsList) {
                CatalogRevsTableItem item = new CatalogRevsTableItem();
                item.setRev(rev);
                item.setXbIndex(rev.getXBIndex());
                item.setLimit(rev.getXBLimit());
                item.setName(nameService.getItemNameText(rev));
                item.setDescription(descService.getItemDescText(rev));
                revs.add(item);
            }
        }
    }

    public void setCatalog(XBCatalog catalog) {
        this.catalog = catalog;
        reloadItems();
    }
}
