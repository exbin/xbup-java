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
package org.xbup.lib.catalog.entity;

import javax.persistence.Entity;
import org.xbup.lib.core.catalog.base.XBCFormatRev;
import org.xbup.lib.core.catalog.base.XBCFormatSpec;

/**
 * Format revision database entity.
 *
 * @version 0.1.21 2011/08/21
 * @author ExBin Project (http://exbin.org)
 */
@Entity(name = "XBFormatRev")
public class XBEFormatRev extends XBERev implements XBCFormatRev {

    @Override
    public XBCFormatSpec getParent() {
        return (XBCFormatSpec) super.getParent();
    }

}
