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
package org.xbup.lib.catalog;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import org.xbup.lib.core.block.declaration.XBContext;
import org.xbup.lib.core.catalog.XBACatalog;
import org.xbup.lib.core.catalog.base.service.XBCLimiService;
import org.xbup.lib.core.catalog.base.service.XBCTranService;
import org.xbup.lib.catalog.entity.service.XBELimiService;
import org.xbup.lib.catalog.entity.service.XBETranService;

/**
 * Basic level 2 catalog class using Java persistence.
 *
 * @version 0.1.25 2015/08/08
 * @author ExBin Project (http://exbin.org)
 */
public class XBAECatalog extends XBECatalog implements XBACatalog {

    public XBAECatalog() {
        super();
    }

    public XBAECatalog(EntityManager em) {
        super(em);

        catalogServices.put(XBCLimiService.class, new XBELimiService(this));
        catalogServices.put(XBCTranService.class, new XBETranService(this));
    }

    public void clear() {
        // TODO: Delete everything
    }

    @Override
    public XBContext getRootContext() {
        return super.getRootContext();
        // TODO: Should be level 2 Context
    }

    @Override
    public void initCatalog() {
        super.initCatalog();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            /*            XBEType node = new XBEType();
             node.setXBIndex(new Long(0));
             node.setOwner(null);
             em.persist(node);

             XBEAttrib attrib = new XBEAttrib();
             attrib.setOwner(node);
             attrib.setXBIndex(new Long(0));
             em.persist(attrib);
             */
            tx.commit();
        } catch (Exception ex) {
            Logger.getLogger(XBAECatalog.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
