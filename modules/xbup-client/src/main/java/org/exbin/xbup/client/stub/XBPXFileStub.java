/*
 * Copyright (C) ExBin Project
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
package org.exbin.xbup.client.stub;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import org.exbin.xbup.client.XBCatalogServiceClient;
import org.exbin.xbup.client.catalog.remote.XBRNode;
import org.exbin.xbup.client.catalog.remote.XBRXFile;
import org.exbin.xbup.core.block.declaration.XBDeclBlockType;
import org.exbin.xbup.core.catalog.base.XBCNode;
import org.exbin.xbup.core.catalog.base.XBCXFile;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.remote.XBCallHandler;
import org.exbin.xbup.core.serial.param.XBPListenerSerialHandler;
import org.exbin.xbup.core.serial.param.XBPProviderSerialHandler;
import org.exbin.xbup.core.type.XBData;
import org.exbin.xbup.core.util.StreamUtils;

/**
 * RPC stub class for XBRXFile catalog items.
 *
 * @version 0.1.25 2015/03/25
 * @author ExBin Project (http://exbin.org)
 */
public class XBPXFileStub extends XBPBaseStub<XBRXFile> {

    public static long[] OWNER_FILE_PROCEDURE = {0, 2, 12, 0, 0};
    public static long[] FILENAME_FILE_PROCEDURE = {0, 2, 12, 1, 0};
    public static long[] DATA_FILE_PROCEDURE = {0, 2, 12, 2, 0};
    public static long[] NODEFILES_FILE_PROCEDURE = {0, 2, 12, 3, 0};

    private final XBCatalogServiceClient client;

    public XBPXFileStub(XBCatalogServiceClient client) {
        super(client, new XBPConstructorMethod<XBRXFile>() {
            @Override
            public XBRXFile itemConstructor(XBCatalogServiceClient client, long itemId) {
                return new XBRXFile(client, itemId);
            }
        }, null);
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

    public InputStream getFile(XBCXFile file) {
        try {
            XBCallHandler procedureCall = client.procedureCall();

            XBPListenerSerialHandler serialInput = new XBPListenerSerialHandler(procedureCall.getParametersInput());
            serialInput.begin();
            serialInput.putType(new XBDeclBlockType(DATA_FILE_PROCEDURE));
            serialInput.putAttribute(file.getId());
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

    public List<XBCXFile> findFilesForNode(XBCNode node) {
        try {
            XBCallHandler procedureCall = client.procedureCall();

            XBPListenerSerialHandler serialInput = new XBPListenerSerialHandler(procedureCall.getParametersInput());
            serialInput.begin();
            serialInput.putType(new XBDeclBlockType(NODEFILES_FILE_PROCEDURE));
            serialInput.putAttribute(node.getId());
            serialInput.end();

            XBPProviderSerialHandler serialOutput = new XBPProviderSerialHandler(procedureCall.getResultOutput());
            if (!serialOutput.pullIfEmptyBlock()) {
                serialOutput.begin();
                serialOutput.matchType();
                long count = serialOutput.pullLongAttribute();
                List<XBCXFile> result = new ArrayList<>();
                for (int i = 0; i < count; i++) {
                    result.add(new XBRXFile(client, serialOutput.pullLongAttribute()));
                }
                serialOutput.end();
                procedureCall.execute();
                return result;
            }
            procedureCall.execute();

            return null;
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBPItemStub.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
