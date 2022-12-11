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
 * @author ExBin Project (https://exbin.org)
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
