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
package org.exbin.xbup.catalog.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import org.exbin.xbup.core.catalog.base.XBCNode;
import org.exbin.xbup.core.catalog.base.XBCSpec;

/**
 * Specification database entity.
 *
 * @version 0.1.22 2012/12/31
 * @author ExBin Project (http://exbin.org)
 */
@Entity(name = "XBSpec")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class XBESpec extends XBEItem implements XBCSpec, Serializable {

    public XBESpec() {
    }

    @Override
    public XBCNode getParent() {
        return (XBCNode) super.getParent();
    }
}
