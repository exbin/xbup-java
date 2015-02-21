/*
 * Copyright (C) XBUP Project
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
package org.xbup.lib.client.catalog.remote.service;

import java.util.List;
import javax.swing.ImageIcon;
import org.xbup.lib.client.catalog.XBRCatalog;
import org.xbup.lib.core.catalog.base.XBCBlockSpec;
import org.xbup.lib.core.catalog.base.XBCItem;
import org.xbup.lib.core.catalog.base.XBCXIcon;
import org.xbup.lib.core.catalog.base.manager.XBCXIconManager;
import org.xbup.lib.core.catalog.base.service.XBCXIconService;
import org.xbup.lib.core.catalog.base.XBCExtension;
import org.xbup.lib.client.catalog.remote.XBRXIcon;
import org.xbup.lib.client.catalog.remote.XBRXIconMode;
import org.xbup.lib.client.catalog.remote.manager.XBRXIconManager;

/**
 * Remote service for XBRXIcon items.
 *
 * @version 0.1.24 2014/11/26
 * @author XBUP Project (http://xbup.org)
 */
public class XBRXIconService extends XBRDefaultService<XBRXIcon> implements XBCXIconService<XBRXIcon> {

    public XBRXIconService(XBRCatalog catalog) {
        super(catalog);
        itemManager = new XBRXIconManager(catalog);
        catalog.addCatalogManager(XBCXIconManager.class, itemManager);
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
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public XBCXIcon getDefaultSmallIcon(XBCItem item) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public byte[] getDefaultBigIconData(XBCItem item) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public byte[] getDefaultSmallIconData(XBCItem item) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
