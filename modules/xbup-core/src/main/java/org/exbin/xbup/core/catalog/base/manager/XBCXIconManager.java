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
package org.exbin.xbup.core.catalog.base.manager;

import java.util.List;
import javax.swing.ImageIcon;
import org.exbin.xbup.core.catalog.base.XBCBlockSpec;
import org.exbin.xbup.core.catalog.base.XBCExtension;
import org.exbin.xbup.core.catalog.base.XBCItem;
import org.exbin.xbup.core.catalog.base.XBCXIcon;
import org.exbin.xbup.core.catalog.base.XBCXIconMode;

/**
 * Interface for XBCXIcon catalog manager.
 *
 * @version 0.1.24 2014/11/26
 * @author ExBin Project (http://exbin.org)
 * @param <T> icon entity
 */
public interface XBCXIconManager<T extends XBCXIcon> extends XBCManager<T>, XBCExtension {

    /**
     * Gets icon mode by unique index.
     *
     * @param iconModeId icon mode id
     * @return icon mode
     */
    XBCXIconMode getIconMode(Long iconModeId);

    /**
     * Gets list of icons for block specification.
     *
     * @param blockSpec block specification
     * @return list of icons
     */
    List<XBCXIcon> getBlockSpecIcons(XBCBlockSpec blockSpec);

    /**
     * Gets default icon for item.
     *
     * @param item item
     * @return icon
     */
    XBCXIcon getDefaultIcon(XBCItem item);

    /**
     * Gets default icon for given item.
     *
     * @param item item we want to get icon of
     * @return icon instance or null
     */
    ImageIcon getDefaultImageIcon(XBCItem item);

    /**
     * Gets default big icon.
     *
     * @param item catalog item
     * @return icon data or null
     */
    XBCXIcon getDefaultBigIcon(XBCItem item);

    /**
     * Gets default small icon.
     *
     * @param item catalog item
     * @return icon data or null
     */
    XBCXIcon getDefaultSmallIcon(XBCItem item);

    /**
     * Gets default big icon.
     *
     * @param item catalog item
     * @return icon data or null
     */
    byte[] getDefaultBigIconData(XBCItem item);

    /**
     * Gets default small icon.
     *
     * @param item catalog item
     * @return icon data or null
     */
    byte[] getDefaultSmallIconData(XBCItem item);
}
