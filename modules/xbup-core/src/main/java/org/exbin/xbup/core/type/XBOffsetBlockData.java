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
package org.exbin.xbup.core.type;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.utils.binary_data.BinaryData;
import org.exbin.xbup.core.util.StreamUtils;

/**
 * Simple offset transformation block data class.
 *
 * @version 0.2.0 2016/05/24
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XBOffsetBlockData implements BinaryData {

    private final BinaryData blockData;
    private final long offset;

    public XBOffsetBlockData(BinaryData blockData, long offset) {
        this.blockData = blockData;
        this.offset = offset;
    }

    @Override
    public boolean isEmpty() {
        return blockData.getDataSize() <= offset;
    }

    @Override
    public long getDataSize() {
        return isEmpty() ? 0 : blockData.getDataSize() - offset;
    }

    @Override
    public byte getByte(long position) {
        return blockData.getByte(position - offset);
    }

    @Override
    public BinaryData copy() {
        return blockData.copy(offset, blockData.getDataSize() - offset);
    }

    @Override
    public BinaryData copy(long startFrom, long length) {
        return blockData.copy(offset + startFrom, blockData.getDataSize() - startFrom - offset);
    }

    @Override
    public void copyToArray(long l, byte[] bytes, int i, int i1) {
        blockData.copyToArray(l, bytes, i, i1);
    }

    @Override
    public InputStream getDataInputStream() {
        final InputStream parentStream = blockData.getDataInputStream();
        try {
            parentStream.skip(offset);
        } catch (IOException ex) {
            Logger.getLogger(XBOffsetBlockData.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return new InputStream() {
            @Override
            public int read() throws IOException {
                return parentStream.read();
            }

            @Override
            public void close() throws IOException {
                parentStream.close();
            }

            @Override
            public int available() throws IOException {
                return parentStream.available();
            }

            @Override
            public long skip(long n) throws IOException {
                return parentStream.skip(n);
            }

            @Override
            public int read(byte[] b, int off, int len) throws IOException {
                return parentStream.read(b, off, len);
            }

            @Override
            public int read(byte[] b) throws IOException {
                return parentStream.read(b);
            }
        };
    }

    @Override
    public void saveToStream(OutputStream outputStream) throws IOException {
        InputStream inputStream = getDataInputStream();
        StreamUtils.copyInputStreamToOutputStream(inputStream, outputStream);
    }
    
    @Override
    public void dispose() {
        blockData.dispose();
    }
}
