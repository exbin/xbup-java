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
package org.xbup.tool.editor.module.xbdoc_editor.panel;

import org.xbup.lib.core.catalog.base.XBCBlockSpec;

/**
 * Parameters list table item record.
 *
 * @version 0.1.24 2014/12/19
 * @author XBUP Project (http://xbup.org)
 */
public class XBPropertyTableItem {

    private XBCBlockSpec spec;
    private String name;
    private String type;

    public XBPropertyTableItem(XBCBlockSpec spec, String name, String type) {
        this.spec = spec;
        this.name = name;
        this.type = type;
    }

    public XBCBlockSpec getSpec() {
        return spec;
    }

    public void setSpec(XBCBlockSpec spec) {
        this.spec = spec;
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
}
