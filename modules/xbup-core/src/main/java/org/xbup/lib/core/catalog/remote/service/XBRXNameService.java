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

import java.util.List;
import org.xbup.lib.core.catalog.XBRCatalog;
import org.xbup.lib.core.catalog.base.XBCItem;
import org.xbup.lib.core.catalog.base.XBCXLanguage;
import org.xbup.lib.core.catalog.base.XBCXName;
import org.xbup.lib.core.catalog.base.manager.XBCXLangManager;
import org.xbup.lib.core.catalog.base.manager.XBCXNameManager;
import org.xbup.lib.core.catalog.base.service.XBCXNameService;
import org.xbup.lib.core.catalog.base.XBCExtension;
import org.xbup.lib.core.catalog.remote.XBRXName;
import org.xbup.lib.core.catalog.remote.manager.XBRXLangManager;
import org.xbup.lib.core.catalog.remote.manager.XBRXNameManager;

/**
 * Interface for XBRXName items service.
 *
 * @version 0.1.24 2014/11/17
 * @author XBUP Project (http://xbup.org)
 */
public class XBRXNameService extends XBRDefaultService<XBRXName> implements XBCXNameService<XBRXName> {

    public XBRXNameService(XBRCatalog catalog) {
        super(catalog);
        itemManager = new XBRXNameManager(catalog);
        catalog.addCatalogManager(XBCXNameManager.class, itemManager);
    }

    @Override
    public String getDefaultText(XBCItem item) {
        return ((XBRXNameManager) itemManager).getDefaultText(item);
    }

    @Override
    public XBRXName getItemName(XBCItem item) {
        return ((XBRXNameManager) itemManager).getItemName(item);
    }

    @Override
    public XBRXName getItemName(XBCItem item, XBCXLanguage language) {
        return ((XBRXNameManager) itemManager).getItemName(item, language);
    }

    @Override
    public List<XBCXName> getItemNames(XBCItem item) {
        return ((XBRXNameManager) itemManager).getItemNames(item);
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
    public void setDefaultText(XBCItem item, String text) {
        XBRXName name = getItemName(item);
        if (text == null || text.isEmpty()) {
            if (name != null) {
                removeItem(name);
            }
        } else {
            if (name == null) {
                XBRXLangManager langManager = ((XBRXLangManager) catalog.getCatalogManager(XBCXLangManager.class));
                name = createItem();
                name.setItem(item);
                name.setLang(langManager.getDefaultLang());
            }

            name.setText(text);
            persistItem(name);
        }
    }

    @Override
    public String getItemNamePath(XBCItem item) {
        StringBuilder builder = new StringBuilder();
        builder.append(getDefaultText(item));

        XBCItem parentItem = item.getParent();
        while (parentItem != null && parentItem.getParent() != null) {
            builder.insert(0, getDefaultText(parentItem) + ".");
            parentItem = parentItem.getParent();
        }

        return builder.toString();
    }
}
