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
package org.exbin.xbup.catalog.entity.manager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import org.exbin.xbup.catalog.XBECatalog;
import org.exbin.xbup.catalog.entity.XBENode;
import org.exbin.xbup.catalog.entity.XBERoot;
import org.exbin.xbup.core.catalog.base.XBCNode;
import org.exbin.xbup.core.catalog.base.manager.XBCNodeManager;
import org.springframework.stereotype.Repository;

/**
 * XBUP catalog node manager.
 *
 * @version 0.2.0 2015/09/21
 * @author ExBin Project (http://exbin.org)
 */
@Repository
public class XBENodeManager extends XBEDefaultCatalogManager<XBENode> implements XBCNodeManager<XBENode>, Serializable {

    public XBENodeManager() {
        super();
    }

    public XBENodeManager(XBECatalog catalog) {
        super(catalog);
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
    public XBENode getSubNode(XBCNode node, long index) {
        if (node == null) {
            return null;
        }
        try {
            return (XBENode) em.createQuery("SELECT object(o) FROM XBNode as o WHERE o.parent.id = " + ((XBENode) node).getId() + " AND o.xbIndex = " + index).getSingleResult();
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
    public XBENode getRootNode() {
        try {
            return (XBENode) em.createQuery("SELECT object(n) FROM XBRoot AS o, XBNode AS n WHERE o.node = n AND o.url IS NULL").getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception ex) {
            Logger.getLogger(XBENodeManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public XBENode findNodeByXBPath(Long[] catalogPath) {
        XBENode node = getRootNode();
        for (Long pathComponent : catalogPath) {
            node = (XBENode) getSubNode(node, pathComponent);
            if (node == null) {
                break;
            }
        }
        return node;
    }

    @Override
    public XBENode findParentByXBPath(Long[] catalogPath) {
        if (catalogPath.length == 0) {
            return null;
        }
        XBENode node = getRootNode();
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
        XBENode parent = (XBENode) node;
        while (parent != null) {
            if (parent.getParent() != null) {
                if (parent.getXBIndex() == null) {
                    return null;
                }
                list.add(0, parent.getXBIndex());
            }
            parent = (XBENode) parent.getParent();
        }
        return (Long[]) list.toArray(new Long[list.size()]);
    }

    @Override
    public XBENode findOwnerByXBPath(Long[] catalogPath) {
        if (catalogPath == null || catalogPath.length == 0) {
            return null;
        }

        XBENode node = getRootNode();
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
    public XBERoot getRoot() {
        try {
            return (XBERoot) em.createQuery("SELECT object(o) FROM XBRoot AS o WHERE o.url IS NULL").getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception ex) {
            Logger.getLogger(XBENodeManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public XBERoot getRoot(long rootId) {
        try {
            return (XBERoot) em.createQuery("SELECT object(o) FROM XBRoot AS o WHERE o.id = " + rootId).getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception ex) {
            Logger.getLogger(XBENodeManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * Set last update time mark to current database time.
     *
     * UPDATE XBROOT SET LASTUPDATE = CURRENT_TIMESTAMP() WHERE url IS NULL
     */
    public void setLastUpdateToNow() {
        try {
            em.createQuery("UPDATE XBRoot AS o SET o.lastUpdate = CURRENT_TIMESTAMP WHERE o.url IS NULL").executeUpdate();
        } catch (Exception ex) {
            Logger.getLogger(XBENodeManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
