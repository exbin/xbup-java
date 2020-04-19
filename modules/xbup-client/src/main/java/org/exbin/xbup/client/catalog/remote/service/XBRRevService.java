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

import java.util.List;
import org.exbin.xbup.client.catalog.XBRCatalog;
import org.exbin.xbup.client.catalog.remote.XBRBlockRev;
import org.exbin.xbup.client.catalog.remote.XBRFormatRev;
import org.exbin.xbup.client.catalog.remote.XBRGroupRev;
import org.exbin.xbup.client.catalog.remote.XBRRev;
import org.exbin.xbup.client.catalog.remote.manager.XBRRevManager;
import org.exbin.xbup.core.catalog.base.XBCBlockSpec;
import org.exbin.xbup.core.catalog.base.XBCFormatSpec;
import org.exbin.xbup.core.catalog.base.XBCGroupSpec;
import org.exbin.xbup.core.catalog.base.XBCRev;
import org.exbin.xbup.core.catalog.base.XBCSpec;
import org.exbin.xbup.core.catalog.base.XBCXDesc;
import org.exbin.xbup.core.catalog.base.XBCXHDoc;
import org.exbin.xbup.core.catalog.base.XBCXName;
import org.exbin.xbup.core.catalog.base.XBCXStri;
import org.exbin.xbup.core.catalog.base.manager.XBCRevManager;
import org.exbin.xbup.core.catalog.base.manager.XBCXDescManager;
import org.exbin.xbup.core.catalog.base.manager.XBCXHDocManager;
import org.exbin.xbup.core.catalog.base.manager.XBCXNameManager;
import org.exbin.xbup.core.catalog.base.manager.XBCXStriManager;
import org.exbin.xbup.core.catalog.base.service.XBCRevService;
import org.exbin.xbup.core.catalog.base.service.XBCSpecService;

/**
 * Remote service for XBRRev items.
 *
 * @version 0.1.25 2015/03/19
 * @author ExBin Project (http://exbin.org)
 */
public class XBRRevService extends XBRDefaultService<XBRRev> implements XBCRevService<XBRRev> {

    public XBRRevService(XBRCatalog catalog) {
        super(catalog);
        itemManager = new XBRRevManager(catalog);
        catalog.addCatalogManager(XBCRevManager.class, (XBCRevManager) itemManager);
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
    public long getRevsCount(XBCSpec spec) {
        return ((XBRRevManager) itemManager).getRevsCount(spec);
    }

    @Override
    public void removeItemDepth(XBCRev rev) {
        XBCXNameManager nameManager = catalog.getCatalogManager(XBCXNameManager.class);
        List<XBCXName> itemNames = nameManager.getItemNames(rev);
        for (XBCXName itemName : itemNames) {
            nameManager.removeItem(itemName);
        }

        XBCXDescManager descManager = catalog.getCatalogManager(XBCXDescManager.class);
        List<XBCXDesc> itemDescs = descManager.getItemDescs(rev);
        for (XBCXDesc itemDesc : itemDescs) {
            descManager.removeItem(itemDesc);
        }

        XBCXStriManager striManager = catalog.getCatalogManager(XBCXStriManager.class);
        XBCXStri itemStri = striManager.getItemStringId(rev);
        if (itemStri != null) {
            striManager.removeItem(itemStri);
        }

        XBCXHDocManager hdocManager = catalog.getCatalogManager(XBCXHDocManager.class);
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

    @Override
    public long getRevsLimitSum(XBCSpec spec, long revision) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public long findMaxRevXB(XBCSpec spec) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
