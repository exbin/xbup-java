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
package org.xbup.lib.client.catalog.remote.manager;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xbup.lib.client.catalog.XBRCatalog;
import org.xbup.lib.core.catalog.base.XBCBase;
import org.xbup.lib.core.catalog.base.manager.XBCManager;
import org.xbup.lib.core.catalog.client.XBCatalogServiceClient;
import org.xbup.lib.core.catalog.client.XBCatalogServiceMessage;
import org.xbup.lib.client.catalog.remote.XBRItem;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.basic.XBListener;
import org.xbup.lib.core.remote.XBServiceClient;
import org.xbup.lib.core.stream.XBStreamChecker;

/**
 * Default manager for catalog items.
 *
 * @version 0.1.21 2012/01/01
 * @author XBUP Project (http://xbup.org)
 * @param <T> entity class
 */
public class XBRDefaultManager<T extends XBCBase> implements XBCManager<T> {

    protected XBRCatalog catalog;
    protected XBCatalogServiceClient client;

    /** Creates a new instance */
    public XBRDefaultManager(XBRCatalog catalog) {
        this.catalog = catalog;
        this.client = catalog.getCatalogServiceClient();
    }

    @Override
    public T createItem() {
        return null;
    }

    @Override
    public void removeItem(T item) {
        // TODO
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<T> getAllItems() {
        // TODO
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T getItem(long itemId) {
        // TODO
        return null;
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
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBRItem.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    @Override
    public void persistItem(T item) {
        // TODO
    }
}
