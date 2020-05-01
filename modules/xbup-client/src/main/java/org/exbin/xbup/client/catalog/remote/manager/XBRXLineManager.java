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

import java.util.ArrayList;
import java.util.List;
import org.exbin.xbup.client.catalog.XBRCatalog;
import org.exbin.xbup.client.catalog.remote.XBRXBlockLine;
import org.exbin.xbup.client.catalog.remote.XBRXPlugLine;
import org.exbin.xbup.client.stub.XBPXLineStub;
import org.exbin.xbup.core.catalog.base.XBCBlockRev;
import org.exbin.xbup.core.catalog.base.XBCXBlockLine;
import org.exbin.xbup.core.catalog.base.XBCXPlugLine;
import org.exbin.xbup.core.catalog.base.XBCXPlugin;
import org.exbin.xbup.core.catalog.base.manager.XBCXLineManager;

/**
 * Remote manager class for XBRXBlockLine catalog items.
 *
 * @version 0.2.1 2020/04/18
 * @author ExBin Project (http://exbin.org)
 */
public class XBRXLineManager extends XBRDefaultManager<XBRXBlockLine> implements XBCXLineManager<XBRXBlockLine> {

    private final XBPXLineStub lineStub;

    public XBRXLineManager(XBRCatalog catalog) {
        super(catalog);
        lineStub = new XBPXLineStub(client);
        setManagerStub(lineStub);
    }

    @Override
    public void initializeExtension() {
    }

    @Override
    public String getExtensionName() {
        return "Block Line Extension";
    }

    @Override
    public List<XBCXBlockLine> getLines(XBCBlockRev rev) {
        List<XBCXBlockLine> result = new ArrayList<>();
        long count = getLinesCount(rev);
        for (int i = 0; i < count; i++) {
            result.add(findLineByPR(rev, i));
        }
        return result;
    }

    @Override
    public long getLinesCount(XBCBlockRev rev) {
        return lineStub.getBlockLineCount(rev);
    }

    @Override
    public XBRXBlockLine findLineByPR(XBCBlockRev rev, long priority) {
        return lineStub.findLineByPR(rev, priority);
    }

    @Override
    public List<XBCXPlugLine> getPlugLines(XBCXPlugin plugin) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public long getPlugLinesCount(XBCXPlugin plugin) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public XBRXPlugLine getPlugLine(XBCXPlugin plugin, long lineIndex) {
        return lineStub.getPlugLine(plugin, lineIndex);
    }

    @Override
    public Long getAllPlugLinesCount() {
        return lineStub.getAllPlugLinesCount();
    }

    @Override
    public XBRXBlockLine findById(long id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public XBRXPlugLine findPlugLineById(long id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
