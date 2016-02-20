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
package org.xbup.lib.client;

import java.io.IOException;
import java.io.InputStream;
import org.xbup.lib.core.type.XBData;

/**
 * Input stream copying data to data logging blob.
 *
 * @version 0.2.0 2016/02/20
 * @author XBUP Project (http://xbup.org)
 */
public class XBLoggingInputStream extends InputStream {

    private final XBData data = new XBData();
    private final InputStream sourceInputStream;

    public XBLoggingInputStream(InputStream sourceInputStream) {
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
    public int read(byte[] b, int off, int len) throws IOException {
        int read = sourceInputStream.read(b, off, len);
        if (read >= 0) {
            data.insert(data.getDataSize(), b, off, len);
        }
        return read;
    }

    @Override
    public int read(byte[] b) throws IOException {
        int read = sourceInputStream.read(b);
        if (read >= 0) {
            data.insert(data.getDataSize(), b);
        }
        return read;
    }

    public XBData getData() {
        return data;
    }
}
