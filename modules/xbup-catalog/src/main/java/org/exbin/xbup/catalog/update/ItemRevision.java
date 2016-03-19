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
package org.exbin.xbup.catalog.update;

/**
 * Catalog item revision information record.
 *
 * @version 0.1.24 2015/01/13
 * @author ExBin Project (http://exbin.org)
 */
public class ItemRevision {

    private Long id;
    private Long xbIndex;
    private Long xbLimit;
    private String name;
    private String desc;

    public ItemRevision() {
        id = null;
        xbIndex = null;
        xbLimit = null;
    }

    public Long getXBIndex() {
        return xbIndex;
    }

    public void setXBIndex(Long xbIndex) {
        this.xbIndex = xbIndex;
    }

    public Long getXBLimit() {
        return xbLimit;
    }

    public void setXBLimit(Long xbLimit) {
        this.xbLimit = xbLimit;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
