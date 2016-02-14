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

import org.xbup.lib.core.catalog.base.XBCFormatCons;
import org.xbup.lib.client.XBCatalogServiceClient;
import org.xbup.lib.core.block.definition.XBParamType;

/**
 * Catalog remote format consist specification definition entity.
 *
 * @version 0.1.25 2015/02/21
 * @author XBUP Project (http://xbup.org)
 */
public class XBRFormatCons extends XBRConsDef implements XBCFormatCons {

    public XBRFormatCons(XBCatalogServiceClient client, long id) {
        super(client, id);
    }

    @Override
    public XBRGroupRev getTarget() {
        XBRRev item = super.getTarget();
        if (item == null) {
            return null;
        }
        return new XBRGroupRev(item.client, item.getId());
    }

    @Override
    public XBRFormatSpec getSpec() {
        XBRSpec item = super.getSpec();
        if (item == null) {
            return null;
        }

        return new XBRFormatSpec(item.client, item.getId());
    }

    @Override
    public XBParamType getType() {
        return XBParamType.CONSIST;
    }
}
