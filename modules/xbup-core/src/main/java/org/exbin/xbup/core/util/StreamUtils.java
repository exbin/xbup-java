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
import javax.annotation.Nonnull;

/**
 * Utilities for stream data manipulations.
 *
 * @version 0.2.1 2017/05/15
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
    public static void copyInputStreamToOutputStream(@Nonnull InputStream source, @Nonnull OutputStream target) throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];
        int bufferUsed = 0;

        while (source.available() > 0) {
            int bytesRed = source.read(buffer, bufferUsed, BUFFER_SIZE - bufferUsed);
            bufferUsed += bytesRed;
            if (bufferUsed == BUFFER_SIZE) {
                target.write(buffer, 0, BUFFER_SIZE);
                bufferUsed = 0;
            }
        }

        if (bufferUsed > 0) {
            target.write(buffer, 0, bufferUsed);
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
    public static void copyInputStreamToOutputStream(@Nonnull InputStream source, @Nonnull OutputStream target, long size) throws IOException {
        long remain = size;
        int bufferSize = size < BUFFER_SIZE ? (int) size : BUFFER_SIZE;
        byte[] buffer = new byte[bufferSize];
        int bufferUsed = 0;

        while (source.available() > 0) {
            if (remain == 0) {
                throw new IOException("More data than limited to " + size + " available.");
            }

            int bytesRed = source.read(buffer, bufferUsed, bufferSize - bufferUsed);
            bufferUsed += bytesRed;
            if (bufferUsed == bufferSize) {
                target.write(buffer, 0, bufferSize);
                remain -= bufferSize;
                bufferUsed = 0;
            }
        }

        if (bufferUsed > 0) {
            target.write(buffer, 0, bufferUsed);
            remain -= bufferUsed;
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
    public static void copyFixedSizeInputStreamToOutputStream(@Nonnull InputStream source, @Nonnull OutputStream target, long size) throws IOException {
        long remain = size;
        int bufferSize = size < BUFFER_SIZE ? (int) size : BUFFER_SIZE;
        byte[] buffer = new byte[bufferSize];
        int bufferUsed = 0;

        while ((source.available() > 0) && (bufferUsed != remain)) {

            int length = (bufferSize > remain ? (int) remain : bufferSize) - bufferUsed;
            int bytesRed = source.read(buffer, bufferUsed, length);
            bufferUsed += bytesRed;
            if (bufferUsed == bufferSize) {
                target.write(buffer, 0, bufferSize);
                remain -= bufferSize;
                bufferUsed = 0;
            }
        }

        if (bufferUsed > 0) {
            target.write(buffer, 0, bufferUsed);
            remain -= bufferUsed;
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
    public static void skipInputStreamData(@Nonnull InputStream source) throws IOException {
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
     * @param skipBytes number of bytes to skip
     * @throws IOException if skip fails
     */
    public static void skipInputStreamData(@Nonnull InputStream source, long skipBytes) throws IOException {
        while (skipBytes > 0) {
            long skipped = source.skip(skipBytes > BUFFER_SIZE ? BUFFER_SIZE : skipBytes);
            if (skipped == -1) {
                throw new IOException("Unable to skip data");
            } else {
                skipBytes -= skipped;
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
    public static void copyInputStreamToTwoOutputStreams(@Nonnull InputStream source, @Nonnull OutputStream target, @Nonnull OutputStream secondTarget) throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];
        int bufferUsed = 0;

        while (source.available() > 0) {
            int bytesRed = source.read(buffer, bufferUsed, BUFFER_SIZE - bufferUsed);
            bufferUsed += bytesRed;
            if (bufferUsed == BUFFER_SIZE) {
                target.write(buffer, 0, BUFFER_SIZE);
                secondTarget.write(buffer, 0, BUFFER_SIZE);
                bufferUsed = 0;
            }
        }

        if (bufferUsed > 0) {
            target.write(buffer, 0, bufferUsed);
            secondTarget.write(buffer, 0, bufferUsed);
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
    public static boolean compareStreams(@Nonnull InputStream stream, @Nonnull InputStream compStream) throws IOException {
        byte[] dataBlob = new byte[1];
        byte[] compDataBlob = new byte[1];
        while (stream.available() > 0) {
            int nextByte = stream.read(dataBlob, 0, 1);
            if (nextByte < 0) {
                return false;
            }
            int compNextByte = compStream.read(compDataBlob, 0, 1);
            if (compNextByte < 0) {
                return false;
            }

            if (dataBlob[0] != compDataBlob[0]) {
                return false;
            }
        }

        return compStream.available() == 0;
    }
}
