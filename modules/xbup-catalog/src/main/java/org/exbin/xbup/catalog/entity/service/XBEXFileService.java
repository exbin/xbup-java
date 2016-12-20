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
package org.exbin.xbup.catalog.entity.service;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.List;
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
 * @version 0.1.22 2013/07/28
 * @author ExBin Project (http://exbin.org)
 */
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
        catalog.addCatalogManager(XBCXFileManager.class, itemManager);
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
