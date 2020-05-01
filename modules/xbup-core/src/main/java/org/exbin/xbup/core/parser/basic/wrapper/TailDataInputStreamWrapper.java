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
import java.io.InputStream;

/**
 * Input stream wrapper for tail data.
 *
 * @version 0.2.0 2016/09/25
 * @author ExBin Project (http://exbin.org)
 */
public class TailDataInputStreamWrapper extends InputStream {

    private final InputStream source;

    public TailDataInputStreamWrapper(final InputStream source) {
        this.source = source;
    }

    @Override
    public int read() throws IOException {
        return source.read();
    }

    @Override
    public int available() throws IOException {
        return source.available();
    }

    @Override
    public void close() throws IOException {
        source.close();
    }

    @Override
    public int read(byte[] b) throws IOException {
        return source.read(b);
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        return source.read(b, off, len);
    }

    @Override
    public synchronized void reset() throws IOException {
    }

    @Override
    public long skip(long n) throws IOException {
        return source.skip(n);
    }
}
