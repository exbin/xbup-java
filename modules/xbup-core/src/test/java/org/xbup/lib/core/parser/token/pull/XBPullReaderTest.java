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
package org.xbup.lib.core.parser.token.pull;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import junit.framework.TestCase;
import org.junit.Test;
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.parser.XBParseException;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.XBProcessingExceptionType;
import org.xbup.lib.core.parser.token.XBAttributeToken;
import org.xbup.lib.core.parser.token.XBBeginToken;
import org.xbup.lib.core.parser.token.XBDataToken;
import org.xbup.lib.core.parser.token.XBToken;
import org.xbup.lib.core.parser.token.XBTokenType;

/**
 * Test class for XBPullReader.
 *
 * @version 0.1.23 2014/01/12
 * @author XBUP Project (http://xbup.org)
 */
public class XBPullReaderTest extends TestCase {

    public XBPullReaderTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of open method of the class XBPullReader.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testRead() throws Exception {
        try (InputStream source = XBPullReaderTest.class.getResourceAsStream("/org/xbup/lib/core/resources/test/samples/xhtml-test.xb")) {
            try {
                try (XBPullReader reader = new XBPullReader()) {
                    DebugPullProvider provider = new DebugPullProvider();
                    provider.attachXBPullProvider(reader);
                    reader.open(source);
                    while (!reader.isFinishedXB()) {
                        provider.pullXBToken();
                    }
                }
            } catch (FileNotFoundException ex) {
                fail("FileNotFoundException: " + ex.getMessage());
            } catch (IOException | XBProcessingException ex) {
                fail("FoundException: " + ex.getMessage());
            }
        }
    }

    /**
     * Test of open method of the class XBPullReader.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleSingleEmptyBlock() throws Exception {
        try (InputStream stream = XBPullReaderTest.class.getResourceAsStream("/org/xbup/lib/core/resources/test/samples/l0_singleemptyblock.xb")) {
            try (XBPullReader reader = new XBPullReader(stream)) {
                DebugPullProvider provider = new DebugPullProvider();
                provider.attachXBPullProvider(reader);

                XBToken token = provider.pullXBToken();
                assertEquals(XBTokenType.BEGIN, token.getTokenType());
                assertEquals(XBBlockTerminationMode.SIZE_SPECIFIED, ((XBBeginToken) token).getTerminationMode());

                token = provider.pullXBToken();
                assertEquals(XBTokenType.ATTRIBUTE, token.getTokenType());
                assertEquals(0, ((XBAttributeToken) token).getAttribute().getNaturalInt());

                token = provider.pullXBToken();
                assertEquals(XBTokenType.END, token.getTokenType());
            }
        }
    }

    /**
     * Test of open method of the class XBPullReader.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleExtended() throws Exception {
        try (InputStream stream = XBPullReaderTest.class.getResourceAsStream("/org/xbup/lib/core/resources/test/samples/l0_extended.xb")) {
            try (XBPullReader reader = new XBPullReader(stream)) {
                DebugPullProvider provider = new DebugPullProvider();
                provider.attachXBPullProvider(reader);

                XBToken token = provider.pullXBToken();
                assertEquals(XBTokenType.BEGIN, token.getTokenType());
                assertEquals(XBBlockTerminationMode.SIZE_SPECIFIED, ((XBBeginToken) token).getTerminationMode());

                token = provider.pullXBToken();
                assertEquals(XBTokenType.ATTRIBUTE, token.getTokenType());
                assertEquals(0, ((XBAttributeToken) token).getAttribute().getNaturalInt());

                token = provider.pullXBToken();
                assertEquals(XBTokenType.DATA, token.getTokenType());

                InputStream data = ((XBDataToken) token).getData();
                assertDataBlob(data);

                token = provider.pullXBToken();
                assertEquals(XBTokenType.END, token.getTokenType());
            }
        }
    }

    /**
     * Test of open method of the class XBPullReader.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleEmpty() throws Exception {
        Throwable ex = null;

        try (InputStream stream = XBPullReaderTest.class.getResourceAsStream("/org/xbup/lib/core/resources/test/samples/corrupted/l0_empty.xb")) {
            XBPullReader reader = new XBPullReader(stream);
            DebugPullProvider provider = new DebugPullProvider();
            provider.attachXBPullProvider(reader);
            try {
                provider.pullXBToken();
            } catch (XBProcessingException | IOException e) {
                ex = e;
            }

            assertEquals(new XBProcessingException("End of data reached", XBProcessingExceptionType.UNEXPECTED_END_OF_STREAM), ex);
        }
    }

    /**
     * Test of open method of the class XBPullReader.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleSingleByte() throws Exception {
        Throwable ex = null;
        try (InputStream stream = XBPullReaderTest.class.getResourceAsStream("/org/xbup/lib/core/resources/test/samples/corrupted/l0_singlebyte.xb")) {
            XBPullReader reader = new XBPullReader(stream);
            DebugPullProvider provider = new DebugPullProvider();
            provider.attachXBPullProvider(reader);
            try {
                provider.pullXBToken();
            } catch (XBProcessingException | IOException e) {
                ex = e;
            }

            assertEquals(new XBProcessingException("End of data reached", XBProcessingExceptionType.UNEXPECTED_END_OF_STREAM), ex);
        }
    }

    /**
     * Test of open method of the class XBPullReader.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleWrongHeader() throws Exception {
        Throwable ex = null;

        try (InputStream stream = XBPullReaderTest.class.getResourceAsStream("/org/xbup/lib/core/resources/test/samples/corrupted/l0_wrongheader.xb")) {
            XBPullReader provider = new XBPullReader(stream);
            try {
                provider.pullXBToken();
            } catch (XBProcessingException | IOException e) {
                ex = e;
            }

            assertEquals(new XBParseException("Unsupported header: 0xfe0059420002", XBProcessingExceptionType.CORRUPTED_HEADER), ex);
        }
    }

    /**
     * Test of open method of the class XBPullReader.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleSingleData() throws Exception {
        try (InputStream stream = XBPullReaderTest.class.getResourceAsStream("/org/xbup/lib/core/resources/test/samples/l0_singledata.xb")) {
            try (XBPullReader reader = new XBPullReader(stream)) {
                DebugPullProvider provider = new DebugPullProvider();
                provider.attachXBPullProvider(reader);

                XBToken token = provider.pullXBToken();
                assertEquals(XBTokenType.BEGIN, token.getTokenType());
                assertEquals(XBBlockTerminationMode.SIZE_SPECIFIED, ((XBBeginToken) token).getTerminationMode());

                token = provider.pullXBToken();
                assertEquals(XBTokenType.DATA, token.getTokenType());

                InputStream data = ((XBDataToken) token).getData();
                assertDataBlob(data);

                token = provider.pullXBToken();
                assertEquals(XBTokenType.END, token.getTokenType());
            }
        }
    }

    /**
     * Test of open method of the class XBPullReader.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleTerminated() throws Exception {
        try (InputStream stream = XBPullReaderTest.class.getResourceAsStream("/org/xbup/lib/core/resources/test/samples/l0_terminated.xb")) {
            try (XBPullReader reader = new XBPullReader(stream)) {
                DebugPullProvider provider = new DebugPullProvider();
                provider.attachXBPullProvider(reader);

                XBToken token = provider.pullXBToken();
                assertEquals(XBTokenType.BEGIN, token.getTokenType());
                assertEquals(XBBlockTerminationMode.TERMINATED_BY_ZERO, ((XBBeginToken) token).getTerminationMode());

                token = provider.pullXBToken();
                assertEquals(XBTokenType.ATTRIBUTE, token.getTokenType());
                assertEquals(0, ((XBAttributeToken) token).getAttribute().getNaturalInt());

                token = provider.pullXBToken();
                assertEquals(XBTokenType.ATTRIBUTE, token.getTokenType());
                assertEquals(0, ((XBAttributeToken) token).getAttribute().getNaturalInt());

                token = provider.pullXBToken();
                assertEquals(XBTokenType.ATTRIBUTE, token.getTokenType());
                assertEquals(1, ((XBAttributeToken) token).getAttribute().getNaturalInt());

                token = provider.pullXBToken();
                assertEquals(XBTokenType.ATTRIBUTE, token.getTokenType());
                assertEquals(2, ((XBAttributeToken) token).getAttribute().getNaturalInt());

                token = provider.pullXBToken();
                assertEquals(XBTokenType.END, token.getTokenType());
            }
        }
    }

    /**
     * Test of open method of the class XBPullReader.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleSixBlocks() throws Exception {
        try (InputStream stream = XBPullReaderTest.class.getResourceAsStream("/org/xbup/lib/core/resources/test/samples/l0_sixblocks.xb")) {
            try (XBPullReader reader = new XBPullReader(stream)) {
                DebugPullProvider provider = new DebugPullProvider();
                provider.attachXBPullProvider(reader);

                XBToken token = provider.pullXBToken();
                assertEquals(XBTokenType.BEGIN, token.getTokenType());
                assertEquals(XBBlockTerminationMode.SIZE_SPECIFIED, ((XBBeginToken) token).getTerminationMode());

                token = provider.pullXBToken();
                assertEquals(XBTokenType.ATTRIBUTE, token.getTokenType());
                assertEquals(0, ((XBAttributeToken) token).getAttribute().getNaturalInt());

                token = provider.pullXBToken();
                assertEquals(XBTokenType.BEGIN, token.getTokenType());
                assertEquals(XBBlockTerminationMode.SIZE_SPECIFIED, ((XBBeginToken) token).getTerminationMode());

                token = provider.pullXBToken();
                assertEquals(XBTokenType.ATTRIBUTE, token.getTokenType());
                assertEquals(0, ((XBAttributeToken) token).getAttribute().getNaturalInt());

                token = provider.pullXBToken();
                assertEquals(XBTokenType.BEGIN, token.getTokenType());
                assertEquals(XBBlockTerminationMode.SIZE_SPECIFIED, ((XBBeginToken) token).getTerminationMode());

                token = provider.pullXBToken();
                assertEquals(XBTokenType.ATTRIBUTE, token.getTokenType());
                assertEquals(0, ((XBAttributeToken) token).getAttribute().getNaturalInt());

                token = provider.pullXBToken();
                assertEquals(XBTokenType.BEGIN, token.getTokenType());
                assertEquals(XBBlockTerminationMode.SIZE_SPECIFIED, ((XBBeginToken) token).getTerminationMode());

                token = provider.pullXBToken();
                assertEquals(XBTokenType.DATA, token.getTokenType());

                token = provider.pullXBToken();
                assertEquals(XBTokenType.END, token.getTokenType());

                token = provider.pullXBToken();
                assertEquals(XBTokenType.END, token.getTokenType());

                token = provider.pullXBToken();
                assertEquals(XBTokenType.END, token.getTokenType());

                token = provider.pullXBToken();
                assertEquals(XBTokenType.BEGIN, token.getTokenType());
                assertEquals(XBBlockTerminationMode.SIZE_SPECIFIED, ((XBBeginToken) token).getTerminationMode());

                token = provider.pullXBToken();
                assertEquals(XBTokenType.ATTRIBUTE, token.getTokenType());
                assertEquals(0, ((XBAttributeToken) token).getAttribute().getNaturalInt());

                token = provider.pullXBToken();
                assertEquals(XBTokenType.BEGIN, token.getTokenType());
                assertEquals(XBBlockTerminationMode.SIZE_SPECIFIED, ((XBBeginToken) token).getTerminationMode());

                token = provider.pullXBToken();
                assertEquals(XBTokenType.ATTRIBUTE, token.getTokenType());
                assertEquals(0, ((XBAttributeToken) token).getAttribute().getNaturalInt());

                token = provider.pullXBToken();
                assertEquals(XBTokenType.ATTRIBUTE, token.getTokenType());
                assertEquals(0, ((XBAttributeToken) token).getAttribute().getNaturalInt());

                token = provider.pullXBToken();
                assertEquals(XBTokenType.ATTRIBUTE, token.getTokenType());
                assertEquals(1, ((XBAttributeToken) token).getAttribute().getNaturalInt());

                token = provider.pullXBToken();
                assertEquals(XBTokenType.ATTRIBUTE, token.getTokenType());
                assertEquals(2, ((XBAttributeToken) token).getAttribute().getNaturalInt());

                token = provider.pullXBToken();
                assertEquals(XBTokenType.ATTRIBUTE, token.getTokenType());
                assertEquals(3, ((XBAttributeToken) token).getAttribute().getNaturalInt());

                token = provider.pullXBToken();
                assertEquals(XBTokenType.END, token.getTokenType());

                token = provider.pullXBToken();
                assertEquals(XBTokenType.END, token.getTokenType());

                token = provider.pullXBToken();
                assertEquals(XBTokenType.END, token.getTokenType());
            }
        }
    }

    private static class DebugPullProvider implements XBPullProvider, XBPullConsumer {

        private XBPullProvider pullProvider;

        @Override
        public void attachXBPullProvider(XBPullProvider pullProvider) {
            this.pullProvider = pullProvider;
        }

        @Override
        public XBToken pullXBToken() throws XBProcessingException, IOException {
            XBToken token = pullProvider.pullXBToken();
            switch (token.getTokenType()) {
                case BEGIN: {
                    System.out.println("> Begin (" + (((XBBeginToken) token).getTerminationMode()).toString() + "):");
                    break;
                }
                case ATTRIBUTE: {
                    System.out.println("  Attribute: " + ((XBAttributeToken) token).getAttribute().getNaturalLong());
                    break;
                }
                case DATA: {
                    System.out.println("  Data:" + ((XBDataToken) token).getData().available());
                    break;
                }
                case END: {
                    System.out.println("< End.");
                    break;
                }
                default:
                    throw new UnsupportedOperationException();
            }

            return token;
        }
    }

    private void assertDataBlob(InputStream data) throws IOException {
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
