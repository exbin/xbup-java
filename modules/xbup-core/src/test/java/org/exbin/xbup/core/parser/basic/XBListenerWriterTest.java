/*
 * Copyright (C) ExBin Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.exbin.xbup.core.parser.basic;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import junit.framework.TestCase;
import org.exbin.xbup.core.block.XBBlockTerminationMode;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.parser.XBProcessingExceptionType;
import org.exbin.xbup.core.parser.basic.convert.XBPrintFilter;
import org.exbin.xbup.core.parser.data.XBCoreTestSampleData;
import org.exbin.xbup.core.test.XBTestUtils;
import org.junit.Test;

/**
 * Test class for XBListenerWriter.
 *
 * @author ExBin Project (https://exbin.org)
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

            XBTestUtils.assertEqualsInputStream(new ByteArrayInputStream(target.toByteArray()), stream);
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

            XBTestUtils.assertEqualsInputStream(new ByteArrayInputStream(target.toByteArray()), stream);
        }
    }

    /**
     * Tests XBListenerWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleBlockWithTail() throws Exception {
        try (InputStream stream = XBListenerWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_BLOCK_WITH_TAIL)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBListenerWriter writer = new XBListenerWriter(target)) {
                XBPrintFilter listener = new XBPrintFilter(writer);
                XBCoreTestSampleData.writeSampleBlockWithTail(listener);
                writer.closeXB();
            }

            XBTestUtils.assertEqualsInputStream(new ByteArrayInputStream(target.toByteArray()), stream);
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

            XBTestUtils.assertEqualsInputStream(new ByteArrayInputStream(target.toByteArray()), stream);
        }
    }

    /**
     * Tests XBListenerWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleBlockTerminatedWithTail() throws Exception {
        try (InputStream stream = XBListenerWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_BLOCK_TERMINATED_WITH_TAIL)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBListenerWriter writer = new XBListenerWriter(target)) {
                XBPrintFilter listener = new XBPrintFilter(writer);
                XBCoreTestSampleData.writeSampleBlockTerminatedWithTail(listener);
                writer.closeXB();
            }

            XBTestUtils.assertEqualsInputStream(new ByteArrayInputStream(target.toByteArray()), stream);
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

            XBTestUtils.assertEqualsInputStream(new ByteArrayInputStream(target.toByteArray()), stream);
        }
    }

    /**
     * Tests XBListenerWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleDataWithTail() throws Exception {
        try (InputStream stream = XBListenerWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_DATA_WITH_TAIL)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBListenerWriter writer = new XBListenerWriter(target)) {
                XBPrintFilter listener = new XBPrintFilter(writer);
                XBCoreTestSampleData.writeSampleDataWithTail(listener);
                writer.closeXB();
            }

            XBTestUtils.assertEqualsInputStream(new ByteArrayInputStream(target.toByteArray()), stream);
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

            XBTestUtils.assertEqualsInputStream(new ByteArrayInputStream(target.toByteArray()), stream);
        }
    }

    /**
     * Tests XBListenerWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleDataTerminatedWithTail() throws Exception {
        try (InputStream stream = XBListenerWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_DATA_TERMINATED_WITH_TAIL)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBListenerWriter writer = new XBListenerWriter(target)) {
                XBPrintFilter listener = new XBPrintFilter(writer);
                XBCoreTestSampleData.writeSampleDataTerminatedWithTail(listener);
                writer.closeXB();
            }

            XBTestUtils.assertEqualsInputStream(new ByteArrayInputStream(target.toByteArray()), stream);
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

            XBTestUtils.assertEqualsInputStream(new ByteArrayInputStream(target.toByteArray()), stream);
        }
    }

    /**
     * Tests XBListenerWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleBlockDataWithTail() throws Exception {
        try (InputStream stream = XBListenerWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_BLOCK_DATA_WITH_TAIL)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBListenerWriter writer = new XBListenerWriter(target)) {
                XBPrintFilter listener = new XBPrintFilter(writer);
                XBCoreTestSampleData.writeSampleBlockDataWithTail(listener);
                writer.closeXB();
            }

            XBTestUtils.assertEqualsInputStream(new ByteArrayInputStream(target.toByteArray()), stream);
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

            XBTestUtils.assertEqualsInputStream(new ByteArrayInputStream(target.toByteArray()), stream);
        }
    }

    /**
     * Tests XBListenerWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleBlockDataTerminatedWithTail() throws Exception {
        try (InputStream stream = XBListenerWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_BLOCK_DATA_TERMINATED_WITH_TAIL)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBListenerWriter writer = new XBListenerWriter(target)) {
                XBPrintFilter listener = new XBPrintFilter(writer);
                XBCoreTestSampleData.writeSampleBlockDataTerminatedWithTail(listener);
                writer.closeXB();
            }

            XBTestUtils.assertEqualsInputStream(new ByteArrayInputStream(target.toByteArray()), stream);
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

            XBTestUtils.assertEqualsInputStream(new ByteArrayInputStream(target.toByteArray()), stream);
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

            XBTestUtils.assertEqualsInputStream(new ByteArrayInputStream(target.toByteArray()), stream);
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

            XBTestUtils.assertEqualsInputStream(new ByteArrayInputStream(target.toByteArray()), stream);
        }
    }

    /**
     * Tests XBListenerWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleTwoBlocksWithTail() throws Exception {
        try (InputStream stream = XBListenerWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_TWO_BLOCKS_WITH_TAIL)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBListenerWriter writer = new XBListenerWriter(target)) {
                XBPrintFilter listener = new XBPrintFilter(writer);
                XBCoreTestSampleData.writeSampleTwoBlocksWithTail(listener);
                writer.closeXB();
            }

            XBTestUtils.assertEqualsInputStream(new ByteArrayInputStream(target.toByteArray()), stream);
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

            XBTestUtils.assertEqualsInputStream(new ByteArrayInputStream(target.toByteArray()), stream);
        }
    }

    /**
     * Tests XBListenerWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleTwoBlocksTerminatedWithTail() throws Exception {
        try (InputStream stream = XBListenerWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_TWO_BLOCKS_TERMINATED_WITH_TAIL)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBListenerWriter writer = new XBListenerWriter(target)) {
                XBPrintFilter listener = new XBPrintFilter(writer);
                XBCoreTestSampleData.writeSampleTwoBlocksTerminatedWithTail(listener);
                writer.closeXB();
            }

            XBTestUtils.assertEqualsInputStream(new ByteArrayInputStream(target.toByteArray()), stream);
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

            XBTestUtils.assertEqualsInputStream(new ByteArrayInputStream(target.toByteArray()), stream);
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

            XBTestUtils.assertEqualsInputStream(new ByteArrayInputStream(target.toByteArray()), stream);
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

            XBTestUtils.assertEqualsInputStream(new ByteArrayInputStream(target.toByteArray()), stream);
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
