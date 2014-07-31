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
package org.xbup.lib.core.catalog.remote;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xbup.lib.core.catalog.base.XBCXLanguage;
import org.xbup.lib.core.catalog.client.XBCatalogServiceClient;
import org.xbup.lib.core.catalog.client.XBCatalogServiceMessage;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.basic.XBListener;
import org.xbup.lib.core.remote.XBServiceClient;
import org.xbup.lib.core.serial.XBSerializationType;
import org.xbup.lib.core.serial.child.XBChildProviderSerialHandler;
import org.xbup.lib.core.stream.XBTokenInputStream;
import org.xbup.lib.core.stream.XBStreamChecker;
import org.xbup.lib.core.type.XBString;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * Interface for remote catalog language entity.
 *
 * @version 0.1 wr21.0 2012/04/15
 * @author XBUP Project (http://xbup.org)
 */
public class XBRXLanguage implements XBCXLanguage {

    private long id;
    protected XBCatalogServiceClient client;

    public XBRXLanguage(XBCatalogServiceClient client, long id) {
        this.id = id;
        this.client = client;
    }

    @Override
    public String getLangCode() {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(XBServiceClient.CODE_LANG_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(getId()));
            listener.endXB();
            XBTokenInputStream input = message.getXBInputStream();
            XBStreamChecker checker = message.getXBInput();
            checker.attribXB(new UBNat32(1));
            checker.beginXB();
            checker.attribXB();
            checker.attribXB();
            XBString text = new XBString();
            XBChildProviderSerialHandler handler = new XBChildProviderSerialHandler();
            handler.attachXBPullProvider(input);
            text.serializeXB(XBSerializationType.FROM_XB, 0, handler);
//            new XBL1ToL0DefaultStreamConvertor(new XBL2ToL1DefaultStreamConvertor(text)).readXBStream(input);
            checker.endXB();
            checker.endXB();
            message.close();
            return text.getValue();
        } catch (XBProcessingException ex) {
            Logger.getLogger(XBRXLanguage.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XBRXLanguage.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public void setLangCode(String langCode) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getName() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
