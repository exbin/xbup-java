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
package org.exbin.xbup.catalog.entity.manager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import org.exbin.xbup.catalog.XBECatalog;
import org.exbin.xbup.catalog.entity.XBENode;
import org.exbin.xbup.core.catalog.base.XBCNode;
import org.exbin.xbup.core.catalog.base.manager.XBCNodeManager;
import org.springframework.stereotype.Repository;

/**
 * XBUP catalog node manager.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
@Repository
public class XBENodeManager extends XBEDefaultCatalogManager<XBCNode> implements XBCNodeManager, Serializable {

    public XBENodeManager() {
        super();
    }

    public XBENodeManager(XBECatalog catalog) {
        super(catalog);
    }

    @Nonnull
    @Override
    public Class getEntityClass() {
        return XBENode.class;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<XBCNode> getSubNodes(XBCNode node) {
        try {
            return (List<XBCNode>) em.createQuery("SELECT object(o) FROM XBNode as o WHERE o.parent.id = " + ((XBENode) node).getId()).getResultList();
        } catch (NoResultException e) {
            return null;
        } catch (Exception ex) {
            Logger.getLogger(XBENodeManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public XBENode getSubNode(XBCNode node, long xbIndex) {
        if (node == null) {
            return null;
        }
        try {
            return (XBENode) em.createQuery("SELECT object(o) FROM XBNode as o WHERE o.parent.id = " + ((XBENode) node).getId() + " AND o.xbIndex = " + xbIndex).getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception ex) {
            Logger.getLogger(XBENodeManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public long getSubNodesCount(XBCNode node) {
        try {
            return (Long) em.createQuery("SELECT count(o) FROM XBNode as o WHERE o.parent.id = " + ((XBENode) node).getId()).getSingleResult();
        } catch (NoResultException ex) {
            return 0;
        } catch (Exception ex) {
            Logger.getLogger(XBENodeManager.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }

    @Override
    public Optional<XBCNode> getMainRootNode() {
        try {
            return Optional.of((XBCNode) em.createQuery("SELECT object(n) FROM XBRoot AS o, XBNode AS n WHERE o.node = n AND o.url IS NULL").getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        } catch (Exception ex) {
            Logger.getLogger(XBENodeManager.class.getName()).log(Level.SEVERE, null, ex);
            return Optional.empty();
        }
    }

    @Override
    public XBENode findNodeByXBPath(Long[] catalogPath) {
        XBENode node = (XBENode) getMainRootNode().get();
        for (Long pathComponent : catalogPath) {
            node = (XBENode) getSubNode(node, pathComponent);
            if (node == null) {
                return null;
            }
        }
        return node;
    }

    @Override
    public XBENode findParentByXBPath(Long[] catalogPath) {
        if (catalogPath.length == 0) {
            return null;
        }
        XBENode node = (XBENode) getMainRootNode().get();
        for (int i = 0; i < catalogPath.length - 1; i++) {
            node = (XBENode) getSubNode(node, catalogPath[i]);
            if (node == null) {
                break;
            }
        }
        return node;
    }

    @Override
    public Long[] getNodeXBPath(XBCNode node) {
        ArrayList<Long> list = new ArrayList<>();
        XBENode current = (XBENode) node;
        while (current != null) {
            if (current.getParent().isPresent()) {
                list.add(0, current.getXBIndex());
            }
            current = (XBENode) current.getParent().orElse(null);
        }
        return (Long[]) list.toArray(new Long[list.size()]);
    }

    @Override
    public XBENode findOwnerByXBPath(Long[] catalogPath) {
        if (catalogPath == null || catalogPath.length == 0) {
            return null;
        }

        XBENode node = (XBENode) getMainRootNode().get();
        for (int i = 0; i < catalogPath.length - 1; i++) {
            node = (XBENode) getSubNode(node, catalogPath[i]);
            if (node == null) {
                break;
            }
        }
        return node;
    }

    @Override
    public Long findMaxSubNodeXB(XBCNode node) {
        if ((node == null) || (!(node instanceof XBENode))) {
            return null;
        }
        try {
            return (Long) em.createQuery("SELECT max(o.xbIndex) FROM XBNode as o WHERE o.parent.id = " + ((XBENode) node).getId()).getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception ex) {
            Logger.getLogger(XBENodeManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public XBENode getSubNodeSeq(XBCNode node, long seq) {
        try {
            Query query = em.createQuery("SELECT object(o) FROM XBNode as o WHERE o.parent.id = " + ((XBENode) node).getId() + " ORDER BY o.id");
            query.setFirstResult((int) seq);
            query.setMaxResults(1);
            return (XBENode) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception ex) {
            Logger.getLogger(XBENodeManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public long getSubNodesSeq(XBCNode node) {
        try {
            return (Long) em.createQuery("SELECT count(o) FROM XBNode as o WHERE o.parent.id = " + ((XBENode) node).getId()).getSingleResult();
        } catch (NoResultException ex) {
            return 0;
        } catch (Exception ex) {
            Logger.getLogger(XBENodeManager.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }

    @Override
    public void removeNodeFully(XBCNode node) {
        long nodeId = node.getId();
        String nodeTreeCond = "(nt.node_id = " + nodeId + " OR nt.owner_id = " + nodeId + ")";

        em.createQuery("DELETE * FROM XBXName WHERE EXISTS(SELECT 1 FROM XBItem it, XBNodeTree nt WHERE it.id = XBXName.item_id AND nt.node_id = it.owner_id AND " + nodeTreeCond + ")").executeUpdate();
        em.createQuery("DELETE * FROM XBXDesc WHERE EXISTS(SELECT 1 FROM XBItem it, XBNodeTree nt WHERE it.id = XBXDesc.item_id AND nt.node_id = it.owner_id AND " + nodeTreeCond + ")").executeUpdate();
        em.createQuery("DELETE * FROM XBXStri WHERE EXISTS(SELECT 1 FROM XBItem it, XBNodeTree nt WHERE it.id = XBXStri.item_id AND nt.node_id = it.owner_id AND " + nodeTreeCond + ")").executeUpdate();

        em.createQuery("DELETE * FROM XBXHDoc WHERE EXISTS(SELECT 1 FROM XBXFile fl, XBNodeTree nt WHERE fl.id = XBXHDoc.file_id AND nt.node_id = fl.node_id AND " + nodeTreeCond + ")").executeUpdate();
        em.createQuery("DELETE * FROM XBXHDoc WHERE EXISTS(SELECT 1 FROM XBItem it, XBNodeTree nt WHERE it.id = XBXHDoc.item_id AND nt.node_id = it.owner_id AND " + nodeTreeCond + ")").executeUpdate();

        em.createQuery("DELETE * FROM XBXIcon WHERE EXISTS(SELECT 1 FROM XBXFile fl, XBNodeTree nt WHERE fl.id = XBXFile.file_id AND nt.node_id = fl.node_id AND " + nodeTreeCond + ")").executeUpdate();
        em.createQuery("DELETE * FROM XBXIcon WHERE EXISTS(SELECT 1 FROM XBItem it, XBNodeTree nt WHERE it.id = XBXStri.item_id AND nt.node_id = it.owner_id AND " + nodeTreeCond + ")").executeUpdate();

        em.createQuery("DELETE * FROM XBXPluginUi WHERE EXISTS(SELECT 1 FROM XBPlugin pg, XBXFile fl, XBNodeTree nt WHERE pg.id = XBXPluginUi.plugin_id AND fl.id = pg.pluginfile_id AND nt.node_id = fl.node_id AND " + nodeTreeCond + ")").executeUpdate();
        em.createQuery("DELETE * FROM XBXPluginUi WHERE EXISTS(SELECT 1 FROM XBPlugin pg, XBNodeTree nt WHERE pg.id = XBXPluginUi.plugin_id AND nt.node_id = pg.owner_id AND " + nodeTreeCond + ")").executeUpdate();
        em.createQuery("DELETE * FROM XBXPlugin WHERE EXISTS(SELECT 1 FROM XBXFile fl, XBNodeTree nt WHERE fl.id = XBXPlugin.pluginfile_id AND nt.node_id = fl.node_id AND " + nodeTreeCond + ")").executeUpdate();
        em.createQuery("DELETE * FROM XBXPlugin WHERE EXISTS(SELECT 1 FROM XBNodeTree nt WHERE nt.node_id = XBXPlugin.owner_id AND " + nodeTreeCond + ")").executeUpdate();

        em.createQuery("DELETE * FROM XBXFile WHERE EXISTS(SELECT 1 FROM XBItem it, XBNodeTree nt WHERE it.id = XBXFile.item_id AND nt.node_id = it.owner_id AND " + nodeTreeCond + ")").executeUpdate();

        em.createQuery("DELETE * FROM XBConsDef WHERE EXISTS(SELECT 1 FROM XBItem it, XBItem sp, XBNodeTree nt WHERE it.id = XBConsDef.id AND sp.id = it.owner_id AND nt.node_id = sp.owner_id AND " + nodeTreeCond + ")").executeUpdate();
        em.createQuery("DELETE * FROM XBJoinDef WHERE EXISTS(SELECT 1 FROM XBItem it, XBItem sp, XBNodeTree nt WHERE it.id = XBJoinDef.id AND sp.id = it.owner_id AND nt.node_id = sp.owner_id AND " + nodeTreeCond + ")").executeUpdate();
        em.createQuery("DELETE * FROM XBSpecDef WHERE EXISTS(SELECT 1 FROM XBItem it, XBItem sp, XBNodeTree nt WHERE it.id = XBSpecDef.id AND sp.id = it.owner_id AND nt.node_id = sp.owner_id AND " + nodeTreeCond + ")").executeUpdate();
        em.createQuery("DELETE * FROM XBItem WHERE dtype IN ('XBBlockSpec', 'XBGroupSpec', 'XBFormatSpec') AND EXISTS(SELECT 1 FROM XBItem it, XBItem sp, XBNodeTree nt WHERE it.id = XBItem.id AND sp.id = it.owner_id AND nt.node_id = sp.owner_id AND " + nodeTreeCond + ")").executeUpdate();

        em.createQuery("DELETE * FROM XBRev WHERE EXISTS(SELECT 1 FROM XBItem it, XBItem rv, XBItem sp, XBNodeTree nt WHERE XBRev.id = rv.id AND rv.owner_id = sp.id AND nt.node_id = sp.owner_id AND " + nodeTreeCond + ")").executeUpdate();
        em.createQuery("DELETE * FROM XBItem WHERE dtype IN ('XBRev') AND EXISTS(SELECT 1 FROM XBItem it, XBItem sp, XBNodeTree nt WHERE it.id = XBItem.id AND sp.id = it.owner_id AND nt.node_id = sp.owner_id AND " + nodeTreeCond + ")").executeUpdate();
        em.createQuery("DELETE * FROM XBSpec WHERE EXISTS(SELECT 1 FROM XBItem it, XBNodeTree nt WHERE XBSpec.id = it.owner_id AND nt.node_id = sp.owner_id AND " + nodeTreeCond + ")").executeUpdate();

        em.createQuery("DELETE * FROM XBItem WHERE EXISTS(SELECT 1 FROM XBNodeTree nt WHERE (NOT XBXItem.dType = 'XBNode') AND nt.node_id = XBItem.id AND " + nodeTreeCond + ")").executeUpdate();
        em.createQuery("DELETE * FROM XBNode WHERE EXISTS(SELECT 1 FROM XBNodeTree nt WHERE nt.node_id = XBNode.id AND " + nodeTreeCond + ")").executeUpdate();
        em.createQuery("DELETE * FROM XBItem WHERE EXISTS(SELECT 1 FROM XBNodeTree nt WHERE nt.node_id = XBItem.id AND " + nodeTreeCond + ")").executeUpdate();

        throw new UnsupportedOperationException("Not supported yet.");

        // removeItem(node);
    }
}
