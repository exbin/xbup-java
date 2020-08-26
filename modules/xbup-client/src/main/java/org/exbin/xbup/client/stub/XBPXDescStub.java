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
import org.exbin.xbup.client.catalog.remote.XBRXDesc;
import org.exbin.xbup.client.catalog.remote.XBRXLanguage;
import org.exbin.xbup.core.block.declaration.XBDeclBlockType;
import org.exbin.xbup.core.catalog.base.XBCItem;
import org.exbin.xbup.core.catalog.base.XBCXDesc;
import org.exbin.xbup.core.catalog.base.XBCXLanguage;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.remote.XBCallHandler;
import org.exbin.xbup.core.serial.param.XBPListenerSerialHandler;
import org.exbin.xbup.core.serial.param.XBPProviderSerialHandler;

/**
 * RPC stub class for XBRXDesc catalog items.
 *
 * @version 0.2.1 2020/08/26
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XBPXDescStub extends XBPBaseStub<XBCXDesc> {

    public static long[] ITEM_DESC_PROCEDURE = {0, 2, 10, 0, 0};
    public static long[] TEXT_DESC_PROCEDURE = {0, 2, 10, 1, 0};
    public static long[] LANG_DESC_PROCEDURE = {0, 2, 10, 2, 0};
    public static long[] ITEMDESC_DESC_PROCEDURE = {0, 2, 10, 3, 0};
    public static long[] LANGDESC_DESC_PROCEDURE = {0, 2, 10, 4, 0};
    public static long[] ITEMDESCS_DESC_PROCEDURE = {0, 2, 10, 5, 0};
    public static long[] DESCSCOUNT_DESC_PROCEDURE = {0, 2, 10, 6, 0};

    private final XBCatalogServiceClient client;

    public XBPXDescStub(XBCatalogServiceClient client) {
        super(client, XBRXDesc::new, new XBPBaseProcedureType(null, null, null, null, new XBDeclBlockType(DESCSCOUNT_DESC_PROCEDURE)));
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
                serialOutput.matchType();
                long count = serialOutput.pullLongAttribute();
                List<XBCXDesc> result = new ArrayList<>();
                for (int i = 0; i < count; i++) {
                    result.add(new XBRXDesc(client, serialOutput.pullLongAttribute()));
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
