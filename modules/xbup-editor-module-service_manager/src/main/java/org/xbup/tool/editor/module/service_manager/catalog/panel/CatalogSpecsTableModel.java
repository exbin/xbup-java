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
import java.util.Objects;
import javax.swing.table.AbstractTableModel;
import org.xbup.lib.core.catalog.XBCatalog;
import org.xbup.lib.core.catalog.base.XBCBlockSpec;
import org.xbup.lib.core.catalog.base.XBCFormatSpec;
import org.xbup.lib.core.catalog.base.XBCGroupSpec;
import org.xbup.lib.core.catalog.base.XBCItem;
import org.xbup.lib.core.catalog.base.XBCNode;
import org.xbup.lib.core.catalog.base.XBCXName;
import org.xbup.lib.core.catalog.base.service.XBCSpecService;
import org.xbup.lib.core.catalog.base.service.XBCXNameService;

/**
 * Table model for catalog specifications.
 *
 * @version 0.1.22 2013/04/10
 * @author XBUP Project (http://xbup.org)
 */
public class CatalogSpecsTableModel extends AbstractTableModel {

    private final XBCatalog catalog;
    private XBCXNameService nameService;
    private XBCNode node;

    private final String[] columnNames = new String[]{"Name", "Type", "XBIndex"};
    private final Class[] columnClasses = new Class[]{
        java.lang.String.class, java.lang.String.class, java.lang.Long.class
    };

    private List<CatalogSpecTableItem> items = new ArrayList<>();

    // private long formatCount, groupCount, blockCount, limitCount;
    public CatalogSpecsTableModel(XBCatalog catalog) {
        this.catalog = catalog;
        node = null;
        nameService = null;
        if (catalog != null) {
            nameService = (XBCXNameService) catalog.getCatalogService(XBCXNameService.class);
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
        return 3;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0: {
                return items.get(rowIndex).getName();
            }
            case 1: {
                return items.get(rowIndex).getType();
            }
            case 2: {
                return items.get(rowIndex).getItem().getXBIndex();
            }
        }
        return "";

        /*switch (columnIndex) {
         case 0: {
         if (rowIndex==0) {
         return ".";
         }
         XBCItem item = getItem(rowIndex);
         if (nameService!=null) {
         if (item==null) {
         return null;
         }
         XBCXName name = nameService.getDefaultItemName(item);
         return (name==null)?"":name.getText();
         } else {
         return "spec";
         }
         }
         case 1: {
         if (rowIndex==0) {
         return "Node";
         }
         if (rowIndex<formatCount+1) {
         return "Format";
         }
         if (rowIndex<formatCount+groupCount+1) {
         return "Group";
         }
         if (rowIndex<formatCount+groupCount+blockCount+1) {
         return "Block";
         }
         return "Limitation";
         }

         case 2: {
         XBCItem item = getItem(rowIndex);
         if (item!=null) {
         return item.getXBIndex();
         }
         return null;
         }
         default: return null;
         } */
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
        XBCSpecService specService = (XBCSpecService) catalog.getCatalogService(XBCSpecService.class);
        /*formatCount = specService.getFormatSpecsCount(node);
         groupCount = specService.getGroupSpecsCount(node);
         blockCount = specService.getBlockSpecsCount(node); */

        items = new ArrayList<>();
        if (node != null) {
            items.add(new CatalogSpecTableItem(node, CatalogSpecItemType.NODE));

            List<XBCFormatSpec> formatSpecs = specService.getFormatSpecs(node);
            for (XBCFormatSpec spec : formatSpecs) {
                items.add(new CatalogSpecTableItem(spec, CatalogSpecItemType.FORMAT));
            }

            List<XBCGroupSpec> groupSpecs = specService.getGroupSpecs(node);
            for (XBCGroupSpec spec : groupSpecs) {
                items.add(new CatalogSpecTableItem(spec, CatalogSpecItemType.GROUP));
            }

            List<XBCBlockSpec> blockSpecs = specService.getBlockSpecs(node);
            for (XBCBlockSpec spec : blockSpecs) {
                items.add(new CatalogSpecTableItem(spec, CatalogSpecItemType.BLOCK));
            }
        }
    }

    public XBCItem getItem(int index) {
        return items.get(index).getItem();
        /*if (index==0) {
         return node;
         }
         XBCSpecService specService = (XBCSpecService) catalog.getCatalogService(XBCSpecService.class);
         if (index<formatCount+1) {
         return specService.getFormatSpec(node,index-1);
         }
         if (index<formatCount+groupCount+1) {
         return specService.getGroupSpec(node,index-formatCount-1);
         }
         return specService.getBlockSpec(node,index-formatCount-groupCount-1); */
    }

    public int getIndexOfItem(CatalogSpecTableItem item) {
        return items.indexOf(item);
    }

    public class CatalogSpecTableItem {

        private XBCItem item;
        private String name;
        private String type;

        public CatalogSpecTableItem(XBCItem item) {
            this(item, CatalogSpecItemType.NODE);
        }

        public CatalogSpecTableItem(XBCItem item, CatalogSpecItemType itemType) {
            this.item = item;
            switch (itemType) {
                case NODE: {
                    type = "Node";
                    break;
                }
                case FORMAT: {
                    type = "Format";
                    break;
                }
                case GROUP: {
                    type = "Group";
                    break;
                }
                case BLOCK: {
                    type = "Block";
                    break;
                }
                default: {
                    type = "Unknown";
                }
            }

            if (itemType == CatalogSpecItemType.NODE) {
                name = ".";
            } else if (nameService != null) {
                if (item == null) {
                    name = null;
                } else {
                    XBCXName itemName = nameService.getDefaultItemName(item);
                    name = (itemName == null) ? "" : itemName.getText();
                }
            } else {
                name = "spec";
            }
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public XBCItem getItem() {
            return item;
        }

        public void setItem(XBCItem item) {
            this.item = item;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 23 * hash + Objects.hashCode(this.item.getId());
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final CatalogSpecTableItem other = (CatalogSpecTableItem) obj;
            if (!Objects.equals(this.item.getId(), other.item.getId())) {
                return false;
            }
            return true;
        }
    }
}
