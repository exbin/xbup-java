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

/**
 * Catalog remote block specification revision entity.
 *
 * @version 0.1.17 2009/06/16
 * @author ExBin Project (http://exbin.org)
 */
public class XBRBlockRev extends XBRRev implements XBCBlockRev {

    public XBRBlockRev(XBCatalogServiceClient client, long id) {
        super(client,id);
    }

    @Override
    public XBRBlockSpec getParent() {
        return new XBRBlockSpec(client, super.getParent().getId());
    }
}
