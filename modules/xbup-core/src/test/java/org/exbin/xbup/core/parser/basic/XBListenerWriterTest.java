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
package org.exbin.xbup.core.parser.basic;

import org.exbin.xbup.core.parser.basic.XBListenerWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import static junit.framework.Assert.assertEquals;
import junit.framework.TestCase;
import org.junit.Test;
import org.exbin.xbup.core.block.XBBlockTerminationMode;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.parser.XBProcessingExceptionType;
import org.exbin.xbup.core.parser.basic.convert.XBPrintFilter;
import org.exbin.xbup.core.parser.data.XBCoreTestSampleData;
import static org.exbin.xbup.core.test.XBTestUtils.assertEqualsInputStream;

/**
 * Test class for XBListenerWriter.
 *
 * @version 0.1.25 2015/08/10
 * @author ExBin Project (http://exbin.org)
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
     * Tests XBListenerWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleEmpty() throws Exception {
        try (InputStream stream = XBListenerWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_EMPTY)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBListenerWriter writer = new XBListenerWriter(target)) {
                XBPrintFilter listener = new XBPrintFilter(writer);
                XBCoreTestSampleData.writeSampleEmpty(listener);
                writer.closeXB();
            }

            assertEqualsInputStream(new ByteArrayInputStream(target.toByteArray()), stream);
        }
    }

    /**
     * Tests XBListenerWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleBlock() throws Exception {
        try (InputStream stream = XBListenerWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_BLOCK)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBListenerWriter writer = new XBListenerWriter(target)) {
                XBPrintFilter listener = new XBPrintFilter(writer);
                XBCoreTestSampleData.writeSampleBlock(listener);
                writer.closeXB();
            }

            assertEqualsInputStream(new ByteArrayInputStream(target.toByteArray()), stream);
        }
    }

    /**
     * Tests XBListenerWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleBlockExtended() throws Exception {
        try (InputStream stream = XBListenerWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_BLOCK_EXTENDED)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBListenerWriter writer = new XBListenerWriter(target)) {
                XBPrintFilter listener = new XBPrintFilter(writer);
                XBCoreTestSampleData.writeSampleBlockExtended(listener);
                writer.closeXB();
            }

            assertEqualsInputStream(new ByteArrayInputStream(target.toByteArray()), stream);
        }
    }

    /**
     * Tests XBListenerWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleBlockTerminated() throws Exception {
        try (InputStream stream = XBListenerWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_BLOCK_TERMINATED)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBListenerWriter writer = new XBListenerWriter(target)) {
                XBPrintFilter listener = new XBPrintFilter(writer);
                XBCoreTestSampleData.writeSampleBlockTerminated(listener);
                writer.closeXB();
            }

            assertEqualsInputStream(new ByteArrayInputStream(target.toByteArray()), stream);
        }
    }

    /**
     * Tests XBListenerWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleBlockTerminatedExtended() throws Exception {
        try (InputStream stream = XBListenerWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_BLOCK_TERMINATED_EXTENDED)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBListenerWriter writer = new XBListenerWriter(target)) {
                XBPrintFilter listener = new XBPrintFilter(writer);
                XBCoreTestSampleData.writeSampleBlockTerminatedExtended(listener);
                writer.closeXB();
            }

            assertEqualsInputStream(new ByteArrayInputStream(target.toByteArray()), stream);
        }
    }

    /**
     * Tests XBListenerWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleData() throws Exception {
        try (InputStream stream = XBListenerWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_DATA)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBListenerWriter writer = new XBListenerWriter(target)) {
                XBPrintFilter listener = new XBPrintFilter(writer);
                XBCoreTestSampleData.writeSampleData(listener);
                writer.closeXB();
            }

            assertEqualsInputStream(new ByteArrayInputStream(target.toByteArray()), stream);
        }
    }

    /**
     * Tests XBListenerWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleDataExtended() throws Exception {
        try (InputStream stream = XBListenerWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_DATA_EXTENDED)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBListenerWriter writer = new XBListenerWriter(target)) {
                XBPrintFilter listener = new XBPrintFilter(writer);
                XBCoreTestSampleData.writeSampleDataExtended(listener);
                writer.closeXB();
            }

            assertEqualsInputStream(new ByteArrayInputStream(target.toByteArray()), stream);
        }
    }

    /**
     * Tests XBListenerWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleDataTerminated() throws Exception {
        try (InputStream stream = XBListenerWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_DATA_TERMINATED)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBListenerWriter writer = new XBListenerWriter(target)) {
                XBPrintFilter listener = new XBPrintFilter(writer);
                XBCoreTestSampleData.writeSampleDataTerminated(listener);
                writer.closeXB();
            }

            assertEqualsInputStream(new ByteArrayInputStream(target.toByteArray()), stream);
        }
    }

    /**
     * Tests XBListenerWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleDataTerminatedExtended() throws Exception {
        try (InputStream stream = XBListenerWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_DATA_TERMINATED_EXTENDED)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBListenerWriter writer = new XBListenerWriter(target)) {
                XBPrintFilter listener = new XBPrintFilter(writer);
                XBCoreTestSampleData.writeSampleDataTerminatedExtended(listener);
                writer.closeXB();
            }

            assertEqualsInputStream(new ByteArrayInputStream(target.toByteArray()), stream);
        }
    }

    /**
     * Tests XBListenerWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleBlockData() throws Exception {
        try (InputStream stream = XBListenerWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_BLOCK_DATA)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBListenerWriter writer = new XBListenerWriter(target)) {
                XBPrintFilter listener = new XBPrintFilter(writer);
                XBCoreTestSampleData.writeSampleBlockData(listener);
                writer.closeXB();
            }

            assertEqualsInputStream(new ByteArrayInputStream(target.toByteArray()), stream);
        }
    }

    /**
     * Tests XBListenerWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleBlockDataExtended() throws Exception {
        try (InputStream stream = XBListenerWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_BLOCK_DATA_EXTENDED)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBListenerWriter writer = new XBListenerWriter(target)) {
                XBPrintFilter listener = new XBPrintFilter(writer);
                XBCoreTestSampleData.writeSampleBlockDataExtended(listener);
                writer.closeXB();
            }

            assertEqualsInputStream(new ByteArrayInputStream(target.toByteArray()), stream);
        }
    }

    /**
     * Tests XBListenerWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleBlockDataTerminated() throws Exception {
        try (InputStream stream = XBListenerWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_BLOCK_DATA_TERMINATED)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBListenerWriter writer = new XBListenerWriter(target)) {
                XBPrintFilter listener = new XBPrintFilter(writer);
                XBCoreTestSampleData.writeSampleBlockDataTerminated(listener);
                writer.closeXB();
            }

            assertEqualsInputStream(new ByteArrayInputStream(target.toByteArray()), stream);
        }
    }

    /**
     * Tests XBListenerWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleBlockDataTerminatedExtended() throws Exception {
        try (InputStream stream = XBListenerWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_BLOCK_DATA_TERMINATED_EXTENDED)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBListenerWriter writer = new XBListenerWriter(target)) {
                XBPrintFilter listener = new XBPrintFilter(writer);
                XBCoreTestSampleData.writeSampleBlockDataTerminatedExtended(listener);
                writer.closeXB();
            }

            assertEqualsInputStream(new ByteArrayInputStream(target.toByteArray()), stream);
        }
    }

    /**
     * Tests XBListenerWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleBlockDataHybrid() throws Exception {
        try (InputStream stream = XBListenerWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_BLOCK_DATA_HYBRID)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBListenerWriter writer = new XBListenerWriter(target)) {
                XBPrintFilter listener = new XBPrintFilter(writer);
                XBCoreTestSampleData.writeSampleBlockDataHybrid(listener);
                writer.closeXB();
            }

            assertEqualsInputStream(new ByteArrayInputStream(target.toByteArray()), stream);
        }
    }

    /**
     * Tests XBListenerWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleBlockDataHybrid2() throws Exception {
        try (InputStream stream = XBListenerWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_BLOCK_DATA_HYBRID2)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBListenerWriter writer = new XBListenerWriter(target)) {
                XBPrintFilter listener = new XBPrintFilter(writer);
                XBCoreTestSampleData.writeSampleBlockDataHybrid2(listener);
                writer.closeXB();
            }

            assertEqualsInputStream(new ByteArrayInputStream(target.toByteArray()), stream);
        }
    }

    /**
     * Tests XBListenerWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleTwoBlocks() throws Exception {
        try (InputStream stream = XBListenerWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_TWO_BLOCKS)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBListenerWriter writer = new XBListenerWriter(target)) {
                XBPrintFilter listener = new XBPrintFilter(writer);
                XBCoreTestSampleData.writeSampleTwoBlocks(listener);
                writer.closeXB();
            }

            assertEqualsInputStream(new ByteArrayInputStream(target.toByteArray()), stream);
        }
    }

    /**
     * Tests XBListenerWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleTwoBlocksExtended() throws Exception {
        try (InputStream stream = XBListenerWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_TWO_BLOCKS_EXTENDED)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBListenerWriter writer = new XBListenerWriter(target)) {
                XBPrintFilter listener = new XBPrintFilter(writer);
                XBCoreTestSampleData.writeSampleTwoBlocksExtended(listener);
                writer.closeXB();
            }

            assertEqualsInputStream(new ByteArrayInputStream(target.toByteArray()), stream);
        }
    }

    /**
     * Tests XBListenerWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleTwoBlocksTerminated() throws Exception {
        try (InputStream stream = XBListenerWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_TWO_BLOCKS_TERMINATED)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBListenerWriter writer = new XBListenerWriter(target)) {
                XBPrintFilter listener = new XBPrintFilter(writer);
                XBCoreTestSampleData.writeSampleTwoBlocksTerminated(listener);
                writer.closeXB();
            }

            assertEqualsInputStream(new ByteArrayInputStream(target.toByteArray()), stream);
        }
    }

    /**
     * Tests XBListenerWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleTwoBlocksTerminatedExtended() throws Exception {
        try (InputStream stream = XBListenerWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_TWO_BLOCKS_TERMINATED_EXTENDED)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBListenerWriter writer = new XBListenerWriter(target)) {
                XBPrintFilter listener = new XBPrintFilter(writer);
                XBCoreTestSampleData.writeSampleTwoBlocksTerminatedExtended(listener);
                writer.closeXB();
            }

            assertEqualsInputStream(new ByteArrayInputStream(target.toByteArray()), stream);
        }
    }

    /**
     * Tests XBListenerWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleTwoBlocksHybrid() throws Exception {
        try (InputStream stream = XBListenerWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_TWO_BLOCKS_HYBRID)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBListenerWriter writer = new XBListenerWriter(target)) {
                XBPrintFilter listener = new XBPrintFilter(writer);
                XBCoreTestSampleData.writeSampleTwoBlocksHybrid(listener);
                writer.closeXB();
            }

            assertEqualsInputStream(new ByteArrayInputStream(target.toByteArray()), stream);
        }
    }

    /**
     * Tests XBListenerWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleTwoBlocksHybrid2() throws Exception {
        try (InputStream stream = XBListenerWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_TWO_BLOCKS_HYBRID2)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBListenerWriter writer = new XBListenerWriter(target)) {
                XBPrintFilter listener = new XBPrintFilter(writer);
                XBCoreTestSampleData.writeSampleTwoBlocksHybrid2(listener);
                writer.closeXB();
            }

            assertEqualsInputStream(new ByteArrayInputStream(target.toByteArray()), stream);
        }
    }

    /**
     * Tests XBListenerWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleSixBlocks() throws Exception {
        try (InputStream stream = XBListenerWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_SIX_BLOCKS)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBListenerWriter writer = new XBListenerWriter(target)) {
                XBPrintFilter listener = new XBPrintFilter(writer);
                XBCoreTestSampleData.writeSampleSixBlocks(listener);
                writer.closeXB();
            }

            assertEqualsInputStream(new ByteArrayInputStream(target.toByteArray()), stream);
        }
    }

    /**
     * Tests XBListenerWriter class writing no data.
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
     * Tests XBListenerWriter class writing unfinished block.
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
     * Tests XBListenerWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteCorruptedEndWithoutData() throws Exception {
        Throwable ex = null;

        ByteArrayOutputStream target = new ByteArrayOutputStream();
        try (XBListenerWriter writer = new XBListenerWriter(target)) {
            XBPrintFilter listener = new XBPrintFilter(writer);
            XBCoreTestSampleData.writeCorruptedEndWithoutData(listener);
        } catch (XBProcessingException | IOException e) {
            ex = e;
        }

        assertEquals(new XBProcessingException("Unexpected end token", XBProcessingExceptionType.UNEXPECTED_ORDER), ex);
    }

    /**
     * Tests XBListenerWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteCorruptedDataAttributeMismatch() throws Exception {
        Throwable ex = null;

        ByteArrayOutputStream target = new ByteArrayOutputStream();
        try (XBListenerWriter writer = new XBListenerWriter(target)) {
            XBPrintFilter listener = new XBPrintFilter(writer);
            XBCoreTestSampleData.writeCorruptedDataAttributeMismatch(listener);
        } catch (XBProcessingException | IOException e) {
            ex = e;
        }

        assertEquals(new XBProcessingException("Unexpected attribute", XBProcessingExceptionType.UNEXPECTED_ORDER), ex);
    }

    /**
     * Tests XBListenerWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteCorruptedDataAttributeMismatch2() throws Exception {
        Throwable ex = null;

        ByteArrayOutputStream target = new ByteArrayOutputStream();
        try (XBListenerWriter writer = new XBListenerWriter(target)) {
            XBPrintFilter listener = new XBPrintFilter(writer);
            XBCoreTestSampleData.writeCorruptedDataAttributeMismatch2(listener);
        } catch (XBProcessingException | IOException e) {
            ex = e;
        }

        assertEquals(new XBProcessingException("Unexpected data token", XBProcessingExceptionType.UNEXPECTED_ORDER), ex);
    }

    /**
     * Tests XBListenerWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteCorruptedAttributeAfterEnd() throws Exception {
        Throwable ex = null;

        ByteArrayOutputStream target = new ByteArrayOutputStream();
        try (XBListenerWriter writer = new XBListenerWriter(target)) {
            XBPrintFilter listener = new XBPrintFilter(writer);
            XBCoreTestSampleData.writeCorruptedAttributeAfterEnd(listener);
        } catch (XBProcessingException | IOException e) {
            ex = e;
        }

        assertEquals(new XBProcessingException("Unexpected attribute", XBProcessingExceptionType.UNEXPECTED_ORDER), ex);
    }
}
