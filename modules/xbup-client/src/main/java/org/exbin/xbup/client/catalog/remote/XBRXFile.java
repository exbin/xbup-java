/*
 * Copyright (C) ExBin Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.exbin.xbup.client.catalog.remote;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.exbin.xbup.client.XBCatalogServiceClient;
import org.exbin.xbup.client.stub.XBPXFileStub;
import org.exbin.xbup.core.catalog.base.XBCXFile;
import org.exbin.xbup.core.util.StreamUtils;

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
    public long getId() {
        return id;
    }
}
