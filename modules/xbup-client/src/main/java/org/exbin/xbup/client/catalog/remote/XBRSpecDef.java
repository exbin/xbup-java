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
package org.exbin.xbup.client.catalog.remote;

import org.exbin.xbup.client.XBCatalogServiceClient;
import org.exbin.xbup.client.stub.XBPSpecStub;
import org.exbin.xbup.core.block.definition.XBParamType;
import org.exbin.xbup.core.catalog.base.XBCSpecDef;

/**
 * Catalog remote specification definition entity.
 *
 * @version 0.1.25 2015/02/21
 * @author ExBin Project (http://exbin.org)
 */
public class XBRSpecDef extends XBRItem implements XBCSpecDef {

    private final XBPSpecStub specStub;

    public XBRSpecDef(XBCatalogServiceClient client, long id) {
        super(client, id);
        specStub = new XBPSpecStub(client);
    }

    @Override
    public XBRSpec getSpec() {
        XBRItem parent = super.getParent();
        return parent == null ? null : new XBRSpec(client, parent.getId());
    }

    @Override
    public XBRRev getTarget() {
        return specStub.getTarget(getId());
    }

    @Override
    public XBParamType getType() {
        return null;
    }
}
