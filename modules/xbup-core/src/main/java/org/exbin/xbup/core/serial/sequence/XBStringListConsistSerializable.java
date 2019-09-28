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
package org.exbin.xbup.core.serial.sequence;

import java.io.IOException;
import java.security.InvalidParameterException;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.serial.XBSerializable;
import org.exbin.xbup.core.serial.param.XBPSequenceSerialHandler;
import org.exbin.xbup.core.serial.param.XBPSequenceSerializable;
import org.exbin.xbup.core.serial.param.XBSerializationMode;
import org.exbin.xbup.core.type.XBString;
import org.exbin.xbup.core.ubnumber.UBENatural;
import org.exbin.xbup.core.ubnumber.type.UBENat32;

/**
 * XBUP level 1 serialization interface for potentionally infinite list.
 *
 * @version 0.2.0 2015/12/03
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XBStringListConsistSerializable implements XBListConsistSerializable {

    private int position = 0;
    private final List<String> values;

    public XBStringListConsistSerializable(List<String> values) {
        this.values = values;
    }

    @Override
    public UBENatural getSize() {
        return new UBENat32(values.size());
    }

    @Override
    public void setSize(UBENatural size) {
        if (size == null || size.isInfinity()) {
            throw new InvalidParameterException("Specified size must be natural number");
        }

        int intSize = size.getInt();
        int valuesSize = values.size();
        if (intSize > valuesSize) {
            while (valuesSize < intSize) {
                values.add(null);
                valuesSize++;
            }
        } else if (intSize < valuesSize) {
            while (valuesSize > intSize) {
                values.remove(valuesSize - 1);
                valuesSize--;
            }
        }
    }

    @Override
    public void reset() {
        position = 0;
    }

    @Nonnull
    @Override
    public XBSerializable next() {
        return new ItemHandler(position++);
    }

    private class ItemHandler implements XBPSequenceSerializable {

        private final int itemIndex;

        public ItemHandler(int itemIndex) {
            this.itemIndex = itemIndex;
        }

        @Override
        public void serializeXB(XBPSequenceSerialHandler serial) throws XBProcessingException, IOException {
            serial.begin();
            serial.matchType();
            if (serial.getSerializationMode() == XBSerializationMode.PULL) {
                XBString value = new XBString();
                serial.consist(value);
                values.set(itemIndex, value.getValue());
            } else {
                serial.consist(new XBString(values.get(itemIndex)));
            }
            serial.end();
        }
    }
}
