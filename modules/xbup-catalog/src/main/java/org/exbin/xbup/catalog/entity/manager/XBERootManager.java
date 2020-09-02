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
import java.util.Date;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.persistence.NoResultException;
import org.exbin.xbup.catalog.XBECatalog;
import org.exbin.xbup.catalog.entity.XBENode;
import org.exbin.xbup.catalog.entity.XBERoot;
import org.exbin.xbup.core.catalog.base.XBCRoot;
import org.exbin.xbup.core.catalog.base.manager.XBCRootManager;
import org.springframework.stereotype.Repository;

/**
 * XBUP catalog root manager.
 *
 * @version 0.2.1 2020/08/26
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
@Repository
public class XBERootManager extends XBEDefaultCatalogManager<XBCRoot> implements XBCRootManager, Serializable {

    public XBERootManager() {
        super();
    }

    public XBERootManager(XBECatalog catalog) {
        super(catalog);
    }

    @Nonnull
    @Override
    public Class getEntityClass() {
        return XBERoot.class;
    }

    @Nonnull
    @Override
    public XBERoot getMainRoot() {
        try {
            return (XBERoot) em.createQuery("SELECT object(o) FROM XBRoot AS o WHERE o.url IS NULL").getSingleResult();
        } catch (NoResultException e) {
            throw new IllegalStateException("Missing main root", e);
        } catch (Exception ex) {
            throw new IllegalStateException("Missing main root", ex);
        }
    }

    @Nonnull
    @Override
    public Optional<Date> getMainLastUpdate() {
        try {
            return Optional.ofNullable((Date) em.createQuery("SELECT o.lastUpdate FROM XBRoot AS o WHERE o.url IS NULL").getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        } catch (Exception ex) {
            Logger.getLogger(XBERootManager.class.getName()).log(Level.SEVERE, null, ex);
            return Optional.empty();
        }
    }

    /**
     * Set last update time mark to current database time.
     *
     * UPDATE XBROOT SET LASTUPDATE = CURRENT_TIMESTAMP() WHERE url IS NULL
     */
    @Override
    public void setMainLastUpdateToNow() {
        try {
            em.createQuery("UPDATE XBRoot AS o SET o.lastUpdate = CURRENT_TIMESTAMP WHERE o.url IS NULL").executeUpdate();
        } catch (Exception ex) {
            Logger.getLogger(XBERootManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public boolean isMainPresent() {
        try {
            em.createQuery("SELECT 1 FROM XBRoot AS o WHERE o.url IS NULL").getSingleResult();
            return true;
        } catch (NoResultException e) {
            return false;
        } catch (Exception ex) {
            throw new IllegalStateException("Missing main root", ex);
        }
    }

    @Override
    public boolean initCatalog() {
        XBENode node = new XBENode();
        node.setXBIndex(Long.valueOf(0));
        node.setParent(null);
        em.persist(node);

        XBERoot root = new XBERoot();
        root.setNode(node);
        em.persist(root);

        return true;
    }
}
