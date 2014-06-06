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
package org.xbup.lib.xbcatalog.entity.service;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xbup.lib.xb.catalog.base.XBCBlockSpec;
import org.xbup.lib.xb.catalog.base.XBCItem;
import org.xbup.lib.xb.catalog.base.XBCSpec;
import org.xbup.lib.xb.catalog.base.XBCXStri;
import org.xbup.lib.xb.catalog.base.manager.XBCXStriManager;
import org.xbup.lib.xb.catalog.base.service.XBCXStriService;
import org.xbup.lib.xb.catalog.base.XBCExtension;
import org.xbup.lib.xbcatalog.XBECatalog;
import org.xbup.lib.xbcatalog.entity.XBEXStri;
import org.xbup.lib.xbcatalog.entity.manager.XBEXStriManager;

/**
 * Interface for XBEXStri items service.
 *
 * @version 0.1 wr22.0 2013/01/13
 * @author XBUP Project (http://xbup.org)
 */
@Service
public class XBEXStriService extends XBEDefaultService<XBEXStri> implements XBCXStriService<XBEXStri>, Serializable {

    @Autowired
    private XBEXStriManager manager;

    public XBEXStriService() {
        super();
    }

    public XBEXStriService(XBECatalog catalog) {
        super(catalog);
        itemManager = new XBEXStriManager(catalog);
        catalog.addCatalogManager(XBCXStriManager.class, itemManager);
    }

    @PostConstruct
    public void init() {
        itemManager = manager;
    }

    @Override
    public String getDefaultStringId(XBCBlockSpec blockSpec) {
        return ((XBEXStriManager)itemManager).getDefaultStringId(blockSpec);
    }

    @Override
    public XBEXStri getItemStringId(XBCItem item) {
        return ((XBEXStriManager)itemManager).getItemStringId(item);
    }

    @Override
    public List<XBCXStri> getItemStringIds(XBCItem item) {
        return ((XBEXStriManager)itemManager).getItemStringIds(item);
    }

    @Override
    public String getExtensionName() {
        return ((XBCExtension) itemManager).getExtensionName();
    }

    @Override
    public void initializeExtension() {
        ((XBCExtension) itemManager).initializeExtension();
    }

    @Override
    public String getItemStringIdText(XBCItem item) {
        XBEXStri Stri = getItemStringId(item);
        if (Stri == null) {
            return null;
        }
        return Stri.getText();
    }

    @Override
    public void setItemStringIdText(XBCItem item, String text) {
        XBEXStri stringId = getItemStringId(item);
        if (stringId == null) {
            stringId = createItem();
            stringId.setItem(item);
        }

        stringId.setText(text);
        persistItem(stringId);
    }

    @Override
    public String getFullPath(XBCXStri itemString) {
        return ((XBEXStriManager)itemManager).getFullPath(itemString);
    }

    @Override
    public String getItemFullPath(XBCItem item) {
        XBEXStri stri = getItemStringId(item);
        if (stri == null) {
            return null;
        }
        return getFullPath(stri);
    }

    @Override
    public XBCSpec getSpecByFullPath(String fullPath) {
        return ((XBEXStriManager)itemManager).getSpecByFullPath(fullPath);
    }
}
