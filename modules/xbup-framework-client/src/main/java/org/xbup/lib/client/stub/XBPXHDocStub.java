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
import org.xbup.lib.client.catalog.remote.XBRBlockSpec;
import org.xbup.lib.client.catalog.remote.XBRItem;
import org.xbup.lib.client.catalog.remote.XBRXFile;
import org.xbup.lib.client.catalog.remote.XBRXHDoc;
import org.xbup.lib.core.catalog.base.XBCXFile;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.basic.XBListener;
import org.xbup.lib.core.parser.basic.XBMatchingProvider;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * RPC stub class for XBRXHDoc catalog items.
 *
 * @version 0.1.25 2015/02/21
 * @author XBUP Project (http://xbup.org)
 */
public class XBPXHDocStub implements XBPManagerStub<XBRXHDoc> {

    public static long[] OWNER_HDOC_PROCEDURE = {0, 2, 17, 0, 0};
    public static long[] XBINDEX_HDOC_PROCEDURE = {0, 2, 17, 1, 0};
    public static long[] ITEM_HDOC_PROCEDURE = {0, 2, 17, 2, 0};
    public static long[] FILE_HDOC_PROCEDURE = {0, 2, 17, 3, 0};

    private final XBCatalogServiceClient client;

    public XBPXHDocStub(XBCatalogServiceClient client) {
        this.client = client;
    }

    public XBRItem getHDocItem(long hdocId) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(OWNER_HDOC_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(hdocId));
            listener.endXB();
            XBMatchingProvider checker = message.getXBInput();
            long ownerId = checker.matchAttribXB().getNaturalLong();
            checker.matchEndXB();
            message.close();
            if (ownerId == 0) {
                return null;
            }
            return new XBRBlockSpec(client, ownerId);
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPXHDocStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Long getXBIndex(long hdocId) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(XBINDEX_HDOC_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(hdocId));
            listener.endXB();
            XBMatchingProvider checker = message.getXBInput();
            long index = checker.matchAttribXB().getNaturalLong();
            checker.matchEndXB();
            message.close();
            return index;
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPXHDocStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public XBCXFile getDocFile(long hdocId) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(FILE_HDOC_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(hdocId));
            listener.endXB();
            XBMatchingProvider checker = message.getXBInput();
            long fileId = checker.matchAttribXB().getNaturalLong();
            checker.matchEndXB();
            message.close();
            if (fileId == 0) {
                return null;
            }
            return new XBRXFile(client, fileId);
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPXHDocStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public XBRXHDoc createItem() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void persistItem(XBRXHDoc item) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void removeItem(XBRXHDoc item) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public XBRXHDoc getItem(long itemId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<XBRXHDoc> getAllItems() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public long getItemsCount() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
