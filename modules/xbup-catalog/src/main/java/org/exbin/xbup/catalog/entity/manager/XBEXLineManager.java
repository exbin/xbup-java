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
import javax.persistence.NoResultException;
import org.exbin.xbup.catalog.XBECatalog;
import org.exbin.xbup.catalog.entity.XBEBlockRev;
import org.exbin.xbup.catalog.entity.XBEXBlockLine;
import org.exbin.xbup.catalog.entity.XBEXPlugLine;
import org.exbin.xbup.catalog.entity.XBEXPlugin;
import org.exbin.xbup.core.catalog.base.XBCBlockRev;
import org.exbin.xbup.core.catalog.base.XBCXBlockLine;
import org.exbin.xbup.core.catalog.base.XBCXPlugLine;
import org.exbin.xbup.core.catalog.base.XBCXPlugin;
import org.exbin.xbup.core.catalog.base.manager.XBCXLineManager;
import org.springframework.stereotype.Repository;

/**
 * XBUP catalog line editors manager.
 *
 * @version 0.1.25 2015/09/06
 * @author ExBin Project (http://exbin.org)
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
