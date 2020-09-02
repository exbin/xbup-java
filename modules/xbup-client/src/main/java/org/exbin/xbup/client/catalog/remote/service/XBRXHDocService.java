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
package org.exbin.xbup.client.catalog.remote.service;

import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.client.catalog.XBRCatalog;
import org.exbin.xbup.client.catalog.remote.XBRXHDoc;
import org.exbin.xbup.client.catalog.remote.manager.XBRXHDocManager;
import org.exbin.xbup.core.catalog.base.XBCExtension;
import org.exbin.xbup.core.catalog.base.XBCItem;
import org.exbin.xbup.core.catalog.base.XBCXFile;
import org.exbin.xbup.core.catalog.base.XBCXHDoc;
import org.exbin.xbup.core.catalog.base.manager.XBCXHDocManager;
import org.exbin.xbup.core.catalog.base.service.XBCXFileService;
import org.exbin.xbup.core.catalog.base.service.XBCXHDocService;

/**
 * Remote service for XBRXHDoc items.
 *
 * @version 0.2.1 2020/08/26
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XBRXHDocService extends XBRDefaultService<XBCXHDoc> implements XBCXHDocService {

    public XBRXHDocService(XBRCatalog catalog) {
        super(catalog);
        itemManager = new XBRXHDocManager(catalog);
        catalog.addCatalogManager(XBCXHDocManager.class, (XBCXHDocManager) itemManager);
    }

    public XBRXHDoc findById(Long id) {
        return ((XBRXHDocManager) itemManager).findById(id);
    }

    public Long getAllHDocsCount() {
        return ((XBRXHDocManager) itemManager).getAllHDocsCount();
    }

    @Override
    public XBRXHDoc getDocumentation(XBCItem item) {
        return ((XBRXHDocManager) itemManager).getDocumentation(item);
    }

    @Override
    public String getExtensionName() {
        return ((XBCExtension) itemManager).getExtensionName();
    }

    @Override
    public void initializeExtension() {
        ((XBCExtension) itemManager).initializeExtension();
    }

    @Override
    public String getDocumentationText(XBCItem item) {
        XBRXHDoc hdoc = getDocumentation(item);
        if (hdoc == null) {
            return null;
        }
        XBCXFile file = hdoc.getDocFile();
        if (file == null) {
            return null;
        }
        XBCXFileService fileService = catalog.getCatalogService(XBCXFileService.class);
        return new java.util.Scanner(fileService.getFile(file)).useDelimiter("\\A").next();
    }

    @Override
    public String getDocumentationBodyText(XBCItem item) {
        String text = getDocumentationText(item);
        if (text == null || text.isEmpty()) {
            return text;
        }

        int bodyPos = text.indexOf("<body");
        int startPos = text.indexOf(">", bodyPos + 5);

        int endPos = text.lastIndexOf("</body>");

        return text.substring(startPos + 1, endPos);
    }
}
