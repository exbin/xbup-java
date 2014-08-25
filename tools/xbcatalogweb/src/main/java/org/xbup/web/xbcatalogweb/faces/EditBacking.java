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
package org.xbup.web.xbcatalogweb.faces;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.xbup.web.xbcatalogweb.base.XBCItemRecord;
import org.xbup.web.xbcatalogweb.base.service.XBCItemRecordService;
import org.xbup.web.xbcatalogweb.utils.PagedList;

/**
 * Browse controller.
 *
 * @version 0.1.23 2014/04/10
 * @author XBUP Project (http://xbup.org)
 */
@Controller
@Scope("view")
@Qualifier("browseBacking")
public final class EditBacking implements Serializable {

    @Autowired
    private XBCItemRecordService itemRecordService;
    @Autowired
    private ItemEditBacking itemEditBacking;

    private String searchId;
    private String searchStringId;
    private String searchXbIndex;
    private String searchType;
    private String searchName;
    private String searchDescription;

    private Integer dataPage = 1;
    private Integer dataRowsPerPage = 20;
    private Integer[] rowsPerPage = {10, 20, 30, 50, 100};
    private Boolean lastActionSuccess = false;

    private Long selectedItemId;

    private ItemPagedList items;

    public EditBacking() {
        /* nodes = new ArrayList<XBENode>();
         XBENode node = new XBENode();
         node.setId(new Long(1));
         nodes.add(node); */
    }

    @PostConstruct
    public void init() {
        items = new ItemPagedList();
    }

    public List<XBCItemRecord> getItems() {
        return items;
    }

    public void search() {
        String searchCondition;
        searchCondition = (searchId == null || searchId.isEmpty() ? ""
                : "LOWER(item.id) LIKE '%"
                + searchId.toLowerCase().replace("'", "''") + "%'");
        searchCondition += (searchStringId == null || searchStringId.isEmpty() ? ""
                : (!searchCondition.isEmpty() ? " AND " : "")
                + "LOWER(stri.text) LIKE '%"
                + searchStringId.toLowerCase().replace("'", "''") + "%'");
        searchCondition += (searchXbIndex == null || searchXbIndex.isEmpty() ? ""
                : (!searchCondition.isEmpty() ? " AND " : "")
                + "LOWER(item.xbIndex) LIKE '%"
                + searchXbIndex.toLowerCase().replace("'", "''")
                + "%'");
        searchCondition += (searchType == null || searchType.isEmpty() ? ""
                : (!searchCondition.isEmpty() ? " AND " : "")
                + "LOWER(item.dtype) LIKE '%"
                + searchType.toLowerCase().replace("'", "''") + "%'");
        searchCondition += (searchName == null || searchName.isEmpty() ? ""
                : (!searchCondition.isEmpty() ? " AND " : "")
                + "LOWER(name.text) LIKE '%" + searchName.replace("'", "''")
                + "%'");
        searchCondition += (searchDescription == null || searchDescription.isEmpty() ? ""
                : (!searchCondition.isEmpty() ? " AND " : "")
                + "LOWER(dsc.text) LIKE '%"
                + searchDescription.replace("'", "''") + "%'");

        items.filter(searchCondition);
        dataPage = 1;
    }

    public void itemEdited() {
        itemEditBacking.save();
        items.reload();
    }

    public void sortById() {
        items.reorder("item.id".equals(items.getOrderCondition()) ? "item.id DESC" : "item.id");
    }

    public void sortByStringId() {
        items.reorder("stri.text, item.id".equals(items.getOrderCondition()) ? "stri.text DESC, item.id DESC" : "stri.text, item.id");
    }

    public void sortByXbIndex() {
        items.reorder("item.xbIndex, item.id".equals(items.getOrderCondition()) ? "item.xbIndex DESC, item.id DESC" : "item.xbIndex, item.id");
    }

    public void sortByType() {
        items.reorder("item.dtype, item.id".equals(items.getOrderCondition()) ? "item.dtype DESC, item.id DESC" : "item.dtype, item.id");
    }

    public void sortByName() {
        items.reorder("name.text, item.id".equals(items.getOrderCondition()) ? "name.text DESC, item.id DESC" : "name.text, item.id");
    }

    public void sortByDescription() {
        items.reorder("dsc.text, item.id".equals(items.getOrderCondition()) ? "dsc.text DESC, item.id DESC" : "dsc.text, item.id");
    }

    public void editItem() {
        itemEditBacking.setEditedItemId(selectedItemId);
        itemEditBacking.editItem();
        lastActionSuccess = true;
    }

    public void createItem() {
        itemEditBacking.setEditedItemId(new Long(0));
        itemEditBacking.createItem();
        lastActionSuccess = true;
    }

    public String getDataInfo() {
        int rows = items.size();
        return "(Entries: "
                + (((dataPage - 1) * dataRowsPerPage) + 1)
                + " - "
                + (dataPage * dataRowsPerPage > rows ? rows : dataPage
                * dataRowsPerPage) + " of " + rows + ", Page: "
                + dataPage + "/" + ((rows / dataRowsPerPage) + 1) + ")";
    }

    public Integer getDataPage() {
        return dataPage;
    }

    public void setDataPage(Integer dataPage) {
        this.dataPage = dataPage;
    }

    public Integer getPageJump() {
        return null;
    }

    public void setPageJump(Integer dataPage) {
        if (dataPage != null) {
            this.dataPage = dataPage;
        }
    }

    public Integer getDataRowsPerPage() {
        return dataRowsPerPage;
    }

    public void setDataRowsPerPage(Integer dataRowsPerPage) {
        this.dataRowsPerPage = dataRowsPerPage;
    }

    public Integer[] getRowsPerPage() {
        return rowsPerPage;
    }

    public void setRowsPerPage(Integer[] rowsPerPage) {
        this.rowsPerPage = rowsPerPage;
    }

    public Long getSelectedItemId() {
        return selectedItemId;
    }

    public void setSelectedItemId(Long selectedItemId) {
        this.selectedItemId = selectedItemId;
    }

    public Boolean getLastActionSuccess() {
        return lastActionSuccess;
    }

    public String getSearchId() {
        return searchId;
    }

    public void setSearchId(String searchId) {
        this.searchId = searchId;
    }

    public String getSearchStringId() {
        return searchStringId;
    }

    public void setSearchStringId(String searchStringId) {
        this.searchStringId = searchStringId;
    }

    public String getSearchXbIndex() {
        return searchXbIndex;
    }

    public void setSearchXbIndex(String searchXbIndex) {
        this.searchXbIndex = searchXbIndex;
    }

    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    public String getSearchName() {
        return searchName;
    }

    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }

    public String getSearchDescription() {
        return searchDescription;
    }

    public void setSearchDescription(String searchDescription) {
        this.searchDescription = searchDescription;
    }

    private class ItemPagedList extends PagedList<XBCItemRecord> implements Serializable {

        private final int PAGE_SIZE = 20;
        private final List<List<XBCItemRecord>> pages;
        private int size;
        private String orderCondition;
        private String filterCondition = "";

        public ItemPagedList() {
            size = itemRecordService.findAllPagedCount(filterCondition);

            pages = new ArrayList<List<XBCItemRecord>>();
            for (int i = 0; i < size / PAGE_SIZE + 1; i++) {
                pages.add(null);
            }

            orderCondition = "item.id";
        }

        @Override
        public XBCItemRecord get(int index) {
            int page = index / PAGE_SIZE;
            if (pages.get(page) == null) {
                pages.set(page, itemRecordService.findAllPaged(
                        page * PAGE_SIZE, PAGE_SIZE, filterCondition,
                        orderCondition));
            }

            return pages.get(page).get(index % PAGE_SIZE);
        }

        public void reorder(String orderCondition) {
            this.orderCondition = orderCondition;
            reload();
        }

        public void filter(String filterCondition) {
            this.filterCondition = filterCondition;
            reload();
        }

        public void reload() {
            for (int i = 0; i < pages.size(); i++) {
                pages.set(i, null);
            }

            size = itemRecordService.findAllPagedCount(filterCondition);
            for (int i = pages.size(); i < size / PAGE_SIZE + 1; i++) {
                pages.add(null);
            }
        }

        public String getOrderCondition() {
            return orderCondition;
        }

        @Override
        public boolean isEmpty() {
            return size == 0;
        }

        @Override
        public int size() {
            return size;
        }
    }
}
