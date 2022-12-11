/*
 * Copyright (C) ExBin Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
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
import org.exbin.xbup.client.catalog.remote.XBRBlockSpec;
import org.exbin.xbup.client.catalog.remote.XBRFormatSpec;
import org.exbin.xbup.client.catalog.remote.XBRGroupSpec;
import org.exbin.xbup.client.catalog.remote.XBRSpec;
import org.exbin.xbup.client.catalog.remote.XBRSpecDef;
import org.exbin.xbup.client.catalog.remote.manager.XBRSpecManager;
import org.exbin.xbup.core.block.declaration.catalog.XBCBlockDecl;
import org.exbin.xbup.core.block.declaration.catalog.XBCFormatDecl;
import org.exbin.xbup.core.block.declaration.catalog.XBCGroupDecl;
import org.exbin.xbup.core.block.declaration.local.XBLBlockDecl;
import org.exbin.xbup.core.block.declaration.local.XBLFormatDecl;
import org.exbin.xbup.core.block.declaration.local.XBLGroupDecl;
import org.exbin.xbup.core.block.definition.XBParamType;
import org.exbin.xbup.core.catalog.base.XBCBlockSpec;
import org.exbin.xbup.core.catalog.base.XBCFormatSpec;
import org.exbin.xbup.core.catalog.base.XBCGroupSpec;
import org.exbin.xbup.core.catalog.base.XBCNode;
import org.exbin.xbup.core.catalog.base.XBCSpec;
import org.exbin.xbup.core.catalog.base.XBCSpecDef;
import org.exbin.xbup.core.catalog.base.XBCXDesc;
import org.exbin.xbup.core.catalog.base.XBCXHDoc;
import org.exbin.xbup.core.catalog.base.XBCXName;
import org.exbin.xbup.core.catalog.base.XBCXStri;
import org.exbin.xbup.core.catalog.base.manager.XBCSpecManager;
import org.exbin.xbup.core.catalog.base.manager.XBCXDescManager;
import org.exbin.xbup.core.catalog.base.manager.XBCXHDocManager;
import org.exbin.xbup.core.catalog.base.manager.XBCXNameManager;
import org.exbin.xbup.core.catalog.base.manager.XBCXStriManager;
import org.exbin.xbup.core.catalog.base.service.XBCSpecService;

/**
 * Remote service for XBRSpec items.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XBRSpecService extends XBRDefaultService<XBCSpec> implements XBCSpecService {

    public XBRSpecService(XBRCatalog catalog) {
        super(catalog);
        itemManager = new XBRSpecManager(catalog);
        catalog.addCatalogManager(XBCSpecManager.class, (XBCSpecManager) itemManager);
    }

    @Override
    public XBRBlockSpec findBlockSpecByXB(XBCNode node, long xbIndex) {
        return ((XBRSpecManager) itemManager).findBlockSpecByXB(node, xbIndex);
    }

    @Override
    public XBRFormatSpec findFormatSpecByXB(XBCNode node, long xbIndex) {
        return ((XBRSpecManager) itemManager).findFormatSpecByXB(node, xbIndex);
    }

    @Override
    public XBRGroupSpec findGroupSpecByXB(XBCNode node, long xbIndex) {
        return ((XBRSpecManager) itemManager).findGroupSpecByXB(node, xbIndex);
    }

    @Override
    public Long findMaxBlockSpecXB(XBCNode node) {
        return ((XBRSpecManager) itemManager).findMaxBlockSpecXB(node);
    }

    @Override
    public Long findMaxFormatSpecXB(XBCNode node) {
        return ((XBRSpecManager) itemManager).findMaxFormatSpecXB(node);
    }

    @Override
    public Long findMaxGroupSpecXB(XBCNode node) {
        return ((XBRSpecManager) itemManager).findMaxGroupSpecXB(node);
    }

    @Override
    public Long getAllBlockSpecsCount() {
        return ((XBRSpecManager) itemManager).getAllBlockSpecsCount();
    }

    @Override
    public Long getAllFormatSpecsCount() {
        return ((XBRSpecManager) itemManager).getAllFormatSpecsCount();
    }

    @Override
    public Long getAllGroupSpecsCount() {
        return ((XBRSpecManager) itemManager).getAllGroupSpecsCount();
    }

    @Override
    public Long getAllSpecsCount() {
        return ((XBRSpecManager) itemManager).getAllSpecsCount();
    }

    @Override
    public XBRBlockSpec getBlockSpec(XBCNode node, long index) {
        return ((XBRSpecManager) itemManager).getBlockSpec(node, index);
    }

    @Override
    public List<XBCBlockSpec> getBlockSpecs(XBCNode node) {
        return ((XBRSpecManager) itemManager).getBlockSpecs(node);
    }

    @Override
    public long getBlockSpecsCount(XBCNode node) {
        return ((XBRSpecManager) itemManager).getBlockSpecsCount(node);
    }

    @Override
    public XBRFormatSpec getFormatSpec(XBCNode node, long index) {
        return ((XBRSpecManager) itemManager).getFormatSpec(node, index);
    }

    @Override
    public List<XBCFormatSpec> getFormatSpecs(XBCNode node) {
        return ((XBRSpecManager) itemManager).getFormatSpecs(node);
    }

    @Override
    public long getFormatSpecsCount(XBCNode node) {
        return ((XBRSpecManager) itemManager).getFormatSpecsCount(node);
    }

    @Override
    public XBRGroupSpec getGroupSpec(XBCNode node, long index) {
        return ((XBRSpecManager) itemManager).getGroupSpec(node, index);
    }

    @Override
    public List<XBCGroupSpec> getGroupSpecs(XBCNode node) {
        return ((XBRSpecManager) itemManager).getGroupSpecs(node);
    }

    @Override
    public long getGroupSpecsCount(XBCNode node) {
        return ((XBRSpecManager) itemManager).getGroupSpecsCount(node);
    }

    @Override
    public XBRSpec getSpecByOrder(XBCNode node, long index) {
        return ((XBRSpecManager) itemManager).getSpec(node, index);
    }

    @Override
    public Long[] getSpecXBPath(XBCSpec spec) {
        return ((XBRSpecManager) itemManager).getSpecXBPath(spec);
    }

    @Override
    public List<XBCSpec> getSpecs(XBCNode node) {
        return ((XBRSpecManager) itemManager).getSpecs(node);
    }

    @Override
    public long getSpecsCount(XBCNode node) {
        return ((XBRSpecManager) itemManager).getSpecsCount(node);
    }

    @Override
    public XBRSpecDef findSpecDefByXB(XBCSpec spec, long xbIndex) {
        return ((XBRSpecManager) itemManager).findSpecDefByXB(spec, xbIndex);
    }

    @Override
    public Long findMaxSpecDefXB(XBCSpec spec) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public XBRSpecDef getSpecDefByOrder(XBCSpec spec, long index) {
        return ((XBRSpecManager) itemManager).getSpecDefByOrder(spec, index);
    }

    @Override
    public List<XBCSpecDef> getSpecDefs(XBCSpec spec) {
        return ((XBRSpecManager) itemManager).getSpecDefs(spec);
    }

    @Override
    public long getSpecDefsCount(XBCSpec spec) {
        return ((XBRSpecManager) itemManager).getSpecDefsCount(spec);
    }

    @Override
    public long getDefsCount() {
        return ((XBRSpecManager) itemManager).getDefsCount();
    }

    @Override
    public XBCSpecDef getSpecDef(long itemId) {
        return ((XBRSpecManager) itemManager).getSpecDef(itemId);
    }

    @Override
    public XBCSpecDef createSpecDef(XBCSpec spec, XBParamType type) {
        return ((XBRSpecManager) itemManager).createSpecDef(spec, type);
    }

    @Override
    public XBCBlockSpec createBlockSpec() {
        return ((XBRSpecManager) itemManager).createBlockSpec();
    }

    @Override
    public XBCGroupSpec createGroupSpec() {
        return ((XBRSpecManager) itemManager).createGroupSpec();
    }

    @Override
    public XBCFormatSpec createFormatSpec() {
        return ((XBRSpecManager) itemManager).createFormatSpec();
    }

    @Override
    public void removeItemDepth(XBCSpecDef specDef) {
        XBCXNameManager nameManager = catalog.getCatalogManager(XBCXNameManager.class);
        List<XBCXName> itemNames = nameManager.getItemNames(specDef);
        for (XBCXName itemName : itemNames) {
            nameManager.removeItem(itemName);
        }

        XBCXDescManager descManager = catalog.getCatalogManager(XBCXDescManager.class);
        List<XBCXDesc> itemDescs = descManager.getItemDescs(specDef);
        for (XBCXDesc itemDesc : itemDescs) {
            descManager.removeItem(itemDesc);
        }

        XBCXStriManager striManager = catalog.getCatalogManager(XBCXStriManager.class);
        XBCXStri itemStri = striManager.getItemStringId(specDef);
        if (itemStri != null) {
            striManager.removeItem(itemStri);
        }

        XBCXHDocManager hdocManager = catalog.getCatalogManager(XBCXHDocManager.class);
        XBCXHDoc itemHDoc = hdocManager.getDocumentation(specDef);
        if (itemHDoc != null) {
            hdocManager.removeItem(itemHDoc);
        }

        ((XBCSpecService) this).removeSpecDef(specDef);
    }

    @Override
    public XBLFormatDecl getFormatDeclAsLocal(XBCFormatDecl formatDecl) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public XBLGroupDecl getGroupDeclAsLocal(XBCGroupDecl groupDecl) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public XBLBlockDecl getBlockDeclAsLocal(XBCBlockDecl blockDecl) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void persistSpecDef(XBCSpecDef specDef) {
        ((XBRSpecManager) itemManager).persistSpecDef(specDef);
    }

    @Override
    public void removeSpecDef(XBCSpecDef specDef) {
        ((XBRSpecManager) itemManager).removeSpecDef(specDef);
    }
}
