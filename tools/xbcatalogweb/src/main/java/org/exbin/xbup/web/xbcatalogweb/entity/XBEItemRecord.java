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
package org.exbin.xbup.web.xbcatalogweb.entity;

import java.io.Serializable;
import org.exbin.xbup.catalog.entity.XBEBlockCons;
import org.exbin.xbup.catalog.entity.XBEBlockJoin;
import org.exbin.xbup.catalog.entity.XBEBlockRev;
import org.exbin.xbup.catalog.entity.XBEBlockSpec;
import org.exbin.xbup.catalog.entity.XBEFormatCons;
import org.exbin.xbup.catalog.entity.XBEFormatJoin;
import org.exbin.xbup.catalog.entity.XBEFormatRev;
import org.exbin.xbup.catalog.entity.XBEFormatSpec;
import org.exbin.xbup.catalog.entity.XBEGroupCons;
import org.exbin.xbup.catalog.entity.XBEGroupJoin;
import org.exbin.xbup.catalog.entity.XBEGroupRev;
import org.exbin.xbup.catalog.entity.XBEGroupSpec;
import org.exbin.xbup.catalog.entity.XBEItem;
import org.exbin.xbup.catalog.entity.XBENode;
import org.exbin.xbup.catalog.entity.XBESpecDef;
import org.exbin.xbup.catalog.entity.XBEXDesc;
import org.exbin.xbup.catalog.entity.XBEXName;
import org.exbin.xbup.catalog.entity.XBEXStri;
import org.exbin.xbup.web.xbcatalogweb.base.XBCItemRecord;

/**
 * Item record entity.
 *
 * @version 0.1.23 2014/04/04
 * @author ExBin Project (http://exbin.org)
 */
public class XBEItemRecord implements  Serializable, XBCItemRecord {

    private XBEItem item;
    private XBEXStri stri;
    private XBEXName name;
    private XBEXDesc desc;

    @Override
    public String getItemType() {
        if (item instanceof XBENode) {
            return "Node";
        }
        
        if (item instanceof XBEBlockSpec) {
            return "Block Specification";
        }

        if (item instanceof XBEGroupSpec) {
            return "Group Specification";
        }
        
        if (item instanceof XBEFormatSpec) {
            return "Format Specification";
        }
        
        if (item instanceof XBEBlockRev) {
            return "Block Revision";
        }

        if (item instanceof XBEGroupRev) {
            return "Group Revision";
        }
        
        if (item instanceof XBEFormatRev) {
            return "Format Revision";
        }
        
        if (item instanceof XBESpecDef) {
            return "SpecDef";
        }

        if (item instanceof XBEBlockJoin) {
            return "Block Join";
        }

        if (item instanceof XBEBlockCons) {
            return "Block Consist";
        }

        if (item instanceof XBEGroupJoin) {
            return "Group Join";
        }

        if (item instanceof XBEGroupCons) {
            return "Group Consist";
        }

        if (item instanceof XBEFormatJoin) {
            return "Format Join";
        }

        if (item instanceof XBEFormatCons) {
            return "Format Consist";
        }

        return "Item";
    }
    
    @Override
    public String getNameOrId() {
        if (name == null || name.getText() == null) {
            return "[" + getId() + "]";
        }
        
        return name.getText();
    }

    @Override
    public Long getId() {
        return item == null ? null : item.getId();
    }

    @Override
    public XBEItem getItem() {
        return item;
    }

    @Override
    public void setItem(XBEItem item) {
        this.item = item;
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
}
