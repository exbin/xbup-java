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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xbup.lib.client.XBCatalogServiceClient;
import org.xbup.lib.client.XBCatalogServiceMessage;
import org.xbup.lib.client.catalog.remote.XBRItem;
import org.xbup.lib.client.catalog.remote.XBRXStri;
import org.xbup.lib.core.catalog.base.XBCItem;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.basic.XBListener;
import org.xbup.lib.core.parser.basic.XBMatchingProvider;
import org.xbup.lib.core.parser.token.pull.XBPullProvider;
import org.xbup.lib.core.serial.child.XBChildInputSerialHandler;
import org.xbup.lib.core.serial.child.XBChildProviderSerialHandler;
import org.xbup.lib.core.type.XBString;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * RPC stub class for XBRXStri catalog items.
 *
 * @version 0.1.25 2015/02/21
 * @author XBUP Project (http://xbup.org)
 */
public class XBPXStriStub implements XBPManagerStub<XBRXStri> {

    public static long[] ITEM_STRI_PROCEDURE = {0, 2, 18, 0, 0};
    public static long[] TEXT_STRI_PROCEDURE = {0, 2, 18, 1, 0};
    public static long[] NODEPATH_STRI_PROCEDURE = {0, 2, 18, 2, 0};
    public static long[] ITEMSTRI_STRI_PROCEDURE = {0, 2, 18, 3, 0};
    public static long[] STRISCOUNT_STRI_PROCEDURE = {0, 2, 18, 5, 0};

    private final XBCatalogServiceClient client;

    public XBPXStriStub(XBCatalogServiceClient client) {
        this.client = client;
    }

    public XBCItem getStriItem(long striId) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(ITEM_STRI_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(striId));
            listener.endXB();
            XBMatchingProvider checker = message.getXBInput();
            long ownerId = checker.matchAttribXB().getNaturalLong();
            checker.matchEndXB();
            message.close();
            return new XBRItem(client, ownerId);
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPXStriStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public String getText(long striId) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(TEXT_STRI_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(striId));
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
            checker.matchEndXB();
            checker.matchEndXB();
            message.close();
            return text.getValue();
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPXStriStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public String getNodePath(long striId) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(NODEPATH_STRI_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(striId));
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
            checker.matchEndXB();
            checker.matchEndXB();
            message.close();
            return text.getValue();
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPXStriStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public XBRXStri getItemStringId(XBCItem item) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(ITEMSTRI_STRI_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(((XBRItem) item).getId()));
            listener.endXB();
            XBMatchingProvider checker = message.getXBInput();
            long ownerId = checker.matchAttribXB().getNaturalLong();
            checker.matchEndXB();
            message.close();
            if (ownerId == 0) {
                return null;
            }
            return new XBRXStri(client, ownerId);
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPXStriStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public XBRXStri createItem() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void persistItem(XBRXStri item) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void removeItem(XBRXStri item) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public XBRXStri getItem(long itemId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<XBRXStri> getAllItems() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public long getItemsCount() {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(STRISCOUNT_STRI_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.endXB();
            XBMatchingProvider checker = message.getXBInput();
            Long count = checker.matchAttribXB().getNaturalLong();
            checker.matchEndXB();
            message.close();
            return count;
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPXStriStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
}
