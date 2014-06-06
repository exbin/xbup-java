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
package org.xbup.lib.xb.catalog.update;

/**
 * Path to revision structure.
 *
 * @version 0.1 wr16.0 2009/06/14
 * @author XBUP Project (http://xbup.org)
 */
public class RevisionPath {

    private Long[] path;
    private Long specId;
    private Long revXBId;
    private Long bindType;

    public RevisionPath() {
        path = null;
        specId = null;
    }

    public Long[] getPath() {
        return path;
    }

    public void setPath(Long[] path) {
        this.path = path;
    }

    public Long getSpecId() {
        return specId;
    }

    public void setSpecId(Long specId) {
        this.specId = specId;
    }

    public Long getRevXBId() {
        return revXBId;
    }

    public void setRevXBId(Long revXBId) {
        this.revXBId = revXBId;
    }

    public Long getBindType() {
        return bindType;
    }

    public void setBindType(Long bindType) {
        this.bindType = bindType;
    }
}
