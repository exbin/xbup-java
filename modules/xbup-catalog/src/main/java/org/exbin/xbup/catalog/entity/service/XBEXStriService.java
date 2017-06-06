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
package org.exbin.xbup.catalog.entity.service;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import org.exbin.xbup.catalog.XBECatalog;
import org.exbin.xbup.catalog.entity.XBEXStri;
import org.exbin.xbup.catalog.entity.manager.XBEXStriManager;
import org.exbin.xbup.core.catalog.base.XBCExtension;
import org.exbin.xbup.core.catalog.base.XBCItem;
import org.exbin.xbup.core.catalog.base.XBCSpec;
import org.exbin.xbup.core.catalog.base.XBCXStri;
import org.exbin.xbup.core.catalog.base.manager.XBCXStriManager;
import org.exbin.xbup.core.catalog.base.service.XBCXStriService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Interface for XBEXStri items service.
 *
 * @version 0.1.24 2014/11/18
 * @author ExBin Project (http://exbin.org)
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
        catalog.addCatalogManager(XBCXStriManager.class, (XBCXStriManager) itemManager);
    }

    @PostConstruct
    public void init() {
        itemManager = manager;
    }

    @Override
    public String getItemStringIdText(XBCItem item) {
        return ((XBEXStriManager) itemManager).getItemStringIdText(item);
    }

    @Override
    public XBEXStri getItemStringId(XBCItem item) {
        return ((XBEXStriManager) itemManager).getItemStringId(item);
    }

    @Override
    public String getExtensionName() {
        return ((XBCExtension) itemManager).getExtensionName();
    }

    @Override
    public void initializeExtension() {
        ((XBCExtension) itemManager).initializeExtension();
    }

    public void setItemStringIdText(XBCItem item, String text) {
        XBEXStri stringId = getItemStringId(item);
        if (text == null || text.isEmpty()) {
            if (stringId != null) {
                removeItem(stringId);
            }
        } else {
            if (stringId == null) {
                stringId = createItem();
                stringId.setItem(item);
            }

            stringId.setText(text);
            persistItem(stringId);
        }
    }

    @Override
    public String getFullPath(XBCXStri itemString) {
        return ((XBEXStriManager) itemManager).getFullPath(itemString);
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
        return ((XBEXStriManager) itemManager).getSpecByFullPath(fullPath);
    }
}
