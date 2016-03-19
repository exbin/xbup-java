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
import java.util.List;
import javax.annotation.PostConstruct;
import org.exbin.xbup.web.xbcatalogweb.base.XBCItemRecord;
import org.exbin.xbup.web.xbcatalogweb.base.XBCPackageRecord;
import org.exbin.xbup.web.xbcatalogweb.base.service.XBCItemRecordService;
import org.exbin.xbup.web.xbcatalogweb.base.service.XBCPackageRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

/**
 * Browse controller.
 *
 * @version 0.1.24 2015/01/16
 * @author ExBin Project (http://exbin.org)
 */
@Controller
@Scope("view")
@Qualifier("browseBacking")
public final class BrowseBacking implements Serializable {

    @Autowired
    private XBCPackageRecordService packageService;
    @Autowired
    private XBCItemRecordService itemService;
    @Autowired
    private ItemBacking itemBacking;

    private Long selectedPackageId;
    private XBCPackageRecord selectedPackage;
    private Long selectedItemId;

    private List<XBCPackageRecord> packages;
    private List<XBCItemRecord> items;

    public BrowseBacking() {
        /* nodes = new ArrayList<XBENode>();
         XBENode node = new XBENode();
         node.setId(new Long(1));
         nodes.add(node); */
    }

    @PostConstruct
    public void init() {
        selectedPackageId = null;
        packages = packageService.findFullTree();
        items = itemService.findAllByParent(null);
    }

    public void selectPackage() {
        loadItems();
    }

    public void selectItem() {
        itemBacking.selectItem(selectedItemId);
    }

    public void loadItems() {
        selectedPackage = packageService.getItem(selectedPackageId);
        items = itemService.findAllByParent(selectedPackageId);
    }

    public List<XBCPackageRecord> getPackages() {
        return packages;
    }

    public List<XBCItemRecord> getItems() {
        return items;
    }

    public Long getSelectedPackageId() {
        return selectedPackageId;
    }

    public void setSelectedPackageId(Long selectedPackageId) {
        this.selectedPackageId = selectedPackageId;
    }

    public Long getSelectedItemId() {
        return selectedItemId;
    }

    public void setSelectedItemId(Long selectedItemId) {
        this.selectedItemId = selectedItemId;
    }

    public XBCPackageRecord getSelectedPackage() {
        return selectedPackage;
    }
}
