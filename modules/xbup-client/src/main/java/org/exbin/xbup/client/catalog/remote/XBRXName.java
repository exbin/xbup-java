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
import org.exbin.xbup.client.stub.XBPXNameStub;
import org.exbin.xbup.core.catalog.base.XBCItem;
import org.exbin.xbup.core.catalog.base.XBCXName;

/**
 * Catalog remote item name entity.
 *
 * @version 0.1.25 2015/02/21
 * @author ExBin Project (http://exbin.org)
 */
public class XBRXName implements XBCXName {

    private final long id;
    protected XBCatalogServiceClient client;
    private final XBPXNameStub nameStub;

    public XBRXName(XBCatalogServiceClient client, long id) {
        this.id = id;
        this.client = client;
        nameStub = new XBPXNameStub(client);
    }

    @Override
    public XBCItem getItem() {
        return nameStub.getNameItem(id);
    }

    @Override
    public String getText() {
        return nameStub.getText(id);
    }

    @Override
    public XBRXLanguage getLang() {
        return nameStub.getLang(id);
    }

    @Override
    public long getId() {
        return id;
    }
}
