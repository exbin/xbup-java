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
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.xbup.lib.catalog.entity.XBEXUser;
import org.xbup.lib.catalog.entity.service.XBEXUserService;
import org.xbup.lib.core.catalog.base.XBCXUser;
import org.xbup.web.xbcatalogweb.service.XBEUserRecordService;

/**
 * Authentication controller.
 *
 * @version 0.1.23 2014/05/29
 * @author ExBin Project (http://exbin.org)
 */
@Controller
@Scope("session")
@Qualifier("authBacking")
public final class AuthBacking implements Serializable {

    @Autowired
    private XBEXUserService userService;
    @Autowired
    private XBEUserRecordService userRecordService;

    private String userLogin = null;
    private XBEXUser selectedUser;
    private Long userId = null;
    private Boolean lastActionSuccess = false;

    private Boolean resetPassword = false;
    private String password = null;
    private String repeatedPassword = null;
    
    public AuthBacking() {
    }

    @PostConstruct
    public void init() {
        userId = null;
        userLogin = null;
        
        // Autologin to admin for testing purposes
        // userId = 1l;
        // userLogin = "admin";
    }

    public void loginAttempt() {
        lastActionSuccess = false;
        password = "";
    }

    public void registerAttempt() {
        selectedUser = new XBEXUser();
        password = "";
        repeatedPassword = "";
    }

    public void login() {
        lastActionSuccess = false;
        XBCXUser user = userService.findByLogin(userLogin);
        if (user != null) {
            try {
                String passwordHash = md5(password);

                if (user.getPasswd().equals(passwordHash)) {
                    userId = user.getId();
                    lastActionSuccess = true;
                } else {
                    FacesMessage errorMessage = new FacesMessage("Incorrect password or user login");
                    errorMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
                    FacesContext.getCurrentInstance().addMessage(
                            "loginPopupForm:password", errorMessage);
                }
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(AuthBacking.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            FacesMessage errorMessage = new FacesMessage("Incorrect password or user login");
            errorMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage(
                    "loginPopupForm:password", errorMessage);
        }
    }

    public void register() {
        lastActionSuccess = false;
        
        if (!password.equals(repeatedPassword)) {
            FacesMessage errorMessage = new FacesMessage("Password doesn't match");
            errorMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
            FacesContext.getCurrentInstance().addMessage(
                    "editItemForm:password", errorMessage);
        }
        try {
            selectedUser.setPasswd(AuthBacking.md5(password));
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(UserBacking.class.getName()).log(Level.SEVERE, null, ex);
        }

        userRecordService.persistUser(selectedUser);
        userId = selectedUser.getId();
        userLogin = selectedUser.getLogin();
        lastActionSuccess = true;
    }

    public void editProfile() {
        selectedUser = userService.getItem(userId);
        password = "";
        repeatedPassword = "";
    }
    
    public void updateProfile() {
        lastActionSuccess = false;
        
        if (resetPassword) {
            if (!password.equals(repeatedPassword)) {
                FacesMessage errorMessage = new FacesMessage("Password doesn't match");
                errorMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
                FacesContext.getCurrentInstance().addMessage(
                        "editItemForm:password", errorMessage);
            }
            try {
                selectedUser.setPasswd(AuthBacking.md5(password));
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(UserBacking.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        userRecordService.persistUser(selectedUser);
        lastActionSuccess = true;
    }

    public void logout() {
        userId = null;
        userLogin = null;
    }

    public void loginRedirectListener() {
        FacesContext.getCurrentInstance()
                .getApplication()
                .getNavigationHandler()
                .handleNavigation(FacesContext.getCurrentInstance(), null,
                        "/views/login.xhtml?faces-redirect=true");
    }

    public void forwardIfNotLogged(ComponentSystemEvent cse) {
        String viewId = FacesContext.getCurrentInstance().getViewRoot().getViewId();
        if (!getAuthenticated() && !viewId.startsWith("/views/login")) {
            FacesContext.getCurrentInstance()
                    .getApplication()
                    .getNavigationHandler()
                    .handleNavigation(FacesContext.getCurrentInstance(), null,
                            "/views/login.xhtml?faces-redirect=true");
        }
    }

    public boolean getAuthenticated() {
        return userId != null;
    }

    public static String md5(String input) throws NoSuchAlgorithmException {
        String result = input;
        if (input != null) {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(input.getBytes());
            BigInteger hash = new BigInteger(1, md.digest());
            result = hash.toString(16);
            while (result.length() < 32) {
                result = "0" + result;
            }
        }

        return result;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public Boolean getLastActionSuccess() {
        return lastActionSuccess;
    }

    public Boolean getResetPassword() {
        return resetPassword;
    }

    public void setResetPassword(Boolean resetPassword) {
        this.resetPassword = resetPassword;
    }

    public XBEXUser getSelectedUser() {
        return selectedUser;
    }

    public void setSelectedUser(XBEXUser selectedUser) {
        this.selectedUser = selectedUser;
    }
}
