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
package org.xbup.lib.catalog.entity.manager;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.NoResultException;
import org.springframework.stereotype.Repository;
import org.xbup.lib.catalog.XBECatalog;
import org.xbup.lib.catalog.entity.XBEItem;
import org.xbup.lib.catalog.entity.XBESpec;
import org.xbup.lib.catalog.entity.XBEXStri;
import org.xbup.lib.core.catalog.base.XBCItem;
import org.xbup.lib.core.catalog.base.XBCSpec;
import org.xbup.lib.core.catalog.base.XBCXStri;
import org.xbup.lib.core.catalog.base.manager.XBCNodeManager;
import org.xbup.lib.core.catalog.base.manager.XBCXStriManager;

/**
 * XBUP catalog string ID manager.
 *
 * @version 0.1.24 2014/11/18
 * @author ExBin Project (http://exbin.org)
 */
@Repository
public class XBEXStriManager extends XBEDefaultCatalogManager<XBEXStri> implements XBCXStriManager<XBEXStri>, Serializable {

    public XBEXStriManager() {
        super();
    }

    public XBEXStriManager(XBECatalog catalog) {
        super(catalog);
    }

    @Override
    public XBEXStri getItemStringId(XBCItem item) {
        if (!(item instanceof XBEItem)) {
            return null;
        }
        try {
            return (XBEXStri) catalog.getEntityManager().createQuery("SELECT object(o) FROM XBXStri as o WHERE o.item.id = " + ((XBEItem) item).getId()).getSingleResult();
        } catch (NoResultException ex) {
            return null;
        } catch (Exception ex) {
            Logger.getLogger(XBEXStriManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public void initializeExtension() {
    }

    @Override
    public String getExtensionName() {
        return "Stri Extension";
    }

    @Override
    public String getItemStringIdText(XBCItem item) {
        XBCXStri stri = getItemStringId(item);
        if (stri == null) {
            return null;
        }

        return stri.getText();
    }

    @Override
    public String getFullPath(XBCXStri itemString) {
        if (itemString == null) {
            return "";
        }
        final String nodePath = itemString.getNodePath();
        if ("/".equals(nodePath)) {
            XBCNodeManager nodeManager = (XBCNodeManager) catalog.getCatalogManager(XBCNodeManager.class);
            if (itemString.getItem().getId().equals(nodeManager.getRootNode().getId())) {
                return "";
            }
            return nodePath + itemString.getText();
        }
        return nodePath + "/" + itemString.getText();
    }

    public XBCSpec getSpecByFullPath(String fullPath) {
        if (fullPath == null) {
            return null;
        }

        int lastSlash = fullPath.lastIndexOf("/");
        String nodePath = fullPath.substring(0, lastSlash);
        String specStri = fullPath.substring(lastSlash + 1);
        try {
            return (XBESpec) catalog.getEntityManager().createQuery("SELECT object(o) FROM XBSpec as o WHERE EXISTS ( SELECT p FROM XBXStri as p WHERE p.item.id = o.id AND p.text = '" + specStri + "' AND p.nodePath = '" + nodePath + "')").getSingleResult();
        } catch (NoResultException ex) {
            return null;
        } catch (Exception ex) {
            Logger.getLogger(XBEXStriManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
