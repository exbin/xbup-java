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
package org.exbin.xbup.catalog.entity.service;

import java.io.Serializable;
import java.util.List;
import javax.annotation.ParametersAreNonnullByDefault;
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
 * @version 0.2.1 2020/08/26
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
@Service
public class XBERevService extends XBEDefaultService<XBCRev> implements XBCRevService, Serializable {

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
        Long findMaxRevXB = ((XBERevManager) itemManager).findMaxRevXB(spec);
        return findMaxRevXB == null ? -1 : findMaxRevXB;
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
            XBEBlockRev rev = new XBEBlockRev();
            rev.setParent(spec);
            return rev;
        } else if (spec instanceof XBCGroupSpec) {
            XBEGroupRev rev = new XBEGroupRev();
            rev.setParent(spec);
            return rev;
        } else if (spec instanceof XBCFormatSpec) {
            XBEFormatRev rev = new XBEFormatRev();
            rev.setParent(spec);
            return rev;
        }

        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public long getRevsLimitSum(XBCSpec spec, long revision) {
        return ((XBERevManager) itemManager).getRevsLimitSum(spec, revision);
    }
}
