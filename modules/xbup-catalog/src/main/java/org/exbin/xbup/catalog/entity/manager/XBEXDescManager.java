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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import org.exbin.xbup.catalog.XBECatalog;
import org.exbin.xbup.catalog.entity.XBEItem;
import org.exbin.xbup.catalog.entity.XBEXDesc;
import org.exbin.xbup.catalog.entity.XBEXLanguage;
import org.exbin.xbup.core.catalog.base.XBCItem;
import org.exbin.xbup.core.catalog.base.XBCNode;
import org.exbin.xbup.core.catalog.base.XBCSpec;
import org.exbin.xbup.core.catalog.base.XBCXDesc;
import org.exbin.xbup.core.catalog.base.XBCXLanguage;
import org.exbin.xbup.core.catalog.base.manager.XBCNodeManager;
import org.exbin.xbup.core.catalog.base.manager.XBCSpecManager;
import org.exbin.xbup.core.catalog.base.manager.XBCXDescManager;
import org.exbin.xbup.core.catalog.base.manager.XBCXLangManager;
import org.springframework.stereotype.Repository;

/**
 * XBUP catalog description manager.
 *
 * @version 0.2.1 2020/08/26
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
@Repository
public class XBEXDescManager extends XBEDefaultCatalogManager<XBCXDesc> implements XBCXDescManager, Serializable {

    public XBEXDescManager() {
        super();
    }

    public XBEXDescManager(XBECatalog catalog) {
        super(catalog);
    }

    @Override
    public XBEXDesc getDefaultItemDesc(XBCItem item) {
        if (!(item instanceof XBEItem)) {
            return null;
        }

        XBCXLangManager langManager = catalog.getCatalogManager(XBCXLangManager.class);
        XBCXLanguage language = langManager.getDefaultLang();
        return getItemDesc(item, language);
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
        XBCNodeManager nodeManager = catalog.getCatalogManager(XBCNodeManager.class);
        XBCSpecManager specManager = catalog.getCatalogManager(XBCSpecManager.class);
        throw new UnsupportedOperationException("Not supported yet.");
//        try {
//            tx.begin();
//            XBCNode node = nodeManager.getMainRootNode().get();
//            XBCXLangManager langManager = catalog.getCatalogManager(XBCXLangManager.class);
//            XBCXLanguage defaultLang = langManager.getDefaultLang();
//
//            XBEXDesc desc = new XBEXDesc();
//            desc.setItem(node);
//            desc.setLang(defaultLang);
//            desc.setText("Specification's catalog root node");
//            em.persist(desc);
//
//            XBCSpec spec = specManager.findFormatSpecByXB(node, 0);
//            desc = new XBEXDesc();
//            desc.setItem(spec);
//            desc.setLang(defaultLang);
//            desc.setText("Default specification's format");
//            em.persist(desc);
//
//            spec = specManager.findGroupSpecByXB(node, 0);
//            desc = new XBEXDesc();
//            desc.setItem(spec);
//            desc.setLang(defaultLang);
//            desc.setText("Default specification's group");
//            em.persist(desc);
//
//            tx.commit();
//        } catch (Exception ex) {
//            Logger.getLogger(XBEXDescManager.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }

    @Override
    public String getExtensionName() {
        return "Description Extension";
    }

    public XBEXDesc findById(long id) {
        try {
            return (XBEXDesc) catalog.getEntityManager().createQuery("SELECT object(o) FROM XBXDesc as o WHERE o.id = " + id).getSingleResult();
        } catch (NoResultException ex) {
            return null;
        } catch (Exception ex) {
            Logger.getLogger(XBEXDescManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public String getDefaultText(XBCItem item) {
        XBEXDesc desc = getDefaultItemDesc(item);
        if (desc == null) {
            return "";
        }

        return desc.getText();
    }
}
