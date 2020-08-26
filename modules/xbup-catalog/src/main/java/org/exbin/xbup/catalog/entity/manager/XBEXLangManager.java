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
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import org.exbin.xbup.catalog.XBECatalog;
import org.exbin.xbup.catalog.entity.XBEXLanguage;
import org.exbin.xbup.core.catalog.base.XBCXLanguage;
import org.exbin.xbup.core.catalog.base.manager.XBCXLangManager;
import org.springframework.stereotype.Repository;

/**
 * XBUP catalog language manager.
 *
 * @version 0.2.1 2020/08/26
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
@Repository
public class XBEXLangManager extends XBEDefaultCatalogManager<XBCXLanguage> implements XBCXLangManager, Serializable {

    private XBEXLanguage defaultLanguageCache = null;

    public XBEXLangManager() {
        super();
    }

    public XBEXLangManager(XBECatalog catalog) {
        super(catalog);
    }

    @Override
    public XBEXLanguage getDefaultLang() {
        if (defaultLanguageCache != null) {
            return defaultLanguageCache;
        }

        try {
            Query query = catalog.getEntityManager().createQuery("SELECT object(o) FROM XBXLanguage as o");
            query.setMaxResults(1);
            defaultLanguageCache = (XBEXLanguage) query.getSingleResult();
            return defaultLanguageCache;
        } catch (NoResultException ex) {
            return null;
        } catch (Exception ex) {
            Logger.getLogger(XBEXLangManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public List<XBCXLanguage> getLangs() {
        try {
            return (List<XBCXLanguage>) catalog.getEntityManager().createQuery("SELECT object(o) FROM XBXLanguage as o").getResultList();
        } catch (NoResultException ex) {
            return null;
        } catch (Exception ex) {
            Logger.getLogger(XBEXLangManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public void initializeExtension() {
        EntityManager em = catalog.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            XBEXLanguage lang = new XBEXLanguage();
            lang.setLangCode("en");
            em.persist(lang);
            tx.commit();
        } catch (Exception ex) {
            Logger.getLogger(XBEXLangManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String getExtensionName() {
        return "Language Extension";
    }

    public Long getAllLangsCount() {
        try {
            return (Long) catalog.getEntityManager().createQuery("SELECT count(o) FROM XBLanguage as o").getSingleResult();
        } catch (NoResultException ex) {
            return 0l;
        } catch (Exception ex) {
            Logger.getLogger(XBEXLangManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public XBEXLanguage findById(long id) {
        try {
            return (XBEXLanguage) catalog.getEntityManager().createQuery("SELECT object(o) FROM XBXLanguage as o WHERE o.id = "+ id).getSingleResult();
        } catch (NoResultException ex) {
            return null;
        } catch (Exception ex) {
            Logger.getLogger(XBEXLangManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
