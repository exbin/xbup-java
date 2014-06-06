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
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xbup.lib.xb.catalog.XBRCatalog;
import org.xbup.lib.xb.catalog.base.XBCNode;
import org.xbup.lib.xb.catalog.base.manager.XBCXInfoManager;
import org.xbup.lib.xb.catalog.client.XBCatalogServiceMessage;
import org.xbup.lib.xb.catalog.remote.XBRItem;
import org.xbup.lib.xb.catalog.remote.XBRItemInfo;
import org.xbup.lib.xb.catalog.remote.XBRNode;
import org.xbup.lib.xb.parser.XBProcessingException;
import org.xbup.lib.xb.parser.basic.XBListener;
import org.xbup.lib.xb.remote.XBServiceClient;
import org.xbup.lib.xb.stream.XBStreamChecker;
import org.xbup.lib.xb.ubnumber.type.UBNat32;

/**
 * Manager class for XBRItemInfo catalog items.
 *
 * @version 0.1 wr21.0 2011/12/31
 * @author XBUP Project (http://xbup.org)
 */
public class XBRInfoManager extends XBRDefaultManager<XBRItemInfo> implements XBCXInfoManager<XBRItemInfo> {

    public XBRInfoManager(XBRCatalog catalog) {
        super(catalog);
    }

    @Override
    public XBRItemInfo getNodeInfo(XBCNode node) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(XBServiceClient.NODE_INFO_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(((XBRNode) node).getId()));
            listener.endXB();
            XBStreamChecker checker = message.getXBInput();
            long infoId = checker.attribXB().getLong();
            checker.endXB();
            message.close();
            return new XBRItemInfo(client,infoId);
        } catch (XBProcessingException ex) {
            Logger.getLogger(XBRItem.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XBRItem.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public Long getAllInfosCount() {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(XBServiceClient.INFOSCOUNT_INFO_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.endXB();
            XBStreamChecker checker = message.getXBInput();
            long index = checker.attribXB().getLong();
            checker.endXB();
            message.close();
            return index;
        } catch (XBProcessingException ex) {
            Logger.getLogger(XBRItem.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XBRItem.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
