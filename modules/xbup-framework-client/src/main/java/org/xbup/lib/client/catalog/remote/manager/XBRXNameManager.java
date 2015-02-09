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
import org.xbup.lib.core.catalog.base.XBCXLanguage;
import org.xbup.lib.core.catalog.base.XBCXName;
import org.xbup.lib.core.catalog.base.manager.XBCXNameManager;
import org.xbup.lib.client.XBCatalogServiceMessage;
import org.xbup.lib.client.catalog.remote.XBRItem;
import org.xbup.lib.client.catalog.remote.XBRXLanguage;
import org.xbup.lib.client.catalog.remote.XBRXName;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.basic.XBListener;
import org.xbup.lib.core.remote.XBServiceClient;
import org.xbup.lib.core.stream.XBStreamChecker;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * Manager class for XBRXName catalog items.
 *
 * @version 0.1.24 2014/11/17
 * @author XBUP Project (http://xbup.org)
 */
public class XBRXNameManager extends XBRDefaultManager<XBRXName> implements XBCXNameManager<XBRXName> {

    public XBRXNameManager(XBRCatalog catalog) {
        super(catalog);
    }

    @Override
    public XBRXName getDefaultItemName(XBCItem item) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(XBServiceClient.ITEMNAME_NAME_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(((XBRItem) item).getId()));
            listener.endXB();
            XBStreamChecker checker = message.getXBInput();
            long ownerId = checker.attribXB().getNaturalLong();
            checker.endXB();
            message.close();
            if (ownerId == 0) {
                return null;
            }
            return new XBRXName(client, ownerId);
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBRXNameManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public XBRXName getItemName(XBCItem item, XBCXLanguage language) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(XBServiceClient.LANGNAME_NAME_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(((XBRItem) item).getId()));
            listener.attribXB(new UBNat32(((XBRXLanguage) language).getId()));
            listener.endXB();
            XBStreamChecker checker = message.getXBInput();
            long ownerId = checker.attribXB().getNaturalLong();
            checker.endXB();
            message.close();
            if (ownerId == 0) {
                return null;
            }
            return new XBRXName(client, ownerId);
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBRXNameManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public List<XBCXName> getItemNames(XBCItem item) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(XBServiceClient.ITEMNAMES_NAME_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(((XBRItem) item).getId()));
            listener.endXB();
            XBStreamChecker checker = message.getXBInput();
            List<XBCXName> result = new ArrayList<>();
            long count = checker.attribXB().getNaturalLong();
            for (int i = 0; i < count; i++) {
                result.add(new XBRXName(client, checker.attribXB().getNaturalLong()));
            }
            checker.endXB();
            message.close();
            return result;
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBRXNameManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public long getItemsCount() {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(XBServiceClient.NAMESCOUNT_NAME_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.endXB();
            XBStreamChecker checker = message.getXBInput();
            Long count = checker.attribXB().getNaturalLong();
            checker.endXB();
            message.close();
            return count;
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBRXNameManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    @Override
    public void initializeExtension() {
    }

    @Override
    public String getExtensionName() {
        return "Name Extension";
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
    public String getDefaultText(XBCItem item) {
        XBCXName name = getDefaultItemName(item);
        if (name == null) {
            return null;
        }

        return name.getText();
    }
}
