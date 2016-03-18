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
package org.xbup.lib.client.catalog.remote.manager;

import java.util.List;
import org.xbup.lib.client.catalog.XBRCatalog;
import org.xbup.lib.client.catalog.remote.XBRXLanguage;
import org.xbup.lib.client.stub.XBPXLangStub;
import org.xbup.lib.core.catalog.base.XBCXLanguage;
import org.xbup.lib.core.catalog.base.manager.XBCXLangManager;

/**
 * Remote manager class for XBRXLanguage catalog items.
 *
 * @version 0.1.25 2015/02/21
 * @author ExBin Project (http://exbin.org)
 */
public class XBRXLangManager extends XBRDefaultManager<XBRXLanguage> implements XBCXLangManager<XBRXLanguage> {

    private final XBPXLangStub langStub;

    public XBRXLangManager(XBRCatalog catalog) {
        super(catalog);
        langStub = new XBPXLangStub(client);
        setManagerStub(langStub);
    }

    @Override
    public XBRXLanguage getDefaultLang() {
        return langStub.getDefaultLang();
    }

    public List<XBCXLanguage> getLangs() {
        return langStub.getLangs();
    }

    @Override
    public void initializeExtension() {
    }

    @Override
    public String getExtensionName() {
        return "Language Extension";
    }
}
