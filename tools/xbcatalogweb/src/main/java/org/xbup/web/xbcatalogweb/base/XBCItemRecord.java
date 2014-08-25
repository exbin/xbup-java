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
package org.xbup.web.xbcatalogweb.base;

import org.xbup.lib.core.catalog.base.XBCBase;
import org.xbup.lib.catalog.entity.XBEItem;
import org.xbup.lib.catalog.entity.XBEXDesc;
import org.xbup.lib.catalog.entity.XBEXName;
import org.xbup.lib.catalog.entity.XBEXStri;

/**
 * Item record entity interface.
 *
 * @version 0.1.23 2014/05/23
 * @author XBUP Project (http://xbup.org)
 */
public interface XBCItemRecord extends XBCBase {

    XBEXDesc getDesc();

    XBEItem getItem();

    String getItemType();

    XBEXName getName();

    String getNameOrId();

    XBEXStri getStri();

    void setDesc(XBEXDesc desc);

    void setItem(XBEItem item);

    void setName(XBEXName name);

    void setStri(XBEXStri stri);
    
}
