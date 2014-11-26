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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xbup.lib.client.catalog.XBRCatalog;
import org.xbup.lib.core.catalog.base.XBCItem;
import org.xbup.lib.core.catalog.base.XBCXDesc;
import org.xbup.lib.core.catalog.base.XBCXLanguage;
import org.xbup.lib.core.catalog.base.manager.XBCXDescManager;
import org.xbup.lib.client.XBCatalogServiceMessage;
import org.xbup.lib.client.catalog.remote.XBRItem;
import org.xbup.lib.client.catalog.remote.XBRXDesc;
import org.xbup.lib.client.catalog.remote.XBRXLanguage;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.basic.XBListener;
import org.xbup.lib.core.remote.XBServiceClient;
import org.xbup.lib.core.stream.XBStreamChecker;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * Manager class for XBRXDesc catalog items.
 *
 * @version 0.1.21 2011/12/31
 * @author XBUP Project (http://xbup.org)
 */
public class XBRXDescManager extends XBRDefaultManager<XBRXDesc> implements XBCXDescManager<XBRXDesc> {

    public XBRXDescManager(XBRCatalog catalog) {
        super(catalog);
    }

    @Override
    public XBRXDesc getItemDesc(XBCItem item) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(XBServiceClient.ITEMDESC_DESC_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(((XBRItem) item).getId()));
            listener.endXB();
            XBStreamChecker checker = message.getXBInput();
            long ownerId = checker.attribXB().getLong();
            checker.endXB();
            message.close();
            if (ownerId == 0) {
                return null;
            }
            return new XBRXDesc(client, ownerId);
        } catch (XBProcessingException ex) {
            Logger.getLogger(XBRXNameManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XBRXNameManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public XBRXDesc getItemDesc(XBCItem item, XBCXLanguage language) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(XBServiceClient.LANGNAME_DESC_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(((XBRItem) item).getId()));
            listener.attribXB(new UBNat32(((XBRXLanguage) language).getId()));
            listener.endXB();
            XBStreamChecker checker = message.getXBInput();
            long ownerId = checker.attribXB().getLong();
            checker.endXB();
            message.close();
            if (ownerId == 0) {
                return null;
            }
            return new XBRXDesc(client, ownerId);
        } catch (XBProcessingException ex) {
            Logger.getLogger(XBRXNameManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XBRXNameManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public List<XBCXDesc> getItemDescs(XBCItem item) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(XBServiceClient.ITEMDESCS_DESC_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(((XBRItem) item).getId()));
            listener.endXB();
            XBStreamChecker checker = message.getXBInput();
            List<XBCXDesc> result = new ArrayList<XBCXDesc>();
            long count = checker.attribXB().getLong();
            for (int i = 0; i < count; i++) {
                result.add(new XBRXDesc(client,checker.attribXB().getLong()));
            }
            checker.endXB();
            message.close();
            return result;
        } catch (XBProcessingException ex) {
            Logger.getLogger(XBRXNameManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XBRXNameManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public long getItemsCount() {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(XBServiceClient.DESCSCOUNT_DESC_PROCEDURE);
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
        return 0;
    }

    @Override
    public void initializeExtension() {
    }

    @Override
    public String getExtensionName() {
        return "Description Extension";
    }

}
