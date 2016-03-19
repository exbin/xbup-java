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
package org.exbin.xbup.web.xbcatalogweb.faces;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import org.exbin.xbup.web.xbcatalogweb.base.XBCFullItemRecord;
import org.exbin.xbup.web.xbcatalogweb.base.service.XBCItemRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 * Item edit controller.
 *
 * @version 0.1.23 2014/04/20
 * @author ExBin Project (http://exbin.org)
 */
@Controller
@Scope("view")
@Qualifier("itemEditBacking")
public final class ItemEditBacking implements Serializable {

    @Autowired
    private XBCItemRecordService itemRecordService;

    private XBCFullItemRecord editedItem;
    private Long editedItemId;
    private Boolean lastActionSuccess = false;

    public ItemEditBacking() {
    }

    @PostConstruct
    public void init() {
    }
    
    public void save() {
        lastActionSuccess = false;
        itemRecordService.persistItem(editedItem);
        lastActionSuccess = true;
    }

    public void editItem() {
        editedItem = itemRecordService.findForEditById(editedItemId);
    }

    public void createItem() {
        editedItem = itemRecordService.createForEdit();
    }

    public XBCFullItemRecord getEditedItem() {
        return editedItem;
    }

    public Boolean getLastActionSuccess() {
        return lastActionSuccess;
    }

    public Long getEditedItemId() {
        return editedItemId;
    }

    public void setEditedItemId(Long editedItemId) {
        this.editedItemId = editedItemId;
    }
}
