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
package org.xbup.lib.core.type;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xbup.lib.core.block.XBBlockData;
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.block.XBEditableBlockData;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.stream.FinishableStream;
import org.xbup.lib.core.stream.SeekableStream;
import org.xbup.lib.core.serial.child.XBTChildInputSerialHandler;
import org.xbup.lib.core.serial.child.XBTChildOutputSerialHandler;
import org.xbup.lib.core.serial.child.XBTChildSerializable;

/**
 * Encapsulation class for binary blob.
 *
 * Data are stored using paging. Last page might be shorter than page size, but
 * not empty.
 *
 * @version 0.1.25 2015/05/01
 * @author XBUP Project (http://xbup.org)
 */
public class XBData implements XBBlockData, XBEditableBlockData, XBTChildSerializable {

    public int DEFAULT_PAGE_SIZE = 4096;

    private int pageSize = DEFAULT_PAGE_SIZE;
    private final List<byte[]> data = new ArrayList<>();

    public XBData() {
    }

    public XBData(int pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public void insert(long startFrom, long length) {
        insertUninitialized(startFrom, length);
        fillData(startFrom, length);
    }

    @Override
    public void insert(long startFrom, XBBlockData insertedData) {
        long dataSize = insertedData.getDataSize();
        insertUninitialized(startFrom, dataSize);
        insertedData.copyTo(this, 0, dataSize, startFrom);
    }

    @Override
    public void insertUninitialized(long startFrom, long length) {
        if (length < 0) {
            throw new IllegalArgumentException("Length of inserted block must be nonnegative");
        }
        if (startFrom < 0) {
            throw new IllegalArgumentException("Position of inserted block must be nonnegative");
        }
        long dataSize = getDataSize();
        if (startFrom > dataSize) {
            throw new IllegalArgumentException("Inserted block must be inside or directly after existing data");
        }

        if (length > 0) {
            long copyLength = dataSize - startFrom;
            dataSize = startFrom >= dataSize ? startFrom + length : dataSize + length;
            setDataSize(dataSize);

            long sourceEnd = dataSize - length - 1;
            long targetEnd = dataSize - 1;
            // Backward copy
            while (copyLength > 0) {
                byte[] sourcePage = getPage((int) (sourceEnd / pageSize));
                int sourceOffset = (int) (sourceEnd % pageSize);

                byte[] targetPage = getPage((int) (targetEnd / pageSize));
                int targetOffset = (int) (targetEnd % pageSize);

                int copySize = sourceOffset > targetOffset ? targetOffset : sourceOffset;
                if (copySize > length) {
                    copySize = (int) length;
                }

                System.arraycopy(sourcePage, sourceOffset - copySize, targetPage, targetOffset - copySize, copySize);
                length -= copySize;
                sourceEnd -= copySize;
                targetEnd -= copySize;
            }
        }
    }

    @Override
    public void fillData(long startFrom, long length) {
        fillData(startFrom, length, (byte) 0);
    }

    @Override
    public void fillData(long startFrom, long length, byte fill) {
        if (length < 0) {
            throw new IllegalArgumentException("Length of filled block must be nonnegative");
        }
        if (startFrom < 0) {
            throw new IllegalArgumentException("Position of filler block must be nonnegative");
        }
        if (startFrom + length > getDataSize()) {
            throw new IllegalArgumentException("Filled block must be inside existing data");
        }

        while (length > 0) {
            byte[] page = getPage((int) (startFrom / pageSize));
            int pageOffset = (int) (startFrom % pageSize);
            int fillSize = page.length - pageOffset;
            if (fillSize > length) {
                fillSize = (int) length;
            }
            Arrays.fill(page, pageOffset, fillSize, (byte) fill);
            length -= fillSize;
            startFrom += fillSize;
        }
    }

    @Override
    public XBData copy() {
        XBData targetData = new XBData();
        copyTo(targetData, 0, getDataSize(), 0);
        return targetData;
    }

    @Override
    public XBData copy(long startFrom, long length) {
        XBData targetData = new XBData();
        copyTo(targetData, startFrom, length, 0);
        return targetData;
    }

    @Override
    public void copyTo(XBEditableBlockData targetData, long startFrom, long length, long targetPos) {
        if (targetData.getDataSize() < targetPos + length) {
            targetData.setDataSize(targetPos + length);
        }

        while (length > 0) {
            byte[] sourcePage = getPage((int) (startFrom / pageSize));
            int sourceOffset = (int) (startFrom % pageSize);

            byte[] targetPage;
            int targetOffset;
            if (targetData instanceof XBData) {
                targetPage = ((XBData) targetData).getPage((int) (targetPos / pageSize));
                targetOffset = (int) (targetPos % pageSize);
            } else {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            int copySize = pageSize - sourceOffset;
            if (copySize > pageSize - targetOffset) {
                copySize = pageSize - targetOffset;
            }
            if (copySize > length) {
                copySize = (int) length;
            }

            System.arraycopy(sourcePage, sourceOffset, targetPage, targetOffset, copySize);
            length -= copySize;
            startFrom += copySize;
            targetPos += copySize;
        }
    }

    @Override
    public void setData(XBBlockData newData) {
        clear();
        newData.copyTo(this, 0, newData.getDataSize(), 0);
    }

    @Override
    public void remove(long startFrom, long length) {
        if (length < 0) {
            throw new IllegalArgumentException("Length of removed block must be nonnegative");
        }
        if (startFrom < 0) {
            throw new IllegalArgumentException("Position of removed block must be nonnegative");
        }
        if (startFrom + length > getDataSize()) {
            throw new IllegalArgumentException("Removed block must be inside existing data");
        }

        if (length > 0) {
            copyTo(this, startFrom + length, getDataSize() - startFrom - length, startFrom);
            setDataSize(getDataSize() - length);
        }
    }

    @Override
    public boolean isEmpty() {
        return data.isEmpty();
    }

    @Override
    public void clear() {
        data.clear();
    }

    public int getPagesCount() {
        return data.size();
    }

    public int getPageSize() {
        return pageSize;
    }

    @Override
    public long getDataSize() {
        return (data.size() > 1 ? (data.size() - 1) * pageSize : 0) + (data.size() > 0 ? data.get(data.size() - 1).length : 0);
    }

    @Override
    public void setDataSize(long size) {
        long dataSize = getDataSize();
        if (size > dataSize) {
            int lastPage = (int) (dataSize / pageSize);
            int lastPageSize = (int) (dataSize % pageSize);
            long remaining = size - dataSize;
            // extend last page
            if (lastPageSize > 0) {
                byte[] page = getPage(lastPage);
                int nextPageSize = remaining + lastPageSize > pageSize ? pageSize : (int) remaining + lastPageSize;
                byte[] newPage = new byte[nextPageSize];
                System.arraycopy(page, 0, newPage, 0, lastPageSize);
                setPage(lastPage, newPage);
                remaining -= (nextPageSize - lastPageSize);
                lastPage++;
            }

            while (remaining > 0) {
                int nextPageSize = remaining > pageSize ? pageSize : (int) remaining;
                data.add(new byte[nextPageSize]);
                remaining -= nextPageSize;
            }
        } else if (size < dataSize) {
            int lastPage = (int) (size / pageSize);
            int lastPageSize = (int) (size % pageSize);
            // shrink last page
            if (lastPageSize > 0) {
                byte[] page = getPage(lastPage);
                byte[] newPage = new byte[lastPageSize];
                System.arraycopy(page, 0, newPage, 0, lastPageSize);
                setPage(lastPage, newPage);
                lastPage++;
            }

            for (int pageIndex = data.size() - 1; pageIndex >= lastPage; pageIndex--) {
                data.remove(pageIndex);
            }
        }
    }

    /**
     * Gets data page allowing direct access to it.
     *
     * @param pageIndex page index
     * @return data page
     */
    public byte[] getPage(int pageIndex) {
        return data.get(pageIndex);
    }

    /**
     * Sets data page replacing existing page by reference.
     *
     * @param pageIndex page index
     * @param dataPage data page
     */
    public void setPage(int pageIndex, byte[] dataPage) {
        data.set(pageIndex, dataPage);
    }

    @Override
    public byte getByte(long position) {
        byte[] page = getPage((int) (position / pageSize));
        return page[(int) (position % pageSize)];
    }

    @Override
    public void setByte(long position, byte value) {
        byte[] page;
        page = getPage((int) (position / pageSize));
        page[(int) (position % pageSize)] = value;
    }

    @Override
    public void loadFromStream(InputStream inputStream) {
        try {
            data.clear();
            byte[] buffer = new byte[pageSize];
            int cnt;
            int offset = 0;
            while ((cnt = inputStream.read(buffer, offset, buffer.length - offset)) != -1) {
                if (cnt + offset < pageSize) {
                    offset = offset + cnt;
                } else {
                    data.add(buffer);
                    buffer = new byte[pageSize];
                    offset = 0;
                }
            }

            if (offset > 0) {
                byte[] tail = new byte[offset];
                System.arraycopy(buffer, 0, tail, 0, offset);
                data.add(tail);
            }
        } catch (IOException ex) {
            Logger.getLogger(XBData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void loadFromStream(InputStream inputStream, long dataSize) throws IOException {
        try {
            data.clear();
            while (dataSize > 0) {
                int blockSize = dataSize < pageSize ? (int) dataSize : pageSize;
                byte[] page = new byte[blockSize];

                int offset = 0;
                while (blockSize > 0) {
                    int red = inputStream.read(page, offset, blockSize);
                    if (red == -1) {
                        throw new IOException("Unexpected data processed - " + dataSize + " expected, but not met.");
                    } else {
                        offset += red;
                        blockSize -= red;
                    }
                }

                data.add(page);
                dataSize -= page.length;
            }
        } catch (IOException ex) {
            Logger.getLogger(XBData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void saveToStream(OutputStream outputStream) throws IOException {
        for (byte[] dataPage : data) {
            outputStream.write(dataPage);
        }
    }

    @Override
    public void serializeFromXB(XBTChildInputSerialHandler serial) throws XBProcessingException, IOException {
        serial.pullBegin();
        loadFromStream(serial.pullData());
        serial.pullEnd();
    }

    @Override
    public void serializeToXB(XBTChildOutputSerialHandler serial) throws XBProcessingException, IOException {
        serial.putBegin(XBBlockTerminationMode.SIZE_SPECIFIED);
        serial.putData(new DataInputStream());
        serial.putEnd();
    }

    @Override
    public DataInputStream getDataInputStream() {
        return new DataInputStream();
    }

    @Override
    public DataOutputStream getDataOutputStream() {
        return new DataOutputStream();
    }

    public class DataInputStream extends InputStream implements SeekableStream, FinishableStream {

        private long position = 0;

        @Override
        public int read() throws IOException {
            return getByte(position++);
        }

        @Override
        public void close() throws IOException {
            finish();
        }

        @Override
        public int available() throws IOException {
            return (int) (getDataSize() - position);
        }

        @Override
        public int read(byte[] output, int off, int len) throws IOException {
            if (output.length == 0 || len == 0) {
                return 0;
            }

            int length = len;
            int offset = off;
            while (length > 0) {
                byte[] page = getPage((int) (position / pageSize));
                int srcPos = (int) (position % pageSize);
                int copyLength = page.length - srcPos;
                if (copyLength > length) {
                    copyLength = length;
                }

                if (copyLength == 0) {
                    return len - length;
                }

                System.arraycopy(page, srcPos, output, offset, copyLength);
                length -= copyLength;
                position += copyLength;
                offset += copyLength;
            }

            return len;
        }

        @Override
        public void seek(long position) throws IOException {
            this.position = position;
        }

        @Override
        public long finish() throws IOException {
            position = getDataSize();
            return position;
        }

        @Override
        public long getLength() {
            return position;
        }

        @Override
        public long getStreamSize() {
            return getDataSize();
        }
    }

    public class DataOutputStream extends OutputStream implements SeekableStream, FinishableStream {

        private long position = 0;

        @Override
        public void write(int value) throws IOException {
            long dataSize = getDataSize();
            if (position == dataSize) {
                dataSize++;
                setDataSize(dataSize);
            }
            setByte(position++, (byte) value);
        }

        @Override
        public void write(byte[] input, int off, int len) throws IOException {
            long dataSize = getDataSize();
            if (position + len > dataSize) {
                setDataSize(position + len);
            }

            int length = len;
            int offset = off;
            while (length > 0) {
                byte[] page = getPage((int) (position / pageSize));
                int srcPos = (int) (position % pageSize);
                int copyLength = page.length - srcPos;
                if (copyLength > length) {
                    copyLength = length;
                }

                if (copyLength == 0) {
                    return;
                }

                System.arraycopy(input, offset, page, srcPos, copyLength);
                length -= copyLength;
                position += copyLength;
                offset += copyLength;
            }
        }

        @Override
        public void seek(long position) throws IOException {
            this.position = position;
        }

        @Override
        public long getStreamSize() {
            return getDataSize();
        }

        @Override
        public long getLength() {
            return position;
        }

        @Override
        public void close() throws IOException {
            finish();
        }

        @Override
        public long finish() throws IOException {
            position = getDataSize();
            return position;
        }
    }
}
