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
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
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
 * Test class for XBProviderReader.
 *
 * @version 0.1.23 2013/12/15
 * @author XBUP Project (http://xbup.org)
 */
public class XBProviderReaderTest extends TestCase {

    public XBProviderReaderTest(String testName) {
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
     * Test of open method of the class XBProviderReader.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testRead() throws Exception {
        InputStream source = getClass().getResourceAsStream("/org/xbup/lib/core/resources/test/samples/xhtml-test.xb");
        try {
            XBProviderReader reader = new XBProviderReader();
            reader.open(source);
            while (!reader.isFinished()) {
                reader.produceXB(new DebugListener());
            }

            reader.close();
        } catch (FileNotFoundException ex) {
            fail("File not found");
        } catch (IOException | XBProcessingException ex) {
            fail("Processing error: " + ex.getMessage());
        }
    }

    /**
     * Test of open method of the class XBProviderReader.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleSingleEmptyBlock() throws Exception {
        InputStream stream = getClass().getResourceAsStream("/org/xbup/lib/core/resources/test/samples/l0_singleemptyblock.xb");
        XBProviderReader instance = new XBProviderReader(stream);
        AssertListener assertListener;

        assertListener = new AssertListener(new XBBeginToken(XBBlockTerminationMode.SIZE_SPECIFIED));
        instance.produceXB(assertListener);
        assertTrue(assertListener.isCorrect());

        assertListener = new AssertListener(new XBAttributeToken(new UBNat32(0)));
        instance.produceXB(assertListener);
        assertTrue(assertListener.isCorrect());

        assertListener = new AssertListener(new XBEndToken());
        instance.produceXB(assertListener);
        assertTrue(assertListener.isCorrect());

        instance.close();
    }

    /**
     * Test of open method of the class XBProviderReader.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleExtended() throws Exception {
        InputStream stream = getClass().getResourceAsStream("/org/xbup/lib/core/resources/test/samples/l0_extended.xb");
        XBProviderReader instance = new XBProviderReader(stream);
        AssertListener assertListener;

        assertListener = new AssertListener(new XBBeginToken(XBBlockTerminationMode.SIZE_SPECIFIED));
        instance.produceXB(assertListener);
        assertTrue(assertListener.isCorrect());

        assertListener = new AssertListener(new XBAttributeToken(new UBNat32(0)));
        instance.produceXB(assertListener);
        assertTrue(assertListener.isCorrect());

        assertListener = new DataAssertListener(new XBDataToken(null));
        instance.produceXB(assertListener);
        assertTrue(assertListener.isCorrect());

        assertListener = new AssertListener(new XBEndToken());
        instance.produceXB(assertListener);
        assertTrue(assertListener.isCorrect());

        instance.close();
    }

    /**
     * Test of open method of the class XBProviderReader.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleEmpty() throws Exception {
        Throwable ex = null;

        InputStream stream = getClass().getResourceAsStream("/org/xbup/lib/core/resources/test/samples/corrupted/l0_empty.xb");
        XBProviderReader instance = new XBProviderReader(stream);
        try {
            instance.produceXB(new AssertListener(null));
        } catch (XBProcessingException | IOException e) {
            ex = e;
        }

        assertEquals(new XBProcessingException("End of data reached", XBProcessingExceptionType.UNEXPECTED_END_OF_STREAM), ex);
    }

    /**
     * Test of open method of the class XBProviderReader.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleSingleByte() throws Exception {
        Throwable ex = null;

        InputStream stream = getClass().getResourceAsStream("/org/xbup/lib/core/resources/test/samples/corrupted/l0_singlebyte.xb");
        XBProviderReader instance = new XBProviderReader(stream);
        try {
            instance.produceXB(new AssertListener(null));
        } catch (XBProcessingException | IOException e) {
            ex = e;
        }

        assertEquals(new XBProcessingException("End of data reached", XBProcessingExceptionType.UNEXPECTED_END_OF_STREAM), ex);
    }

    /**
     * Test of open method of the class XBProviderReader.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleWrongHeader() throws Exception {
        Throwable ex = null;

        InputStream stream = getClass().getResourceAsStream("/org/xbup/lib/core/resources/test/samples/corrupted/l0_wrongheader.xb");
        XBProviderReader instance = new XBProviderReader(stream);
        try {
            instance.produceXB(new AssertListener(null));
        } catch (XBProcessingException | IOException e) {
            ex = e;
        }

        assertEquals(new XBParseException("Unsupported header: 0xfe0059420002", XBProcessingExceptionType.CORRUPTED_HEADER), ex);
    }

    /**
     * Test of open method of the class XBProviderReader.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleSingleData() throws Exception {
        InputStream stream = getClass().getResourceAsStream("/org/xbup/lib/core/resources/test/samples/l0_singledata.xb");
        XBProviderReader instance = new XBProviderReader(stream);
        AssertListener assertListener;

        assertListener = new AssertListener(new XBBeginToken(XBBlockTerminationMode.SIZE_SPECIFIED));
        instance.produceXB(assertListener);
        assertTrue(assertListener.isCorrect());

        assertListener = new DataAssertListener(new XBDataToken(null));
        instance.produceXB(assertListener);
        assertTrue(assertListener.isCorrect());

        assertListener = new AssertListener(new XBEndToken());
        instance.produceXB(assertListener);
        assertTrue(assertListener.isCorrect());

        instance.close();
    }

    /**
     * Test of open method of the class XBProviderReader.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleTerminated() throws Exception {
        InputStream stream = getClass().getResourceAsStream("/org/xbup/lib/core/resources/test/samples/l0_terminated.xb");
        XBProviderReader instance = new XBProviderReader(stream);
        AssertListener assertListener;

        assertListener = new AssertListener(new XBBeginToken(XBBlockTerminationMode.TERMINATED_BY_ZERO));
        instance.produceXB(assertListener);
        assertTrue(assertListener.isCorrect());

        assertListener = new AssertListener(new XBAttributeToken(new UBNat32(0)));
        instance.produceXB(assertListener);
        assertTrue(assertListener.isCorrect());

        assertListener = new AssertListener(new XBAttributeToken(new UBNat32(0)));
        instance.produceXB(assertListener);
        assertTrue(assertListener.isCorrect());

        assertListener = new AssertListener(new XBAttributeToken(new UBNat32(1)));
        instance.produceXB(assertListener);
        assertTrue(assertListener.isCorrect());

        assertListener = new AssertListener(new XBAttributeToken(new UBNat32(2)));
        instance.produceXB(assertListener);
        assertTrue(assertListener.isCorrect());

        assertListener = new AssertListener(new XBEndToken());
        instance.produceXB(assertListener);
        assertTrue(assertListener.isCorrect());

        instance.close();
    }

    /**
     * Test of open method of the class XBProviderReader.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleSixBlocks() throws Exception {
        InputStream stream = getClass().getResourceAsStream("/org/xbup/lib/core/resources/test/samples/l0_sixblocks.xb");
        XBProviderReader instance = new XBProviderReader(stream);
        AssertListener assertListener;

        assertListener = new AssertListener(new XBBeginToken(XBBlockTerminationMode.SIZE_SPECIFIED));
        instance.produceXB(assertListener);
        assertTrue(assertListener.isCorrect());

        assertListener = new AssertListener(new XBAttributeToken(new UBNat32(0)));
        instance.produceXB(assertListener);
        assertTrue(assertListener.isCorrect());

        assertListener = new AssertListener(new XBBeginToken(XBBlockTerminationMode.SIZE_SPECIFIED));
        instance.produceXB(assertListener);
        assertTrue(assertListener.isCorrect());

        assertListener = new AssertListener(new XBAttributeToken(new UBNat32(0)));
        instance.produceXB(assertListener);
        assertTrue(assertListener.isCorrect());

        assertListener = new AssertListener(new XBBeginToken(XBBlockTerminationMode.SIZE_SPECIFIED));
        instance.produceXB(assertListener);
        assertTrue(assertListener.isCorrect());

        assertListener = new AssertListener(new XBAttributeToken(new UBNat32(0)));
        instance.produceXB(assertListener);
        assertTrue(assertListener.isCorrect());

        assertListener = new AssertListener(new XBBeginToken(XBBlockTerminationMode.SIZE_SPECIFIED));
        instance.produceXB(assertListener);
        assertTrue(assertListener.isCorrect());

        assertListener = new AssertListener(new XBDataToken(null));
        instance.produceXB(assertListener);
        assertTrue(assertListener.isCorrect());

        assertListener = new AssertListener(new XBEndToken());
        instance.produceXB(assertListener);
        assertTrue(assertListener.isCorrect());

        assertListener = new AssertListener(new XBEndToken());
        instance.produceXB(assertListener);
        assertTrue(assertListener.isCorrect());

        assertListener = new AssertListener(new XBEndToken());
        instance.produceXB(assertListener);
        assertTrue(assertListener.isCorrect());

        assertListener = new AssertListener(new XBBeginToken(XBBlockTerminationMode.SIZE_SPECIFIED));
        instance.produceXB(assertListener);
        assertTrue(assertListener.isCorrect());

        assertListener = new AssertListener(new XBAttributeToken(new UBNat32(0)));
        instance.produceXB(assertListener);
        assertTrue(assertListener.isCorrect());

        assertListener = new AssertListener(new XBBeginToken(XBBlockTerminationMode.SIZE_SPECIFIED));
        instance.produceXB(assertListener);
        assertTrue(assertListener.isCorrect());

        assertListener = new AssertListener(new XBAttributeToken(new UBNat32(0)));
        instance.produceXB(assertListener);
        assertTrue(assertListener.isCorrect());

        assertListener = new AssertListener(new XBAttributeToken(new UBNat32(0)));
        instance.produceXB(assertListener);
        assertTrue(assertListener.isCorrect());

        assertListener = new AssertListener(new XBAttributeToken(new UBNat32(1)));
        instance.produceXB(assertListener);
        assertTrue(assertListener.isCorrect());

        assertListener = new AssertListener(new XBAttributeToken(new UBNat32(2)));
        instance.produceXB(assertListener);
        assertTrue(assertListener.isCorrect());

        assertListener = new AssertListener(new XBAttributeToken(new UBNat32(3)));
        instance.produceXB(assertListener);
        assertTrue(assertListener.isCorrect());

        assertListener = new AssertListener(new XBEndToken());
        instance.produceXB(assertListener);
        assertTrue(assertListener.isCorrect());

        assertListener = new AssertListener(new XBEndToken());
        instance.produceXB(assertListener);
        assertTrue(assertListener.isCorrect());

        assertListener = new AssertListener(new XBEndToken());
        instance.produceXB(assertListener);
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
        private boolean awaiting = true;
        private final XBToken token;

        public AssertListener(XBToken token) {
            this.token = token;
        }

        @Override
        public void beginXB(XBBlockTerminationMode terminationMode) throws XBProcessingException, IOException {
            super.beginXB(terminationMode);
            if (token != null && awaiting && token.getTokenType() == XBTokenType.BEGIN && ((XBBeginToken) token).getTerminationMode() == terminationMode) {
                awaiting = false;
                correct = true;
            } else {
                correct = false;
            }
        }

        @Override
        public void attribXB(XBAttribute attribute) throws XBProcessingException, IOException {
            super.attribXB(attribute);
            if (token != null && awaiting && token.getTokenType() == XBTokenType.ATTRIBUTE && ((XBAttributeToken) token).getAttribute().getNaturalLong() == attribute.getNaturalLong()) {
                awaiting = false;
                correct = true;
            } else {
                correct = false;
            }
        }

        @Override
        public void dataXB(InputStream data) throws XBProcessingException, IOException {
            super.dataXB(data);
            if (token != null && awaiting && token.getTokenType() == XBTokenType.DATA) {
                awaiting = false;
                correct = true;
            } else {
                correct = false;
            }
        }

        @Override
        public void endXB() throws XBProcessingException, IOException {
            super.endXB();
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
    }

    private class DataAssertListener extends AssertListener {

        public DataAssertListener(XBToken token) {
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
}
