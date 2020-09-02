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
import java.util.ArrayList;
import java.util.List;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.PostConstruct;
import org.exbin.xbup.catalog.XBECatalog;
import org.exbin.xbup.catalog.entity.XBEBlockSpec;
import org.exbin.xbup.catalog.entity.XBEFormatSpec;
import org.exbin.xbup.catalog.entity.XBEGroupSpec;
import org.exbin.xbup.catalog.entity.XBESpec;
import org.exbin.xbup.catalog.entity.XBESpecDef;
import org.exbin.xbup.catalog.entity.manager.XBESpecManager;
import org.exbin.xbup.core.block.declaration.catalog.XBCBlockDecl;
import org.exbin.xbup.core.block.declaration.catalog.XBCFormatDecl;
import org.exbin.xbup.core.block.declaration.catalog.XBCGroupDecl;
import org.exbin.xbup.core.block.declaration.local.XBLBlockDecl;
import org.exbin.xbup.core.block.declaration.local.XBLFormatDecl;
import org.exbin.xbup.core.block.declaration.local.XBLGroupDecl;
import org.exbin.xbup.core.block.definition.XBFormatParam;
import org.exbin.xbup.core.block.definition.XBFormatParamConsist;
import org.exbin.xbup.core.block.definition.XBFormatParamJoin;
import org.exbin.xbup.core.block.definition.XBGroupParam;
import org.exbin.xbup.core.block.definition.XBGroupParamConsist;
import org.exbin.xbup.core.block.definition.XBGroupParamJoin;
import org.exbin.xbup.core.block.definition.XBParamType;
import org.exbin.xbup.core.block.definition.catalog.XBCBlockDef;
import org.exbin.xbup.core.block.definition.catalog.XBCFormatDef;
import org.exbin.xbup.core.block.definition.catalog.XBCGroupDef;
import org.exbin.xbup.core.block.definition.local.XBLFormatDef;
import org.exbin.xbup.core.block.definition.local.XBLGroupDef;
import org.exbin.xbup.core.block.definition.local.XBLRevisionDef;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Entity class for XBESpec items service.
 *
 * @version 0.2.1 2020/08/26
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
@Service
public class XBESpecService extends XBEDefaultService<XBCSpec> implements XBCSpecService, Serializable {

    @Autowired
    private XBESpecManager manager;

    public XBESpecService() {
        super();
    }

    public XBESpecService(XBECatalog catalog) {
        super(catalog);
        itemManager = new XBESpecManager(catalog);
        catalog.addCatalogManager(XBCSpecManager.class, (XBCSpecManager) itemManager);
    }

    @PostConstruct
    public void init() {
        itemManager = manager;
    }

    @Override
    public XBEBlockSpec findBlockSpecByXB(XBCNode node, long xbIndex) {
        return ((XBESpecManager) itemManager).findBlockSpecByXB(node, xbIndex);
    }

    @Override
    public XBEFormatSpec findFormatSpecByXB(XBCNode node, long xbIndex) {
        return ((XBESpecManager) itemManager).findFormatSpecByXB(node, xbIndex);
    }

    @Override
    public XBEGroupSpec findGroupSpecByXB(XBCNode node, long xbIndex) {
        return ((XBESpecManager) itemManager).findGroupSpecByXB(node, xbIndex);
    }

    @Override
    public Long findMaxBlockSpecXB(XBCNode node) {
        return ((XBESpecManager) itemManager).findMaxBlockSpecXB(node);
    }

    @Override
    public Long findMaxFormatSpecXB(XBCNode node) {
        return ((XBESpecManager) itemManager).findMaxFormatSpecXB(node);
    }

    @Override
    public Long findMaxGroupSpecXB(XBCNode node) {
        return ((XBESpecManager) itemManager).findMaxGroupSpecXB(node);
    }

    @Override
    public Long getAllBlockSpecsCount() {
        return ((XBESpecManager) itemManager).getAllBlockSpecsCount();
    }

    @Override
    public Long getAllFormatSpecsCount() {
        return ((XBESpecManager) itemManager).getAllFormatSpecsCount();
    }

    @Override
    public Long getAllGroupSpecsCount() {
        return ((XBESpecManager) itemManager).getAllGroupSpecsCount();
    }

    @Override
    public Long getAllSpecsCount() {
        return ((XBESpecManager) itemManager).getAllSpecsCount();
    }

    @Override
    public XBEBlockSpec getBlockSpec(XBCNode node, long index) {
        return ((XBESpecManager) itemManager).getBlockSpec(node, index);
    }

    @Override
    public List<XBCBlockSpec> getBlockSpecs(XBCNode node) {
        return ((XBESpecManager) itemManager).getBlockSpecs(node);
    }

    @Override
    public long getBlockSpecsCount(XBCNode node) {
        return ((XBESpecManager) itemManager).getBlockSpecsCount(node);
    }

    @Override
    public XBEFormatSpec getFormatSpec(XBCNode node, long index) {
        return ((XBESpecManager) itemManager).getFormatSpec(node, index);
    }

    @Override
    public List<XBCFormatSpec> getFormatSpecs(XBCNode node) {
        return ((XBESpecManager) itemManager).getFormatSpecs(node);
    }

    @Override
    public long getFormatSpecsCount(XBCNode node) {
        return ((XBESpecManager) itemManager).getFormatSpecsCount(node);
    }

    @Override
    public XBEGroupSpec getGroupSpec(XBCNode node, long index) {
        return ((XBESpecManager) itemManager).getGroupSpec(node, index);
    }

    @Override
    public List<XBCGroupSpec> getGroupSpecs(XBCNode node) {
        return ((XBESpecManager) itemManager).getGroupSpecs(node);
    }

    @Override
    public long getGroupSpecsCount(XBCNode node) {
        return ((XBESpecManager) itemManager).getGroupSpecsCount(node);
    }

    @Override
    public XBESpec getSpecByOrder(XBCNode node, long index) {
        return ((XBESpecManager) itemManager).getSpec(node, index);
    }

    @Override
    public Long[] getSpecXBPath(XBCSpec spec) {
        return ((XBESpecManager) itemManager).getSpecXBPath(spec);
    }

    @Override
    public List<XBCSpec> getSpecs(XBCNode node) {
        return ((XBESpecManager) itemManager).getSpecs(node);
    }

    @Override
    public long getSpecsCount(XBCNode node) {
        return ((XBESpecManager) itemManager).getSpecsCount(node);
    }

    @Override
    public XBESpecDef findSpecDefByXB(XBCSpec spec, long xbIndex) {
        return ((XBESpecManager) itemManager).findSpecDefByXB(spec, xbIndex);
    }

    @Override
    public Long findMaxSpecDefXB(XBCSpec spec) {
        return ((XBESpecManager) itemManager).findMaxSpecDefXB(spec);
    }

    @Override
    public XBESpecDef getSpecDefByOrder(XBCSpec spec, long index) {
        return ((XBESpecManager) itemManager).getSpecDefByOrder(spec, index);
    }

    @Override
    public List<XBCSpecDef> getSpecDefs(XBCSpec spec) {
        return ((XBESpecManager) itemManager).getSpecDefs(spec);
    }

    @Override
    public long getSpecDefsCount(XBCSpec spec) {
        return ((XBESpecManager) itemManager).getSpecDefsCount(spec);
    }

    @Override
    public long getDefsCount() {
        return ((XBESpecManager) itemManager).getDefsCount();
    }

    @Override
    public XBESpecDef getSpecDef(long itemId) {
        return (XBESpecDef) ((XBESpecManager) itemManager).getSpecDef(itemId);
    }

    @Override
    public XBCSpecDef createSpecDef(XBCSpec spec, XBParamType type) {
        return ((XBESpecManager) itemManager).createSpecDef(spec, type);
    }

    @Override
    public XBEBlockSpec createBlockSpec() {
        return ((XBESpecManager) itemManager).createBlockSpec();
    }

    @Override
    public XBCGroupSpec createGroupSpec() {
        return ((XBESpecManager) itemManager).createGroupSpec();
    }

    @Override
    public XBCFormatSpec createFormatSpec() {
        return ((XBESpecManager) itemManager).createFormatSpec();
    }

    @Override
    public void persistSpecDef(XBCSpecDef specDef) {
        ((XBESpecManager) itemManager).persistSpecDef(specDef);
    }

    @Override
    public void removeSpecDef(XBCSpecDef specDef) {
        ((XBESpecManager) itemManager).removeSpecDef(specDef);
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
        XBLFormatDecl result = new XBLFormatDecl(getSpecXBPath(formatDecl.getFormatSpecRev().getParent()), (int) formatDecl.getRevision());
        XBLFormatDef formatDef = new XBLFormatDef();
        XBCFormatDef srcFormatDef = new XBCFormatDef(catalog, formatDecl.getFormatSpecRev().getParent());
        List<XBFormatParam> formatParams = new ArrayList<>();
        for (XBFormatParam formatParam : srcFormatDef.getFormatParams()) {
            if (formatParam instanceof XBFormatParamConsist) {
                formatParams.add(new XBFormatParamConsist(getGroupDeclAsLocal((XBCGroupDecl) ((XBFormatParamConsist) formatParam).getGroupDecl())));
            } else if (formatParam instanceof XBFormatParamJoin) {
                formatParams.add(new XBFormatParamJoin(getFormatDeclAsLocal((XBCFormatDecl) ((XBFormatParamJoin) formatParam).getFormatDecl())));
            }
        }

        formatDef.setFormatParams(formatParams);
        formatDef.setRevisionDef(new XBLRevisionDef(srcFormatDef.getRevisionDef().getRevParams()));
        result.setFormatDef(formatDef);
        return result;
    }

    @Override
    public XBLGroupDecl getGroupDeclAsLocal(XBCGroupDecl groupDecl) {
        XBLGroupDecl result = new XBLGroupDecl(getSpecXBPath(groupDecl.getGroupSpecRev().getParent()), (int) groupDecl.getRevision());
        XBLGroupDef groupDef = new XBLGroupDef();
        XBCGroupDef srcGroupDef = new XBCGroupDef(catalog, groupDecl.getGroupSpecRev().getParent());
        List<XBGroupParam> groupParams = new ArrayList<>();
        for (XBGroupParam groupParam : srcGroupDef.getGroupParams()) {
            if (groupParam instanceof XBGroupParamConsist) {
                groupParams.add(new XBGroupParamConsist(getBlockDeclAsLocal((XBCBlockDecl) ((XBGroupParamConsist) groupParam).getBlockDecl())));
            } else if (groupParam instanceof XBFormatParamJoin) {
                groupParams.add(new XBGroupParamJoin(getGroupDeclAsLocal((XBCGroupDecl) ((XBGroupParamJoin) groupParam).getGroupDecl())));
            }
        }

        groupDef.setGroupParams(groupParams);
        groupDef.setRevisionDef(new XBLRevisionDef(srcGroupDef.getRevisionDef().getRevParams()));
        result.setGroupDef(groupDef);
        return result;
    }

    @Override
    public XBLBlockDecl getBlockDeclAsLocal(XBCBlockDecl blockDecl) {
        XBLBlockDecl result = new XBLBlockDecl(getSpecXBPath(blockDecl.getBlockSpecRev().getParent()), (int) blockDecl.getRevision());
        result.setBlockDef(new XBCBlockDef(catalog, blockDecl.getBlockSpecRev().getParent()));
        return result;
    }
}
