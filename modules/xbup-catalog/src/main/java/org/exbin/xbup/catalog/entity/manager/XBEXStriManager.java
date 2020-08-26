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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.persistence.NoResultException;
import org.exbin.xbup.catalog.XBECatalog;
import org.exbin.xbup.catalog.entity.XBEItem;
import org.exbin.xbup.catalog.entity.XBESpec;
import org.exbin.xbup.catalog.entity.XBEXStri;
import org.exbin.xbup.core.catalog.base.XBCItem;
import org.exbin.xbup.core.catalog.base.XBCSpec;
import org.exbin.xbup.core.catalog.base.XBCXStri;
import org.exbin.xbup.core.catalog.base.manager.XBCNodeManager;
import org.exbin.xbup.core.catalog.base.manager.XBCXStriManager;
import org.springframework.stereotype.Repository;

/**
 * XBUP catalog string ID manager.
 *
 * @version 0.2.1 2020/08/26
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
@Repository
public class XBEXStriManager extends XBEDefaultCatalogManager<XBCXStri> implements XBCXStriManager, Serializable {

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
            XBCNodeManager nodeManager = catalog.getCatalogManager(XBCNodeManager.class);
            if (itemString.getItem().getId() == nodeManager.getMainRootNode().get().getId()) {
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
