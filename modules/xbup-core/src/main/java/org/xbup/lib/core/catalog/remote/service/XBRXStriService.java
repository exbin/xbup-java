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
package org.xbup.lib.core.catalog.remote.service;

import org.xbup.lib.core.catalog.XBRCatalog;
import org.xbup.lib.core.catalog.base.XBCItem;
import org.xbup.lib.core.catalog.base.XBCSpec;
import org.xbup.lib.core.catalog.base.XBCXStri;
import org.xbup.lib.core.catalog.base.manager.XBCXStriManager;
import org.xbup.lib.core.catalog.base.service.XBCXStriService;
import org.xbup.lib.core.catalog.base.XBCExtension;
import org.xbup.lib.core.catalog.remote.XBRXStri;
import org.xbup.lib.core.catalog.remote.manager.XBRXStriManager;

/**
 * Interface for XBRXStri items service.
 *
 * @version 0.1.24 2014/11/17
 * @author XBUP Project (http://xbup.org)
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

    @Override
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
