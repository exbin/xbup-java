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
package org.exbin.xbup.core.parser.basic;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import javax.annotation.Nonnull;
import org.exbin.xbup.core.parser.XBParsingException;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.parser.XBProcessingExceptionType;

/**
 * XBUP document header validator/provider.
 *
 * @author ExBin Project (https://exbin.org)
 */
public class XBHead {

    /**
     * MIME type description.
     *
     * See http://en.wikipedia.org/wiki/MIME
     */
    public static final String XBUP_MIME_TYPE = "others/x-xbup";

    /**
     * Blob header for files encoded using XBUP protocol.
     *
     * FE - means cluster size 7<br>
     * 00 - Protocol version - means testing release<br>
     * 58 - aka 'X' for magic number<br>
     * 42 - aka 'B' for magic number<br>
     * 00 - Major version<br>
     * 02 - Minor version (09 used from wr9, 11 used from wr11, 01 used till
     * wr16)<br>
     */
    public static final byte[] XB_HEADER_DEVELOPMENT_VERSION = {-2, 0, 'X', 'B', 0, 2};

    private static final String DEFAULT_BUNLDE_PACKAGE = "sun.util.logging.resources.logging";
    public static final Level XB_DEBUG_LEVEL = new XBDebugLevel("XB_DEBUG", 658, DEFAULT_BUNLDE_PACKAGE);

    /**
     * Writes head of XBUP file into stream.
     *
     * @param stream output stream
     * @return count of written bytes
     * @throws java.io.IOException if input/output error
     */
    public static int writeXBUPHead(OutputStream stream) throws IOException {
        stream.write(XBHead.XB_HEADER_DEVELOPMENT_VERSION);
        return XBHead.XB_HEADER_DEVELOPMENT_VERSION.length;
    }

    /**
     * Returns length of XBUP file head.
     *
     * @return count of bytes of XBUP file head
     */
    public static int getXBUPHeadSize() {
        return XBHead.XB_HEADER_DEVELOPMENT_VERSION.length;
    }

    /**
     * Validates head of XBUP file in stream.
     *
     * @param stream input stream
     * @return count of processed bytes
     * @throws IOException and ParseException
     * @throws XBProcessingException if header doesn't match correct format
     */
    public static int checkXBUPHead(@Nonnull InputStream stream) throws IOException, XBProcessingException {
        byte[] head = new byte[XB_HEADER_DEVELOPMENT_VERSION.length];
        int length = XB_HEADER_DEVELOPMENT_VERSION.length;
        int offset = 0;
        int redByte = 0;
        while (redByte < length) {
            redByte = stream.read(head, offset, length);
            if (redByte < 0) {
                throw new XBParsingException("Corrupted or missing header", XBProcessingExceptionType.CORRUPTED_HEADER);
            }

            offset += redByte;
            length -= redByte;
        }

        if (!Arrays.equals(XB_HEADER_DEVELOPMENT_VERSION, head)) {
            String header = "";
            for (int i = 0; i < head.length; i++) {
                header += Integer.toString((head[i] & 0xff) + 0x100, 16).substring(1);
            }

            throw new XBParsingException("Unsupported header: 0x" + header, XBProcessingExceptionType.CORRUPTED_HEADER);
        }

        return XB_HEADER_DEVELOPMENT_VERSION.length;
    }

    private static class XBDebugLevel extends Level {

        public XBDebugLevel(String string, int i, String defaultBundle) {
            super(string, i, defaultBundle);
        }
    }

    public static class XBLogHandler extends Handler {

        boolean verboseMode;

        public XBLogHandler(boolean verboseMode) {
            this.verboseMode = verboseMode;
        }

        @Override
        public void publish(LogRecord record) {
            if (record.getLevel() == XB_DEBUG_LEVEL) {
                if (verboseMode) {
                    System.out.println("DEBUG: " + record.getMessage());
                }
            }
        }

        @Override
        public void flush() {
        }

        @Override
        public void close() throws SecurityException {
        }
    }
}
