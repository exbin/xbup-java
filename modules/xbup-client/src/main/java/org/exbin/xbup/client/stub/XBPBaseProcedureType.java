/*
 * Copyright (C) ExBin Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.exbin.xbup.client.stub;

import org.exbin.xbup.core.block.XBBlockType;

/**
 * Plain class for list of basic procedures.
 *
 * @version 0.1.25 2015/03/21
 * @author ExBin Project (http://exbin.org)
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
