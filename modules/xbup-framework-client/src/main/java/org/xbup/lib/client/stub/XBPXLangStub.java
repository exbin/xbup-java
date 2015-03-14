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
import org.xbup.lib.client.XBCatalogServiceMessage;
import org.xbup.lib.client.catalog.remote.XBRXLanguage;
import org.xbup.lib.core.block.declaration.XBDeclBlockType;
import org.xbup.lib.core.catalog.base.XBCXLanguage;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.basic.XBListener;
import org.xbup.lib.core.parser.basic.XBMatchingProvider;
import org.xbup.lib.core.parser.token.pull.XBPullProvider;
import org.xbup.lib.core.remote.XBCallHandler;
import org.xbup.lib.core.serial.child.XBChildInputSerialHandler;
import org.xbup.lib.core.serial.child.XBChildProviderSerialHandler;
import org.xbup.lib.core.serial.param.XBPListenerSerialHandler;
import org.xbup.lib.core.serial.param.XBPProviderSerialHandler;
import org.xbup.lib.core.type.XBString;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * RPC stub class for XBRXLanguage catalog items.
 *
 * @version 0.1.25 2015/03/14
 * @author XBUP Project (http://xbup.org)
 */
public class XBPXLangStub implements XBPManagerStub<XBRXLanguage> {

    public static long[] CODE_LANG_PROCEDURE = {0, 2, 8, 0, 0};
    public static long[] DEFAULT_LANG_PROCEDURE = {0, 2, 8, 1, 0};
    public static long[] LANGS_LANG_PROCEDURE = {0, 2, 8, 2, 0};
    public static long[] LANGSCOUNT_LANG_PROCEDURE = {0, 2, 8, 3, 0};

    private final XBCatalogServiceClient client;

    public XBPXLangStub(XBCatalogServiceClient client) {
        this.client = client;
    }

    public String getLangCode(long langId) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(CODE_LANG_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(langId));
            listener.endXB();
            XBPullProvider input = message.getXBInputStream();
            XBMatchingProvider checker = message.getXBInput();
            checker.matchAttribXB(new UBNat32(1));
            checker.matchBeginXB();
            checker.matchAttribXB();
            checker.matchAttribXB();
            XBString text = new XBString();
            XBChildInputSerialHandler handler = new XBChildProviderSerialHandler();
            handler.attachXBPullProvider(input);
            text.new DataBlockSerializator().serializeFromXB(handler);
//            new XBL1ToL0DefaultStreamConvertor(new XBL2ToL1DefaultStreamConvertor(text)).readXBStream(input);
            checker.matchEndXB();
            checker.matchEndXB();
            message.close();
            return text.getValue();
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPXLangStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public XBRXLanguage getDefaultLang() {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(DEFAULT_LANG_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.endXB();
            XBMatchingProvider checker = message.getXBInput();
            long langId = checker.matchAttribXB().getNaturalLong();
            checker.matchEndXB();
            message.close();
            return new XBRXLanguage(client, langId);
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPXLangStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<XBCXLanguage> getLangs() {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(LANGS_LANG_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.endXB();
            XBMatchingProvider checker = message.getXBInput();
            List<XBCXLanguage> result = new ArrayList<>();
            long count = checker.matchAttribXB().getNaturalLong();
            for (int i = 0; i < count; i++) {
                result.add(new XBRXLanguage(client, checker.matchAttribXB().getNaturalLong()));
            }
            checker.matchEndXB();
            message.close();
            return result;
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPXLangStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public XBRXLanguage createItem() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void persistItem(XBRXLanguage item) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void removeItem(XBRXLanguage item) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public XBRXLanguage getItem(long itemId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<XBRXLanguage> getAllItems() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public long getItemsCount() {
        try {
            XBCallHandler procedureCall = client.procedureCall();

            XBPListenerSerialHandler serialInput = new XBPListenerSerialHandler(procedureCall.getParametersInput());
            serialInput.begin();
            serialInput.putType(new XBDeclBlockType(LANGSCOUNT_LANG_PROCEDURE));
            serialInput.end();

            XBPProviderSerialHandler serialOutput = new XBPProviderSerialHandler(procedureCall.getResultOutput());
            UBNat32 count = new UBNat32();
            serialOutput.process(count);

            return count.getLong();
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPItemStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
}
