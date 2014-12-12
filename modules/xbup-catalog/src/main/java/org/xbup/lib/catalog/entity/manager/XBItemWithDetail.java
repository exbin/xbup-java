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
package org.xbup.lib.catalog.entity.manager;

import java.util.Objects;
import org.xbup.lib.core.catalog.base.XBCBlockCons;
import org.xbup.lib.core.catalog.base.XBCBlockJoin;
import org.xbup.lib.core.catalog.base.XBCBlockListCons;
import org.xbup.lib.core.catalog.base.XBCBlockListJoin;
import org.xbup.lib.core.catalog.base.XBCBlockRev;
import org.xbup.lib.core.catalog.base.XBCBlockSpec;
import org.xbup.lib.core.catalog.base.XBCFormatCons;
import org.xbup.lib.core.catalog.base.XBCFormatJoin;
import org.xbup.lib.core.catalog.base.XBCFormatRev;
import org.xbup.lib.core.catalog.base.XBCFormatSpec;
import org.xbup.lib.core.catalog.base.XBCGroupCons;
import org.xbup.lib.core.catalog.base.XBCGroupJoin;
import org.xbup.lib.core.catalog.base.XBCGroupRev;
import org.xbup.lib.core.catalog.base.XBCGroupSpec;
import org.xbup.lib.core.catalog.base.XBCItem;
import org.xbup.lib.core.catalog.base.XBCNode;

/**
 * Table model for catalog specifications.
 *
 * @version 0.1.24 2014/12/11
 * @author XBUP Project (http://xbup.org)
 */
public class XBItemWithDetail {

    private XBCItem item;
    private String name;
    private String description;
    private String stringId;
    private String type;

    public XBItemWithDetail() {
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

    public String getStringId() {
        return stringId;
    }

    public void setStringId(String stringId) {
        this.stringId = stringId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public XBCItem getItem() {
        return item;
    }

    public void setItem(XBCItem item) {
        this.item = item;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Objects.hashCode(this.item.getId());
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final XBItemWithDetail other = (XBItemWithDetail) obj;
        return Objects.equals(this.item.getId(), other.item.getId());
    }

    void setTypeFromItem() {
        if (item instanceof XBCNode) {
            type = "Node";
        } else if (item instanceof XBCFormatSpec) {
            type = "Format Specification";
        } else if (item instanceof XBCGroupSpec) {
            type = "Group Specification";
        } else if (item instanceof XBCBlockSpec) {
            type = "Block Specification";
        } else if (item instanceof XBCFormatRev) {
            type = "Format Revision";
        } else if (item instanceof XBCGroupRev) {
            type = "Group Revision";
        } else if (item instanceof XBCBlockRev) {
            type = "Block Revision";
        } else if (item instanceof XBCFormatJoin) {
            type = "Format Join";
        } else if (item instanceof XBCFormatCons) {
            type = "Format Consist";
        } else if (item instanceof XBCGroupJoin) {
            type = "Group Join";
        } else if (item instanceof XBCGroupCons) {
            type = "Group Consist";
        } else if (item instanceof XBCBlockJoin) {
            type = "Block Join";
        } else if (item instanceof XBCBlockCons) {
            type = "Block Consist";
        } else if (item instanceof XBCBlockListJoin) {
            type = "Block List Join";
        } else if (item instanceof XBCBlockListCons) {
            type = "Block List Consist";
        } else {
            type = "TODO:" + item.getClass().getName();
        }

    }

}
