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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import org.xbup.lib.core.catalog.base.XBCBlockSpec;
import org.xbup.lib.core.catalog.base.XBCItemLimi;
import org.xbup.lib.core.catalog.base.XBCLimitSpec;

/**
 * Item limitations database entity.
 *
 * @version 0.1.21 2011/08/21
 * @author XBUP Project (http://xbup.org)
 */
@Entity(name="XBItemLimi")
public class XBEItemLimi implements XBCItemLimi, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private XBEBlockSpec owner;
    @ManyToOne
    private XBELimitSpec target;

    private Long xbIndex;

    public XBEItemLimi() {
    }

    @Override
    public XBCBlockSpec getOwner() {
        return (XBCBlockSpec) owner;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public XBCLimitSpec getTarget() {
        return target;
    }

    @Override
    public Long getXBIndex() {
        return xbIndex;
    }

    public void setOwner(XBEBlockSpec owner) {
        this.owner = owner;
    }

    public void setTarget(XBELimitSpec target) {
        this.target = target;
    }

    public void setXbIndex(Long xbIndex) {
        this.xbIndex = xbIndex;
    }
}
