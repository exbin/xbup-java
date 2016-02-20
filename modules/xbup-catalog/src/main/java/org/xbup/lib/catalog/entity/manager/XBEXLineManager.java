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
package org.xbup.lib.catalog.entity.manager;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.NoResultException;
import org.springframework.stereotype.Repository;
import org.xbup.lib.core.catalog.base.XBCBlockRev;
import org.xbup.lib.core.catalog.base.XBCXBlockLine;
import org.xbup.lib.core.catalog.base.XBCXPlugLine;
import org.xbup.lib.core.catalog.base.XBCXPlugin;
import org.xbup.lib.core.catalog.base.manager.XBCXLineManager;
import org.xbup.lib.catalog.XBECatalog;
import org.xbup.lib.catalog.entity.XBEBlockRev;
import org.xbup.lib.catalog.entity.XBEXBlockLine;
import org.xbup.lib.catalog.entity.XBEXPlugLine;
import org.xbup.lib.catalog.entity.XBEXPlugin;

/**
 * XBUP catalog line editors manager.
 *
 * @version 0.1.25 2015/09/06
 * @author XBUP Project (http://xbup.org)
 */
@Repository
public class XBEXLineManager extends XBEDefaultCatalogManager<XBEXBlockLine> implements XBCXLineManager<XBEXBlockLine>, Serializable {

    public XBEXLineManager() {
        super();
    }

    public XBEXLineManager(XBECatalog catalog) {
        super(catalog);
    }

    @Override
    public void initializeExtension() {
    }

    @Override
    public String getExtensionName() {
        return "Line Extension";
    }

    @Override
    public XBEXBlockLine findById(long id) {
        try {
            return (XBEXBlockLine) catalog.getEntityManager().createQuery("SELECT object(o) FROM XBXBlockLine as o WHERE o.id = "+ id).getSingleResult();
        } catch (NoResultException ex) {
            return null;
        } catch (Exception ex) {
            Logger.getLogger(XBEXLineManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public List<XBCXBlockLine> getLines(XBCBlockRev rev) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public long getLinesCount(XBCBlockRev rev) {
        if (!(rev instanceof XBEBlockRev)) {
            return 0;
        }
        try {
            return (Long) catalog.getEntityManager().createQuery("SELECT count(o) FROM XBXBlockLine as o WHERE o.blockRev.id = "+((XBEBlockRev) rev).getId()).getSingleResult();
        } catch (NoResultException ex) {
            return 0;
        } catch (Exception ex) {
            Logger.getLogger(XBEXLineManager.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }

    @Override
    public XBEXBlockLine findLineByPR(XBCBlockRev rev, long priority) {
        if (!(rev instanceof XBEBlockRev)) {
            return null;
        }
        try {
            return (XBEXBlockLine) catalog.getEntityManager().createQuery("SELECT object(o) FROM XBXBlockLine as o WHERE o.blockRev.id = "+((XBEBlockRev) rev).getId()+" AND o.priority = "+Long.toString(priority)).getSingleResult();
        } catch (NoResultException ex) {
            return null;
        } catch (Exception ex) {
            Logger.getLogger(XBEXLineManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public List<XBCXPlugLine> getPlugLines(XBCXPlugin plugin) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public long getPlugLinesCount(XBCXPlugin plugin) {
        if (!(plugin instanceof XBEXPlugin)) {
            return 0;
        }
        try {
            return (Long) catalog.getEntityManager().createQuery("SELECT count(o) FROM XBXPlugLine as o WHERE o.plugin.id = "+((XBEXPlugin) plugin).getId()).getSingleResult();
        } catch (NoResultException ex) {
            return 0;
        } catch (Exception ex) {
            Logger.getLogger(XBEXLineManager.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }

    @Override
    public XBEXPlugLine getPlugLine(XBCXPlugin plugin, long lineIndex) {
        if (!(plugin instanceof XBEXPlugin)) {
            return null;
        }
        try {
            return (XBEXPlugLine) catalog.getEntityManager().createQuery("SELECT object(o) FROM XBXPlugLine as o WHERE o.plugin.id = "+((XBEXPlugin) plugin).getId()+" AND o.lineIndex = "+Long.toString(lineIndex)).getSingleResult();
        } catch (NoResultException ex) {
            return null;
        } catch (Exception ex) {
            Logger.getLogger(XBEXLineManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public Long getAllPlugLinesCount() {
        try {
            return (Long) catalog.getEntityManager().createQuery("SELECT count(o) FROM XBXPlugLine as o").getSingleResult();
        } catch (NoResultException ex) {
            return null;
        } catch (Exception ex) {
            Logger.getLogger(XBEXLineManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public XBEXPlugLine findPlugLineById(long id) {
        try {
            return (XBEXPlugLine) catalog.getEntityManager().createQuery("SELECT object(o) FROM XBXPlugLine as o WHERE o.id = "+ id).getSingleResult();
        } catch (NoResultException ex) {
            return null;
        } catch (Exception ex) {
            Logger.getLogger(XBEXLineManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
