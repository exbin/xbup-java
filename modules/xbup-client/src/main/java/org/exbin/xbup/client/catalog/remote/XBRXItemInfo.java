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

import java.sql.Time;
import org.exbin.xbup.client.XBCatalogServiceClient;
import org.exbin.xbup.core.catalog.base.XBCItem;
import org.exbin.xbup.core.catalog.base.XBCXItemInfo;
import org.exbin.xbup.core.catalog.base.XBCXUser;

/**
 * Remote catalog item info.
 *
 * @version 0.1.25 2015/03/20
 * @author ExBin Project (http://exbin.org)
 */
public class XBRXItemInfo implements XBCXItemInfo {

    private final long id;
    protected XBCatalogServiceClient client;

    public XBRXItemInfo(XBCatalogServiceClient client, long id) {
        this.id = id;
        this.client = client;
    }

    @Override
    public XBCItem getItem() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public XBCXUser getOwner() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public XBCXUser getCreatedByUser() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Time getCreationDate() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Long getId() {
        return id;
    }
}
