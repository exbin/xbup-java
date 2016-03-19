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
 * Catalog item file information record.
 *
 * @version 0.1.24 2015/01/12
 * @author ExBin Project (http://exbin.org)
 */
public class ItemFile {

    private Long id;
    private Long xbIndex;
    private String fileName;
    private Long mode;
    private Long dataSize;

    public ItemFile() {
        id = null;
        xbIndex = null;
        fileName = null;
        mode = null;
    }

    public Long getXBIndex() {
        return xbIndex;
    }

    public void setXBIndex(Long xbIndex) {
        this.xbIndex = xbIndex;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getMode() {
        return mode;
    }

    public void setMode(Long mode) {
        this.mode = mode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDataSize() {
        return dataSize;
    }

    public void setDataSize(Long dataSize) {
        this.dataSize = dataSize;
    }
}
