/*
 * Copyright (C) ExBin Project
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
package org.exbin.xbup.client.catalog.remote.service;

import org.exbin.xbup.client.catalog.XBRCatalog;
import org.exbin.xbup.client.catalog.remote.XBRXHDoc;
import org.exbin.xbup.client.catalog.remote.manager.XBRXHDocManager;
import org.exbin.xbup.core.catalog.base.XBCExtension;
import org.exbin.xbup.core.catalog.base.XBCItem;
import org.exbin.xbup.core.catalog.base.XBCXFile;
import org.exbin.xbup.core.catalog.base.manager.XBCXHDocManager;
import org.exbin.xbup.core.catalog.base.service.XBCXFileService;
import org.exbin.xbup.core.catalog.base.service.XBCXHDocService;

/**
 * Remote service for XBRXHDoc items.
 *
 * @version 0.1.25 2015/03/19
 * @author ExBin Project (http://exbin.org)
 */
public class XBRXHDocService extends XBRDefaultService<XBRXHDoc> implements XBCXHDocService<XBRXHDoc> {

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
        XBCXFileService fileService = (XBCXFileService) catalog.getCatalogService(XBCXFileService.class);
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
