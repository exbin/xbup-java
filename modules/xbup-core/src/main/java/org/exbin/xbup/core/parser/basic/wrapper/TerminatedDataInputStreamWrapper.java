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
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.core.parser.XBParsingException;
import org.exbin.xbup.core.parser.XBProcessingExceptionType;
import org.exbin.xbup.core.stream.FinishableStream;

/**
 * Input stream wrapper for terminated data block.
 *
 * Terminator is 0x0000. Sequence 0x00XX for XX &gt; 0 is interpreted as
 * sequence of zeros XX bytes long.
 *
 * @version 0.2.1 2020/09/11
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
public class TerminatedDataInputStreamWrapper extends InputStream implements FinishableStream {

    private final InputStream source;

    private int zeroCount = 0;
    private long processedLength = 0;
    private boolean terminated = false;
    private int nextValue = 0;

    public TerminatedDataInputStreamWrapper(final InputStream source) throws IOException {
        this.source = source;
        nextValue = getNextValue();
    }

    @Override
    public int read() throws IOException {
        int next = nextValue;
        if (nextValue != -1) {
            nextValue = getNextValue();
        }
        return next;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        return super.read(b, off, len);
    }

    @Override
    public int read(byte[] b) throws IOException {
        return super.read(b);
    }

    @Override
    public long finish() throws IOException, XBParsingException {
        while (!terminated) {
            read();
        }

        return processedLength;
    }

    @Override
    public int available() throws IOException {
        return terminated ? 0 : 1;
    }

    @Override
    public long getLength() {
        return processedLength;
    }

    private int getNextValue() throws IOException {
        if (zeroCount > 0) {
            zeroCount--;
            return 0;
        }

        int next = source.read();
        if (next == -1) {
            throw new XBParsingException("Missing data block terminator", XBProcessingExceptionType.UNEXPECTED_END_OF_STREAM);
        }
        if (next == 0) {
            zeroCount = source.read();
            if (zeroCount < 0) {
                throw new XBParsingException("Missing data block terminator", XBProcessingExceptionType.UNEXPECTED_END_OF_STREAM);
            } else if (zeroCount == 0) {
                processedLength += 2;
                terminated = true;
                return -1;
            }

            processedLength += 2;
            zeroCount--;
            return 0;
        }

        processedLength++;
        return next;
    }
}
