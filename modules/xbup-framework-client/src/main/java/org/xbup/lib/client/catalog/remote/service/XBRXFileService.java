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
package org.xbup.lib.client.catalog.remote.service;

import java.io.InputStream;
import java.util.List;
import javax.swing.ImageIcon;
import org.xbup.lib.client.catalog.XBRCatalog;
import org.xbup.lib.core.catalog.base.XBCNode;
import org.xbup.lib.core.catalog.base.XBCXFile;
import org.xbup.lib.core.catalog.base.manager.XBCXFileManager;
import org.xbup.lib.core.catalog.base.service.XBCXFileService;
import org.xbup.lib.core.catalog.base.XBCExtension;
import org.xbup.lib.client.catalog.remote.XBRXFile;
import org.xbup.lib.client.catalog.remote.manager.XBRXFileManager;

/**
 * Interface for XBRXFile items service.
 *
 * @version 0.1.22 2013/07/28
 * @author XBUP Project (http://xbup.org)
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
        return ((XBCExtension) itemManager).getExtensionName();
    }

    @Override
    public void initializeExtension() {
        ((XBCExtension) itemManager).initializeExtension();
    }

    @Override
    public List<XBCXFile> findFilesForNode(XBCNode node) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
