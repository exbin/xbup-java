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
package org.exbin.xbup.service.skeleton;

import org.exbin.xbup.catalog.XBAECatalog;
import org.exbin.xbup.core.remote.XBServiceServer;

/**
 * RPC skeleton class for XBRLimit catalog items.
 *
 * @author ExBin Project (https://exbin.org)
 */
public class XBPLimiSkeleton implements XBPCatalogSkeleton {

    private XBAECatalog catalog;

    public XBPLimiSkeleton(XBAECatalog catalog) {
        this.catalog = catalog;
    }

    @Override
    public void registerProcedures(XBServiceServer remoteServer) {

    }

    @Override
    public void setCatalog(XBAECatalog catalog) {
        this.catalog = catalog;
    }
}
