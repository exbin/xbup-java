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

import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.client.catalog.XBRCatalog;
import org.exbin.xbup.client.catalog.remote.XBRXStri;
import org.exbin.xbup.client.stub.XBPXStriStub;
import org.exbin.xbup.core.catalog.base.XBCItem;
import org.exbin.xbup.core.catalog.base.XBCXStri;
import org.exbin.xbup.core.catalog.base.manager.XBCNodeManager;
import org.exbin.xbup.core.catalog.base.manager.XBCXStriManager;

/**
 * Remote manager class for XBRXStri catalog items.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XBRXStriManager extends XBRDefaultManager<XBCXStri> implements XBCXStriManager {

    private final XBPXStriStub striStub;

    public XBRXStriManager(XBRCatalog catalog) {
        super(catalog);
        striStub = new XBPXStriStub(client);
        setManagerStub(striStub);
    }

    @Override
    public XBRXStri getItemStringId(XBCItem item) {
        return striStub.getItemStringId(item);
    }

    @Override
    public void initializeExtension() {
    }

    @Override
    public String getExtensionName() {
        return "Stri Extension";
    }

    @Override
    public String getItemStringIdText(XBCItem item) {
        XBCXStri Stri = getItemStringId(item);
        if (Stri == null) {
            return null;
        }
        return Stri.getText();
    }

    @Override
    public String getFullPath(XBCXStri itemString) {
        if ("/".equals(itemString.getNodePath())) {
            XBRNodeManager nodeManager = (XBRNodeManager) catalog.getCatalogManager(XBCNodeManager.class);
            if (itemString.getItem().getId() == nodeManager.getMainRootNode().get().getId()) {
                return "";
            }
        }
        return itemString.getNodePath() + "/" + itemString.getText();
    }
}
