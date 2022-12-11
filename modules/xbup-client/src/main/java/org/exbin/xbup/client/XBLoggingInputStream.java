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
package org.exbin.xbup.client;

import java.io.IOException;
import java.io.InputStream;
import javax.annotation.Nonnull;
import org.exbin.xbup.core.type.XBData;

/**
 * Input stream copying data to data logging blob.
 *
 * @author ExBin Project (https://exbin.org)
 */
public class XBLoggingInputStream extends InputStream {

    @Nonnull
    private final XBData data = new XBData();
    @Nonnull
    private final InputStream sourceInputStream;

    public XBLoggingInputStream(@Nonnull InputStream sourceInputStream) {
        this.sourceInputStream = sourceInputStream;
    }

    @Override
    public int read() throws IOException {
        int result = sourceInputStream.read();
        if (result >= 0) {
            data.setByte(data.getDataSize(), (byte) result);
        }
        return result;
    }

    @Override
    public int read(@Nonnull byte[] bufferData, int offset, int length) throws IOException {
        int read = sourceInputStream.read(bufferData, offset, length);
        if (read >= 0) {
            data.insert(data.getDataSize(), bufferData, offset, length);
        }
        return read;
    }

    @Override
    public int read(@Nonnull byte[] bufferData) throws IOException {
        int read = sourceInputStream.read(bufferData);
        if (read >= 0) {
            data.insert(data.getDataSize(), bufferData);
        }
        return read;
    }

    @Nonnull
    public XBData getData() {
        return data;
    }
}
