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
package org.xbup.lib.client.catalog.remote;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xbup.lib.client.XBCatalogServiceClient;
import org.xbup.lib.client.stub.XBPXFileStub;
import org.xbup.lib.core.catalog.base.XBCXFile;
import org.xbup.lib.core.util.StreamUtils;

/**
 * Catalog remote file entity.
 *
 * @version 0.1.25 2015/03/25
 * @author ExBin Project (http://exbin.org)
 */
public class XBRXFile implements XBCXFile {

    private final long id;
    protected XBCatalogServiceClient client;
    private final XBPXFileStub fileStub;

    public XBRXFile(XBCatalogServiceClient client, long id) {
        this.id = id;
        this.client = client;
        fileStub = new XBPXFileStub(client);
    }

    @Override
    public XBRNode getNode() {
        return fileStub.getNode(id);
    }

    @Override
    public String getFilename() {
        return fileStub.getFilename(id);
    }

    @Override
    public byte[] getContent() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            StreamUtils.copyInputStreamToOutputStream(fileStub.getFile(this), byteArrayOutputStream);
        } catch (IOException ex) {
            Logger.getLogger(XBRXFile.class.getName()).log(Level.SEVERE, null, ex);
        }
        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public Long getId() {
        return id;
    }
}
