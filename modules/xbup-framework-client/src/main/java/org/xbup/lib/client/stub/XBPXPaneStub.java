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
import org.xbup.lib.client.catalog.remote.XBRBlockRev;
import org.xbup.lib.client.catalog.remote.XBRXBlockPane;
import org.xbup.lib.client.catalog.remote.XBRXPlugPane;
import org.xbup.lib.client.catalog.remote.XBRXPlugin;
import org.xbup.lib.core.block.declaration.XBDeclBlockType;
import org.xbup.lib.core.catalog.base.XBCBlockRev;
import org.xbup.lib.core.catalog.base.XBCXPlugPane;
import org.xbup.lib.core.catalog.base.XBCXPlugin;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.basic.XBListener;
import org.xbup.lib.core.parser.basic.XBMatchingProvider;
import org.xbup.lib.core.remote.XBCallHandler;
import org.xbup.lib.core.serial.param.XBPListenerSerialHandler;
import org.xbup.lib.core.serial.param.XBPProviderSerialHandler;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * RPC stub class for XBRXBlockPane catalog items.
 *
 * @version 0.1.25 2015/02/21
 * @author XBUP Project (http://xbup.org)
 */
public class XBPXPaneStub implements XBPManagerStub<XBRXBlockPane> {

    public static long[] REV_PANE_PROCEDURE = {0, 2, 16, 0, 0};
    public static long[] PLUGIN_PANE_PROCEDURE = {0, 2, 16, 1, 0};
    public static long[] PRIORITY_PANE_PROCEDURE = {0, 2, 16, 2, 0};
    public static long[] PANESCOUNT_PANE_PROCEDURE = {0, 2, 16, 3, 0};
    public static long[] REVPANE_PANE_PROCEDURE = {0, 2, 16, 4, 0};
    public static long[] PLUGPANESCOUNT_PANE_PROCEDURE = {0, 2, 16, 5, 0};
    public static long[] PLUGPANE_PANE_PROCEDURE = {0, 2, 16, 6, 0};
    public static long[] PANEPLUGIN_PLUGIN_PROCEDURE = {0, 2, 14, 5, 0};
    public static long[] PANEINDEX_PLUGIN_PROCEDURE = {0, 2, 14, 6, 0};

    private final XBCatalogServiceClient client;

    public XBPXPaneStub(XBCatalogServiceClient client) {
        this.client = client;
    }

    public XBCXPlugin getPlugin(long paneId) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(PANEPLUGIN_PLUGIN_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(paneId));
            listener.endXB();
            XBMatchingProvider checker = message.getXBInput();
            long ownerId = checker.matchAttribXB().getNaturalLong();
            checker.matchEndXB();
            message.close();
            return new XBRXPlugin(client, ownerId);
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPXPaneStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Long getPaneIndex(long paneId) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(PANEINDEX_PLUGIN_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(paneId));
            listener.endXB();
            XBMatchingProvider checker = message.getXBInput();
            long index = checker.matchAttribXB().getNaturalLong();
            checker.matchEndXB();
            message.close();
            return index;
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPXPaneStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public XBCBlockRev getBlockRev(long blockPaneId) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(REV_PANE_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(blockPaneId));
            listener.endXB();
            XBMatchingProvider checker = message.getXBInput();
            long ownerId = checker.matchAttribXB().getNaturalLong();
            checker.matchEndXB();
            message.close();
            return new XBRBlockRev(client, ownerId);
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPXPaneStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public XBCXPlugPane getPane(long blockPaneId) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(PLUGIN_PANE_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(blockPaneId));
            listener.endXB();
            XBMatchingProvider checker = message.getXBInput();
            long ownerId = checker.matchAttribXB().getNaturalLong();
            checker.matchEndXB();
            message.close();
            if (ownerId == 0) {
                return null;
            }
            return new XBRXPlugPane(client, ownerId);
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPXPaneStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Long getPriority(long blockPaneId) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(PRIORITY_PANE_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(blockPaneId));
            listener.endXB();
            XBMatchingProvider checker = message.getXBInput();
            long index = checker.matchAttribXB().getNaturalLong();
            checker.matchEndXB();
            message.close();
            return index;
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPXPaneStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public XBRXBlockPane findPaneByPR(XBCBlockRev rev, long priority) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(REVPANE_PANE_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(((XBRBlockRev) rev).getId()));
            listener.attribXB(new UBNat32(priority));
            listener.endXB();
            XBMatchingProvider checker = message.getXBInput();
            long itemId = checker.matchAttribXB().getNaturalLong();
            checker.matchEndXB();
            message.close();
            if (itemId == 0) {
                return null;
            }
            return new XBRXBlockPane(client, itemId);
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPXPaneStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public XBRXPlugPane getPlugPane(XBCXPlugin plugin, long paneIndex) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(PLUGPANE_PANE_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(((XBRXPlugin) plugin).getId()));
            listener.attribXB(new UBNat32(paneIndex));
            listener.endXB();
            XBMatchingProvider checker = message.getXBInput();
            long itemId = checker.matchAttribXB().getNaturalLong();
            checker.matchEndXB();
            message.close();
            if (itemId == 0) {
                return null;
            }
            return new XBRXPlugPane(client, itemId);
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPXPaneStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Long getAllPlugPanesCount() {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(PLUGPANESCOUNT_PANE_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.endXB();
            XBMatchingProvider checker = message.getXBInput();
            Long count = checker.matchAttribXB().getNaturalLong();
            checker.matchEndXB();
            message.close();
            return count;
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPXPaneStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public XBRXBlockPane createItem() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void persistItem(XBRXBlockPane item) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void removeItem(XBRXBlockPane item) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public XBRXBlockPane getItem(long itemId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<XBRXBlockPane> getAllItems() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public long getItemsCount() {
        try {
            XBCallHandler procedureCall = client.procedureCall();

            XBPListenerSerialHandler serialInput = new XBPListenerSerialHandler(procedureCall.getParametersInput());
            serialInput.begin();
            serialInput.putType(new XBDeclBlockType(PANESCOUNT_PANE_PROCEDURE));
            serialInput.end();

            XBPProviderSerialHandler serialOutput = new XBPProviderSerialHandler(procedureCall.getResultOutput());
            UBNat32 count = new UBNat32();
            serialOutput.process(count);

            return count.getLong();
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPItemStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }
}
