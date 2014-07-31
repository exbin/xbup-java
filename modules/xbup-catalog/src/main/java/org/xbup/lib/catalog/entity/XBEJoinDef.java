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
package org.xbup.lib.catalog.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import org.xbup.lib.core.catalog.base.XBCJoinDef;
import org.xbup.lib.core.catalog.base.XBCSpec;
import org.xbup.lib.core.catalog.base.XBCSpecDefType;

/**
 * Join database database entity.
 *
 * @version 0.1 wr22.0 2013/01/11
 * @author XBUP Project (http://xbup.org)
 */
@Entity(name="XBJoinDef")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class XBEJoinDef extends XBESpecDef implements Serializable, XBCJoinDef {

    public XBEJoinDef() {
    }

    @Override
    public XBCSpec getSpec() {
        return (XBCSpec) super.getSpec();
    }

    @Override
    public XBCSpecDefType getType() {
        return XBCSpecDefType.JOIN;
    }
}
