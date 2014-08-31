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
package org.xbup.tool.editor.module.xbdoc_editor.dialog;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import org.xbup.lib.core.ubnumber.UBNatural;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * Table Model for attribute list for item editing.
 *
 * @version 0.1.22 2013/02/27
 * @author XBUP Project (http://xbup.org)
 */
public class ItemAttribsTableModel extends AbstractTableModel {

    private ResourceBundle resourceBundle;
    private List<UBNatural> attribs;
    private List<UBNatural> attrList;

    private String[] columns;
    private Class[] types = new Class[]{
        java.lang.String.class, java.lang.String.class, java.lang.String.class,
        java.lang.Integer.class
    };
    private boolean[] canEdit = new boolean[]{false, false, false, true};
    private List<TableModelListener> tableModelListeners = new ArrayList<TableModelListener>();

    /**
     * Creates a new instance of ItemAttribsTableModel
     */
    public ItemAttribsTableModel() {
        resourceBundle = java.util.ResourceBundle.getBundle("org/xbup/tool/xbeditor/module/xbdoceditor/dialog/resources/ItemModifyDialog");
        columns = new String[]{resourceBundle.getString("itemmod_itemtype"), resourceBundle.getString("itemmod_itemname"), resourceBundle.getString("itemmod_itemdesc"), resourceBundle.getString("itemmod_itemvalue")};
        attribs = new ArrayList<UBNatural>();
        attrList = null;
    }

    @Override
    public int getRowCount() {
        return attribs.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columns[columnIndex];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return getTypes()[columnIndex];
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return canEdit[columnIndex];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex == 3) {
            return ((UBNat32) getAttribs().get(rowIndex)).getInt();
        }
        if (rowIndex < 2) {
            switch (columnIndex) {
                case 0:
                    return "UBNatural";
                case 1:
                    if (rowIndex == 0) {
                        return "BlockGroup";
                    } else {
                        return "BlockType";
                    }
                case 2:
                    if (rowIndex == 0) {
                        return resourceBundle.getString("itemmod_attrib_group");
                    } else {
                        return resourceBundle.getString("itemmod_attrib_block");
                    }
                default:
                    throw new IllegalArgumentException();
            }
        } else {
            switch (columnIndex) {
                case 0:
                    return "UBNumber";
                case 1:
                    return "attribute";
                case 2:
                    return "";
                default:
                    throw new IllegalArgumentException();
            }
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (columnIndex == 3) {
            ((UBNat32) attribs.get(rowIndex)).setValue((Integer) aValue);
            if (rowIndex < 2) {
                updateBlockAttribs();
                fireTableDataChanged();
            }
        }
    }

    public List<UBNatural> getAttribs() {
        return attribs;
    }

    public void setAttribs(List<UBNatural> attribs) {
        this.attribs = attribs;
    }

    public Class[] getTypes() {
        return types;
    }

    public void setTypes(Class[] types) {
        this.types = types;
    }

    @Override
    public void addTableModelListener(TableModelListener listener) {
        tableModelListeners.add(listener);
    }

    @Override
    public void removeTableModelListener(TableModelListener listener) {
        tableModelListeners.remove(listener);
    }

    public int getAttribute(int index) {
        if (attribs.size() <= index) {
            return 0;
        }
        UBNat32 attribute = (UBNat32) attribs.get(index);
        if (attribute != null) {
            return attribute.getInt();
        } else {
            return 0;
        }
    }

    public void updateBlockAttribs() {
        /*        if (getTypeSpec()!=null) {
         XBCBlockType typeBlock = getTypeSpec().getBlockSpec(getAttribute(0),getAttribute(1));
         if (typeBlock != null) {
         attrList = typeBlock.getAttrList();
         } else attrList = new ArrayList<XBAttributeType>();
         } else attrList = null; */
    }
}
