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
import java.io.OutputStream;
import org.exbin.xbup.core.stream.FinishableStream;

/**
 * Output stream wrapper for terminated data block.
 *
 * Terminator is 0x0000. Sequence 0x00XX for XX &gt; 0 is interpreted as
 * sequence of zeros XX bytes long.
 *
 * @version 0.1.25 2015/07/21
 * @author ExBin Project (http://exbin.org)
 */
public class TerminatedDataOutputStreamWrapper extends OutputStream implements FinishableStream {

    private final OutputStream stream;

    private int zeroCount = 0;
    private int length = 0;

    public TerminatedDataOutputStreamWrapper(final OutputStream stream) throws IOException {
        this.stream = stream;
    }

    @Override
    public void write(int b) throws IOException {
        if (b == 0) {
            writeProcessZero();
        } else {
            writeFlushZeros();

            stream.write(b);
            length++;
        }
    }

    @Override
    public void write(byte[] buffer, int off, int len) throws IOException {
        int pos = off;
        int processFrom = off;
        while (pos < off + len) {
            int b = buffer[off + pos];
            if (b == 0) {
                if (pos > processFrom) {
                    int writeLen = pos - processFrom;
                    stream.write(buffer, processFrom, writeLen);
                    length += writeLen;
                    processFrom = pos + 1;
                }

                writeProcessZero();
            } else {
                writeFlushZeros();
            }

            pos++;
        }

        if (processFrom < pos) {
            int writeLen = pos - processFrom;
            stream.write(buffer, processFrom, writeLen);
            length += writeLen;
        }
    }

    private void writeProcessZero() throws IOException {
        if (zeroCount < 254) {
            zeroCount++;
        } else {
            writeFlushZeros();
        }
    }

    private void writeFlushZeros() throws IOException {
        if (zeroCount > 0) {
            stream.write(0);
            stream.write(zeroCount);
            length += 2;
            zeroCount = 0;
        }
    }

    @Override
    public long finish() throws IOException {
        writeFlushZeros();

        stream.write(0);
        stream.write(0);
        length += 2;

        return length;
    }

    @Override
    public long getLength() {
        return length;
    }
}
