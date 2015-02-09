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
package org.xbup.lib.core.parser.basic;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import junit.framework.TestCase;
import org.junit.Test;
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.parser.XBParseException;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.XBProcessingExceptionType;
import org.xbup.lib.core.parser.token.XBAttribute;
import org.xbup.lib.core.parser.token.XBAttributeToken;
import org.xbup.lib.core.parser.token.XBBeginToken;
import org.xbup.lib.core.parser.token.XBDataToken;
import org.xbup.lib.core.parser.token.XBEndToken;
import org.xbup.lib.core.parser.token.XBToken;
import org.xbup.lib.core.parser.token.XBTokenType;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * Test class for XBProducerReader.
 *
 * @version 0.1.23 2013/12/15
 * @author XBUP Project (http://xbup.org)
 */
public class XBProducerReaderTest extends TestCase {

    public XBProducerReaderTest(String testName) {
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
     * Test of open method of the class XBProducerReader.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testRead() throws Exception {
        InputStream source = getClass().getResourceAsStream("/org/xbup/lib/core/resources/test/samples/xhtml-test.xb");
        try {
            XBProducerReader reader = new XBProducerReader();
            reader.open(source);
            reader.attachXBListener(new DebugListener());
            reader.read();

            reader.close();
        } catch (FileNotFoundException ex) {
            fail("File not found");
        } catch (IOException | XBProcessingException ex) {
            fail("Processing error: " + ex.getMessage());
        }
    }

    /**
     * Test of open method of the class XBProducerReader.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleSingleEmptyBlock() throws Exception {
        InputStream stream = getClass().getResourceAsStream("/org/xbup/lib/core/resources/test/samples/l0_singleemptyblock.xb");
        XBProducerReader instance = new XBProducerReader(stream);
        AssertListener assertListener;

        List<XBToken> tokenList = new ArrayList<>();
        tokenList.add(new XBBeginToken(XBBlockTerminationMode.SIZE_SPECIFIED));
        tokenList.add(new XBAttributeToken(new UBNat32(0)));
        tokenList.add(new XBEndToken());
        tokenList.add(null);

        assertListener = new AssertListener(tokenList);
        instance.attachXBListener(assertListener);
        instance.read();
        assertTrue(assertListener.isCorrect());

        instance.close();
    }

    /**
     * Test of open method of the class XBProducerReader.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleExtended() throws Exception {
        InputStream stream = getClass().getResourceAsStream("/org/xbup/lib/core/resources/test/samples/l0_extended.xb");
        XBProducerReader instance = new XBProducerReader(stream);
        AssertListener assertListener;

        List<XBToken> tokenList = new ArrayList<>();
        tokenList.add(new XBBeginToken(XBBlockTerminationMode.SIZE_SPECIFIED));
        tokenList.add(new XBAttributeToken(new UBNat32(0)));
        tokenList.add(new XBDataToken(null));
        tokenList.add(new XBEndToken());

        assertListener = new DataAssertListener(tokenList);
        instance.attachXBListener(assertListener);
        instance.read();
        assertTrue(assertListener.isCorrect());

        instance.close();
    }

    /**
     * Test of open method of the class XBProducerReader.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleEmpty() throws Exception {
        Throwable ex = null;

        InputStream stream = getClass().getResourceAsStream("/org/xbup/lib/core/resources/test/samples/corrupted/l0_empty.xb");
        XBProducerReader instance = new XBProducerReader(stream);
        try {
            instance.attachXBListener(new AssertListener(null));
            instance.read();
        } catch (XBProcessingException | IOException e) {
            ex = e;
        }

        assertEquals(new XBProcessingException("End of data reached", XBProcessingExceptionType.UNEXPECTED_END_OF_STREAM), ex);
    }

    /**
     * Test of open method of the class XBProducerReader.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleSingleByte() throws Exception {
        Throwable ex = null;

        InputStream stream = getClass().getResourceAsStream("/org/xbup/lib/core/resources/test/samples/corrupted/l0_singlebyte.xb");
        XBProducerReader instance = new XBProducerReader(stream);
        try {
            instance.attachXBListener(new AssertListener(null));
            instance.read();
        } catch (XBProcessingException | IOException e) {
            ex = e;
        }

        assertEquals(new XBProcessingException("End of data reached", XBProcessingExceptionType.UNEXPECTED_END_OF_STREAM), ex);
    }

    /**
     * Test of open method of the class XBProducerReader.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleWrongHeader() throws Exception {
        Throwable ex = null;

        InputStream stream = getClass().getResourceAsStream("/org/xbup/lib/core/resources/test/samples/corrupted/l0_wrongheader.xb");
        XBProducerReader instance = new XBProducerReader(stream);
        try {
            instance.attachXBListener(new AssertListener(null));
            instance.read();
        } catch (XBProcessingException | IOException e) {
            ex = e;
        }

        assertEquals(new XBParseException("Unsupported header: 0xfe0059420002", XBProcessingExceptionType.CORRUPTED_HEADER), ex);
    }

    /**
     * Test of open method of the class XBProducerReader.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleSingleData() throws Exception {
        InputStream stream = getClass().getResourceAsStream("/org/xbup/lib/core/resources/test/samples/l0_singledata.xb");
        XBProducerReader instance = new XBProducerReader(stream);
        AssertListener assertListener;

        List<XBToken> tokenList = new ArrayList<>();
        tokenList.add(new XBBeginToken(XBBlockTerminationMode.SIZE_SPECIFIED));
        tokenList.add(new XBDataToken(null));
        tokenList.add(new XBEndToken());
        tokenList.add(null);

        assertListener = new DataAssertListener(tokenList);
        instance.attachXBListener(assertListener);
        instance.read();
        assertTrue(assertListener.isCorrect());

        instance.close();
    }

    /**
     * Test of open method of the class XBProducerReader.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleTerminated() throws Exception {
        InputStream stream = getClass().getResourceAsStream("/org/xbup/lib/core/resources/test/samples/l0_terminated.xb");
        XBProducerReader instance = new XBProducerReader(stream);
        AssertListener assertListener;

        List<XBToken> tokenList = new ArrayList<>();
        tokenList.add(new XBBeginToken(XBBlockTerminationMode.TERMINATED_BY_ZERO));
        tokenList.add(new XBAttributeToken(new UBNat32(0)));
        tokenList.add(new XBAttributeToken(new UBNat32(0)));
        tokenList.add(new XBAttributeToken(new UBNat32(1)));
        tokenList.add(new XBAttributeToken(new UBNat32(2)));
        tokenList.add(new XBEndToken());
        tokenList.add(null);

        assertListener = new AssertListener(tokenList);
        instance.attachXBListener(assertListener);
        instance.read();
        assertTrue(assertListener.isCorrect());

        instance.close();
    }

    /**
     * Test of open method of the class XBProducerReader.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleSixBlocks() throws Exception {
        InputStream stream = getClass().getResourceAsStream("/org/xbup/lib/core/resources/test/samples/l0_sixblocks.xb");
        XBProducerReader instance = new XBProducerReader(stream);
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
        instance.attachXBListener(assertListener);
        instance.read();
        assertTrue(assertListener.isCorrect());

        instance.close();
    }

    private class DebugListener implements XBListener {

        @Override
        public void beginXB(XBBlockTerminationMode terminationMode) throws XBProcessingException, IOException {
            System.out.println("> Begin (" + terminationMode.toString() + "):");
        }

        @Override
        public void attribXB(XBAttribute attribute) throws XBProcessingException, IOException {
            System.out.println("  Attribute: " + attribute.getNaturalLong());
        }

        @Override
        public void dataXB(InputStream data) throws XBProcessingException, IOException {
            System.out.println("  Data:" + data.available());
        }

        @Override
        public void endXB() throws XBProcessingException, IOException {
            System.out.println("< End.");
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
        public void beginXB(XBBlockTerminationMode terminationMode) throws XBProcessingException, IOException {
            super.beginXB(terminationMode);
            if (tokenList != null && position < tokenList.size() && tokenList.get(position).getTokenType() == XBTokenType.BEGIN && ((XBBeginToken) tokenList.get(position)).getTerminationMode() == terminationMode) {
                position++;
                correct = true;
            } else {
                correct = false;
            }
        }

        @Override
        public void attribXB(XBAttribute attribute) throws XBProcessingException, IOException {
            super.attribXB(attribute);
            if (tokenList != null && position < tokenList.size() && tokenList.get(position).getTokenType() == XBTokenType.ATTRIBUTE && ((XBAttributeToken) tokenList.get(position)).getAttribute().getNaturalLong() == attribute.getNaturalLong()) {
                position++;
                correct = true;
            } else {
                correct = false;
            }
        }

        @Override
        public void dataXB(InputStream data) throws XBProcessingException, IOException {
            super.dataXB(data);
            if (tokenList != null && position < tokenList.size() && (tokenList.get(position) == null || tokenList.get(position).getTokenType() == XBTokenType.DATA)) {
                position++;
                correct = true;
            } else {
                correct = false;
            }
        }

        @Override
        public void endXB() throws XBProcessingException, IOException {
            super.endXB();
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
    }

    private class DataAssertListener extends AssertListener {

        public DataAssertListener(List<XBToken> tokenList) {
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
