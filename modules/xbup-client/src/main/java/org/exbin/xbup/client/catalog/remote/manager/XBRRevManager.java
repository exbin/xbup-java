/*
 * Copyright (C) ExBin Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.exbin.xbup.client.catalog.remote.manager;

import java.util.List;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.client.catalog.XBRCatalog;
import org.exbin.xbup.client.catalog.remote.XBRRev;
import org.exbin.xbup.client.stub.XBPRevStub;
import org.exbin.xbup.core.catalog.base.XBCRev;
import org.exbin.xbup.core.catalog.base.XBCSpec;
import org.exbin.xbup.core.catalog.base.manager.XBCRevManager;

/**
 * Remote manager class for XBRRev catalog items.
 *
 * @version 0.2.1 2020/08/26
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XBRRevManager extends XBRDefaultManager<XBCRev> implements XBCRevManager {

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
