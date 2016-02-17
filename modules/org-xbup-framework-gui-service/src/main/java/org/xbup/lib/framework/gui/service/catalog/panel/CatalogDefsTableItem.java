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
package org.xbup.lib.framework.gui.service.catalog.panel;

import org.xbup.lib.catalog.entity.XBERev;
import org.xbup.lib.core.catalog.base.XBCRev;
import org.xbup.lib.core.catalog.base.XBCSpecDef;
import org.xbup.lib.core.block.definition.XBParamType;

/**
 * Table model for catalog specification definition.
 *
 * @version 0.2.0 2016/02/01
 * @author XBUP Project (http://xbup.org)
 */
public class CatalogDefsTableItem {

    private XBCSpecDef specDef;
    private Long xbIndex;
    private XBParamType defType;
    private XBCRev target;
    private String stringId;
    private String name;
    private String description;

    /**
     * Chached values.
     */
    private Long revision;
    private String operation;
    private String type;

    public XBCSpecDef getSpecDef() {
        return specDef;
    }

    public void setSpecDef(XBCSpecDef specDef) {
        this.specDef = specDef;
    }

    public Long getXbIndex() {
        return xbIndex;
    }

    public void setXbIndex(Long xbIndex) {
        this.xbIndex = xbIndex;
    }

    public Long getRevision() {
        return revision;
    }

    public void setRevision(Long revision) {
        this.revision = revision;
    }

    public String getStringId() {
        return stringId;
    }

    public void setStringId(String stringId) {
        this.stringId = stringId;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String target) {
        this.operation = target;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public XBCRev getTarget() {
        return target;
    }

    public void setTarget(XBCRev target) {
        this.target = target;
    }

    public Long getTargetRevision() {
        return target != null ? target.getXBIndex() : null;
    }

    public void setTargetRevision(Long revision) {
        if (target != null) {
            ((XBERev) target).setXBIndex(revision);
        }
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

    public XBParamType getDefType() {
        return defType;
    }

    public void setDefType(XBParamType defType) {
        this.defType = defType;
    }
}
