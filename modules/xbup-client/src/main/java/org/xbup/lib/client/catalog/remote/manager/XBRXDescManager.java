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
import org.xbup.lib.core.catalog.base.XBCXDesc;
import org.xbup.lib.core.catalog.base.XBCXLanguage;
import org.xbup.lib.core.catalog.base.manager.XBCXDescManager;
import org.xbup.lib.client.catalog.remote.XBRXDesc;
import org.xbup.lib.client.stub.XBPXDescStub;

/**
 * Remote manager class for XBRXDesc catalog items.
 *
 * @version 0.1.25 2015/02/21
 * @author ExBin Project (http://exbin.org)
 */
public class XBRXDescManager extends XBRDefaultManager<XBRXDesc> implements XBCXDescManager<XBRXDesc> {

    private final XBPXDescStub descStub;

    public XBRXDescManager(XBRCatalog catalog) {
        super(catalog);
        descStub = new XBPXDescStub(client);
        setManagerStub(descStub);
    }

    @Override
    public XBRXDesc getDefaultItemDesc(XBCItem item) {
        return descStub.getDefaultItemDesc(item);
    }

    @Override
    public XBRXDesc getItemDesc(XBCItem item, XBCXLanguage language) {
        return descStub.getItemDesc(item, language);
    }

    @Override
    public List<XBCXDesc> getItemDescs(XBCItem item) {
        return descStub.getItemDescs(item);
    }

    @Override
    public void initializeExtension() {
    }

    @Override
    public String getExtensionName() {
        return "Description Extension";
    }

}
