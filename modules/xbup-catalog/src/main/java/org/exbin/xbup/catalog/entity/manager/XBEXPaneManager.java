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
import javax.persistence.NoResultException;
import org.exbin.xbup.catalog.XBECatalog;
import org.exbin.xbup.catalog.entity.XBEBlockRev;
import org.exbin.xbup.catalog.entity.XBEXBlockPane;
import org.exbin.xbup.catalog.entity.XBEXPlugPane;
import org.exbin.xbup.catalog.entity.XBEXPlugin;
import org.exbin.xbup.core.catalog.base.XBCBlockRev;
import org.exbin.xbup.core.catalog.base.XBCXBlockPane;
import org.exbin.xbup.core.catalog.base.XBCXPlugPane;
import org.exbin.xbup.core.catalog.base.XBCXPlugin;
import org.exbin.xbup.core.catalog.base.manager.XBCXPaneManager;
import org.springframework.stereotype.Repository;

/**
 * XBUP catalog item panel editors manager.
 *
 * @version 0.1.25 2015/09/06
 * @author ExBin Project (http://exbin.org)
 */
@Repository
public class XBEXPaneManager extends XBEDefaultCatalogManager<XBEXBlockPane> implements XBCXPaneManager<XBEXBlockPane>, Serializable {

    public XBEXPaneManager() {
        super();
    }

    public XBEXPaneManager(XBECatalog catalog) {
        super(catalog);
    }

    @Override
    public void initializeExtension() {
    }

    @Override
    public String getExtensionName() {
        return "Pane Extension";
    }

    @Override
    public XBEXBlockPane findById(long id) {
        try {
            return (XBEXBlockPane) catalog.getEntityManager().createQuery("SELECT object(o) FROM XBXBlockPane as o WHERE o.id = " + id).getSingleResult();
        } catch (NoResultException ex) {
            return null;
        } catch (Exception ex) {
            Logger.getLogger(XBEXPaneManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public List<XBCXBlockPane> getPanes(XBCBlockRev rev) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public long getPanesCount(XBCBlockRev rev) {
        if (!(rev instanceof XBEBlockRev)) {
            return 0;
        }
        try {
            return (Long) catalog.getEntityManager().createQuery("SELECT count(o) FROM XBXBlockPane as o WHERE o.blockRev.id = " + ((XBEBlockRev) rev).getId()).getSingleResult();
        } catch (NoResultException ex) {
            return 0;
        } catch (Exception ex) {
            Logger.getLogger(XBEXPaneManager.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }

    @Override
    public XBEXBlockPane findPaneByPR(XBCBlockRev rev, long priority) {
        if (!(rev instanceof XBEBlockRev)) {
            return null;
        }
        try {
            return (XBEXBlockPane) catalog.getEntityManager().createQuery("SELECT object(o) FROM XBXBlockPane as o WHERE o.blockRev.id = " + ((XBEBlockRev) rev).getId() + " AND o.priority = " + Long.toString(priority)).getSingleResult();
        } catch (NoResultException ex) {
            return null;
        } catch (Exception ex) {
            Logger.getLogger(XBEXPaneManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public List<XBCXPlugPane> getPlugPanes(XBCXPlugin plugin) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public long getPlugPanesCount(XBCXPlugin plugin) {
        if (!(plugin instanceof XBEXPlugin)) {
            return 0;
        }
        try {
            return (Long) catalog.getEntityManager().createQuery("SELECT count(o) FROM XBXPlugPane as o WHERE o.plugin.id = " + ((XBEXPlugin) plugin).getId()).getSingleResult();
        } catch (NoResultException ex) {
            return 0;
        } catch (Exception ex) {
            Logger.getLogger(XBEXPaneManager.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }

    @Override
    public XBEXPlugPane getPlugPane(XBCXPlugin plugin, long paneIndex) {
        if (!(plugin instanceof XBEXPlugin)) {
            return null;
        }
        try {
            return (XBEXPlugPane) catalog.getEntityManager().createQuery("SELECT object(o) FROM XBXPlugPane as o WHERE o.plugin.id = " + ((XBEXPlugin) plugin).getId() + " AND o.paneIndex = " + Long.toString(paneIndex)).getSingleResult();
        } catch (NoResultException ex) {
            return null;
        } catch (Exception ex) {
            Logger.getLogger(XBEXPaneManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public Long getAllPlugPanesCount() {
        try {
            return (Long) catalog.getEntityManager().createQuery("SELECT count(o) FROM XBXPlugPane as o").getSingleResult();
        } catch (NoResultException ex) {
            return null;
        } catch (Exception ex) {
            Logger.getLogger(XBEXPaneManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public XBEXPlugPane findPlugPaneById(long id) {
        try {
            return (XBEXPlugPane) catalog.getEntityManager().createQuery("SELECT object(o) FROM XBXPlugPane as o WHERE o.id = " + id).getSingleResult();
        } catch (NoResultException ex) {
            return null;
        } catch (Exception ex) {
            Logger.getLogger(XBEXPaneManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
