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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import org.exbin.xbup.catalog.XBECatalog;
import org.exbin.xbup.catalog.entity.XBEItem;
import org.exbin.xbup.catalog.entity.XBENode;
import org.exbin.xbup.catalog.entity.XBESpec;
import org.exbin.xbup.catalog.entity.XBEXLanguage;
import org.exbin.xbup.catalog.entity.XBEXName;
import org.exbin.xbup.core.catalog.base.XBCItem;
import org.exbin.xbup.core.catalog.base.XBCXLanguage;
import org.exbin.xbup.core.catalog.base.XBCXName;
import org.exbin.xbup.core.catalog.base.manager.XBCNodeManager;
import org.exbin.xbup.core.catalog.base.manager.XBCSpecManager;
import org.exbin.xbup.core.catalog.base.manager.XBCXLangManager;
import org.exbin.xbup.core.catalog.base.manager.XBCXNameManager;
import org.springframework.stereotype.Repository;

/**
 * XBUP catalog item name manager.
 *
 * @version 0.1.24 2014/11/17
 * @author ExBin Project (http://exbin.org)
 */
@Repository
public class XBEXNameManager extends XBEDefaultCatalogManager<XBEXName> implements XBCXNameManager<XBEXName>, Serializable {

    public XBEXNameManager() {
        super();
    }

    public XBEXNameManager(XBECatalog catalog) {
        super(catalog);
    }

    @Override
    public XBEXName getDefaultItemName(XBCItem item) {
        if (!(item instanceof XBEItem)) {
            return null;
        }

        XBEXLangManager langManager = ((XBEXLangManager) catalog.getCatalogManager(XBCXLangManager.class));
        return getItemName(item, langManager.getDefaultLang());
    }

    @Override
    public XBEXName getItemName(XBCItem item, XBCXLanguage language) {
        if (!(item instanceof XBEItem)) {
            return null;
        }

        try {
            return (XBEXName) catalog.getEntityManager().createQuery("SELECT object(o) FROM XBXName as o WHERE o.item.id = " + ((XBEItem) item).getId() + " AND o.lang.id = " + (((XBEXLanguage) language).getId())).getSingleResult();
        } catch (NoResultException ex) {
            return null;
        } catch (Exception ex) {
            Logger.getLogger(XBEXNameManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<XBCXName> getItemNames(XBCItem item) {
        if (!(item instanceof XBEItem)) {
            return null;
        }
        try {
            return (List<XBCXName>) catalog.getEntityManager().createQuery("SELECT object(o) FROM XBXName as o WHERE o.item.id = " + ((XBEItem) item).getId()).getResultList();
        } catch (NoResultException ex) {
            return null;
        } catch (Exception ex) {
            Logger.getLogger(XBEXNameManager.class.getName()).log(Level.SEVERE, null, ex);
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

            XBEXName name = new XBEXName();
            name.setItem(node);
            name.setLang(defaultLang);
            name.setText("Root");
            em.persist(name);

            XBESpec spec = specManager.findFormatSpecByXB(node, 0);
            name = new XBEXName();
            name.setItem(spec);
            name.setLang(defaultLang);
            name.setText("Default");
            em.persist(name);

            spec = specManager.findGroupSpecByXB(node, 0);
            name = new XBEXName();
            name.setItem(spec);
            name.setLang(defaultLang);
            name.setText("Basic");
            em.persist(name);

            spec = specManager.findBlockSpecByXB(node, 0);
            name = new XBEXName();
            name.setItem(spec);
            name.setLang(defaultLang);
            name.setText("Specification");
            em.persist(name);

            spec = specManager.findBlockSpecByXB(node, 8);
            name = new XBEXName();
            name.setItem(spec);
            name.setLang(defaultLang);
            name.setText("CatalogLink");
            em.persist(name);

            tx.commit();
        } catch (Exception ex) {
            Logger.getLogger(XBEXNameManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String getExtensionName() {
        return "Name Extension";
    }

    @Override
    public String getDefaultText(XBCItem item) {
        XBCXName name = getDefaultItemName(item);
        if (name == null) {
            return "";
        }

        return name.getText();
    }
}
