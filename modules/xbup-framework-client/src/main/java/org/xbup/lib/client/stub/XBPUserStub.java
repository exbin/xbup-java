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
import org.xbup.lib.client.catalog.remote.XBRItemInfo;
import org.xbup.lib.client.catalog.remote.XBRNode;
import org.xbup.lib.core.catalog.base.XBCNode;
import org.xbup.lib.core.catalog.base.XBCXItemInfo;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.basic.XBListener;
import org.xbup.lib.core.parser.basic.XBMatchingProvider;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * RPC Stub for Info extension related operations.
 *
 * @version 0.1.25 2015/02/21
 * @author XBUP Project (http://xbup.org)
 */
public class XBPUserStub implements XBPManagerStub<XBCXItemInfo> {

    public static long[] NODE_INFO_PROCEDURE = {0, 2, 11, 0, 0};
    public static long[] INFOSCOUNT_INFO_PROCEDURE = {0, 2, 11, 1, 0};
    public static long[] FILENAME_INFO_PROCEDURE = {0, 2, 12, 2, 0};
    public static long[] PATH_INFO_PROCEDURE = {0, 2, 12, 3, 0};

    private final XBCatalogServiceClient client;

    public XBPUserStub(XBCatalogServiceClient client) {
        this.client = client;
    }

    public XBRItemInfo getNodeInfo(XBCNode node) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(NODE_INFO_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(((XBRNode) node).getId()));
            listener.endXB();
            XBMatchingProvider checker = message.getXBInput();
            long infoId = checker.matchAttribXB().getNaturalLong();
            checker.matchEndXB();
            message.close();
            return new XBRItemInfo(client, infoId);
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPUserStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public XBCXItemInfo createItem() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void persistItem(XBCXItemInfo item) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void removeItem(XBCXItemInfo item) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public XBCXItemInfo getItem(long itemId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<XBCXItemInfo> getAllItems() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public long getItemsCount() {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(INFOSCOUNT_INFO_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.endXB();
            XBMatchingProvider checker = message.getXBInput();
            long index = checker.matchAttribXB().getNaturalLong();
            checker.matchEndXB();
            message.close();
            return index;
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPUserStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
}