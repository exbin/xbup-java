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
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.client.catalog.XBRCatalog;
import org.exbin.xbup.client.catalog.remote.XBRXDesc;
import org.exbin.xbup.client.catalog.remote.manager.XBRXDescManager;
import org.exbin.xbup.core.catalog.base.XBCExtension;
import org.exbin.xbup.core.catalog.base.XBCItem;
import org.exbin.xbup.core.catalog.base.XBCXDesc;
import org.exbin.xbup.core.catalog.base.XBCXLanguage;
import org.exbin.xbup.core.catalog.base.manager.XBCXDescManager;
import org.exbin.xbup.core.catalog.base.service.XBCXDescService;

/**
 * Remote service for XBRXDesc items.
 *
 * @version 0.2.1 2020/08/26
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XBRXDescService extends XBRDefaultService<XBCXDesc> implements XBCXDescService {

    public XBRXDescService(XBRCatalog catalog) {
        super(catalog);
        itemManager = new XBRXDescManager(catalog);
        catalog.addCatalogManager(XBCXDescManager.class, (XBCXDescManager) itemManager);
    }

    @Override
    public XBRXDesc getDefaultItemDesc(XBCItem item) {
        return ((XBRXDescManager) itemManager).getDefaultItemDesc(item);
    }

    @Override
    public XBRXDesc getItemDesc(XBCItem item, XBCXLanguage language) {
        return ((XBRXDescManager) itemManager).getItemDesc(item, language);
    }

    @Override
    public List<XBCXDesc> getItemDescs(XBCItem item) {
        return ((XBRXDescManager) itemManager).getItemDescs(item);
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
        XBRXDesc desc = getDefaultItemDesc(item);
        if (desc == null) {
            return null;
        }
        return desc.getText();
    }

    public void setDefaultText(XBCItem item, String text) {
        throw new UnsupportedOperationException("Not supported yet.");
//        XBRXDesc desc = getDefaultItemDesc(item);
//        if (desc == null) {
//            XBRXLangManager langManager = ((XBRXLangManager) catalog.getCatalogManager(XBCXLangManager.class));
//            desc = createItem();
//            desc.setItem(item);
//            desc.setLang(langManager.getDefaultLang());
//        }
//
//        desc.setText(text);
//        persistItem(desc);
    }

}
