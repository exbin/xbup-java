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
package org.exbin.xbup.client.catalog.remote.service;

import java.io.InputStream;
import java.util.List;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.swing.ImageIcon;
import org.exbin.xbup.client.catalog.XBRCatalog;
import org.exbin.xbup.client.catalog.remote.XBRXFile;
import org.exbin.xbup.client.catalog.remote.manager.XBRXFileManager;
import org.exbin.xbup.core.catalog.base.XBCNode;
import org.exbin.xbup.core.catalog.base.XBCXFile;
import org.exbin.xbup.core.catalog.base.manager.XBCXFileManager;
import org.exbin.xbup.core.catalog.base.service.XBCXFileService;

/**
 * Remote service for XBRXFile items.
 *
 * @version 0.2.1 2020/08/26
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XBRXFileService extends XBRDefaultService<XBRXFile> implements XBCXFileService<XBRXFile> {

    public XBRXFileService(XBRCatalog catalog) {
        super(catalog);
        itemManager = new XBRXFileManager(catalog);
        catalog.addCatalogManager(XBCXFileManager.class, (XBCXFileManager) itemManager);
    }

    @Override
    public XBRXFile findFile(XBCNode node, String fileName) {
        return ((XBRXFileManager) itemManager).findFile(node, fileName);
    }

    @Override
    public InputStream getFile(XBCXFile file) {
        return ((XBRXFileManager) itemManager).getFile(file);
    }

    @Override
    public ImageIcon getFileAsImageIcon(XBCXFile file) {
        return ((XBRXFileManager) itemManager).getFileAsImageIcon(file);
    }

    @Override
    public Long[] getFileXBPath(XBCXFile file) {
        return ((XBRXFileManager) itemManager).getFileXBPath(file);
    }

    @Override
    public String getExtensionName() {
        return ((XBRXFileManager) itemManager).getExtensionName();
    }

    @Override
    public void initializeExtension() {
        ((XBRXFileManager) itemManager).initializeExtension();
    }

    @Override
    public List<XBCXFile> findFilesForNode(XBCNode node) {
        return ((XBRXFileManager) itemManager).findFilesForNode(node);
    }
}
