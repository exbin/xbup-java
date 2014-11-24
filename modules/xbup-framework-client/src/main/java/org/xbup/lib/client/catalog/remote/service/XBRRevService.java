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

import java.util.List;
import org.xbup.lib.client.catalog.XBRCatalog;
import org.xbup.lib.core.catalog.base.XBCBlockSpec;
import org.xbup.lib.core.catalog.base.XBCFormatSpec;
import org.xbup.lib.core.catalog.base.XBCGroupSpec;
import org.xbup.lib.core.catalog.base.XBCRev;
import org.xbup.lib.core.catalog.base.XBCSpec;
import org.xbup.lib.core.catalog.base.XBCXDesc;
import org.xbup.lib.core.catalog.base.XBCXHDoc;
import org.xbup.lib.core.catalog.base.XBCXName;
import org.xbup.lib.core.catalog.base.XBCXStri;
import org.xbup.lib.core.catalog.base.manager.XBCRevManager;
import org.xbup.lib.core.catalog.base.manager.XBCXDescManager;
import org.xbup.lib.core.catalog.base.manager.XBCXHDocManager;
import org.xbup.lib.core.catalog.base.manager.XBCXNameManager;
import org.xbup.lib.core.catalog.base.manager.XBCXStriManager;
import org.xbup.lib.core.catalog.base.service.XBCRevService;
import org.xbup.lib.core.catalog.base.service.XBCSpecService;
import org.xbup.lib.client.catalog.remote.XBRBlockRev;
import org.xbup.lib.client.catalog.remote.XBRFormatRev;
import org.xbup.lib.client.catalog.remote.XBRGroupRev;
import org.xbup.lib.client.catalog.remote.XBRRev;
import org.xbup.lib.client.catalog.remote.manager.XBRRevManager;

/**
 * Interface for XBRRev items service.
 *
 * @version 0.1.24 2014/11/20
 * @author XBUP Project (http://xbup.org)
 */
public class XBRRevService extends XBRDefaultService<XBRRev> implements XBCRevService<XBRRev> {

    public XBRRevService(XBRCatalog catalog) {
        super(catalog);
        itemManager = new XBRRevManager(catalog);
        catalog.addCatalogManager(XBCRevManager.class, itemManager);
    }

    @Override
    public XBRRev findRevById(long id) {
        return ((XBRRevManager) itemManager).findRevById(id);
    }

    @Override
    public XBRRev findRevByXB(XBCSpec spec, long xbIndex) {
        return ((XBRRevManager) itemManager).findRevByXB(spec, xbIndex);
    }

    @Override
    public XBRRev getRev(XBCSpec spec, long index) {
        return ((XBRRevManager) itemManager).getRev(spec, index);
    }

    @Override
    public List<XBCRev> getRevs(XBCSpec spec) {
        return ((XBRRevManager) itemManager).getRevs(spec);
    }

    @Override
    public Long getAllRevisionsCount() {
        return ((XBRRevManager) itemManager).getAllRevisionsCount();
    }

    @Override
    public long getRevsCount(XBCSpec spec) {
        return ((XBRRevManager) itemManager).getRevsCount(spec);
    }

    @Override
    public void removeItemDepth(XBCRev rev) {
        XBCXNameManager nameManager = (XBCXNameManager) catalog.getCatalogManager(XBCXNameManager.class);
        List<XBCXName> itemNames = nameManager.getItemNames(rev);
        for (XBCXName itemName : itemNames) {
            nameManager.removeItem(itemName);
        }

        XBCXDescManager descManager = (XBCXDescManager) catalog.getCatalogManager(XBCXDescManager.class);
        List<XBCXDesc> itemDescs = descManager.getItemDescs(rev);
        for (XBCXDesc itemDesc : itemDescs) {
            descManager.removeItem(itemDesc);
        }

        XBCXStriManager striManager = (XBCXStriManager) catalog.getCatalogManager(XBCXStriManager.class);
        XBCXStri itemStri = striManager.getItemStringId(rev);
        if (itemStri != null) {
            striManager.removeItem(itemStri);
        }

        XBCXHDocManager hdocManager = (XBCXHDocManager) catalog.getCatalogManager(XBCXHDocManager.class);
        XBCXHDoc itemHDoc = hdocManager.getDocumentation(rev);
        if (itemHDoc != null) {
            hdocManager.removeItem(itemHDoc);
        }

        ((XBCSpecService) this).removeItem(rev);
    }

    @Override
    public XBCRev createRev(XBCSpec spec) {
        if (spec instanceof XBCBlockSpec) {
            return new XBRBlockRev(catalog.getCatalogServiceClient(), 0);
        } else if (spec instanceof XBCGroupSpec) {
            return new XBRGroupRev(catalog.getCatalogServiceClient(), 0);
        } else if (spec instanceof XBCFormatSpec) {
            return new XBRFormatRev(catalog.getCatalogServiceClient(), 0);
        }

        throw new UnsupportedOperationException("Not supported yet.");
    }
}
