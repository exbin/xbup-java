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
 * Input stream wrapper for fixed length data block.
 *
 * @version 0.1.25 2015/07/27
 * @author ExBin Project (http://exbin.org)
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
    public long finish() throws IOException, XBParseException {
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
                throw new XBParseException("Unexpected end of stream", XBProcessingExceptionType.UNEXPECTED_END_OF_STREAM);
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
