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
import org.xbup.lib.catalog.entity.XBENode;
import org.xbup.lib.catalog.entity.XBEXFile;
import org.xbup.lib.core.catalog.XBCatalog;
import org.xbup.lib.core.catalog.base.XBCNode;
import org.xbup.lib.core.catalog.base.XBCXFile;
import org.xbup.lib.core.catalog.base.service.XBCXFileService;

/**
 * Table model for catalog specifications.
 *
 * @version 0.1.24 2015/01/14
 * @author ExBin Project (http://exbin.org)
 */
public class CatalogFilesTableModel extends AbstractTableModel {

    private XBCatalog catalog;
    private XBCXFileService fileService;
    private XBCNode node;

    private final String[] columnNames = new String[]{"Filename", "Size"};
    private final Class[] columnClasses = new Class[]{
        java.lang.String.class, java.lang.Long.class
    };

    private List<FileItemRecord> items = new ArrayList<>();

    public CatalogFilesTableModel() {
        node = null;
    }

    @Override
    public int getRowCount() {
        return items.size();
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0: {
                return items.get(rowIndex).fileName;
            }
            case 1: {
                byte[] data = items.get(rowIndex).modifiedData;
                if (data == null) {
                    data = ((XBCXFile) items.get(rowIndex).file).getContent();
                }

                return data == null ? 0 : data.length;
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

    public XBCNode getNode() {
        return node;
    }

    public void setNode(XBCNode node) {
        this.node = node;
        items = new ArrayList<>();
        if (node != null) {
            for (XBCXFile file : ((List<XBCXFile>) fileService.findFilesForNode(node))) {
                items.add(new FileItemRecord(file));
            }
        }
    }

    public XBCXFile getItem(int rowIndex) {
        return items.get(rowIndex).file;
    }

    public void addItem(String fileName, byte[] data) {
        items.add(new FileItemRecord(fileName, data));
        fireTableDataChanged();
    }

    public XBCXFile removeItem(int rowIndex) {
        XBCXFile result = items.remove(rowIndex).file;
        fireTableDataChanged();
        return result;
    }

    public void setCatalog(XBCatalog catalog) {
        this.catalog = catalog;

        fileService = catalog == null ? null : (XBCXFileService) catalog.getCatalogService(XBCXFileService.class);
    }

    public void persist() {
        for (FileItemRecord itemRecord : items) {
            if (itemRecord.file == null) {
                XBEXFile file = new XBEXFile();
                file.setNode((XBENode) node);
                file.setFilename(itemRecord.fileName);
            }
            
            if (itemRecord.modifiedData != null) {
                ((XBEXFile) itemRecord.file).setContent(itemRecord.modifiedData);
                fileService.persistItem(itemRecord.file);
            }
        }
    }

    public void setItemData(int rowIndex, byte[] fileContent) {
        items.get(rowIndex).modifiedData = fileContent;
        fireTableRowsUpdated(rowIndex, rowIndex);
    }

    private class FileItemRecord {

        public XBCXFile file = null;
        public String fileName = null;
        public byte[] modifiedData = null;

        public FileItemRecord(String fileName, byte[] data) {
            this.fileName = fileName;
            modifiedData = data;
        }

        public FileItemRecord(XBCXFile file) {
            this.file = file;
            fileName = file.getFilename();
        }
    }
}
