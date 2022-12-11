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
package org.exbin.xbup.client.catalog.remote.manager;

import java.util.List;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.client.catalog.XBRCatalog;
import org.exbin.xbup.client.catalog.remote.XBRXName;
import org.exbin.xbup.client.stub.XBPXNameStub;
import org.exbin.xbup.core.catalog.base.XBCItem;
import org.exbin.xbup.core.catalog.base.XBCXLanguage;
import org.exbin.xbup.core.catalog.base.XBCXName;
import org.exbin.xbup.core.catalog.base.manager.XBCXNameManager;

/**
 * Remote manager class for XBRXName catalog items.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XBRXNameManager extends XBRDefaultManager<XBCXName> implements XBCXNameManager {

    private final XBPXNameStub nameStub;

    public XBRXNameManager(XBRCatalog catalog) {
        super(catalog);
        nameStub = new XBPXNameStub(client);
        setManagerStub(nameStub);
    }

    @Override
    public XBRXName getDefaultItemName(XBCItem item) {
        return nameStub.getDefaultItemName(item);
    }

    @Override
    public XBRXName getItemName(XBCItem item, XBCXLanguage language) {
        return nameStub.getItemName(item, language);
    }

    @Override
    public List<XBCXName> getItemNames(XBCItem item) {
        return nameStub.getItemNames(item);
    }

    @Override
    public void initializeExtension() {
    }

    @Override
    public String getExtensionName() {
        return "Name Extension";
    }

    @Override
    public String getDefaultText(XBCItem item) {
        XBCXName name = getDefaultItemName(item);
        if (name == null) {
            return null;
        }

        return name.getText();
    }
}
