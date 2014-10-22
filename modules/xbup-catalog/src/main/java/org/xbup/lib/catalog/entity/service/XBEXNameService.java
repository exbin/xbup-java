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
package org.xbup.lib.catalog.entity.service;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xbup.lib.core.catalog.base.XBCBlockSpec;
import org.xbup.lib.core.catalog.base.XBCItem;
import org.xbup.lib.core.catalog.base.XBCXLanguage;
import org.xbup.lib.core.catalog.base.XBCXName;
import org.xbup.lib.core.catalog.base.manager.XBCXLangManager;
import org.xbup.lib.core.catalog.base.manager.XBCXNameManager;
import org.xbup.lib.core.catalog.base.service.XBCXNameService;
import org.xbup.lib.core.catalog.base.XBCExtension;
import org.xbup.lib.catalog.XBECatalog;
import org.xbup.lib.catalog.entity.XBEXName;
import org.xbup.lib.catalog.entity.manager.XBEXLangManager;
import org.xbup.lib.catalog.entity.manager.XBEXNameManager;

/**
 * Interface for XBEXName items service.
 *
 * @version 0.1.21 2012/01/22
 * @author XBUP Project (http://xbup.org)
 */
@Service
public class XBEXNameService extends XBEDefaultService<XBEXName> implements XBCXNameService<XBEXName>, Serializable {

    @Autowired
    private XBEXNameManager manager;

    public XBEXNameService() {
        super();
    }

    public XBEXNameService(XBECatalog catalog) {
        super(catalog);
        itemManager = new XBEXNameManager(catalog);
        catalog.addCatalogManager(XBCXNameManager.class, itemManager);
    }

    @PostConstruct
    public void init() {
        itemManager = manager;
    }

    @Override
    public String getDefaultCaption(XBCBlockSpec blockSpec) {
        return ((XBEXNameManager)itemManager).getDefaultCaption(blockSpec);
    }

    @Override
    public XBEXName getItemName(XBCItem item) {
        return ((XBEXNameManager)itemManager).getItemName(item);
    }

    @Override
    public XBEXName getItemName(XBCItem item, XBCXLanguage language) {
        return ((XBEXNameManager)itemManager).getItemName(item, language);
    }

    @Override
    public List<XBCXName> getItemNames(XBCItem item) {
        return ((XBEXNameManager)itemManager).getItemNames(item);
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
    public String getItemNameText(XBCItem item) {
        XBEXName name = getItemName(item);
        if (name == null) {
            return null;
        }
        return name.getText();
    }

    @Override
    public void setItemNameText(XBCItem item, String text) {
        XBEXName name = getItemName(item);
        if (name == null) {
            XBEXLangManager langManager = ((XBEXLangManager) catalog.getCatalogManager(XBCXLangManager.class));
            name = createItem();
            name.setItem(item);
            name.setLang(langManager.getDefaultLang());
        }

        name.setText(text);
        persistItem(name);
    }
}