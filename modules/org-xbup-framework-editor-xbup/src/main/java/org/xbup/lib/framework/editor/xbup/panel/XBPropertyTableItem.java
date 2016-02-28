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
package org.xbup.lib.framework.editor.xbup.panel;

import org.xbup.lib.core.catalog.base.XBCSpecDef;
import org.xbup.lib.core.block.definition.XBParamType;
import org.xbup.lib.plugin.XBLineEditor;

/**
 * Parameters list table item record.
 *
 * @version 0.1.24 2015/01/10
 * @author ExBin Project (http://exbin.org)
 */
public class XBPropertyTableItem {

    private XBCSpecDef specDef;
    private String valueName;
    private String typeName;
    private XBLineEditor lineEditor;

    public XBPropertyTableItem(XBCSpecDef specDef, String valueName, String typeName, XBLineEditor lineEditor) {
        this.specDef = specDef;
        this.valueName = valueName;
        this.typeName = typeName;
        this.lineEditor = lineEditor;
    }

    public XBPropertyTableItem(XBCSpecDef specDef, String name, String type) {
        this(specDef, name, type, null);
    }

    public XBCSpecDef getSpecDef() {
        return specDef;
    }

    public void setSpecDef(XBCSpecDef specDef) {
        this.specDef = specDef;
    }

    public String getValueName() {
        return valueName;
    }

    public void setValueName(String valueName) {
        this.valueName = valueName;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public XBLineEditor getLineEditor() {
        return lineEditor;
    }

    public void setLineEditor(XBLineEditor lineEditor) {
        this.lineEditor = lineEditor;
    }

    public String getDefTypeName() {
        String defTypeName = "";
        if (specDef.getTarget() == null) {
            switch (specDef.getType()) {
                case CONSIST:
                case LIST_CONSIST: {
                    defTypeName = "Any";
                    break;
                }
                case JOIN:
                case LIST_JOIN: {
                    defTypeName = "Attribute";
                    break;
                }
            }
        } else {
            defTypeName = typeName;
        }
        if (specDef.getType() == XBParamType.LIST_CONSIST || specDef.getType() == XBParamType.LIST_JOIN) {
            defTypeName += "[]";
        }
        
        return defTypeName;
    }
}
