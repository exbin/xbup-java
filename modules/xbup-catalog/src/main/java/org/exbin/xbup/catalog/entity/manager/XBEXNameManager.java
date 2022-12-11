/*
 * Copyright (C) ExBin Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
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
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.persistence.NoResultException;
import org.exbin.xbup.catalog.XBECatalog;
import org.exbin.xbup.catalog.entity.XBEItem;
import org.exbin.xbup.catalog.entity.XBENode;
import org.exbin.xbup.catalog.entity.XBEXLanguage;
import org.exbin.xbup.catalog.entity.XBEXName;
import org.exbin.xbup.core.catalog.base.XBCItem;
import org.exbin.xbup.core.catalog.base.XBCXLanguage;
import org.exbin.xbup.core.catalog.base.XBCXName;
import org.exbin.xbup.core.catalog.base.manager.XBCNodeManager;
import org.exbin.xbup.core.catalog.base.manager.XBCXLangManager;
import org.exbin.xbup.core.catalog.base.manager.XBCXNameManager;
import org.springframework.stereotype.Repository;

/**
 * XBUP catalog item name manager.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
@Repository
public class XBEXNameManager extends XBEDefaultCatalogManager<XBCXName> implements XBCXNameManager, Serializable {

    public XBEXNameManager() {
        super();
    }

    public XBEXNameManager(XBECatalog catalog) {
        super(catalog);
    }

    @Nonnull
    @Override
    public Class getEntityClass() {
        return XBEXName.class;
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

    @Override
    public boolean initCatalog() {
        XBENodeManager nodeManager = (XBENodeManager) catalog.getCatalogManager(XBCNodeManager.class);
        XBENode node = (XBENode) nodeManager.getMainRootNode().orElse(null);
        if (node == null) {
            return false;
        }
        XBEXLanguage defaultLang = (XBEXLanguage) ((XBEXLangManager) catalog.getCatalogManager(XBCXLangManager.class)).getDefaultLang();
        if (defaultLang == null) {
            return false;
        }

        XBEXName name = new XBEXName();
        name.setItem(node);
        name.setLang(defaultLang);
        name.setText("Root");
        em.persist(name);

        return true;
    }
}
