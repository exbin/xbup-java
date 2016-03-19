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
package org.exbin.xbup.client.catalog.remote.service;

import org.exbin.xbup.client.catalog.XBRCatalog;
import org.exbin.xbup.client.catalog.remote.XBRXStri;
import org.exbin.xbup.client.catalog.remote.manager.XBRXStriManager;
import org.exbin.xbup.core.catalog.base.XBCExtension;
import org.exbin.xbup.core.catalog.base.XBCItem;
import org.exbin.xbup.core.catalog.base.XBCSpec;
import org.exbin.xbup.core.catalog.base.XBCXStri;
import org.exbin.xbup.core.catalog.base.manager.XBCXStriManager;
import org.exbin.xbup.core.catalog.base.service.XBCXStriService;

/**
 * Remote service for XBRXStri items.
 *
 * @version 0.1.25 2015/03/19
 * @author ExBin Project (http://exbin.org)
 */
public class XBRXStriService extends XBRDefaultService<XBRXStri> implements XBCXStriService<XBRXStri> {

    public XBRXStriService(XBRCatalog catalog) {
        super(catalog);
        itemManager = new XBRXStriManager(catalog);
        catalog.addCatalogManager(XBCXStriManager.class, itemManager);
    }

    @Override
    public String getItemStringIdText(XBCItem item) {
        return ((XBRXStriManager) itemManager).getItemStringIdText(item);
    }

    @Override
    public XBRXStri getItemStringId(XBCItem item) {
        return ((XBRXStriManager) itemManager).getItemStringId(item);
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
        XBRXStri stringId = getItemStringId(item);
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
        return ((XBRXStriManager) itemManager).getFullPath(itemString);
    }

    @Override
    public String getItemFullPath(XBCItem item) {
        return getFullPath(getItemStringId(item));
    }

    @Override
    public XBCSpec getSpecByFullPath(String fullPath) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
