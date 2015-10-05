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
package org.xbup.lib.parser_command;

import java.io.IOException;
import java.io.InputStream;
import org.xbup.lib.core.stream.SeekableStream;

/**
 * Utilities for testing.
 *
 * @version 0.2.0 2015/09/23
 * @author XBUP Project (http://xbup.org)
 */
public class XBCommandParserTestUtils {

    /**
     * Returns input stream for given resource supporting SeekableStream
     * interface.
     *
     * @param name resource name
     * @return input stream
     */
    public static InputStream getResourceAsSeekableStream(String name) {
        return new SeekableInputStream(name);
    }

    /**
     * Encapsulating class for resource stream as seekable stream.
     */
    public static class SeekableInputStream extends InputStream implements SeekableStream {

        private InputStream source = null;
        private String name = null;
        private boolean closed = false;

        public SeekableInputStream(String name) {
            this.name = name;
            source = XBReaderBlockReadTest.class.getResourceAsStream(name);
        }

        @Override
        public int available() throws IOException {
            return source.available();
        }

        @Override
        public int read() throws IOException {
            return source.read();
        }

        @Override
        public int read(byte[] b, int off, int len) throws IOException {
            return source.read(b, off, len);
        }

        @Override
        public int read(byte[] b) throws IOException {
            return source.read(b);
        }

        @Override
        public long skip(long n) throws IOException {
            return source.skip(n);
        }

        @Override
        public void close() throws IOException {
            closed = true;
            source.close();
        }

        @Override
        public void seek(long position) throws IOException {
            if (closed) {
                throw new IOException("Attempt to seek closed stream");
            }

            source.close();
            source = XBReaderBlockReadTest.class.getResourceAsStream(name);
            source.skip(position);
        }

        @Override
        public long getStreamSize() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
}
