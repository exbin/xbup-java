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
import org.xbup.lib.core.block.declaration.XBDeclBlockType;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.serial.param.XBPSequenceSerialHandler;
import org.xbup.lib.core.serial.param.XBPSequenceSerializable;
import org.xbup.lib.core.ubnumber.UBInteger;
import org.xbup.lib.core.ubnumber.UBReal;
import org.xbup.lib.core.ubnumber.exception.UBOverFlowException;

/**
 * TODO: UBReal stored as two UBInteger values.
 *
 * @version 0.2.0 2015/11/29
 * @author XBUP Project (http://xbup.org)
 */
public class UBRea implements UBReal, XBPSequenceSerializable {

    private UBInteger value;
    private UBInteger mantissa;

    public UBRea() {
        value = new UBInt32();
        mantissa = new UBInt32();
    }

    public UBRea(float srcValue) {
        // TODO Replace with more efficient algorithm later
        // TODO Handle conversion exceptions

        int valueInteger = 0;
        int mantissaInteger = 0;
        if (srcValue > 0) {
            if (srcValue - Math.floor(srcValue) > 0) {
                do {
                    srcValue = srcValue * 2;
                    mantissaInteger--;
                } while (srcValue - Math.floor(srcValue) > 0 && mantissaInteger > -32);
                valueInteger = (int) Math.floor(srcValue);
            } else {
                while (srcValue % 2 == 0 && mantissaInteger < 32) {
                    srcValue = srcValue / 2;
                    mantissaInteger++;
                }
                do {
                } while (srcValue - Math.floor(srcValue) > 0 && mantissaInteger > -32);
                valueInteger = (int) Math.floor(srcValue);
            }
            // TODO
        } else if (srcValue < 0) {
            // TODO
        }

        value = new UBInt32(valueInteger);
        mantissa = new UBInt32(mantissaInteger);
    }

    public UBRea(UBReal real) {
        value = real.getBase();
        mantissa = real.getMantissa();
    }

    @Override
    public int getInt() throws UBOverFlowException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public long getLong() throws UBOverFlowException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public float getFloat() throws UBOverFlowException {
        // TODO Validate
        if (mantissa.isZero()) {
            if (value.isZero()) {
                return 0;
            }

            int intValue = value.getInt();
            return intValue > 0 ? intValue * 2 - 1 : intValue * 2 + 1;
        } else {
            int intValue = value.getInt();
            int intMantissa = value.getInt();
            if (intMantissa > 0) {
                return (intValue * 2 + 1) * (1 >> intMantissa);
            } else {
                return (intValue * 2 + 1) / (1 >> (-intMantissa));
            }
        }
    }

    @Override
    public double getDouble() throws UBOverFlowException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public UBInteger getBase() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public UBInteger getMantissa() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isZero() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setValue(int value) throws UBOverFlowException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setValue(long value) throws UBOverFlowException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setValue(float value) throws UBOverFlowException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setValue(double value) throws UBOverFlowException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int hashCode() {
        return value.hashCode() ^ mantissa.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }

        final UBRea other = (UBRea) obj;
        return this.value.equals(other.value) && this.mantissa.equals(other.mantissa);
    }

    @Override
    public void serializeXB(XBPSequenceSerialHandler serial) throws XBProcessingException, IOException {
        serial.begin();
        serial.matchType(new XBDeclBlockType(XBUP_BLOCKREV_CATALOGPATH));
        serial.attribute(value);
        serial.attribute(mantissa);
        serial.end();
    }
}
