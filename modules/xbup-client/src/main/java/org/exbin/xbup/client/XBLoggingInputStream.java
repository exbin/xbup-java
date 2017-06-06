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
package org.exbin.xbup.client;

import java.io.IOException;
import java.io.InputStream;
import javax.annotation.Nonnull;
import org.exbin.xbup.core.type.XBData;

/**
 * Input stream copying data to data logging blob.
 *
 * @version 0.2.1 2017/06/06
 * @author ExBin Project (http://exbin.org)
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
