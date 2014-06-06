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
package org.xbup.lib.xbcatalog.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import org.xbup.lib.xb.catalog.base.XBCRev;
import org.xbup.lib.xb.catalog.base.XBCSpec;
import org.xbup.lib.xb.catalog.base.XBCSpecDef;
import org.xbup.lib.xb.catalog.base.XBCSpecDefType;

/**
 * Specification definition database entity.
 *
 * @version 0.1 wr22.0 2013/01/11
 * @author XBUP Project (http://xbup.org)
 */
@Entity(name="XBSpecDef")
@Inheritance(strategy = InheritanceType.JOINED)
public class XBESpecDef extends XBEItem implements Serializable, XBCSpecDef {

    @ManyToOne
    private XBERev target;

    public XBESpecDef() {
    }

    @Override
    public XBCRev getTarget() {
        return target;
    }

    public void setTarget(XBERev target) {
        this.target = target;
    }

    @Override
    public XBCSpec getSpec() {
        return (XBCSpec) getParent();
    }

    public void setSpec(XBESpec spec) {
        setParent(spec);
    }

    @Override
    public XBCSpecDefType getType() {
        return null;
    }
}
