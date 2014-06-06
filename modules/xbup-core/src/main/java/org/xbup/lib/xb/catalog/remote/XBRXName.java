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
package org.xbup.lib.xb.catalog.remote;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xbup.lib.xb.catalog.base.XBCItem;
import org.xbup.lib.xb.catalog.base.XBCXLanguage;
import org.xbup.lib.xb.catalog.base.XBCXName;
import org.xbup.lib.xb.catalog.client.XBCatalogServiceClient;
import org.xbup.lib.xb.catalog.client.XBCatalogServiceMessage;
import org.xbup.lib.xb.parser.XBProcessingException;
import org.xbup.lib.xb.parser.basic.XBListener;
import org.xbup.lib.xb.remote.XBServiceClient;
import org.xbup.lib.xb.serial.XBSerializationType;
import org.xbup.lib.xb.serial.child.XBChildProviderSerialHandler;
import org.xbup.lib.xb.stream.XBTokenInputStream;
import org.xbup.lib.xb.stream.XBStreamChecker;
import org.xbup.lib.xb.type.XBString;
import org.xbup.lib.xb.ubnumber.type.UBNat32;

/**
 * Catalog remote item name entity.
 *
 * @version 0.1 wr21.0 2011/12/16
 * @author XBUP Project (http://xbup.org)
 */
public class XBRXName implements XBCXName {

    private long id;
    protected XBCatalogServiceClient client;

    public XBRXName(XBCatalogServiceClient client, long id) {
        this.id = id;
        this.client = client;
    }

    @Override
    public XBCItem getItem() {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(XBServiceClient.ITEM_NAME_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(getId()));
            listener.endXB();
            XBStreamChecker checker = message.getXBInput();
            long ownerId = checker.attribXB().getLong();
            checker.endXB();
            message.close();
            return new XBRItem(client, ownerId);
        } catch (XBProcessingException ex) {
            Logger.getLogger(XBRXName.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XBRXName.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public String getText() {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(XBServiceClient.TEXT_NAME_PROCEDURE);
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
            checker.endXB();
            checker.endXB();
            message.close();
            return text.getValue();
        } catch (XBProcessingException ex) {
            Logger.getLogger(XBRXName.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XBRXName.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public XBRXLanguage getLang() {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(XBServiceClient.LANG_NAME_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(getId()));
            listener.endXB();
            XBStreamChecker checker = message.getXBInput();
            long index = checker.attribXB().getLong();
            checker.endXB();
            message.close();
            return new XBRXLanguage(client, index);
        } catch (XBProcessingException ex) {
            Logger.getLogger(XBRXName.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XBRXName.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public void setItem(XBCItem item) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setLang(XBCXLanguage language) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setText(String text) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
