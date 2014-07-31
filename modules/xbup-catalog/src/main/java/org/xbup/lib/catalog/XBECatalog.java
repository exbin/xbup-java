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
import org.xbup.lib.core.block.declaration.XBBlockDecl;
import org.xbup.lib.core.block.declaration.XBContext;
import org.xbup.lib.core.block.declaration.XBFormatDecl;
import org.xbup.lib.core.block.declaration.XBGroupDecl;
import org.xbup.lib.core.catalog.XBCatalog;
import org.xbup.lib.core.catalog.base.XBCBase;
import org.xbup.lib.core.catalog.base.XBCBlockRev;
import org.xbup.lib.core.catalog.base.XBCFormatSpec;
import org.xbup.lib.core.catalog.base.XBCGroupRev;
import org.xbup.lib.core.catalog.base.XBCGroupSpec;
import org.xbup.lib.core.catalog.base.XBCSpecDef;
import org.xbup.lib.core.catalog.base.manager.XBCManager;
import org.xbup.lib.core.catalog.base.service.XBCXInfoService;
import org.xbup.lib.core.catalog.base.service.XBCItemService;
import org.xbup.lib.core.catalog.base.service.XBCNodeService;
import org.xbup.lib.core.catalog.base.service.XBCRevService;
import org.xbup.lib.core.catalog.base.service.XBCService;
import org.xbup.lib.core.catalog.base.service.XBCSpecService;
import org.xbup.lib.core.catalog.declaration.XBCGroupDecl;
import org.xbup.lib.core.catalog.declaration.XBCPContext;
import org.xbup.lib.core.catalog.declaration.XBCPFormatDecl;
import org.xbup.lib.core.catalog.declaration.XBCSBlockDecl;
import org.xbup.lib.core.catalog.base.XBCExtension;
import org.xbup.lib.core.catalog.update.XBCUpdateHandler;
import org.xbup.lib.catalog.entity.XBEBlockSpec;
import org.xbup.lib.catalog.entity.XBEFormatSpec;
import org.xbup.lib.catalog.entity.XBEGroupSpec;
import org.xbup.lib.catalog.entity.XBENode;
import org.xbup.lib.catalog.entity.XBERoot;
import org.xbup.lib.catalog.entity.service.XBEXInfoService;
import org.xbup.lib.catalog.entity.service.XBEItemService;
import org.xbup.lib.catalog.entity.service.XBENodeService;
import org.xbup.lib.catalog.entity.service.XBERevService;
import org.xbup.lib.catalog.entity.service.XBESpecService;

/**
 * Basic level 1 catalog class using Java persistence.
 *
 * @version 0.1 wr23.0 2014/03/25
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
        catalogManagers = new HashMap<Class, XBCManager<?>>();
        catalogServices = new HashMap<Class, XBCService<?>>();

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
    public XBFormatDecl findFormatTypeByPath(Long[] xbCatalogPath) {
        XBEFormatSpec spec = findFormatSpecByPath(xbCatalogPath);
        if (spec == null) {
            return null;
        }
        return new XBCPFormatDecl(this, spec);
    }

    @Override
    public XBGroupDecl findGroupTypeByPath(Long[] xbCatalogPath) {
        XBEGroupSpec spec = findGroupSpecByPath(xbCatalogPath);
        if (spec == null) {
            return null;
        }
        return new XBCGroupDecl(this, spec);
    }

    @Override
    public XBBlockDecl findBlockTypeByPath(Long[] xbCatalogPath) {
        XBEBlockSpec spec = findBlockSpecByPath(xbCatalogPath);
        if (spec == null) {
            return null;
        }
        return new XBCSBlockDecl(spec);
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
        emptyContext = new XBContext(this);
    }

    private XBContext createFormatContext(XBEFormatSpec spec) {
        if (spec == null) {
            return null;
        }
//        return new XBContext(new XBCPContextFormat(this,spec));
        return new XBCPContext(this, spec);
    }

    @Override
    public List<XBGroupDecl> getGroups(XBCFormatSpec spec) {
        XBESpecService specService = (XBESpecService) getCatalogService(XBCSpecService.class);
        List<XBCSpecDef> binds = specService.getSpecDefs(spec);
        ArrayList<XBGroupDecl> result = new ArrayList<XBGroupDecl>();
        for (Iterator it = binds.iterator(); it.hasNext();) {
            result.add(new XBCGroupDecl(this, ((XBCGroupRev) ((XBCSpecDef) it.next()).getTarget()).getParent()));
        }
        return result;
    }

    @Override
    public List<XBBlockDecl> getBlocks(XBCGroupSpec spec) {
        XBESpecService specService = (XBESpecService) getCatalogService(XBCSpecService.class);
        List<XBCSpecDef> binds = specService.getSpecDefs(spec);
        ArrayList<XBBlockDecl> result = new ArrayList<XBBlockDecl>();
        for (Iterator it = binds.iterator(); it.hasNext();) {
            result.add(new XBCSBlockDecl(((XBCBlockRev) ((XBCSpecDef) it.next()).getTarget()).getParent()));
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
        return new ArrayList<XBCService<? extends XBCBase>>(catalogServices.values());
    }

    @Override
    public List<XBCManager<? extends XBCBase>> getCatalogManagers() {
        return new ArrayList<XBCManager<? extends XBCBase>>(catalogManagers.values());
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
}
