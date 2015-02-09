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

import java.io.IOException;
import java.nio.charset.Charset;
import org.xbup.lib.core.block.declaration.XBDeclBlockType;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.serial.param.XBPSequenceSerialHandler;
import org.xbup.lib.core.serial.param.XBPSequenceSerializable;
import org.xbup.lib.core.serial.param.XBSerializationMode;

/**
 * Encapsulation class for charset.
 *
 * @version 0.1.24 2015/01/28
 * @author XBUP Project (http://xbup.org)
 */
public class XBCharset implements XBPSequenceSerializable {
    
    private Charset charset;
    public static long[] XBUP_BLOCKREV_CATALOGPATH = {1, 2, 3, 0};

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
    
    @Override
    public void serializeXB(XBPSequenceSerialHandler serial) throws XBProcessingException, IOException {
        serial.begin();
        serial.matchType(new XBDeclBlockType(XBUP_BLOCKREV_CATALOGPATH));
        if (serial.getSerializationMode() == XBSerializationMode.PULL) {
            XBString charsetName = new XBString();
            serial.join(charsetName);
            charset = Charset.forName(charsetName.getValue());
        } else {
            XBString charsetName = new XBString(charset.name());
            serial.join(charsetName);
        }
        serial.end();
    }
}
