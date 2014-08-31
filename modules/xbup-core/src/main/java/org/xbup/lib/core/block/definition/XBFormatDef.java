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
package org.xbup.lib.core.block.definition;

import java.util.List;
import org.xbup.lib.core.block.declaration.local.XBDFormatDecl;
import org.xbup.lib.core.serial.XBSerializable;
import org.xbup.lib.core.ubnumber.UBNatural;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * XBUP level 1 format definition.
 *
 * @version 0.1.21 2011/12/02
 * @author XBUP Project (http://xbup.org)
 */
public class XBFormatDef implements XBSerializable {

    private UBNatural consistSkip = new UBNat32(0);
    private UBNatural joinCount = new UBNat32(0);
    private List<XBDFormatDecl> formats;

    public XBFormatDef() {
    }

    /**
     * @return the consistSkip
     */
    public UBNatural getConsistSkip() {
        return consistSkip;
    }

    /**
     * @param consistSkip the consistSkip to set
     */
    public void setConsistSkip(UBNatural consistSkip) {
        this.consistSkip = consistSkip;
    }

    /**
     * @return the joinCount
     */
    public UBNatural getJoinCount() {
        return joinCount;
    }

    /**
     * @param joinCount the joinCount to set
     */
    public void setJoinCount(UBNatural joinCount) {
        this.joinCount = joinCount;
    }

    /**
     * @return the formats
     */
    public List<XBDFormatDecl> getFormats() {
        return formats;
    }

    /**
     * @param formats the formats to set
     */
    public void setFormats(List<XBDFormatDecl> formats) {
        this.formats = formats;
    }
}
