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
package org.exbin.xbup.core.block.definition;

import java.io.IOException;
import org.exbin.xbup.core.block.XBBasicBlockType;
import org.exbin.xbup.core.block.XBFixedBlockType;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.serial.param.XBPSequenceSerialHandler;
import org.exbin.xbup.core.serial.param.XBPSequenceSerializable;
import org.exbin.xbup.core.serial.param.XBSerializationMode;

/**
 * XBUP level 1 revision parameter.
 *
 * @version 0.1.25 2015/02/20
 * @author ExBin Project (http://exbin.org)
 */
public class XBRevisionParam implements XBPSequenceSerializable {

    private long paramCount;

    public XBRevisionParam() {
    }

    public XBRevisionParam(long paramCount) {
        this.paramCount = paramCount;
    }

    public long getParamCount() {
        return paramCount;
    }

    public void setParamCount(long paramCount) {
        this.paramCount = paramCount;
    }

    @Override
    public void serializeXB(XBPSequenceSerialHandler serial) throws XBProcessingException, IOException {
        serial.begin();
        serial.matchType(new XBFixedBlockType(XBBasicBlockType.REVISION_PARAMETER));
        if (serial.getSerializationMode() == XBSerializationMode.PULL) {
            paramCount = serial.pullLongAttribute();
        } else {
            serial.putAttribute(paramCount);
        }
        serial.end();
    }
}
