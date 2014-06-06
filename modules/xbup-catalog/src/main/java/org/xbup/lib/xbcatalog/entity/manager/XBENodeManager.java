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
package org.xbup.lib.xbcatalog.entity.manager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import org.springframework.stereotype.Repository;
import org.xbup.lib.xb.catalog.base.XBCNode;
import org.xbup.lib.xb.catalog.base.manager.XBCNodeManager;
import org.xbup.lib.xbcatalog.XBECatalog;
import org.xbup.lib.xbcatalog.entity.XBENode;
import org.xbup.lib.xbcatalog.entity.XBERoot;

/**
 * XBUP catalog node manager.
 *
 * @version 0.1 wr22.0 2013/08/17
 * @author XBUP Project (http://xbup.org)
 */
@Repository
public class XBENodeManager extends XBEDefaultManager<XBENode> implements XBCNodeManager<XBENode>, Serializable {

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
            Logger.getLogger(XBENodeManager.class.getName()).log(Level.SEVERE, null, ex);
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
    public XBENode findNodeByXBPath(Long[] xbCatalogPath) {
        XBENode node = getRootNode();
        for (int i = 0; i < xbCatalogPath.length; i++) {
            node = (XBENode) getSubNode(node, xbCatalogPath[i]);
            if (node == null) {
                break;
            }
        }
        return node;
    }

    @Override
    public XBENode findParentByXBPath(Long[] xbCatalogPath) {
        if (xbCatalogPath.length == 0) {
            return null;
        }
        XBENode node = getRootNode();
        for (int i = 0; i < xbCatalogPath.length - 1; i++) {
            node = (XBENode) getSubNode(node, xbCatalogPath[i]);
            if (node == null) {
                break;
            }
        }
        return node;
    }

    @Override
    public Long[] getNodeXBPath(XBCNode node) {
        ArrayList<Long> list = new ArrayList<Long>();
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
    public XBENode findOwnerByXBPath(Long[] xbCatalogPath) {
        XBENode node = getRootNode();
        if (xbCatalogPath.length == 0) {
            return null;
        }
        for (int i = 0; i < xbCatalogPath.length - 1; i++) {
            node = (XBENode) getSubNode(node, xbCatalogPath[i]);
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
            Logger.getLogger(XBENodeManager.class.getName()).log(Level.SEVERE, null, ex);
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
}
