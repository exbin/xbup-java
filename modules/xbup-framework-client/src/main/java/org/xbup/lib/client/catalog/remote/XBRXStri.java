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
package org.xbup.lib.client.catalog.remote;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xbup.lib.core.catalog.base.XBCItem;
import org.xbup.lib.core.catalog.base.XBCXStri;
import org.xbup.lib.client.XBCatalogServiceClient;
import org.xbup.lib.client.XBCatalogServiceMessage;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.basic.XBListener;
import org.xbup.lib.core.remote.XBServiceClient;
import org.xbup.lib.core.serial.child.XBChildInputSerialHandler;
import org.xbup.lib.core.serial.child.XBChildProviderSerialHandler;
import org.xbup.lib.core.stream.XBTokenInputStream;
import org.xbup.lib.core.stream.XBStreamChecker;
import org.xbup.lib.core.type.XBString;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * Catalog remote item string identificator entity.
 *
 * @version 0.1.21 2012/04/18
 * @author XBUP Project (http://xbup.org)
 */
public class XBRXStri implements XBCXStri {

    private long id;
    protected XBCatalogServiceClient client;

    public XBRXStri(XBCatalogServiceClient client, long id) {
        this.id = id;
        this.client = client;
    }

    @Override
    public XBCItem getItem() {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(XBServiceClient.ITEM_STRI_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(getId()));
            listener.endXB();
            XBStreamChecker checker = message.getXBInput();
            long ownerId = checker.attribXB().getLong();
            checker.endXB();
            message.close();
            return new XBRItem(client, ownerId);
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBRXStri.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public String getText() {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(XBServiceClient.TEXT_STRI_PROCEDURE);
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
            XBChildInputSerialHandler handler = new XBChildProviderSerialHandler();
            handler.attachXBPullProvider(input);
            text.new DataBlockSerializator().serializeFromXB(handler);
            checker.endXB();
            checker.endXB();
            message.close();
            return text.getValue();
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBRXName.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * @return the id
     */
    @Override
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public void setItem(XBCItem item) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setText(String text) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getNodePath() {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(XBServiceClient.NODEPATH_STRI_PROCEDURE);
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
            XBChildInputSerialHandler handler = new XBChildProviderSerialHandler();
            handler.attachXBPullProvider(input);
            text.new DataBlockSerializator().serializeFromXB(handler);
            checker.endXB();
            checker.endXB();
            message.close();
            return text.getValue();
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBRXName.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public void setNodePath(String nodePath) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
