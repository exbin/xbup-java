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
import org.xbup.lib.core.catalog.base.XBCItem;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.basic.XBListener;
import org.xbup.lib.core.parser.basic.XBMatchingProvider;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * RPC stub class for XBRItem catalog items.
 *
 * @version 0.1.25 2015/02/21
 * @author XBUP Project (http://xbup.org)
 */
public class XBPItemStub implements XBPManagerStub<XBCItem> {

    public static long[] OWNER_ITEM_PROCEDURE = {0, 2, 3, 0, 0};
    public static long[] XBINDEX_ITEM_PROCEDURE = {0, 2, 3, 1, 0};
    public static long[] ITEMSCOUNT_ITEM_PROCEDURE = {0, 2, 3, 2, 0};

    private final XBCatalogServiceClient client;

    public XBPItemStub(XBCatalogServiceClient client) {
        this.client = client;
    }

    public XBRItem getParent(Long itemId) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(OWNER_ITEM_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(itemId));
            listener.endXB();
            XBMatchingProvider checker = message.getXBInput();
            long ownerId = checker.matchAttribXB().getNaturalLong();
            checker.matchEndXB();
            message.close();
            if (ownerId == 0) {
                return null;
            }
            return new XBRItem(client, ownerId);
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPItemStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Long getXBIndex(Long itemId) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(XBINDEX_ITEM_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(itemId));
            listener.endXB();
            XBMatchingProvider checker = message.getXBInput();
            long index = checker.matchAttribXB().getNaturalLong();
            checker.matchEndXB();
            message.close();
            return index;
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPItemStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public XBCItem createItem() {
        throw new IllegalStateException("Cannot create generic item");
    }

    @Override
    public void persistItem(XBCItem item) {
        throw new IllegalStateException("Cannot persist generic item");
    }

    @Override
    public void removeItem(XBCItem item) {
        throw new IllegalStateException("Cannot remove generic item");
    }

    @Override
    public XBCItem getItem(long itemId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<XBCItem> getAllItems() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public long getItemsCount() {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(ITEMSCOUNT_ITEM_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.endXB();
            XBMatchingProvider checker = message.getXBInput();
            long index = checker.matchAttribXB().getNaturalLong();
            checker.matchEndXB();
            message.close();
            return index;
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPItemStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
}
