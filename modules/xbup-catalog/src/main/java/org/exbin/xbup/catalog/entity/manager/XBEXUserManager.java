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
package org.exbin.xbup.catalog.entity.manager;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.NoResultException;
import org.exbin.xbup.catalog.XBECatalog;
import org.exbin.xbup.catalog.entity.XBEXUser;
import org.exbin.xbup.core.catalog.base.manager.XBCXUserManager;
import org.springframework.stereotype.Repository;

/**
 * XBUP catalog limitation manager.
 *
 * @version 0.1.21 2011/12/29
 * @author ExBin Project (http://exbin.org)
 */
@Repository
public class XBEXUserManager extends XBEDefaultCatalogManager<XBEXUser> implements XBCXUserManager<XBEXUser>, Serializable {

    public XBEXUserManager() {
        super();
    }

    public XBEXUserManager(XBECatalog catalog) {
        super(catalog);
    }

    public XBEXUser findByLogin(String userLogin) {
        try {
            return (XBEXUser) em.createQuery("SELECT object(o) FROM XBXUser as o WHERE o.login = '" + userLogin.replace("'", "''").trim().toLowerCase() + "'").getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception ex) {
            Logger.getLogger(XBENodeManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
