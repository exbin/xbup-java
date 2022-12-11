/*
 * Copyright (C) ExBin Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.exbin.xbup.client.catalog.remote.manager;

import java.util.Date;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.client.catalog.XBRCatalog;
import org.exbin.xbup.client.stub.XBPRootStub;
import org.exbin.xbup.core.catalog.base.XBCRoot;
import org.exbin.xbup.core.catalog.base.manager.XBCRootManager;

/**
 * Remote manager class for XBRRoot catalog items.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XBRRootManager extends XBRDefaultManager<XBCRoot> implements XBCRootManager {

    private final XBPRootStub rootStub;

    public XBRRootManager(XBRCatalog catalog) {
        super(catalog);
        rootStub = new XBPRootStub(client);
        setManagerStub(rootStub);
    }

    @Nonnull
    @Override
    public XBCRoot getMainRoot() {
        XBCRoot mainRoot = rootStub.getMainRoot();
        if (mainRoot == null) {
            throw new IllegalStateException("Missing main root");
        }
        return mainRoot;
    }

    @Override
    public Optional<Date> getMainLastUpdate() {
        return rootStub.getMainLastUpdate();
    }

    @Override
    public boolean isMainPresent() {
        XBCRoot mainRoot = rootStub.getMainRoot();
        return mainRoot != null;
    }

    @Nonnull
    @Override
    public XBCRoot createEmptyRoot(String catalogUrl) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void removeFully(XBCRoot root) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setMainLastUpdateToNow() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setMainLastUpdate(Date updateDate) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
