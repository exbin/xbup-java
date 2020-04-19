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
package org.exbin.xbup.client.catalog.remote.manager;

import org.exbin.xbup.client.catalog.XBRCatalog;
import org.exbin.xbup.client.catalog.remote.XBRXStri;
import org.exbin.xbup.client.stub.XBPXStriStub;
import org.exbin.xbup.core.catalog.base.XBCItem;
import org.exbin.xbup.core.catalog.base.XBCXStri;
import org.exbin.xbup.core.catalog.base.manager.XBCNodeManager;
import org.exbin.xbup.core.catalog.base.manager.XBCXStriManager;

/**
 * Remote manager class for XBRXStri catalog items.
 *
 * @version 0.1.25 2015/02/21
 * @author ExBin Project (http://exbin.org)
 */
public class XBRXStriManager extends XBRDefaultManager<XBRXStri> implements XBCXStriManager<XBRXStri> {

    private final XBPXStriStub striStub;

    public XBRXStriManager(XBRCatalog catalog) {
        super(catalog);
        striStub = new XBPXStriStub(client);
        setManagerStub(striStub);
    }

    @Override
    public XBRXStri getItemStringId(XBCItem item) {
        return striStub.getItemStringId(item);
    }

    @Override
    public void initializeExtension() {
    }

    @Override
    public String getExtensionName() {
        return "Stri Extension";
    }

    @Override
    public String getItemStringIdText(XBCItem item) {
        XBCXStri Stri = getItemStringId(item);
        if (Stri == null) {
            return null;
        }
        return Stri.getText();
    }

    @Override
    public String getFullPath(XBCXStri itemString) {
        if ("/".equals(itemString.getNodePath())) {
            XBCNodeManager nodeManager = catalog.getCatalogManager(XBCNodeManager.class);
            if (itemString.getItem().getId().equals(nodeManager.getRootNode().getId())) {
                return "";
            }
        }
        return itemString.getNodePath() + "/" + itemString.getText();
    }
}
