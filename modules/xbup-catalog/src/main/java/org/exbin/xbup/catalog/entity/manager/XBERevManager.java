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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import org.exbin.xbup.catalog.XBECatalog;
import org.exbin.xbup.catalog.entity.XBERev;
import org.exbin.xbup.catalog.entity.XBESpec;
import org.exbin.xbup.core.catalog.base.XBCRev;
import org.exbin.xbup.core.catalog.base.XBCSpec;
import org.exbin.xbup.core.catalog.base.manager.XBCRevManager;
import org.springframework.stereotype.Repository;

/**
 * XBUP catalog specification revision manager.
 *
 * @version 0.2.1 2020/08/26
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
@Repository
public class XBERevManager extends XBEDefaultCatalogManager<XBCRev> implements XBCRevManager, Serializable {

    public XBERevManager() {
        super();
    }

    public XBERevManager(XBECatalog catalog) {
        super(catalog);
    }

    @Override
    public XBERev findRevByXB(XBCSpec spec, long xbIndex) {
        if (spec == null) {
            return null;
        }
        try {
            return (XBERev) em.createQuery("SELECT object(o) FROM XBRev as o WHERE o.parent.id = " + ((XBESpec) spec).getId() + " AND o.xbIndex = " + xbIndex).getSingleResult();
        } catch (NoResultException ex) {
            return null;
        } catch (Exception ex) {
            Logger.getLogger(XBERevManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public XBERev getRev(XBCSpec spec, long index) {
        try {
            Query query = em.createQuery("SELECT object(o) FROM XBRev as o WHERE o.parent.id = " + ((XBESpec) spec).getId());
            query.setFirstResult((int) index);
            query.setMaxResults(1);
            return (XBERev) query.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        } catch (Exception ex) {
            Logger.getLogger(XBERevManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public Long findMaxRevXB(XBCSpec spec) {
        try {
            return (Long) em.createQuery("SELECT MAX(o.xbIndex) FROM XBRev as o WHERE o.parent.id = " + ((XBESpec) spec).getId()).getSingleResult();
        } catch (NoResultException ex) {
            return null;
        } catch (Exception ex) {
            Logger.getLogger(XBERevManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public long getRevsCount(XBCSpec spec) {
        try {
            return (Long) em.createQuery("SELECT count(o) FROM XBRev as o WHERE o.parent.id = " + ((XBESpec) spec).getId()).getSingleResult();
        } catch (NoResultException ex) {
            return 0;
        } catch (Exception ex) {
            Logger.getLogger(XBERevManager.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }

    @Override
    public long getRevsLimitSum(XBCSpec spec, long revision) {
        try {
            return (Long) em.createQuery("SELECT sum(o.xbLimit) FROM XBRev as o WHERE o.parent.id = " + ((XBESpec) spec).getId() + " AND o.xbIndex <= " + revision).getSingleResult();
        } catch (NoResultException ex) {
            return 0;
        } catch (Exception ex) {
            Logger.getLogger(XBERevManager.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<XBCRev> getRevs(XBCSpec spec) {
        try {
            return (List<XBCRev>) em.createQuery("SELECT object(o) FROM XBRev as o WHERE o.parent.id = " + ((XBESpec) spec).getId()).getResultList();
        } catch (NoResultException ex) {
            return null;
        } catch (Exception ex) {
            Logger.getLogger(XBERevManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
