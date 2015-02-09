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
package org.xbup.lib.core.parser.token.event;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import junit.framework.TestCase;
import org.junit.Test;
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.XBProcessingExceptionType;
import org.xbup.lib.core.parser.token.XBAttributeToken;
import org.xbup.lib.core.parser.token.XBBeginToken;
import org.xbup.lib.core.parser.token.XBDataToken;
import org.xbup.lib.core.parser.token.XBEndToken;
import org.xbup.lib.core.parser.token.XBToken;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * Test class for XBEventWriter.
 *
 * @version 0.1.23 2014/02/05
 * @author XBUP Project (http://xbup.org)
 */
public class XBEventWriterTest extends TestCase {

    public XBEventWriterTest(String testName) {
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
     * Test of open method of the class XBEventWriter.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWrite() throws Exception {
        ByteArrayOutputStream target = new ByteArrayOutputStream();
        try {
            try (XBEventWriter writer = new XBEventWriter()) {
                DebugListener listener = new DebugListener(writer);

                writer.open(target);
                listener.putXBToken(new XBBeginToken(XBBlockTerminationMode.SIZE_SPECIFIED));
                listener.putXBToken(new XBAttributeToken(new UBNat32(1)));
                listener.putXBToken(new XBAttributeToken(new UBNat32(2)));
                listener.putXBToken(new XBEndToken());
                writer.closeXB();
            }
        } catch (FileNotFoundException ex) {
            fail("File not found");
        } catch (IOException | XBProcessingException ex) {
            fail("Processing error: " + ex.getMessage());
        }
    }

    /**
     * Test of open method of the class XBEventWriter.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleSingleEmptyBlock() throws Exception {
        InputStream stream = getClass().getResourceAsStream("/org/xbup/lib/core/resources/test/samples/l0_singleemptyblock.xb");
        ByteArrayOutputStream target = new ByteArrayOutputStream();
        try (XBEventWriter writer = new XBEventWriter(target)) {
            DebugListener listener = new DebugListener(writer);

            listener.putXBToken(new XBBeginToken(XBBlockTerminationMode.SIZE_SPECIFIED));
            listener.putXBToken(new XBAttributeToken(new UBNat32(0)));
            listener.putXBToken(new XBEndToken());
            writer.closeXB();
        }

        assertEqualsInputStream(stream, new ByteArrayInputStream(target.toByteArray()));
    }

    /**
     * Test of open method of the class XBEventWriter.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleExtended() throws Exception {
        InputStream stream = getClass().getResourceAsStream("/org/xbup/lib/core/resources/test/samples/l0_extended.xb");
        ByteArrayOutputStream target = new ByteArrayOutputStream();
        try (XBEventWriter writer = new XBEventWriter(target)) {
            DebugListener listener = new DebugListener(writer);

            listener.putXBToken(new XBBeginToken(XBBlockTerminationMode.SIZE_SPECIFIED));
            listener.putXBToken(new XBAttributeToken(new UBNat32(0)));
            byte[] data = {48, 49, 50, 51, 52, 53, 54, 55, 56, 57};
            ByteArrayInputStream dataStream = new ByteArrayInputStream(data);
            listener.putXBToken(new XBDataToken(dataStream));
            listener.putXBToken(new XBEndToken());
            writer.closeXB();
        }

        assertEqualsInputStream(stream, new ByteArrayInputStream(target.toByteArray()));
    }

    /**
     * Test of open method of the class XBEventWriter.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteNone() throws Exception {
        Throwable ex = null;

        ByteArrayOutputStream target = new ByteArrayOutputStream();
        try {
            try (XBEventWriter writer = new XBEventWriter(target)) {
                writer.closeXB();
            }
        } catch (XBProcessingException | IOException e) {
            ex = e;
        }

        assertEquals(new XBProcessingException("Unexpected end of stream", XBProcessingExceptionType.UNEXPECTED_END_OF_STREAM), ex);
    }

    /**
     * Test of open method of the class XBEventWriter.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteUnfinishedBlock() throws Exception {
        Throwable ex = null;

        ByteArrayOutputStream target = new ByteArrayOutputStream();
        try {
            try (XBEventWriter writer = new XBEventWriter(target)) {
                DebugListener listener = new DebugListener(writer);

                listener.putXBToken(new XBBeginToken(XBBlockTerminationMode.SIZE_SPECIFIED));
                writer.closeXB();
            }
        } catch (XBProcessingException | IOException e) {
            ex = e;
        }

        assertEquals(new XBProcessingException("Unexpected end of stream", XBProcessingExceptionType.UNEXPECTED_END_OF_STREAM), ex);
    }

    /**
     * Test of open method of the class XBEventWriter.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleSingleData() throws Exception {
        InputStream stream = getClass().getResourceAsStream("/org/xbup/lib/core/resources/test/samples/l0_singledata.xb");
        ByteArrayOutputStream target = new ByteArrayOutputStream();
        try (XBEventWriter writer = new XBEventWriter(target)) {
            DebugListener listener = new DebugListener(writer);

            listener.putXBToken(new XBBeginToken(XBBlockTerminationMode.SIZE_SPECIFIED));
            byte[] data = {48, 49, 50, 51, 52, 53, 54, 55, 56, 57};
            ByteArrayInputStream dataStream = new ByteArrayInputStream(data);
            listener.putXBToken(new XBDataToken(dataStream));
            listener.putXBToken(new XBEndToken());
            writer.closeXB();
        }

        assertEqualsInputStream(stream, new ByteArrayInputStream(target.toByteArray()));
    }

    /**
     * Test of open method of the class XBEventWriter.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleTerminated() throws Exception {
        InputStream stream = getClass().getResourceAsStream("/org/xbup/lib/core/resources/test/samples/l0_terminated.xb");
        ByteArrayOutputStream target = new ByteArrayOutputStream();
        try (XBEventWriter writer = new XBEventWriter(target)) {
            DebugListener listener = new DebugListener(writer);

            listener.putXBToken(new XBBeginToken(XBBlockTerminationMode.TERMINATED_BY_ZERO));
            listener.putXBToken(new XBAttributeToken(new UBNat32(0)));
            listener.putXBToken(new XBAttributeToken(new UBNat32(0)));
            listener.putXBToken(new XBAttributeToken(new UBNat32(1)));
            listener.putXBToken(new XBAttributeToken(new UBNat32(2)));
            listener.putXBToken(new XBEndToken());
            writer.closeXB();
        }

        assertEqualsInputStream(stream, new ByteArrayInputStream(target.toByteArray()));
    }

    /**
     * Test of open method of the class XBEventWriter.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleSixBlocks() throws Exception {
        InputStream stream = getClass().getResourceAsStream("/org/xbup/lib/core/resources/test/samples/l0_sixblocks.xb");
        ByteArrayOutputStream target = new ByteArrayOutputStream();
        try (XBEventWriter writer = new XBEventWriter(target)) {
            DebugListener listener = new DebugListener(writer);

            listener.putXBToken(new XBBeginToken(XBBlockTerminationMode.SIZE_SPECIFIED));
            listener.putXBToken(new XBAttributeToken(new UBNat32(0)));
            listener.putXBToken(new XBBeginToken(XBBlockTerminationMode.SIZE_SPECIFIED));
            listener.putXBToken(new XBAttributeToken(new UBNat32(0)));
            listener.putXBToken(new XBBeginToken(XBBlockTerminationMode.SIZE_SPECIFIED));
            listener.putXBToken(new XBAttributeToken(new UBNat32(0)));
            listener.putXBToken(new XBBeginToken(XBBlockTerminationMode.SIZE_SPECIFIED));
            byte[] data = {};
            ByteArrayInputStream dataStream = new ByteArrayInputStream(data);
            listener.putXBToken(new XBDataToken(dataStream));
            listener.putXBToken(new XBEndToken());
            listener.putXBToken(new XBEndToken());
            listener.putXBToken(new XBEndToken());
            listener.putXBToken(new XBBeginToken(XBBlockTerminationMode.SIZE_SPECIFIED));
            listener.putXBToken(new XBAttributeToken(new UBNat32(0)));
            listener.putXBToken(new XBBeginToken(XBBlockTerminationMode.SIZE_SPECIFIED));
            listener.putXBToken(new XBAttributeToken(new UBNat32(0)));
            listener.putXBToken(new XBAttributeToken(new UBNat32(0)));
            listener.putXBToken(new XBAttributeToken(new UBNat32(1)));
            listener.putXBToken(new XBAttributeToken(new UBNat32(2)));
            listener.putXBToken(new XBAttributeToken(new UBNat32(3)));
            listener.putXBToken(new XBEndToken());
            listener.putXBToken(new XBEndToken());
            listener.putXBToken(new XBEndToken());
            writer.closeXB();
        }

        assertEqualsInputStream(stream, new ByteArrayInputStream(target.toByteArray()));
    }

    private class DebugListener implements XBEventListener {

        private final XBEventListener listener;

        public DebugListener(XBEventListener listener) {
            this.listener = listener;
        }

        @Override
        public void putXBToken(XBToken token) throws XBProcessingException, IOException {
            switch (token.getTokenType()) {
                case BEGIN: {
                    System.out.println("> Begin (" + ((XBBeginToken) token).getTerminationMode().toString() + "):");
                    listener.putXBToken(token);
                    break;
                }
                case ATTRIBUTE: {
                    System.out.println("  Attribute: " + ((XBAttributeToken) token).getAttribute().getNaturalLong());
                    listener.putXBToken(token);
                    break;
                }
                case DATA: {
                    System.out.println("  Data:" + ((XBDataToken) token).getData().available());
                    listener.putXBToken(token);
                    break;
                }
                case END: {
                    System.out.println("< End.");
                    listener.putXBToken(token);
                    break;
                }
            }
        }
    }

    private void assertEqualsInputStream(InputStream expectedStream, InputStream stream) {
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
}
