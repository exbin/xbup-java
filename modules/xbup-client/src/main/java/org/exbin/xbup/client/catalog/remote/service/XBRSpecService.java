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
 * @version 0.1.25 2015/03/19
 * @author ExBin Project (http://exbin.org)
 */
public class XBRSpecService extends XBRDefaultService<XBRSpec> implements XBCSpecService<XBRSpec> {

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
        XBCXNameManager nameManager = (XBCXNameManager) catalog.getCatalogManager(XBCXNameManager.class);
        List<XBCXName> itemNames = nameManager.getItemNames(specDef);
        for (XBCXName itemName : itemNames) {
            nameManager.removeItem(itemName);
        }

        XBCXDescManager descManager = (XBCXDescManager) catalog.getCatalogManager(XBCXDescManager.class);
        List<XBCXDesc> itemDescs = descManager.getItemDescs(specDef);
        for (XBCXDesc itemDesc : itemDescs) {
            descManager.removeItem(itemDesc);
        }

        XBCXStriManager striManager = (XBCXStriManager) catalog.getCatalogManager(XBCXStriManager.class);
        XBCXStri itemStri = striManager.getItemStringId(specDef);
        if (itemStri != null) {
            striManager.removeItem(itemStri);
        }

        XBCXHDocManager hdocManager = (XBCXHDocManager) catalog.getCatalogManager(XBCXHDocManager.class);
        XBCXHDoc itemHDoc = hdocManager.getDocumentation(specDef);
        if (itemHDoc != null) {
            hdocManager.removeItem(itemHDoc);
        }

        ((XBCSpecService) this).removeItem(specDef);
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
}
