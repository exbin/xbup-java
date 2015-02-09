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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import org.xbup.lib.client.catalog.XBRCatalog;
import org.xbup.lib.core.catalog.base.XBCBlockSpec;
import org.xbup.lib.core.catalog.base.XBCItem;
import org.xbup.lib.core.catalog.base.XBCNode;
import org.xbup.lib.core.catalog.base.XBCXFile;
import org.xbup.lib.core.catalog.base.XBCXIcon;
import org.xbup.lib.core.catalog.base.manager.XBCXFileManager;
import org.xbup.lib.core.catalog.base.manager.XBCXIconManager;
import org.xbup.lib.client.XBCatalogServiceMessage;
import org.xbup.lib.client.catalog.remote.XBRItem;
import org.xbup.lib.client.catalog.remote.XBRXIcon;
import org.xbup.lib.client.catalog.remote.XBRXIconMode;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.basic.XBListener;
import org.xbup.lib.core.remote.XBServiceClient;
import org.xbup.lib.core.stream.XBStreamChecker;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * Manager class for XBRXIcon catalog items.
 *
 * @version 0.1.24 2014/11/26
 * @author XBUP Project (http://xbup.org)
 */
public class XBRXIconManager extends XBRDefaultManager<XBRXIcon> implements XBCXIconManager<XBRXIcon> {

    public XBRXIconManager(XBRCatalog catalog) {
        super(catalog);
    }

    public Long getAllIconsCount() {
        throw new UnsupportedOperationException("Not supported yet.");
        /*        try {
         XBCatalogServiceMessage message = client.executeProcedure(XBServiceClient.SPECSCOUNT_SPEC_PROCEDURE);
         XBListener listener = message.getXBOutput();
         listener.endXB();
         XBStreamChecker checker = message.getXBInput();
         Long index = checker.attribXB().getNaturalLong();
         checker.endXB();
         message.close();
         return index;
         } catch (XBProcessingException ex) {
         Logger.getLogger(XBRItem.class.getExtensionName()).log(Level.SEVERE, null, ex);
         } catch (IOException ex) {
         Logger.getLogger(XBRItem.class.getExtensionName()).log(Level.SEVERE, null, ex);
         }
         return null; */
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
        try {
            XBCatalogServiceMessage message = client.executeProcedure(XBServiceClient.DEFAULTITEM_ICON_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(((XBRItem) item).getId()));
            listener.endXB();
            XBStreamChecker checker = message.getXBInput();
            Long index = checker.attribXB().getNaturalLong();
            checker.endXB();
            message.close();
            if (index == 0) {
                return null;
            }
            return new XBRXIcon(client, index);
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBRItem.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
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
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public XBCXIcon getDefaultSmallIcon(XBCItem item) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public byte[] getDefaultBigIconData(XBCItem item) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public byte[] getDefaultSmallIconData(XBCItem item) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
