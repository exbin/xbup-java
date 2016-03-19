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
package org.exbin.xbup.core.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Utilities for stream data manipulations.
 *
 * @version 0.2.0 2015/09/20
 * @author ExBin Project (http://exbin.org)
 */
public abstract class StreamUtils {

    private static final int BUFFER_SIZE = 1024;

    /**
     * Copies all data from input stream to output stream using 1k buffer.
     *
     * @param source input stream
     * @param target output stream
     * @throws IOException if read or write fails
     */
    public static void copyInputStreamToOutputStream(InputStream source, OutputStream target) throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];
        int used = 0;

        while (source.available() > 0) {
            int read = source.read(buffer, used, BUFFER_SIZE - used);
            used += read;
            if (used == BUFFER_SIZE) {
                target.write(buffer, 0, BUFFER_SIZE);
                used = 0;
            }
        }

        if (used > 0) {
            target.write(buffer, 0, used);
        }
    }

    /**
     * Copies all data from input stream to output stream using 1k buffer with
     * size limitation.
     *
     * @param source input stream
     * @param target output stream
     * @param size data size limitation
     * @throws IOException if read or write fails
     */
    public static void copyInputStreamToOutputStream(InputStream source, OutputStream target, long size) throws IOException {
        long remain = size;
        int bufferSize = size < BUFFER_SIZE ? (int) size : BUFFER_SIZE;
        byte[] buffer = new byte[bufferSize];
        int used = 0;

        while (source.available() > 0) {
            if (remain == 0) {
                throw new IOException("More data than limited to " + size + " available.");
            }

            int read = source.read(buffer, used, bufferSize - used);
            used += read;
            if (used == bufferSize) {
                target.write(buffer, 0, bufferSize);
                remain -= bufferSize;
                used = 0;
            }
        }

        if (used > 0) {
            target.write(buffer, 0, used);
            remain -= used;
        }

        if (remain > 0) {
            throw new IOException("Unexpected data processed - " + size + " expected, " + (size - remain) + " processed.");
        }
    }

    /**
     * Copies data of given size from input stream to output stream using 1k
     * buffer with size limitation.
     *
     * @param source input stream
     * @param target output stream
     * @param size data size limitation
     * @throws IOException if read or write fails
     */
    public static void copyFixedSizeInputStreamToOutputStream(InputStream source, OutputStream target, long size) throws IOException {
        long remain = size;
        int bufferSize = size < BUFFER_SIZE ? (int) size : BUFFER_SIZE;
        byte[] buffer = new byte[bufferSize];
        int used = 0;

        while ((source.available() > 0) && (used != remain)) {

            int read = source.read(buffer, used, (bufferSize > remain ? (int) remain : bufferSize) - used);
            used += read;
            if (used == bufferSize) {
                target.write(buffer, 0, bufferSize);
                remain -= bufferSize;
                used = 0;
            }
        }

        if (used > 0) {
            target.write(buffer, 0, used);
            remain -= used;
        }

        if (remain > 0) {
            throw new IOException("Unexpected data processed - " + size + " expected, " + (size - remain) + " processed.");
        }
    }

    /**
     * Skips all remaining data from input stream.
     *
     * @param source input stream
     * @throws IOException if read fails
     */
    public static void skipInputStreamData(InputStream source) throws IOException {
        while (source.available() > 0) {
            if (source.skip(BUFFER_SIZE) == -1) {
                break;
            }
        }
    }

    /**
     * Skips given amount of data from input stream.
     *
     * @param source input stream
     * @param skip number of bytes to skip
     * @throws IOException if skip fails
     */
    public static void skipInputStreamData(InputStream source, long skip) throws IOException {
        while (skip > 0) {
            long skipped = source.skip(skip > BUFFER_SIZE ? BUFFER_SIZE : skip);
            if (skipped == -1) {
                throw new IOException("Unable to skip data");
            } else {
                skip -= skipped;
            }
        }
    }

    /**
     * Copies all data from input stream to two output streams using 1k buffer.
     *
     * @param source input stream
     * @param target output stream
     * @param secondTarget second output stream
     * @throws IOException if read or write fails
     */
    public static void copyInputStreamToTwoOutputStreams(InputStream source, OutputStream target, OutputStream secondTarget) throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];
        int used = 0;

        while (source.available() > 0) {
            int read = source.read(buffer, used, BUFFER_SIZE - used);
            used += read;
            if (used == BUFFER_SIZE) {
                target.write(buffer, 0, BUFFER_SIZE);
                secondTarget.write(buffer, 0, BUFFER_SIZE);
                used = 0;
            }
        }

        if (used > 0) {
            target.write(buffer, 0, used);
            secondTarget.write(buffer, 0, used);
        }
    }

    /**
     * Compares two streams for matching data.
     *
     * @param stream one stream
     * @param compStream other stream
     * @return true if both streams have same data and length
     * @throws IOException if read or write fails
     */
    public static boolean compareStreams(InputStream stream, InputStream compStream) throws IOException {
        byte[] dataBlob = new byte[1];
        byte[] dataBlob2 = new byte[1];
        while (stream.available() > 0) {
            int readStat = stream.read(dataBlob, 0, 1);
            if (readStat < 0) {
                return false;
            }
            int readStat2 = compStream.read(dataBlob2, 0, 1);
            if (readStat2 < 0) {
                return false;
            }

            if (dataBlob[0] != dataBlob2[0]) {
                return false;
            }
        }

        return compStream.available() == 0;
    }
}
