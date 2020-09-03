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
package org.exbin.xbup.client.catalog.remote;

import java.util.Optional;
import javax.annotation.Nonnull;
import org.exbin.xbup.client.XBCatalogServiceClient;
import org.exbin.xbup.core.catalog.base.XBCItem;
import org.exbin.xbup.core.catalog.base.XBCLimitSpec;
import org.exbin.xbup.core.catalog.base.XBCNode;

/**
 *
 * @version 0.1.22 2013/07/28
 * @author ExBin Project (http://exbin.org)
 */
public class XBRLimitSpec implements XBCLimitSpec {

    private long id;
    protected XBCatalogServiceClient client;

    public XBRLimitSpec(XBCatalogServiceClient client, long id) {
        this.id = id;
        this.client = client;
    }

    @Override
    public long getId() {
        return id;
    }

    @Nonnull
    @Override
    public XBCNode getParent() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Nonnull
    @Override
    public Optional<XBCItem> getParentItem() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public long getXBIndex() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
