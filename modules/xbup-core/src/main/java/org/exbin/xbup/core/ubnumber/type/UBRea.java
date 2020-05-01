/*
 * Copyright (C) ExBin Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.exbin.xbup.core.ubnumber.type;

import java.io.IOException;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.core.block.declaration.XBDeclBlockType;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.serial.param.XBPSequenceSerialHandler;
import org.exbin.xbup.core.serial.param.XBPSequenceSerializable;
import org.exbin.xbup.core.ubnumber.UBInteger;
import org.exbin.xbup.core.ubnumber.UBReal;
import org.exbin.xbup.core.ubnumber.exception.UBOverFlowException;

/**
 * TODO: UBReal stored as two UBInteger values.
 *
 * @version 0.2.0 2015/12/02
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
public class UBRea implements UBReal, XBPSequenceSerializable {

    private UBInteger value;
    private UBInteger mantissa;

    public UBRea() {
        value = new UBInt32();
        mantissa = new UBInt32();
    }

    public UBRea(float srcValue) {
        setFloatValue(srcValue);
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

            long valueLong = value.getLong();
            return valueLong > 0 ? valueLong * 2 - 1 : valueLong * 2 + 1;
        } else {
            long valueLong = value.getLong();
            long mantissaLong = mantissa.getLong();
            if (mantissaLong > 0) {
                return (float) (((double) valueLong * 2 + 1) * (double) (1 << mantissaLong));
            } else {
                return (float) (((double) valueLong * 2 + 1) / (double) (1 << (-mantissaLong)));
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
        setFloatValue(value);
    }

    @Override
    public void setValue(double value) throws UBOverFlowException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private void setFloatValue(float srcValue) {
        // TODO Replace with more efficient algorithm later
        // TODO Handle conversion exceptions
        long valueLong = 0;
        long mantissaLong = 0;
        if (srcValue > 0) {
            if (srcValue - Math.floor(srcValue) > 0) {
                float nextValue;
                boolean hasTail;
                do {
                    nextValue = srcValue * 2;
                    hasTail = nextValue - Math.floor(nextValue) > 0;
                    if (hasTail) {
                        srcValue = nextValue;
                    }

                    mantissaLong--;
                } while (hasTail && mantissaLong > -32);
                valueLong = (long) Math.floor(srcValue);
            } else {
                float nextValue;
                boolean hasTail;
                do {
                    nextValue = srcValue / 2;
                    hasTail = nextValue - Math.floor(nextValue) > 0;
                    if (!hasTail) {
                        mantissaLong++;
                    }

                    srcValue = nextValue;
                } while (!hasTail && mantissaLong < 32);
                valueLong = (long) Math.floor(srcValue) + (mantissaLong == 0 ? 1 : 0);
            }
        } else if (srcValue < 0) {
            if (srcValue - Math.ceil(srcValue) < 0) {
                float nextValue;
                boolean hasTail;
                do {
                    nextValue = srcValue * 2;
                    hasTail = nextValue - Math.ceil(nextValue) < 0;
                    if (hasTail) {
                        srcValue = nextValue;
                    }

                    mantissaLong--;
                } while (hasTail && mantissaLong > -32);
                valueLong = (long) Math.floor(srcValue);
            } else {
                float nextValue;
                boolean hasTail;
                do {
                    nextValue = srcValue / 2;
                    hasTail = nextValue - Math.ceil(nextValue) < 0;
                    if (!hasTail) {
                        mantissaLong++;
                    }

                    srcValue = nextValue;
                } while (!hasTail && mantissaLong < 32);
                valueLong = (long) Math.floor(srcValue);
            }
        }

        value = new UBInt32(valueLong);
        mantissa = new UBInt32(mantissaLong);
    }

    @Override
    public int hashCode() {
        return value.hashCode() ^ mantissa.hashCode();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
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
