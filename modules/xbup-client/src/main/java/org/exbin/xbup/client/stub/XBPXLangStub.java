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
import org.exbin.xbup.client.XBCatalogServiceClient;
import org.exbin.xbup.client.catalog.remote.XBRXLanguage;
import org.exbin.xbup.core.block.declaration.XBDeclBlockType;
import org.exbin.xbup.core.catalog.base.XBCXLanguage;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.remote.XBCallHandler;
import org.exbin.xbup.core.serial.param.XBPListenerSerialHandler;
import org.exbin.xbup.core.serial.param.XBPProviderSerialHandler;

/**
 * RPC stub class for XBRXLanguage catalog items.
 *
 * @version 0.1.25 2015/03/20
 * @author ExBin Project (http://exbin.org)
 */
public class XBPXLangStub extends XBPBaseStub<XBRXLanguage> {

    public static long[] CODE_LANG_PROCEDURE = {0, 2, 8, 0, 0};
    public static long[] DEFAULT_LANG_PROCEDURE = {0, 2, 8, 1, 0};
    public static long[] LANGS_LANG_PROCEDURE = {0, 2, 8, 2, 0};
    public static long[] LANGSCOUNT_LANG_PROCEDURE = {0, 2, 8, 3, 0};

    private final XBCatalogServiceClient client;

    public XBPXLangStub(XBCatalogServiceClient client) {
        super(client, new XBPConstructorMethod<XBRXLanguage>() {
            @Override
            public XBRXLanguage itemConstructor(XBCatalogServiceClient client, long itemId) {
                return new XBRXLanguage(client, itemId);
            }
        }, null);
        this.client = client;
    }

    public String getLangCode(long langId) {
        return XBPStubUtils.longToStringMethod(client.procedureCall(), new XBDeclBlockType(CODE_LANG_PROCEDURE), langId);
    }

    public XBRXLanguage getDefaultLang() {
        Long index = XBPStubUtils.voidToLongMethod(client.procedureCall(), new XBDeclBlockType(DEFAULT_LANG_PROCEDURE));
        return index == null ? null : new XBRXLanguage(client, index);
    }

    public List<XBCXLanguage> getLangs() {
        try {
            XBCallHandler procedureCall = client.procedureCall();

            XBPListenerSerialHandler serialInput = new XBPListenerSerialHandler(procedureCall.getParametersInput());
            serialInput.begin();
            serialInput.putType(new XBDeclBlockType(LANGS_LANG_PROCEDURE));
            serialInput.end();

            XBPProviderSerialHandler serialOutput = new XBPProviderSerialHandler(procedureCall.getResultOutput());
            if (!serialOutput.pullIfEmptyBlock()) {
                serialOutput.begin();
                serialOutput.matchType();
                long count = serialOutput.pullLongAttribute();
                List<XBCXLanguage> result = new ArrayList<>();
                for (int i = 0; i < count; i++) {
                    result.add(new XBRXLanguage(client, serialOutput.pullLongAttribute()));
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
