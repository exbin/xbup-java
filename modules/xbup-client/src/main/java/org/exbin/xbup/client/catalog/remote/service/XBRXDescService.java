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
import org.exbin.xbup.client.catalog.XBRCatalog;
import org.exbin.xbup.client.catalog.remote.XBRXDesc;
import org.exbin.xbup.client.catalog.remote.manager.XBRXDescManager;
import org.exbin.xbup.client.catalog.remote.manager.XBRXLangManager;
import org.exbin.xbup.core.catalog.base.XBCExtension;
import org.exbin.xbup.core.catalog.base.XBCItem;
import org.exbin.xbup.core.catalog.base.XBCXDesc;
import org.exbin.xbup.core.catalog.base.XBCXLanguage;
import org.exbin.xbup.core.catalog.base.manager.XBCXDescManager;
import org.exbin.xbup.core.catalog.base.manager.XBCXLangManager;
import org.exbin.xbup.core.catalog.base.service.XBCXDescService;

/**
 * Remote service for XBRXDesc items.
 *
 * @version 0.1.25 2015/03/19
 * @author ExBin Project (http://exbin.org)
 */
public class XBRXDescService extends XBRDefaultService<XBRXDesc> implements XBCXDescService<XBRXDesc> {

    public XBRXDescService(XBRCatalog catalog) {
        super(catalog);
        itemManager = new XBRXDescManager(catalog);
        catalog.addCatalogManager(XBCXDescManager.class, (XBCXDescManager) itemManager);
    }

    @Override
    public XBRXDesc getDefaultItemDesc(XBCItem item) {
        return ((XBRXDescManager) itemManager).getDefaultItemDesc(item);
    }

    @Override
    public XBRXDesc getItemDesc(XBCItem item, XBCXLanguage language) {
        return ((XBRXDescManager) itemManager).getItemDesc(item, language);
    }

    @Override
    public List<XBCXDesc> getItemDescs(XBCItem item) {
        return ((XBRXDescManager) itemManager).getItemDescs(item);
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
    public String getDefaultText(XBCItem item) {
        XBRXDesc desc = getDefaultItemDesc(item);
        if (desc == null) {
            return null;
        }
        return desc.getText();
    }

    public void setDefaultText(XBCItem item, String text) {
        XBRXDesc desc = getDefaultItemDesc(item);
        if (desc == null) {
            XBRXLangManager langManager = ((XBRXLangManager) catalog.getCatalogManager(XBCXLangManager.class));
            desc = createItem();
            desc.setItem(item);
            desc.setLang(langManager.getDefaultLang());
        }

        desc.setText(text);
        persistItem(desc);
    }

}
