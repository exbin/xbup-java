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
package org.exbin.xbup.client.catalog.remote;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.client.XBCatalogServiceClient;
import org.exbin.xbup.client.stub.XBPXUiStub;
import org.exbin.xbup.core.catalog.base.XBCBlockRev;
import org.exbin.xbup.core.catalog.base.XBCXBlockUi;
import org.exbin.xbup.core.catalog.base.XBCXPlugUi;

/**
 * Catalog remote block UI editor entity.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XBRXBlockUi implements XBCXBlockUi {

    private final long id;
    protected XBCatalogServiceClient client;
    private final XBPXUiStub uiStub;

    public XBRXBlockUi(XBCatalogServiceClient client, long id) {
        this.id = id;
        this.client = client;
        uiStub = new XBPXUiStub(client);
    }

    @Override
    public XBCBlockRev getBlockRev() {
        return uiStub.getBlockRev(id);
    }

    @Override
    public XBCXPlugUi getUi() {
        return uiStub.getUi(id);
    }

    @Override
    public long getPriority() {
        return uiStub.getPriority(id);
    }

    @Override
    public long getId() {
        return id;
    }

    @Nonnull
    @Override
    public String getName() {
        return uiStub.getName(id);
    }
}
