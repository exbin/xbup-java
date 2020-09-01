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
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.persistence.NoResultException;
import javax.swing.ImageIcon;
import org.exbin.xbup.catalog.XBECatalog;
import org.exbin.xbup.catalog.entity.XBEXIcon;
import org.exbin.xbup.catalog.entity.XBEXIconMode;
import org.exbin.xbup.core.catalog.base.XBCItem;
import org.exbin.xbup.core.catalog.base.XBCXFile;
import org.exbin.xbup.core.catalog.base.XBCXIcon;
import org.exbin.xbup.core.catalog.base.manager.XBCXFileManager;
import org.exbin.xbup.core.catalog.base.manager.XBCXIconManager;
import org.springframework.stereotype.Repository;

/**
 * XBUP catalog icon manager.
 *
 * @version 0.2.1 2020/08/26
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
@Repository
public class XBEXIconManager extends XBEDefaultCatalogManager<XBEXIcon> implements XBCXIconManager<XBEXIcon>, Serializable {

    public XBEXIconManager() {
        super();
    }

    public XBEXIconManager(XBECatalog catalog) {
        super(catalog);
    }

    public Long getAllIconsCount() {
        try {
            return (Long) catalog.getEntityManager().createQuery("SELECT count(o) FROM XBXIcon as o").getSingleResult();
        } catch (NoResultException ex) {
            return 0l;
        } catch (Exception ex) {
            Logger.getLogger(XBEXIconManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public XBEXIcon findById(Long id) {
        try {
            return (XBEXIcon) catalog.getEntityManager().createQuery("SELECT object(o) FROM XBXIcon as o WHERE o.id = " + id).getSingleResult();
        } catch (NoResultException ex) {
            return null;
        } catch (Exception ex) {
            Logger.getLogger(XBEXIconManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public void initializeExtension() {
    }

    @Override
    public XBEXIcon getDefaultIcon(XBCItem item) {
        try {
            return (XBEXIcon) catalog.getEntityManager().createQuery("SELECT object(o) FROM XBXIcon as o WHERE o.parent.id = " + item.getId()
                    + " AND NOT EXISTS(SELECT b.id FROM XBXIcon as b WHERE b.parent.id = " + item.getId() + " AND (b.mode.id > o.mode.id OR (b.mode.id = o.mode.id AND b.id > o.id)))").getSingleResult();
        } catch (NoResultException ex) {
            return null;
        } catch (Exception ex) {
            Logger.getLogger(XBEXIconManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Nonnull
    @Override
    public List<XBCXIcon> getItemIcons(XBCItem item) {
        try {
            return catalog.getEntityManager().createQuery("SELECT object(o) FROM XBXIcon as o WHERE o.parent.id = " + item.getId()).getResultList();
        } catch (NoResultException ex) {
            return List.of();
        } catch (Exception ex) {
            Logger.getLogger(XBEXIconManager.class.getName()).log(Level.SEVERE, null, ex);
            return List.of();
        }
    }

    @Override
    public String getExtensionName() {
        return "Icon Extension";
    }

    @Override
    public XBEXIconMode getIconMode(Long modeId) {
        try {
            return (XBEXIconMode) catalog.getEntityManager().createQuery("SELECT object(o) FROM XBXIconMode as o WHERE o.id = " + modeId).getSingleResult();
        } catch (NoResultException ex) {
            return null;
        } catch (Exception ex) {
            Logger.getLogger(XBEXIconManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public ImageIcon getDefaultImageIcon(XBCItem item) {
        XBCXFileManager fileManager = catalog.getCatalogManager(XBCXFileManager.class);
        XBCXIcon icon = getDefaultIcon(item);
        if (icon == null) {
            return null;
        }
        XBCXFile file = icon.getIconFile();
        if (file == null) {
            return null;
        }
        return fileManager.getFileAsImageIcon(file);
    }

    @Override
    public XBCXIcon getDefaultBigIcon(XBCItem item) {
        XBEXIcon icon;
        try {
            icon = (XBEXIcon) catalog.getEntityManager().createQuery("SELECT object(o) FROM XBXIcon as o, XBXIconMode as m WHERE o.parent.id = " + item.getId() + " AND o.mode = m AND m.caption = 'PNG 32x32'").getSingleResult();
        } catch (NoResultException ex) {
            return null;
        } catch (Exception ex) {
            Logger.getLogger(XBEXIconManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

        return icon;
    }

    @Override
    public XBCXIcon getDefaultSmallIcon(XBCItem item) {
        XBEXIcon icon;
        try {
            icon = (XBEXIcon) catalog.getEntityManager().createQuery("SELECT object(o) FROM XBXIcon as o, XBXIconMode as m WHERE o.parent.id = " + item.getId() + " AND o.mode = m AND m.caption = 'PNG 16x16'").getSingleResult();
        } catch (NoResultException ex) {
            return null;
        } catch (Exception ex) {
            Logger.getLogger(XBEXIconManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

        return icon;
    }

    @Override
    public byte[] getDefaultBigIconData(XBCItem item) {
        XBEXIcon icon = (XBEXIcon) getDefaultBigIcon(item);
        return icon == null || icon.getIconFile() == null ? null : icon.getIconFile().getContent();
    }

    @Override
    public byte[] getDefaultSmallIconData(XBCItem item) {
        XBEXIcon icon = (XBEXIcon) getDefaultSmallIcon(item);
        return icon == null || icon.getIconFile() == null ? null : icon.getIconFile().getContent();
    }
}
