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
package org.exbin.xbup.client;

import java.io.IOException;
import java.io.OutputStream;
import javax.annotation.Nonnull;
import org.exbin.xbup.core.type.XBData;

/**
 * Output stream copying data to data logging blob.
 *
 * @version 0.2.1 2017/06/06
 * @author ExBin Project (http://exbin.org)
 */
public class XBLoggingOutputStream extends OutputStream {

    @Nonnull
    private final XBData data = new XBData();
    @Nonnull
    private final OutputStream targetOutputStream;

    public XBLoggingOutputStream(@Nonnull OutputStream targetOutputStream) {
        this.targetOutputStream = targetOutputStream;
    }

    @Override
    public void write(int bufferData) throws IOException {
        targetOutputStream.write(bufferData);
        long dataSize = data.getDataSize();
        data.insert(dataSize, 1);
        data.setByte(dataSize, (byte) bufferData);
    }

    @Override
    public void write(@Nonnull byte[] bufferData, int offset, int length) throws IOException {
        targetOutputStream.write(bufferData, offset, length);
        data.insert(data.getDataSize(), bufferData, offset, length);
    }

    @Override
    public void write(@Nonnull byte[] bufferData) throws IOException {
        targetOutputStream.write(bufferData);
        data.insert(data.getDataSize(), bufferData);
    }

    @Nonnull
    public XBData getData() {
        return data;
    }

}
