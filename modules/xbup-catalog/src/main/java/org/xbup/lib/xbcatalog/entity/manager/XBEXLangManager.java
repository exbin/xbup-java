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
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import org.springframework.stereotype.Repository;
import org.xbup.lib.xb.catalog.base.XBCXLanguage;
import org.xbup.lib.xb.catalog.base.manager.XBCXLangManager;
import org.xbup.lib.xbcatalog.XBECatalog;
import org.xbup.lib.xbcatalog.entity.XBEXLanguage;

/**
 * XBUP catalog language manager.
 *
 * @version 0.1 wr23.0 2013/09/30
 * @author XBUP Project (http://xbup.org)
 */
@Repository
public class XBEXLangManager extends XBEDefaultManager<XBEXLanguage> implements XBCXLangManager<XBEXLanguage>, Serializable {

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
            Logger.getLogger(XBEXLangManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (Exception ex) {
            Logger.getLogger(XBEXLangManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public XBEXLanguage findById(long id) {
        try {
            return (XBEXLanguage) catalog.getEntityManager().createQuery("SELECT object(o) FROM XBXLanguage as o WHERE o.id = "+ id).getSingleResult();
        } catch (NoResultException ex) {
            Logger.getLogger(XBEXLangManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (Exception ex) {
            Logger.getLogger(XBEXLangManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
