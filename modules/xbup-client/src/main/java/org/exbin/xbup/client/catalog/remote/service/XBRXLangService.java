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

import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.client.catalog.XBRCatalog;
import org.exbin.xbup.client.catalog.remote.XBRXLanguage;
import org.exbin.xbup.client.catalog.remote.manager.XBRXLangManager;
import org.exbin.xbup.core.catalog.base.XBCExtension;
import org.exbin.xbup.core.catalog.base.XBCXLanguage;
import org.exbin.xbup.core.catalog.base.manager.XBCXLangManager;
import org.exbin.xbup.core.catalog.base.service.XBCXLangService;

/**
 * Remote service for XBRXLanguage items.
 *
 * @version 0.2.1 2020/08/26
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XBRXLangService extends XBRDefaultService<XBCXLanguage> implements XBCXLangService {

    public XBRXLangService(XBRCatalog catalog) {
        super(catalog);
        itemManager = new XBRXLangManager(catalog);
        catalog.addCatalogManager(XBCXLangManager.class, (XBCXLangManager) itemManager);
    }

    @Override
    public XBRXLanguage getDefaultLang() {
        return ((XBRXLangManager) itemManager).getDefaultLang();
    }

    @Override
    public String getExtensionName() {
        return ((XBCExtension) itemManager).getExtensionName();
    }

    @Override
    public void initializeExtension() {
        ((XBCExtension) itemManager).initializeExtension();
    }

}
