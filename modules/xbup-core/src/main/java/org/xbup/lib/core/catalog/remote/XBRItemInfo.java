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

import java.sql.Time;
import org.xbup.lib.core.catalog.base.XBCItem;
import org.xbup.lib.core.catalog.base.XBCXItemInfo;
import org.xbup.lib.core.catalog.base.XBCXUser;
import org.xbup.lib.core.catalog.client.XBCatalogServiceClient;

/**
 * Remote catalog item info.
 *
 * @version 0.1 wr21.0 2012/01/28
 * @author XBUP Project (http://xbup.org)
 */
public class XBRItemInfo implements XBCXItemInfo {

    private long id;
    protected XBCatalogServiceClient client;

    public XBRItemInfo(XBCatalogServiceClient client, long id) {
        this.id = id;
        this.client = client;
    }
/*
    @Override
    public XBRNode getOwner() {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(XBServiceClient.OWNER_ITEM_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(getId()));
            listener.endXB();
            XBStreamChecker checker = message.getXBInput();
            long ownerId = checker.attribXB().getLong();
            checker.endXB();
            message.close();
            if (ownerId == 0) return null;
            return new XBRNode(client, ownerId);
        } catch (XBProcessingException ex) {
            Logger.getLogger(XBRItem.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XBRItem.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
*/
    @Override
    public Long getId() {
        return id;
    }
/*
    public String getFilename() {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(XBServiceClient.FILENAME_INFO_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(getId()));
            listener.endXB();
            XBInputStream input = message.getXBInputStream();
            XBStreamChecker checker = message.getXBInput();
            checker.attribXB(new UBNat32(1));
            checker.beginXB();
            checker.attribXB();
            checker.attribXB();
            XBString text = new XBString();
            XBSerialPullConsumer consumer = new XBSerialPullConsumer(text);
            consumer.attachXBPullProvider(input);
            consumer.processXBPulls();
//            new XBL1ToL0DefaultStreamConvertor(new XBL2ToL1DefaultStreamConvertor(text)).readXBStream(input);
            checker.endXB();
            checker.endXB();
            message.close();
            return text.getValue();
        } catch (XBProcessingException ex) {
            Logger.getLogger(XBRXDesc.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XBRXDesc.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public String getPath() {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(XBServiceClient.PATH_INFO_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(getId()));
            listener.endXB();
            XBInputStream input = message.getXBInputStream();
            XBStreamChecker checker = message.getXBInput();
            checker.attribXB(new UBNat32(1));
            checker.beginXB();
            checker.attribXB();
            checker.attribXB();
            XBString text = new XBString();
            XBSerialPullConsumer consumer = new XBSerialPullConsumer(text);
            consumer.attachXBPullProvider(input);
            consumer.processXBPulls();
//            new XBL1ToL0DefaultStreamConvertor(new XBL2ToL1DefaultStreamConvertor(text)).readXBStream(input);
            checker.endXB();
            checker.endXB();
            message.close();
            return text.getValue();
        } catch (XBProcessingException ex) {
            Logger.getLogger(XBRXDesc.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XBRXDesc.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
     */

    @Override
    public XBCItem getItem() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public XBCXUser getOwner() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public XBCXUser getCreatedByUser() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Time getCreationDate() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
