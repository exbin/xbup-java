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
package org.xbup.lib.client.catalog.remote.service;

import org.xbup.lib.client.catalog.XBRCatalog;
import org.xbup.lib.core.catalog.base.XBCItem;
import org.xbup.lib.core.catalog.base.XBCXFile;
import org.xbup.lib.core.catalog.base.manager.XBCXHDocManager;
import org.xbup.lib.core.catalog.base.service.XBCXFileService;
import org.xbup.lib.core.catalog.base.service.XBCXHDocService;
import org.xbup.lib.core.catalog.base.XBCExtension;
import org.xbup.lib.client.catalog.remote.XBRXHDoc;
import org.xbup.lib.client.catalog.remote.manager.XBRXHDocManager;

/**
 * Interface for XBRXHDoc items service.
 *
 * @version 0.1.21 2011/02/05
 * @author XBUP Project (http://xbup.org)
 */
public class XBRXHDocService extends XBRDefaultService<XBRXHDoc> implements XBCXHDocService<XBRXHDoc> {

    public XBRXHDocService(XBRCatalog catalog) {
        super(catalog);
        itemManager = new XBRXHDocManager(catalog);
        catalog.addCatalogManager(XBCXHDocManager.class, itemManager);
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
