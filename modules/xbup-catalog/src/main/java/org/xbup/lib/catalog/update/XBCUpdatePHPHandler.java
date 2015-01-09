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
package org.xbup.lib.catalog.update;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import org.xbup.lib.core.block.declaration.XBContext;
import org.xbup.lib.core.catalog.base.XBCBlockSpec;
import org.xbup.lib.core.catalog.base.XBCFormatSpec;
import org.xbup.lib.core.catalog.base.XBCGroupSpec;
import org.xbup.lib.core.catalog.base.XBCNode;
import org.xbup.lib.core.catalog.base.XBCRev;
import org.xbup.lib.core.catalog.base.service.XBCNodeService;
import org.xbup.lib.core.catalog.base.service.XBCRevService;
import org.xbup.lib.core.catalog.base.service.XBCSpecService;
import org.xbup.lib.core.catalog.base.service.XBCXFileService;
import org.xbup.lib.core.catalog.base.service.XBCXIconService;
import org.xbup.lib.core.catalog.base.service.XBCXLineService;
import org.xbup.lib.core.catalog.base.service.XBCXPaneService;
import org.xbup.lib.core.catalog.base.service.XBCXPlugService;
import org.xbup.lib.core.catalog.base.service.XBCXStriService;
import org.xbup.lib.core.util.CopyStreamUtils;
import org.xbup.lib.catalog.XBAECatalog;
import org.xbup.lib.catalog.entity.XBEBlockCons;
import org.xbup.lib.catalog.entity.XBEBlockJoin;
import org.xbup.lib.catalog.entity.XBEBlockListCons;
import org.xbup.lib.catalog.entity.XBEBlockListJoin;
import org.xbup.lib.catalog.entity.XBEBlockRev;
import org.xbup.lib.catalog.entity.XBEBlockSpec;
import org.xbup.lib.catalog.entity.XBEFormatCons;
import org.xbup.lib.catalog.entity.XBEFormatJoin;
import org.xbup.lib.catalog.entity.XBEFormatRev;
import org.xbup.lib.catalog.entity.XBEFormatSpec;
import org.xbup.lib.catalog.entity.XBEGroupCons;
import org.xbup.lib.catalog.entity.XBEGroupJoin;
import org.xbup.lib.catalog.entity.XBEGroupRev;
import org.xbup.lib.catalog.entity.XBEGroupSpec;
import org.xbup.lib.catalog.entity.XBEItem;
import org.xbup.lib.catalog.entity.XBENode;
import org.xbup.lib.catalog.entity.XBERev;
import org.xbup.lib.catalog.entity.XBERoot;
import org.xbup.lib.catalog.entity.XBESpec;
import org.xbup.lib.catalog.entity.XBEXBlockLine;
import org.xbup.lib.catalog.entity.XBEXBlockPane;
import org.xbup.lib.catalog.entity.XBEXDesc;
import org.xbup.lib.catalog.entity.XBEXFile;
import org.xbup.lib.catalog.entity.XBEXHDoc;
import org.xbup.lib.catalog.entity.XBEXIcon;
import org.xbup.lib.catalog.entity.XBEXIconMode;
import org.xbup.lib.catalog.entity.XBEXLanguage;
import org.xbup.lib.catalog.entity.XBEXName;
import org.xbup.lib.catalog.entity.XBEXPlugLine;
import org.xbup.lib.catalog.entity.XBEXPlugPane;
import org.xbup.lib.catalog.entity.XBEXPlugin;
import org.xbup.lib.catalog.entity.XBEXStri;
import org.xbup.lib.catalog.entity.service.XBENodeService;
import org.xbup.lib.catalog.entity.service.XBERevService;
import org.xbup.lib.catalog.entity.service.XBESpecService;
import org.xbup.lib.catalog.entity.service.XBEXDescService;
import org.xbup.lib.catalog.entity.service.XBEXFileService;
import org.xbup.lib.catalog.entity.service.XBEXIconService;
import org.xbup.lib.catalog.entity.service.XBEXLangService;
import org.xbup.lib.catalog.entity.service.XBEXLineService;
import org.xbup.lib.catalog.entity.service.XBEXNameService;
import org.xbup.lib.catalog.entity.service.XBEXPaneService;
import org.xbup.lib.catalog.entity.service.XBEXPlugService;
import org.xbup.lib.catalog.entity.service.XBEXStriService;
import org.xbup.lib.core.catalog.base.XBCSpecDef;
import org.xbup.lib.core.catalog.base.service.XBCXDescService;
import org.xbup.lib.core.catalog.base.service.XBCXNameService;

/**
 * Handler for processing Web Service Calls with PHP Catalog interface.
 *
 * @version 0.1.24 2015/01/07
 * @author XBUP Project (http://xbup.org)
 */
public class XBCUpdatePHPHandler implements XBCUpdateHandler {

    private XBCUpdatePHPPort port;
    private XBAECatalog catalog;
    private Long lang;
    private XBEXLanguage localLang;
    private EntityManager em;
    private List<XBCUpdateListener> wsListeners = new ArrayList<>();
    private boolean usage;
    private Map<XBCRev, Long> revCache;

    public XBCUpdatePHPHandler(XBAECatalog catalog) {
        this.catalog = catalog;
        em = catalog.getEntityManager();
        port = null;
        lang = new Long(0);
        localLang = null;
        usage = false;
    }

    /**
     * Initialize WebService
     */
    @Override
    public void init() {
//        XBCatalogPHPService service = new XBCatalogPHPService();
        fireUsageEvent(true);
        port = new XBCUpdatePHPPort();
        reinit();
        fireUsageEvent(false);
    }

    public void reinit() {
        try {
            lang = port.getLanguageId("en");
        } catch (Exception ex) {
            lang = new Long(0);
        }
        XBEXLangService langService = new XBEXLangService(catalog);
        if (langService != null) {
            localLang = (XBEXLanguage) langService.getDefaultLang();
        }
        if (localLang == null) {
            localLang = new XBEXLanguage();
            localLang.setId(lang);
            localLang.setLangCode("en");
            EntityTransaction tx = em.getTransaction();
            tx.begin();
            em.persist(localLang);
            tx.commit();
        }

        // TODO: Icon modes - currently static only
        XBEXIconService iconService = (XBEXIconService) catalog.getCatalogService(XBCXIconService.class);
        if (iconService != null) {
            if (iconService.getIconMode(new Long(1)) == null) {
                XBEXIconMode iconMode = new XBEXIconMode();
                iconMode.setCaption("PNG 16x16");
                iconMode.setId(new Long(1));
                iconMode.setMIME("image/png");
                iconMode.setType(new Long(1));
                EntityTransaction tx = em.getTransaction();
                tx.begin();
                em.persist(iconMode);
                iconMode = new XBEXIconMode();
                iconMode.setCaption("PNG 32x32");
                iconMode.setId(new Long(2));
                iconMode.setMIME("image/png");
                iconMode.setType(new Long(1));
                em.persist(iconMode);
                tx.commit();
            }
        }
    }

    public XBCUpdatePHPPort getPort() {
        return port;
    }

    public void setPort(XBCUpdatePHPPort port) {
        this.port = port;
    }

    @Override
    public XBContext updateTypeSpecFromWS(Long[] path, long specId) {
        try {
            fireUsageEvent(true);
            // Test existence
            ItemInfo result = port.getFormatCatalogSpecInfo(path, new Long(specId), lang);
            if (result == null) {
                return null;
            }
            XBEFormatSpec spec = updateFormatSpec(path, new Long(specId));
            if (spec == null) {
                return null;
            }
//            return new XBContext(new XBCPContextFormat(catalog,spec));
            XBContext context = new XBContext();
            // TODO new XBContext(catalog, spec)
            return context;
        } catch (Exception ex) {
            // TODO
            Logger.getLogger(XBCUpdatePHPHandler.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public XBEFormatSpec updateFormatSpec(Long[] path, Long specId) {
        XBENodeService nodeService = (XBENodeService) catalog.getCatalogService(XBCNodeService.class);
        XBESpecService specService = (XBESpecService) catalog.getCatalogService(XBCSpecService.class);
        if (!processNodePath(path)) {
            return null;
        }
        XBENode node = nodeService.findNodeByXBPath(path);
        if (node == null) {
            return null;
        }
        XBESpec spec = specService.findFormatSpecByXB(node, specId);
        if (spec == null) {
            fireUsageEvent(true);
            spec = addFormatSpecFromWS(node, specId);
            if (spec == null) {
                return null;
            }
        }
        return (XBEFormatSpec) spec;
    }

    @Override
    public XBEGroupSpec updateGroupSpec(Long[] path, Long specId) {
        if (!processNodePath(path)) {
            return null;
        }
        XBENodeService nodeService = (XBENodeService) catalog.getCatalogService(XBCNodeService.class);
        XBESpecService specService = (XBESpecService) catalog.getCatalogService(XBCSpecService.class);
        XBENode node = nodeService.findNodeByXBPath(path);
        if (node == null) {
            return null;
        }
        XBESpec spec = specService.findGroupSpecByXB(node, specId);
        if (spec == null) {
            fireUsageEvent(true);
            spec = addGroupSpecFromWS(node, specId);
            if (spec == null) {
                return null;
            }
        }
        return (XBEGroupSpec) spec;
    }

    @Override
    public XBEBlockSpec updateBlockSpec(Long[] path, Long specId) {
        if (!processNodePath(path)) {
            return null;
        }
        XBENodeService nodeService = (XBENodeService) catalog.getCatalogService(XBCNodeService.class);
        XBESpecService specService = (XBESpecService) catalog.getCatalogService(XBCSpecService.class);
        XBENode node = nodeService.findNodeByXBPath(path);
        if (node == null) {
            return null;
        }
        XBESpec spec = specService.findBlockSpecByXB(node, specId);
        if (spec == null) {
            fireUsageEvent(true);
            spec = addBlockSpecFromWS(node, specId);
            if (spec == null) {
                return null;
            }
        }
        return (XBEBlockSpec) spec;
    }
    /*
     public XBAttributeType updateAttribSpec(Long[] path, Long specId) {
     if (!processAttribNodesPath(path)) return null;
     XBAttributeTypeNode node = catalog.getAttributeTypeTree().findNodeByPath(path);
     if (node==null) return null;
     XBAttributeType spec = catalog.getAttributeTypeTree().findSpecbyXB(node,specId);
     if (spec==null) spec = addAttribSpecFromWS(node,specId);
     return spec;
     }
     */

    @Override
    public XBEFormatSpec addFormatSpecFromWS(XBCNode node, Long specId) {
        XBENodeService nodeService = (XBENodeService) catalog.getCatalogService(XBCNodeService.class);
        fireUsageEvent(true);
        Long[] path = nodeService.getNodeXBPath(node);
        ItemInfo info = port.getFormatCatalogSpecInfo(path, specId, lang);
        if (info.getXbIndex() == null) {
            return null;
        }
        XBESpec spec = new XBEFormatSpec();
        spec.setParent((XBENode) node);
        updateItem(spec, info);
        XBERev rev;
        Long max = port.getFormatCatalogSpecMaxRevIndex(path, specId);
        if (max != null) {
            for (Long i = new Long(0); i.intValue() <= max; i = new Long(i.longValue() + 1)) {
                EntityTransaction tx = em.getTransaction();
                tx.begin();
                ItemRevision itemRev = port.getFormatSpecRevision(path, specId, i);
                rev = new XBEFormatRev();
                rev.setParent(spec);
                rev.setXBIndex(itemRev.getXBIndex());
                rev.setXBLimit(itemRev.getXBLimit());
                em.persist(rev);
                tx.commit();
            }
        }
        return (XBEFormatSpec) spec;
    }

    @Override
    public XBEGroupSpec addGroupSpecFromWS(XBCNode node, Long specId) {
        XBENodeService nodeService = (XBENodeService) catalog.getCatalogService(XBCNodeService.class);
        fireUsageEvent(true);
        Long[] path = nodeService.getNodeXBPath(node);
        ItemInfo info = port.getGroupCatalogSpecInfo(path, specId, lang);
        if (info.getXbIndex() == null) {
            return null;
        }
        XBESpec spec = new XBEGroupSpec();
        spec.setParent((XBENode) node);
        updateItem(spec, info);
        XBERev rev;
        Long max = port.getGroupCatalogSpecMaxRevIndex(path, specId);
        if (max != null) {
            for (Long i = new Long(0); i.intValue() <= max; i = new Long(i.longValue() + 1)) {
                EntityTransaction tx = em.getTransaction();
                tx.begin();
                ItemRevision itemRev = port.getGroupSpecRevision(path, specId, i);
                rev = new XBEGroupRev();
                rev.setParent(spec);
                rev.setXBIndex(itemRev.getXBIndex());
                rev.setXBLimit(itemRev.getXBLimit());
                em.persist(rev);
                tx.commit();
            }
        }
        return (XBEGroupSpec) spec;
    }

    @Override
    public XBEBlockSpec addBlockSpecFromWS(XBCNode node, Long specId) {
        XBENodeService nodeService = (XBENodeService) catalog.getCatalogService(XBCNodeService.class);
        fireUsageEvent(true);
        Long[] path = nodeService.getNodeXBPath(node);
        ItemInfo info = port.getBlockCatalogSpecInfo(path, specId, lang);
        if (info.getXbIndex() == null) {
            return null;
        }
        XBESpec spec = new XBEBlockSpec();
        spec.setParent((XBENode) node);
        updateItem(spec, info);
        XBERev rev;
        Long max = port.getBlockCatalogSpecMaxRevIndex(path, specId);
        if (max != null) {
            for (Long i = new Long(0); i.intValue() <= max; i = new Long(i.longValue() + 1)) {
                EntityTransaction tx = em.getTransaction();
                tx.begin();
                ItemRevision itemRev = port.getBlockSpecRevision(path, specId, i);
                rev = new XBEBlockRev();
                rev.setParent(spec);
                rev.setXBIndex(itemRev.getXBIndex());
                rev.setXBLimit(itemRev.getXBLimit());
                em.persist(rev);
                tx.commit();
                revCache.put(rev, itemRev.getId());
            }
        }
        return (XBEBlockSpec) spec;
    }

    /**
     * Adds node from WS Catalog interface
     *
     */
    @Override
    public XBENode addNodeFromWS(XBCNode node, Long nodeId) {
        XBENodeService nodeService = (XBENodeService) catalog.getCatalogService(XBCNodeService.class);
        fireUsageEvent(true);
        Long[] oldPath = nodeService.getNodeXBPath(node);
        if (oldPath == null) {
            return null;
        }
        Long[] newPath = new Long[oldPath.length + 1];
        System.arraycopy(oldPath, 0, newPath, 0, oldPath.length);
        newPath[newPath.length - 1] = nodeId;
        ItemInfo info = port.getCatalogNodeInfo(newPath, lang);
        if (info.getXbIndex() == null) {
            return null;
        }
        XBENode newNode = new XBENode();
        newNode.setOwner((XBENode) node);
        updateItem(newNode, info);
        return newNode;
    }

    @Override
    public boolean processNodePath(Long[] path) {
        XBENodeService nodeService = (XBENodeService) catalog.getCatalogService(XBCNodeService.class);
        XBENode node = nodeService.getRootNode();
        XBENode parent;
        for (int i = 0; i < path.length; i++) {
            parent = node;
            Long elem = (Long) path[i];
            node = (XBENode) nodeService.getSubNode(parent, elem);
            if (node == null) {
                node = addNodeFromWS(parent, elem);
            }
            if (node == null) {
                return false;
            }
        }
        return true;
    }

    public XBAECatalog getCatalog() {
        return catalog;
    }

    public void setCatalog(XBAECatalog catalog) {
        this.catalog = catalog;
    }

    public Long getLang() {
        return lang;
    }

    public void setLang(Long lang) {
        this.lang = lang;
    }
    /*
     public static List<Object> toObjectList(List<Long> list) {
     ArrayList<Object> result = new ArrayList<Object>(); // TODO
     for (Iterator it = list.iterator(); it.hasNext();) result.add(it.next());
     return result;
     }
     */

    public void updateItem(XBEItem item, ItemInfo info) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            item.setXBIndex(info.getXbIndex()); // Or defensive copy should be better
            em.persist(item);
            XBEXName name = new XBEXName();
            name.setItem(item);
            name.setText(info.getName());
            name.setLang(localLang);
            em.persist(name);
            XBEXDesc desc = new XBEXDesc();
            desc.setItem(item);
            desc.setText(info.getDesc());
            desc.setLang(localLang);
            em.persist(desc);
            tx.commit();
        } catch (Exception ex) {
            Logger.getLogger(XBCUpdatePHPHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public EntityManager getEm() {
        return em;
    }

    public void setEm(EntityManager em) {
        this.em = em;
//        tx = em.getTransaction();
    }

    @Override
    public void addWSListener(XBCUpdateListener wsl) {
        wsListeners.add(wsl);
    }

    @Override
    public void removeWSListener(XBCUpdateListener wsl) {
        wsListeners.remove(wsl);
    }

    @Override
    public void fireUsageEvent(boolean usage) {
        if (this.usage != usage) {
            /*            if (!usage) {
             if (tx.isActive()) tx.commit();
             } else tx.begin(); */
            for (Iterator it = wsListeners.iterator(); it.hasNext();) {
                ((XBCUpdateListener) it.next()).webServiceUsage(usage);
            }
            this.usage = usage;
        }
    }

    /**
     * Recursively receive all information about given specification node
     */
    @Override
    public void processAllData(Long[] path) {
        XBENodeService nodeService = (XBENodeService) catalog.getCatalogService(XBCNodeService.class);
        // Update runs in three rounds

        // First nodes, specifications, files
        // Second plugins
        // Third binds, line editors and pane editors
        // Optimalization for root node:
        revCache = new HashMap<XBCRev, Long>();
        XBENode node = nodeService.findNodeByXBPath(path);

        if (isRootPath(path)) {
            ItemInfo info = port.getCatalogFullContent(path, lang);
            updateItem(node, info);
            processAllNodes(node);
            processAllPlugins(node);
            processAllBinds(node);
            port.finishCompound();
        } else {
            ItemInfo info = port.getCatalogNodeInfo(path, lang);
            updateItem(node, info);
            processAllNodes(node);
            processAllPlugins(node);
            processAllBinds(node);
        }
        revCache.clear();
    }

    public void processAllNodes(XBENode node) {
        XBENodeService nodeService = (XBENodeService) catalog.getCatalogService(XBCNodeService.class);
        if (node != null) {
            processNodeFiles(node);
            processNode(node);
            Long[] path = nodeService.getNodeXBPath(node);
            Long max = port.getSubNodeCatalogMaxIndex(path);
            if (max != null) {
                for (int i = 0; i <= max; i++) {
                    XBENode sub = addNodeFromWS(node, new Long(i));
                    processAllNodes(sub);
                }
            }
            processNodeFormatSpecs(node);
            processNodeGroupSpecs(node);
            processNodeBlockSpecs(node);
        }
    }

    private void processAllPlugins(XBENode node) {
        XBENodeService nodeService = (XBENodeService) catalog.getCatalogService(XBCNodeService.class);
        if (node != null) {
            List<XBCNode> nodes = nodeService.getSubNodes(node);
            for (Iterator<XBCNode> it = nodes.iterator(); it.hasNext();) {
                processAllPlugins((XBENode) it.next());
            }
            processNodePlugins(node);
        }
    }

    public void processAllBinds(XBENode node) {
        XBENodeService nodeService = (XBENodeService) catalog.getCatalogService(XBCNodeService.class);
        XBESpecService specService = (XBESpecService) catalog.getCatalogService(XBCSpecService.class);
        XBERevService revService = (XBERevService) catalog.getCatalogService(XBCRevService.class);
        if (node != null) {
            List<XBCNode> nodes = nodeService.getSubNodes(node);
            for (Iterator<XBCNode> it = nodes.iterator(); it.hasNext();) {
                processAllBinds((XBENode) it.next());
            }
            if (node.getParent() != null) { // Skip testing specifications
                List<XBCFormatSpec> formatSpecs = specService.getFormatSpecs(node);
                for (Iterator<XBCFormatSpec> it = formatSpecs.iterator(); it.hasNext();) {
                    processFormatSpecBinds((XBEFormatSpec) it.next());
                }
                List<XBCGroupSpec> groupSpecs = specService.getGroupSpecs(node);
                for (Iterator<XBCGroupSpec> it = groupSpecs.iterator(); it.hasNext();) {
                    processGroupSpecBinds((XBEGroupSpec) it.next());
                }
                List<XBCBlockSpec> blockSpecs = specService.getBlockSpecs(node);
                for (Iterator<XBCBlockSpec> it = blockSpecs.iterator(); it.hasNext();) {
                    XBEBlockSpec spec = (XBEBlockSpec) it.next();
                    processBlockSpecBinds(spec);
                    List<XBCRev> revisions = revService.getRevs(spec);
                    for (XBCRev rev : revisions) {
                        processRevLines(rev);
                        processRevPanes(rev);
                    }
                }
            }
        }
    }

    public void processNodeBlockSpecs(XBENode node) {
        XBENodeService nodeService = (XBENodeService) catalog.getCatalogService(XBCNodeService.class);
        if (node != null) {
            Long[] path = nodeService.getNodeXBPath(node);
            Long maxIndex = port.getBlockCatalogSpecMaxIndex(path);
            if (maxIndex != null) {
                for (int i = 0; i <= maxIndex; i++) {
                    XBEBlockSpec spec = addBlockSpecFromWS(node, new Long(i));
                    processBlockSpecIcons(spec);
                }
            }
        }
    }

    public void processNodeGroupSpecs(XBENode node) {
        XBENodeService nodeService = (XBENodeService) catalog.getCatalogService(XBCNodeService.class);
        if (node != null) {
            Long[] path = nodeService.getNodeXBPath(node);
            Long maxIndex = port.getGroupCatalogSpecMaxIndex(path);
            if (maxIndex != null) {
                for (int index = 0; index <= maxIndex; index++) {
                    XBEGroupSpec spec = addGroupSpecFromWS(node, new Long(index));
                    /*                Long[] path = catalog.getNodeService().getNodeXBPath(node);
                     Long max = port.getGroupCatalogSpecMaxBindId(path, new Long(index));
                     XBEBind bind;
                     XBESpec toTarget;
                     if (max!=null) for (Long i = new Long(0); i.intValue() < max; i = new Long(i.longValue()+1)) {
                     RevisionPath specPath = port.getGroupCatalogBindTargetPath(path,new Long(index), i);
                     if (specPath!=null) {
                     toTarget = updateBlockSpec(specPath.getPath(), specPath.getSpecId());
                     if (toTarget!=null) {
                     EntityTransaction tx = em.getTransaction();
                     tx.begin();
                     bind = new XBEBind(); // TODO XBEBindGroup
                     bind.setNode(spec);
                     bind.setTarget(toTarget);
                     bind.setXBIndex(i);
                     em.persist(bind);
                     tx.commit();
                     }
                     }
                     } */
                    processGroupSpecIcons(spec);
                }
            }
        }
    }

    public void processNodeFormatSpecs(XBENode node) {
        XBENodeService nodeService = (XBENodeService) catalog.getCatalogService(XBCNodeService.class);
        if (node != null) {
            Long[] path = nodeService.getNodeXBPath(node);
            Long maxIndex = port.getFormatCatalogSpecMaxIndex(path);
            if (maxIndex != null) {
                for (int index = 0; index <= maxIndex; index++) {
                    XBEFormatSpec spec = addFormatSpecFromWS(node, new Long(index));
                    /*                Long max = port.getFormatCatalogSpecMaxBindId(path, new Long(index));
                     XBEBind bind;
                     XBESpec toTarget;
                     if (max!=null) for (Long i = new Long(0); i.intValue() < max; i = new Long(i.longValue()+1)) {
                     RevisionPath specPath = port.getFormatCatalogBindTargetPath(path,new Long(index), i);
                     if (specPath!=null) {
                     toTarget = updateGroupSpec(specPath.getPath(), specPath.getSpecId());
                     if (toTarget!=null) {
                     EntityTransaction tx = em.getTransaction();
                     tx.begin();
                     bind = new XBEBind(); // TODO XBEBindFormat
                     bind.setNode(spec);
                     bind.setTarget(toTarget);
                     bind.setXBIndex(i);
                     em.persist(bind);
                     tx.commit();
                     }
                     }
                     } */
                    processFormatSpecIcons(spec);
                }
            }
        }
    }

    public void processFormatSpecBinds(XBEFormatSpec spec) {
        if (spec == null) {
            return;
        }
        XBENodeService nodeService = (XBENodeService) catalog.getCatalogService(XBCNodeService.class);
        XBESpecService specService = (XBESpecService) catalog.getCatalogService(XBCSpecService.class);
        XBERevService revService = (XBERevService) catalog.getCatalogService(XBCRevService.class);
        Long[] path = nodeService.getNodeXBPath(spec.getParent());
        Long maxIndex = port.getFormatCatalogSpecMaxBindId(path, spec.getXBIndex());
        if (maxIndex != null) {
            for (long index = 0; index <= maxIndex; index++) {
                RevisionPath revPath = port.getFormatCatalogBindTargetPath(path, spec.getXBIndex(), index, lang);
                if (revPath != null && revPath.getSpecId() != null) {
                    XBENode specNode = nodeService.findNodeByXBPath(revPath.getPath());
                    if (revPath.getBindType() == 0) {
                        XBEGroupSpec target = specService.findGroupSpecByXB(specNode, revPath.getSpecId());
//                    if (target == null) break;
                        XBEGroupRev rev = (XBEGroupRev) revService.findRevByXB(target, revPath.getRevXBId());
                        EntityTransaction tx = em.getTransaction();
                        tx.begin();
                        XBEFormatCons bind = new XBEFormatCons();
                        bind.setSpec(spec);
                        bind.setTarget(rev);
                        bind.setXBIndex(index);
                        em.persist(bind);
                        tx.commit();
                        setSpecDefInfo(bind, revPath);
                    } else {
                        XBEFormatSpec target = specService.findFormatSpecByXB(specNode, revPath.getSpecId());
//                    if (target == null) break;
                        XBEFormatRev rev = (XBEFormatRev) revService.findRevByXB(target, revPath.getRevXBId());
                        EntityTransaction tx = em.getTransaction();
                        tx.begin();
                        XBEFormatJoin bind = new XBEFormatJoin();
                        bind.setSpec(spec);
                        bind.setTarget(rev);
                        bind.setXBIndex(index);
                        em.persist(bind);
                        tx.commit();
                        setSpecDefInfo(bind, revPath);
                    }
                }
            }
        }
    }

    public void processGroupSpecBinds(XBEGroupSpec spec) {
        if (spec == null) {
            return;
        }
        XBENodeService nodeService = (XBENodeService) catalog.getCatalogService(XBCNodeService.class);
        XBESpecService specService = (XBESpecService) catalog.getCatalogService(XBCSpecService.class);
        XBERevService revService = (XBERevService) catalog.getCatalogService(XBCRevService.class);
        Long[] path = nodeService.getNodeXBPath(spec.getParent());
        Long maxIndex = port.getGroupCatalogSpecMaxBindId(path, spec.getXBIndex());
        if (maxIndex != null) {
            for (long index = 0; index <= maxIndex; index++) {
                RevisionPath revPath = port.getGroupCatalogBindTargetPath(path, spec.getXBIndex(), index, lang);
                if (revPath != null && revPath.getSpecId() != null) {
                    XBENode specNode = nodeService.findNodeByXBPath(revPath.getPath());
                    if (revPath.getBindType() == 0) {
                        XBEBlockSpec target = specService.findBlockSpecByXB(specNode, revPath.getSpecId());
//                    if (target == null) break;
                        XBEBlockRev rev = (XBEBlockRev) revService.findRevByXB(target, revPath.getRevXBId());
                        EntityTransaction tx = em.getTransaction();
                        tx.begin();
                        XBEGroupCons bind = new XBEGroupCons();
                        bind.setSpec(spec);
                        bind.setTarget(rev);
                        bind.setXBIndex(index);
                        em.persist(bind);
                        tx.commit();
                        setSpecDefInfo(bind, revPath);
                    } else {
                        XBEGroupSpec target = specService.findGroupSpecByXB(specNode, revPath.getSpecId());
//                    if (target == null) break;
                        XBEGroupRev rev = (XBEGroupRev) revService.findRevByXB(target, revPath.getRevXBId());
                        EntityTransaction tx = em.getTransaction();
                        tx.begin();
                        XBEGroupJoin bind = new XBEGroupJoin();
                        bind.setSpec(spec);
                        bind.setTarget(rev);
                        bind.setXBIndex(index);
                        em.persist(bind);
                        tx.commit();
                        setSpecDefInfo(bind, revPath);
                    }
                }
            }
        }
    }

    public void processBlockSpecBinds(XBEBlockSpec spec) {
        if (spec == null) {
            return;
        }
        XBENodeService nodeService = (XBENodeService) catalog.getCatalogService(XBCNodeService.class);
        XBESpecService specService = (XBESpecService) catalog.getCatalogService(XBCSpecService.class);
        XBERevService revService = (XBERevService) catalog.getCatalogService(XBCRevService.class);
        Long[] path = nodeService.getNodeXBPath(spec.getParent());
        Long maxIndex = port.getBlockCatalogSpecMaxBindId(path, spec.getXBIndex());
        if (maxIndex != null) {
            for (long index = 0; index <= maxIndex; index++) {
                RevisionPath revPath = port.getBlockCatalogBindTargetPath(path, spec.getXBIndex(), index, lang);
                if (revPath != null) {
                    XBENode specNode = null;
                    if (revPath.getSpecId() != null) {
                        specNode = nodeService.findNodeByXBPath(revPath.getPath());
                    }
                    if (revPath.getBindType() == 0) {
                        XBEBlockRev rev = null;
                        if (revPath.getSpecId() != null) {
                            XBEBlockSpec target = specService.findBlockSpecByXB(specNode, revPath.getSpecId());
                            rev = (XBEBlockRev) revService.findRevByXB(target, revPath.getRevXBId());
                        }
                        EntityTransaction tx = em.getTransaction();
                        tx.begin();
                        XBEBlockCons bind = new XBEBlockCons();
                        bind.setSpec(spec);
                        bind.setTarget(rev);
                        bind.setXBIndex(index);
                        em.persist(bind);
                        tx.commit();
                        setSpecDefInfo(bind, revPath);
                    } else if (revPath.getBindType() == 1) {
                        XBEBlockRev rev = null;
                        if (revPath.getSpecId() != null) {
                            XBEBlockSpec target = specService.findBlockSpecByXB(specNode, revPath.getSpecId());
                            rev = (XBEBlockRev) revService.findRevByXB(target, revPath.getRevXBId());
                        }
                        EntityTransaction tx = em.getTransaction();
                        tx.begin();
                        XBEBlockJoin bind = new XBEBlockJoin();
                        bind.setSpec(spec);
                        bind.setTarget(rev);
                        bind.setXBIndex(index);
                        em.persist(bind);
                        tx.commit();
                        setSpecDefInfo(bind, revPath);
                    } else if (revPath.getBindType() == 2) {
                        XBEBlockRev rev = null;
                        if (revPath.getSpecId() != null) {
                            XBEBlockSpec target = specService.findBlockSpecByXB(specNode, revPath.getSpecId());
                            rev = (XBEBlockRev) revService.findRevByXB(target, revPath.getRevXBId());
                        }
                        EntityTransaction tx = em.getTransaction();
                        tx.begin();
                        XBEBlockListCons bind = new XBEBlockListCons();
                        bind.setSpec(spec);
                        bind.setTarget(rev);
                        bind.setXBIndex(index);
                        em.persist(bind);
                        tx.commit();
                        setSpecDefInfo(bind, revPath);
                    } else if (revPath.getBindType() == 3) {
                        XBEBlockSpec target = specService.findBlockSpecByXB(specNode, revPath.getSpecId());
                        XBEBlockRev rev = null;
                        if (revPath.getRevXBId() != null) {
                            rev = (XBEBlockRev) revService.findRevByXB(target, revPath.getRevXBId());
                        }
                        EntityTransaction tx = em.getTransaction();
                        tx.begin();
                        XBEBlockListJoin bind = new XBEBlockListJoin();
                        bind.setSpec(spec);
                        bind.setTarget(rev);
                        bind.setXBIndex(index);
                        em.persist(bind);
                        tx.commit();
                        setSpecDefInfo(bind, revPath);
                    }
                }
            }
        }
    }

    public void setSpecDefInfo(XBCSpecDef bind, RevisionPath revPath) {
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        XBEXNameService nameService = (XBEXNameService) catalog.getCatalogService(XBCXNameService.class);
        XBEXDescService descService = (XBEXDescService) catalog.getCatalogService(XBCXDescService.class);
        XBEXStriService striService = (XBEXStriService) catalog.getCatalogService(XBCXStriService.class);
        nameService.setDefaultText(bind, revPath.getName());
        descService.setDefaultText(bind, revPath.getDesc());
        striService.setItemStringIdText(bind, revPath.getStri());
        tx.commit();
    }

    private void processNodeFiles(XBENode node) {
        XBENodeService nodeService = (XBENodeService) catalog.getCatalogService(XBCNodeService.class);
        XBEXStriService striService = (XBEXStriService) catalog.getCatalogService(XBCXStriService.class);
        if (node != null) {
            Long[] path = nodeService.getNodeXBPath(node);
            ItemFile itemFile = port.getCatalogNodeFile(path);
            if (itemFile == null) {
                return;
            }
            if ((itemFile.getFileName() == null) || ("".equals(itemFile.getFileName()))) {
                return;
            }
            EntityTransaction tx = em.getTransaction();
            tx.begin();
            XBEXStri stri = striService.createItem();
            String parentPath = "";
            XBEXFileService fileService = (XBEXFileService) catalog.getCatalogService(XBCXFileService.class);
            XBEXStri parentStringId = (XBEXStri) striService.getItemStringId(node.getParent());
            if (node.getParent() != null) {
                parentPath = striService.getFullPath(parentStringId);
                parentPath += "/" + itemFile.getFileName();
            }
            if (fileService != null) {
                new File(catalog.getFileRepositoryPath() + parentPath).mkdirs();
            }

            String nodePath = null;
            if (node.getParent() == null) {
                nodePath = "/";
            } else {
                nodePath = parentStringId.getNodePath();
                if (parentStringId.getItem().getParent() == null) {
                    nodePath = "/";
                } else {
                    if (!"/".equals(nodePath)) {
                        nodePath += "/";
                    }
                    nodePath += parentStringId.getText();
                }
            }
            stri.setItem(node);
            stri.setText(itemFile.getFileName());
            stri.setNodePath(nodePath);
            em.persist(stri);

            Long prev = null;
            do {
                itemFile = port.getCatalogFile(path, prev);
                if (itemFile != null) {
                    XBEXFile file = new XBEXFile();
                    file.setFilename(itemFile.getFileName());
                    file.setNode(node);
                    em.persist(file);
                    if (fileService != null) {
                        InputStream istream = port.getFileContent(parentPath + "/" + itemFile.getFileName());
                        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
                        if (istream != null) {
                            try {
                                OutputStream ostream = new FileOutputStream(new File(catalog.getFileRepositoryPath() + parentPath + "/" + itemFile.getFileName()));
                                CopyStreamUtils.copyInputStreamToTwoOutputStreams(istream, ostream, byteStream);
                                istream.close();
                                ostream.close();
                                file.setContent(byteStream.toByteArray());
                                byteStream.close();
                            } catch (FileNotFoundException ex) {
                                Logger.getLogger(XBCUpdatePHPHandler.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (IOException ex) {
                                Logger.getLogger(XBCUpdatePHPHandler.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }

                    }
                    prev = itemFile.getId();
                }
            } while (itemFile != null);
            tx.commit();
        }
    }

    private void processNode(XBENode node) {
        if (node == null) {
            return;
        }
        XBENodeService nodeService = (XBENodeService) catalog.getCatalogService(XBCNodeService.class);
        Long[] path = nodeService.getNodeXBPath(node);
        processItem(node, port.getCatalogNodeId(path));
    }

    private void processFormatSpecIcons(XBEFormatSpec spec) {
        if (spec == null) {
            return;
        }
        XBENodeService nodeService = (XBENodeService) catalog.getCatalogService(XBCNodeService.class);
        Long[] path = nodeService.getNodeXBPath(spec.getParent());
        processItem(spec, port.getCatalogFormatSpecId(path, spec.getXBIndex()));
    }

    private void processGroupSpecIcons(XBEGroupSpec spec) {
        if (spec == null) {
            return;
        }
        XBENodeService nodeService = (XBENodeService) catalog.getCatalogService(XBCNodeService.class);
        Long[] path = nodeService.getNodeXBPath(spec.getParent());
        processItem(spec, port.getCatalogGroupSpecId(path, spec.getXBIndex()));
    }

    private void processBlockSpecIcons(XBEBlockSpec spec) {
        if (spec == null) {
            return;
        }
        XBENodeService nodeService = (XBENodeService) catalog.getCatalogService(XBCNodeService.class);
        Long[] path = nodeService.getNodeXBPath(spec.getParent());
        processItem(spec, port.getCatalogBlockSpecId(path, spec.getXBIndex()));
    }

    private void processItem(XBEItem item, Long itemId) {
        if (itemId == null) {
            return;
        }
        XBENodeService nodeService = (XBENodeService) catalog.getCatalogService(XBCNodeService.class);
        Long max = port.getItemIconMaxIndex(itemId);
        if (max != null) {
            for (Long i = new Long(0); i.intValue() <= max; i = new Long(i.longValue() + 1)) {
                EntityTransaction tx = em.getTransaction();
                ItemFile itemIcon = port.getItemIcon(itemId, i);
                if (itemIcon != null) {
                    XBEXIcon icon;
                    Long[] path = port.getNodePath(itemIcon.getXBIndex());
                    XBENode node = nodeService.findNodeByXBPath(path);
                    XBEXFileService fileService = (XBEXFileService) catalog.getCatalogService(XBCXFileService.class);
                    XBEXIconService iconService = (XBEXIconService) catalog.getCatalogService(XBCXIconService.class);
                    XBEXFile file = (XBEXFile) fileService.findFile(node, itemIcon.getFileName());
                    tx.begin();
                    icon = new XBEXIcon();
                    icon.setParent(item);
                    icon.setIconFile(file);
                    icon.setMode(iconService.getIconMode(itemIcon.getMode()));
                    em.persist(icon);
                    tx.commit();
                }
            }
        }

        EntityTransaction tx = em.getTransaction();
        ItemFile itemHdoc = port.getItemHdoc(itemId, lang);
        if (itemHdoc != null) {
            XBEXHDoc hdoc;
            Long[] path = port.getNodePath(itemHdoc.getXBIndex());
            XBENode node = nodeService.findNodeByXBPath(path);
            XBEXFileService fileService = (XBEXFileService) catalog.getCatalogService(XBCXFileService.class);
            XBEXFile file = (XBEXFile) fileService.findFile(node, itemHdoc.getFileName());
            if (file != null) {
                tx.begin();
                hdoc = new XBEXHDoc();
                hdoc.setItem(item);
                hdoc.setDocFile(file);
                hdoc.setLang(localLang);
                em.persist(hdoc);
                tx.commit();
            }
        }
    }

    private void processNodePlugins(XBENode node) {
        if (node == null) {
            return;
        }
        XBENodeService nodeService = (XBENodeService) catalog.getCatalogService(XBCNodeService.class);
        Long[] path = nodeService.getNodeXBPath(node);
        Long nodeId = port.getCatalogNodeId(path);
        Long max = port.getPluginCatalogNodeMaxPluginIndex(nodeId);
        XBEXPlugin plugin;
        if (max != null) {
            for (long i = 0; i <= max; i++) {
                ItemPlugin itemPlug = port.getPluginCatalogNodePlugin(nodeId, i);
                EntityTransaction tx = em.getTransaction();
                tx.begin();
                plugin = new XBEXPlugin();
                plugin.setOwner(node);
                plugin.setPluginIndex(itemPlug.getPlugin());
                XBEXFileService fileService = (XBEXFileService) catalog.getCatalogService(XBCXFileService.class);
                Long[] fileNodePath = port.getNodePath(itemPlug.getNodeId());
                XBENode fileNode = nodeService.findNodeByXBPath(fileNodePath);
                XBEXFile file = fileService.findFile(fileNode, itemPlug.getFilename());
                plugin.setPluginFile(file);
                em.persist(plugin);
                tx.commit();

                processPluginLines(plugin, itemPlug.getId());
                processPluginPanes(plugin, itemPlug.getId());
            }
        }
    }

    private void processRevLines(XBCRev rev) {
        XBENodeService nodeService = (XBENodeService) catalog.getCatalogService(XBCNodeService.class);
        Long revId = revCache.get(rev);
        Long max = port.getBlockCatalogRevMaxLineIndex(revId);
        XBEXBlockLine line;
        if (max != null) {
            for (Long i = (long) 0; i.intValue() <= max; i = i + 1) {
                RevisionPlug revPlug = port.getBlockCatalogRevLine(revId, i);
                line = new XBEXBlockLine();
                line.setBlockRev((XBEBlockRev) rev);
                Long[] plugNodePath = port.getNodePath(revPlug.getPlugNode());
                if (plugNodePath != null) {
                    EntityTransaction tx = em.getTransaction();
                    tx.begin();
                    XBENode plugNode = nodeService.findNodeByXBPath(plugNodePath);
                    XBEXPlugService plugService = (XBEXPlugService) catalog.getCatalogService(XBCXPlugService.class);
                    XBEXPlugin plugin = (XBEXPlugin) plugService.findPlugin(plugNode, revPlug.getPlugin());
                    XBEXLineService lineService = (XBEXLineService) catalog.getCatalogService(XBCXLineService.class);
                    XBEXPlugLine plugLine = (XBEXPlugLine) lineService.getPlugLine(plugin, revPlug.getItemId());
                    line.setLine(plugLine);
                    line.setPriority(i);
                    em.persist(line);
                    tx.commit();
                }
            }
        }
    }

    private void processRevPanes(XBCRev rev) {
        XBENodeService nodeService = (XBENodeService) catalog.getCatalogService(XBCNodeService.class);
        Long revId = revCache.get(rev);
        Long max = port.getBlockCatalogRevMaxPaneIndex(revId);
        XBEXBlockPane pane;
        if (max != null) {
            for (Long i = (long) 0; i.intValue() <= max; i = i + 1) {
                RevisionPlug revPlug = port.getBlockCatalogRevPane(revId, i);
                pane = new XBEXBlockPane();
                pane.setBlockRev((XBEBlockRev) rev);
                Long[] plugNodePath = port.getNodePath(revPlug.getPlugNode());
                if (plugNodePath != null) {
                    EntityTransaction tx = em.getTransaction();
                    tx.begin();
                    XBENode plugNode = nodeService.findNodeByXBPath(plugNodePath);
                    XBEXPlugService plugService = (XBEXPlugService) catalog.getCatalogService(XBCXPlugService.class);
                    XBEXPlugin plugin = (XBEXPlugin) plugService.findPlugin(plugNode, revPlug.getPlugin());
                    XBEXPaneService paneService = (XBEXPaneService) catalog.getCatalogService(XBCXPaneService.class);
                    XBEXPlugPane plugPane = (XBEXPlugPane) paneService.getPlugPane(plugin, revPlug.getItemId());
                    pane.setPane(plugPane);
                    pane.setPriority(i);
                    em.persist(pane);
                    tx.commit();
                }
            }
        }
    }

    private void processPluginLines(XBEXPlugin plugin, Long itemId) {
        if (plugin == null) {
            return;
        }
        if (itemId == null) {
            return;
        }
        Long max = port.getPluginCatalogPlugLineMaxIndex(itemId);
        if (max != null) {
            for (Long i = new Long(0); i.intValue() <= max; i = new Long(i.longValue() + 1)) {
                Long plugLineId = port.getPluginCatalogPlugLine(itemId, i);
                if (plugLineId != null) {
                    EntityTransaction tx = em.getTransaction();
                    tx.begin();
                    XBEXPlugLine plugLine = new XBEXPlugLine();
                    plugLine.setPlugin(plugin);
                    plugLine.setLineIndex(i);
                    em.persist(plugLine);
                    tx.commit();
                }
            }
        }
    }

    private void processPluginPanes(XBEXPlugin plugin, Long itemId) {
        if (plugin == null) {
            return;
        }
        if (itemId == null) {
            return;
        }
        Long max = port.getPluginCatalogPlugPaneMaxIndex(itemId);
        if (max != null) {
            for (Long i = new Long(0); i.intValue() <= max; i = new Long(i.longValue() + 1)) {
                Long plugPaneId = port.getPluginCatalogPlugPane(itemId, i);
                if (plugPaneId != null) {
                    EntityTransaction tx = em.getTransaction();
                    tx.begin();
                    XBEXPlugPane plugPane = new XBEXPlugPane();
                    plugPane.setPlugin(plugin);
                    plugPane.setPaneIndex(i);
                    em.persist(plugPane);
                    tx.commit();
                }
            }
        }
    }

    private boolean isRootPath(Long[] path) {
        return (path.length == 0);
    }

    public void updateCatalog(XBERoot root, Date lastUpdate) {
        processAllData(new Long[0]);

        EntityTransaction tx = em.getTransaction();
        tx.begin();
        root.setLastUpdate(lastUpdate);
        em.persist(root);
        em.flush();
        tx.commit();
    }
}
