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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.NoResultException;
import javax.swing.ImageIcon;
import org.springframework.stereotype.Repository;
import org.xbup.lib.core.catalog.base.XBCBlockSpec;
import org.xbup.lib.core.catalog.base.XBCItem;
import org.xbup.lib.core.catalog.base.XBCXFile;
import org.xbup.lib.core.catalog.base.XBCXIcon;
import org.xbup.lib.core.catalog.base.manager.XBCXFileManager;
import org.xbup.lib.core.catalog.base.manager.XBCXIconManager;
import org.xbup.lib.catalog.XBECatalog;
import org.xbup.lib.catalog.entity.XBEXIcon;
import org.xbup.lib.catalog.entity.XBEXIconMode;

/**
 * XBUP catalog icon manager.
 *
 * @version 0.1.21 2012/01/01
 * @author XBUP Project (http://xbup.org)
 */
@Repository
public class XBEXIconManager extends XBEDefaultManager<XBEXIcon> implements XBCXIconManager<XBEXIcon>, Serializable {

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
            Logger.getLogger(XBEXIconManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (Exception ex) {
            Logger.getLogger(XBEXIconManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public XBEXIcon findById(Long id) {
        try {
            return (XBEXIcon) catalog.getEntityManager().createQuery("SELECT object(o) FROM XBXIcon as o WHERE o.id = " + id).getSingleResult();
        } catch (NoResultException ex) {
//            Logger.getLogger(XBEXIconManager.class.getExtensionName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (Exception ex) {
            Logger.getLogger(XBEXIconManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public void initializeExtension() {
//        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public XBEXIcon getDefaultIcon(XBCItem item) {
        try {
            return (XBEXIcon) catalog.getEntityManager().createQuery("SELECT object(o) FROM XBXIcon as o WHERE o.parent.id = " + item.getId()
                    + " AND NOT EXISTS(SELECT b.id FROM XBXIcon as b WHERE b.parent.id = " + item.getId() + " AND (b.mode.id < o.mode.id OR (b.mode.id = o.mode.id AND b.id < o.id)))").getSingleResult();
        } catch (NoResultException ex) {
            return null;
        } catch (Exception ex) {
            Logger.getLogger(XBEXIconManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public List<XBCXIcon> getBlockSpecIcons(XBCBlockSpec icon) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getExtensionName() {
        return "Icon Extension";
    }

    @Override
    public XBEXIconMode getIconMode(Long type) {
        try {
            return (XBEXIconMode) catalog.getEntityManager().createQuery("SELECT object(o) FROM XBXIconMode as o WHERE o.id = " + type).getSingleResult();
        } catch (NoResultException ex) {
            return null;
        } catch (Exception ex) {
            Logger.getLogger(XBEXIconManager.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public ImageIcon getDefaultImageIcon(XBCItem item) {
        XBEXFileManager fileManager = (XBEXFileManager) catalog.getCatalogManager(XBCXFileManager.class);
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
}