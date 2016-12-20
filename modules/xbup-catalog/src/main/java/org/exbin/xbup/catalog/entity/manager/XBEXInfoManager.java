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
import org.exbin.xbup.catalog.entity.XBEXItemInfo;
import org.exbin.xbup.core.catalog.base.XBCNode;
import org.exbin.xbup.core.catalog.base.manager.XBCXInfoManager;
import org.springframework.stereotype.Repository;

/**
 * XBUP catalog infomation manager.
 *
 * @version 0.1.21 2011/12/31
 * @author ExBin Project (http://exbin.org)
 */
@Repository
public class XBEXInfoManager extends XBEDefaultCatalogManager<XBEXItemInfo> implements XBCXInfoManager<XBEXItemInfo>, Serializable {

    public XBEXInfoManager() {
        super();
    }

    public XBEXInfoManager(XBECatalog catalog) {
        super(catalog);
    }

    @Override
    public XBEXItemInfo getNodeInfo(XBCNode dir) {
        if (dir == null) {
            return null;
        }
        try {
            return (XBEXItemInfo) em.createQuery("SELECT object(o) FROM XBItemInfo as o WHERE o.parent.id = " + dir.getId()).getSingleResult();
        } catch (NoResultException ex) {
            return null;
        } catch (Exception ex) {
            Logger.getLogger(XBEXInfoManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public XBEXItemInfo findById(long id) {
        try {
            return (XBEXItemInfo) em.createQuery("SELECT object(o) FROM XBItemInfo as o WHERE o.id = " + id).getSingleResult();
        } catch (NoResultException ex) {
            return null;
        } catch (Exception ex) {
            Logger.getLogger(XBEXInfoManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
