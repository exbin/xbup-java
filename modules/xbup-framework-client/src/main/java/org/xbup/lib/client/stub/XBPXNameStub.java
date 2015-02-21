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
import org.xbup.lib.client.catalog.remote.XBRItem;
import org.xbup.lib.client.catalog.remote.XBRXLanguage;
import org.xbup.lib.client.catalog.remote.XBRXName;
import org.xbup.lib.core.catalog.base.XBCItem;
import org.xbup.lib.core.catalog.base.XBCXLanguage;
import org.xbup.lib.core.catalog.base.XBCXName;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.basic.XBListener;
import org.xbup.lib.core.parser.basic.XBMatchingProvider;
import org.xbup.lib.core.parser.token.pull.XBPullProvider;
import org.xbup.lib.core.serial.child.XBChildInputSerialHandler;
import org.xbup.lib.core.serial.child.XBChildProviderSerialHandler;
import org.xbup.lib.core.type.XBString;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * RPC stub class for XBRXName catalog items.
 *
 * @version 0.1.25 2015/02/21
 * @author XBUP Project (http://xbup.org)
 */
public class XBPXNameStub implements XBPManagerStub<XBRXName> {

    public static long[] ITEM_NAME_PROCEDURE = {0, 2, 9, 0, 0};
    public static long[] TEXT_NAME_PROCEDURE = {0, 2, 9, 1, 0};
    public static long[] LANG_NAME_PROCEDURE = {0, 2, 9, 2, 0};
    public static long[] ITEMNAME_NAME_PROCEDURE = {0, 2, 9, 3, 0};
    public static long[] LANGNAME_NAME_PROCEDURE = {0, 2, 9, 4, 0};
    public static long[] ITEMNAMES_NAME_PROCEDURE = {0, 2, 9, 5, 0};
    public static long[] NAMESCOUNT_NAME_PROCEDURE = {0, 2, 9, 6, 0};

    private final XBCatalogServiceClient client;

    public XBPXNameStub(XBCatalogServiceClient client) {
        this.client = client;
    }

    public XBRItem getNameItem(long nameId) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(ITEM_NAME_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(nameId));
            listener.endXB();
            XBMatchingProvider checker = message.getXBInput();
            long ownerId = checker.matchAttribXB().getNaturalLong();
            checker.matchEndXB();
            message.close();
            return new XBRItem(client, ownerId);
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBRXName.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public String getText(long nameId) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(TEXT_NAME_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(nameId));
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
            Logger.getLogger(XBRXName.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public XBRXLanguage getLang(long nameId) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(LANG_NAME_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(nameId));
            listener.endXB();
            XBMatchingProvider checker = message.getXBInput();
            long index = checker.matchAttribXB().getNaturalLong();
            checker.matchEndXB();
            message.close();
            return new XBRXLanguage(client, index);
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBRXName.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public XBRXName getDefaultItemName(XBCItem item) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(ITEMNAME_NAME_PROCEDURE);
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
            return new XBRXName(client, ownerId);
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPXNameStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public XBRXName getItemName(XBCItem item, XBCXLanguage language) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(LANGNAME_NAME_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(((XBRItem) item).getId()));
            listener.attribXB(new UBNat32(((XBRXLanguage) language).getId()));
            listener.endXB();
            XBMatchingProvider checker = message.getXBInput();
            long ownerId = checker.matchAttribXB().getNaturalLong();
            checker.matchEndXB();
            message.close();
            if (ownerId == 0) {
                return null;
            }
            return new XBRXName(client, ownerId);
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPXNameStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<XBCXName> getItemNames(XBCItem item) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(ITEMNAMES_NAME_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(((XBRItem) item).getId()));
            listener.endXB();
            XBMatchingProvider checker = message.getXBInput();
            List<XBCXName> result = new ArrayList<>();
            long count = checker.matchAttribXB().getNaturalLong();
            for (int i = 0; i < count; i++) {
                result.add(new XBRXName(client, checker.matchAttribXB().getNaturalLong()));
            }
            checker.matchEndXB();
            message.close();
            return result;
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPXNameStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public XBRXName createItem() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void persistItem(XBRXName item) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void removeItem(XBRXName item) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public XBRXName getItem(long itemId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<XBRXName> getAllItems() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public long getItemsCount() {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(NAMESCOUNT_NAME_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.endXB();
            XBMatchingProvider checker = message.getXBInput();
            Long count = checker.matchAttribXB().getNaturalLong();
            checker.matchEndXB();
            message.close();
            return count;
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPXNameStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
}
