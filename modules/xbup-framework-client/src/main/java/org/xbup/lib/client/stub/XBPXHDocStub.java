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
package org.xbup.lib.client.stub;

import java.util.List;
import org.xbup.lib.client.XBCatalogServiceClient;
import org.xbup.lib.client.catalog.remote.XBRItem;
import org.xbup.lib.client.catalog.remote.XBRXFile;
import org.xbup.lib.client.catalog.remote.XBRXHDoc;
import org.xbup.lib.core.block.declaration.XBDeclBlockType;
import org.xbup.lib.core.catalog.base.XBCXFile;

/**
 * RPC stub class for XBRXHDoc catalog items.
 *
 * @version 0.1.25 2015/03/15
 * @author XBUP Project (http://xbup.org)
 */
public class XBPXHDocStub implements XBPManagerStub<XBRXHDoc> {

    public static long[] OWNER_HDOC_PROCEDURE = {0, 2, 17, 0, 0};
    public static long[] XBINDEX_HDOC_PROCEDURE = {0, 2, 17, 1, 0};
    public static long[] ITEM_HDOC_PROCEDURE = {0, 2, 17, 2, 0};
    public static long[] FILE_HDOC_PROCEDURE = {0, 2, 17, 3, 0};

    private final XBCatalogServiceClient client;

    public XBPXHDocStub(XBCatalogServiceClient client) {
        this.client = client;
    }

    public XBRItem getHDocItem(long hdocId) {
        Long index = XBPStubUtils.longToLongMethod(client.procedureCall(), new XBDeclBlockType(OWNER_HDOC_PROCEDURE), hdocId);
        return index == null ? null : new XBRItem(client, index);
    }

    public Long getXBIndex(long hdocId) {
        return XBPStubUtils.longToLongMethod(client.procedureCall(), new XBDeclBlockType(XBINDEX_HDOC_PROCEDURE), hdocId);
    }

    public XBCXFile getDocFile(long hdocId) {
        Long index = XBPStubUtils.longToLongMethod(client.procedureCall(), new XBDeclBlockType(FILE_HDOC_PROCEDURE), hdocId);
        return index == null ? null : new XBRXFile(client, index);
    }

    @Override
    public XBRXHDoc createItem() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void persistItem(XBRXHDoc item) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void removeItem(XBRXHDoc item) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public XBRXHDoc getItem(long itemId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<XBRXHDoc> getAllItems() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public long getItemsCount() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
