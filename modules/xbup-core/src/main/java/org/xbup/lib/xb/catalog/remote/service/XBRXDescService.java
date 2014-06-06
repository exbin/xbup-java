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
package org.xbup.lib.xb.catalog.remote.service;

import java.util.List;
import org.xbup.lib.xb.catalog.XBRCatalog;
import org.xbup.lib.xb.catalog.base.XBCItem;
import org.xbup.lib.xb.catalog.base.XBCXDesc;
import org.xbup.lib.xb.catalog.base.XBCXLanguage;
import org.xbup.lib.xb.catalog.base.manager.XBCXDescManager;
import org.xbup.lib.xb.catalog.base.manager.XBCXLangManager;
import org.xbup.lib.xb.catalog.base.service.XBCXDescService;
import org.xbup.lib.xb.catalog.base.XBCExtension;
import org.xbup.lib.xb.catalog.remote.XBRXDesc;
import org.xbup.lib.xb.catalog.remote.manager.XBRXDescManager;
import org.xbup.lib.xb.catalog.remote.manager.XBRXLangManager;

/**
 * Interface for XBRXDesc items service.
 *
 * @version 0.1 wr21.0 2012/01/22
 * @author XBUP Project (http://xbup.org)
 */
public class XBRXDescService extends XBRDefaultService<XBRXDesc> implements XBCXDescService<XBRXDesc> {

    public XBRXDescService(XBRCatalog catalog) {
        super(catalog);
        itemManager = new XBRXDescManager(catalog);
        catalog.addCatalogManager(XBCXDescManager.class, itemManager);
    }

    @Override
    public XBRXDesc getItemDesc(XBCItem item) {
        return ((XBRXDescManager)itemManager).getItemDesc(item);
    }

    @Override
    public XBRXDesc getItemDesc(XBCItem item, XBCXLanguage language) {
        return ((XBRXDescManager)itemManager).getItemDesc(item, language);
    }

    @Override
    public List<XBCXDesc> getItemDescs(XBCItem item) {
        return ((XBRXDescManager)itemManager).getItemDescs(item);
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
    public String getItemDescText(XBCItem item) {
        XBRXDesc desc = getItemDesc(item);
        if (desc == null) {
            return null;
        }
        return desc.getText();
    }

    @Override
    public void setItemDescText(XBCItem item, String text) {
        XBRXDesc desc = getItemDesc(item);
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
