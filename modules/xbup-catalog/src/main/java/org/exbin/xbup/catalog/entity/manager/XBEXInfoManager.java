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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.persistence.NoResultException;
import org.exbin.xbup.catalog.XBECatalog;
import org.exbin.xbup.catalog.entity.XBEXItemInfo;
import org.exbin.xbup.core.catalog.base.XBCNode;
import org.exbin.xbup.core.catalog.base.XBCXItemInfo;
import org.exbin.xbup.core.catalog.base.manager.XBCXInfoManager;
import org.springframework.stereotype.Repository;

/**
 * XBUP catalog infomation manager.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
@Repository
public class XBEXInfoManager extends XBEDefaultCatalogManager<XBCXItemInfo> implements XBCXInfoManager, Serializable {

    public XBEXInfoManager() {
        super();
    }

    public XBEXInfoManager(XBECatalog catalog) {
        super(catalog);
    }

    @Nonnull
    @Override
    public Class getEntityClass() {
        return XBEXItemInfo.class;
    }

    @Override
    public XBEXItemInfo getNodeInfo(XBCNode dir) {
        if (dir == null) {
            return null;
        }
        try {
            return (XBEXItemInfo) em.createQuery("SELECT object(o) FROM XBItemInfo as o WHERE o.parent.id = " + dir.getId()).getSingleResult();
        } catch (NoResultException ex) {
            return null;
        } catch (Exception ex) {
            Logger.getLogger(XBEXInfoManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public XBEXItemInfo findById(long id) {
        try {
            return (XBEXItemInfo) em.createQuery("SELECT object(o) FROM XBItemInfo as o WHERE o.id = " + id).getSingleResult();
        } catch (NoResultException ex) {
            return null;
        } catch (Exception ex) {
            Logger.getLogger(XBEXInfoManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
