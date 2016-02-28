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
import org.xbup.lib.core.catalog.base.XBCItem;
import org.xbup.lib.core.catalog.base.XBCXLanguage;
import org.xbup.lib.core.catalog.base.XBCXName;
import org.xbup.lib.core.catalog.base.manager.XBCXNameManager;
import org.xbup.lib.client.catalog.remote.XBRXName;
import org.xbup.lib.client.stub.XBPXNameStub;

/**
 * Remote manager class for XBRXName catalog items.
 *
 * @version 0.1.25 2015/02/21
 * @author ExBin Project (http://exbin.org)
 */
public class XBRXNameManager extends XBRDefaultManager<XBRXName> implements XBCXNameManager<XBRXName> {

    private final XBPXNameStub nameStub;

    public XBRXNameManager(XBRCatalog catalog) {
        super(catalog);
        nameStub = new XBPXNameStub(client);
        setManagerStub(nameStub);
    }

    @Override
    public XBRXName getDefaultItemName(XBCItem item) {
        return nameStub.getDefaultItemName(item);
    }

    @Override
    public XBRXName getItemName(XBCItem item, XBCXLanguage language) {
        return nameStub.getItemName(item, language);
    }

    @Override
    public List<XBCXName> getItemNames(XBCItem item) {
        return nameStub.getItemNames(item);
    }

    @Override
    public void initializeExtension() {
    }

    @Override
    public String getExtensionName() {
        return "Name Extension";
    }

    @Override
    public String getDefaultText(XBCItem item) {
        XBCXName name = getDefaultItemName(item);
        if (name == null) {
            return null;
        }

        return name.getText();
    }
}
