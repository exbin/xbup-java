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
package org.exbin.xbup.core.parser.basic.wrapper;

import java.io.IOException;
import java.io.OutputStream;
import org.exbin.xbup.core.stream.FinishableStream;

/**
 * Output stream wrapper for fixed length data block.
 *
 * @version 0.1.23 2014/03/06
 * @author ExBin Project (http://exbin.org)
 */
public class FixedDataOutputStreamWrapper extends OutputStream implements FinishableStream {

    private final OutputStream stream;

    private int remaining = 0;
    private int length = 0;

    public FixedDataOutputStreamWrapper(final OutputStream stream, int fixedLength) throws IOException {
        this.stream = stream;
        remaining = fixedLength;
    }

    @Override
    public void write(int b) throws IOException {
        if (remaining > 0) {
            stream.write(b);
            remaining--;
            length++;
        }
    }

    @Override
    public void write(byte b[]) throws IOException {
        if (b.length < remaining) {
            stream.write(b, 0, b.length);
            remaining -= b.length;
            length += b.length;
        } else {
            throw new IOException("Attempt to write more data than specified");
        }
    }

    @Override
    public void write(byte b[], int off, int len) throws IOException {
        if (len < remaining) {
            stream.write(b, off, len);
            remaining -= len;
            length += len;
        } else {
            throw new IOException("Attempt to write more data than specified");
        }
    }

    @Override
    public long finish() throws IOException {
        if (remaining > 0) {
            write(0);
        }

        return length;
    }

    @Override
    public long getLength() {
        return length;
    }
}
