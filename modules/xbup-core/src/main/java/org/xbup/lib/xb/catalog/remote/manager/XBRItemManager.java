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
import org.xbup.lib.xb.catalog.base.manager.XBCItemManager;
import org.xbup.lib.xb.catalog.client.XBCatalogServiceMessage;
import org.xbup.lib.xb.catalog.remote.XBRItem;
import org.xbup.lib.xb.parser.XBProcessingException;
import org.xbup.lib.xb.parser.basic.XBListener;
import org.xbup.lib.xb.remote.XBServiceClient;
import org.xbup.lib.xb.stream.XBStreamChecker;

/**
 * Manager class for XBRItem catalog items.
 *
 * @version 0.1 wr21.0 2011/12/30
 * @author XBUP Project (http://xbup.org)
 */
public class XBRItemManager extends XBRDefaultManager<XBRItem> implements XBCItemManager<XBRItem> {

    public XBRItemManager(XBRCatalog catalog) {
        super(catalog);
    }

    @Override
    public long getItemsCount() {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(XBServiceClient.ITEMSCOUNT_ITEM_PROCEDURE);
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
        return 0;
    }
}
