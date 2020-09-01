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
package org.exbin.xbup.catalog.entity.service;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.List;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.PostConstruct;
import javax.swing.ImageIcon;
import org.exbin.xbup.catalog.XBECatalog;
import org.exbin.xbup.catalog.entity.XBEXFile;
import org.exbin.xbup.catalog.entity.manager.XBEXFileManager;
import org.exbin.xbup.core.catalog.base.XBCExtension;
import org.exbin.xbup.core.catalog.base.XBCNode;
import org.exbin.xbup.core.catalog.base.XBCXFile;
import org.exbin.xbup.core.catalog.base.manager.XBCXFileManager;
import org.exbin.xbup.core.catalog.base.service.XBCXFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Interface for XBEXFile items service.
 *
 * @version 0.2.1 2020/08/26
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
@Service
public class XBEXFileService extends XBEDefaultService<XBEXFile> implements XBCXFileService<XBEXFile>, Serializable {

    @Autowired
    private XBEXFileManager manager;

    public XBEXFileService() {
        super();
    }

    public XBEXFileService(XBECatalog catalog) {
        super(catalog);
        itemManager = new XBEXFileManager(catalog);
        catalog.addCatalogManager(XBCXFileManager.class, (XBCXFileManager) itemManager);
    }

    @PostConstruct
    public void init() {
        itemManager = manager;
    }

    @Override
    public XBEXFile findFile(XBCNode node, String fileName) {
        return ((XBEXFileManager) itemManager).findFile(node, fileName);
    }

    @Override
    public InputStream getFile(XBCXFile file) {
        return ((XBEXFileManager) itemManager).getFile(file);
    }

    @Override
    public ImageIcon getFileAsImageIcon(XBCXFile file) {
        return ((XBEXFileManager) itemManager).getFileAsImageIcon(file);
    }

    @Override
    public Long[] getFileXBPath(XBCXFile file) {
        return ((XBEXFileManager) itemManager).getFileXBPath(file);
    }

    @Override
    public String getExtensionName() {
        return ((XBCExtension) itemManager).getExtensionName();
    }

    @Override
    public void initializeExtension() {
        ((XBCExtension) itemManager).initializeExtension();
    }

    public OutputStream setFile(XBCXFile file) {
        return ((XBEXFileManager) itemManager).setFile(file);
    }

    @Override
    public List<XBCXFile> findFilesForNode(XBCNode node) {
        return ((XBEXFileManager) itemManager).findFilesForNode(node);
    }
}
