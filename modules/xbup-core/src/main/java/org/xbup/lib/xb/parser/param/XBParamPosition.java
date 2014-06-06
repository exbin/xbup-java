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
package org.xbup.lib.xb.parser.param;

/**
 * Position of parameter within sequence of attributes and subblocks.
 *
 * @version 0.1 wr23.0 2014/02/06
 * @author XBUP Project (http://xbup.org)
 */
public class XBParamPosition {

    private long attributePosition;
    private long subBlockPosition;

    public long getAttributePosition() {
        return attributePosition;
    }

    public void setAttributePosition(long attributePosition) {
        this.attributePosition = attributePosition;
    }

    public long getSubBlockPosition() {
        return subBlockPosition;
    }

    public void setSubBlockPosition(long subBlockPosition) {
        this.subBlockPosition = subBlockPosition;
    }
}
