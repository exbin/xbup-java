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
package org.exbin.xbup.client.catalog.remote.manager;

import java.io.InputStream;
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
 * @version 0.1.25 2015/03/26
 * @author ExBin Project (http://exbin.org)
 */
public class XBRXHDocManager extends XBRDefaultManager<XBRXHDoc> implements XBCXHDocManager<XBRXHDoc> {

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
