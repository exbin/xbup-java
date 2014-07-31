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
 * @version 0.1 wr21.0 2011/12/31
 * @author XBUP Project (http://xbup.org)
 */
public interface XBCXIconService<T extends XBCXIcon> extends XBCService<T>, XBCExtension {

    public XBCXIcon getDefaultIcon(XBCItem item);

    public XBCXIconMode getIconMode(Long type);

    public List<XBCXIcon> getBlockSpecIcons(XBCBlockSpec icon);

    /**
     * Get default icon for given block type.
     *
     * @param item Item we want to get icon of.
     * @return icon instance or null.
     */
    public ImageIcon getDefaultImageIcon(XBCItem item);
}
