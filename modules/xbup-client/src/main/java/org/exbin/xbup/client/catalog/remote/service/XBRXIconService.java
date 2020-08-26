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
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.ImageIcon;
import org.exbin.xbup.client.catalog.XBRCatalog;
import org.exbin.xbup.client.catalog.remote.XBRXIcon;
import org.exbin.xbup.client.catalog.remote.XBRXIconMode;
import org.exbin.xbup.client.catalog.remote.manager.XBRXIconManager;
import org.exbin.xbup.core.catalog.base.XBCExtension;
import org.exbin.xbup.core.catalog.base.XBCItem;
import org.exbin.xbup.core.catalog.base.XBCXIcon;
import org.exbin.xbup.core.catalog.base.manager.XBCXIconManager;
import org.exbin.xbup.core.catalog.base.service.XBCXIconService;

/**
 * Remote service for XBRXIcon items.
 *
 * @version 0.2.1 2020/08/26
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XBRXIconService extends XBRDefaultService<XBCXIcon> implements XBCXIconService {

    public XBRXIconService(XBRCatalog catalog) {
        super(catalog);
        itemManager = new XBRXIconManager(catalog);
        catalog.addCatalogManager(XBCXIconManager.class, (XBCXIconManager) itemManager);
    }

    @Nonnull
    @Override
    public List<XBCXIcon> getItemIcons(XBCItem item) {
        return ((XBRXIconManager) itemManager).getItemIcons(item);
    }

    @Override
    public XBRXIcon getDefaultIcon(XBCItem item) {
        return ((XBRXIconManager) itemManager).getDefaultIcon(item);
    }

    @Override
    public ImageIcon getDefaultImageIcon(XBCItem item) {
        return ((XBRXIconManager) itemManager).getDefaultImageIcon(item);
    }

    @Override
    public XBRXIconMode getIconMode(Long type) {
        return ((XBRXIconManager) itemManager).getIconMode(type);
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
    public XBCXIcon getDefaultBigIcon(XBCItem item) {
        return ((XBRXIconManager) itemManager).getDefaultBigIcon(item);
    }

    @Override
    public XBCXIcon getDefaultSmallIcon(XBCItem item) {
        return ((XBRXIconManager) itemManager).getDefaultSmallIcon(item);
    }

    @Override
    public byte[] getDefaultBigIconData(XBCItem item) {
        return ((XBRXIconManager) itemManager).getDefaultBigIconData(item);
    }

    @Override
    public byte[] getDefaultSmallIconData(XBCItem item) {
        return ((XBRXIconManager) itemManager).getDefaultSmallIconData(item);
    }
}
