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

import org.xbup.lib.core.catalog.base.XBCSpecDef;

/**
 * Table model for catalog bindings.
 *
 * @version 0.1.22 2013/05/17
 * @author XBUP Project (http://xbup.org)
 */
public class CatalogBindsTableItem {

    private XBCSpecDef specDef;
    private Long xbIndex;
    private String stringId;
    private String target;
    private String type;
    private Long revision;
    private String name;

    /**
     * @return the specDef
     */
    public XBCSpecDef getSpecDef() {
        return specDef;
    }

    /**
     * @param specDef the specDef to set
     */
    public void setSpecDef(XBCSpecDef specDef) {
        this.specDef = specDef;
    }

    /**
     * @return the xbIndex
     */
    public Long getXbIndex() {
        return xbIndex;
    }

    /**
     * @param xbIndex the xbIndex to set
     */
    public void setXbIndex(Long xbIndex) {
        this.xbIndex = xbIndex;
    }

    /**
     * @return the stringId
     */
    public String getStringId() {
        return stringId;
    }

    /**
     * @param stringId the stringId to set
     */
    public void setStringId(String stringId) {
        this.stringId = stringId;
    }

    /**
     * @return the target
     */
    public String getTarget() {
        return target;
    }

    /**
     * @param target the target to set
     */
    public void setTarget(String target) {
        this.target = target;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the revision
     */
    public Long getRevision() {
        return revision;
    }

    /**
     * @param revision the revision to set
     */
    public void setRevision(Long revision) {
        this.revision = revision;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

}
