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
package org.xbup.lib.catalog.entity.manager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import org.springframework.stereotype.Repository;
import org.xbup.lib.core.catalog.base.XBCBlockSpec;
import org.xbup.lib.core.catalog.base.XBCFormatSpec;
import org.xbup.lib.core.catalog.base.XBCGroupSpec;
import org.xbup.lib.core.catalog.base.XBCNode;
import org.xbup.lib.core.catalog.base.XBCSpec;
import org.xbup.lib.core.catalog.base.XBCSpecDef;
import org.xbup.lib.core.block.definition.XBParamType;
import org.xbup.lib.core.catalog.base.manager.XBCSpecManager;
import org.xbup.lib.catalog.XBECatalog;
import org.xbup.lib.catalog.entity.XBEBlockCons;
import org.xbup.lib.catalog.entity.XBEBlockJoin;
import org.xbup.lib.catalog.entity.XBEBlockListCons;
import org.xbup.lib.catalog.entity.XBEBlockListJoin;
import org.xbup.lib.catalog.entity.XBEBlockSpec;
import org.xbup.lib.catalog.entity.XBEFormatCons;
import org.xbup.lib.catalog.entity.XBEFormatJoin;
import org.xbup.lib.catalog.entity.XBEFormatSpec;
import org.xbup.lib.catalog.entity.XBEGroupCons;
import org.xbup.lib.catalog.entity.XBEGroupJoin;
import org.xbup.lib.catalog.entity.XBEGroupSpec;
import org.xbup.lib.catalog.entity.XBENode;
import org.xbup.lib.catalog.entity.XBESpec;
import org.xbup.lib.catalog.entity.XBESpecDef;

/**
 * XBUP catalog specification manager.
 *
 * @version 0.1.23 2014/05/19
 * @author XBUP Project (http://xbup.org)
 */
@Repository
public class XBESpecManager extends XBEDefaultManager<XBESpec> implements XBCSpecManager<XBESpec>, Serializable {

    public XBESpecManager() {
        super();
    }
    
    public XBESpecManager(XBECatalog catalog) {
        super(catalog);
    }

    @Override
    public Long getAllSpecsCount() {
        try {
            return (Long) em.createQuery("SELECT count(o) FROM XBSpec as o").getSingleResult();
        } catch (NoResultException ex) {
            Logger.getLogger(XBESpecManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (Exception ex) {
            Logger.getLogger(XBESpecManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public Long getAllFormatSpecsCount() {
        try {
            return (Long) em.createQuery("SELECT count(o) FROM XBFormatSpec as o").getSingleResult();
        } catch (NoResultException ex) {
            Logger.getLogger(XBESpecManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (Exception ex) {
            Logger.getLogger(XBESpecManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public Long getAllGroupSpecsCount() {
        try {
            return (Long) em.createQuery("SELECT count(o) FROM XBGroupSpec as o").getSingleResult();
        } catch (NoResultException ex) {
            Logger.getLogger(XBESpecManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (Exception ex) {
            Logger.getLogger(XBESpecManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public Long getAllBlockSpecsCount() {
        try {
            return (Long) em.createQuery("SELECT count(o) FROM XBBlockSpec as o").getSingleResult();
        } catch (NoResultException ex) {
            Logger.getLogger(XBESpecManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (Exception ex) {
            Logger.getLogger(XBESpecManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public XBESpec findById(long id) {
        try {
            return (XBESpec) em.createQuery("SELECT object(o) FROM XBSpec as o WHERE o.id = " + id).getSingleResult();
        } catch (NoResultException ex) {
//            Logger.getLogger(XBESpecManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (Exception ex) {
            Logger.getLogger(XBESpecManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public Long[] getSpecXBPath(XBCSpec node) {
        ArrayList<Long> list = new ArrayList<>();
        XBCNode parent = node.getParent();
        while (parent != null) {
            if (parent.getParent() != null) {
                if (parent.getXBIndex() == null) {
                    return null;
                }
                list.add(0, parent.getXBIndex());
            }
            parent = (XBCNode) parent.getParent();
        }
        list.add(node.getXBIndex());
        return (Long[]) list.toArray(new Long[list.size()]);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<XBCSpec> getSpecs(XBCNode node) {
        try {
            return (List<XBCSpec>) em.createQuery("SELECT object(o) FROM XBSpec as o WHERE o.parent.id = " + ((XBENode) node).getId()).getResultList();
        } catch (NoResultException e) {
            return null;
        } catch (Exception ex) {
            Logger.getLogger(XBESpecManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public XBESpec getSpec(XBCNode node, long index) {
        try {
            Query query = em.createQuery("SELECT object(o) FROM XBSpec AS o WHERE o.parent.id = " + ((XBENode) node).getId());
            query.setFirstResult((int) index);
            query.setMaxResults(1);
            return (XBESpec) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception ex) {
            Logger.getLogger(XBESpecManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public XBEFormatSpec getFormatSpec(XBCNode node, long index) {
        try {
            Query query = em.createQuery("SELECT object(o) FROM XBFormatSpec as o WHERE o.parent.id = " + ((XBENode) node).getId());
            query.setFirstResult((int) index);
            query.setMaxResults(1);
            return (XBEFormatSpec) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception ex) {
            Logger.getLogger(XBESpecManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<XBCFormatSpec> getFormatSpecs(XBCNode node) {
        try {
            return (List<XBCFormatSpec>) em.createQuery("SELECT object(o) FROM XBFormatSpec as o WHERE o.parent.id = " + ((XBENode) node).getId()).getResultList();
        } catch (NoResultException e) {
            return null;
        } catch (Exception ex) {
            Logger.getLogger(XBESpecManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public XBEBlockSpec getBlockSpec(XBCNode node, long index) {
        try {
            Query query = em.createQuery("SELECT object(o) FROM XBBlockSpec as o WHERE o.parent.id = " + ((XBENode) node).getId());
            query.setFirstResult((int) index);
            query.setMaxResults(1);
            return (XBEBlockSpec) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception ex) {
            Logger.getLogger(XBESpecManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<XBCBlockSpec> getBlockSpecs(XBCNode node) {
        try {
            return (List<XBCBlockSpec>) em.createQuery("SELECT object(o) FROM XBBlockSpec as o WHERE o.parent.id = " + ((XBENode) node).getId()).getResultList();
        } catch (NoResultException e) {
            return null;
        } catch (Exception ex) {
            Logger.getLogger(XBESpecManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public XBEGroupSpec getGroupSpec(XBCNode node, long index) {
        try {
            Query query = em.createQuery("SELECT object(o) FROM XBGroupSpec as o WHERE o.parent.id = " + ((XBENode) node).getId());
            query.setFirstResult((int) index);
            query.setMaxResults(1);
            return (XBEGroupSpec) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception ex) {
            Logger.getLogger(XBESpecManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<XBCGroupSpec> getGroupSpecs(XBCNode node) {
        try {
            return (List<XBCGroupSpec>) em.createQuery("SELECT object(o) FROM XBGroupSpec as o WHERE o.parent.id = " + ((XBENode) node).getId()).getResultList();
        } catch (NoResultException e) {
            return null;
        } catch (Exception ex) {
            Logger.getLogger(XBESpecManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public XBEBlockSpec findBlockSpecByXB(XBCNode node, long xbIndex) {
        if ((node == null) || (!(node instanceof XBENode))) {
            return null;
        }
        try {
            return (XBEBlockSpec) em.createQuery("SELECT object(o) FROM XBBlockSpec as o WHERE o.parent.id = " + ((XBENode) node).getId() + " AND o.xbIndex = " + xbIndex).getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception ex) {
            Logger.getLogger(XBESpecManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public XBEGroupSpec findGroupSpecByXB(XBCNode node, long xbIndex) {
        if ((node == null) || (!(node instanceof XBENode))) {
            return null;
        }
        try {
            return (XBEGroupSpec) em.createQuery("SELECT object(o) FROM XBGroupSpec as o WHERE o.parent.id = " + ((XBENode) node).getId() + " AND o.xbIndex = " + xbIndex).getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception ex) {
            Logger.getLogger(XBESpecManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public XBEFormatSpec findFormatSpecByXB(XBCNode node, long xbIndex) {
        if ((node == null) || (!(node instanceof XBENode))) {
            return null;
        }
        try {
            return (XBEFormatSpec) em.createQuery("SELECT object(o) FROM XBFormatSpec as o WHERE o.parent.id = " + ((XBENode) node).getId() + " AND o.xbIndex = " + xbIndex).getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception ex) {
            Logger.getLogger(XBESpecManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public Long findMaxBlockSpecXB(XBCNode node) {
        if ((node == null) || (!(node instanceof XBENode))) {
            return null;
        }
        try {
            return (Long) em.createQuery("SELECT max(o.xbIndex) FROM XBBlockSpec as o WHERE o.parent.id = " + ((XBENode) node).getId()).getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception ex) {
            Logger.getLogger(XBESpecManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public Long findMaxGroupSpecXB(XBCNode node) {
        if ((node == null) || (!(node instanceof XBENode))) {
            return null;
        }
        try {
            return (Long) em.createQuery("SELECT max(o.xbIndex) FROM XBGroupSpec as o WHERE o.parent.id = " + ((XBENode) node).getId()).getSingleResult();
        } catch (NoResultException e) {
            return null;
        } catch (Exception ex) {
            Logger.getLogger(XBESpecManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public Long findMaxFormatSpecXB(XBCNode node) {
        if ((node == null) || (!(node instanceof XBENode))) {
            return null;
        }
        try {
            return (Long) em.createQuery("SELECT max(o.xbIndex) FROM XBFormatSpec as o WHERE o.parent.id = " + ((XBENode) node).getId()).getSingleResult();
        } catch (NoResultException ex) {
            return null;
        } catch (Exception ex) {
            Logger.getLogger(XBESpecManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public long getFormatSpecsCount(XBCNode node) {
        if (node == null) {
            return 0;
        }
        try {
            return (Long) em.createQuery("SELECT count(o) FROM XBFormatSpec as o WHERE o.parent.id = " + ((XBENode) node).getId()).getSingleResult();
        } catch (NoResultException ex) {
            Logger.getLogger(XBESpecManager.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        } catch (Exception ex) {
            Logger.getLogger(XBESpecManager.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }

    @Override
    public long getGroupSpecsCount(XBCNode node) {
        if (node == null) {
            return 0;
        }
        try {
            return (Long) em.createQuery("SELECT count(o) FROM XBGroupSpec as o WHERE o.parent.id = " + ((XBENode) node).getId()).getSingleResult();
        } catch (NoResultException ex) {
            Logger.getLogger(XBESpecManager.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        } catch (Exception ex) {
            Logger.getLogger(XBESpecManager.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }

    @Override
    public long getBlockSpecsCount(XBCNode node) {
        if (node == null) {
            return 0;
        }
        try {
            return (Long) em.createQuery("SELECT count(o) FROM XBBlockSpec as o WHERE o.parent.id = " + ((XBENode) node).getId()).getSingleResult();
        } catch (NoResultException ex) {
            Logger.getLogger(XBESpecManager.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        } catch (Exception ex) {
            Logger.getLogger(XBESpecManager.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }

    @Override
    public long getSpecsCount(XBCNode node) {
        return getFormatSpecsCount(node) + getGroupSpecsCount(node) + getBlockSpecsCount(node);
    }

    @Override
    public XBESpecDef getSpecDefByOrder(XBCSpec spec, long index) {
        try {
            Query query = em.createQuery("SELECT object(o) FROM XBSpecDef as o WHERE o.parent.id = " + ((XBESpec) spec).getId());
            query.setFirstResult((int) index);
            query.setMaxResults(1);
            return (XBESpecDef) query.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        } catch (Exception ex) {
            Logger.getLogger(XBESpecManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<XBCSpecDef> getSpecDefs(XBCSpec spec) {
        try {
            return (List<XBCSpecDef>) em.createQuery("SELECT object(o) FROM XBSpecDef as o WHERE o.parent.id = " + ((XBESpec) spec).getId() + " ORDER BY o.xbIndex").getResultList();
        } catch (NoResultException ex) {
            return null;
        } catch (Exception ex) {
            Logger.getLogger(XBESpecManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public long getSpecDefsCount(XBCSpec spec) {
        try {
            return (Long) em.createQuery("SELECT count(o) FROM XBSpecDef as o WHERE o.parent.id = " + ((XBESpec) spec).getId()).getSingleResult();
        } catch (NoResultException ex) {
            Logger.getLogger(XBESpecManager.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        } catch (Exception ex) {
            Logger.getLogger(XBESpecManager.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }

    @Override
    public XBESpecDef findSpecDefByXB(XBCSpec spec, long xbIndex) {
        if (spec == null) {
            return null;
        }
        try {
            return (XBESpecDef) em.createQuery("SELECT object(o) FROM XBSpecDef as o WHERE o.parent.id = " + ((XBESpec) spec).getId() + " AND o.xbIndex = " + xbIndex).getSingleResult();
        } catch (NoResultException ex) {
            return null;
        } catch (Exception ex) {
            Logger.getLogger(XBESpecManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public Long findMaxSpecDefXB(XBCSpec spec) {
        if (spec == null) {
            return null;
        }
        try {
            return (Long) em.createQuery("SELECT max(o.xbIndex) FROM XBSpecDef as o WHERE o.parent.id = " + ((XBESpec) spec).getId()).getSingleResult();
        } catch (NoResultException ex) {
            return null;
        } catch (Exception ex) {
            Logger.getLogger(XBESpecManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public long getDefsCount() {
        try {
            return (Long) em.createQuery("SELECT count(o) FROM XBSpecDef as o").getSingleResult();
        } catch (NoResultException ex) {
            Logger.getLogger(XBEItemManager.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        } catch (Exception ex) {
            Logger.getLogger(XBEItemManager.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }

    @Override
    public XBCSpecDef getSpecDef(long itemId) {
        Object result = em.find(XBESpecDef.class, itemId);
        return (XBESpecDef) result;
        /*        try {
         return (T) em.createQuery("SELECT object(o) FROM XBItem as o WHERE o.id = "+ itemId).getSingleResult();
         } catch (NoResultException ex) {
         return null;
         } catch (Exception ex) {
         Logger.getLogger(XBEItemManager.class.getName()).log(Level.SEVERE, null, ex);
         return null;
         } */
    }

    @Override
    public XBESpecDef createSpecDef(XBCSpec spec, XBParamType type) {
        try {
            if (spec instanceof XBCBlockSpec) {
                switch (type) {
                    case CONSIST:
                        return XBEBlockCons.class.newInstance();
                    case JOIN:
                        return XBEBlockJoin.class.newInstance();
                    case LIST_CONSIST:
                        return XBEBlockListCons.class.newInstance();
                    case LIST_JOIN:
                        return XBEBlockListJoin.class.newInstance();
                    default:
                        return null; // TODO: Or throw?
                }
            } else if (spec instanceof XBCGroupSpec) {
                switch (type) {
                    case CONSIST:
                        return XBEGroupCons.class.newInstance();
                    case JOIN:
                        return XBEGroupJoin.class.newInstance();
                    default:
                        return null; // TODO: Or throw?
                }
            } else if (spec instanceof XBCFormatSpec) {
                switch (type) {
                    case CONSIST:
                        return XBEFormatCons.class.newInstance();
                    case JOIN:
                        return XBEFormatJoin.class.newInstance();
                    default:
                        return null; // TODO: Or throw?
                }
            }
            return null;
        } catch (InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(XBESpecManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public XBEBlockSpec createBlockSpec() {
        try {
            return XBEBlockSpec.class.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(XBESpecManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public XBCGroupSpec createGroupSpec() {
        try {
            return XBEGroupSpec.class.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(XBESpecManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public XBCFormatSpec createFormatSpec() {
        try {
            return XBEFormatSpec.class.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(XBESpecManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
