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
