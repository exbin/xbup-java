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
import static junit.framework.Assert.fail;
import junit.framework.TestCase;
import org.junit.Test;
import org.xbup.lib.core.parser.XBParseException;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.XBProcessingExceptionType;
import org.xbup.lib.core.parser.basic.XBListenerWriter;
import org.xbup.lib.core.parser.basic.convert.XBPrintFilter;
import org.xbup.lib.core.parser.data.XBCoreTestSampleData;
import org.xbup.lib.core.parser.token.event.convert.XBPrintEventFilter;
import static org.xbup.lib.core.test.XBTestUtils.assertEqualsInputStream;

/**
 * Test class for XBEventReader.
 *
 * @version 0.1.25 2015/07/13
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
        try (InputStream source = XBEventReaderTest.class.getResourceAsStream("/org/xbup/lib/core/resources/test/samples/xhtml-test.xb")) {
            try {
                XBEventReader reader = new XBEventReader();
                reader.open(source);
                reader.attachXBEventListener(new XBPrintEventFilter(null));
                reader.read();
                reader.close();
            } catch (FileNotFoundException ex) {
                fail("File not found");
            } catch (IOException | XBProcessingException ex) {
                fail("Processing error: " + ex.getMessage());
            }
        }
    }

    /**
     * Test of open method of the class XBEventReader.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleSingleEmptyBlock() throws Exception {
        try (InputStream stream = XBEventReaderTest.class.getResourceAsStream("/org/xbup/lib/core/resources/test/samples/l0_singleemptyblock.xb")) {
            XBEventReader instance = new XBEventReader(stream);
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBListenerWriter writer = new XBListenerWriter(target)) {
                XBPrintFilter listener = new XBPrintFilter(writer);
                XBCoreTestSampleData.writeSampleSingleEmptyBlock(listener);
                writer.closeXB();
            }

            assertEqualsInputStream(stream, new ByteArrayInputStream(target.toByteArray()));
            instance.close();
        }
    }

    /**
     * Test of open method of the class XBEventReader.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleExtended() throws Exception {
        try (InputStream stream = XBEventReaderTest.class.getResourceAsStream("/org/xbup/lib/core/resources/test/samples/l0_extended_new.xb")) {
            XBEventReader instance = new XBEventReader(stream);
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBListenerWriter writer = new XBListenerWriter(target)) {
                XBPrintFilter listener = new XBPrintFilter(writer);
                XBCoreTestSampleData.writeSampleExtended(listener);
                writer.closeXB();
            }

            assertEqualsInputStream(stream, new ByteArrayInputStream(target.toByteArray()));
            instance.close();
        }
    }

    /**
     * Test of open method of the class XBEventReader.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleEmpty() throws Exception {
        Throwable ex = null;

        try (InputStream stream = XBEventReaderTest.class.getResourceAsStream("/org/xbup/lib/core/resources/test/samples/corrupted/l0_empty.xb")) {
            XBEventReader instance = new XBEventReader(stream);
            try {
                instance.attachXBEventListener(new XBPrintEventFilter(null));
                instance.read();
            } catch (XBProcessingException | IOException e) {
                ex = e;
            }

            assertEquals(new XBProcessingException("End of data reached", XBProcessingExceptionType.UNEXPECTED_END_OF_STREAM), ex);
        }
    }

    /**
     * Test of open method of the class XBEventReader.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleSingleByte() throws Exception {
        Throwable ex = null;

        try (InputStream stream = XBEventReaderTest.class.getResourceAsStream("/org/xbup/lib/core/resources/test/samples/corrupted/l0_singlebyte.xb")) {
            XBEventReader instance = new XBEventReader(stream);
            try {
                instance.attachXBEventListener(new XBPrintEventFilter(null));
                instance.read();
            } catch (XBProcessingException | IOException e) {
                ex = e;
            }

            assertEquals(new XBProcessingException("End of data reached", XBProcessingExceptionType.UNEXPECTED_END_OF_STREAM), ex);
        }
    }

    /**
     * Test of open method of the class XBEventReader.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleWrongHeader() throws Exception {
        Throwable ex = null;

        try (InputStream stream = XBEventReaderTest.class.getResourceAsStream("/org/xbup/lib/core/resources/test/samples/corrupted/l0_wrongheader.xb")) {
            XBEventReader instance = new XBEventReader(stream);
            try {
                instance.attachXBEventListener(new XBPrintEventFilter(null));
                instance.read();
            } catch (XBProcessingException | IOException e) {
                ex = e;
            }

            assertEquals(new XBParseException("Unsupported header: 0xfe0059420002", XBProcessingExceptionType.CORRUPTED_HEADER), ex);
        }
    }

    /**
     * Test of open method of the class XBEventReader.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleSingleData() throws Exception {
        try (InputStream stream = XBEventReaderTest.class.getResourceAsStream("/org/xbup/lib/core/resources/test/samples/l0_singledata.xb")) {
            XBEventReader instance = new XBEventReader(stream);
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBListenerWriter writer = new XBListenerWriter(target)) {
                XBPrintFilter listener = new XBPrintFilter(writer);
                XBCoreTestSampleData.writeSampleSingleData(listener);
                writer.closeXB();
            }

            assertEqualsInputStream(stream, new ByteArrayInputStream(target.toByteArray()));
            instance.close();
        }
    }

    /**
     * Test of open method of the class XBEventReader.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleTerminated() throws Exception {
        try (InputStream stream = XBEventReaderTest.class.getResourceAsStream("/org/xbup/lib/core/resources/test/samples/l0_terminated.xb")) {
            XBEventReader instance = new XBEventReader(stream);
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBListenerWriter writer = new XBListenerWriter(target)) {
                XBPrintFilter listener = new XBPrintFilter(writer);
                XBCoreTestSampleData.writeSampleTerminated(listener);
                writer.closeXB();
            }

            assertEqualsInputStream(stream, new ByteArrayInputStream(target.toByteArray()));
            instance.close();
        }
    }

    /**
     * Test of open method of the class XBEventReader.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleSixBlocks() throws Exception {
        try (InputStream stream = XBEventReaderTest.class.getResourceAsStream("/org/xbup/lib/core/resources/test/samples/l0_sixblocks.xb")) {
            XBEventReader instance = new XBEventReader(stream);
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBListenerWriter writer = new XBListenerWriter(target)) {
                XBPrintFilter listener = new XBPrintFilter(writer);
                XBCoreTestSampleData.writeSampleSixBlocks(listener);
                writer.closeXB();
            }

            assertEqualsInputStream(stream, new ByteArrayInputStream(target.toByteArray()));
            instance.close();
        }
    }
}
