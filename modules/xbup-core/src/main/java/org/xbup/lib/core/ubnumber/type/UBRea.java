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

import org.xbup.lib.core.ubnumber.UBInteger;
import org.xbup.lib.core.ubnumber.UBReal;
import org.xbup.lib.core.ubnumber.exception.UBOverFlowException;

/**
 * TODO: UBReal stored as two UBInteger values.
 *
 * @version 0.1 wr24.0 2014/06/08
 * @author XBUP Project (http://xbup.org)
 */
public class UBRea implements UBReal {

    private UBInteger value;
    private UBInteger mantissa;

    public UBRea(int value) {
    }

    public UBRea() {
        this(0);
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
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public double getDouble() throws UBOverFlowException {
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
    public UBInteger getBase() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public UBInteger getMantissa() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
