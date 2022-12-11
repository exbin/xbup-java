/*
 * Copyright (C) ExBin Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
 * @author ExBin Project (https://exbin.org)
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
