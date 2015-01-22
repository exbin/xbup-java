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
package org.xbup.lib.core.ubnumber.type;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.xbup.lib.core.block.declaration.XBDeclBlockType;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.XBProcessingExceptionType;
import org.xbup.lib.core.serial.param.XBPSequenceSerialHandler;
import org.xbup.lib.core.serial.param.XBPSequenceSerializable;
import org.xbup.lib.core.ubnumber.UBBoolean;
import org.xbup.lib.core.ubnumber.exception.UBOverFlowException;

/**
 * UBBoolean stored as boolean value.
 *
 * @version 0.1.24 2015/01/13
 * @author XBUP Project (http://xbup.org)
 */
public class UBBool implements UBBoolean, XBPSequenceSerializable {

    private boolean value;

    public UBBool() {
        value = false;
    }

    public UBBool(boolean value) {
        this.value = value;
    }

    @Override
    public boolean getBoolean() throws UBOverFlowException {
        return value;
    }

    @Override
    public void setValue(boolean value) throws UBOverFlowException {
        this.value = value;
    }

    @Override
    public long getSegmentCount() {
        return 1;
    }

    @Override
    public long getValueSegment(long segmentIndex) {
        if (segmentIndex != 0) {
            throw new IndexOutOfBoundsException();
        }

        return value ? 1 : 0;
    }

    @Override
    public int fromStreamUB(InputStream stream) throws IOException, XBProcessingException {
        byte buf[] = new byte[1];
        if (stream.read(buf) < 0) {
            throw new XBProcessingException("End of data reached", XBProcessingExceptionType.UNEXPECTED_END_OF_STREAM);
        }
        long input = (char) buf[0] & 0xFF;
        if (input > 1 || input < 0) {
            throw new XBProcessingException("Unsupported boolean value", XBProcessingExceptionType.UNEXPECTED_END_OF_STREAM);
        }
        value = input == 1;
        return 1;
    }

    @Override
    public int toStreamUB(OutputStream stream) throws IOException {
        stream.write((char) (value ? 1 : 0));
        return 1;
    }

    @Override
    public int getSizeUB() {
        return 1;
    }

    @Override
    public void serializeXB(XBPSequenceSerialHandler serial) throws XBProcessingException, IOException {
        serial.begin();
        serial.matchType(new XBDeclBlockType(XBUP_BLOCK_TYPE));
        serial.attribute(new UBNat32(value ? 1 : 0));
        serial.end();
    }
}
