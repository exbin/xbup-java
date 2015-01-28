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
package org.xbup.lib.core.type;

import java.nio.charset.Charset;
import org.xbup.lib.core.serial.XBSerializable;

/**
 * Encapsulation class for charset.
 *
 * @version 0.1.23 2014/03/03
 * @author XBUP Project (http://xbup.org)
 */
public class XBCharset implements XBSerializable {

    private Charset charset;
    public static long[] XB_BLOCK_PATH = {1, 2, 3, 0}; // Testing only

    public XBCharset() {
        charset = Charset.defaultCharset();
    }

    public XBCharset(Charset charset) {
        this.charset = charset;
    }

    public Charset getCharset() {
        return charset;
    }

    public void setCharset(Charset charset) {
        this.charset = charset;
    }
    
    // TODO Serialization
}
