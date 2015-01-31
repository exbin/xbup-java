/*
 * Copyright (C) XBUP Project
 *
 * This application or library is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * This application or library is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along this application.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.xbup.lib.catalog.entity.service;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xbup.lib.core.catalog.base.XBCItem;
import org.xbup.lib.core.catalog.base.manager.XBCXHDocManager;
import org.xbup.lib.core.catalog.base.service.XBCXFileService;
import org.xbup.lib.core.catalog.base.service.XBCXHDocService;
import org.xbup.lib.core.catalog.base.XBCExtension;
import org.xbup.lib.catalog.XBECatalog;
import org.xbup.lib.catalog.entity.XBEXFile;
import org.xbup.lib.catalog.entity.XBEXHDoc;
import org.xbup.lib.catalog.entity.manager.XBEXHDocManager;

/**
 * Interface for XBEXHDoc items service.
 *
 * @version 0.1.24 2015/01/31
 * @author XBUP Project (http://xbup.org)
 */
@Service
public class XBEXHDocService extends XBEDefaultService<XBEXHDoc> implements XBCXHDocService<XBEXHDoc>, Serializable {

    @Autowired
    private XBEXHDocManager manager;

    public XBEXHDocService() {
        super();
    }

    public XBEXHDocService(XBECatalog catalog) {
        super(catalog);
        itemManager = new XBEXHDocManager(catalog);
        catalog.addCatalogManager(XBCXHDocManager.class, itemManager);
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
        XBCXFileService fileService = (XBCXFileService) catalog.getCatalogService(XBCXFileService.class);
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
