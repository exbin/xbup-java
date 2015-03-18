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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import org.xbup.lib.client.XBCatalogServiceClient;
import org.xbup.lib.client.catalog.remote.XBRNode;
import org.xbup.lib.client.catalog.remote.XBRXFile;
import org.xbup.lib.core.block.declaration.XBDeclBlockType;
import org.xbup.lib.core.catalog.base.XBCNode;
import org.xbup.lib.core.catalog.base.XBCXFile;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.remote.XBCallHandler;
import org.xbup.lib.core.serial.param.XBPListenerSerialHandler;
import org.xbup.lib.core.serial.param.XBPProviderSerialHandler;
import org.xbup.lib.core.type.XBData;
import org.xbup.lib.core.util.StreamUtils;

/**
 * RPC stub class for XBRXFile catalog items.
 *
 * @version 0.1.25 2015/03/15
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
        Long index = XBPStubUtils.longToLongMethod(client.procedureCall(), new XBDeclBlockType(OWNER_FILE_PROCEDURE), fileId);
        return index == null ? null : new XBRNode(client, index);
    }

    public String getFilename(long fileId) {
        return XBPStubUtils.longToStringMethod(client.procedureCall(), new XBDeclBlockType(FILENAME_FILE_PROCEDURE), fileId);
    }

    public ImageIcon getFileAsImageIcon(XBCXFile iconFile) {
        try {
            XBCallHandler procedureCall = client.procedureCall();

            XBPListenerSerialHandler serialInput = new XBPListenerSerialHandler(procedureCall.getParametersInput());
            serialInput.begin();
            serialInput.putType(new XBDeclBlockType(DATA_FILE_PROCEDURE));
            serialInput.putAttribute(iconFile.getId());
            serialInput.end();

            XBPProviderSerialHandler serialOutput = new XBPProviderSerialHandler(procedureCall.getResultOutput());
            serialOutput.begin();
            ByteArrayOutputStream data = new ByteArrayOutputStream();
            StreamUtils.copyInputStreamToOutputStream(serialOutput.pullData(), data);
            serialOutput.end();
            procedureCall.execute();
            return new ImageIcon(data.toByteArray());
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPItemStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public InputStream getFile(XBCXFile iconFile) {
        try {
            XBCallHandler procedureCall = client.procedureCall();

            XBPListenerSerialHandler serialInput = new XBPListenerSerialHandler(procedureCall.getParametersInput());
            serialInput.begin();
            serialInput.putType(new XBDeclBlockType(DATA_FILE_PROCEDURE));
            serialInput.putAttribute(iconFile.getId());
            serialInput.end();

            XBPProviderSerialHandler serialOutput = new XBPProviderSerialHandler(procedureCall.getResultOutput());
            XBData data = new XBData();
            serialOutput.process(data);
            procedureCall.execute();
            return data.getDataInputStream();
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPItemStub.class.getName()).log(Level.SEVERE, null, ex);
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

    public List<XBCXFile> findFilesForNode(XBCNode node) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
