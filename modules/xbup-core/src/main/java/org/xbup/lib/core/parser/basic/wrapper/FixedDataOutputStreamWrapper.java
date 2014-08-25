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
import java.io.OutputStream;

/**
 * Output stream wrapper for fixed length data block.
 *
 * @version 0.1.23 2014/03/06
 * @author XBUP Project (http://xbup.org)
 */
public class FixedDataOutputStreamWrapper extends OutputStream implements OutputStreamWrapper {

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
    public int finish() throws IOException {
        if (remaining > 0) {
            write(0);
        }
        
        return length;
    }

    @Override
    public int getLength() {
        return length;
    }
}
