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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xbup.lib.client.XBCatalogServiceClient;
import org.xbup.lib.client.catalog.remote.XBRItem;
import org.xbup.lib.client.catalog.remote.XBRXDesc;
import org.xbup.lib.client.catalog.remote.XBRXLanguage;
import org.xbup.lib.core.block.XBFixedBlockType;
import org.xbup.lib.core.block.declaration.XBDeclBlockType;
import org.xbup.lib.core.catalog.base.XBCItem;
import org.xbup.lib.core.catalog.base.XBCXDesc;
import org.xbup.lib.core.catalog.base.XBCXLanguage;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.remote.XBCallHandler;
import org.xbup.lib.core.serial.param.XBPListenerSerialHandler;
import org.xbup.lib.core.serial.param.XBPProviderSerialHandler;

/**
 * RPC stub class for XBRXDesc catalog items.
 *
 * @version 0.1.25 2015/03/15
 * @author XBUP Project (http://xbup.org)
 */
public class XBPXDescStub implements XBPManagerStub<XBRXDesc> {

    public static long[] ITEM_DESC_PROCEDURE = {0, 2, 10, 0, 0};
    public static long[] TEXT_DESC_PROCEDURE = {0, 2, 10, 1, 0};
    public static long[] LANG_DESC_PROCEDURE = {0, 2, 10, 2, 0};
    public static long[] ITEMDESC_DESC_PROCEDURE = {0, 2, 10, 3, 0};
    public static long[] LANGDESC_DESC_PROCEDURE = {0, 2, 10, 4, 0};
    public static long[] ITEMDESCS_DESC_PROCEDURE = {0, 2, 10, 5, 0};
    public static long[] DESCSCOUNT_DESC_PROCEDURE = {0, 2, 10, 6, 0};

    private final XBCatalogServiceClient client;

    public XBPXDescStub(XBCatalogServiceClient client) {
        this.client = client;
    }

    public XBRItem getDescItem(long descId) {
        Long index = XBPStubUtils.longToLongMethod(client.procedureCall(), new XBDeclBlockType(ITEM_DESC_PROCEDURE), descId);
        return index == null ? null : new XBRItem(client, index);
    }

    public String getText(long descId) {
        return XBPStubUtils.longToStringMethod(client.procedureCall(), new XBDeclBlockType(TEXT_DESC_PROCEDURE), descId);
    }

    public XBRXLanguage getLang(long descId) {
        Long index = XBPStubUtils.longToLongMethod(client.procedureCall(), new XBDeclBlockType(LANG_DESC_PROCEDURE), descId);
        return index == null ? null : new XBRXLanguage(client, index);
    }

    public XBRXDesc getDefaultItemDesc(XBCItem item) {
        Long index = XBPStubUtils.longToLongMethod(client.procedureCall(), new XBDeclBlockType(ITEMDESC_DESC_PROCEDURE), item.getId());
        return index == null ? null : new XBRXDesc(client, index);
    }

    public XBRXDesc getItemDesc(XBCItem item, XBCXLanguage language) {
        Long index = XBPStubUtils.twoLongsToLongMethod(client.procedureCall(), new XBDeclBlockType(LANGDESC_DESC_PROCEDURE), item.getId(), language.getId());
        return index == null ? null : new XBRXDesc(client, index);
    }

    public List<XBCXDesc> getItemDescs(XBCItem item) {
        try {
            XBCallHandler procedureCall = client.procedureCall();

            XBPListenerSerialHandler serialInput = new XBPListenerSerialHandler(procedureCall.getParametersInput());
            serialInput.begin();
            serialInput.putType(new XBDeclBlockType(ITEMDESCS_DESC_PROCEDURE));
            serialInput.putAttribute(item.getId());
            serialInput.end();

            XBPProviderSerialHandler serialOutput = new XBPProviderSerialHandler(procedureCall.getResultOutput());
            if (!serialOutput.pullIfEmptyBlock()) {
                serialOutput.begin();
                serialOutput.matchType(new XBFixedBlockType());
                long count = serialOutput.pullLongAttribute();
                List<XBCXDesc> result = new ArrayList<>();
                for (int i = 0; i < count; i++) {
                    result.add(new XBRXDesc(client, serialOutput.pullLongAttribute()));
                }
                serialOutput.end();
                return result;
            }

            return null;
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPItemStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public XBRXDesc createItem() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void persistItem(XBRXDesc item) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void removeItem(XBRXDesc item) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public XBRXDesc getItem(long itemId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<XBRXDesc> getAllItems() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public long getItemsCount() {
        return XBPStubUtils.voidToLongMethod(client.procedureCall(), new XBDeclBlockType(DESCSCOUNT_DESC_PROCEDURE));
    }
}
