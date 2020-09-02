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
package org.exbin.xbup.catalog.entity.manager;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.persistence.NoResultException;
import org.exbin.xbup.catalog.XBECatalog;
import org.exbin.xbup.catalog.entity.XBEXUser;
import org.exbin.xbup.core.catalog.base.XBCXUser;
import org.exbin.xbup.core.catalog.base.manager.XBCXUserManager;
import org.springframework.stereotype.Repository;

/**
 * XBUP catalog limitation manager.
 *
 * @version 0.2.1 2020/08/26
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
@Repository
public class XBEXUserManager extends XBEDefaultCatalogManager<XBCXUser> implements XBCXUserManager, Serializable {

    public XBEXUserManager() {
        super();
    }

    public XBEXUserManager(XBECatalog catalog) {
        super(catalog);
    }

    @Nonnull
    @Override
    public Class getEntityClass() {
        return XBEXUser.class;
    }

    public XBEXUser findByLogin(String userLogin) {
        try {
            return (XBEXUser) em.createQuery("SELECT object(o) FROM XBXUser as o WHERE o.login = '" + DatabaseUtils.sqlEscapeString(userLogin.trim().toLowerCase()) + "'").getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception ex) {
            Logger.getLogger(XBENodeManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
