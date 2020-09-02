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
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.client.catalog.XBRCatalog;
import org.exbin.xbup.client.catalog.remote.XBRXBlockUi;
import org.exbin.xbup.client.catalog.remote.XBRXPlugUi;
import org.exbin.xbup.client.stub.XBPXUiStub;
import org.exbin.xbup.core.catalog.XBPlugUiType;
import org.exbin.xbup.core.catalog.base.XBCBlockRev;
import org.exbin.xbup.core.catalog.base.XBCXBlockUi;
import org.exbin.xbup.core.catalog.base.XBCXPlugUi;
import org.exbin.xbup.core.catalog.base.XBCXPlugUiType;
import org.exbin.xbup.core.catalog.base.XBCXPlugin;
import org.exbin.xbup.core.catalog.base.manager.XBCXUiManager;

/**
 * Remote manager class for XBRXBlockUi catalog items.
 *
 * @version 0.2.1 2020/08/26
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XBRXUiManager extends XBRDefaultManager<XBCXBlockUi> implements XBCXUiManager {

    private final XBPXUiStub uiStub;

    public XBRXUiManager(XBRCatalog catalog) {
        super(catalog);
        uiStub = new XBPXUiStub(client);
        setManagerStub(uiStub);
    }

    @Override
    public void initializeExtension() {
    }

    @Override
    public String getExtensionName() {
        return "Block UI Extension";
    }

    @Nonnull
    @Override
    public List<XBCXBlockUi> getUis(XBCBlockRev revision) {
        throw new UnsupportedOperationException("Not supported yet.");
//        List<XBCXBlockUi> result = new ArrayList<>();
//        long count = getUisCount(revision);
//        for (int i = 0; i < count; i++) {
//            XBRXBlockUi blockUi = findUiByPR(revision, i);
//            result.add(blockUi);
//        }
//        return result;
    }

    @Nonnull
    @Override
    public List<XBCXBlockUi> getUis(XBCBlockRev revision, XBPlugUiType type) {
        List<XBCXBlockUi> result = new ArrayList<>();
        long count = getUisCount(revision, type);
        for (int i = 0; i < count; i++) {
            XBRXBlockUi blockUi = findUiByPR(revision, type, i);
            result.add(blockUi);
        }
        return result;
    }

    @Override
    public long getUisCount(XBCBlockRev rev) {
        return uiStub.getBlockUiCount(rev);
    }

    @Override
    public long getUisCount(XBCBlockRev rev, XBPlugUiType type) {
        throw new UnsupportedOperationException("Not supported yet.");
        //return uiStub.getBlockUiCount(rev, type);
    }

    @Override
    public XBRXBlockUi findUiByPR(XBCBlockRev rev, XBPlugUiType type, long priority) {
        return uiStub.findUiByPR(rev, type, priority);
    }

    @Override
    public XBCXPlugUiType findTypeById(long index) {
        throw new UnsupportedOperationException("Not supported yet.");
//        XBPlugUiType type = XBPlugUiType.findByDbIndex((int) index);
//        return new XBRXPlugUiType(client, type.getDbIndex());
    }

    @Nonnull
    @Override
    public List<XBCXPlugUi> getPlugUis(XBCXPlugin plugin) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Nonnull
    @Override
    public List<XBCXPlugUi> getPlugUis(XBCXPlugin plugin, XBPlugUiType type) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public long getPlugUisCount(XBCXPlugin plugin) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public long getPlugUisCount(XBCXPlugin plugin, XBPlugUiType type) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public XBRXPlugUi getPlugUi(XBCXPlugin plugin, XBPlugUiType type, long methodIndex) {
        return (XBRXPlugUi) uiStub.getPlugUi(plugin, type, methodIndex);
    }

    @Override
    public long getAllPlugUisCount() {
        return uiStub.getAllPlugUisCount();
    }

    @Override
    public XBRXBlockUi findById(long id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public XBRXPlugUi findPlugUiById(long id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
