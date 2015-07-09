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
package org.xbup.lib.core.test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.basic.XBFilter;
import org.xbup.lib.core.parser.basic.XBListener;
import org.xbup.lib.core.parser.token.XBAttribute;
import org.xbup.lib.core.parser.token.XBAttributeToken;
import org.xbup.lib.core.parser.token.XBBeginToken;
import org.xbup.lib.core.parser.token.XBToken;
import org.xbup.lib.core.parser.token.XBTokenType;

/**
 * Utilities for testing.
 *
 * @version 0.1.25 2015/07/06
 * @author XBUP Project (http://xbup.org)
 */
public class XBTestUtils {

    /**
     * Loads data from two streams and test them for exact match.
     *
     * @param expectedStream stream to be considered as template
     * @param stream stream for matching
     */
    public static void assertEqualsInputStream(InputStream expectedStream, InputStream stream) {
        try {
            byte[] dataBlob = new byte[2];
            byte[] dataBlob2 = new byte[2];
            int position = 0;
            while (expectedStream.available() > 0) {
                int readStat = expectedStream.read(dataBlob, 0, 1);
                if (readStat < 0) {
                    fail("Unable to read expected stream on position " + position);
                }
                int readStat2 = stream.read(dataBlob2, 0, 1);
                if (readStat2 < 0) {
                    fail("Unable to read compared stream on position " + position);
                }

                assertEquals(dataBlob[0], dataBlob2[0]);
                position++;
            }

            assertTrue(stream.available() == 0);
        } catch (IOException ex) {
            fail("IOException " + ex.getMessage());
        }
    }

    /**
     * Filters awaiting single token and checks for matching value.
     */
    public static class TokenAssertXBFilter implements XBFilter {

        private boolean correct = false;
        private boolean awaiting = true;
        private final XBToken token;
        private XBListener listener;

        public TokenAssertXBFilter(XBToken token) {
            this.token = token;
        }

        @Override
        public void beginXB(XBBlockTerminationMode terminationMode) throws XBProcessingException, IOException {
            if (listener != null) {
                listener.beginXB(terminationMode);
            }

            if (token != null && awaiting && token.getTokenType() == XBTokenType.BEGIN && ((XBBeginToken) token).getTerminationMode() == terminationMode) {
                awaiting = false;
                correct = true;
            } else {
                correct = false;
            }
        }

        @Override
        public void attribXB(XBAttribute attribute) throws XBProcessingException, IOException {
            if (listener != null) {
                listener.attribXB(attribute);
            }

            if (token != null && awaiting && token.getTokenType() == XBTokenType.ATTRIBUTE && ((XBAttributeToken) token).getAttribute().getNaturalLong() == attribute.getNaturalLong()) {
                awaiting = false;
                correct = true;
            } else {
                correct = false;
            }
        }

        @Override
        public void dataXB(InputStream data) throws XBProcessingException, IOException {
            if (listener != null) {
                listener.dataXB(data);
            }

            if (token != null && awaiting && token.getTokenType() == XBTokenType.DATA) {
                awaiting = false;
                correct = true;
            } else {
                correct = false;
            }
        }

        @Override
        public void endXB() throws XBProcessingException, IOException {
            if (listener != null) {
                listener.endXB();
            }

            if (token != null && awaiting && token.getTokenType() == XBTokenType.END) {
                awaiting = false;
                correct = true;
            } else {
                correct = false;
            }
        }

        public boolean isCorrect() {
            return correct;
        }

        @Override
        public void attachXBListener(XBListener listener) {
            this.listener = listener;
        }
    }

    /**
     * Filters awaiting single data token and checks for matching value.
     */
    public static class DataTokenAssertXBFilter extends TokenAssertXBFilter {

        public DataTokenAssertXBFilter(XBToken token) {
            super(token);
        }

        @Override
        public void dataXB(InputStream data) throws XBProcessingException, IOException {
            super.dataXB(data);
            byte[] dataBlob = new byte[2];
            int position = 0;
            while ((data.available() >= 0) && (position < 10)) {
                int readStat = data.read(dataBlob, 0, 1);
                if (readStat < 0) {
                    fail("Incorrect extended data");
                }

                assertEquals('0' + position, dataBlob[0]);
                position++;
            }

            assertEquals("Extended data are too short", 10, position);
        }
    }

    /**
     * Filters awaiting tokens and checks for match to given token buffer.
     */
    public static class BufferAssertXBFilter implements XBFilter {

        private boolean correct = false;
        private int position = 0;
        private final List<XBToken> tokenList;
        private XBListener listener;

        public BufferAssertXBFilter(List<XBToken> tokenList) {
            this.tokenList = tokenList;
        }

        @Override
        public void beginXB(XBBlockTerminationMode terminationMode) throws XBProcessingException, IOException {
            if (listener != null) {
                listener.beginXB(terminationMode);
            }

            if (tokenList != null && position < tokenList.size() && tokenList.get(position).getTokenType() == XBTokenType.BEGIN && ((XBBeginToken) tokenList.get(position)).getTerminationMode() == terminationMode) {
                position++;
                correct = true;
            } else {
                correct = false;
            }
        }

        @Override
        public void attribXB(XBAttribute attribute) throws XBProcessingException, IOException {
            if (listener != null) {
                listener.attribXB(attribute);
            }

            if (tokenList != null && position < tokenList.size() && tokenList.get(position).getTokenType() == XBTokenType.ATTRIBUTE && ((XBAttributeToken) tokenList.get(position)).getAttribute().getNaturalLong() == attribute.getNaturalLong()) {
                position++;
                correct = true;
            } else {
                correct = false;
            }
        }

        @Override
        public void dataXB(InputStream data) throws XBProcessingException, IOException {
            if (listener != null) {
                listener.dataXB(data);
            }

            if (tokenList != null && position < tokenList.size() && (tokenList.get(position) == null || tokenList.get(position).getTokenType() == XBTokenType.DATA)) {
                position++;
                correct = true;
            } else {
                correct = false;
            }
        }

        @Override
        public void endXB() throws XBProcessingException, IOException {
            if (listener != null) {
                listener.endXB();
            }

            if (tokenList != null && position < tokenList.size() && tokenList.get(position).getTokenType() == XBTokenType.END) {
                position++;
                correct = true;
            } else {
                correct = false;
            }
        }

        public boolean isCorrect() {
            return correct;
        }

        @Override
        public void attachXBListener(XBListener listener) {
            this.listener = listener;
        }
    }

    /**
     * Filters awaiting tokens and checks for match to given token buffer and
     * particular data blob.
     */
    public static class DataBufferAssertXBFilter extends BufferAssertXBFilter {

        public DataBufferAssertXBFilter(List<XBToken> tokenList) {
            super(tokenList);
        }

        @Override
        public void dataXB(InputStream data) throws XBProcessingException, IOException {
            XBToken token = super.tokenList.get(super.position);
            super.dataXB(data);
            if (token != null) {
                byte[] dataBlob = new byte[2];
                int position = 0;
                while ((data.available() >= 0) && (position < 10)) {
                    int readStat = data.read(dataBlob, 0, 1);
                    if (readStat < 0) {
                        fail("Incorrect extended data");
                    }

                    assertEquals('0' + position, dataBlob[0]);
                    position++;
                }

                assertEquals("Extended data are too short", 10, position);
            }
        }
    }
}
