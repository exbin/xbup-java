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
package org.xbup.lib.xb.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Utilities for copying stream data.
 *
 * @version 0.1 wr23.0 2014/03/09
 * @author XBUP Project (http://xbup.org)
 */
public abstract class CopyStreamUtils {

    private static final int BUFFER_SIZE = 1024;

    /**
     * Copy all data from input stream to output stream using 1k buffer.
     *
     * @param source input stream
     * @param target output stream
     * @throws java.io.IOException if read or write fails
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
     * Copy all data from input stream to output stream using 1k buffer with
     * size limitation.
     *
     * @param source input stream
     * @param target output stream
     * @param size data size limitation
     * @throws java.io.IOException if read or write fails
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
     * Copy data of given size from input stream to output stream using 1k
     * buffer with size limitation.
     *
     * @param source input stream
     * @param target output stream
     * @param size data size limitation
     * @throws java.io.IOException if read or write fails
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

    public static void readUpInputStream(InputStream source) throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];

        while (source.available() > 0) {
            source.read(buffer, 0, BUFFER_SIZE);
        }
    }

    /**
     * Copy all data from input stream to output stream using 1k buffer.
     *
     * @param source input stream
     * @param target output stream
     * @param secondTarget second output stream
     * @throws java.io.IOException if read or write fails
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
}
