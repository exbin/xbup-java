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
package org.exbin.xbup.catalog.entity.manager;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.NoResultException;
import javax.swing.ImageIcon;
import org.exbin.xbup.catalog.XBECatalog;
import org.exbin.xbup.catalog.entity.XBEXIcon;
import org.exbin.xbup.catalog.entity.XBEXIconMode;
import org.exbin.xbup.core.catalog.base.XBCBlockSpec;
import org.exbin.xbup.core.catalog.base.XBCItem;
import org.exbin.xbup.core.catalog.base.XBCXFile;
import org.exbin.xbup.core.catalog.base.XBCXIcon;
import org.exbin.xbup.core.catalog.base.manager.XBCXFileManager;
import org.exbin.xbup.core.catalog.base.manager.XBCXIconManager;
import org.springframework.stereotype.Repository;

/**
 * XBUP catalog icon manager.
 *
 * @version 0.1.25 2015/09/06
 * @author ExBin Project (http://exbin.org)
 */
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
//        throw new UnsupportedOperationException("Not supported yet.");
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

    @Override
    public List<XBCXIcon> getBlockSpecIcons(XBCBlockSpec icon) {
        throw new UnsupportedOperationException("Not supported yet.");
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
