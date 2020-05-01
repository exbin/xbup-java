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
package org.exbin.xbup.client.catalog.remote.manager;

import java.util.List;
import org.exbin.xbup.client.catalog.XBRCatalog;
import org.exbin.xbup.client.catalog.remote.XBRXLanguage;
import org.exbin.xbup.client.stub.XBPXLangStub;
import org.exbin.xbup.core.catalog.base.XBCXLanguage;
import org.exbin.xbup.core.catalog.base.manager.XBCXLangManager;

/**
 * Remote manager class for XBRXLanguage catalog items.
 *
 * @version 0.1.25 2015/02/21
 * @author ExBin Project (http://exbin.org)
 */
public class XBRXLangManager extends XBRDefaultManager<XBRXLanguage> implements XBCXLangManager<XBRXLanguage> {

    private final XBPXLangStub langStub;

    public XBRXLangManager(XBRCatalog catalog) {
        super(catalog);
        langStub = new XBPXLangStub(client);
        setManagerStub(langStub);
    }

    @Override
    public XBRXLanguage getDefaultLang() {
        return langStub.getDefaultLang();
    }

    public List<XBCXLanguage> getLangs() {
        return langStub.getLangs();
    }

    @Override
    public void initializeExtension() {
    }

    @Override
    public String getExtensionName() {
        return "Language Extension";
    }
}
