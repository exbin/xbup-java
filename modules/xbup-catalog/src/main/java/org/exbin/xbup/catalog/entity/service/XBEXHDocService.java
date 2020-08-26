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
package org.exbin.xbup.catalog.entity.service;

import java.io.Serializable;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.PostConstruct;
import org.exbin.xbup.catalog.XBECatalog;
import org.exbin.xbup.catalog.entity.XBEXFile;
import org.exbin.xbup.catalog.entity.XBEXHDoc;
import org.exbin.xbup.catalog.entity.manager.XBEXHDocManager;
import org.exbin.xbup.core.catalog.base.XBCExtension;
import org.exbin.xbup.core.catalog.base.XBCItem;
import org.exbin.xbup.core.catalog.base.XBCXHDoc;
import org.exbin.xbup.core.catalog.base.manager.XBCXHDocManager;
import org.exbin.xbup.core.catalog.base.service.XBCXFileService;
import org.exbin.xbup.core.catalog.base.service.XBCXHDocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Interface for XBEXHDoc items service.
 *
 * @version 0.2.1 2020/08/26
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
@Service
public class XBEXHDocService extends XBEDefaultService<XBCXHDoc> implements XBCXHDocService, Serializable {

    @Autowired
    private XBEXHDocManager manager;

    public XBEXHDocService() {
        super();
    }

    public XBEXHDocService(XBECatalog catalog) {
        super(catalog);
        itemManager = new XBEXHDocManager(catalog);
        catalog.addCatalogManager(XBCXHDocManager.class, (XBCXHDocManager) itemManager);
    }

    @PostConstruct
    public void init() {
        itemManager = manager;
    }

    public XBEXHDoc findById(Long id) {
        return ((XBEXHDocManager) itemManager).findById(id);
    }

    public Long getAllHDocsCount() {
        return ((XBEXHDocManager) itemManager).getAllHDocsCount();
    }

    @Override
    public XBEXHDoc getDocumentation(XBCItem item) {
        return ((XBEXHDocManager) itemManager).getDocumentation(item);
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
        XBEXHDoc hdoc = getDocumentation(item);
        if (hdoc == null) {
            return null;
        }
        XBEXFile file = hdoc.getDocFile();
        if (file == null) {
            return null;
        }
        XBCXFileService fileService = catalog.getCatalogService(XBCXFileService.class);
        try {
            return new java.util.Scanner(fileService.getFile(file)).useDelimiter("\\A").next();
        } catch (java.util.NoSuchElementException ex) {
            return "";
        }
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
