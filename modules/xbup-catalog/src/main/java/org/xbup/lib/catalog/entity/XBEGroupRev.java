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
package org.xbup.lib.catalog.entity;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import org.xbup.lib.core.catalog.base.XBCGroupRev;
import org.xbup.lib.core.catalog.base.XBCGroupSpec;

/**
 * Group revision database entity.
 *
 * @version 0.1.21 2011/08/21
 * @author XBUP Project (http://xbup.org)
 */
@Entity(name="XBGroupRev")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class XBEGroupRev extends XBERev implements XBCGroupRev {

    @Override
    public XBCGroupSpec getParent() {
        return (XBCGroupSpec) super.getParent();
    }
}