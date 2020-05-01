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
import org.exbin.xbup.client.catalog.remote.XBRXBlockPane;
import org.exbin.xbup.client.catalog.remote.XBRXPlugPane;
import org.exbin.xbup.client.stub.XBPXPaneStub;
import org.exbin.xbup.core.catalog.base.XBCBlockRev;
import org.exbin.xbup.core.catalog.base.XBCXBlockPane;
import org.exbin.xbup.core.catalog.base.XBCXPlugPane;
import org.exbin.xbup.core.catalog.base.XBCXPlugin;
import org.exbin.xbup.core.catalog.base.manager.XBCXPaneManager;

/**
 * Remote manager class for XBRXBlockPane catalog items.
 *
 * @version 0.1.25 2015/02/21
 * @author ExBin Project (http://exbin.org)
 */
public class XBRXPaneManager extends XBRDefaultManager<XBRXBlockPane> implements XBCXPaneManager<XBRXBlockPane> {

    private final XBPXPaneStub paneStub;

    public XBRXPaneManager(XBRCatalog catalog) {
        super(catalog);
        paneStub = new XBPXPaneStub(client);
        setManagerStub(paneStub);
    }

    @Override
    public void initializeExtension() {
    }

    @Override
    public String getExtensionName() {
        return "Block Panel Extension";
    }

    @Override
    public List<XBCXBlockPane> getPanes(XBCBlockRev rev) {
        List<XBCXBlockPane> result = new ArrayList<>();
        long count = getPanesCount(rev);
        for (int i = 0; i < count; i++) {
            result.add(findPaneByPR(rev, i));
        }
        return result;
    }

    @Override
    public long getPanesCount(XBCBlockRev rev) {
        return paneStub.getBlockPanesCount(rev);
    }

    @Override
    public XBRXBlockPane findPaneByPR(XBCBlockRev rev, long priority) {
        return paneStub.findPaneByPR(rev, priority);
    }

    @Override
    public List<XBCXPlugPane> getPlugPanes(XBCXPlugin plugin) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public long getPlugPanesCount(XBCXPlugin plugin) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public XBRXPlugPane getPlugPane(XBCXPlugin plugin, long paneIndex) {
        return paneStub.getPlugPane(plugin, paneIndex);
    }

    @Override
    public Long getAllPlugPanesCount() {
        return paneStub.getAllPlugPanesCount();
    }

    @Override
    public XBRXBlockPane findById(long id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public XBRXPlugPane findPlugPaneById(long id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
