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
package org.xbup.lib.catalog.entity.manager;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import org.springframework.stereotype.Repository;
import org.xbup.lib.core.catalog.base.XBCRev;
import org.xbup.lib.core.catalog.base.XBCSpec;
import org.xbup.lib.core.catalog.base.manager.XBCRevManager;
import org.xbup.lib.catalog.XBECatalog;
import org.xbup.lib.catalog.entity.XBERev;
import org.xbup.lib.catalog.entity.XBESpec;

/**
 * XBUP catalog specification revision manager.
 *
 * @version 0.1.25 2015/09/06
 * @author ExBin Project (http://exbin.org)
 */
@Repository
public class XBERevManager extends XBEDefaultCatalogManager<XBERev> implements XBCRevManager<XBERev>, Serializable {

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
            Logger.getLogger(XBESpecManager.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(XBESpecManager.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(XBESpecManager.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(XBESpecManager.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(XBESpecManager.class.getName()).log(Level.SEVERE, null, ex);
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
            Logger.getLogger(XBESpecManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
