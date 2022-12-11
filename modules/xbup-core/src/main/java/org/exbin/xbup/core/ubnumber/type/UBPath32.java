/*
 * Copyright (C) ExBin Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.exbin.xbup.core.ubnumber.type;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.core.block.XBBlockTerminationMode;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.parser.token.XBAttribute;
import org.exbin.xbup.core.serial.child.XBTChildInputSerialHandler;
import org.exbin.xbup.core.serial.child.XBTChildOutputSerialHandler;
import org.exbin.xbup.core.serial.child.XBTChildSerializable;
import org.exbin.xbup.core.ubnumber.UBStreamable;
import org.exbin.xbup.core.ubnumber.exception.UBOverFlowException;

/**
 * UBPath with 32bit long items.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class UBPath32 implements UBStreamable, XBTChildSerializable {

    private long[] path;

    public UBPath32() {
        path = new long[0];
    }

    public UBPath32(long[] path) {
        this.path = path;
    }

    public UBPath32(Long[] catalogPath) {
        setValue(catalogPath);
    }

    @Override
    public int fromStreamUB(InputStream stream) throws IOException, XBProcessingException {
        UBNat32 arrayLength = new UBNat32();
        int length = arrayLength.fromStreamUB(stream);
        path = new long[arrayLength.getInt()];
        for (int i = 0; i < path.length; i++) {
            UBNat32 value = new UBNat32();
            length += value.fromStreamUB(stream);
            path[i] = value.getLong();
        }
        return length;
    }

    @Override
    public int toStreamUB(OutputStream stream) throws IOException {
        int length = (new UBNat32(path.length)).toStreamUB(stream);
        for (int i = 0; i < path.length; i++) {
            length += (new UBNat32(path[i])).toStreamUB(stream);
        }
        return length;
    }

    @Override
    public int getSizeUB() {
        int length = (new UBNat32(path.length)).getSizeUB();
        for (int i = 0; i < path.length; i++) {
            length += (new UBNat32(path[i])).getSizeUB();
        }
        return length;
    }

    public void setValue(long[] path) throws UBOverFlowException {
        this.path = path;
    }

    private void setValue(Long[] catalogPath) {
        path = new long[catalogPath.length];
        for (int i = 0; i < catalogPath.length; i++) {
            path[i] = catalogPath[i];
        }
    }

    public long[] getPath() throws UBOverFlowException {
        return path;
    }

    public Long[] getLongPath() throws UBOverFlowException {
        Long[] longPath = new Long[path.length];
        for (int i = 0; i < path.length; i++) {
            longPath[i] = path[i];
        }
        return longPath;
    }

    @Override
    public void serializeFromXB(XBTChildInputSerialHandler serial) throws XBProcessingException, IOException {
        serial.pullBegin();
        XBAttribute length = serial.pullAttribute();
        path = new long[length.getNaturalInt()];
        for (int i = 0; i < path.length; i++) {
            path[i] = serial.pullAttribute().getNaturalLong();
        }

        serial.pullEnd();
    }

    @Override
    public void serializeToXB(XBTChildOutputSerialHandler serial) throws XBProcessingException, IOException {
        serial.putBegin(XBBlockTerminationMode.SIZE_SPECIFIED);
        serial.putAttribute(new UBNat32(path.length));
        for (int i = 0; i < path.length; i++) {
            serial.putAttribute(new UBNat32(path[i]));
        }

        serial.putEnd();
    }
}
