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
package org.exbin.xbup.client;

import java.io.IOException;
import javax.annotation.Nullable;
import org.exbin.xbup.core.block.declaration.local.XBLFormatDecl;
import org.exbin.xbup.core.catalog.XBPCatalog;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.serial.XBPSerialReader;

/**
 * XBService catalog client using IP networking.
 *
 * @version 0.2.1 2017/06/06
 * @author ExBin Project (http://exbin.org)
 */
public class XBCatalogNetServiceClient extends XBTCPServiceClient implements XBCatalogServiceClient {

    public XBCatalogNetServiceClient(String host, int port) {
        super(host, port);
        init();
    }

    private void init() {
        XBPCatalog catalog = new XBPCatalog();
        catalog.addFormatDecl(getContextFormatDecl());
        catalog.generateContext();
        setCatalog(catalog);
    }

    /**
     * Returns local format declaration when catalog or service is not
     * available.
     *
     * @return local format declaration
     */
    @Nullable
    private XBLFormatDecl getContextFormatDecl() {
        XBPSerialReader reader = new XBPSerialReader(getClass().getResourceAsStream("/org/exbin/xbup/client/resources/catalog_service_format.xb"));
        XBLFormatDecl formatDecl = new XBLFormatDecl();
        try {
            reader.read(formatDecl);
        } catch (XBProcessingException | IOException ex) {
            return null;
        }
        return formatDecl;
    }
}
