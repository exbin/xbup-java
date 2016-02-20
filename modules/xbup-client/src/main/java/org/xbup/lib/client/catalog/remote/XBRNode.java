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
package org.xbup.lib.client.catalog.remote;

import org.xbup.lib.core.catalog.base.XBCNode;
import org.xbup.lib.client.XBCatalogServiceClient;

/**
 * Catalog remote node entity.
 *
 * @version 0.1.25 2015/02/21
 * @author XBUP Project (http://xbup.org)
 */
public class XBRNode extends XBRItem implements XBCNode {

    public XBRNode(XBCatalogServiceClient client, long id) {
        super(client, id);
    }

    @Override
    public XBRNode getParent() {
        XBRItem item = super.getParent();
        if (item == null) {
            return null;
        }
        return new XBRNode(item.client, item.getId());
    }
}