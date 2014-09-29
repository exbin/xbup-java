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
package org.xbup.lib.catalog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContext;
import org.xbup.lib.catalog.entity.XBEBlockSpec;
import org.xbup.lib.catalog.entity.XBEFormatSpec;
import org.xbup.lib.catalog.entity.XBEGroupSpec;
import org.xbup.lib.catalog.entity.XBENode;
import org.xbup.lib.catalog.entity.XBERoot;
import org.xbup.lib.catalog.entity.service.XBEItemService;
import org.xbup.lib.catalog.entity.service.XBENodeService;
import org.xbup.lib.catalog.entity.service.XBERevService;
import org.xbup.lib.catalog.entity.service.XBESpecService;
import org.xbup.lib.catalog.entity.service.XBEXInfoService;
import org.xbup.lib.core.block.XBBasicBlockType;
import org.xbup.lib.core.block.XBBlockDataMode;
import org.xbup.lib.core.block.XBBlockType;
import org.xbup.lib.core.block.XBFixedBlockType;
import org.xbup.lib.core.block.declaration.XBBlockDecl;
import org.xbup.lib.core.block.declaration.XBContext;
import org.xbup.lib.core.block.declaration.XBDeclaration;
import org.xbup.lib.core.block.declaration.XBFormatDecl;
import org.xbup.lib.core.block.declaration.XBGroup;
import org.xbup.lib.core.block.declaration.XBGroupDecl;
import org.xbup.lib.core.block.declaration.catalog.XBCBlockDecl;
import org.xbup.lib.core.block.declaration.catalog.XBCBlockType;
import org.xbup.lib.core.block.declaration.catalog.XBCFormatDecl;
import org.xbup.lib.core.block.declaration.catalog.XBCGroupDecl;
import org.xbup.lib.core.block.declaration.local.XBDGroupDecl;
import org.xbup.lib.core.catalog.XBCatalog;
import org.xbup.lib.core.catalog.base.XBCBase;
import org.xbup.lib.core.catalog.base.XBCBlockRev;
import org.xbup.lib.core.catalog.base.XBCExtension;
import org.xbup.lib.core.catalog.base.XBCFormatRev;
import org.xbup.lib.core.catalog.base.XBCFormatSpec;
import org.xbup.lib.core.catalog.base.XBCGroupRev;
import org.xbup.lib.core.catalog.base.XBCGroupSpec;
import org.xbup.lib.core.catalog.base.XBCRev;
import org.xbup.lib.core.catalog.base.XBCSpec;
import org.xbup.lib.core.catalog.base.XBCSpecDef;
import org.xbup.lib.core.catalog.base.manager.XBCManager;
import org.xbup.lib.core.catalog.base.service.XBCItemService;
import org.xbup.lib.core.catalog.base.service.XBCNodeService;
import org.xbup.lib.core.catalog.base.service.XBCRevService;
import org.xbup.lib.core.catalog.base.service.XBCService;
import org.xbup.lib.core.catalog.base.service.XBCSpecService;
import org.xbup.lib.core.catalog.base.service.XBCXInfoService;
import org.xbup.lib.core.catalog.remote.service.XBRSpecService;
import org.xbup.lib.core.catalog.update.XBCUpdateHandler;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.token.pull.XBTPullProvider;
import org.xbup.lib.core.serial.sequence.XBTSequenceProviderSerialHandler;

/**
 * Basic level 1 catalog class using Java persistence.
 *
 * @version 0.1.24 2014/08/31
 * @author XBUP Project (http://xbup.org)
 */
public class XBECatalog implements XBCatalog {

    private XBContext emptyContext;
    private XBCUpdateHandler updateHandler;
    private boolean shallInit;
    private String fileRepositoryPath;

    @PersistenceContext
    protected EntityManager em;
    protected Map<Class, XBCManager<?>> catalogManagers;
    protected Map<Class, XBCService<?>> catalogServices;

    public XBECatalog() {
        fileRepositoryPath = null;
    }

    public XBECatalog(EntityManager em) {
        this.em = em;
        fileRepositoryPath = null;
        init();
    }

    /**
     * Creates a new instance of XBECatalog
     *
     * @param em entity manager
     * @param fileRepositoryPath path to file repository
     */
    public XBECatalog(EntityManager em, String fileRepositoryPath) {
        this.em = em;
        this.fileRepositoryPath = fileRepositoryPath;
        init();
    }

    @PostConstruct
    private void init() {
        updateHandler = null;
        emptyContext = null;
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
//        if (!isShallInit()) initContext();
    }

    public XBEFormatSpec findFormatSpecByPath(Long[] xbCatalogPath) {
        XBENodeService nodeService = (XBENodeService) getCatalogService(XBCNodeService.class);
        XBESpecService specService = (XBESpecService) getCatalogService(XBCSpecService.class);
        XBENode node = nodeService.findOwnerByXBPath(xbCatalogPath);
        if (node == null) {
            return null;
        }
        return specService.findFormatSpecByXB(node, xbCatalogPath[xbCatalogPath.length - 1]);
    }

    public XBEGroupSpec findGroupSpecByPath(Long[] xbCatalogPath) {
        XBENodeService nodeService = (XBENodeService) getCatalogService(XBCNodeService.class);
        XBESpecService specService = (XBESpecService) getCatalogService(XBCSpecService.class);
        XBENode node = nodeService.findOwnerByXBPath(xbCatalogPath);
        if (node == null) {
            return null;
        }
        return specService.findGroupSpecByXB(node, xbCatalogPath[xbCatalogPath.length - 1]);
    }

    public XBEBlockSpec findBlockSpecByPath(Long[] xbCatalogPath) {
        XBENodeService nodeService = (XBENodeService) getCatalogService(XBCNodeService.class);
        XBESpecService specService = (XBESpecService) getCatalogService(XBCSpecService.class);
        XBENode node = nodeService.findOwnerByXBPath(xbCatalogPath);
        if (node == null) {
            return null;
        }
        return specService.findBlockSpecByXB(node, xbCatalogPath[xbCatalogPath.length - 1]);
    }

    @Override
    public XBFormatDecl findFormatTypeByPath(Long[] xbCatalogPath, int revision) {
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
    public XBGroupDecl findGroupTypeByPath(Long[] xbCatalogPath, int revision) {
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
    public XBBlockDecl findBlockTypeByPath(Long[] xbCatalogPath, int revision) {
        XBERevService revService = (XBERevService) getCatalogService(XBCRevService.class);
        XBEBlockSpec spec = findBlockSpecByPath(xbCatalogPath);
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
    public XBContext getRootContext() {
        return emptyContext;
    }

    public void initCatalog() {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            XBENode node = new XBENode();
            node.setXBIndex(new Long(0));
            node.setOwner(null);
            em.persist(node);

            XBERoot root = new XBERoot();
            root.setNode(node);
            em.persist(root);

            /*XBEFormatSpec format = new XBEFormatSpec();
             format.setParent(node);
             format.setXBIndex(new Long(0));
             em.persist(format);
             XBEFormatRev formatRev = new XBEFormatRev();
             formatRev.setParent(format);
             formatRev.setXBIndex(new Long(0));
             formatRev.setXBLimit(new Long(1));
             em.persist(formatRev);

             XBEGroupSpec group = new XBEGroupSpec();
             group.setParent(node);
             group.setXBIndex(new Long(0));
             em.persist(group);
             XBEGroupRev groupRev = new XBEGroupRev();
             groupRev.setParent(group);
             groupRev.setXBIndex(new Long(0));
             groupRev.setXBLimit(new Long(1));
             em.persist(groupRev);

             XBESpecDef formatBind = new XBESpecDef();
             formatBind.setSpec(format);
             formatBind.setTarget(groupRev);
             formatBind.setXBIndex(new Long(0));
             em.persist(formatBind);

             XBEBlockSpec block = new XBEBlockSpec();
             block.setParent(node);
             block.setXBIndex(new Long(0));
             //            block.setAttrCount(new Long(0));
             em.persist(block);
             XBEBlockRev blockRev = new XBEBlockRev();
             blockRev.setParent(block);
             blockRev.setXBIndex(new Long(0));
             blockRev.setXBLimit(new Long(1));
             em.persist(blockRev);

             XBESpecDef groupBind = new XBESpecDef();
             groupBind.setSpec(group);
             groupBind.setTarget(blockRev);
             groupBind.setXBIndex(new Long(0));
             em.persist(groupBind);

             block = new XBEBlockSpec();
             block.setParent(node);
             block.setXBIndex(new Long(1));
             //            block.setAttrCount(new Long(0));
             em.persist(block);
             blockRev = new XBEBlockRev();
             blockRev.setParent(block);
             blockRev.setXBIndex(new Long(0));
             blockRev.setXBLimit(new Long(1));
             em.persist(blockRev);

             groupBind = new XBESpecDef();
             groupBind.setSpec(group);
             groupBind.setTarget(blockRev);
             groupBind.setXBIndex(new Long(1));
             em.persist(groupBind);

             block = new XBEBlockSpec();
             block.setParent(node);
             block.setXBIndex(new Long(8));
             //            block.setAttrCount(new Long(0));
             em.persist(block);
             blockRev = new XBEBlockRev();
             blockRev.setParent(block);
             blockRev.setXBIndex(new Long(0));
             blockRev.setXBLimit(new Long(1));
             em.persist(blockRev);

             groupBind = new XBESpecDef();
             groupBind.setSpec(group);
             groupBind.setTarget(blockRev);
             groupBind.setXBIndex(new Long(8));
             em.persist(groupBind); */
            tx.commit();

            initContext();
            shallInit = false;
        } catch (Exception ex) {
            Logger.getLogger(XBECatalog.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void initContext() {
        XBENodeService nodeService = (XBENodeService) getCatalogService(XBCNodeService.class);
        XBESpecService specService = (XBESpecService) getCatalogService(XBCSpecService.class);
        XBENode node = nodeService.getRootNode();
        XBEFormatSpec spec = specService.findFormatSpecByXB(node, 0);
//        emptyContext = createFormatContext(spec);
        emptyContext = new XBContext();
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
    public void addCatalogManager(Class type, XBCManager<? extends XBCBase> manager) {
        catalogManagers.put(type, manager);
        if (isShallInit() && (manager instanceof XBCExtension)) {
            ((XBCExtension) manager).initializeExtension();
        }
    }

    @Override
    public void addCatalogService(Class type, XBCService<? extends XBCBase> service) {
        catalogServices.put(type, service);
        if (isShallInit() && (service instanceof XBCExtension)) {
            ((XBCExtension) service).initializeExtension();
        }
    }

    public EntityManager getEntityManager() {
        return em;
    }

    @Override
    public List<XBCService<? extends XBCBase>> getCatalogServices() {
        return new ArrayList<>(catalogServices.values());
    }

    @Override
    public List<XBCManager<? extends XBCBase>> getCatalogManagers() {
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

    @Override
    public XBCManager<? extends XBCBase> getCatalogManager(Class type) {
        return (XBCManager<? extends XBCBase>) catalogManagers.get(type);
    }

    @Override
    public XBCService<? extends XBCBase> getCatalogService(Class type) {
        return (XBCService<? extends XBCBase>) catalogServices.get(type);
    }

    /**
     * @return the fileRepositoryPath
     */
    public String getFileRepositoryPath() {
        return fileRepositoryPath;
    }

    /**
     * @param fileRepositoryPath the fileRepositoryPath to set
     */
    public void setFileRepositoryPath(String fileRepositoryPath) {
        this.fileRepositoryPath = fileRepositoryPath;
    }

    /**
     * Provide fixed block type for given block declaration.
     *
     * @param context
     * @param type block type
     * @return static block type
     */
    @Override
    public XBFixedBlockType findFixedType(XBContext context, XBBlockType type) {
        if (type instanceof XBFixedBlockType) {
            return (XBFixedBlockType) type;
        } else if (type instanceof XBCBlockType) {
            return findFixedType(context, new XBCBlockDecl((XBCBlockRev) ((XBCBlockType) type).getBlockSpec(), this));
        } else {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    /**
     * Provide fixed block type for given block declaration.
     *
     * @param context
     * @param decl block declaration
     * @return static block type
     */
    @Override
    public XBFixedBlockType findFixedType(XBContext context, XBBlockDecl decl) {
        XBFixedBlockType blockType = null;
        if (context.getParent() != null) {
            blockType = findFixedType(context.getParent(), decl);
        }
        
        if (blockType == null) {
            for (int groupId = 0; groupId < context.getGroups().size(); groupId++) {
                XBGroup group = context.getGroups().get(groupId);
                Long blockId = findBlockIdForGroup(group, decl);
                if (blockId != null) {
                    return new XBFixedBlockType(groupId + context.getStartFrom(), blockId);
                }
            }
        }

        return blockType;
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
        if (group instanceof XBDGroupDecl) {
            List<XBBlockDecl> blocks = ((XBDGroupDecl) group).getBlocks();
            for (int blockId = 0; blockId < blocks.size(); blockId++) {
                XBBlockDecl block = blocks.get(blockId);
                if (block.equals(decl)) {
                    return (long) blockId;
                }
            }
        } else if (group instanceof XBCGroupSpec) {
            XBCGroupRev groupRev = ((XBCGroupDecl) group).getGroupSpec();
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
     * Get block type for specification defined by IDs in given context.
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
        try {
            XBTSequenceProviderSerialHandler serialHandler = new XBTSequenceProviderSerialHandler();
            serialHandler.attachXBTPullProvider(blockProvider);
            declaration.serializeDeclaration(serialHandler);
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBECatalog.class.getName()).log(Level.SEVERE, null, ex);
        }

        XBContext context = declaration.generateContext(this);
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

    /* public static XBDFormatDecl processFormatSpec(XBCatalog catalog, XBTBlock specBlock) {
        // TODO: Provide nonstatic method to call super
        XBCNodeService nodeService = (XBCNodeService) catalog.getCatalogService(XBCNodeService.class);
        XBCSpecService specService = (XBCSpecService) catalog.getCatalogService(XBCSpecService.class);
        if (specBlock.getDataMode() == XBBlockDataMode.NODE_BLOCK) {
            if (specBlock.getAttributesCount() > 1) {
                if ((specBlock.getBlockType().getGroupID().getLong() == 0) && (specBlock.getBlockType().getBlockID().getLong() == XBBasicBlockType.FORMAT_DECLARATION.ordinal())) {
                    XBCFormatDecl decl = new XBCFormatDecl(catalog);
                    decl.setGroupsLimit(specBlock.getAttribute(0).getLong());
                    Long[] catalogPath = new Long[specBlock.getAttribute(1).getInt()];
                    int i;
                    for (i = 0; i < catalogPath.length; i++) {
                        catalogPath[i] = specBlock.getAttribute(i + 2).getLong();
                    }
                    XBCNode node = nodeService.findParentByXBPath(catalogPath);
                    if (node != null) {
                        decl.setFormatSpec(specService.findFormatSpecByXB(node, catalogPath[catalogPath.length - 1]));
                    }
                    decl.setRevision(specBlock.getAttribute(i + 2).getLong());
                    return decl;
                }
            }
        }
        return null;
    } */

    /* private void populateBlocks() {
        XBCSpecService bindService = (XBCSpecService) catalog.getCatalogService(XBCSpecService.class);
        XBCRevService revService = (XBCRevService) catalog.getCatalogService(XBCRevService.class);
        if (groupSpec == null) {
            return;
        }
        XBCRev rev = revService.getRev(groupSpec, getRevision());
        List<XBBlockDecl> blocks = new ArrayList<XBBlockDecl>();
        for (int i = 0; i <= rev.getXBLimit(); i++) {
            XBCSpecDef bind = bindService.findSpecDefByXB(groupSpec, i);
            if (bind != null) {
                XBCRev target = bind.getTarget();
                if (target != null) {
                    blocks.add(new XBCBlockDecl((XBCBlockSpec) target.getParent()));
                } else {
                    blocks.add(null);
                }
            } else {
                blocks.add(null);
            }
            setBlocks(blocks);
        }
    } */

    @Override
    public Long[] getSpecPath(XBCSpec spec) {
        XBRSpecService specService = (XBRSpecService) getCatalogService(XBCSpecService.class);
        return specService.getSpecXBPath(spec);
    }
}
