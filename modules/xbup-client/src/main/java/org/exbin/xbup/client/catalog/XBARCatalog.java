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
package org.exbin.xbup.client.catalog;

import org.exbin.xbup.client.XBCatalogServiceClient;
import org.exbin.xbup.client.catalog.remote.service.XBRLimiService;
import org.exbin.xbup.client.catalog.remote.service.XBRTranService;
import org.exbin.xbup.core.catalog.XBACatalog;
import org.exbin.xbup.core.catalog.base.service.XBCLimiService;
import org.exbin.xbup.core.catalog.base.service.XBCTranService;

/**
 * XBUP level 2 remote catalog.
 *
 * @version 0.1.21 2012/01/01
 * @author ExBin Project (http://exbin.org)
 */
public class XBARCatalog extends XBRCatalog implements XBACatalog {

    /**
     * Creates a new instance of XBARCatalog.
     *
     * @param client connection client
     */
    public XBARCatalog(XBCatalogServiceClient client) {
        super(client);
        catalogServices.put(XBCLimiService.class, new XBRLimiService(this));
        catalogServices.put(XBCTranService.class, new XBRTranService(this));
    }
}
