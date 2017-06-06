/*
 * Copyright (C) ExBin Project
 *
 * This application or library is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * This application or library is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along this application.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.exbin.xbup.catalog.entity.service;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.swing.ImageIcon;
import org.exbin.xbup.catalog.XBECatalog;
import org.exbin.xbup.catalog.entity.XBEXIcon;
import org.exbin.xbup.catalog.entity.XBEXIconMode;
import org.exbin.xbup.catalog.entity.manager.XBEXIconManager;
import org.exbin.xbup.core.catalog.base.XBCBlockSpec;
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
 * @version 0.1.24 2014/11/26
 * @author ExBin Project (http://exbin.org)
 */
@Service
public class XBEXIconService extends XBEDefaultService<XBEXIcon> implements XBCXIconService<XBEXIcon>, Serializable {

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

    @Override
    public List<XBCXIcon> getBlockSpecIcons(XBCBlockSpec icon) {
        return ((XBEXIconManager) itemManager).getBlockSpecIcons(icon);
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
    public XBEXIconMode getIconMode(Long type) {
        return ((XBEXIconManager) itemManager).getIconMode(type);
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
