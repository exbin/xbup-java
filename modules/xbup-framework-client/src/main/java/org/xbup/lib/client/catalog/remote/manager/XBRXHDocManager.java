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
import org.xbup.lib.client.catalog.XBRCatalog;
import org.xbup.lib.core.catalog.base.XBCBlockSpec;
import org.xbup.lib.core.catalog.base.XBCItem;
import org.xbup.lib.core.catalog.base.XBCNode;
import org.xbup.lib.core.catalog.base.XBCXFile;
import org.xbup.lib.core.catalog.base.XBCXHDoc;
import org.xbup.lib.core.catalog.base.XBCXIcon;
import org.xbup.lib.core.catalog.base.XBCXIconMode;
import org.xbup.lib.core.catalog.base.manager.XBCXFileManager;
import org.xbup.lib.core.catalog.base.manager.XBCXHDocManager;
import org.xbup.lib.client.catalog.remote.XBRXHDoc;

/**
 * Manager class for XBRXHDoc catalog items.
 *
 * @version 0.1.21 2011/12/31
 * @author XBUP Project (http://xbup.org)
 */
public class XBRXHDocManager extends XBRDefaultManager<XBRXHDoc> implements XBCXHDocManager<XBRXHDoc> {

    private XBCXFileManager fileManager = null;

    public XBRXHDocManager(XBRCatalog catalog) {
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
        ArrayList<Long> list = new ArrayList<Long>();
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

    public List<XBCXIcon> getBlockSpecIcons(XBCBlockSpec icon) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public XBCXIconMode getIconMode(Long type) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public XBRXHDoc findById(Long id) {
        return new XBRXHDoc(client, id);
    }

    @Override
    public void initializeExtension() {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getExtensionName() {
        return "HDoc Extension";
    }

    public InputStream getDocumentData(XBCItem item) {
        XBCXHDoc doc = getDocumentation(item);
        if (doc == null) {
            return null;
        }
        XBCXFile file = doc.getDocFile();
        if (file == null) {
            return null;
        }
        // TODO: This is ugly copy, catalog instance should be passed?
        if (fileManager == null) {
            fileManager = new XBRXFileManager(catalog);
        }
        return fileManager.getFile(file);
    }

    @Override
    public XBRXHDoc getDocumentation(XBCItem item) {
        // TODO: update catalog
        /*try {
            XBCatalogServiceMessage message = client.executeProcedure(XBServiceClient.ITEM_HDOC_PROCEDURE);
            XBListener listener = message.getXBOutput();
            listener.attribXB(new UBNat32(((XBRItem) item).getId()));
            listener.endXB();
            XBStreamChecker checker = message.getXBInput();
            Long index = checker.attribXB().getNaturalLong();
            checker.endXB();
            message.close();
            if (index == 0) return null;
            return new XBRXHDoc(client, index);
        } catch (XBProcessingException ex) {
            Logger.getLogger(XBRItem.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(XBRItem.class.getName()).log(Level.SEVERE, null, ex);
        } */
        return null;
    }

    @Override
    public Long getAllHDocsCount() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
