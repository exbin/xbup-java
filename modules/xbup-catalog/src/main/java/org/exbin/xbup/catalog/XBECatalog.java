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
package org.exbin.xbup.catalog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import org.exbin.xbup.catalog.entity.XBEBlockSpec;
import org.exbin.xbup.catalog.entity.XBEFormatSpec;
import org.exbin.xbup.catalog.entity.XBEGroupSpec;
import org.exbin.xbup.catalog.entity.XBENode;
import org.exbin.xbup.catalog.entity.XBERoot;
import org.exbin.xbup.catalog.entity.service.XBEItemService;
import org.exbin.xbup.catalog.entity.service.XBENodeService;
import org.exbin.xbup.catalog.entity.service.XBERevService;
import org.exbin.xbup.catalog.entity.service.XBESpecService;
import org.exbin.xbup.catalog.entity.service.XBEXInfoService;
import org.exbin.xbup.catalog.update.XBCUpdateHandler;
import org.exbin.xbup.core.block.XBBasicBlockType;
import org.exbin.xbup.core.block.XBBlockType;
import org.exbin.xbup.core.block.XBFixedBlockType;
import org.exbin.xbup.core.block.declaration.XBBlockDecl;
import org.exbin.xbup.core.block.declaration.XBContext;
import org.exbin.xbup.core.block.declaration.XBDeclBlockType;
import org.exbin.xbup.core.block.declaration.XBDeclaration;
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
import org.exbin.xbup.core.catalog.base.XBCRev;
import org.exbin.xbup.core.catalog.base.XBCSpec;
import org.exbin.xbup.core.catalog.base.XBCSpecDef;
import org.exbin.xbup.core.catalog.base.manager.XBCManager;
import org.exbin.xbup.core.catalog.base.service.XBCItemService;
import org.exbin.xbup.core.catalog.base.service.XBCNodeService;
import org.exbin.xbup.core.catalog.base.service.XBCRevService;
import org.exbin.xbup.core.catalog.base.service.XBCService;
import org.exbin.xbup.core.catalog.base.service.XBCSpecService;
import org.exbin.xbup.core.catalog.base.service.XBCXInfoService;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.parser.token.pull.XBTPullProvider;
import org.exbin.xbup.core.serial.XBPSerialReader;

/**
 * Basic level 1 catalog class using Java persistence.
 *
 * @version 0.2.1 2017/06/06
 * @author ExBin Project (http://exbin.org)
 */
public class XBECatalog implements XBCatalog {

    private XBContext rootContext = null;
    private XBCUpdateHandler updateHandler;
    private boolean shallInit;

    @PersistenceContext
    protected EntityManager em;
    protected Map<Class, XBCManager<?>> catalogManagers;
    protected Map<Class, XBCService<?>> catalogServices;

    public XBECatalog() {
    }

    /**
     * Creates a new instance of XBECatalog.
     *
     * @param em entity manager
     */
    public XBECatalog(EntityManager em) {
        this.em = em;
        init();
    }

    @PostConstruct
    private void init() {
        updateHandler = null;
        catalogManagers = new HashMap<>();
        catalogServices = new HashMap<>();

        // TODO: Or maybe IoC would be better...
        catalogServices.put(XBCItemService.class, new XBEItemService(this));
        XBENodeService nodeService = new XBENodeService(this);
        catalogServices.put(XBCNodeService.class, nodeService);
        catalogServices.put(XBCSpecService.class, new XBESpecService(this));
        catalogServices.put(XBCXInfoService.class, new XBEXInfoService(this));
        catalogServices.put(XBCRevService.class, new XBERevService(this));
        shallInit = (nodeService.getRootNode() == null);
    }

    @Nullable
    public XBEFormatSpec findFormatSpecByPath(@Nonnull Long[] xbCatalogPath) {
        XBENodeService nodeService = (XBENodeService) getCatalogService(XBCNodeService.class);
        XBESpecService specService = (XBESpecService) getCatalogService(XBCSpecService.class);
        XBENode node = nodeService.findOwnerByXBPath(xbCatalogPath);
        if (node == null) {
            return null;
        }
        return specService.findFormatSpecByXB(node, xbCatalogPath[xbCatalogPath.length - 1]);
    }

    @Nullable
    public XBEGroupSpec findGroupSpecByPath(@Nonnull Long[] xbCatalogPath) {
        XBENodeService nodeService = (XBENodeService) getCatalogService(XBCNodeService.class);
        XBESpecService specService = (XBESpecService) getCatalogService(XBCSpecService.class);
        XBENode node = nodeService.findOwnerByXBPath(xbCatalogPath);
        if (node == null) {
            return null;
        }
        return specService.findGroupSpecByXB(node, xbCatalogPath[xbCatalogPath.length - 1]);
    }

    @Nullable
    public XBEBlockSpec findBlockSpecByPath(@Nonnull Long[] xbCatalogPath) {
        XBENodeService nodeService = (XBENodeService) getCatalogService(XBCNodeService.class);
        XBESpecService specService = (XBESpecService) getCatalogService(XBCSpecService.class);
        XBENode node = nodeService.findOwnerByXBPath(xbCatalogPath);
        if (node == null) {
            return null;
        }
        return specService.findBlockSpecByXB(node, xbCatalogPath[xbCatalogPath.length - 1]);
    }

    @Override
    @Nullable
    public XBFormatDecl findFormatTypeByPath(@Nonnull Long[] xbCatalogPath, int revision) {
        XBERevService revService = (XBERevService) getCatalogService(XBCRevService.class);
        XBEFormatSpec spec = findFormatSpecByPath(xbCatalogPath);
        if (spec == null) {
            return null;
        }

        XBCRev rev = revService.findRevByXB(spec, revision);

        if (rev == null) {
            return null;
        }

        return new XBCFormatDecl((XBCFormatRev) rev, this);
    }

    @Override
    @Nullable
    public XBGroupDecl findGroupTypeByPath(@Nonnull Long[] xbCatalogPath, int revision) {
        XBERevService revService = (XBERevService) getCatalogService(XBCRevService.class);
        XBEGroupSpec spec = findGroupSpecByPath(xbCatalogPath);
        if (spec == null) {
            return null;
        }

        XBCRev rev = revService.findRevByXB(spec, revision);

        if (rev == null) {
            return null;
        }

        return new XBCGroupDecl((XBCGroupRev) rev, this);
    }

    @Override
    @Nullable
    public XBBlockDecl findBlockTypeByPath(@Nonnull Long[] blockSpecCatalogPath, int revision) {
        XBERevService revService = (XBERevService) getCatalogService(XBCRevService.class);
        XBEBlockSpec spec = findBlockSpecByPath(blockSpecCatalogPath);
        if (spec == null) {
            return null;
        }

        XBCRev rev = revService.findRevByXB(spec, revision);

        if (rev == null) {
            return null;
        }

        return new XBCBlockDecl((XBCBlockRev) rev, this);
    }

    @Override
    @Nonnull
    public XBContext getRootContext() {
        if (rootContext == null) {
            // Generate root context
            XBENodeService nodeService = (XBENodeService) getCatalogService(XBCNodeService.class);
            XBESpecService specService = (XBESpecService) getCatalogService(XBCSpecService.class);
            XBERevService revService = (XBERevService) getCatalogService(XBCRevService.class);
            XBENode rootNode = nodeService.getRootNode();
            XBENode basicNode = nodeService.getSubNode(rootNode, 0);
            // XBCFormatDecl formatDecl = new XBCFormatDecl((XBCFormatRev) revService.findRevByXB(specService.findFormatSpecByXB(rootNode, 0),0), this);
            // XBCGroupDecl groupDecl = (XBCGroupDecl) formatDecl.getBlockDecls().get(0);
            XBCGroupDecl groupDecl = new XBCGroupDecl((XBCGroupRev) revService.findRevByXB(specService.findGroupSpecByXB(basicNode, 0), 0), this);
            rootContext = new XBContext();
            rootContext.setStartFrom(0);
            rootContext.getGroups().add(XBDeclaration.convertCatalogGroup(groupDecl));
        }

        return rootContext;
    }

    public void setRootContext(XBContext context) {
        rootContext = context;
    }

    public void initCatalog() {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            XBENode node = new XBENode();
            node.setXBIndex(Long.valueOf(0));
            node.setOwner(null);
            em.persist(node);

            XBERoot root = new XBERoot();
            root.setNode(node);
            em.persist(root);
            tx.commit();

            shallInit = false;
        } catch (Exception ex) {
            Logger.getLogger(XBECatalog.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public List<XBGroupDecl> getGroups(XBCFormatSpec spec) {
        XBESpecService specService = (XBESpecService) getCatalogService(XBCSpecService.class);
        List<XBCSpecDef> binds = specService.getSpecDefs(spec);
        ArrayList<XBGroupDecl> result = new ArrayList<>();
        for (Iterator it = binds.iterator(); it.hasNext();) {
            result.add(new XBCGroupDecl((XBCGroupRev) ((XBCSpecDef) it.next()).getTarget(), this));
        }
        return result;
    }

    @Override
    public List<XBBlockDecl> getBlocks(XBCGroupSpec spec) {
        XBESpecService specService = (XBESpecService) getCatalogService(XBCSpecService.class);
        List<XBCSpecDef> binds = specService.getSpecDefs(spec);
        ArrayList<XBBlockDecl> result = new ArrayList<>();
        for (Iterator it = binds.iterator(); it.hasNext();) {
            result.add(new XBCBlockDecl((XBCBlockRev) ((XBCSpecDef) it.next()).getTarget(), this));
        }
        return result;
    }

    @Override
    public <T extends XBCManager<?>> void addCatalogManager(Class<T> type, T manager) {
        catalogManagers.put(type, manager);
        if (isShallInit() && (manager instanceof XBCExtension)) {
            ((XBCExtension) manager).initializeExtension();
        }
    }

    @Override
    public <T extends XBCService<?>> void addCatalogService(Class<T> type, T service) {
        catalogServices.put(type, service);
        if (isShallInit() && (service instanceof XBCExtension)) {
            ((XBCExtension) service).initializeExtension();
        }
    }

    public EntityManager getEntityManager() {
        return em;
    }

    @Override
    @Nonnull
    public List<XBCService<?>> getCatalogServices() {
        return new ArrayList<>(catalogServices.values());
    }

    @Override
    @Nonnull
    public List<XBCManager<?>> getCatalogManagers() {
        return new ArrayList<>(catalogManagers.values());
    }

    public XBCUpdateHandler getUpdateHandler() {
        return updateHandler;
    }

    public void setUpdateHandler(XBCUpdateHandler updateHandler) {
        this.updateHandler = updateHandler;
    }

    public boolean isShallInit() {
        return shallInit;
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

    /**
     * Gets block type for specification defined by IDs in given context.
     *
     * @param context current context
     * @param groupId group ID
     * @param blockId block ID
     * @return block type declaration
     */
    public XBBlockDecl getBlockType(XBContext context, int groupId, int blockId) {
        XBGroup group = context.getGroupForId(groupId);

        if (group == null) {
            return null;
        }

        List<XBBlockDecl> blocks = group.getBlocks();
        return blockId < blocks.size() ? blocks.get(blockId) : null;
    }

    @Override
    public XBContext processDeclaration(XBContext parent, XBTPullProvider blockProvider) {
        XBDeclaration declaration = new XBDeclaration(new XBCFormatDecl(null, this));
        declaration.setHeaderMode(true);
        XBPSerialReader serialHandler = new XBPSerialReader(blockProvider);
        try {
            serialHandler.read(declaration);
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBECatalog.class.getName()).log(Level.SEVERE, null, ex);
        }

        XBContext context = declaration.generateContext();
        /*declaration.se
         XBContext context = new XBContext();
         context.setParent(parent);
         context.getDeclaration().setGroupsReserved(specBlock.getAttribute(0).getLong());
         context.getDeclaration().setPreserveCount(specBlock.getAttribute(1).getLong());
         if (specBlock.getChildCount() > 0) {
         context.getDeclaration().setFormat(processFormatSpec(catalog, specBlock.getChildAt(0)));
         }
         return context; */
        return context;
    }

    @Override
    public Long[] getSpecPath(XBCSpec spec) {
        XBESpecService specService = (XBESpecService) getCatalogService(XBCSpecService.class);
        return specService.getSpecXBPath(spec);
    }

    @Override
    public XBBlockType getBasicBlockType(XBBasicBlockType blockType) {
        Long[] catalogBlockPath = new Long[2];
        catalogBlockPath[0] = 0l;
        catalogBlockPath[1] = (long) blockType.ordinal();
        XBBlockDecl blockDecl = findBlockTypeByPath(catalogBlockPath, 0);
        return blockDecl != null ? new XBDeclBlockType(blockDecl) : new XBFixedBlockType(blockType);
    }
}
