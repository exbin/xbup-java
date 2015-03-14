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
import org.xbup.lib.client.catalog.remote.XBRXBlockLine;
import org.xbup.lib.client.catalog.remote.XBRXPlugLine;
import org.xbup.lib.client.catalog.remote.XBRXPlugin;
import static org.xbup.lib.client.stub.XBPXLangStub.LANGSCOUNT_LANG_PROCEDURE;
import org.xbup.lib.core.block.declaration.XBDeclBlockType;
import org.xbup.lib.core.catalog.base.XBCBlockRev;
import org.xbup.lib.core.catalog.base.XBCXPlugLine;
import org.xbup.lib.core.catalog.base.XBCXPlugin;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.basic.XBListener;
import org.xbup.lib.core.parser.basic.XBMatchingProvider;
import org.xbup.lib.core.remote.XBCallHandler;
import org.xbup.lib.core.serial.param.XBPListenerSerialHandler;
import org.xbup.lib.core.serial.param.XBPProviderSerialHandler;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * RPC stub class for XBRXBlockLine catalog items.
 *
 * @version 0.1.25 2015/02/21
 * @author XBUP Project (http://xbup.org)
 */
public class XBPXLineStub implements XBPManagerStub<XBRXBlockLine> {

    public static long[] REV_LINE_PROCEDURE = {0, 2, 15, 0, 0};
    public static long[] PLUGIN_LINE_PROCEDURE = {0, 2, 15, 1, 0};
    public static long[] PRIORITY_LINE_PROCEDURE = {0, 2, 15, 2, 0};
    public static long[] LINESCOUNT_LINE_PROCEDURE = {0, 2, 15, 3, 0};
    public static long[] REVLINE_LINE_PROCEDURE = {0, 2, 15, 4, 0};
    public static long[] PLUGLINESCOUNT_LINE_PROCEDURE = {0, 2, 15, 5, 0};
    public static long[] PLUGLINE_LINE_PROCEDURE = {0, 2, 15, 6, 0};
    public static long[] LINEPLUGIN_PLUGIN_PROCEDURE = {0, 2, 14, 3, 0};
    public static long[] LINEINDEX_PLUGIN_PROCEDURE = {0, 2, 14, 4, 0};

    private final XBCatalogServiceClient client;

    public XBPXLineStub(XBCatalogServiceClient client) {
        this.client = client;
    }

    public XBCXPlugin getPlugin(long lineId) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(LINEPLUGIN_PLUGIN_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(lineId));
            listener.endXB();
            XBMatchingProvider checker = message.getXBInput();
            long ownerId = checker.matchAttribXB().getNaturalLong();
            checker.matchEndXB();
            message.close();
            return new XBRXPlugin(client, ownerId);
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPXLineStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Long getLineIndex(long lineId) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(LINEINDEX_PLUGIN_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(lineId));
            listener.endXB();
            XBMatchingProvider checker = message.getXBInput();
            long index = checker.matchAttribXB().getNaturalLong();
            checker.matchEndXB();
            message.close();
            return index;
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPXLineStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public XBCBlockRev getBlockRev(long blockLineId) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(REV_LINE_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(blockLineId));
            listener.endXB();
            XBMatchingProvider checker = message.getXBInput();
            long ownerId = checker.matchAttribXB().getNaturalLong();
            checker.matchEndXB();
            message.close();
            return new XBRBlockRev(client, ownerId);
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPXLineStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public XBCXPlugLine getLine(long blockLineId) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(PLUGIN_LINE_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(blockLineId));
            listener.endXB();
            XBMatchingProvider checker = message.getXBInput();
            long ownerId = checker.matchAttribXB().getNaturalLong();
            checker.matchEndXB();
            message.close();
            return new XBRXPlugLine(client, ownerId);
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPXLineStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Long getPriority(long blockLineId) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(PRIORITY_LINE_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(blockLineId));
            listener.endXB();
            XBMatchingProvider checker = message.getXBInput();
            long index = checker.matchAttribXB().getNaturalLong();
            checker.matchEndXB();
            message.close();
            return index;
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPXLineStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public XBRXBlockLine findLineByPR(XBCBlockRev rev, long priority) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(REVLINE_LINE_PROCEDURE);
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
            return new XBRXBlockLine(client, itemId);
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPXLineStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public XBRXPlugLine getPlugLine(XBCXPlugin plugin, long lineIndex) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(PLUGLINE_LINE_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(((XBRXPlugin) plugin).getId()));
            listener.attribXB(new UBNat32(lineIndex));
            listener.endXB();
            XBMatchingProvider checker = message.getXBInput();
            long itemId = checker.matchAttribXB().getNaturalLong();
            checker.matchEndXB();
            message.close();
            if (itemId == 0) {
                return null;
            }
            return new XBRXPlugLine(client, itemId);
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPXLineStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Long getAllPlugLinesCount() {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(PLUGLINESCOUNT_LINE_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.endXB();
            XBMatchingProvider checker = message.getXBInput();
            Long count = checker.matchAttribXB().getNaturalLong();
            checker.matchEndXB();
            message.close();
            return count;
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPXLineStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public XBRXBlockLine createItem() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void persistItem(XBRXBlockLine item) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void removeItem(XBRXBlockLine item) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public XBRXBlockLine getItem(long itemId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<XBRXBlockLine> getAllItems() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public long getItemsCount() {
        try {
            XBCallHandler procedureCall = client.procedureCall();

            XBPListenerSerialHandler serialInput = new XBPListenerSerialHandler(procedureCall.getParametersInput());
            serialInput.begin();
            serialInput.putType(new XBDeclBlockType(LINESCOUNT_LINE_PROCEDURE));
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
