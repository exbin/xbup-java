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
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nullable;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import org.exbin.xbup.catalog.XBECatalog;
import org.exbin.xbup.catalog.entity.XBEBlockCons;
import org.exbin.xbup.catalog.entity.XBEBlockJoin;
import org.exbin.xbup.catalog.entity.XBEBlockListCons;
import org.exbin.xbup.catalog.entity.XBEBlockListJoin;
import org.exbin.xbup.catalog.entity.XBEBlockSpec;
import org.exbin.xbup.catalog.entity.XBEFormatCons;
import org.exbin.xbup.catalog.entity.XBEFormatJoin;
import org.exbin.xbup.catalog.entity.XBEFormatSpec;
import org.exbin.xbup.catalog.entity.XBEGroupCons;
import org.exbin.xbup.catalog.entity.XBEGroupJoin;
import org.exbin.xbup.catalog.entity.XBEGroupSpec;
import org.exbin.xbup.catalog.entity.XBENode;
import org.exbin.xbup.catalog.entity.XBESpec;
import org.exbin.xbup.catalog.entity.XBESpecDef;
import org.exbin.xbup.core.block.definition.XBParamType;
import org.exbin.xbup.core.catalog.base.XBCBlockSpec;
import org.exbin.xbup.core.catalog.base.XBCFormatSpec;
import org.exbin.xbup.core.catalog.base.XBCGroupSpec;
import org.exbin.xbup.core.catalog.base.XBCNode;
import org.exbin.xbup.core.catalog.base.XBCSpec;
import org.exbin.xbup.core.catalog.base.XBCSpecDef;
import org.exbin.xbup.core.catalog.base.manager.XBCSpecManager;
import org.springframework.stereotype.Repository;

/**
 * XBUP catalog specification manager.
 *
 * @version 0.1.23 2014/05/19
 * @author ExBin Project (http://exbin.org)
 */
@Repository
public class XBESpecManager extends XBEDefaultCatalogManager<XBESpec> implements XBCSpecManager<XBESpec>, Serializable {

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
            return 0l;
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
            return 0l;
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
            return 0l;
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
            return 0l;
        } catch (Exception ex) {
            Logger.getLogger(XBESpecManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public XBESpec findById(long id) {
        try {
            return (XBESpec) em.createQuery("SELECT object(o) FROM XBSpec as o WHERE o.id = " + id).getSingleResult();
        } catch (NoResultException ex) {
            return null;
        } catch (Exception ex) {
            Logger.getLogger(XBESpecManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public Long[] getSpecXBPath(XBCSpec node) {
        ArrayList<Long> list = new ArrayList<>();
        Optional<XBCNode> optionalParent = node.getParent();
        while (optionalParent.isPresent()) {
            XBCNode parent = optionalParent.get();
            if (parent.getParent().isPresent()) {
                list.add(0, parent.getXBIndex());
            }
            optionalParent = parent.getParent();
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
            return 0l;
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
            return 0l;
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
            return 0l;
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
            return 0l;
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
        if (spec instanceof XBCBlockSpec) {
            switch (type) {
                case CONSIST:
                    return newInstance(XBEBlockCons.class);
                case JOIN:
                    return newInstance(XBEBlockJoin.class);
                case LIST_CONSIST:
                    return newInstance(XBEBlockListCons.class);
                case LIST_JOIN:
                    return newInstance(XBEBlockListJoin.class);
                default:
                    return null; // TODO: Or throw?
                }
        } else if (spec instanceof XBCGroupSpec) {
            switch (type) {
                case CONSIST:
                    return newInstance(XBEGroupCons.class);
                case JOIN:
                    return newInstance(XBEGroupJoin.class);
                default:
                    return null; // TODO: Or throw?
                }
        } else if (spec instanceof XBCFormatSpec) {
            switch (type) {
                case CONSIST:
                    return newInstance(XBEFormatCons.class);
                case JOIN:
                    return newInstance(XBEFormatJoin.class);
                default:
                    return null; // TODO: Or throw?
                }
        }

        return null;
    }

    @Override
    public XBEBlockSpec createBlockSpec() {
        return newInstance(XBEBlockSpec.class);
    }

    @Override
    public XBCGroupSpec createGroupSpec() {
        return newInstance(XBEGroupSpec.class);
    }

    @Override
    public XBCFormatSpec createFormatSpec() {
        return newInstance(XBEFormatSpec.class);
    }

    @Nullable
    private static <T> T newInstance(Class<T> tClass) {
        try {
            return tClass.getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(XBESpecManager.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }
}
