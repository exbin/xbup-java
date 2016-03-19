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

import org.exbin.xbup.core.catalog.base.XBCRev;

/**
 * Table model for catalog specification revisions.
 *
 * @version 0.1.24 2014/11/13
 * @author ExBin Project (http://exbin.org)
 */
public class CatalogRevsTableItem {

    private XBCRev rev;
    private Long xbIndex;
    private Long limit;
    private String name;
    private String description;

    public XBCRev getRev() {
        return rev;
    }

    public void setRev(XBCRev rev) {
        this.rev = rev;
    }

    public Long getXbIndex() {
        return xbIndex;
    }

    public void setXbIndex(Long xbIndex) {
        this.xbIndex = xbIndex;
    }

    public Long getLimit() {
        return limit;
    }

    public void setLimit(Long limit) {
        this.limit = limit;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
