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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import org.xbup.lib.client.XBCatalogServiceClient;
import org.xbup.lib.client.XBCatalogServiceMessage;
import org.xbup.lib.client.catalog.remote.XBRItem;
import org.xbup.lib.client.catalog.remote.XBRNode;
import org.xbup.lib.client.catalog.remote.XBRXDesc;
import org.xbup.lib.client.catalog.remote.XBRXFile;
import org.xbup.lib.core.catalog.base.XBCXFile;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.basic.XBListener;
import org.xbup.lib.core.parser.basic.XBMatchingProvider;
import org.xbup.lib.core.parser.token.pull.XBPullProvider;
import org.xbup.lib.core.serial.child.XBChildInputSerialHandler;
import org.xbup.lib.core.serial.child.XBChildProviderSerialHandler;
import org.xbup.lib.core.type.XBString;
import org.xbup.lib.core.ubnumber.type.UBNat32;
import org.xbup.lib.core.util.StreamUtils;

/**
 * RPC stub class for XBRXFile catalog items.
 *
 * @version 0.1.25 2015/02/21
 * @author XBUP Project (http://xbup.org)
 */
public class XBPXFileStub implements XBPManagerStub<XBRXFile> {

    public static long[] OWNER_FILE_PROCEDURE = {0, 2, 12, 0, 0};
    public static long[] FILENAME_FILE_PROCEDURE = {0, 2, 12, 1, 0};
    public static long[] DATA_FILE_PROCEDURE = {0, 2, 12, 2, 0};

    private final XBCatalogServiceClient client;

    public XBPXFileStub(XBCatalogServiceClient client) {
        this.client = client;
    }

    public XBRNode getNode(long fileId) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(OWNER_FILE_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(fileId));
            listener.endXB();
            XBMatchingProvider checker = message.getXBInput();
            long ownerId = checker.matchAttribXB().getNaturalLong();
            checker.matchEndXB();
            message.close();
            if (ownerId == 0) {
                return null;
            }
            return new XBRNode(client, ownerId);
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBRItem.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public String getFilename(long fileId) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(FILENAME_FILE_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(fileId));
            listener.endXB();
            XBPullProvider input = message.getXBInputStream();
            XBMatchingProvider checker = message.getXBInput();
            checker.matchAttribXB(new UBNat32(1));
            checker.matchBeginXB();
            checker.matchAttribXB();
            checker.matchAttribXB();
            XBString text = new XBString();
            XBChildInputSerialHandler handler = new XBChildProviderSerialHandler();
            handler.attachXBPullProvider(input);
            text.new DataBlockSerializator().serializeFromXB(handler);
//            new XBL1ToL0DefaultStreamConvertor(new XBL2ToL1DefaultStreamConvertor(text)).readXBStream(input);
            checker.matchEndXB();
            checker.matchEndXB();
            message.close();
            return text.getValue();
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBRXDesc.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public ImageIcon getFileAsImageIcon(XBCXFile iconFile) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(DATA_FILE_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(iconFile.getId()));
            listener.endXB();
            XBMatchingProvider checker = message.getXBInput();
            checker.matchAttribXB(new UBNat32(1));
            checker.matchBeginXB();
            InputStream istream = checker.matchDataXB();
            ByteArrayOutputStream data = new ByteArrayOutputStream();
            StreamUtils.copyInputStreamToOutputStream(istream, data);
            checker.matchEndXB();
            message.close();
            return new ImageIcon(data.toByteArray()); // TODO: Is there better to convert InputStream?
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPXFileStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public InputStream getFile(XBCXFile iconFile) {
        try {
            XBCatalogServiceMessage message = client.executeProcedure(DATA_FILE_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(iconFile.getId()));
            listener.endXB();
            XBMatchingProvider checker = message.getXBInput();
            checker.matchAttribXB(new UBNat32(1));
            checker.matchBeginXB();
            InputStream istream = checker.matchDataXB();
            ByteArrayOutputStream data = new ByteArrayOutputStream();
            StreamUtils.copyInputStreamToOutputStream(istream, data);
            checker.matchEndXB();
            message.close();
            return new ByteArrayInputStream(data.toByteArray()); // TODO: Is there better to convert InputStream?
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPXFileStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public XBRXFile createItem() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void persistItem(XBRXFile item) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void removeItem(XBRXFile item) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public XBRXFile getItem(long itemId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List<XBRXFile> getAllItems() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public long getItemsCount() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
