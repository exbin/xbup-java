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
package org.exbin.xbup.client.catalog.remote.manager;

import java.io.InputStream;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.client.catalog.XBRCatalog;
import org.exbin.xbup.client.catalog.remote.XBRXHDoc;
import org.exbin.xbup.client.stub.XBPXHDocStub;
import org.exbin.xbup.core.catalog.base.XBCItem;
import org.exbin.xbup.core.catalog.base.XBCXFile;
import org.exbin.xbup.core.catalog.base.XBCXHDoc;
import org.exbin.xbup.core.catalog.base.manager.XBCXFileManager;
import org.exbin.xbup.core.catalog.base.manager.XBCXHDocManager;

/**
 * Remote manager class for XBRXHDoc catalog items.
 *
 * @version 0.2.1 2020/08/26
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XBRXHDocManager extends XBRDefaultManager<XBCXHDoc> implements XBCXHDocManager {

    private XBCXFileManager fileManager = null;
    private final XBPXHDocStub hdocStub;

    public XBRXHDocManager(XBRCatalog catalog) {
        super(catalog);
        hdocStub = new XBPXHDocStub(client);
        setManagerStub(hdocStub);
    }

    @Override
    public void initializeExtension() {
    }

    @Override
    public XBRXHDoc findById(Long id) {
        return new XBRXHDoc(client, id);
    }

    @Override
    public String getExtensionName() {
        return "HDoc Extension";
    }

    public InputStream getDocumentData(XBCItem item) {
        XBCXHDoc doc = getDocumentation(item);
        if (doc == null) {
            return null;
        }
        XBCXFile file = doc.getDocFile();
        if (file == null) {
            return null;
        }
        // TODO: This is ugly copy, catalog instance should be passed?
        if (fileManager == null) {
            fileManager = new XBRXFileManager(catalog);
        }
        return fileManager.getFile(file);
    }

    @Override
    public XBRXHDoc getDocumentation(XBCItem item) {
        return hdocStub.getItemHDoc(item.getId());
    }

    @Override
    public Long getAllHDocsCount() {
        return hdocStub.getItemsCount();
    }
}
