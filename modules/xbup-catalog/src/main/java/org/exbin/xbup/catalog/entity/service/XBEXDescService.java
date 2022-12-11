/*
 * Copyright (C) ExBin Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
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
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.PostConstruct;
import org.exbin.xbup.catalog.XBECatalog;
import org.exbin.xbup.catalog.entity.XBEXDesc;
import org.exbin.xbup.catalog.entity.manager.XBEXDescManager;
import org.exbin.xbup.core.catalog.base.XBCExtension;
import org.exbin.xbup.core.catalog.base.XBCItem;
import org.exbin.xbup.core.catalog.base.XBCXDesc;
import org.exbin.xbup.core.catalog.base.XBCXLanguage;
import org.exbin.xbup.core.catalog.base.manager.XBCXDescManager;
import org.exbin.xbup.core.catalog.base.manager.XBCXLangManager;
import org.exbin.xbup.core.catalog.base.service.XBCXDescService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Interface for XBEXDesc items service.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
@Service
public class XBEXDescService extends XBEDefaultService<XBCXDesc> implements XBCXDescService, Serializable {

    @Autowired
    private XBEXDescManager manager;

    public XBEXDescService() {
        super();
    }

    public XBEXDescService(XBECatalog catalog) {
        super(catalog);
        itemManager = new XBEXDescManager(catalog);
        catalog.addCatalogManager(XBCXDescManager.class, (XBCXDescManager) itemManager);
    }

    @PostConstruct
    public void init() {
        itemManager = manager;
    }

    @Override
    public XBEXDesc getDefaultItemDesc(XBCItem item) {
        return ((XBEXDescManager) itemManager).getDefaultItemDesc(item);
    }

    @Override
    public XBEXDesc getItemDesc(XBCItem item, XBCXLanguage language) {
        return ((XBEXDescManager) itemManager).getItemDesc(item, language);
    }

    @Override
    public List<XBCXDesc> getItemDescs(XBCItem item) {
        return ((XBEXDescManager) itemManager).getItemDescs(item);
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
        return ((XBEXDescManager) itemManager).getDefaultText(item);
    }

    public void setDefaultText(XBCItem item, String text) {
        XBEXDesc desc = getDefaultItemDesc(item);
        if (text == null || text.isEmpty()) {
            if (desc != null) {
                removeItem(desc);
            }
        } else {
            if (desc == null) {
                XBCXLangManager langManager = catalog.getCatalogManager(XBCXLangManager.class);
                desc = (XBEXDesc) createItem();
                desc.setItem(item);
                desc.setLang(langManager.getDefaultLang());
            }

            desc.setText(text);
            persistItem(desc);
        }
    }
}
