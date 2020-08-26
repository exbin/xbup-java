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
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.PostConstruct;
import org.exbin.xbup.catalog.XBECatalog;
import org.exbin.xbup.catalog.entity.XBEXStri;
import org.exbin.xbup.catalog.entity.manager.XBEXStriManager;
import org.exbin.xbup.core.catalog.base.XBCExtension;
import org.exbin.xbup.core.catalog.base.XBCItem;
import org.exbin.xbup.core.catalog.base.XBCSpec;
import org.exbin.xbup.core.catalog.base.XBCXStri;
import org.exbin.xbup.core.catalog.base.manager.XBCXStriManager;
import org.exbin.xbup.core.catalog.base.service.XBCXStriService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Interface for XBEXStri items service.
 *
 * @version 0.2.1 2020/08/26
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
@Service
public class XBEXStriService extends XBEDefaultService<XBCXStri> implements XBCXStriService, Serializable {

    @Autowired
    private XBEXStriManager manager;

    public XBEXStriService() {
        super();
    }

    public XBEXStriService(XBECatalog catalog) {
        super(catalog);
        itemManager = new XBEXStriManager(catalog);
        catalog.addCatalogManager(XBCXStriManager.class, (XBCXStriManager) itemManager);
    }

    @PostConstruct
    public void init() {
        itemManager = manager;
    }

    @Override
    public String getItemStringIdText(XBCItem item) {
        return ((XBEXStriManager) itemManager).getItemStringIdText(item);
    }

    @Override
    public XBEXStri getItemStringId(XBCItem item) {
        return ((XBEXStriManager) itemManager).getItemStringId(item);
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
        XBEXStri stringId = getItemStringId(item);
        if (text == null || text.isEmpty()) {
            if (stringId != null) {
                removeItem(stringId);
            }
        } else {
            if (stringId == null) {
                stringId = (XBEXStri) createItem();
                stringId.setItem(item);
            }

            stringId.setText(text);
            persistItem(stringId);
        }
    }

    @Override
    public String getFullPath(XBCXStri itemString) {
        return ((XBEXStriManager) itemManager).getFullPath(itemString);
    }

    @Override
    public String getItemFullPath(XBCItem item) {
        XBEXStri stri = getItemStringId(item);
        if (stri == null) {
            return null;
        }
        return getFullPath(stri);
    }

    @Override
    public XBCSpec getSpecByFullPath(String fullPath) {
        return ((XBEXStriManager) itemManager).getSpecByFullPath(fullPath);
    }
}
