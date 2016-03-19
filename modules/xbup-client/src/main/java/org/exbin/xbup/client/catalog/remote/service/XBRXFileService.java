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
package org.exbin.xbup.client.catalog.remote.service;

import java.io.InputStream;
import java.util.List;
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
 * @version 0.1.25 2015/03/19
 * @author ExBin Project (http://exbin.org)
 */
public class XBRXFileService extends XBRDefaultService<XBRXFile> implements XBCXFileService<XBRXFile> {

    public XBRXFileService(XBRCatalog catalog) {
        super(catalog);
        itemManager = new XBRXFileManager(catalog);
        catalog.addCatalogManager(XBCXFileManager.class, itemManager);
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
