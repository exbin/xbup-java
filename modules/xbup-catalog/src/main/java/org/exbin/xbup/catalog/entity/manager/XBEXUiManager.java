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
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.persistence.NoResultException;
import org.exbin.xbup.catalog.XBECatalog;
import org.exbin.xbup.catalog.entity.XBEBlockRev;
import org.exbin.xbup.catalog.entity.XBEXBlockUi;
import org.exbin.xbup.catalog.entity.XBEXPlugUi;
import org.exbin.xbup.catalog.entity.XBEXPlugUiType;
import org.exbin.xbup.catalog.entity.XBEXPlugin;
import org.exbin.xbup.core.catalog.XBPlugUiType;
import org.exbin.xbup.core.catalog.base.XBCBlockRev;
import org.exbin.xbup.core.catalog.base.XBCXBlockUi;
import org.exbin.xbup.core.catalog.base.XBCXPlugUi;
import org.exbin.xbup.core.catalog.base.XBCXPlugin;
import org.exbin.xbup.core.catalog.base.manager.XBCXUiManager;
import org.springframework.stereotype.Repository;

/**
 * XBUP catalog UI editors manager.
 *
 * @version 0.2.1 2020/09/01
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
@Repository
public class XBEXUiManager extends XBEDefaultCatalogManager<XBCXBlockUi> implements XBCXUiManager, Serializable {

    public XBEXUiManager() {
        super();
    }

    public XBEXUiManager(XBECatalog catalog) {
        super(catalog);
    }

    @Nonnull
    @Override
    public Class getEntityClass() {
        return XBEXBlockUi.class;
    }

    @Override
    public void initializeExtension() {
    }

    @Override
    public String getExtensionName() {
        return "UI Extension";
    }

    @Override
    public XBEXBlockUi findById(long id) {
        try {
            return (XBEXBlockUi) catalog.getEntityManager().createQuery("SELECT object(o) FROM XBXBlockUi as o WHERE o.id = " + id).getSingleResult();
        } catch (NoResultException ex) {
            return null;
        } catch (Exception ex) {
            Logger.getLogger(XBEXUiManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public XBEXPlugUiType findTypeById(long id) {
        try {
            return (XBEXPlugUiType) catalog.getEntityManager().createQuery("SELECT object(o) FROM XBXPlugUiType as o WHERE o.id = " + id).getSingleResult();
        } catch (NoResultException ex) {
            return null;
        } catch (Exception ex) {
            Logger.getLogger(XBEXUiManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Nonnull
    @Override
    public List<XBCXBlockUi> getUis(XBCBlockRev rev) {
        try {
            return (List<XBCXBlockUi>) catalog.getEntityManager().createQuery("SELECT object(o) FROM XBXBlockUi as o WHERE o.blockRev.id = " + ((XBEBlockRev) rev).getId()).getResultList();
        } catch (NoResultException e) {
            return List.of();
        } catch (Exception ex) {
            Logger.getLogger(XBEXUiManager.class.getName()).log(Level.SEVERE, null, ex);
            return List.of();
        }
    }

    @Nonnull
    @Override
    public List<XBCXBlockUi> getUis(XBCBlockRev rev, XBPlugUiType type) {
        try {
            return (List<XBCXBlockUi>) catalog.getEntityManager().createQuery("SELECT object(o) FROM XBXBlockUi as o WHERE o.blockRev.id = " + ((XBEBlockRev) rev).getId() + " AND o.plugUi.uiType.id = " + type.getDbIndex()).getResultList();
        } catch (NoResultException e) {
            return List.of();
        } catch (Exception ex) {
            Logger.getLogger(XBEXUiManager.class.getName()).log(Level.SEVERE, null, ex);
            return List.of();
        }
    }

    @Override
    public long getUisCount(XBCBlockRev rev) {
        if (!(rev instanceof XBEBlockRev)) {
            return 0;
        }
        try {
            return (Long) catalog.getEntityManager().createQuery("SELECT count(o) FROM XBXBlockUi as o WHERE o.blockRev.id = " + ((XBEBlockRev) rev).getId()).getSingleResult();
        } catch (NoResultException ex) {
            return 0;
        } catch (Exception ex) {
            Logger.getLogger(XBEXUiManager.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }

    @Override
    public long getUisCount(XBCBlockRev rev, XBPlugUiType type) {
        if (!(rev instanceof XBEBlockRev)) {
            return 0;
        }
        try {
            return (Long) catalog.getEntityManager().createQuery("SELECT count(o) FROM XBXBlockUi as o WHERE o.blockRev.id = " + ((XBEBlockRev) rev).getId() + " AND o.plugUi.uiType.id = " + type.getDbIndex()).getSingleResult();
        } catch (NoResultException ex) {
            return 0;
        } catch (Exception ex) {
            Logger.getLogger(XBEXUiManager.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }

    @Override
    public XBEXBlockUi findUiByPR(XBCBlockRev rev, XBPlugUiType type, long priority) {
        if (!(rev instanceof XBEBlockRev)) {
            return null;
        }
        try {
            return (XBEXBlockUi) catalog.getEntityManager().createQuery("SELECT object(o) FROM XBXBlockUi as o WHERE o.blockRev.id = " + ((XBEBlockRev) rev).getId() + " AND o.priority = " + Long.toString(priority) + " AND o.plugUi.uiType.id = " + type.getDbIndex()).getSingleResult();
        } catch (NoResultException ex) {
            return null;
        } catch (Exception ex) {
            Logger.getLogger(XBEXUiManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Nonnull
    @Override
    public List<XBCXPlugUi> getPlugUis(XBCXPlugin plugin) {
        try {
            return (List<XBCXPlugUi>) catalog.getEntityManager().createQuery("SELECT object(o) FROM XBXPlugUi as o WHERE o.plugin.id = " + ((XBEXPlugin) plugin).getId()).getResultList();
        } catch (NoResultException e) {
            return List.of();
        } catch (Exception ex) {
            Logger.getLogger(XBEXUiManager.class.getName()).log(Level.SEVERE, null, ex);
            return List.of();
        }
    }

    @Nonnull
    @Override
    public List<XBCXPlugUi> getPlugUis(XBCXPlugin plugin, XBPlugUiType type) {
        try {
            return (List<XBCXPlugUi>) catalog.getEntityManager().createQuery("SELECT object(o) FROM XBXPlugUi as o WHERE o.plugin.id = " + ((XBEXPlugin) plugin).getId() + " AND o.uiType.id = " + type.getDbIndex()).getResultList();
        } catch (NoResultException e) {
            return List.of();
        } catch (Exception ex) {
            Logger.getLogger(XBEXUiManager.class.getName()).log(Level.SEVERE, null, ex);
            return List.of();
        }
    }

    @Override
    public long getPlugUisCount(XBCXPlugin plugin) {
        if (!(plugin instanceof XBEXPlugin)) {
            return 0;
        }
        try {
            return (Long) catalog.getEntityManager().createQuery("SELECT count(o) FROM XBXPlugUi as o WHERE o.plugin.id = " + ((XBEXPlugin) plugin).getId()).getSingleResult();
        } catch (NoResultException ex) {
            return 0;
        } catch (Exception ex) {
            Logger.getLogger(XBEXUiManager.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }

    @Override
    public long getPlugUisCount(XBCXPlugin plugin, XBPlugUiType type) {
        if (!(plugin instanceof XBEXPlugin)) {
            return 0;
        }
        try {
            return (Long) catalog.getEntityManager().createQuery("SELECT count(o) FROM XBXPlugUi as o WHERE o.plugin.id = " + ((XBEXPlugin) plugin).getId() + " AND o.uiType.id = " + type.getDbIndex()).getSingleResult();
        } catch (NoResultException ex) {
            return 0;
        } catch (Exception ex) {
            Logger.getLogger(XBEXUiManager.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }

    @Override
    public XBEXPlugUi getPlugUi(XBCXPlugin plugin, XBPlugUiType type, long methodIndex) {
        if (!(plugin instanceof XBEXPlugin)) {
            return null;
        }
        try {
            return (XBEXPlugUi) catalog.getEntityManager().createQuery("SELECT object(o) FROM XBXPlugUi as o WHERE o.plugin.id = " + ((XBEXPlugin) plugin).getId() + " AND o.methodIndex = " + Long.toString(methodIndex) + " AND o.uiType.id = " + type.getDbIndex()).getSingleResult();
        } catch (NoResultException ex) {
            return null;
        } catch (Exception ex) {
            Logger.getLogger(XBEXUiManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public long getAllPlugUisCount() {
        try {
            return (Long) catalog.getEntityManager().createQuery("SELECT count(o) FROM XBXPlugUi as o").getSingleResult();
        } catch (NoResultException ex) {
            return 0;
        } catch (Exception ex) {
            Logger.getLogger(XBEXUiManager.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }

    @Override
    public XBEXPlugUi findPlugUiById(long id) {
        try {
            return (XBEXPlugUi) catalog.getEntityManager().createQuery("SELECT object(o) FROM XBXPlugUi as o WHERE o.id = " + id).getSingleResult();
        } catch (NoResultException ex) {
            return null;
        } catch (Exception ex) {
            Logger.getLogger(XBEXUiManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public boolean initCatalog() {
        for (int i = 1; i < XBPlugUiType.values().length; i++) {
            XBEXPlugUiType type = new XBEXPlugUiType();
            XBPlugUiType uiType = XBPlugUiType.findByDbIndex(i);
            type.setName(uiType.getName());
            em.persist(type);
        }

        return true;
    }
}
