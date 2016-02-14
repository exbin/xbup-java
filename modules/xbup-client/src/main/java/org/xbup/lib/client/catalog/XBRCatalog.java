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
package org.xbup.lib.client.catalog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.xbup.lib.core.block.declaration.XBBlockDecl;
import org.xbup.lib.core.block.declaration.XBFormatDecl;
import org.xbup.lib.core.block.declaration.XBGroupDecl;
import org.xbup.lib.core.block.declaration.catalog.XBCBlockDecl;
import org.xbup.lib.core.block.declaration.catalog.XBCFormatDecl;
import org.xbup.lib.core.block.declaration.catalog.XBCGroupDecl;
import org.xbup.lib.core.block.declaration.XBContext;
import org.xbup.lib.core.block.declaration.XBGroup;
import org.xbup.lib.core.block.declaration.local.XBLGroupDecl;
import org.xbup.lib.core.catalog.base.XBCBase;
import org.xbup.lib.core.catalog.base.XBCBlockRev;
import org.xbup.lib.core.catalog.base.XBCExtension;
import org.xbup.lib.core.catalog.base.XBCFormatRev;
import org.xbup.lib.core.catalog.base.XBCFormatSpec;
import org.xbup.lib.core.catalog.base.XBCGroupRev;
import org.xbup.lib.core.catalog.base.XBCGroupSpec;
import org.xbup.lib.core.catalog.base.XBCSpec;
import org.xbup.lib.core.catalog.base.XBCSpecDef;
import org.xbup.lib.core.catalog.base.manager.XBCManager;
import org.xbup.lib.core.catalog.base.service.XBCItemService;
import org.xbup.lib.core.catalog.base.service.XBCNodeService;
import org.xbup.lib.core.catalog.base.service.XBCRevService;
import org.xbup.lib.core.catalog.base.service.XBCService;
import org.xbup.lib.core.catalog.base.service.XBCSpecService;
import org.xbup.lib.core.catalog.base.service.XBCXInfoService;
import org.xbup.lib.client.XBCatalogServiceClient;
import org.xbup.lib.client.catalog.remote.XBRBlockSpec;
import org.xbup.lib.client.catalog.remote.XBRFormatSpec;
import org.xbup.lib.client.catalog.remote.XBRGroupSpec;
import org.xbup.lib.client.catalog.remote.XBRNode;
import org.xbup.lib.client.catalog.remote.XBRSpecDef;
import org.xbup.lib.client.catalog.remote.service.XBRInfoService;
import org.xbup.lib.client.catalog.remote.service.XBRItemService;
import org.xbup.lib.client.catalog.remote.service.XBRNodeService;
import org.xbup.lib.client.catalog.remote.service.XBRRevService;
import org.xbup.lib.client.catalog.remote.service.XBRSpecService;
import org.xbup.lib.core.block.XBBasicBlockType;
import org.xbup.lib.core.block.XBBlockType;
import org.xbup.lib.core.catalog.XBCatalog;
import org.xbup.lib.core.parser.token.pull.XBTPullProvider;

/**
 * XBUP level 1 remote catalog.
 *
 * Catalog is accesed using XBService RPC network interface.
 *
 * @version 0.1.24 2014/09/03
 * @author XBUP Project (http://xbup.org)
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
        catalogServices.put(XBCNodeService.class, new XBRNodeService(this));
        catalogServices.put(XBCSpecService.class, new XBRSpecService(this));
        catalogServices.put(XBCXInfoService.class, new XBRInfoService(this));
        catalogServices.put(XBCRevService.class, new XBRRevService(this));
    }

    @Override
    public XBContext getRootContext() {
        XBRNodeService nodeService = (XBRNodeService) getCatalogService(XBCNodeService.class);
        XBRSpecService specService = (XBRSpecService) getCatalogService(XBCSpecService.class);
        XBRNode node = (XBRNode) nodeService.getRootNode();
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
            result.add(new XBCGroupDecl((XBCGroupRev) ((XBRSpecDef) it.next()).getTarget(), this));
        }

        return result;
    }

    @Override
    public List<XBBlockDecl> getBlocks(XBCGroupSpec spec) {
        XBRSpecService specService = (XBRSpecService) getCatalogService(XBCSpecService.class);
        List<XBCSpecDef> binds = specService.getSpecDefs(spec);
        ArrayList<XBBlockDecl> result = new ArrayList<>();
        for (Iterator it = binds.iterator(); it.hasNext();) {
            result.add(new XBCBlockDecl((XBCBlockRev) ((XBRSpecDef) it.next()).getTarget(), this));
        }

        return result;
    }

    @Override
    public List<XBCService<? extends XBCBase>> getCatalogServices() {
        return new ArrayList<>(catalogServices.values());
    }

    @Override
    public List<XBCManager<? extends XBCBase>> getCatalogManagers() {
        return new ArrayList<>(catalogManagers.values());
    }

    @Override
    public void addCatalogManager(Class type, XBCManager<? extends XBCBase> manager) {
        catalogManagers.put(type, manager);
        if (manager instanceof XBCExtension) {
            ((XBCExtension) manager).initializeExtension();
        }
    }

    @Override
    public void addCatalogService(Class type, XBCService<? extends XBCBase> service) {
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

    @Override
    public XBCManager<? extends XBCBase> getCatalogManager(Class type) {
        return (XBCManager<? extends XBCBase>) catalogManagers.get(type);
    }

    @Override
    public XBCService<? extends XBCBase> getCatalogService(Class type) {
        return (XBCService<? extends XBCBase>) catalogServices.get(type);
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
