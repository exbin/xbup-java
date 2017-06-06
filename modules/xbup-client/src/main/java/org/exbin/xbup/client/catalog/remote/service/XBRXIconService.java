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
package org.exbin.xbup.client.catalog.remote.service;

import java.util.List;
import javax.swing.ImageIcon;
import org.exbin.xbup.client.catalog.XBRCatalog;
import org.exbin.xbup.client.catalog.remote.XBRXIcon;
import org.exbin.xbup.client.catalog.remote.XBRXIconMode;
import org.exbin.xbup.client.catalog.remote.manager.XBRXIconManager;
import org.exbin.xbup.core.catalog.base.XBCBlockSpec;
import org.exbin.xbup.core.catalog.base.XBCExtension;
import org.exbin.xbup.core.catalog.base.XBCItem;
import org.exbin.xbup.core.catalog.base.XBCXIcon;
import org.exbin.xbup.core.catalog.base.manager.XBCXIconManager;
import org.exbin.xbup.core.catalog.base.service.XBCXIconService;

/**
 * Remote service for XBRXIcon items.
 *
 * @version 0.1.25 2015/09/06
 * @author ExBin Project (http://exbin.org)
 */
public class XBRXIconService extends XBRDefaultService<XBRXIcon> implements XBCXIconService<XBRXIcon> {

    public XBRXIconService(XBRCatalog catalog) {
        super(catalog);
        itemManager = new XBRXIconManager(catalog);
        catalog.addCatalogManager(XBCXIconManager.class, (XBCXIconManager) itemManager);
    }

    @Override
    public List<XBCXIcon> getBlockSpecIcons(XBCBlockSpec icon) {
        return ((XBRXIconManager) itemManager).getBlockSpecIcons(icon);
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
