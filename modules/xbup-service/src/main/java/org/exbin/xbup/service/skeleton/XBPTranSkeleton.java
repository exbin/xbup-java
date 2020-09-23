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
package org.exbin.xbup.service.skeleton;

import org.exbin.xbup.catalog.XBAECatalog;
import org.exbin.xbup.core.remote.XBServiceServer;

/**
 * RPC skeleton class for XBRRev catalog items.
 *
 * @version 0.1.25 2015/02/18
 * @author ExBin Project (http://exbin.org)
 */
public class XBPTranSkeleton implements XBPCatalogSkeleton {

    @Override
    public void registerProcedures(XBServiceServer remoteServer) {

    }

    @Override
    public void setCatalog(XBAECatalog catalog) {
    }
}
