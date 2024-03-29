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

import java.util.Date;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.client.XBCatalogServiceClient;
import org.exbin.xbup.client.stub.XBPRootStub;
import org.exbin.xbup.core.catalog.base.XBCNode;
import org.exbin.xbup.core.catalog.base.XBCRoot;

/**
 * Catalog remote root entity.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XBRRoot implements XBCRoot {

    private final long id;
    protected XBCatalogServiceClient client;
    private final XBPRootStub rootStub;

    public XBRRoot(XBCatalogServiceClient client, long id) {
        this.id = id;
        this.client = client;
        rootStub = new XBPRootStub(client);
    }

    @Nonnull
    @Override
    public XBCNode getNode() {
        return rootStub.getRoot(id).getNode();
    }

    @Nonnull
    @Override
    public Optional<String> getUrl() {
        return rootStub.getRoot(id).getUrl();
    }

    @Nonnull
    @Override
    public Optional<Date> getLastUpdate() {
        return rootStub.getRoot(id).getLastUpdate();
    }

    @Override
    public long getId() {
        return id;
    }
}
