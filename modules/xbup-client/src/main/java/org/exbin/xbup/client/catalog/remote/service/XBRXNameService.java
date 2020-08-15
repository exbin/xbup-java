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
package org.exbin.xbup.client.catalog.remote.service;

import java.util.List;
import java.util.Optional;
import org.exbin.xbup.client.catalog.XBRCatalog;
import org.exbin.xbup.client.catalog.remote.XBRXName;
import org.exbin.xbup.client.catalog.remote.manager.XBRXLangManager;
import org.exbin.xbup.client.catalog.remote.manager.XBRXNameManager;
import org.exbin.xbup.core.catalog.base.XBCExtension;
import org.exbin.xbup.core.catalog.base.XBCItem;
import org.exbin.xbup.core.catalog.base.XBCXLanguage;
import org.exbin.xbup.core.catalog.base.XBCXName;
import org.exbin.xbup.core.catalog.base.manager.XBCXLangManager;
import org.exbin.xbup.core.catalog.base.manager.XBCXNameManager;
import org.exbin.xbup.core.catalog.base.service.XBCXNameService;

/**
 * Remote service for XBRXName items.
 *
 * @version 0.1.25 2015/03/19
 * @author ExBin Project (http://exbin.org)
 */
public class XBRXNameService extends XBRDefaultService<XBRXName> implements XBCXNameService<XBRXName> {

    public XBRXNameService(XBRCatalog catalog) {
        super(catalog);
        itemManager = new XBRXNameManager(catalog);
        catalog.addCatalogManager(XBCXNameManager.class, (XBCXNameManager) itemManager);
    }

    @Override
    public String getDefaultText(XBCItem item) {
        return ((XBRXNameManager) itemManager).getDefaultText(item);
    }

    @Override
    public XBRXName getDefaultItemName(XBCItem item) {
        return ((XBRXNameManager) itemManager).getDefaultItemName(item);
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

    public void setDefaultText(XBCItem item, String text) {
        XBRXName name = getDefaultItemName(item);
        if (text == null || text.isEmpty()) {
            if (name != null) {
                removeItem(name);
            }
        } else {
            throw new UnsupportedOperationException("Not supported yet.");
//            if (name == null) {
//                XBRXLangManager langManager = ((XBRXLangManager) catalog.getCatalogManager(XBCXLangManager.class));
//                name = createItem();
//                name.setItem(item);
//                name.setLang(langManager.getDefaultLang());
//            }
//
//            name.setText(text);
//            persistItem(name);
        }
    }

    @Override
    public String getItemNamePath(XBCItem item) {
        StringBuilder builder = new StringBuilder();
        builder.append(getDefaultText(item));

        Optional<XBCItem> parentItem = item.getParentItem();
        while (parentItem.isPresent() && parentItem.get().getParentItem().isPresent()) {
            builder.insert(0, getDefaultText(parentItem.get()) + ".");
            parentItem = parentItem.get().getParentItem();
        }

        return builder.toString();
    }
}
