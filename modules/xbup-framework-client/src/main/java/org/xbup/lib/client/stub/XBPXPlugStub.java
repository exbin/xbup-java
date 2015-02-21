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
import org.xbup.lib.client.catalog.remote.XBRNode;
import org.xbup.lib.client.catalog.remote.XBRXFile;
import org.xbup.lib.client.catalog.remote.XBRXPlugin;
import org.xbup.lib.core.catalog.base.XBCXFile;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.basic.XBListener;
import org.xbup.lib.core.parser.basic.XBMatchingProvider;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * RPC stub class for XBRXPlugin catalog items.
 *
 * @version 0.1.25 2015/02/21
 * @author XBUP Project (http://xbup.org)
 */
public class XBPXPlugStub implements XBPManagerStub<XBRXPlugin> {

    public static long[] OWNER_PLUGIN_PROCEDURE = {0, 2, 14, 0, 0};
    public static long[] FILE_PLUGIN_PROCEDURE = {0, 2, 14, 1, 0};
    public static long[] INDEX_PLUGIN_PROCEDURE = {0, 2, 14, 2, 0};

    private final XBCatalogServiceClient client;

    public XBPXPlugStub(XBCatalogServiceClient client) {
        this.client = client;
    }

    public XBRNode getOwner(long pluginId) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(OWNER_PLUGIN_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(pluginId));
            listener.endXB();
            XBMatchingProvider checker = message.getXBInput();
            long ownerId = checker.matchAttribXB().getNaturalLong();
            checker.matchEndXB();
            message.close();
            if (ownerId == 0) {
                return null;
            }
            return new XBRNode(client, ownerId);
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPXPlugStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public XBCXFile getPluginFile(long pluginId) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(FILE_PLUGIN_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(pluginId));
            listener.endXB();
            XBMatchingProvider checker = message.getXBInput();
            long index = checker.matchAttribXB().getNaturalLong();
            checker.matchEndXB();
            message.close();
            return new XBRXFile(client, index);
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPXPlugStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Long getPluginIndex(long pluginId) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(INDEX_PLUGIN_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(pluginId));
            listener.endXB();
            XBMatchingProvider checker = message.getXBInput();
            long index = checker.matchAttribXB().getNaturalLong();
            checker.matchEndXB();
            message.close();
            return index;
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPXPlugStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public XBRXPlugin createItem() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void persistItem(XBRXPlugin item) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void removeItem(XBRXPlugin item) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public XBRXPlugin getItem(long itemId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<XBRXPlugin> getAllItems() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public long getItemsCount() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
