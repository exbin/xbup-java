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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.NoResultException;
import org.springframework.stereotype.Repository;
import org.xbup.lib.xb.catalog.base.XBCItem;
import org.xbup.lib.xb.catalog.base.XBCXLanguage;
import org.xbup.lib.xb.catalog.base.manager.XBCXHDocManager;
import org.xbup.lib.xb.catalog.base.manager.XBCXLangManager;
import org.xbup.lib.xbcatalog.XBECatalog;
import org.xbup.lib.xbcatalog.entity.XBEItem;
import org.xbup.lib.xbcatalog.entity.XBEXHDoc;
import org.xbup.lib.xbcatalog.entity.XBEXLanguage;

/**
 * XBUP catalog HTML documentation manager.
 *
 * @version 0.1 wr21.0 2011/12/31
 * @author XBUP Project (http://xbup.org)
 */
@Repository
public class XBEXHDocManager extends XBEDefaultManager<XBEXHDoc> implements XBCXHDocManager<XBEXHDoc>, Serializable {

    public XBEXHDocManager() {
        super();
    }

    public XBEXHDocManager(XBECatalog catalog) {
        super(catalog);
    }

    @Override
    public Long getAllHDocsCount() {
        try {
            return (Long) catalog.getEntityManager().createQuery("SELECT count(o) FROM XBXHDoc as o").getSingleResult();
        } catch (NoResultException ex) {
            Logger.getLogger(XBEXHDocManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (Exception ex) {
            Logger.getLogger(XBEXHDocManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public XBEXHDoc findById(Long id) {
        try {
            return (XBEXHDoc) catalog.getEntityManager().createQuery("SELECT object(o) FROM XBXHDoc as o WHERE o.id = " + id).getSingleResult();
        } catch (NoResultException ex) {
//            Logger.getLogger(XBEXIconManager.class.getExtensionName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (Exception ex) {
            Logger.getLogger(XBEXHDocManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public void initializeExtension() {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public XBEXHDoc getDocumentation(XBCItem item) {
        XBEXLangManager langManager = ((XBEXLangManager) catalog.getCatalogManager(XBCXLangManager.class));
        if (!(item instanceof XBEItem)) {
            return null;
        }
        XBCXLanguage language = langManager.getDefaultLang();
        return getDocumentation(item, language);
    }

    public XBEXHDoc getDocumentation(XBCItem item, XBCXLanguage language) {
        try {
            return (XBEXHDoc) catalog.getEntityManager().createQuery("SELECT object(o) FROM XBXHDoc as o WHERE o.item.id = " + item.getId() + " AND o.lang.id = " + (((XBEXLanguage) language).getId())).getSingleResult();
        } catch (NoResultException ex) {
            return null;
        } catch (Exception ex) {
            Logger.getLogger(XBEXHDocManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public String getExtensionName() {
        return "HDoc Extension";
    }
}
