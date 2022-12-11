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

import org.exbin.xbup.client.XBCatalogServiceClient;
import org.exbin.xbup.client.stub.XBPXHDocStub;
import org.exbin.xbup.core.catalog.base.XBCXFile;
import org.exbin.xbup.core.catalog.base.XBCXHDoc;
import org.exbin.xbup.core.catalog.base.XBCXLanguage;

/**
 * Catalog remote item HTML documentation entity.
 *
 * @author ExBin Project (https://exbin.org)
 */
public class XBRXHDoc implements XBCXHDoc {

    private final long id;
    protected XBCatalogServiceClient client;
    private final XBPXHDocStub hdocStub;

    public XBRXHDoc(XBCatalogServiceClient client, long id) {
        this.id = id;
        this.client = client;
        hdocStub = new XBPXHDocStub(client);
    }

    @Override
    public XBRItem getItem() {
        return hdocStub.getHDocItem(id);
    }

    @Override
    public XBCXFile getDocFile() {
        return hdocStub.getDocFile(id);
    }

    @Override
    public XBCXLanguage getLang() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public long getId() {
        return id;
    }
}
