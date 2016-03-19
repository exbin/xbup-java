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
package org.exbin.xbup.web.xbcatalogweb.base;

import org.exbin.xbup.catalog.entity.XBENode;
import org.exbin.xbup.catalog.entity.XBEXDesc;
import org.exbin.xbup.catalog.entity.XBEXName;
import org.exbin.xbup.catalog.entity.XBEXStri;
import org.exbin.xbup.core.catalog.base.XBCBase;

/**
 * Package record entity interface.
 *
 * @version 0.1.23 2014/05/23
 * @author ExBin Project (http://exbin.org)
 */
public interface XBCPackageRecord extends XBCBase {

    XBEXDesc getDesc();

    String getFullName();

    XBEXName getName();

    XBENode getNode();

    String getPackageName();

    String getPrefix();

    XBEXStri getStri();

    boolean isHasChildren();

    void setDesc(XBEXDesc desc);

    void setHasChildren(boolean hasChildren);

    void setName(XBEXName name);

    void setNode(XBENode node);

    void setPrefix(String prefix);

    void setStri(XBEXStri stri);
    
}
