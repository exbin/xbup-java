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
package org.xbup.lib.client.catalog.remote.manager;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import org.xbup.lib.client.catalog.XBRCatalog;
import org.xbup.lib.core.catalog.base.XBCNode;
import org.xbup.lib.core.catalog.base.XBCXFile;
import org.xbup.lib.core.catalog.base.manager.XBCXFileManager;
import org.xbup.lib.client.catalog.remote.XBRXFile;
import org.xbup.lib.client.stub.XBPXFileStub;

/**
 * Remote manager class for XBRXFile catalog items.
 *
 * @version 0.1.25 2015/03/18
 * @author XBUP Project (http://xbup.org)
 */
public class XBRXFileManager extends XBRDefaultManager<XBRXFile> implements XBCXFileManager<XBRXFile> {

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
            if (parent.getParent() != null) {
                if (parent.getXBIndex() == null) {
                    return null;
                }
                list.add(0, parent.getXBIndex());
            }
            parent = (XBCNode) parent.getParent();
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
