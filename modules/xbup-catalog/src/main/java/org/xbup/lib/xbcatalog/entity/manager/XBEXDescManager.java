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
package org.xbup.lib.xbcatalog.entity.manager;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import org.springframework.stereotype.Repository;
import org.xbup.lib.xb.catalog.base.XBCItem;
import org.xbup.lib.xb.catalog.base.XBCXDesc;
import org.xbup.lib.xb.catalog.base.XBCXLanguage;
import org.xbup.lib.xb.catalog.base.manager.XBCNodeManager;
import org.xbup.lib.xb.catalog.base.manager.XBCSpecManager;
import org.xbup.lib.xb.catalog.base.manager.XBCXDescManager;
import org.xbup.lib.xb.catalog.base.manager.XBCXLangManager;
import org.xbup.lib.xbcatalog.XBECatalog;
import org.xbup.lib.xbcatalog.entity.XBEItem;
import org.xbup.lib.xbcatalog.entity.XBENode;
import org.xbup.lib.xbcatalog.entity.XBESpec;
import org.xbup.lib.xbcatalog.entity.XBEXDesc;
import org.xbup.lib.xbcatalog.entity.XBEXLanguage;

/**
 * XBUP catalog description manager.
 *
 * @version 0.1 wr21.0 2012/01/01
 * @author XBUP Project (http://xbup.org)
 */
@Repository
public class XBEXDescManager extends XBEDefaultManager<XBEXDesc> implements XBCXDescManager<XBEXDesc>, Serializable {

    public XBEXDescManager() {
        super();
    }
    
    public XBEXDescManager(XBECatalog catalog) {
        super(catalog);
    }

    @Override
    public XBEXDesc getItemDesc(XBCItem item) {
        if (!(item instanceof XBEItem)) {
            return null;
        }
        XBCXLanguage language = ((XBCXLangManager) catalog.getCatalogManager(XBCXLangManager.class)).getDefaultLang();
        return getItemDesc(item, language); // TODO Try another language if default not available
    }

    @Override
    public XBEXDesc getItemDesc(XBCItem item, XBCXLanguage language) {
        if (!(item instanceof XBEItem)) {
            return null;
        }
        try {
            return (XBEXDesc) catalog.getEntityManager().createQuery("SELECT object(o) FROM XBXDesc as o WHERE o.item.id = " + ((XBEItem) item).getId() + " AND o.lang.id = " + (((XBEXLanguage) language).getId())).getSingleResult();
        } catch (NoResultException ex) {
            return null;
        } catch (Exception ex) {
            Logger.getLogger(XBEXDescManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<XBCXDesc> getItemDescs(XBCItem item) {
        if (!(item instanceof XBEItem)) {
            return null;
        }
        try {
            return (List<XBCXDesc>) catalog.getEntityManager().createQuery("SELECT object(o) FROM XBXDesc as o WHERE o.item.id = " + ((XBEItem) item).getId()).getResultList();
        } catch (NoResultException ex) {
            return null;
        } catch (Exception ex) {
            Logger.getLogger(XBEXDescManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public void initializeExtension() {
        EntityTransaction tx = em.getTransaction();
        XBENodeManager nodeManager = (XBENodeManager) catalog.getCatalogManager(XBCNodeManager.class);
        XBESpecManager specManager = (XBESpecManager) catalog.getCatalogManager(XBCSpecManager.class);
        try {
            tx.begin();
            XBENode node = nodeManager.getRootNode();
            XBEXLanguage defaultLang = (XBEXLanguage) ((XBEXLangManager) catalog.getCatalogManager(XBCXLangManager.class)).getDefaultLang();

            XBEXDesc desc = new XBEXDesc();
            desc.setItem(node);
            desc.setLang(defaultLang);
            desc.setText("Specification's catalog root node");
            em.persist(desc);

            XBESpec spec = specManager.findFormatSpecByXB(node, 0);
            desc = new XBEXDesc();
            desc.setItem(spec);
            desc.setLang(defaultLang);
            desc.setText("Default specification's format");
            em.persist(desc);

            spec = specManager.findGroupSpecByXB(node, 0);
            desc = new XBEXDesc();
            desc.setItem(spec);
            desc.setLang(defaultLang);
            desc.setText("Default specification's group");
            em.persist(desc);

            tx.commit();
        } catch (Exception ex) {
            Logger.getLogger(XBEXDescManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String getExtensionName() {
        return "Description Extension";
    }

    public XBEXDesc findById(long id) {
        try {
            return (XBEXDesc) catalog.getEntityManager().createQuery("SELECT object(o) FROM XBXDesc as o WHERE o.id = " + id).getSingleResult();
        } catch (NoResultException ex) {
            Logger.getLogger(XBEXDescManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (Exception ex) {
            Logger.getLogger(XBEXDescManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
