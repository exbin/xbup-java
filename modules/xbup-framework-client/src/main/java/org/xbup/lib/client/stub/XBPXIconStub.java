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
import org.xbup.lib.client.catalog.remote.XBRXIcon;
import org.xbup.lib.client.catalog.remote.XBRXIconMode;
import org.xbup.lib.core.catalog.base.XBCItem;
import org.xbup.lib.core.catalog.base.XBCXFile;
import org.xbup.lib.core.catalog.base.XBCXIconMode;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.basic.XBListener;
import org.xbup.lib.core.parser.basic.XBMatchingProvider;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * RPC stub class for XBRXIcon catalog items.
 *
 * @version 0.1.25 2015/02/21
 * @author XBUP Project (http://xbup.org)
 */
public class XBPXIconStub implements XBPManagerStub<XBRXIcon> {

    public static long[] OWNER_ICON_PROCEDURE = {0, 2, 13, 0, 0};
    public static long[] MODE_ICON_PROCEDURE = {0, 2, 13, 1, 0};
    public static long[] XBINDEX_ICON_PROCEDURE = {0, 2, 13, 2, 0};
    public static long[] DEFAULTITEM_ICON_PROCEDURE = {0, 2, 13, 3, 0};
    public static long[] FILE_ICON_PROCEDURE = {0, 2, 13, 4, 0};

    private final XBCatalogServiceClient client;

    public XBPXIconStub(XBCatalogServiceClient client) {
        this.client = client;
    }

    public XBRItem getParent(long iconId) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(OWNER_ICON_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(iconId));
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
            Logger.getLogger(XBPXIconStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public XBCXIconMode getMode(long iconId) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(MODE_ICON_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(iconId));
            listener.endXB();
            XBMatchingProvider checker = message.getXBInput();
            long ownerId = checker.matchAttribXB().getNaturalLong();
            checker.matchEndXB();
            message.close();
            if (ownerId == 0) {
                return null;
            }
            return new XBRXIconMode(client, ownerId);
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPXIconStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public XBCXFile getIconFile(long iconId) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(FILE_ICON_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(iconId));
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
            Logger.getLogger(XBPXIconStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public XBRXIcon getDefaultIcon(XBCItem item) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(DEFAULTITEM_ICON_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(((XBRItem) item).getId()));
            listener.endXB();
            XBMatchingProvider checker = message.getXBInput();
            Long index = checker.matchAttribXB().getNaturalLong();
            checker.matchEndXB();
            message.close();
            if (index == 0) {
                return null;
            }
            return new XBRXIcon(client, index);
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPXIconStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public XBRXIcon createItem() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void persistItem(XBRXIcon item) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void removeItem(XBRXIcon item) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public XBRXIcon getItem(long itemId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<XBRXIcon> getAllItems() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public long getItemsCount() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
