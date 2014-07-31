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
import org.xbup.lib.core.catalog.XBACatalog;
import org.xbup.lib.core.catalog.base.XBCBlockCons;
import org.xbup.lib.core.catalog.base.XBCBlockJoin;
import org.xbup.lib.core.catalog.base.XBCBlockListCons;
import org.xbup.lib.core.catalog.base.XBCBlockListJoin;
import org.xbup.lib.core.catalog.base.XBCConsDef;
import org.xbup.lib.core.catalog.base.XBCItem;
import org.xbup.lib.core.catalog.base.XBCJoinDef;
import org.xbup.lib.core.catalog.base.XBCSpec;
import org.xbup.lib.core.catalog.base.XBCSpecDef;
import org.xbup.lib.core.catalog.base.XBCXName;
import org.xbup.lib.core.catalog.base.service.XBCSpecService;
import org.xbup.lib.core.catalog.base.service.XBCXDescService;
import org.xbup.lib.core.catalog.base.service.XBCXNameService;
import org.xbup.lib.core.catalog.base.service.XBCXStriService;

/**
 * Table model for catalog bindings.
 *
 * @version 0.1 wr23.0 2013/09/22
 * @author XBUP Project (http://xbup.org)
 */
public class CatalogBindsTableModel extends AbstractTableModel {

    private XBACatalog catalog;
    private XBCSpec spec;
    private XBCXNameService nameService;
    private XBCXDescService descService;
    private XBCXStriService striService;
    private XBCSpecService bindService;

    private String[] columnNames = new String [] { "XBIndex", "StringId", "Target", "Type", "Revision", "Name", "Description" };
    private Class[] classes = new Class [] {
        java.lang.Long.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Long.class, java.lang.String.class, java.lang.String.class
    };

    private List<CatalogBindsTableItem> items;

    /**
     * Creates a new instance of CatalogSpecsTableModel
     */
    public CatalogBindsTableModel(XBACatalog catalog) {
        initCatalog(catalog);
        spec = null;
        items = new ArrayList<CatalogBindsTableItem>();
    }

    @Override
    public int getRowCount() {
        if (spec==null) {
            return 0;
        }
        return items.size();
    }

    @Override
    public int getColumnCount() {
        return 7;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (spec == null) {
            return null;
        }
        CatalogBindsTableItem item = items.get(rowIndex);

        switch (columnIndex) {
            case 0: return item.getXbIndex();
            case 1: return item.getStringId();
            case 2: return item.getTarget();
            case 3: return item.getType();
            case 4: return item.getRevision();
            case 5: return item.getName();
            case 6: return ""; //item.getDescription();

            default: return "";
        }
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

    public void setItem(XBCItem item) {
        items.clear();
        if (item instanceof XBCSpec) {
            spec = (XBCSpec) item;
            List<XBCSpecDef> specDefs = bindService.getSpecDefs(spec);
            for (XBCSpecDef specDef : specDefs) {
                CatalogBindsTableItem tableItem = new CatalogBindsTableItem();
                tableItem.setSpecDef(specDef);
                tableItem.setXbIndex(specDef.getXBIndex());
                tableItem.setStringId(striService.getItemStringIdText(specDef));
                String target;

                if (specDef instanceof XBCBlockJoin) {
                    if (specDef.getTarget() == null) {
                        target = "Attribute";
                    } else {
                        target = "Join";
                    }
                } else if (specDef instanceof XBCBlockCons) {
                    if (specDef.getTarget() == null) {
                        target = "Data Block";
                    } else {
                        target = "Consist";
                    }
                } else if (specDef instanceof XBCBlockListJoin) {
                    target = "Join List";
                } else if (specDef instanceof XBCBlockListCons) {
                    target = "Consist List";
                } else if (specDef instanceof XBCJoinDef) {
                    target = "Join";
                } else if (specDef instanceof XBCConsDef) {
                    target = "Consist";
                } else {
                    target = "Unknown";
                }
                tableItem.setTarget(target);
                tableItem.setType("");
                tableItem.setRevision(0L);
                if (specDef.getTarget() != null) {
                    XBCSpec targetSpec = (XBCSpec) specDef.getTarget().getParent();
                    if (targetSpec != null) {
                        XBCXName name = nameService.getItemName(targetSpec);
                        if (name != null) {
                            tableItem.setType(name.getText());
                        }
                    }
                    tableItem.setRevision(specDef.getTarget().getXBIndex());

                }
                items.add(tableItem);
                tableItem.setName(""); //nameService.getItemNameText(specDef));
                // tableItem.setDescription(descService.getItemDescText(specDef));
            }
        }
    }

    public CatalogBindsTableItem getRowItem(int rowIndex) {
        return items.get(rowIndex);
    }

    private void initCatalog(XBACatalog catalog) {
        this.catalog = catalog;

        nameService = null;
        if (catalog!=null) {
            nameService = (XBCXNameService) catalog.getCatalogService(XBCXNameService.class);
            descService = (XBCXDescService) catalog.getCatalogService(XBCXDescService.class);
            striService = (XBCXStriService) catalog.getCatalogService(XBCXStriService.class);
            // TODO: OnAddExtension
            bindService = (XBCSpecService) catalog.getCatalogService(XBCSpecService.class);
        }
    }

    public void setCatalog(XBACatalog catalog) {
        initCatalog(catalog);
    }

    public void removeItem(XBCSpecDef specDef) {
        for (int i = 0; i < items.size(); i++) {
            CatalogBindsTableItem item = items.get(i);
            if (item.getSpecDef() == specDef) {
                items.remove(item);
                break;
            }
        }
    }

    public void moveItemDown(int itemPos) {
        CatalogBindsTableItem item = items.get(itemPos);
        item.setXbIndex(item.getSpecDef().getXBIndex());
        items.remove(item);
        items.add(itemPos+1, item);

        item = items.get(itemPos);
        item.setXbIndex(item.getSpecDef().getXBIndex());
    }
}
