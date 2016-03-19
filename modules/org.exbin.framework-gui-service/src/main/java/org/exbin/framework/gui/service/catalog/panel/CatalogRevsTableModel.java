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
import org.exbin.xbup.core.catalog.XBCatalog;
import org.exbin.xbup.core.catalog.base.XBCRev;
import org.exbin.xbup.core.catalog.base.XBCSpec;
import org.exbin.xbup.core.catalog.base.service.XBCRevService;
import org.exbin.xbup.core.catalog.base.service.XBCXDescService;
import org.exbin.xbup.core.catalog.base.service.XBCXNameService;

/**
 * Table model for catalog revisions.
 *
 * @version 0.1.24 2014/12/09
 * @author ExBin Project (http://exbin.org)
 */
public class CatalogRevsTableModel extends AbstractTableModel {

    private XBCatalog catalog;
    private XBCSpec spec;

    private final String[] columnNames = new String[]{"XBIndex", "Name", "Description", "XBLimit"};
    private final Class[] classes = new Class[]{
        java.lang.Long.class, java.lang.String.class, java.lang.String.class, java.lang.Long.class
    };
    private final List<CatalogRevsTableItem> revs = new ArrayList<>();

    public CatalogRevsTableModel() {
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
                item.setName(nameService.getDefaultText(rev));
                item.setDescription(descService.getDefaultText(rev));
                revs.add(item);
            }
        }

        fireTableDataChanged();
    }

    public void setCatalog(XBCatalog catalog) {
        this.catalog = catalog;
        reloadItems();
    }

    public CatalogRevsTableItem getRowItem(int index) {
        return revs.get(index);
    }

    public List<CatalogRevsTableItem> getRevs() {
        return revs;
    }

    public Long getRevisionForIndex(Long xbIndex) {
        long limitSum = 0;

        for (int revision = 0; revision < revs.size(); revision++) {
            CatalogRevsTableItem revItem = revs.get(revision);

            if (xbIndex < limitSum + revItem.getLimit()) {
                return (long) revision;
            }

            limitSum += revItem.getLimit();
        }

        return null;
    }
}
