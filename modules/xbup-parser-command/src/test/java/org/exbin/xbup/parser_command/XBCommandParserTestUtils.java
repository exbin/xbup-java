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
package org.exbin.xbup.parser_command;

import java.io.IOException;
import java.io.InputStream;
import org.exbin.xbup.core.stream.SeekableStream;

/**
 * Utilities for testing.
 *
 * @version 0.2.0 2015/09/23
 * @author ExBin Project (http://exbin.org)
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
