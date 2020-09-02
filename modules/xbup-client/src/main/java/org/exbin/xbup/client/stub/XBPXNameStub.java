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
package org.exbin.xbup.client.stub;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.client.XBCatalogServiceClient;
import org.exbin.xbup.client.catalog.remote.XBRItem;
import org.exbin.xbup.client.catalog.remote.XBRXLanguage;
import org.exbin.xbup.client.catalog.remote.XBRXName;
import org.exbin.xbup.core.block.declaration.XBDeclBlockType;
import org.exbin.xbup.core.catalog.base.XBCItem;
import org.exbin.xbup.core.catalog.base.XBCXLanguage;
import org.exbin.xbup.core.catalog.base.XBCXName;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.remote.XBCallHandler;
import org.exbin.xbup.core.serial.param.XBPListenerSerialHandler;
import org.exbin.xbup.core.serial.param.XBPProviderSerialHandler;

/**
 * RPC stub class for XBRXName catalog items.
 *
 * @version 0.2.1 2020/08/26
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XBPXNameStub extends XBPBaseStub<XBCXName> {

    public static long[] ITEM_NAME_PROCEDURE = {0, 2, 9, 0, 0};
    public static long[] TEXT_NAME_PROCEDURE = {0, 2, 9, 1, 0};
    public static long[] LANG_NAME_PROCEDURE = {0, 2, 9, 2, 0};
    public static long[] ITEMNAME_NAME_PROCEDURE = {0, 2, 9, 3, 0};
    public static long[] LANGNAME_NAME_PROCEDURE = {0, 2, 9, 4, 0};
    public static long[] ITEMNAMES_NAME_PROCEDURE = {0, 2, 9, 5, 0};
    public static long[] NAMESCOUNT_NAME_PROCEDURE = {0, 2, 9, 6, 0};

    private final XBCatalogServiceClient client;

    public XBPXNameStub(XBCatalogServiceClient client) {
        super(client, XBRXName::new, new XBPBaseProcedureType(null, null, null, null, new XBDeclBlockType(NAMESCOUNT_NAME_PROCEDURE)));
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
        if (item == null) {
            return null;
        }
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
