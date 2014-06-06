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
package org.xbup.lib.xb.serial;

/**
 * Serialization record to describe particular serialization to XB protocol.
 *
 * @version 0.1 wr23.0 2014/03/09
 * @author XBUP Project (http://xbup.org)
 */
public class XBSerializationToXB {
    
    private XBSerializable serializable;
    private int methodIndex;

    public XBSerializationToXB(XBSerializable serializable) {
        this(serializable, 0);
    }

    public XBSerializationToXB(XBSerializable serializable, int methodIndex) {
        this.serializable = serializable;
        this.methodIndex = methodIndex;
    }
    
    public XBSerializationType getType() {
        return XBSerializationType.TO_XB;
    }

    public XBSerializable getSerializable() {
        return serializable;
    }

    public void setSerializable(XBSerializable serializable) {
        this.serializable = serializable;
    }

    public int getMethodIndex() {
        return methodIndex;
    }

    public void setMethodIndex(int methodIndex) {
        this.methodIndex = methodIndex;
    }
}
