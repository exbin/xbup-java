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
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.xbup.lib.catalog.entity.XBEXUser;
import org.xbup.web.xbcatalogweb.base.XBCUserRecord;
import org.xbup.web.xbcatalogweb.base.service.XBCUserRecordService;
import org.xbup.web.xbcatalogweb.entity.XBEUserRecord;
import org.xbup.web.xbcatalogweb.utils.PagedList;

/**
 * Persistence manager for XBCXUser items.
 *
 * @version 0.1.23 2014/05/29
 * @author XBUP Project (http://xbup.org)
 */
@Controller
@Scope("view")
@Qualifier("userBacking")
public final class UserBacking implements Serializable {

    @Autowired
    private XBCUserRecordService userRecordService;

    private Integer dataPage = 1;
    private Integer dataRowsPerPage = 20;
    private Integer[] rowsPerPage = {10, 20, 30, 50, 100};
    private Boolean lastActionSuccess = false;
    private Integer selectedItemId;
    private XBEUserRecord selectedUser;

    private Boolean resetPassword = false;
    private String password = null;
    private String repeatedPassword = null;

    private String searchId;
    private String searchLogin;
    private String searchEmail;
    private String searchName;

    private UserPagedList items;

    public UserBacking() {

    }

    @PostConstruct
    public void init() {
        items = new UserPagedList();
    }

    public void search() {
        String searchCondition;
        searchCondition = (searchId == null || searchId.isEmpty() ? ""
                : "LOWER(item.id) LIKE '%"
                + searchId.toLowerCase().replace("'", "''") + "%'");
        searchCondition += (searchLogin == null || searchLogin.isEmpty() ? ""
                : (!searchCondition.isEmpty() ? " AND " : "")
                + "LOWER(stri.text) LIKE '%"
                + searchLogin.toLowerCase().replace("'", "''") + "%'");
        searchCondition += (searchEmail == null || searchEmail.isEmpty() ? ""
                : (!searchCondition.isEmpty() ? " AND " : "")
                + "LOWER(stri.text) LIKE '%"
                + searchEmail.toLowerCase().replace("'", "''") + "%'");
        searchCondition += (searchName == null || searchName.isEmpty() ? ""
                : (!searchCondition.isEmpty() ? " AND " : "")
                + "LOWER(stri.text) LIKE '%"
                + searchName.toLowerCase().replace("'", "''") + "%'");
    }

    public void editItem() {
        lastActionSuccess = false;
        selectedUser = (XBEUserRecord) userRecordService.getItem(selectedItemId);
        resetPassword = false;
        lastActionSuccess = true;
    }

    public void deleteItem() {
        lastActionSuccess = false;
        selectedUser = (XBEUserRecord) userRecordService.getItem(selectedItemId);
        lastActionSuccess = true;
    }

    public void createItem() {
        lastActionSuccess = false;
        selectedUser = new XBEUserRecord();
        selectedUser.setUser(new XBEXUser());
        resetPassword = true;
        lastActionSuccess = true;
    }
    
    public void updateUser() {
        lastActionSuccess = false;

        if (resetPassword) {
            if (!password.equals(repeatedPassword)) {
                FacesMessage errorMessage = new FacesMessage("Password doesn't match");
                errorMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
                FacesContext.getCurrentInstance().addMessage(
                        "editItemForm:password", errorMessage);
                return;
            }
            try {
                selectedUser.getUser().setPasswd(AuthBacking.md5(password));
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(UserBacking.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        userRecordService.persistItem(selectedUser);

        items.reload();
        lastActionSuccess = true;
    }

    public void deleteUser() {
        lastActionSuccess = false;
        userRecordService.removeItem(selectedUser);
        items.reload();
        lastActionSuccess = true;
    }
    
    public void sortById() {
        items.reorder("user.id".equals(items.getOrderCondition()) ? "user.id DESC" : "user.id");
    }

    public void sortByLogin() {
        items.reorder("user.login, user.id".equals(items.getOrderCondition()) ? "user.login DESC, user.id DESC" : "user.login, user.id");
    }

    public void sortByEmail() {
        items.reorder("user.email, user.id".equals(items.getOrderCondition()) ? "user.email DESC, user.id DESC" : "user.email, user.id");
    }

    public void sortByName() {
        items.reorder("user.fullName, user.id".equals(items.getOrderCondition()) ? "user.fullName DESC, user.id DESC" : "user.fullName, user.id");
    }

    public void sortByLastAccess() {
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

    public Integer getSelectedItemId() {
        return selectedItemId;
    }

    public void setSelectedItemId(Integer selectedItemId) {
        this.selectedItemId = selectedItemId;
    }

    public String getSearchId() {
        return searchId;
    }

    public void setSearchId(String searchId) {
        this.searchId = searchId;
    }

    public String getSearchLogin() {
        return searchLogin;
    }

    public void setSearchLogin(String searchLogin) {
        this.searchLogin = searchLogin;
    }

    public String getSearchEmail() {
        return searchEmail;
    }

    public void setSearchEmail(String searchEmail) {
        this.searchEmail = searchEmail;
    }

    public String getSearchName() {
        return searchName;
    }

    public void setSearchName(String searchName) {
        this.searchName = searchName;
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

    public List<XBCUserRecord> getItems() {
        return items;
    }

    public Boolean getLastActionSuccess() {
        return lastActionSuccess;
    }

    public XBEUserRecord getSelectedUser() {
        return selectedUser;
    }

    public Boolean getResetPassword() {
        return resetPassword;
    }

    public void setResetPassword(Boolean resetPassword) {
        this.resetPassword = resetPassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRepeatedPassword() {
        return repeatedPassword;
    }

    public void setRepeatedPassword(String repeatedPassword) {
        this.repeatedPassword = repeatedPassword;
    }

    private class UserPagedList extends PagedList<XBCUserRecord> implements Serializable {

        private final int PAGE_SIZE = 20;
        private final List<List<XBCUserRecord>> pages;
        private int size;
        private String orderCondition;
        private String filterCondition = "";

        public UserPagedList() {
            size = userRecordService.findAllPagedCount(filterCondition);

            pages = new ArrayList<List<XBCUserRecord>>();
            for (int i = 0; i < size / PAGE_SIZE + 1; i++) {
                pages.add(null);
            }

            orderCondition = "user.id";
        }

        @Override
        public XBEUserRecord get(int index) {
            int page = index / PAGE_SIZE;
            if (pages.get(page) == null) {
                pages.set(page, userRecordService.findAllPaged(
                        page * PAGE_SIZE, PAGE_SIZE, filterCondition,
                        orderCondition));
            }

            return (XBEUserRecord) pages.get(page).get(index % PAGE_SIZE);
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

            size = userRecordService.findAllPagedCount(filterCondition);
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
