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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.exbin.xbup.catalog.XBECatalog;
import org.exbin.xbup.catalog.entity.XBEXDesc;
import org.exbin.xbup.catalog.entity.manager.XBEXDescManager;
import org.exbin.xbup.catalog.entity.manager.XBEXLangManager;
import org.exbin.xbup.core.catalog.base.XBCExtension;
import org.exbin.xbup.core.catalog.base.XBCItem;
import org.exbin.xbup.core.catalog.base.XBCXDesc;
import org.exbin.xbup.core.catalog.base.XBCXLanguage;
import org.exbin.xbup.core.catalog.base.manager.XBCXDescManager;
import org.exbin.xbup.core.catalog.base.manager.XBCXLangManager;
import org.exbin.xbup.core.catalog.base.service.XBCXDescService;

/**
 * Interface for XBEXDesc items service.
 *
 * @version 0.1.24 2014/11/17
 * @author ExBin Project (http://exbin.org)
 */
@Service
public class XBEXDescService extends XBEDefaultService<XBEXDesc> implements XBCXDescService<XBEXDesc>, Serializable {

    @Autowired
    private XBEXDescManager manager;

    public XBEXDescService() {
        super();
    }

    public XBEXDescService(XBECatalog catalog) {
        super(catalog);
        itemManager = new XBEXDescManager(catalog);
        catalog.addCatalogManager(XBCXDescManager.class, itemManager);
    }

    @PostConstruct
    public void init() {
        itemManager = manager;
    }

    @Override
    public XBEXDesc getDefaultItemDesc(XBCItem item) {
        return ((XBEXDescManager) itemManager).getDefaultItemDesc(item);
    }

    @Override
    public XBEXDesc getItemDesc(XBCItem item, XBCXLanguage language) {
        return ((XBEXDescManager) itemManager).getItemDesc(item, language);
    }

    @Override
    public List<XBCXDesc> getItemDescs(XBCItem item) {
        return ((XBEXDescManager) itemManager).getItemDescs(item);
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
        return ((XBEXDescManager) itemManager).getDefaultText(item);
    }

    public void setDefaultText(XBCItem item, String text) {
        XBEXDesc desc = getDefaultItemDesc(item);
        if (text == null || text.isEmpty()) {
            if (desc != null) {
                removeItem(desc);
            }
        } else {
            if (desc == null) {
                XBEXLangManager langManager = ((XBEXLangManager) catalog.getCatalogManager(XBCXLangManager.class));
                desc = createItem();
                desc.setItem(item);
                desc.setLang(langManager.getDefaultLang());
            }

            desc.setText(text);
            persistItem(desc);
        }
    }
}
