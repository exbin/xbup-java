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
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.PostConstruct;
import javax.swing.ImageIcon;
import org.exbin.xbup.catalog.XBECatalog;
import org.exbin.xbup.catalog.entity.XBEXIcon;
import org.exbin.xbup.catalog.entity.XBEXIconMode;
import org.exbin.xbup.catalog.entity.manager.XBEXIconManager;
import org.exbin.xbup.core.catalog.base.XBCExtension;
import org.exbin.xbup.core.catalog.base.XBCItem;
import org.exbin.xbup.core.catalog.base.XBCXIcon;
import org.exbin.xbup.core.catalog.base.manager.XBCXIconManager;
import org.exbin.xbup.core.catalog.base.service.XBCXIconService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Interface for XBEXIcon items service.
 *
 * @version 0.2.1 2020/08/26
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
@Service
public class XBEXIconService extends XBEDefaultService<XBCXIcon> implements XBCXIconService, Serializable {

    @Autowired
    private XBEXIconManager manager;

    public XBEXIconService() {
        super();
    }

    public XBEXIconService(XBECatalog catalog) {
        super(catalog);
        itemManager = new XBEXIconManager(catalog);
        catalog.addCatalogManager(XBCXIconManager.class, (XBCXIconManager) itemManager);
    }

    @PostConstruct
    public void init() {
        itemManager = manager;
    }

    @Nonnull
    @Override
    public List<XBCXIcon> getItemIcons(XBCItem item) {
        return ((XBEXIconManager) itemManager).getItemIcons(item);
    }

    @Override
    public XBEXIcon getDefaultIcon(XBCItem item) {
        return ((XBEXIconManager) itemManager).getDefaultIcon(item);
    }

    @Override
    public ImageIcon getDefaultImageIcon(XBCItem item) {
        return ((XBEXIconManager) itemManager).getDefaultImageIcon(item);
    }

    @Override
    public XBEXIconMode getIconMode(Long modeId) {
        return ((XBEXIconManager) itemManager).getIconMode(modeId);
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
        return ((XBEXIconManager) itemManager).getDefaultBigIcon(item);
    }

    @Override
    public XBCXIcon getDefaultSmallIcon(XBCItem item) {
        return ((XBEXIconManager) itemManager).getDefaultSmallIcon(item);
    }

    @Override
    public byte[] getDefaultBigIconData(XBCItem item) {
        return ((XBEXIconManager) itemManager).getDefaultBigIconData(item);
    }

    @Override
    public byte[] getDefaultSmallIconData(XBCItem item) {
        return ((XBEXIconManager) itemManager).getDefaultSmallIconData(item);
    }
}
