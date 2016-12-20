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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import static junit.framework.Assert.assertEquals;
import junit.framework.TestCase;
import org.exbin.xbup.core.block.XBBlockTerminationMode;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.parser.XBProcessingExceptionType;
import org.exbin.xbup.core.parser.basic.convert.XBConsumerToListener;
import org.exbin.xbup.core.parser.basic.convert.XBPrintFilter;
import org.exbin.xbup.core.parser.data.XBCoreTestSampleData;
import static org.exbin.xbup.core.test.XBTestUtils.assertEqualsInputStream;
import org.junit.Test;

/**
 * Test class for XBConsumerWriter.
 *
 * @version 0.1.25 2015/08/10
 * @author ExBin Project (http://exbin.org)
 */
public class XBConsumerWriterTest extends TestCase {

    public XBConsumerWriterTest(String testName) {
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
     * Tests XBConsumerWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleEmpty() throws Exception {
        try (InputStream stream = XBConsumerWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_EMPTY)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBConsumerWriter writer = new XBConsumerWriter(target)) {
                XBPrintFilter listener = new XBPrintFilter(new XBConsumerToListener(writer));
                XBCoreTestSampleData.writeSampleEmpty(listener);
                writer.write();
            }

            assertEqualsInputStream(stream, new ByteArrayInputStream(target.toByteArray()));
        }
    }

    /**
     * Tests XBConsumerWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleBlock() throws Exception {
        try (InputStream stream = XBConsumerWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_BLOCK)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBConsumerWriter writer = new XBConsumerWriter(target)) {
                XBPrintFilter listener = new XBPrintFilter(new XBConsumerToListener(writer));
                XBCoreTestSampleData.writeSampleBlock(listener);
                writer.write();
            }

            assertEqualsInputStream(stream, new ByteArrayInputStream(target.toByteArray()));
        }
    }

    /**
     * Tests XBConsumerWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleBlockWithTail() throws Exception {
        try (InputStream stream = XBConsumerWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_BLOCK_WITH_TAIL)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBConsumerWriter writer = new XBConsumerWriter(target)) {
                XBPrintFilter listener = new XBPrintFilter(new XBConsumerToListener(writer));
                XBCoreTestSampleData.writeSampleBlockWithTail(listener);
                writer.write();
            }

            assertEqualsInputStream(stream, new ByteArrayInputStream(target.toByteArray()));
        }
    }

    /**
     * Tests XBConsumerWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleBlockTerminated() throws Exception {
        try (InputStream stream = XBConsumerWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_BLOCK_TERMINATED)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBConsumerWriter writer = new XBConsumerWriter(target)) {
                XBPrintFilter listener = new XBPrintFilter(new XBConsumerToListener(writer));
                XBCoreTestSampleData.writeSampleBlockTerminated(listener);
                writer.write();
            }

            assertEqualsInputStream(stream, new ByteArrayInputStream(target.toByteArray()));
        }
    }

    /**
     * Tests XBConsumerWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleBlockTerminatedWithTail() throws Exception {
        try (InputStream stream = XBConsumerWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_BLOCK_TERMINATED_WITH_TAIL)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBConsumerWriter writer = new XBConsumerWriter(target)) {
                XBPrintFilter listener = new XBPrintFilter(new XBConsumerToListener(writer));
                XBCoreTestSampleData.writeSampleBlockTerminatedWithTail(listener);
                writer.write();
            }

            assertEqualsInputStream(stream, new ByteArrayInputStream(target.toByteArray()));
        }
    }

    /**
     * Tests XBConsumerWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleData() throws Exception {
        try (InputStream stream = XBConsumerWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_DATA)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBConsumerWriter writer = new XBConsumerWriter(target)) {
                XBPrintFilter listener = new XBPrintFilter(new XBConsumerToListener(writer));
                XBCoreTestSampleData.writeSampleData(listener);
                writer.write();
            }

            assertEqualsInputStream(stream, new ByteArrayInputStream(target.toByteArray()));
        }
    }

    /**
     * Tests XBConsumerWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleDataWithTail() throws Exception {
        try (InputStream stream = XBConsumerWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_DATA_WITH_TAIL)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBConsumerWriter writer = new XBConsumerWriter(target)) {
                XBPrintFilter listener = new XBPrintFilter(new XBConsumerToListener(writer));
                XBCoreTestSampleData.writeSampleDataWithTail(listener);
                writer.write();
            }

            assertEqualsInputStream(stream, new ByteArrayInputStream(target.toByteArray()));
        }
    }

    /**
     * Tests XBConsumerWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleDataTerminated() throws Exception {
        try (InputStream stream = XBConsumerWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_DATA_TERMINATED)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBConsumerWriter writer = new XBConsumerWriter(target)) {
                XBPrintFilter listener = new XBPrintFilter(new XBConsumerToListener(writer));
                XBCoreTestSampleData.writeSampleDataTerminated(listener);
                writer.write();
            }

            assertEqualsInputStream(stream, new ByteArrayInputStream(target.toByteArray()));
        }
    }

    /**
     * Tests XBConsumerWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleDataTerminatedWithTail() throws Exception {
        try (InputStream stream = XBConsumerWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_DATA_TERMINATED_WITH_TAIL)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBConsumerWriter writer = new XBConsumerWriter(target)) {
                XBPrintFilter listener = new XBPrintFilter(new XBConsumerToListener(writer));
                XBCoreTestSampleData.writeSampleDataTerminatedWithTail(listener);
                writer.write();
            }

            assertEqualsInputStream(stream, new ByteArrayInputStream(target.toByteArray()));
        }
    }

    /**
     * Tests XBConsumerWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleBlockData() throws Exception {
        try (InputStream stream = XBConsumerWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_BLOCK_DATA)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBConsumerWriter writer = new XBConsumerWriter(target)) {
                XBPrintFilter listener = new XBPrintFilter(new XBConsumerToListener(writer));
                XBCoreTestSampleData.writeSampleBlockData(listener);
                writer.write();
            }

            assertEqualsInputStream(stream, new ByteArrayInputStream(target.toByteArray()));
        }
    }

    /**
     * Tests XBConsumerWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleBlockDataWithTail() throws Exception {
        try (InputStream stream = XBConsumerWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_BLOCK_DATA_WITH_TAIL)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBConsumerWriter writer = new XBConsumerWriter(target)) {
                XBPrintFilter listener = new XBPrintFilter(new XBConsumerToListener(writer));
                XBCoreTestSampleData.writeSampleBlockDataWithTail(listener);
                writer.write();
            }

            assertEqualsInputStream(stream, new ByteArrayInputStream(target.toByteArray()));
        }
    }

    /**
     * Tests XBConsumerWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleBlockDataTerminated() throws Exception {
        try (InputStream stream = XBConsumerWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_BLOCK_DATA_TERMINATED)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBConsumerWriter writer = new XBConsumerWriter(target)) {
                XBPrintFilter listener = new XBPrintFilter(new XBConsumerToListener(writer));
                XBCoreTestSampleData.writeSampleBlockDataTerminated(listener);
                writer.write();
            }

            assertEqualsInputStream(stream, new ByteArrayInputStream(target.toByteArray()));
        }
    }

    /**
     * Tests XBConsumerWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleBlockDataTerminatedWithTail() throws Exception {
        try (InputStream stream = XBConsumerWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_BLOCK_DATA_TERMINATED_WITH_TAIL)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBConsumerWriter writer = new XBConsumerWriter(target)) {
                XBPrintFilter listener = new XBPrintFilter(new XBConsumerToListener(writer));
                XBCoreTestSampleData.writeSampleBlockDataTerminatedWithTail(listener);
                writer.write();
            }

            assertEqualsInputStream(stream, new ByteArrayInputStream(target.toByteArray()));
        }
    }

    /**
     * Tests XBConsumerWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleBlockDataHybrid() throws Exception {
        try (InputStream stream = XBConsumerWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_BLOCK_DATA_HYBRID)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBConsumerWriter writer = new XBConsumerWriter(target)) {
                XBPrintFilter listener = new XBPrintFilter(new XBConsumerToListener(writer));
                XBCoreTestSampleData.writeSampleBlockDataHybrid(listener);
                writer.write();
            }

            assertEqualsInputStream(stream, new ByteArrayInputStream(target.toByteArray()));
        }
    }

    /**
     * Tests XBConsumerWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleBlockDataHybrid2() throws Exception {
        try (InputStream stream = XBConsumerWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_BLOCK_DATA_HYBRID2)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBConsumerWriter writer = new XBConsumerWriter(target)) {
                XBPrintFilter listener = new XBPrintFilter(new XBConsumerToListener(writer));
                XBCoreTestSampleData.writeSampleBlockDataHybrid2(listener);
                writer.write();
            }

            assertEqualsInputStream(stream, new ByteArrayInputStream(target.toByteArray()));
        }
    }

    /**
     * Tests XBConsumerWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleTwoBlocks() throws Exception {
        try (InputStream stream = XBConsumerWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_TWO_BLOCKS)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBConsumerWriter writer = new XBConsumerWriter(target)) {
                XBPrintFilter listener = new XBPrintFilter(new XBConsumerToListener(writer));
                XBCoreTestSampleData.writeSampleTwoBlocks(listener);
                writer.write();
            }

            assertEqualsInputStream(stream, new ByteArrayInputStream(target.toByteArray()));
        }
    }

    /**
     * Tests XBConsumerWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleTwoBlocksWithTail() throws Exception {
        try (InputStream stream = XBConsumerWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_TWO_BLOCKS_WITH_TAIL)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBConsumerWriter writer = new XBConsumerWriter(target)) {
                XBPrintFilter listener = new XBPrintFilter(new XBConsumerToListener(writer));
                XBCoreTestSampleData.writeSampleTwoBlocksWithTail(listener);
                writer.write();
            }

            assertEqualsInputStream(stream, new ByteArrayInputStream(target.toByteArray()));
        }
    }

    /**
     * Tests XBConsumerWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleTwoBlocksTerminated() throws Exception {
        try (InputStream stream = XBConsumerWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_TWO_BLOCKS_TERMINATED)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBConsumerWriter writer = new XBConsumerWriter(target)) {
                XBPrintFilter listener = new XBPrintFilter(new XBConsumerToListener(writer));
                XBCoreTestSampleData.writeSampleTwoBlocksTerminated(listener);
                writer.write();
            }

            assertEqualsInputStream(stream, new ByteArrayInputStream(target.toByteArray()));
        }
    }

    /**
     * Tests XBConsumerWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleTwoBlocksTerminatedWithTail() throws Exception {
        try (InputStream stream = XBConsumerWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_TWO_BLOCKS_TERMINATED_WITH_TAIL)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBConsumerWriter writer = new XBConsumerWriter(target)) {
                XBPrintFilter listener = new XBPrintFilter(new XBConsumerToListener(writer));
                XBCoreTestSampleData.writeSampleTwoBlocksTerminatedWithTail(listener);
                writer.write();
            }

            assertEqualsInputStream(stream, new ByteArrayInputStream(target.toByteArray()));
        }
    }

    /**
     * Tests XBConsumerWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleTwoBlocksHybrid() throws Exception {
        try (InputStream stream = XBConsumerWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_TWO_BLOCKS_HYBRID)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBConsumerWriter writer = new XBConsumerWriter(target)) {
                XBPrintFilter listener = new XBPrintFilter(new XBConsumerToListener(writer));
                XBCoreTestSampleData.writeSampleTwoBlocksHybrid(listener);
                writer.write();
            }

            assertEqualsInputStream(stream, new ByteArrayInputStream(target.toByteArray()));
        }
    }

    /**
     * Tests XBConsumerWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleTwoBlocksHybrid2() throws Exception {
        try (InputStream stream = XBConsumerWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_TWO_BLOCKS_HYBRID2)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBConsumerWriter writer = new XBConsumerWriter(target)) {
                XBPrintFilter listener = new XBPrintFilter(new XBConsumerToListener(writer));
                XBCoreTestSampleData.writeSampleTwoBlocksHybrid2(listener);
                writer.write();
            }

            assertEqualsInputStream(stream, new ByteArrayInputStream(target.toByteArray()));
        }
    }

    /**
     * Tests XBConsumerWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleSixBlocks() throws Exception {
        try (InputStream stream = XBConsumerWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_SIX_BLOCKS)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBConsumerWriter writer = new XBConsumerWriter(target)) {
                XBPrintFilter listener = new XBPrintFilter(new XBConsumerToListener(writer));
                XBCoreTestSampleData.writeSampleSixBlocks(listener);
                writer.write();
            }

            assertEqualsInputStream(stream, new ByteArrayInputStream(target.toByteArray()));
        }
    }

    /**
     * Tests XBConsumerWriter class writing no data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteNone() throws Exception {
        Throwable ex = null;

        ByteArrayOutputStream target = new ByteArrayOutputStream();
        try {
            try (XBConsumerWriter writer = new XBConsumerWriter(target)) {
                assertNotNull(new XBPrintFilter(new XBConsumerToListener(writer)));
                writer.write();
            }
        } catch (XBProcessingException | IOException e) {
            ex = e;
        }

        assertEquals(new XBProcessingException("End of data reached", XBProcessingExceptionType.UNEXPECTED_END_OF_STREAM), ex);
    }

    /**
     * Tests XBConsumerWriter class writing unfinished block.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteUnfinishedBlock() throws Exception {
        Throwable ex = null;

        ByteArrayOutputStream target = new ByteArrayOutputStream();
        try {
            try (XBConsumerWriter writer = new XBConsumerWriter(target)) {
                XBPrintFilter listener = new XBPrintFilter(new XBConsumerToListener(writer));
                listener.beginXB(XBBlockTerminationMode.SIZE_SPECIFIED);
                writer.write();
            }
        } catch (XBProcessingException | IOException e) {
            ex = e;
        }

        assertEquals(new XBProcessingException("End of data reached", XBProcessingExceptionType.UNEXPECTED_END_OF_STREAM), ex);
    }

    /**
     * Tests XBConsumerWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteCorruptedEndWithoutData() throws Exception {
        Throwable ex = null;

        ByteArrayOutputStream target = new ByteArrayOutputStream();
        try (XBConsumerWriter writer = new XBConsumerWriter(target)) {
            XBPrintFilter listener = new XBPrintFilter(new XBConsumerToListener(writer));
            XBCoreTestSampleData.writeCorruptedEndWithoutData(listener);
            writer.write();
        } catch (XBProcessingException | IOException e) {
            ex = e;
        }

        assertEquals(new XBProcessingException("Missing at least one attribute", XBProcessingExceptionType.UNEXPECTED_ORDER), ex);
    }

    /**
     * Tests XBConsumerWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteCorruptedDataAttributeMismatch() throws Exception {
        Throwable ex = null;

        ByteArrayOutputStream target = new ByteArrayOutputStream();
        try (XBConsumerWriter writer = new XBConsumerWriter(target)) {
            XBPrintFilter listener = new XBPrintFilter(new XBConsumerToListener(writer));
            XBCoreTestSampleData.writeCorruptedDataAttributeMismatch(listener);
            writer.write();
        } catch (XBProcessingException | IOException e) {
            ex = e;
        }

        assertEquals(new XBProcessingException("Data block must be followed by block end", XBProcessingExceptionType.UNEXPECTED_ORDER), ex);
    }

    /**
     * Tests XBConsumerWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteCorruptedDataAttributeMismatch2() throws Exception {
        Throwable ex = null;

        ByteArrayOutputStream target = new ByteArrayOutputStream();
        try (XBConsumerWriter writer = new XBConsumerWriter(target)) {
            XBPrintFilter listener = new XBPrintFilter(new XBConsumerToListener(writer));
            XBCoreTestSampleData.writeCorruptedDataAttributeMismatch2(listener);
            writer.write();
        } catch (XBProcessingException | IOException e) {
            ex = e;
        }

        assertEquals(new XBProcessingException("Unexpected token order", XBProcessingExceptionType.UNEXPECTED_ORDER), ex);
    }

    /**
     * Tests XBConsumerWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteCorruptedAttributeAfterEnd() throws Exception {
        Throwable ex = null;

        ByteArrayOutputStream target = new ByteArrayOutputStream();
        try (XBConsumerWriter writer = new XBConsumerWriter(target)) {
            XBPrintFilter listener = new XBPrintFilter(new XBConsumerToListener(writer));
            XBCoreTestSampleData.writeCorruptedAttributeAfterEnd(listener);
            writer.write();
        } catch (XBProcessingException | IOException e) {
            ex = e;
        }

        assertEquals(new XBProcessingException("Missing at least one attribute", XBProcessingExceptionType.UNEXPECTED_ORDER), ex);
    }
}
