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
package org.exbin.xbup.core.test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import junit.framework.Assert;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import org.exbin.xbup.core.block.XBBlock;
import org.exbin.xbup.core.block.XBBlockDataMode;
import org.exbin.xbup.core.block.XBBlockTerminationMode;
import org.exbin.xbup.core.block.XBDocument;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.parser.basic.XBFilter;
import org.exbin.xbup.core.parser.basic.XBListener;
import org.exbin.xbup.core.parser.token.XBAttribute;
import org.exbin.xbup.core.parser.token.XBAttributeToken;
import org.exbin.xbup.core.parser.token.XBBeginToken;
import org.exbin.xbup.core.parser.token.XBDataToken;
import org.exbin.xbup.core.parser.token.XBToken;
import org.exbin.xbup.core.parser.token.XBTokenType;
import org.exbin.xbup.core.type.XBData;
import org.exbin.xbup.core.util.StreamUtils;

/**
 * Utilities for testing.
 *
 * @version 0.1.25 2015/07/25
 * @author ExBin Project (http://exbin.org)
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
     * Compares two documents for full exact match.
     *
     * @param expectedDocument expected document
     * @param document compared document
     */
    public static void assertEqualsXBDocuments(XBDocument expectedDocument, XBDocument document) {
        long extendedAreaSize = expectedDocument.getExtendedAreaSize();
        assertEquals(extendedAreaSize, document.getExtendedAreaSize());
        assertEqualsXBBlock(expectedDocument.getRootBlock(), document.getRootBlock());
    }

    /**
     * Compares two blocks for full exact match.
     *
     * @param expectedBlock expected block
     * @param block compared block
     */
    public static void assertEqualsXBBlock(XBBlock expectedBlock, XBBlock block) {
        if (expectedBlock == null && block == null) {
            return;
        }

        Assert.assertNotNull(expectedBlock);
        Assert.assertNotNull(block);
        assertEquals(expectedBlock.getDataMode(), block.getDataMode());
        assertEquals(expectedBlock.getTerminationMode(), block.getTerminationMode());
        if (block.getDataMode() == XBBlockDataMode.NODE_BLOCK) {
            int attributesCount = expectedBlock.getAttributesCount();
            assertEquals(attributesCount, block.getAttributesCount());
            if (attributesCount > 0) {
                XBAttribute[] expectedAttributes = expectedBlock.getAttributes();
                XBAttribute[] attributes = block.getAttributes();
                for (int i = 0; i < expectedAttributes.length; i++) {
                    assertEquals(expectedAttributes[i].getNaturalLong(), attributes[i].getNaturalLong());
                }
            }
            int childrenCount = expectedBlock.getChildrenCount();
            assertEquals(childrenCount, block.getChildrenCount());
            if (childrenCount > 0) {
                XBBlock[] expectedChildren = expectedBlock.getChildren();
                XBBlock[] children = block.getChildren();
                for (int i = 0; i < expectedChildren.length; i++) {
                    assertEqualsXBBlock(expectedChildren[i], children[i]);
                }
            }
        } else {
            assertEqualsInputStream(expectedBlock.getData(), block.getData());
        }
    }

    /**
     * Filters awaiting single token and checks for matching value.
     */
    public static class TokenAssertXBFilter implements XBFilter {

        private boolean correct = true;
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

            if (correct && token != null && awaiting && token.getTokenType() == XBTokenType.BEGIN && ((XBBeginToken) token).getTerminationMode() == terminationMode) {
                awaiting = false;
            } else {
                correct = false;
            }
        }

        @Override
        public void attribXB(XBAttribute attribute) throws XBProcessingException, IOException {
            if (listener != null) {
                listener.attribXB(attribute);
            }

            if (correct && token != null && awaiting && token.getTokenType() == XBTokenType.ATTRIBUTE && ((XBAttributeToken) token).getAttribute().getNaturalLong() == attribute.getNaturalLong()) {
                awaiting = false;
            } else {
                correct = false;
            }
        }

        @Override
        public void dataXB(InputStream data) throws XBProcessingException, IOException {
            if (listener != null) {
                listener.dataXB(data);
            }

            if (correct && token != null && awaiting && token.getTokenType() == XBTokenType.DATA) {
                awaiting = false;
            } else {
                correct = false;
            }
        }

        @Override
        public void endXB() throws XBProcessingException, IOException {
            if (listener != null) {
                listener.endXB();
            }

            if (correct && token != null && awaiting && token.getTokenType() == XBTokenType.END) {
                awaiting = false;
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

        private FinishedMode finishedMode = FinishedMode.START;
        private int depth = 0;
        private int position = 0;
        private final List<XBToken> tokenList;
        private XBListener listener;

        public BufferAssertXBFilter(List<XBToken> tokenList) {
            this.tokenList = tokenList;
        }

        @Override
        public void beginXB(XBBlockTerminationMode terminationMode) throws XBProcessingException, IOException {
            if (finishedMode == FinishedMode.FINISHED) {
                finishedMode = FinishedMode.AFTER_END;
            }

            if (listener != null) {
                listener.beginXB(terminationMode);
            }
            depth++;

            assertTrue(tokenList != null && position < tokenList.size() && tokenList.get(position).getTokenType() == XBTokenType.BEGIN && ((XBBeginToken) tokenList.get(position)).getTerminationMode() == terminationMode);
            position++;
        }

        @Override
        public void attribXB(XBAttribute attribute) throws XBProcessingException, IOException {
            if (finishedMode == FinishedMode.FINISHED) {
                finishedMode = FinishedMode.AFTER_END;
            }

            if (listener != null) {
                listener.attribXB(attribute);
            }

            assertTrue(tokenList != null && position < tokenList.size() && tokenList.get(position).getTokenType() == XBTokenType.ATTRIBUTE && ((XBAttributeToken) tokenList.get(position)).getAttribute().getNaturalLong() == attribute.getNaturalLong());
            position++;
        }

        @Override
        public void dataXB(InputStream data) throws XBProcessingException, IOException {
            if (finishedMode == FinishedMode.FINISHED) {
                finishedMode = FinishedMode.AFTER_END;
            }

            InputStream comparableData = data;

            if (listener != null) {
                XBData passedData = new XBData();
                XBData matchedData = new XBData();
                StreamUtils.copyInputStreamToTwoOutputStreams(data, passedData.getDataOutputStream(), matchedData.getDataOutputStream());
                listener.dataXB(passedData.getDataInputStream());
                comparableData = matchedData.getDataInputStream();
            }

            assertTrue(tokenList != null && position < tokenList.size() && tokenList.get(position).getTokenType() == XBTokenType.DATA);
            XBDataToken dataToken = (XBDataToken) tokenList.get(position);
            assertEqualsInputStream(dataToken.getData(), comparableData);
            position++;
        }

        @Override
        public void endXB() throws XBProcessingException, IOException {
            if (finishedMode == FinishedMode.FINISHED) {
                finishedMode = FinishedMode.AFTER_END;
            }

            if (listener != null) {
                listener.endXB();
            }

            depth--;
            if (depth == 0 && finishedMode == FinishedMode.START) {
                finishedMode = FinishedMode.FINISHED;
            }

            assertTrue(tokenList != null && position < tokenList.size() && tokenList.get(position).getTokenType() == XBTokenType.END);
            position++;
        }

        public boolean isFinished() {
            return finishedMode == FinishedMode.FINISHED;
        }

        @Override
        public void attachXBListener(XBListener listener) {
            this.listener = listener;
        }
    }

    private static enum FinishedMode {

        START, FINISHED, AFTER_END;
    }
}
