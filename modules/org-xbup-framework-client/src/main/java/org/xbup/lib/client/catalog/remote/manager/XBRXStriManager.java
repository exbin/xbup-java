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
package org.xbup.lib.client.catalog.remote.manager;

import org.xbup.lib.client.catalog.XBRCatalog;
import org.xbup.lib.core.catalog.base.XBCItem;
import org.xbup.lib.core.catalog.base.XBCXStri;
import org.xbup.lib.core.catalog.base.manager.XBCNodeManager;
import org.xbup.lib.core.catalog.base.manager.XBCXStriManager;
import org.xbup.lib.client.catalog.remote.XBRXStri;
import org.xbup.lib.client.stub.XBPXStriStub;

/**
 * Remote manager class for XBRXStri catalog items.
 *
 * @version 0.1.25 2015/02/21
 * @author XBUP Project (http://xbup.org)
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
            XBCNodeManager nodeManager = (XBCNodeManager) catalog.getCatalogManager(XBCNodeManager.class);
            if (itemString.getItem().getId().equals(nodeManager.getRootNode().getId())) {
                return "";
            }
        }
        return itemString.getNodePath() + "/" + itemString.getText();
    }
}
