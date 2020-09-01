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
package org.exbin.xbup.client.catalog.remote.service;

import java.util.List;
import javax.annotation.ParametersAreNonnullByDefault;
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

/**
 * Remote service for XBRRev items.
 *
 * @version 0.2.1 2020/08/26
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
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

        ((XBCRevService) this).removeItem(rev);
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
