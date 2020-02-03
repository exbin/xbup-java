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
package org.exbin.xbup.core.catalog.base.service;

import java.util.Objects;
import javax.annotation.Nullable;
import org.exbin.xbup.core.catalog.base.XBCBlockCons;
import org.exbin.xbup.core.catalog.base.XBCBlockJoin;
import org.exbin.xbup.core.catalog.base.XBCBlockListCons;
import org.exbin.xbup.core.catalog.base.XBCBlockListJoin;
import org.exbin.xbup.core.catalog.base.XBCBlockRev;
import org.exbin.xbup.core.catalog.base.XBCBlockSpec;
import org.exbin.xbup.core.catalog.base.XBCFormatCons;
import org.exbin.xbup.core.catalog.base.XBCFormatJoin;
import org.exbin.xbup.core.catalog.base.XBCFormatRev;
import org.exbin.xbup.core.catalog.base.XBCFormatSpec;
import org.exbin.xbup.core.catalog.base.XBCGroupCons;
import org.exbin.xbup.core.catalog.base.XBCGroupJoin;
import org.exbin.xbup.core.catalog.base.XBCGroupRev;
import org.exbin.xbup.core.catalog.base.XBCGroupSpec;
import org.exbin.xbup.core.catalog.base.XBCItem;
import org.exbin.xbup.core.catalog.base.XBCNode;

/**
 * Table model for catalog specifications.
 *
 * @version 0.2.1 2020/02/03
 * @author ExBin Project (http://exbin.org)
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
        setTypeFromItem();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + Objects.hashCode(this.item.getId());
        return hash;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final XBItemWithDetail other = (XBItemWithDetail) obj;
        return Objects.equals(this.item.getId(), other.item.getId());
    }

    private void setTypeFromItem() {
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
