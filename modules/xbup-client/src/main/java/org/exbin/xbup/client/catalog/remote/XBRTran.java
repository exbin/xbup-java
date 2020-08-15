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

import org.exbin.xbup.client.XBCatalogServiceClient;
import org.exbin.xbup.core.catalog.base.XBCBlockRev;
import org.exbin.xbup.core.catalog.base.XBCLimitSpec;
import org.exbin.xbup.core.catalog.base.XBCTran;

/**
 * Catalog remote transaction entity.
 *
 * @version 0.1.18 2009/12/23
 * @author ExBin Project (http://exbin.org)
 */
public class XBRTran implements XBCTran {

    private final long id;
    protected XBCatalogServiceClient client;

    public XBRTran(XBCatalogServiceClient client, long id) {
        this.id = id;
        this.client = client;
    }

    @Override
    public XBRBlockSpec getOwner() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public XBCBlockRev getTarget() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public XBCLimitSpec getLimit() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public XBCBlockRev getExcept() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
