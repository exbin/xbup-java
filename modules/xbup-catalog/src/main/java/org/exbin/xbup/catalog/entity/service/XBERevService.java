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
package org.exbin.xbup.catalog.entity.service;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import org.exbin.xbup.catalog.XBECatalog;
import org.exbin.xbup.catalog.entity.XBEBlockRev;
import org.exbin.xbup.catalog.entity.XBEFormatRev;
import org.exbin.xbup.catalog.entity.XBEGroupRev;
import org.exbin.xbup.catalog.entity.XBERev;
import org.exbin.xbup.catalog.entity.manager.XBERevManager;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Interface for XBERev items service.
 *
 * @version 0.2.1 2017/05/27
 * @author ExBin Project (http://exbin.org)
 */
@Service
public class XBERevService extends XBEDefaultService<XBERev> implements XBCRevService<XBERev>, Serializable {

    @Autowired
    private XBERevManager manager;

    public XBERevService() {
        super();
    }

    public XBERevService(XBECatalog catalog) {
        super(catalog);
        itemManager = new XBERevManager(catalog);
        catalog.addCatalogManager(XBCRevManager.class, (XBCRevManager) itemManager);
    }

    @PostConstruct
    public void init() {
        itemManager = manager;
    }

    @Override
    public XBERev findRevByXB(XBCSpec spec, long xbIndex) {
        return ((XBERevManager) itemManager).findRevByXB(spec, xbIndex);
    }

    @Override
    public XBERev getRev(XBCSpec spec, long index) {
        return ((XBERevManager) itemManager).getRev(spec, index);
    }

    @Override
    public List<XBCRev> getRevs(XBCSpec spec) {
        return ((XBERevManager) itemManager).getRevs(spec);
    }

    @Override
    public long getRevsCount(XBCSpec spec) {
        return ((XBERevManager) itemManager).getRevsCount(spec);
    }

    @Override
    public long findMaxRevXB(XBCSpec spec) {
        return ((XBERevManager) itemManager).findMaxRevXB(spec);
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
            return new XBEBlockRev();
        } else if (spec instanceof XBCGroupSpec) {
            return new XBEGroupRev();
        } else if (spec instanceof XBCFormatSpec) {
            return new XBEFormatRev();
        }

        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public long getRevsLimitSum(XBCSpec spec, long revision) {
        return ((XBERevManager) itemManager).getRevsLimitSum(spec, revision);
    }
}
