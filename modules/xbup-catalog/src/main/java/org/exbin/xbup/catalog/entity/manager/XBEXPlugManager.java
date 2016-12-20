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

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
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
 * @version 0.1.25 2015/09/06
 * @author ExBin Project (http://exbin.org)
 */
@Repository
public class XBEXPlugManager extends XBEDefaultCatalogManager<XBEXPlugin> implements XBCXPlugManager<XBEXPlugin>, Serializable {

    public XBEXPlugManager() {
        super();
    }

    public XBEXPlugManager(XBECatalog catalog) {
        super(catalog);
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
            if (parent.getParent() != null) {
                if (parent.getXBIndex() == null) {
                    return null;
                }
                list.add(0, parent.getXBIndex());
            }
            parent = (XBCNode) parent.getParent();
        }
        list.add(file.getId());
        return (Long[]) list.toArray(new Long[list.size()]);
    }

    @Override
    public void initializeExtension() {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getExtensionName() {
        return "Plugin Extension";
    }

    public XBEXFile findFile(XBCNode node, String fileName) {
        try {
            return (XBEXFile) catalog.getEntityManager().createQuery("SELECT object(o) FROM XBXFile as o WHERE o.parent.id = " + node.getId() + " AND o.filename = '" + fileName + "'").getSingleResult();
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

    @Override
    public InputStream getPlugin(XBCXPlugin plugin) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
