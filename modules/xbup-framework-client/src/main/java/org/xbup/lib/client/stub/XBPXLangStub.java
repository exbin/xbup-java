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
import org.xbup.lib.client.catalog.remote.XBRXLanguage;
import org.xbup.lib.core.block.XBFixedBlockType;
import org.xbup.lib.core.block.declaration.XBDeclBlockType;
import org.xbup.lib.core.catalog.base.XBCXLanguage;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.remote.XBCallHandler;
import org.xbup.lib.core.serial.param.XBPListenerSerialHandler;
import org.xbup.lib.core.serial.param.XBPProviderSerialHandler;

/**
 * RPC stub class for XBRXLanguage catalog items.
 *
 * @version 0.1.25 2015/03/20
 * @author XBUP Project (http://xbup.org)
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
                serialOutput.matchType(new XBFixedBlockType());
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
