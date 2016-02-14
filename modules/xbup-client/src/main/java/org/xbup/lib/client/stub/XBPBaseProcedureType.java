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
package org.xbup.lib.client.stub;

import org.xbup.lib.core.block.XBBlockType;

/**
 * Plain class for list of basic procedures.
 *
 * @version 0.1.25 2015/03/21
 * @author XBUP Project (http://xbup.org)
 */
public class XBPBaseProcedureType {

    private XBBlockType createItemType;
    private XBBlockType removeItemType;
    private XBBlockType itemByIdType;
    private XBBlockType allItemsType;
    private XBBlockType itemsCountType;

    public XBPBaseProcedureType(XBBlockType createItemType, XBBlockType removeItemType, XBBlockType itemByIdType, XBBlockType allItemsType, XBBlockType itemsCountType) {
        this.createItemType = createItemType;
        this.removeItemType = removeItemType;
        this.itemByIdType = itemByIdType;
        this.allItemsType = allItemsType;
        this.itemsCountType = itemsCountType;
    }

    public XBBlockType getCreateItemType() {
        return createItemType;
    }

    public void setCreateItemType(XBBlockType createItemType) {
        this.createItemType = createItemType;
    }

    public XBBlockType getRemoveItemType() {
        return removeItemType;
    }

    public void setRemoveItemType(XBBlockType removeItemType) {
        this.removeItemType = removeItemType;
    }

    public XBBlockType getItemByIdType() {
        return itemByIdType;
    }

    public void setItemByIdType(XBBlockType itemByIdType) {
        this.itemByIdType = itemByIdType;
    }

    public XBBlockType getAllItemsType() {
        return allItemsType;
    }

    public void setAllItemsType(XBBlockType allItemsType) {
        this.allItemsType = allItemsType;
    }

    public XBBlockType getItemsCountType() {
        return itemsCountType;
    }

    public void setItemsCountType(XBBlockType itemsCountType) {
        this.itemsCountType = itemsCountType;
    }
}
