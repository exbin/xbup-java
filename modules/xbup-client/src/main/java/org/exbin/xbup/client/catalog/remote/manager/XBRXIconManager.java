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
package org.exbin.xbup.client.catalog.remote.manager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import org.exbin.xbup.client.catalog.XBRCatalog;
import org.exbin.xbup.client.catalog.remote.XBRXIcon;
import org.exbin.xbup.client.catalog.remote.XBRXIconMode;
import org.exbin.xbup.client.stub.XBPXIconStub;
import org.exbin.xbup.core.catalog.base.XBCBlockSpec;
import org.exbin.xbup.core.catalog.base.XBCItem;
import org.exbin.xbup.core.catalog.base.XBCNode;
import org.exbin.xbup.core.catalog.base.XBCXFile;
import org.exbin.xbup.core.catalog.base.XBCXIcon;
import org.exbin.xbup.core.catalog.base.manager.XBCXFileManager;
import org.exbin.xbup.core.catalog.base.manager.XBCXIconManager;
import org.exbin.xbup.core.util.StreamUtils;

/**
 * Remote manager class for XBRXIcon catalog items.
 *
 * @version 0.1.25 2015/09/06
 * @author ExBin Project (http://exbin.org)
 */
public class XBRXIconManager extends XBRDefaultManager<XBRXIcon> implements XBCXIconManager<XBRXIcon> {

    private final XBPXIconStub iconStub;

    public XBRXIconManager(XBRCatalog catalog) {
        super(catalog);
        iconStub = new XBPXIconStub(client);
        setManagerStub(iconStub);
    }

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
    public XBRXIcon getDefaultIcon(XBCItem item) {
        return iconStub.getDefaultIcon(item);
    }

    @Override
    public List<XBCXIcon> getBlockSpecIcons(XBCBlockSpec icon) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public XBRXIconMode getIconMode(Long type) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public XBCXIcon findById(Long id) {
        return new XBRXIcon(client, id);
    }

    @Override
    public void initializeExtension() {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getExtensionName() {
        return "Icon Extension";
    }

    @Override
    public ImageIcon getDefaultImageIcon(XBCItem item) {
        XBRXFileManager fileManager = (XBRXFileManager) catalog.getCatalogManager(XBCXFileManager.class);
        XBCXIcon icon = getDefaultIcon(item);
        if (icon == null) {
            return null;
        }
        XBCXFile file = icon.getIconFile();
        if (file == null) {
            return null;
        }
        // TODO: This is ugly copy, catalog instance should be passed?
        return fileManager.getFileAsImageIcon(file);
    }

    @Override
    public XBCXIcon getDefaultBigIcon(XBCItem item) {
        return iconStub.getDefaultBigIcon(item);
    }

    @Override
    public XBCXIcon getDefaultSmallIcon(XBCItem item) {
        return iconStub.getDefaultSmallIcon(item);
    }

    @Override
    public byte[] getDefaultBigIconData(XBCItem item) {
        XBRXFileManager fileManager = (XBRXFileManager) catalog.getCatalogManager(XBCXFileManager.class);
        XBCXIcon icon = getDefaultBigIcon(item);
        if (icon == null) {
            return null;
        }
        XBCXFile file = icon.getIconFile();
        if (file == null) {
            return null;
        }

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try {
            StreamUtils.copyInputStreamToOutputStream(fileManager.getFile(file), buffer);
        } catch (IOException ex) {
            Logger.getLogger(XBRXIconManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        return buffer.toByteArray();
    }

    @Override
    public byte[] getDefaultSmallIconData(XBCItem item) {
        XBRXFileManager fileManager = (XBRXFileManager) catalog.getCatalogManager(XBCXFileManager.class);
        XBCXIcon icon = getDefaultSmallIcon(item);
        if (icon == null) {
            return null;
        }
        XBCXFile file = icon.getIconFile();
        if (file == null) {
            return null;
        }

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try {
            StreamUtils.copyInputStreamToOutputStream(fileManager.getFile(file), buffer);
        } catch (IOException ex) {
            Logger.getLogger(XBRXIconManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        return buffer.toByteArray();
    }
}
