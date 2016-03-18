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
import org.xbup.lib.client.catalog.remote.XBRRev;
import org.xbup.lib.client.stub.XBPRevStub;
import org.xbup.lib.core.catalog.base.XBCRev;
import org.xbup.lib.core.catalog.base.XBCSpec;
import org.xbup.lib.core.catalog.base.manager.XBCRevManager;

/**
 * Remote manager class for XBRRev catalog items.
 *
 * @version 0.1.25 2015/02/21
 * @author ExBin Project (http://exbin.org)
 */
public class XBRRevManager extends XBRDefaultManager<XBRRev> implements XBCRevManager<XBRRev> {

    private final XBPRevStub revStub;

    public XBRRevManager(XBRCatalog catalog) {
        super(catalog);
        revStub = new XBPRevStub(client);
        setManagerStub(revStub);
    }

    @Override
    public XBRRev findRevByXB(XBCSpec spec, long xbIndex) {
        return revStub.findRevByXB(spec, xbIndex);
    }

    @Override
    public XBRRev getRev(XBCSpec spec, long index) {
        return revStub.getRev(spec, index);
    }

    @Override
    public List<XBCRev> getRevs(XBCSpec spec) {
        return revStub.getRevs(spec);
    }

    @Override
    public long getRevsCount(XBCSpec spec) {
        return revStub.getRevsCount(spec);
    }

    @Override
    public long getRevsLimitSum(XBCSpec spec, long revision) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Long findMaxRevXB(XBCSpec spec) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
