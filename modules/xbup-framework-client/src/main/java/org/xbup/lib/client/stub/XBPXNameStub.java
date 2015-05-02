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
import org.xbup.lib.client.catalog.remote.XBRXLanguage;
import org.xbup.lib.client.catalog.remote.XBRXName;
import org.xbup.lib.core.block.declaration.XBDeclBlockType;
import org.xbup.lib.core.catalog.base.XBCItem;
import org.xbup.lib.core.catalog.base.XBCXLanguage;
import org.xbup.lib.core.catalog.base.XBCXName;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.remote.XBCallHandler;
import org.xbup.lib.core.serial.param.XBPListenerSerialHandler;
import org.xbup.lib.core.serial.param.XBPProviderSerialHandler;

/**
 * RPC stub class for XBRXName catalog items.
 *
 * @version 0.1.25 2015/03/20
 * @author XBUP Project (http://xbup.org)
 */
public class XBPXNameStub extends XBPBaseStub<XBRXName> {

    public static long[] ITEM_NAME_PROCEDURE = {0, 2, 9, 0, 0};
    public static long[] TEXT_NAME_PROCEDURE = {0, 2, 9, 1, 0};
    public static long[] LANG_NAME_PROCEDURE = {0, 2, 9, 2, 0};
    public static long[] ITEMNAME_NAME_PROCEDURE = {0, 2, 9, 3, 0};
    public static long[] LANGNAME_NAME_PROCEDURE = {0, 2, 9, 4, 0};
    public static long[] ITEMNAMES_NAME_PROCEDURE = {0, 2, 9, 5, 0};
    public static long[] NAMESCOUNT_NAME_PROCEDURE = {0, 2, 9, 6, 0};

    private final XBCatalogServiceClient client;

    public XBPXNameStub(XBCatalogServiceClient client) {
        super(client, new XBPConstructorMethod<XBRXName>() {
            @Override
            public XBRXName itemConstructor(XBCatalogServiceClient client, long itemId) {
                return new XBRXName(client, itemId);
            }
        }, new XBPBaseProcedureType(null, null, null, null, new XBDeclBlockType(NAMESCOUNT_NAME_PROCEDURE)));
        this.client = client;
    }

    public XBRItem getNameItem(long nameId) {
        Long index = XBPStubUtils.longToLongMethod(client.procedureCall(), new XBDeclBlockType(ITEM_NAME_PROCEDURE), nameId);
        return index == null ? null : new XBRItem(client, index);
    }

    public String getText(long nameId) {
        return XBPStubUtils.longToStringMethod(client.procedureCall(), new XBDeclBlockType(TEXT_NAME_PROCEDURE), nameId);
    }

    public XBRXLanguage getLang(long nameId) {
        Long index = XBPStubUtils.longToLongMethod(client.procedureCall(), new XBDeclBlockType(LANG_NAME_PROCEDURE), nameId);
        return index == null ? null : new XBRXLanguage(client, index);
    }

    public XBRXName getDefaultItemName(XBCItem item) {
        Long index = XBPStubUtils.longToLongMethod(client.procedureCall(), new XBDeclBlockType(ITEMNAME_NAME_PROCEDURE), item.getId());
        return index == null ? null : new XBRXName(client, index);
    }

    public XBRXName getItemName(XBCItem item, XBCXLanguage language) {
        Long index = XBPStubUtils.twoLongsToLongMethod(client.procedureCall(), new XBDeclBlockType(LANGNAME_NAME_PROCEDURE), item.getId(), language.getId());
        return index == null ? null : new XBRXName(client, index);
    }

    public List<XBCXName> getItemNames(XBCItem item) {
        try {
            XBCallHandler procedureCall = client.procedureCall();

            XBPListenerSerialHandler serialInput = new XBPListenerSerialHandler(procedureCall.getParametersInput());
            serialInput.begin();
            serialInput.putType(new XBDeclBlockType(ITEMNAMES_NAME_PROCEDURE));
            serialInput.putAttribute(item.getId());
            serialInput.end();

            XBPProviderSerialHandler serialOutput = new XBPProviderSerialHandler(procedureCall.getResultOutput());
            if (!serialOutput.pullIfEmptyBlock()) {
                serialOutput.begin();
                serialOutput.matchType();
                long count = serialOutput.pullLongAttribute();
                List<XBCXName> result = new ArrayList<>();
                for (int i = 0; i < count; i++) {
                    result.add(new XBRXName(client, serialOutput.pullLongAttribute()));
                }
                serialOutput.end();
                procedureCall.execute();
                return result;
            }

            procedureCall.execute();
            return null;
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPItemStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
