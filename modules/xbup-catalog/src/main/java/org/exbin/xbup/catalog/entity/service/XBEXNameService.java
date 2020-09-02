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
package org.exbin.xbup.catalog.entity.service;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.PostConstruct;
import org.exbin.xbup.catalog.XBECatalog;
import org.exbin.xbup.catalog.entity.XBEXName;
import org.exbin.xbup.catalog.entity.manager.XBEXNameManager;
import org.exbin.xbup.core.catalog.base.XBCExtension;
import org.exbin.xbup.core.catalog.base.XBCItem;
import org.exbin.xbup.core.catalog.base.XBCXLanguage;
import org.exbin.xbup.core.catalog.base.XBCXName;
import org.exbin.xbup.core.catalog.base.manager.XBCXLangManager;
import org.exbin.xbup.core.catalog.base.manager.XBCXNameManager;
import org.exbin.xbup.core.catalog.base.service.XBCXNameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Interface for XBEXName items service.
 *
 * @version 0.2.1 2020/08/11
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
@Service
public class XBEXNameService extends XBEDefaultService<XBCXName> implements XBCXNameService, Serializable {

    @Autowired
    private XBEXNameManager manager;

    public XBEXNameService() {
        super();
    }

    public XBEXNameService(XBECatalog catalog) {
        super(catalog);
        itemManager = new XBEXNameManager(catalog);
        catalog.addCatalogManager(XBCXNameManager.class, (XBCXNameManager) itemManager);
    }

    @PostConstruct
    public void init() {
        itemManager = manager;
    }

    @Override
    public XBEXName getDefaultItemName(XBCItem item) {
        return ((XBEXNameManager) itemManager).getDefaultItemName(item);
    }

    @Override
    public XBEXName getItemName(XBCItem item, XBCXLanguage language) {
        return ((XBEXNameManager) itemManager).getItemName(item, language);
    }

    @Override
    public List<XBCXName> getItemNames(XBCItem item) {
        return ((XBEXNameManager) itemManager).getItemNames(item);
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
    public String getDefaultText(XBCItem item) {
        return ((XBEXNameManager) itemManager).getDefaultText(item);
    }

    public void setDefaultText(XBCItem item, String text) {
        XBEXName name = getDefaultItemName(item);
        if (text == null || text.isEmpty()) {
            if (name != null) {
                removeItem(name);
            }
        } else {
            if (name == null) {
                XBCXLangManager langManager = catalog.getCatalogManager(XBCXLangManager.class);
                name = (XBEXName) createItem();
                name.setItem(item);
                name.setLang(langManager.getDefaultLang());
            }

            name.setText(text);
            persistItem(name);
        }
    }

    @Override
    public String getItemNamePath(@Nullable XBCItem item) {
        if (item == null) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        builder.append(getDefaultText(item));

        Optional<XBCItem> optionalParent = item.getParentItem();
        while (optionalParent.isPresent() && optionalParent.get().getParentItem().isPresent()) {
            XBCItem parentItem = optionalParent.get();
            builder.insert(0, getDefaultText(parentItem) + ".");
            optionalParent = parentItem.getParentItem();
        }

        return builder.toString();
    }
}
