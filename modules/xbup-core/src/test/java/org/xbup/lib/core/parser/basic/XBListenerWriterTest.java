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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import static junit.framework.Assert.assertEquals;
import junit.framework.TestCase;
import org.junit.Test;
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.XBProcessingExceptionType;
import org.xbup.lib.core.parser.basic.convert.XBPrintFilter;
import org.xbup.lib.core.parser.data.TestSampleData;
import static org.xbup.lib.core.test.TestUtils.assertEqualsInputStream;

/**
 * Test class for XBListenerWriter.
 *
 * @version 0.1.25 2015/07/08
 * @author XBUP Project (http://xbup.org)
 */
public class XBListenerWriterTest extends TestCase {

    public XBListenerWriterTest(String testName) {
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
     * Test of open method of the class XBListenerWriter.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWrite() throws Exception {
        ByteArrayOutputStream target = new ByteArrayOutputStream();
        try {
            try (XBListenerWriter writer = new XBListenerWriter()) {
                XBPrintFilter listener = new XBPrintFilter(writer);

                writer.open(target);
                TestSampleData.writeSampleSingleBlock(listener);
                writer.closeXB();
            }
        } catch (FileNotFoundException ex) {
            fail("File not found");
        } catch (IOException | XBProcessingException ex) {
            fail("Processing error: " + ex.getMessage());
        }
    }

    /**
     * Test of open method of the class XBListenerWriter.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleSingleEmptyBlock() throws Exception {
        try (InputStream stream = XBListenerWriterTest.class.getResourceAsStream("/org/xbup/lib/core/resources/test/samples/l0_singleemptyblock.xb")) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBListenerWriter writer = new XBListenerWriter(target)) {
                XBPrintFilter listener = new XBPrintFilter(writer);
                TestSampleData.writeSampleSingleEmptyBlock(listener);
                writer.closeXB();
            }

            assertEqualsInputStream(stream, new ByteArrayInputStream(target.toByteArray()));
        }
    }

    /**
     * Test of open method of the class XBListenerWriter.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleExtended() throws Exception {
        try (InputStream stream = XBListenerWriterTest.class.getResourceAsStream("/org/xbup/lib/core/resources/test/samples/l0_extended_new.xb")) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBListenerWriter writer = new XBListenerWriter(target)) {
                XBPrintFilter listener = new XBPrintFilter(writer);
                TestSampleData.writeSampleExtended(listener);
                writer.closeXB();
            }

            assertEqualsInputStream(stream, new ByteArrayInputStream(target.toByteArray()));
        }
    }

    /**
     * Test of open method of the class XBListenerWriter.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteNone() throws Exception {
        Throwable ex = null;

        ByteArrayOutputStream target = new ByteArrayOutputStream();
        try {
            try (XBListenerWriter writer = new XBListenerWriter(target)) {
                writer.closeXB();
            }
        } catch (XBProcessingException | IOException e) {
            ex = e;
        }

        assertEquals(new XBProcessingException("Unexpected end of stream", XBProcessingExceptionType.UNEXPECTED_END_OF_STREAM), ex);
    }

    /**
     * Test of open method of the class XBListenerWriter.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteUnfinishedBlock() throws Exception {
        Throwable ex = null;

        ByteArrayOutputStream target = new ByteArrayOutputStream();
        try {
            try (XBListenerWriter writer = new XBListenerWriter(target)) {
                XBPrintFilter listener = new XBPrintFilter(writer);
                listener.beginXB(XBBlockTerminationMode.SIZE_SPECIFIED);
                writer.closeXB();
            }
        } catch (XBProcessingException | IOException e) {
            ex = e;
        }

        assertEquals(new XBProcessingException("Unexpected end of stream", XBProcessingExceptionType.UNEXPECTED_END_OF_STREAM), ex);
    }

    /**
     * Test of open method of the class XBListenerWriter.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleSingleData() throws Exception {
        try (InputStream stream = XBListenerWriterTest.class.getResourceAsStream("/org/xbup/lib/core/resources/test/samples/l0_singledata.xb")) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBListenerWriter writer = new XBListenerWriter(target)) {
                XBPrintFilter listener = new XBPrintFilter(writer);
                TestSampleData.writeSampleSingleData(listener);
                writer.closeXB();
            }

            assertEqualsInputStream(stream, new ByteArrayInputStream(target.toByteArray()));
        }
    }

    /**
     * Test of open method of the class XBListenerWriter.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleTerminated() throws Exception {
        try (InputStream stream = XBListenerWriterTest.class.getResourceAsStream("/org/xbup/lib/core/resources/test/samples/l0_terminated.xb")) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBListenerWriter writer = new XBListenerWriter(target)) {
                XBPrintFilter listener = new XBPrintFilter(writer);
                TestSampleData.writeSampleTerminated(listener);
                writer.closeXB();
            }

            assertEqualsInputStream(stream, new ByteArrayInputStream(target.toByteArray()));
        }
    }

    /**
     * Test of open method of the class XBListenerWriter.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleSixBlocks() throws Exception {
        try (InputStream stream = XBListenerWriterTest.class.getResourceAsStream("/org/xbup/lib/core/resources/test/samples/l0_sixblocks.xb")) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBListenerWriter writer = new XBListenerWriter(target)) {
                XBPrintFilter listener = new XBPrintFilter(writer);
                TestSampleData.writeSampleSixBlocks(listener);
                writer.closeXB();
            }

            assertEqualsInputStream(stream, new ByteArrayInputStream(target.toByteArray()));
        }
    }
}
