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

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.persistence.NoResultException;
import org.exbin.xbup.catalog.XBECatalog;
import org.exbin.xbup.catalog.entity.XBEXFile;
import org.exbin.xbup.catalog.entity.XBEXPlugin;
import org.exbin.xbup.core.catalog.base.XBCNode;
import org.exbin.xbup.core.catalog.base.XBCXFile;
import org.exbin.xbup.core.catalog.base.XBCXPlugin;
import org.exbin.xbup.core.catalog.base.manager.XBCXPlugManager;
import org.springframework.stereotype.Repository;

/**
 * XBUP catalog plugin manager.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
@Repository
public class XBEXPlugManager extends XBEDefaultCatalogManager<XBCXPlugin> implements XBCXPlugManager, Serializable {

    public XBEXPlugManager() {
        super();
    }

    public XBEXPlugManager(XBECatalog catalog) {
        super(catalog);
    }

    @Nonnull
    @Override
    public Class getEntityClass() {
        return XBEXPlugin.class;
    }

    @Override
    public Long getAllPluginCount() {
        try {
            return (Long) catalog.getEntityManager().createQuery("SELECT count(o) FROM XBXPlugin as o").getSingleResult();
        } catch (NoResultException ex) {
            return 0l;
        } catch (Exception ex) {
            Logger.getLogger(XBEXPlugManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public XBEXPlugin findById(long id) {
        try {
            return (XBEXPlugin) catalog.getEntityManager().createQuery("SELECT object(o) FROM XBXPlugin as o WHERE o.id = " + id).getSingleResult();
        } catch (NoResultException ex) {
            return null;
        } catch (Exception ex) {
            Logger.getLogger(XBEXPlugManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public Long[] getFileXBPath(XBCXFile file) {
        ArrayList<Long> list = new ArrayList<>();
        XBCNode parent = file.getNode();
        while (parent != null) {
            if (parent.getParent().isPresent()) {
                list.add(0, parent.getXBIndex());
            }
            parent = (XBCNode) parent.getParent().orElse(null);
        }
        list.add(file.getId());
        return (Long[]) list.toArray(new Long[list.size()]);
    }

    @Override
    public void initializeExtension() {
    }

    @Nonnull
    @Override
    public String getExtensionName() {
        return "Plugin Extension";
    }

    public XBEXFile findFile(XBCNode node, String fileName) {
        try {
            return (XBEXFile) catalog.getEntityManager().createQuery("SELECT object(o) FROM XBXFile as o WHERE o.parent.id = " + node.getId() + " AND o.filename = '" + DatabaseUtils.sqlEscapeString(fileName) + "'").getSingleResult();
        } catch (NoResultException ex) {
            return null;
        } catch (Exception ex) {
            Logger.getLogger(XBEXPlugManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public Long[] getPluginXBPath(XBCXPlugin plugin) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public XBEXPlugin findPlugin(XBCNode node, Long index) {
        try {
            return (XBEXPlugin) catalog.getEntityManager().createQuery("SELECT object(o) FROM XBXPlugin as o WHERE o.owner.id = " + node.getId() + " AND o.pluginIndex = " + index).getSingleResult();
        } catch (NoResultException ex) {
            return null;
        } catch (Exception ex) {
            Logger.getLogger(XBEXPlugManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Nonnull
    @Override
    public List<XBCXPlugin> findPluginsForNode(XBCNode node) {
        try {
            return (List<XBCXPlugin>) catalog.getEntityManager().createQuery("SELECT object(o) FROM XBXPlugin as o WHERE o.owner.id = " + node.getId() + " ORDER BY o.pluginIndex").getResultList();
        } catch (NoResultException ex) {
            return null;
        } catch (Exception ex) {
            Logger.getLogger(XBEXPlugManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public InputStream getPlugin(XBCXPlugin plugin) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
