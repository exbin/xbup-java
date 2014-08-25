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
package org.xbup.lib.core.catalog.remote.manager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xbup.lib.core.catalog.XBRCatalog;
import org.xbup.lib.core.catalog.base.XBCBlockSpec;
import org.xbup.lib.core.catalog.base.XBCItem;
import org.xbup.lib.core.catalog.base.XBCXStri;
import org.xbup.lib.core.catalog.base.manager.XBCNodeManager;
import org.xbup.lib.core.catalog.base.manager.XBCXStriManager;
import org.xbup.lib.core.catalog.client.XBCatalogServiceMessage;
import org.xbup.lib.core.catalog.remote.XBRItem;
import org.xbup.lib.core.catalog.remote.XBRXStri;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.basic.XBListener;
import org.xbup.lib.core.remote.XBServiceClient;
import org.xbup.lib.core.stream.XBStreamChecker;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * Manager class for XBRXStri catalog items.
 *
 * @version 0.1.21 2012/05/01
 * @author XBUP Project (http://xbup.org)
 */
public class XBRXStriManager extends XBRDefaultManager<XBRXStri> implements XBCXStriManager<XBRXStri> {

    public XBRXStriManager(XBRCatalog catalog) {
        super(catalog);
    }

    @Override
    public XBRXStri getItemStringId(XBCItem item) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(XBServiceClient.ITEMSTRI_STRI_PROCEDURE);
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
            return new XBRXStri(client, ownerId);
        } catch (XBProcessingException ex) {
            Logger.getLogger(XBRXStriManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XBRXStriManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public List<XBCXStri> getItemStringIds(XBCItem item) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(XBServiceClient.ITEMSTRIS_STRI_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(((XBRItem) item).getId()));
            listener.endXB();
            XBStreamChecker checker = message.getXBInput();
            List<XBCXStri> result = new ArrayList<XBCXStri>();
            long count = checker.attribXB().getLong();
            for (int i = 0; i < count; i++) {
                result.add(new XBRXStri(client,checker.attribXB().getLong()));
            }
            checker.endXB();
            message.close();
            return result;
        } catch (XBProcessingException ex) {
            Logger.getLogger(XBRXStriManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XBRXStriManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public long getItemsCount() {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(XBServiceClient.STRISCOUNT_STRI_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.endXB();
            XBStreamChecker checker = message.getXBInput();
            Long count = checker.attribXB().getLong();
            checker.endXB();
            message.close();
            return count;
        } catch (XBProcessingException ex) {
            Logger.getLogger(XBRXStriManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XBRXStriManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    @Override
    public void initializeExtension() {
    }

    @Override
    public String getExtensionName() {
        return "Stri Extension";
    }

/*    public String getCaption(XBBlockType blockType) {
        if (blockType == null) return "Unknown";
        if (blockType instanceof XBContextBlockType) {
            XBBlockDecl decl = ((XBContextBlockType) blockType).getBlockDecl();
            if (decl != null) {
                if (decl instanceof XBCBlockDecl) return ((XBCBlockDecl) decl).getCaption();
                if (decl instanceof XBCPBlockDecl) return ((XBCPBlockDecl) decl).getCaption();
            }
        }
        return "unknown ("+ Integer.toString(blockType.getGroupID().getInt()) + "," + Integer.toString(blockType.getBlockID().getInt()) + ")";
    } */

    @Override
    public String getDefaultStringId(XBCBlockSpec blockSpec) {
        XBCXStri Stri = getItemStringId(blockSpec);
        if (Stri==null) {
            return ":";
        }
        return Stri.getText();
    }

    @Override
    public String getFullPath(XBCXStri itemString) {
        if ("/".equals(itemString.getNodePath())) {
            XBCNodeManager nodeManager = (XBCNodeManager) catalog.getCatalogManager(XBCNodeManager.class);
            if (itemString.getItem().getId() == nodeManager.getRootNode().getId()) {
                return "";
            }
        }
        return itemString.getNodePath() + "/" + itemString.getText();
    }
}
