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
package org.xbup.lib.xb.catalog.remote.manager;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xbup.lib.xb.catalog.XBRCatalog;
import org.xbup.lib.xb.catalog.base.XBCBlockRev;
import org.xbup.lib.xb.catalog.base.XBCXBlockLine;
import org.xbup.lib.xb.catalog.base.XBCXPlugLine;
import org.xbup.lib.xb.catalog.base.XBCXPlugin;
import org.xbup.lib.xb.catalog.base.manager.XBCXLineManager;
import org.xbup.lib.xb.catalog.client.XBCatalogServiceMessage;
import org.xbup.lib.xb.catalog.remote.XBRBlockRev;
import org.xbup.lib.xb.catalog.remote.XBRXBlockLine;
import org.xbup.lib.xb.catalog.remote.XBRXPlugLine;
import org.xbup.lib.xb.catalog.remote.XBRXPlugin;
import org.xbup.lib.xb.parser.XBProcessingException;
import org.xbup.lib.xb.parser.basic.XBListener;
import org.xbup.lib.xb.remote.XBServiceClient;
import org.xbup.lib.xb.stream.XBStreamChecker;
import org.xbup.lib.xb.ubnumber.type.UBNat32;

/**
 * Manager class for XBRXBlockLine catalog items.
 *
 * @version 0.1 wr21.0 2011/12/31
 * @author XBUP Project (http://xbup.org)
 */
public class XBRXLineManager extends XBRDefaultManager<XBRXBlockLine> implements XBCXLineManager<XBRXBlockLine> {

    public XBRXLineManager(XBRCatalog catalog) {
        super(catalog);
    }

    @Override
    public void initializeExtension() {
    }

    @Override
    public String getExtensionName() {
        return "Line Extension";
    }

    @Override
    public List<XBCXBlockLine> getLines(XBCBlockRev rev) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public long getLinesCount(XBCBlockRev rev) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public XBRXBlockLine findLineByPR(XBCBlockRev rev, long priority) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(XBServiceClient.REVLINE_LINE_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(((XBRBlockRev) rev).getId()));
            listener.attribXB(new UBNat32(priority));
            listener.endXB();
            XBStreamChecker checker = message.getXBInput();
            long itemId = checker.attribXB().getLong();
            checker.endXB();
            message.close();
            if (itemId == 0) {
                return null;
            }
            return new XBRXBlockLine(client, itemId);
        } catch (XBProcessingException ex) {
            Logger.getLogger(XBRXNameManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XBRXNameManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public Long getAllLinesCount() {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(XBServiceClient.LINESCOUNT_LINE_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.endXB();
            XBStreamChecker checker = message.getXBInput();
            Long count = checker.attribXB().getLong();
            checker.endXB();
            message.close();
            return count;
        } catch (XBProcessingException ex) {
            Logger.getLogger(XBRXNameManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XBRXNameManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public List<XBCXPlugLine> getPlugLines(XBCXPlugin plugin) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public long getPlugLinesCount(XBCXPlugin plugin) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public XBRXPlugLine getPlugLine(XBCXPlugin plugin, long lineIndex) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(XBServiceClient.PLUGLINE_LINE_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(((XBRXPlugin) plugin).getId()));
            listener.attribXB(new UBNat32(lineIndex));
            listener.endXB();
            XBStreamChecker checker = message.getXBInput();
            long itemId = checker.attribXB().getLong();
            checker.endXB();
            message.close();
            if (itemId == 0) {
                return null;
            }
            return new XBRXPlugLine(client, itemId);
        } catch (XBProcessingException ex) {
            Logger.getLogger(XBRXNameManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XBRXNameManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public Long getAllPlugLinesCount() {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(XBServiceClient.PLUGLINESCOUNT_LINE_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.endXB();
            XBStreamChecker checker = message.getXBInput();
            Long count = checker.attribXB().getLong();
            checker.endXB();
            message.close();
            return count;
        } catch (XBProcessingException ex) {
            Logger.getLogger(XBRXNameManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XBRXNameManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public XBRXBlockLine findById(long id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public XBRXPlugLine findPlugLineById(long id) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
