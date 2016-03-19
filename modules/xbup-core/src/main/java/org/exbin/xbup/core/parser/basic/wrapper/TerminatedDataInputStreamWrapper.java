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
package org.exbin.xbup.core.parser.basic.wrapper;

import java.io.IOException;
import java.io.InputStream;
import org.exbin.xbup.core.parser.XBParseException;
import org.exbin.xbup.core.parser.XBProcessingExceptionType;
import org.exbin.xbup.core.stream.FinishableStream;

/**
 * Input stream wrapper for terminated data block.
 *
 * Terminator is 0x0000. Sequence 0x00XX for XX &gt; 0 is interpreted as
 * sequence of zeros XX bytes long.
 *
 * @version 0.1.25 2015/07/25
 * @author ExBin Project (http://exbin.org)
 */
public class TerminatedDataInputStreamWrapper extends InputStream implements FinishableStream {

    private final InputStream source;

    private int nextValue;
    private int zeroCount;
    private int length = 0;

    public TerminatedDataInputStreamWrapper(final InputStream source) throws IOException {
        this.source = source;
        fillNextValue();
    }

    @Override
    public int read() throws IOException {
        int value = nextValue;
        if (nextValue != -1) {
            fillNextValue();
        }
        return value;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        return super.read(b, off, len);
    }

    @Override
    public int read(byte[] b) throws IOException {
        return super.read(b);
    }

    private void fillNextValue() throws IOException {
        if (zeroCount > 0) {
            zeroCount--;
            nextValue = 0;
        } else {
            zeroCount = -1;
            nextValue = source.read();
            length++;
            if (nextValue == 0) {
                zeroCount = source.read();
                length++;

                if (zeroCount == 0) {
                    nextValue = -1;
                }
            }
        }
    }

    @Override
    public long finish() throws IOException, XBParseException {
        int blockLength;
        while (nextValue >= 0) {
            blockLength = read();
            if (blockLength < 0) {
                break;
            }
        }

        if (zeroCount < 0) {
            throw new XBParseException("Missing data block terminator", XBProcessingExceptionType.UNEXPECTED_END_OF_STREAM);
        }

        return length;
    }

    @Override
    public int available() throws IOException {
        return nextValue == -1 ? 0 : 1;
    }

    @Override
    public long getLength() {
        return length;
    }
}
