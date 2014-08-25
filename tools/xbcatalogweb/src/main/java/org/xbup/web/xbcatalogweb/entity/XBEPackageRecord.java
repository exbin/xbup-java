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
package org.xbup.web.xbcatalogweb.entity;

import org.xbup.web.xbcatalogweb.base.XBCPackageRecord;
import java.io.Serializable;
import org.xbup.lib.catalog.entity.XBENode;
import org.xbup.lib.catalog.entity.XBEXDesc;
import org.xbup.lib.catalog.entity.XBEXName;
import org.xbup.lib.catalog.entity.XBEXStri;

/**
 * Package record entity.
 *
 * @version 0.1.23 2014/05/12
 * @author XBUP Project (http://xbup.org)
 */
public class XBEPackageRecord implements  Serializable, XBCPackageRecord {

    private XBENode node;
    private XBEXStri stri;
    private XBEXName name;
    private XBEXDesc desc;
    private String prefix = "";
    private boolean hasChildren = false;
    
    @Override
    public String getFullName() {
        return prefix.isEmpty() ? getPackageName() : getPrefix() + "." + getPackageName();
    }
    
    @Override
    public String getPackageName() {
        return getName() == null ? "" : (getName().getText() == null ? "" : getName().getText());
    }

    @Override
    public Long getId() {
        return node == null ? null : node.getId();
    }

    @Override
    public XBENode getNode() {
        return node;
    }

    @Override
    public void setNode(XBENode node) {
        this.node = node;
    }

    @Override
    public XBEXStri getStri() {
        return stri;
    }

    @Override
    public void setStri(XBEXStri stri) {
        this.stri = stri;
    }

    @Override
    public XBEXName getName() {
        return name;
    }

    @Override
    public void setName(XBEXName name) {
        this.name = name;
    }

    @Override
    public XBEXDesc getDesc() {
        return desc;
    }

    @Override
    public void setDesc(XBEXDesc desc) {
        this.desc = desc;
    }

    @Override
    public String getPrefix() {
        return prefix;
    }

    @Override
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public boolean isHasChildren() {
        return hasChildren;
    }

    @Override
    public void setHasChildren(boolean hasChildren) {
        this.hasChildren = hasChildren;
    }
}
