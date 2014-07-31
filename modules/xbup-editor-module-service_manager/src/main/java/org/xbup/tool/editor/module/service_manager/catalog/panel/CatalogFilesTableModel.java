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
import org.xbup.lib.core.catalog.base.XBCNode;
import org.xbup.lib.core.catalog.base.XBCXFile;
import org.xbup.lib.core.catalog.base.service.XBCXFileService;
import org.xbup.lib.catalog.entity.XBEXFile;

/**
 * Table model for catalog specifications.
 *
 * @version 0.1 wr22.0 2013/07/28
 * @author XBUP Project (http://xbup.org)
 */
public class CatalogFilesTableModel extends AbstractTableModel {

    private XBCatalog catalog;
    private XBCXFileService fileService;
    private XBCNode node;

    private String[] columnNames = new String [] { "Filename", "Size" };
    private Class[] classes = new Class [] {
        java.lang.String.class, java.lang.Long.class
    };

    private List<XBCXFile> items = new ArrayList<XBCXFile>();

    // private long formatCount, groupCount, blockCount, limitCount;

    /**
     * Creates a new instance of CatalogSpecsTableModel
     */
    public CatalogFilesTableModel(XBCatalog catalog) {
        this.catalog = catalog;
        node = null;
        fileService = null;
        if (catalog!=null) {
            fileService = (XBCXFileService) catalog.getCatalogService(XBCXFileService.class);
            // TODO: OnAddExtension
        }
    }

    @Override
    public int getRowCount() {
        return items.size();
        /*if (node==null) {
            return 0;
        }
        XBCSpecService specService = (XBCSpecService) catalog.getCatalogService(XBCSpecService.class);
        return (int) specService.getSpecsCount(node)+1; */
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0: {
                return items.get(rowIndex).getFilename();
            }
            case 1: {
                byte[] data = ((XBEXFile) items.get(rowIndex)).getContent();
                if (data == null) {
                    return 0;
                }

                return data.length;
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
        return classes[columnIndex];
    }

    public XBCNode getNode() {
        return node;
    }

    public void setNode(XBCNode node) {
        this.node = node;
        items = new ArrayList<XBCXFile>();
        if (node != null) {
            items.addAll(fileService.findFilesForNode(node));
        }
    }

    public XBCXFile getItem(int index) {
        return items.get(index);
    }
}
