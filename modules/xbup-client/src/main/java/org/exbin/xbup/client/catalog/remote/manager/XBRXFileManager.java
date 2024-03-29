/*
 * Copyright (C) ExBin Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.exbin.xbup.client.catalog.remote.manager;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.ImageIcon;
import org.exbin.xbup.client.catalog.XBRCatalog;
import org.exbin.xbup.client.catalog.remote.XBRXFile;
import org.exbin.xbup.client.stub.XBPXFileStub;
import org.exbin.xbup.core.catalog.base.XBCNode;
import org.exbin.xbup.core.catalog.base.XBCXFile;
import org.exbin.xbup.core.catalog.base.manager.XBCXFileManager;

/**
 * Remote manager class for XBRXFile catalog items.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XBRXFileManager extends XBRDefaultManager<XBCXFile> implements XBCXFileManager {

    private final XBPXFileStub fileStub;

    public XBRXFileManager(XBRCatalog catalog) {
        super(catalog);
        fileStub = new XBPXFileStub(client);
        setManagerStub(fileStub);
    }

    @Override
    public Long[] getFileXBPath(XBCXFile file) {
        ArrayList<Long> list = new ArrayList<>();
        XBCNode parent = file.getNode();
        while (parent != null) {
            if (parent.getParent().isPresent()) {
                list.add(0, parent.getXBIndex());
            }
            parent = (XBCNode) parent.getParent().orElse(null);
        }
        list.add(file.getId());
        return (Long[]) list.toArray(new Long[list.size()]);
    }

    @Override
    public ImageIcon getFileAsImageIcon(XBCXFile iconFile) {
        return fileStub.getFileAsImageIcon(iconFile);
    }

    @Override
    public InputStream getFile(XBCXFile iconFile) {
        return fileStub.getFile(iconFile);
    }

    @Override
    public XBRXFile findFile(XBCNode node, String fileName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void initializeExtension() {
    }

    @Override
    public String getExtensionName() {
        return "File Repository Extension";
    }

    @Override
    public List<XBCXFile> findFilesForNode(XBCNode node) {
        return fileStub.findFilesForNode(node);
    }
}
