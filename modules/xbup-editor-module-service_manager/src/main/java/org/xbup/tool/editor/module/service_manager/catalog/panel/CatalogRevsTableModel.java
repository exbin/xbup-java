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

import javax.swing.table.AbstractTableModel;
import org.xbup.lib.core.catalog.XBCatalog;
import org.xbup.lib.core.catalog.base.XBCRev;
import org.xbup.lib.core.catalog.base.XBCSpec;
import org.xbup.lib.core.catalog.base.service.XBCRevService;

/**
 * Table model for catalog revisions.
 *
 * @version 0.1 wr21.0 2011/12/31
 * @author XBUP Project (http://xbup.org)
 */
public class CatalogRevsTableModel extends AbstractTableModel {

    private XBCatalog catalog;
    private XBCSpec spec;

    private String[] columnNames = new String [] { "Type", "XBIndex", "XBLimit" };
    private Class[] classes = new Class [] {
        java.lang.String.class, java.lang.Long.class, java.lang.Long.class
    };

    /** Creates a new instance of CatalogRevsTableModel */
    public CatalogRevsTableModel(XBCatalog catalog) {
        this.catalog = catalog;
    }

    @Override
    public int getRowCount() {
        if (spec==null) {
            return 0;
        }
        XBCRevService revService = (XBCRevService) catalog.getCatalogService(XBCRevService.class);
        return (int) revService.getRevsCount(spec);
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (spec == null) {
            return null;
        }
        XBCRevService revService = (XBCRevService) catalog.getCatalogService(XBCRevService.class);
        XBCRev rev = revService.getRev(spec, rowIndex);
        if (rev == null) {
            return null;
        }
        switch (columnIndex) {
            case 0: return "Revision";
            case 1: return rev.getXBIndex();
            case 2: return rev.getXBLimit();
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
    }
}
