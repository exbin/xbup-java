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
import java.io.InputStream;
import org.exbin.xbup.core.parser.XBParsingException;
import org.exbin.xbup.core.parser.XBProcessingExceptionType;
import org.exbin.xbup.core.stream.FinishableStream;

/**
 * Input stream wrapper for fixed length data block.
 *
 * @author ExBin Project (https://exbin.org)
 */
public class FixedDataInputStreamWrapper extends InputStream implements FinishableStream {

    private final InputStream source;

    private int remaining = 0;
    private int length = 0;

    public FixedDataInputStreamWrapper(final InputStream source, int fixedLength) throws IOException {
        this.source = source;
        remaining = fixedLength;
    }

    @Override
    public int read() throws IOException {
        if (remaining == 0) {
            return -1;
        }

        int read = source.read();
        if (read >= 0) {
            length++;
            remaining--;
        } else {
            remaining = 0;
        }

        return read;
    }

    @Override
    public int read(byte[] buffer) throws IOException {
        if (remaining == 0) {
            return -1;
        }

        int read = buffer.length > remaining ? source.read(buffer, 0, remaining) : source.read(buffer);

        if (read > 0) {
            length += read;
            remaining -= read;
        }

        return read;
    }

    @Override
    public int read(byte[] buffer, int off, int len) throws IOException {
        if (remaining == 0) {
            return -1;
        }

        int read = source.read(buffer, off, len > remaining ? remaining : len);

        if (read > 0) {
            length += read;
            remaining -= read;
        }

        return read;
    }

    @Override
    public long finish() throws IOException, XBParsingException {
        // Read up remaining data
        if (remaining > 0) {
            byte[] buf = new byte[1024];
            while (remaining > 0) {
                int read = source.read(buf, 0, remaining < 1024 ? remaining : 1024);

                if (read > 0) {
                    length += read;
                    remaining -= read;
                } else {
                    break;
                }
            }

            if (remaining > 0) {
                throw new XBParsingException("Unexpected end of stream", XBProcessingExceptionType.UNEXPECTED_END_OF_STREAM);
            }
        }

        return length;
    }

    @Override
    public int available() throws IOException {
        return remaining > 0 ? 1 : 0;
    }

    @Override
    public long getLength() {
        return length;
    }
}
