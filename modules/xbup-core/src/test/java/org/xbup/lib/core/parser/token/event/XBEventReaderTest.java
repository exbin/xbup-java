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

import org.xbup.lib.core.parser.token.event.XBEventListener;
import org.xbup.lib.core.parser.token.event.XBEventReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;
import junit.framework.TestCase;
import org.junit.Test;
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.parser.XBParseException;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.XBProcessingExceptionType;
import org.xbup.lib.core.parser.token.XBAttributeToken;
import org.xbup.lib.core.parser.token.XBBeginToken;
import org.xbup.lib.core.parser.token.XBDataToken;
import org.xbup.lib.core.parser.token.XBEndToken;
import org.xbup.lib.core.parser.token.XBToken;
import org.xbup.lib.core.parser.token.XBTokenType;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * Test class for XBEventReader.
 *
 * @version 0.1.23 2013/12/21
 * @author XBUP Project (http://xbup.org)
 */
public class XBEventReaderTest extends TestCase {

    public XBEventReaderTest(String testName) {
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
     * Test of open method of the class XBEventReader.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testRead() throws Exception {
        InputStream source = getClass().getResourceAsStream("/org/xbup/lib/core/resources/test/samples/xhtml-test.xb");
        try {
            XBEventReader reader = new XBEventReader();
            reader.open(source);
            reader.attachXBEventListener(new DebugListener());
            reader.read();
            reader.close();
        } catch (FileNotFoundException ex) {
            fail("File not found");
        } catch (IOException | XBProcessingException ex) {
            fail("Processing error: " + ex.getMessage());
        }
    }

    /**
     * Test of open method of the class XBEventReader.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleSingleEmptyBlock() throws Exception {
        InputStream stream = getClass().getResourceAsStream("/org/xbup/lib/core/resources/test/samples/l0_singleemptyblock.xb");
        XBEventReader instance = new XBEventReader(stream);
        AssertListener assertListener;

        List<XBToken> tokenList = new ArrayList<>();
        tokenList.add(new XBBeginToken(XBBlockTerminationMode.SIZE_SPECIFIED));
        tokenList.add(new XBAttributeToken(new UBNat32(0)));
        tokenList.add(new XBEndToken());
        tokenList.add(null);

        assertListener = new AssertListener(tokenList);
        instance.attachXBEventListener(assertListener);
        instance.read();
        assertTrue(assertListener.isCorrect());

        instance.close();
    }

    /**
     * Test of open method of the class XBEventReader.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleExtended() throws Exception {
        InputStream stream = getClass().getResourceAsStream("/org/xbup/lib/core/resources/test/samples/l0_extended.xb");
        XBEventReader instance = new XBEventReader(stream);
        AssertListener assertListener;

        List<XBToken> tokenList = new ArrayList<>();
        tokenList.add(new XBBeginToken(XBBlockTerminationMode.SIZE_SPECIFIED));
        tokenList.add(new XBAttributeToken(new UBNat32(0)));
        tokenList.add(new XBDataToken(null));
        tokenList.add(new XBEndToken());

        assertListener = new DataAssertListener(tokenList);
        instance.attachXBEventListener(assertListener);
        instance.read();
        assertTrue(assertListener.isCorrect());

        instance.close();
    }

    /**
     * Test of open method of the class XBEventReader.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleEmpty() throws Exception {
        Throwable ex = null;

        InputStream stream = getClass().getResourceAsStream("/org/xbup/lib/core/resources/test/samples/corrupted/l0_empty.xb");
        XBEventReader instance = new XBEventReader(stream);
        try {
            instance.attachXBEventListener(new AssertListener(null));
            instance.read();
        } catch (XBProcessingException | IOException e) {
            ex = e;
        }

        assertEquals(new XBProcessingException("End of data reached", XBProcessingExceptionType.UNEXPECTED_END_OF_STREAM), ex);
    }

    /**
     * Test of open method of the class XBEventReader.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleSingleByte() throws Exception {
        Throwable ex = null;

        InputStream stream = getClass().getResourceAsStream("/org/xbup/lib/core/resources/test/samples/corrupted/l0_singlebyte.xb");
        XBEventReader instance = new XBEventReader(stream);
        try {
            instance.attachXBEventListener(new AssertListener(null));
            instance.read();
        } catch (XBProcessingException | IOException e) {
            ex = e;
        }

        assertEquals(new XBProcessingException("End of data reached", XBProcessingExceptionType.UNEXPECTED_END_OF_STREAM), ex);
    }

    /**
     * Test of open method of the class XBEventReader.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleWrongHeader() throws Exception {
        Throwable ex = null;

        InputStream stream = getClass().getResourceAsStream("/org/xbup/lib/core/resources/test/samples/corrupted/l0_wrongheader.xb");
        XBEventReader instance = new XBEventReader(stream);
        try {
            instance.attachXBEventListener(new AssertListener(null));
            instance.read();
        } catch (XBProcessingException | IOException e) {
            ex = e;
        }

        assertEquals(new XBParseException("Unsupported header: 0xfe0059420002", XBProcessingExceptionType.CORRUPTED_HEADER), ex);
    }

    /**
     * Test of open method of the class XBEventReader.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleSingleData() throws Exception {
        InputStream stream = getClass().getResourceAsStream("/org/xbup/lib/core/resources/test/samples/l0_singledata.xb");
        XBEventReader instance = new XBEventReader(stream);
        AssertListener assertListener;

        List<XBToken> tokenList = new ArrayList<>();
        tokenList.add(new XBBeginToken(XBBlockTerminationMode.SIZE_SPECIFIED));
        tokenList.add(new XBDataToken(null));
        tokenList.add(new XBEndToken());
        tokenList.add(null);

        assertListener = new DataAssertListener(tokenList);
        instance.attachXBEventListener(assertListener);
        instance.read();
        assertTrue(assertListener.isCorrect());

        instance.close();
    }

    /**
     * Test of open method of the class XBEventReader.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleTerminated() throws Exception {
        InputStream stream = getClass().getResourceAsStream("/org/xbup/lib/core/resources/test/samples/l0_terminated.xb");
        XBEventReader instance = new XBEventReader(stream);
        AssertListener assertListener;

        List<XBToken> tokenList = new ArrayList<>();
        tokenList.add(new XBBeginToken(XBBlockTerminationMode.SIZE_SPECIFIED));
        tokenList.add(new XBAttributeToken(new UBNat32(0)));
        tokenList.add(new XBAttributeToken(new UBNat32(0)));
        tokenList.add(new XBAttributeToken(new UBNat32(1)));
        tokenList.add(new XBAttributeToken(new UBNat32(2)));
        tokenList.add(new XBEndToken());
        tokenList.add(null);

        assertListener = new AssertListener(tokenList);
        instance.attachXBEventListener(assertListener);
        instance.read();
        assertTrue(assertListener.isCorrect());

        instance.close();
    }

    /**
     * Test of open method of the class XBEventReader.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleSixBlocks() throws Exception {
        InputStream stream = getClass().getResourceAsStream("/org/xbup/lib/core/resources/test/samples/l0_sixblocks.xb");
        XBEventReader instance = new XBEventReader(stream);
        AssertListener assertListener;

        List<XBToken> tokenList = new ArrayList<>();
        tokenList.add(new XBBeginToken(XBBlockTerminationMode.SIZE_SPECIFIED));
        tokenList.add(new XBAttributeToken(new UBNat32(0)));
        tokenList.add(new XBBeginToken(XBBlockTerminationMode.SIZE_SPECIFIED));
        tokenList.add(new XBAttributeToken(new UBNat32(0)));
        tokenList.add(new XBBeginToken(XBBlockTerminationMode.SIZE_SPECIFIED));
        tokenList.add(new XBAttributeToken(new UBNat32(0)));
        tokenList.add(new XBBeginToken(XBBlockTerminationMode.SIZE_SPECIFIED));
        tokenList.add(new XBDataToken(null));
        tokenList.add(new XBEndToken());
        tokenList.add(new XBEndToken());
        tokenList.add(new XBEndToken());
        tokenList.add(new XBBeginToken(XBBlockTerminationMode.SIZE_SPECIFIED));
        tokenList.add(new XBAttributeToken(new UBNat32(0)));
        tokenList.add(new XBBeginToken(XBBlockTerminationMode.SIZE_SPECIFIED));
        tokenList.add(new XBAttributeToken(new UBNat32(0)));
        tokenList.add(new XBAttributeToken(new UBNat32(0)));
        tokenList.add(new XBAttributeToken(new UBNat32(1)));
        tokenList.add(new XBAttributeToken(new UBNat32(2)));
        tokenList.add(new XBAttributeToken(new UBNat32(3)));
        tokenList.add(new XBEndToken());
        tokenList.add(new XBEndToken());
        tokenList.add(new XBEndToken());
        tokenList.add(null);

        assertListener = new AssertListener(tokenList);
        instance.attachXBEventListener(assertListener);
        instance.read();
        assertTrue(assertListener.isCorrect());

        instance.close();
    }

    private class DebugListener implements XBEventListener {

        @Override
        public void putXBToken(XBToken token) throws XBProcessingException, IOException {
            switch (token.getTokenType()) {
                case BEGIN: {
                    System.out.println("> Begin (" + (((XBBeginToken) token).getTerminationMode()).toString() + "):");
                    break;
                }
                case ATTRIBUTE: {
                    System.out.println("  Attribute: " + ((XBAttributeToken) token).getAttribute().getLong());
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
        }
    }

    private class AssertListener extends DebugListener {

        private boolean correct = false;
        private int position = 0;
        private final List<XBToken> tokenList;

        public AssertListener(List<XBToken> tokenList) {
            this.tokenList = tokenList;
        }

        @Override
        public void putXBToken(XBToken token) throws XBProcessingException, IOException {
            super.putXBToken(token);
            switch (token.getTokenType()) {
                case BEGIN: {
                    if (tokenList != null && position < tokenList.size() && tokenList.get(position).getTokenType() == XBTokenType.BEGIN) {
                        position++;
                        correct = true;
                    } else {
                        correct = false;
                    }
                    break;
                }
                case ATTRIBUTE: {
                    if (tokenList != null && position < tokenList.size() && tokenList.get(position).getTokenType() == XBTokenType.ATTRIBUTE && ((XBAttributeToken) tokenList.get(position)).getAttribute().getLong() == (((XBAttributeToken) token).getAttribute()).getLong()) {
                        position++;
                        correct = true;
                    } else {
                        correct = false;
                    }
                    break;
                }
                case DATA: {
                    if (tokenList != null && position < tokenList.size() && (tokenList.get(position) == null || tokenList.get(position).getTokenType() == XBTokenType.DATA)) {
                        position++;
                        correct = true;
                    } else {
                        correct = false;
                    }
                    break;
                }
                case END: {
                    if (tokenList != null && position < tokenList.size() && tokenList.get(position).getTokenType() == XBTokenType.END) {
                        position++;
                        correct = true;
                    } else {
                        correct = false;
                    }
                    break;
                }
                default:
                    throw new UnsupportedOperationException();
            }
        }

        public boolean isCorrect() {
            return correct;
        }
    }

    private class DataAssertListener extends AssertListener {

        public DataAssertListener(List<XBToken> tokenList) {
            super(tokenList);
        }

        @Override
        public void putXBToken(XBToken token) throws XBProcessingException, IOException {
            if (token.getTokenType() == XBTokenType.DATA) {
                XBToken dataToken = super.tokenList.get(super.position);
                super.putXBToken(token);
                InputStream data = ((XBDataToken) token).getData();
                if (dataToken != null) {
                    byte[] dataBlob = new byte[2];
                    int position = 0;
                    while ((data.available() >= 0) && (position < 10)) {
                        int readStat = data.read(dataBlob, 0, 1);
                        if (readStat < 0) {
                            assertTrue("Incorrect extended data", false);
                        }

                        assertEquals('0' + position, dataBlob[0]);
                        position++;
                    }

                    assertTrue("Extended data are too short", position == 10);
                }
            } else {
                super.putXBToken(token);
            }
        }
    }
}
