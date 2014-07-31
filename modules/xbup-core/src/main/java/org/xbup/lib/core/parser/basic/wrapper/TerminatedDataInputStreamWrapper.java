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
package org.xbup.lib.core.parser.basic.wrapper;

import java.io.IOException;
import java.io.InputStream;
import org.xbup.lib.core.parser.XBParseException;
import org.xbup.lib.core.parser.XBProcessingExceptionType;

/**
 * Input stream wrapper for terminated data block.
 *
 * Terminator is 0x0000. Sequence 0x00XX for XX &gt; 0 is interpreted as sequence
 * of zeros XX bytes long.
 *
 * @version 0.1 wr23.0 2014/01/10
 * @author XBUP Project (http://xbup.org)
 */
public class TerminatedDataInputStreamWrapper extends InputStream implements InputStreamWrapper {

    private final InputStream source;

    private boolean eof;
    private int zeroCount;
    private final int value;
    private int length = 0;

    public TerminatedDataInputStreamWrapper(final InputStream source) throws IOException {
        this.source = source;

        eof = false;
        zeroCount = 0;
        value = source.read();
        if (value == -1) {
            zeroCount = -1;
            eof = true;
        } else if (value == 0) {
            zeroCount = source.read();
            eof = (zeroCount < 1);
        }
    }

    @Override
    public int read() throws IOException {
        if (eof) {
            return -1;
        }

        if (zeroCount > 0) {
            zeroCount--;
            return 0;
        }

        int nextValue = source.read();
        if (nextValue < 0) {
            zeroCount = -1;
            eof = true;
        } else {
            length++;
            if (nextValue == 0) {
                zeroCount = source.read();
                if (zeroCount >= 0) {
                    length++;
                }

                eof = (zeroCount < 1);
            }
        }

        return value;
    }

    @Override
    public int finish() throws IOException, XBParseException {
        while (!eof) {
            int blockLength = read();
            if (blockLength < 0) {
                break;
            }

            length += blockLength;
        }

        if (zeroCount < 0) {
            throw new XBParseException("Missing data block terminator", XBProcessingExceptionType.UNEXPECTED_END_OF_STREAM);
        }

        return length;
    }

    @Override
    public int available() throws IOException {
        return eof ? 1 : 0;
    }

    @Override
    public int getLength() {
        return length;
    }
}
