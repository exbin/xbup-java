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
import org.exbin.xbup.client.catalog.remote.XBRXStri;
import org.exbin.xbup.client.catalog.remote.manager.XBRXStriManager;
import org.exbin.xbup.core.catalog.base.XBCExtension;
import org.exbin.xbup.core.catalog.base.XBCItem;
import org.exbin.xbup.core.catalog.base.XBCSpec;
import org.exbin.xbup.core.catalog.base.XBCXStri;
import org.exbin.xbup.core.catalog.base.manager.XBCXStriManager;
import org.exbin.xbup.core.catalog.base.service.XBCXStriService;

/**
 * Remote service for XBRXStri items.
 *
 * @version 0.2.1 2020/08/26
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XBRXStriService extends XBRDefaultService<XBCXStri> implements XBCXStriService {

    public XBRXStriService(XBRCatalog catalog) {
        super(catalog);
        itemManager = new XBRXStriManager(catalog);
        catalog.addCatalogManager(XBCXStriManager.class, (XBCXStriManager) itemManager);
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

    public void setItemStringIdText(XBCItem item, String text) {
        XBRXStri stringId = getItemStringId(item);
        if (text == null || text.isEmpty()) {
            if (stringId != null) {
                removeItem(stringId);
            }
        } else {
            throw new UnsupportedOperationException("Not supported yet.");
//            if (stringId == null) {
//                stringId = createItem();
//                stringId.setItem(item);
//            }
//
//            stringId.setText(text);
//            persistItem(stringId);
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
