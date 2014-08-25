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
package org.xbup.lib.catalog.entity.manager;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.NoResultException;
import org.springframework.stereotype.Repository;
import org.xbup.lib.core.catalog.base.manager.XBCTranManager;
import org.xbup.lib.catalog.XBECatalog;
import org.xbup.lib.catalog.entity.XBEItem;
import org.xbup.lib.catalog.entity.XBETran;

/**
 * XBUP catalog transformation manager.
 *
 * @version 0.1.21 2011/12/29
 * @author XBUP Project (http://xbup.org)
 */
@Repository
public class XBETranManager extends XBEDefaultManager<XBETran> implements XBCTranManager<XBETran>, Serializable {

    public XBETranManager() {
        super();
    }

    public XBETranManager(XBECatalog catalog) {
        super(catalog);
    }

    @Override
    public Long getAllItemsCount() {
        try {
            return (Long) em.createQuery("SELECT count(o) FROM XBItem as o").getSingleResult();
        } catch (NoResultException ex) {
            Logger.getLogger(XBETranManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (Exception ex) {
            Logger.getLogger(XBETranManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public XBEItem findById(long id) {
        try {
            return (XBEItem) em.createQuery("SELECT object(o) FROM XBItem as o WHERE o.id = " + id).getSingleResult();
        } catch (NoResultException ex) {
//            Logger.getLogger(XBETranManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (Exception ex) {
            Logger.getLogger(XBETranManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
