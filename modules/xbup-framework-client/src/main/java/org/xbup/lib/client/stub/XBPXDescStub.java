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
import org.xbup.lib.client.catalog.remote.XBRXDesc;
import org.xbup.lib.client.catalog.remote.XBRXLanguage;
import org.xbup.lib.core.catalog.base.XBCItem;
import org.xbup.lib.core.catalog.base.XBCXDesc;
import org.xbup.lib.core.catalog.base.XBCXLanguage;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.basic.XBListener;
import org.xbup.lib.core.parser.basic.XBMatchingProvider;
import org.xbup.lib.core.parser.token.pull.XBPullProvider;
import org.xbup.lib.core.serial.child.XBChildInputSerialHandler;
import org.xbup.lib.core.serial.child.XBChildProviderSerialHandler;
import org.xbup.lib.core.type.XBString;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * RPC stub class for XBRXDesc catalog items.
 *
 * @version 0.1.25 2015/02/21
 * @author XBUP Project (http://xbup.org)
 */
public class XBPXDescStub implements XBPManagerStub<XBRXDesc> {

    public static long[] ITEM_DESC_PROCEDURE = {0, 2, 10, 0, 0};
    public static long[] TEXT_DESC_PROCEDURE = {0, 2, 10, 1, 0};
    public static long[] LANG_DESC_PROCEDURE = {0, 2, 10, 2, 0};
    public static long[] ITEMDESC_DESC_PROCEDURE = {0, 2, 10, 3, 0};
    public static long[] LANGNAME_DESC_PROCEDURE = {0, 2, 10, 4, 0};
    public static long[] ITEMDESCS_DESC_PROCEDURE = {0, 2, 10, 5, 0};
    public static long[] DESCSCOUNT_DESC_PROCEDURE = {0, 2, 10, 6, 0};

    private final XBCatalogServiceClient client;

    public XBPXDescStub(XBCatalogServiceClient client) {
        this.client = client;
    }

    public XBRItem getDescItem(long descId) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(ITEM_DESC_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(descId));
            listener.endXB();
            XBMatchingProvider checker = message.getXBInput();
            long ownerId = checker.matchAttribXB().getNaturalLong();
            checker.matchEndXB();
            message.close();
            return new XBRItem(client, ownerId);
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPXDescStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public String getText(long descId) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(TEXT_DESC_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(descId));
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
            Logger.getLogger(XBPXDescStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public XBRXLanguage getLang(long descId) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(LANG_DESC_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(descId));
            listener.endXB();
            XBMatchingProvider checker = message.getXBInput();
            long index = checker.matchAttribXB().getNaturalLong();
            checker.matchEndXB();
            message.close();
            return new XBRXLanguage(client, index);
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPXDescStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public XBRXDesc getDefaultItemDesc(XBCItem item) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(ITEMDESC_DESC_PROCEDURE);
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
            return new XBRXDesc(client, ownerId);
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPXDescStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public XBRXDesc getItemDesc(XBCItem item, XBCXLanguage language) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(LANGNAME_DESC_PROCEDURE);
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
            return new XBRXDesc(client, ownerId);
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPXDescStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public List<XBCXDesc> getItemDescs(XBCItem item) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(ITEMDESCS_DESC_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(((XBRItem) item).getId()));
            listener.endXB();
            XBMatchingProvider checker = message.getXBInput();
            List<XBCXDesc> result = new ArrayList<>();
            long count = checker.matchAttribXB().getNaturalLong();
            for (int i = 0; i < count; i++) {
                result.add(new XBRXDesc(client, checker.matchAttribXB().getNaturalLong()));
            }
            checker.matchEndXB();
            message.close();
            return result;
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPXDescStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public XBRXDesc createItem() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void persistItem(XBRXDesc item) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void removeItem(XBRXDesc item) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public XBRXDesc getItem(long itemId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<XBRXDesc> getAllItems() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public long getItemsCount() {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(DESCSCOUNT_DESC_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.endXB();
            XBMatchingProvider checker = message.getXBInput();
            Long count = checker.matchAttribXB().getNaturalLong();
            checker.matchEndXB();
            message.close();
            return count;
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPXDescStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
}
