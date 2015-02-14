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
package org.xbup.lib.client.catalog.remote;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xbup.lib.core.catalog.base.XBCXFile;
import org.xbup.lib.core.catalog.base.XBCXIcon;
import org.xbup.lib.core.catalog.base.XBCXIconMode;
import org.xbup.lib.client.XBCatalogServiceClient;
import org.xbup.lib.client.XBCatalogServiceMessage;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.basic.XBListener;
import org.xbup.lib.core.parser.basic.XBMatchingProvider;
import org.xbup.lib.core.remote.XBServiceClient;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 *
 * @version 0.1.18 2010/01/02
 * @author XBUP Project (http://xbup.org)
 */
public class XBRXIcon implements XBCXIcon {

    private long id;
    protected XBCatalogServiceClient client;

    public XBRXIcon(XBCatalogServiceClient client, long id) {
        this.id = id;
        this.client = client;
    }

    @Override
    public XBRItem getParent() {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(XBServiceClient.OWNER_ICON_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(getId()));
            listener.endXB();
            XBMatchingProvider checker = message.getXBInput();
            long ownerId = checker.matchAttribXB().getNaturalLong();
            checker.matchEndXB();
            message.close();
            if (ownerId == 0) {
                return null;
            }
            return new XBRBlockSpec(client, ownerId);
        } catch (XBProcessingException ex) {
            Logger.getLogger(XBRItem.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XBRItem.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public Long getId() {
        return id;
    }

/*    @Override
    public Long getXBIndex() {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(XBServiceClient.XBINDEX_ICON_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(getId()));
            listener.endXB();
            XBMatchingProvider checker = message.getXBInput();
            long index = checker.matchAttribXB().getLong();
            checker.matchEndXB();
            message.close();
            return index;
        } catch (XBProcessingException ex) {
            Logger.getLogger(XBRItem.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XBRItem.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    } */

    @Override
    public XBCXIconMode getMode() {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(XBServiceClient.MODE_ICON_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(getId()));
            listener.endXB();
            XBMatchingProvider checker = message.getXBInput();
            long ownerId = checker.matchAttribXB().getNaturalLong();
            checker.matchEndXB();
            message.close();
            if (ownerId == 0) {
                return null;
            }
            return new XBRXIconMode(client, ownerId);
        } catch (XBProcessingException ex) {
            Logger.getLogger(XBRItem.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XBRItem.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public XBCXFile getIconFile() {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(XBServiceClient.FILE_ICON_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(getId()));
            listener.endXB();
            XBMatchingProvider checker = message.getXBInput();
            long fileId = checker.matchAttribXB().getNaturalLong();
            checker.matchEndXB();
            message.close();
            if (fileId == 0) {
                return null;
            }
            return new XBRXFile(client, fileId);
        } catch (XBProcessingException ex) {
            Logger.getLogger(XBRItem.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XBRItem.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}
