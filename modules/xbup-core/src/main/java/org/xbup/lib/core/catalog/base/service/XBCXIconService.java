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
package org.xbup.lib.core.catalog.base.service;

import java.util.List;
import javax.swing.ImageIcon;
import org.xbup.lib.core.catalog.base.XBCBlockSpec;
import org.xbup.lib.core.catalog.base.XBCItem;
import org.xbup.lib.core.catalog.base.XBCXIcon;
import org.xbup.lib.core.catalog.base.XBCXIconMode;
import org.xbup.lib.core.catalog.base.XBCExtension;

/**
 * Interface for XBCXIcon items service.
 *
 * @version 0.1.24 2014/10/19
 * @author XBUP Project (http://xbup.org)
 * @param <T> icon entity
 */
public interface XBCXIconService<T extends XBCXIcon> extends XBCService<T>, XBCExtension {

    /**
     * Get default icon for given item.
     *
     * @param item item we want to get icon of
     * @return icon instance or null
     */
    public XBCXIcon getDefaultIcon(XBCItem item);

    /**
     * Get icon mode.
     *
     * @param iconModeIndex
     * @return icon mode
     */
    public XBCXIconMode getIconMode(Long iconModeIndex);

    /**
     * Get list of icons for given specification.
     *
     * @param blockSpec specification
     * @return list of icons
     */
    public List<XBCXIcon> getBlockSpecIcons(XBCBlockSpec blockSpec);

    /**
     * Get default icon for given item.
     *
     * @param item item we want to get icon of
     * @return icon instance or null
     */
    public ImageIcon getDefaultImageIcon(XBCItem item);
}
