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
package org.xbup.web.xbcatalogweb.faces;

import java.io.Serializable;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.xbup.web.xbcatalogweb.base.XBCFullItemRecord;
import org.xbup.web.xbcatalogweb.base.service.XBCItemRecordService;
import org.xbup.web.xbcatalogweb.entity.XBEFullItemRecord;

/**
 * Browse controller.
 *
 * @version 0.1.24 2015/01/16
 * @author ExBin Project (http://exbin.org)
 */
@Controller
@Scope("view")
@Qualifier("itemBacking")
public final class ItemBacking implements Serializable {

    @Autowired
    private XBCItemRecordService itemService;

    private XBEFullItemRecord selectedItem;

    public ItemBacking() {
    }

    @PostConstruct
    public void init() {
        Map<String, String> requestParam = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String node = requestParam.get("node");
        if (node != null) {
            setNode(node);
            return;
        }

        String format = requestParam.get("format");
        if (format != null && !format.isEmpty()) {
            setFormat(format);
            return;
        }

        String group = requestParam.get("group");
        if (group != null && !group.isEmpty()) {
            setGroup(group);
            return;
        }

        String block = requestParam.get("block");
        if (block != null && !block.isEmpty()) {
            setBlock(block);
        }
    }

    public void selectItem(Long selectedItemId) {
        selectedItem = (XBEFullItemRecord) itemService.findForEditById(selectedItemId);
    }

    public void setNode(String path) {
        selectedItem = (XBEFullItemRecord) itemService.findNodeByPath(itemService.stringToPath(path));
    }

    public void setFormat(String path) {
        selectedItem = (XBEFullItemRecord) itemService.findFormatSpecByPath(itemService.stringToPath(path));
    }

    public void setGroup(String path) {
        selectedItem = (XBEFullItemRecord) itemService.findGroupSpecByPath(itemService.stringToPath(path));
    }

    public void setBlock(String path) {
        selectedItem = (XBEFullItemRecord) itemService.findBlockSpecByPath(itemService.stringToPath(path));
    }

    public XBCFullItemRecord getSelectedItem() {
        return selectedItem;
    }
}
