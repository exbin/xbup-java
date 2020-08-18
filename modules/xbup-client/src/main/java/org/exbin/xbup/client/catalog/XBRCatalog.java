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
package org.exbin.xbup.client.catalog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.Nonnull;
import org.exbin.xbup.client.XBCatalogServiceClient;
import org.exbin.xbup.client.catalog.remote.XBRBlockSpec;
import org.exbin.xbup.client.catalog.remote.XBRFormatSpec;
import org.exbin.xbup.client.catalog.remote.XBRGroupSpec;
import org.exbin.xbup.client.catalog.remote.XBRNode;
import org.exbin.xbup.client.catalog.remote.XBRSpecDef;
import org.exbin.xbup.client.catalog.remote.service.XBRInfoService;
import org.exbin.xbup.client.catalog.remote.service.XBRItemService;
import org.exbin.xbup.client.catalog.remote.service.XBRNodeService;
import org.exbin.xbup.client.catalog.remote.service.XBRRevService;
import org.exbin.xbup.client.catalog.remote.service.XBRRootService;
import org.exbin.xbup.client.catalog.remote.service.XBRSpecService;
import org.exbin.xbup.core.block.XBBasicBlockType;
import org.exbin.xbup.core.block.XBBlockType;
import org.exbin.xbup.core.block.declaration.XBBlockDecl;
import org.exbin.xbup.core.block.declaration.XBContext;
import org.exbin.xbup.core.block.declaration.XBFormatDecl;
import org.exbin.xbup.core.block.declaration.XBGroup;
import org.exbin.xbup.core.block.declaration.XBGroupDecl;
import org.exbin.xbup.core.block.declaration.catalog.XBCBlockDecl;
import org.exbin.xbup.core.block.declaration.catalog.XBCFormatDecl;
import org.exbin.xbup.core.block.declaration.catalog.XBCGroupDecl;
import org.exbin.xbup.core.block.declaration.local.XBLGroupDecl;
import org.exbin.xbup.core.catalog.XBCatalog;
import org.exbin.xbup.core.catalog.base.XBCBlockRev;
import org.exbin.xbup.core.catalog.base.XBCExtension;
import org.exbin.xbup.core.catalog.base.XBCFormatRev;
import org.exbin.xbup.core.catalog.base.XBCFormatSpec;
import org.exbin.xbup.core.catalog.base.XBCGroupRev;
import org.exbin.xbup.core.catalog.base.XBCGroupSpec;
import org.exbin.xbup.core.catalog.base.XBCSpec;
import org.exbin.xbup.core.catalog.base.XBCSpecDef;
import org.exbin.xbup.core.catalog.base.manager.XBCManager;
import org.exbin.xbup.core.catalog.base.service.XBCItemService;
import org.exbin.xbup.core.catalog.base.service.XBCNodeService;
import org.exbin.xbup.core.catalog.base.service.XBCRevService;
import org.exbin.xbup.core.catalog.base.service.XBCRootService;
import org.exbin.xbup.core.catalog.base.service.XBCService;
import org.exbin.xbup.core.catalog.base.service.XBCSpecService;
import org.exbin.xbup.core.catalog.base.service.XBCXInfoService;
import org.exbin.xbup.core.parser.token.pull.XBTPullProvider;

/**
 * XBUP level 1 remote catalog.
 *
 * Catalog is accesed using XBService RPC network interface.
 *
 * @version 0.2.1 2020/08/18
 * @author ExBin Project (http://exbin.org)
 */
public class XBRCatalog implements XBCatalog {

    private XBCatalogServiceClient client;

    protected Map<Class, XBCManager<?>> catalogManagers;
    protected Map<Class, XBCService<?>> catalogServices;

    /**
     * Creates a new instance of XBUP level 1 catalog.
     *
     * @param client catalog service client
     */
    public XBRCatalog(XBCatalogServiceClient client) {
        this.client = client;
        catalogManagers = new HashMap<>();
        catalogServices = new HashMap<>();

        // TODO IoC might be better
        catalogServices.put(XBCItemService.class, new XBRItemService(this));
        catalogServices.put(XBCRootService.class, new XBRRootService(this));
        catalogServices.put(XBCNodeService.class, new XBRNodeService(this));
        catalogServices.put(XBCSpecService.class, new XBRSpecService(this));
        catalogServices.put(XBCXInfoService.class, new XBRInfoService(this));
        catalogServices.put(XBCRevService.class, new XBRRevService(this));
    }

    @Override
    public XBContext getRootContext() {
        XBRNodeService nodeService = (XBRNodeService) getCatalogService(XBCNodeService.class);
        XBRSpecService specService = (XBRSpecService) getCatalogService(XBCSpecService.class);
        XBRNode node = (XBRNode) nodeService.getMainRootNode();
        XBRFormatSpec spec = (XBRFormatSpec) specService.findFormatSpecByXB(node, 0);
        if (spec == null) {
            return null;
        }
        return new XBContext();
//        return new XBL1CPContext(this,spec);
    }

    public XBRFormatSpec findFormatSpecByPath(Long[] xbCatalogPath) {
        XBRNodeService nodeService = (XBRNodeService) getCatalogService(XBCNodeService.class);
        XBRSpecService specService = (XBRSpecService) getCatalogService(XBCSpecService.class);
        XBRNode node = (XBRNode) nodeService.findOwnerByXBPath(xbCatalogPath);
        if (node == null) {
            return null;
        }

        return (XBRFormatSpec) specService.findFormatSpecByXB(node, xbCatalogPath[xbCatalogPath.length - 1]);
    }

    public XBRGroupSpec findGroupSpecByPath(Long[] xbCatalogPath) {
        XBRNodeService nodeService = (XBRNodeService) getCatalogService(XBCNodeService.class);
        XBRSpecService specService = (XBRSpecService) getCatalogService(XBCSpecService.class);
        XBRNode node = (XBRNode) nodeService.findOwnerByXBPath(xbCatalogPath);
        if (node == null) {
            return null;
        }

        return (XBRGroupSpec) specService.findGroupSpecByXB(node, xbCatalogPath[xbCatalogPath.length - 1]);
    }

    public XBRBlockSpec findBlockSpecByPath(Long[] xbCatalogPath) {
        XBRNodeService nodeService = (XBRNodeService) getCatalogService(XBCNodeService.class);
        XBRSpecService specService = (XBRSpecService) getCatalogService(XBCSpecService.class);
        XBRNode node = (XBRNode) nodeService.findOwnerByXBPath(xbCatalogPath);
        if (node == null) {
            return null;
        }

        return (XBRBlockSpec) specService.findBlockSpecByXB(node, xbCatalogPath[xbCatalogPath.length - 1]);
    }

    @Override
    public XBBlockDecl findBlockTypeByPath(Long[] xbCatalogPath, int revision) {
        XBRRevService revService = (XBRRevService) getCatalogService(XBCRevService.class);
        XBRBlockSpec spec = findBlockSpecByPath(xbCatalogPath);
        if (spec == null) {
            return null;
        }

        XBCBlockRev rev = (XBCBlockRev) revService.findRevByXB(spec, revision);
        if (rev == null) {
            return null;
        }

        return new XBCBlockDecl(rev, this);
    }

    @Override
    public XBGroupDecl findGroupTypeByPath(Long[] xbCatalogPath, int revision) {
        XBRRevService revService = (XBRRevService) getCatalogService(XBCRevService.class);
        XBRGroupSpec spec = findGroupSpecByPath(xbCatalogPath);
        if (spec == null) {
            return null;
        }

        XBCGroupRev rev = (XBCGroupRev) revService.findRevByXB(spec, revision);
        if (rev == null) {
            return null;
        }

        return new XBCGroupDecl(rev, this);
    }

    @Override
    public XBFormatDecl findFormatTypeByPath(Long[] xbCatalogPath, int revision) {
        XBRRevService revService = (XBRRevService) getCatalogService(XBCRevService.class);
        XBRFormatSpec spec = findFormatSpecByPath(xbCatalogPath);
        if (spec == null) {
            return null;
        }

        XBCFormatRev rev = (XBCFormatRev) revService.findRevByXB(spec, revision);
        if (rev == null) {
            return null;
        }

        return new XBCFormatDecl(rev, this);
    }

    @Override
    public List<XBGroupDecl> getGroups(XBCFormatSpec spec) {
        XBRSpecService specService = (XBRSpecService) getCatalogService(XBCSpecService.class);
        List<XBCSpecDef> binds = specService.getSpecDefs(spec);
        ArrayList<XBGroupDecl> result = new ArrayList<>();
        for (Iterator it = binds.iterator(); it.hasNext();) {
            result.add(new XBCGroupDecl((XBCGroupRev) ((XBRSpecDef) it.next()).getTargetRev().get(), this));
        }

        return result;
    }

    @Override
    public List<XBBlockDecl> getBlocks(XBCGroupSpec spec) {
        XBRSpecService specService = (XBRSpecService) getCatalogService(XBCSpecService.class);
        List<XBCSpecDef> binds = specService.getSpecDefs(spec);
        ArrayList<XBBlockDecl> result = new ArrayList<>();
        for (Iterator it = binds.iterator(); it.hasNext();) {
            result.add(new XBCBlockDecl((XBCBlockRev) ((XBRSpecDef) it.next()).getTargetRev().get(), this));
        }

        return result;
    }

    @Override
    public List<XBCService<?>> getCatalogServices() {
        return new ArrayList<>(catalogServices.values());
    }

    @Override
    public List<XBCManager<?>> getCatalogManagers() {
        return new ArrayList<>(catalogManagers.values());
    }

    @Override
    public <T extends XBCManager<?>> void addCatalogManager(Class<T> type, T manager) {
        catalogManagers.put(type, manager);
        if (manager instanceof XBCExtension) {
            ((XBCExtension) manager).initializeExtension();
        }
    }

    @Override
    public <T extends XBCService<?>> void addCatalogService(Class<T> type, T service) {
        catalogServices.put(type, service);
        if (service instanceof XBCExtension) {
            ((XBCExtension) service).initializeExtension();
        }
    }

    public XBCatalogServiceClient getCatalogServiceClient() {
        return client;
    }

    public void setCatalogServiceClient(XBCatalogServiceClient client) {
        this.client = client;
    }

    @Nonnull
    @Override
    public <T extends XBCManager<?>> T getCatalogManager(@Nonnull Class<T> managerClass) {
        return (T) catalogManagers.get(managerClass);
    }

    @Nonnull
    @Override
    public <T extends XBCService<?>> T getCatalogService(@Nonnull Class<T> serviceClass) {
        return (T) catalogServices.get(serviceClass);
    }

    public Long findBlockIdForGroup(XBGroup group, XBBlockDecl decl) {
        List<XBBlockDecl> blocks = group.getBlocks();
        for (int blockId = 0; blockId < blocks.size(); blockId++) {
            XBBlockDecl block = blocks.get(blockId);
            if (block.equals(decl)) {
                return (long) blockId;
            }
        }

        return null;
    }

    public Long findBlockIdForGroup(XBGroupDecl group, XBBlockDecl decl) {
        if (group instanceof XBLGroupDecl) {
            List<XBBlockDecl> blocks = ((XBLGroupDecl) group).getBlockDecls();
            for (int blockId = 0; blockId < blocks.size(); blockId++) {
                XBBlockDecl block = blocks.get(blockId);
                if (block.equals(decl)) {
                    return (long) blockId;
                }
            }
        } else if (group instanceof XBCGroupSpec) {
            XBCGroupRev groupRev = ((XBCGroupDecl) group).getGroupSpecRev();
            List<XBBlockDecl> blocks = getBlocks(groupRev.getParent());
            Long limit = groupRev.getXBLimit();
            for (int blockId = 0; blockId < blocks.size() && blockId < limit; blockId++) {
                XBBlockDecl block = blocks.get(blockId);
                if (block.equals(decl)) {
                    return (long) blockId;
                }
            }
        } else {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        return null;
    }

    @Override
    public XBContext processDeclaration(XBContext parent, XBTPullProvider blockProvider) {
        throw new UnsupportedOperationException("Not supported yet.");
        /* TODO if (specBlock.getDataMode() == XBBlockDataMode.NODE_BLOCK) {
            if (specBlock.getAttributesCount() > 1) {
                if (specBlock.getBlockType().getAsBasicType() == XBBasicBlockType.DECLARATION) {
                    XBContext context = new XBContext();
                    context.setParent(parent);

                    context.getDeclaration().setGroupsReserved(specBlock.getAttribute(0).getLong());
                    context.getDeclaration().setPreserveCount(specBlock.getAttribute(1).getLong());
                    if (specBlock.getChildCount() > 0) {
                        context.getDeclaration().setFormat(processFormatSpec(catalog, specBlock.getChildAt(0)));
                    }
                    return context;
                }
            }
        }

        return null; */
    }

    @Override
    public Long[] getSpecPath(XBCSpec spec) {
        XBRSpecService specService = (XBRSpecService) getCatalogService(XBCSpecService.class);
        return specService.getSpecXBPath(spec);
    }

    @Override
    public XBBlockType getBasicBlockType(XBBasicBlockType blockType) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
