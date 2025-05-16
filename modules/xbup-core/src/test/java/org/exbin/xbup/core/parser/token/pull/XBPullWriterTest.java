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
package org.exbin.xbup.core.parser.token.pull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import junit.framework.TestCase;
import org.exbin.xbup.core.block.XBBlockTerminationMode;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.parser.XBProcessingExceptionType;
import org.exbin.xbup.core.parser.basic.convert.XBConsumerToListener;
import org.exbin.xbup.core.parser.basic.convert.XBPrintFilter;
import org.exbin.xbup.core.parser.data.XBCoreTestSampleData;
import org.exbin.xbup.core.parser.token.pull.convert.XBPullConsumerToConsumer;
import static org.exbin.xbup.core.test.XBTestUtils.assertEqualsInputStream;
import org.junit.Test;

/**
 * Test class for XBPullWriter.
 *
 * @author ExBin Project (https://exbin.org)
 */
public class XBPullWriterTest extends TestCase {

    public XBPullWriterTest(String testName) {
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
     * Tests XBPullWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleEmpty() throws Exception {
        try (InputStream stream = XBPullWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_EMPTY)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBPullWriter writer = new XBPullWriter(target)) {
                XBPrintFilter listener = new XBPrintFilter(new XBConsumerToListener(new XBPullConsumerToConsumer(writer)));
                XBCoreTestSampleData.writeSampleEmpty(listener);
                writer.write();
            }

            assertEqualsInputStream(stream, new ByteArrayInputStream(target.toByteArray()));
        }
    }

    /**
     * Tests XBPullWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleBlock() throws Exception {
        try (InputStream stream = XBPullWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_BLOCK)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBPullWriter writer = new XBPullWriter(target)) {
                XBPrintFilter listener = new XBPrintFilter(new XBConsumerToListener(new XBPullConsumerToConsumer(writer)));
                XBCoreTestSampleData.writeSampleBlock(listener);
                writer.write();
            }

            assertEqualsInputStream(stream, new ByteArrayInputStream(target.toByteArray()));
        }
    }

    /**
     * Tests XBPullWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleBlockWithTail() throws Exception {
        try (InputStream stream = XBPullWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_BLOCK_WITH_TAIL)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBPullWriter writer = new XBPullWriter(target)) {
                XBPrintFilter listener = new XBPrintFilter(new XBConsumerToListener(new XBPullConsumerToConsumer(writer)));
                XBCoreTestSampleData.writeSampleBlockWithTail(listener);
                writer.write();
            }

            assertEqualsInputStream(stream, new ByteArrayInputStream(target.toByteArray()));
        }
    }

    /**
     * Tests XBPullWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleBlockTerminated() throws Exception {
        try (InputStream stream = XBPullWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_BLOCK_TERMINATED)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBPullWriter writer = new XBPullWriter(target)) {
                XBPrintFilter listener = new XBPrintFilter(new XBConsumerToListener(new XBPullConsumerToConsumer(writer)));
                XBCoreTestSampleData.writeSampleBlockTerminated(listener);
                writer.write();
            }

            assertEqualsInputStream(stream, new ByteArrayInputStream(target.toByteArray()));
        }
    }

    /**
     * Tests XBPullWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleBlockTerminatedWithTail() throws Exception {
        try (InputStream stream = XBPullWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_BLOCK_TERMINATED_WITH_TAIL)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBPullWriter writer = new XBPullWriter(target)) {
                XBPrintFilter listener = new XBPrintFilter(new XBConsumerToListener(new XBPullConsumerToConsumer(writer)));
                XBCoreTestSampleData.writeSampleBlockTerminatedWithTail(listener);
                writer.write();
            }

            assertEqualsInputStream(stream, new ByteArrayInputStream(target.toByteArray()));
        }
    }

    /**
     * Tests XBPullWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleData() throws Exception {
        try (InputStream stream = XBPullWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_DATA)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBPullWriter writer = new XBPullWriter(target)) {
                XBPrintFilter listener = new XBPrintFilter(new XBConsumerToListener(new XBPullConsumerToConsumer(writer)));
                XBCoreTestSampleData.writeSampleData(listener);
                writer.write();
            }

            assertEqualsInputStream(stream, new ByteArrayInputStream(target.toByteArray()));
        }
    }

    /**
     * Tests XBPullWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleDataWithTail() throws Exception {
        try (InputStream stream = XBPullWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_DATA_WITH_TAIL)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBPullWriter writer = new XBPullWriter(target)) {
                XBPrintFilter listener = new XBPrintFilter(new XBConsumerToListener(new XBPullConsumerToConsumer(writer)));
                XBCoreTestSampleData.writeSampleDataWithTail(listener);
                writer.write();
            }

            assertEqualsInputStream(stream, new ByteArrayInputStream(target.toByteArray()));
        }
    }

    /**
     * Tests XBPullWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleDataTerminated() throws Exception {
        try (InputStream stream = XBPullWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_DATA_TERMINATED)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBPullWriter writer = new XBPullWriter(target)) {
                XBPrintFilter listener = new XBPrintFilter(new XBConsumerToListener(new XBPullConsumerToConsumer(writer)));
                XBCoreTestSampleData.writeSampleDataTerminated(listener);
                writer.write();
            }

            assertEqualsInputStream(stream, new ByteArrayInputStream(target.toByteArray()));
        }
    }

    /**
     * Tests XBPullWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleDataTerminatedWithTail() throws Exception {
        try (InputStream stream = XBPullWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_DATA_TERMINATED_WITH_TAIL)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBPullWriter writer = new XBPullWriter(target)) {
                XBPrintFilter listener = new XBPrintFilter(new XBConsumerToListener(new XBPullConsumerToConsumer(writer)));
                XBCoreTestSampleData.writeSampleDataTerminatedWithTail(listener);
                writer.write();
            }

            assertEqualsInputStream(stream, new ByteArrayInputStream(target.toByteArray()));
        }
    }

    /**
     * Tests XBPullWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleBlockData() throws Exception {
        try (InputStream stream = XBPullWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_BLOCK_DATA)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBPullWriter writer = new XBPullWriter(target)) {
                XBPrintFilter listener = new XBPrintFilter(new XBConsumerToListener(new XBPullConsumerToConsumer(writer)));
                XBCoreTestSampleData.writeSampleBlockData(listener);
                writer.write();
            }

            assertEqualsInputStream(stream, new ByteArrayInputStream(target.toByteArray()));
        }
    }

    /**
     * Tests XBPullWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleBlockDataWithTail() throws Exception {
        try (InputStream stream = XBPullWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_BLOCK_DATA_WITH_TAIL)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBPullWriter writer = new XBPullWriter(target)) {
                XBPrintFilter listener = new XBPrintFilter(new XBConsumerToListener(new XBPullConsumerToConsumer(writer)));
                XBCoreTestSampleData.writeSampleBlockDataWithTail(listener);
                writer.write();
            }

            assertEqualsInputStream(stream, new ByteArrayInputStream(target.toByteArray()));
        }
    }

    /**
     * Tests XBPullWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleBlockDataTerminated() throws Exception {
        try (InputStream stream = XBPullWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_BLOCK_DATA_TERMINATED)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBPullWriter writer = new XBPullWriter(target)) {
                XBPrintFilter listener = new XBPrintFilter(new XBConsumerToListener(new XBPullConsumerToConsumer(writer)));
                XBCoreTestSampleData.writeSampleBlockDataTerminated(listener);
                writer.write();
            }

            assertEqualsInputStream(stream, new ByteArrayInputStream(target.toByteArray()));
        }
    }

    /**
     * Tests XBPullWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleBlockDataTerminatedWithTail() throws Exception {
        try (InputStream stream = XBPullWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_BLOCK_DATA_TERMINATED_WITH_TAIL)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBPullWriter writer = new XBPullWriter(target)) {
                XBPrintFilter listener = new XBPrintFilter(new XBConsumerToListener(new XBPullConsumerToConsumer(writer)));
                XBCoreTestSampleData.writeSampleBlockDataTerminatedWithTail(listener);
                writer.write();
            }

            assertEqualsInputStream(stream, new ByteArrayInputStream(target.toByteArray()));
        }
    }

    /**
     * Tests XBPullWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleBlockDataHybrid() throws Exception {
        try (InputStream stream = XBPullWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_BLOCK_DATA_HYBRID)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBPullWriter writer = new XBPullWriter(target)) {
                XBPrintFilter listener = new XBPrintFilter(new XBConsumerToListener(new XBPullConsumerToConsumer(writer)));
                XBCoreTestSampleData.writeSampleBlockDataHybrid(listener);
                writer.write();
            }

            assertEqualsInputStream(stream, new ByteArrayInputStream(target.toByteArray()));
        }
    }

    /**
     * Tests XBPullWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleBlockDataHybrid2() throws Exception {
        try (InputStream stream = XBPullWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_BLOCK_DATA_HYBRID2)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBPullWriter writer = new XBPullWriter(target)) {
                XBPrintFilter listener = new XBPrintFilter(new XBConsumerToListener(new XBPullConsumerToConsumer(writer)));
                XBCoreTestSampleData.writeSampleBlockDataHybrid2(listener);
                writer.write();
            }

            assertEqualsInputStream(stream, new ByteArrayInputStream(target.toByteArray()));
        }
    }

    /**
     * Tests XBPullWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleTwoBlocks() throws Exception {
        try (InputStream stream = XBPullWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_TWO_BLOCKS)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBPullWriter writer = new XBPullWriter(target)) {
                XBPrintFilter listener = new XBPrintFilter(new XBConsumerToListener(new XBPullConsumerToConsumer(writer)));
                XBCoreTestSampleData.writeSampleTwoBlocks(listener);
                writer.write();
            }

            assertEqualsInputStream(stream, new ByteArrayInputStream(target.toByteArray()));
        }
    }

    /**
     * Tests XBPullWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleTwoBlocksWithTail() throws Exception {
        try (InputStream stream = XBPullWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_TWO_BLOCKS_WITH_TAIL)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBPullWriter writer = new XBPullWriter(target)) {
                XBPrintFilter listener = new XBPrintFilter(new XBConsumerToListener(new XBPullConsumerToConsumer(writer)));
                XBCoreTestSampleData.writeSampleTwoBlocksWithTail(listener);
                writer.write();
            }

            assertEqualsInputStream(stream, new ByteArrayInputStream(target.toByteArray()));
        }
    }

    /**
     * Tests XBPullWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleTwoBlocksTerminated() throws Exception {
        try (InputStream stream = XBPullWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_TWO_BLOCKS_TERMINATED)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBPullWriter writer = new XBPullWriter(target)) {
                XBPrintFilter listener = new XBPrintFilter(new XBConsumerToListener(new XBPullConsumerToConsumer(writer)));
                XBCoreTestSampleData.writeSampleTwoBlocksTerminated(listener);
                writer.write();
            }

            assertEqualsInputStream(stream, new ByteArrayInputStream(target.toByteArray()));
        }
    }

    /**
     * Tests XBPullWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleTwoBlocksTerminatedWithTail() throws Exception {
        try (InputStream stream = XBPullWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_TWO_BLOCKS_TERMINATED_WITH_TAIL)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBPullWriter writer = new XBPullWriter(target)) {
                XBPrintFilter listener = new XBPrintFilter(new XBConsumerToListener(new XBPullConsumerToConsumer(writer)));
                XBCoreTestSampleData.writeSampleTwoBlocksTerminatedWithTail(listener);
                writer.write();
            }

            assertEqualsInputStream(stream, new ByteArrayInputStream(target.toByteArray()));
        }
    }

    /**
     * Tests XBPullWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleTwoBlocksHybrid() throws Exception {
        try (InputStream stream = XBPullWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_TWO_BLOCKS_HYBRID)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBPullWriter writer = new XBPullWriter(target)) {
                XBPrintFilter listener = new XBPrintFilter(new XBConsumerToListener(new XBPullConsumerToConsumer(writer)));
                XBCoreTestSampleData.writeSampleTwoBlocksHybrid(listener);
                writer.write();
            }

            assertEqualsInputStream(stream, new ByteArrayInputStream(target.toByteArray()));
        }
    }

    /**
     * Tests XBPullWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleTwoBlocksHybrid2() throws Exception {
        try (InputStream stream = XBPullWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_TWO_BLOCKS_HYBRID2)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBPullWriter writer = new XBPullWriter(target)) {
                XBPrintFilter listener = new XBPrintFilter(new XBConsumerToListener(new XBPullConsumerToConsumer(writer)));
                XBCoreTestSampleData.writeSampleTwoBlocksHybrid2(listener);
                writer.write();
            }

            assertEqualsInputStream(stream, new ByteArrayInputStream(target.toByteArray()));
        }
    }

    /**
     * Tests XBPullWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleSixBlocks() throws Exception {
        try (InputStream stream = XBPullWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_SIX_BLOCKS)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBPullWriter writer = new XBPullWriter(target)) {
                XBPrintFilter listener = new XBPrintFilter(new XBConsumerToListener(new XBPullConsumerToConsumer(writer)));
                XBCoreTestSampleData.writeSampleSixBlocks(listener);
                writer.write();
            }

            assertEqualsInputStream(stream, new ByteArrayInputStream(target.toByteArray()));
        }
    }

    /**
     * Tests XBPullWriter class writing corrupted data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteNone() throws Exception {
        Throwable ex = null;

        ByteArrayOutputStream target = new ByteArrayOutputStream();
        try {
            try (XBPullWriter writer = new XBPullWriter(target)) {
                assertNotNull(new XBPrintFilter(new XBConsumerToListener(new XBPullConsumerToConsumer(writer))));
                writer.write();
            }
        } catch (XBProcessingException | IOException e) {
            ex = e;
        }

        assertEquals(new XBProcessingException("End of data reached", XBProcessingExceptionType.UNEXPECTED_END_OF_STREAM), ex);
    }

    /**
     * Tests XBPullWriter class writing corrupted data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteUnfinishedBlock() throws Exception {
        Throwable ex = null;

        ByteArrayOutputStream target = new ByteArrayOutputStream();
        try {
            try (XBPullWriter writer = new XBPullWriter(target)) {
                XBPrintFilter listener = new XBPrintFilter(new XBConsumerToListener(new XBPullConsumerToConsumer(writer)));

                listener.beginXB(XBBlockTerminationMode.SIZE_SPECIFIED);
                writer.write();
            }
        } catch (XBProcessingException | IOException e) {
            ex = e;
        }

        assertEquals(new XBProcessingException("End of data reached", XBProcessingExceptionType.UNEXPECTED_END_OF_STREAM), ex);
    }

    /**
     * Tests XBPullWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteCorruptedEndWithoutData() throws Exception {
        Throwable ex = null;

        ByteArrayOutputStream target = new ByteArrayOutputStream();
        try (XBPullWriter writer = new XBPullWriter(target)) {
            XBPrintFilter listener = new XBPrintFilter(new XBConsumerToListener(new XBPullConsumerToConsumer(writer)));
            XBCoreTestSampleData.writeCorruptedEndWithoutData(listener);
            writer.write();
        } catch (XBProcessingException | IOException e) {
            ex = e;
        }

        assertEquals(new XBProcessingException("Missing at least one attribute", XBProcessingExceptionType.UNEXPECTED_ORDER), ex);
    }

    /**
     * Tests XBPullWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteCorruptedDataAttributeMismatch() throws Exception {
        Throwable ex = null;

        ByteArrayOutputStream target = new ByteArrayOutputStream();
        try (XBPullWriter writer = new XBPullWriter(target)) {
            XBPrintFilter listener = new XBPrintFilter(new XBConsumerToListener(new XBPullConsumerToConsumer(writer)));
            XBCoreTestSampleData.writeCorruptedDataAttributeMismatch(listener);
            writer.write();
        } catch (XBProcessingException | IOException e) {
            ex = e;
        }

        assertEquals(new XBProcessingException("Data block must be followed by block end", XBProcessingExceptionType.UNEXPECTED_ORDER), ex);
    }

    /**
     * Tests XBPullWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteCorruptedDataAttributeMismatch2() throws Exception {
        Throwable ex = null;

        ByteArrayOutputStream target = new ByteArrayOutputStream();
        try (XBPullWriter writer = new XBPullWriter(target)) {
            XBPrintFilter listener = new XBPrintFilter(new XBConsumerToListener(new XBPullConsumerToConsumer(writer)));
            XBCoreTestSampleData.writeCorruptedDataAttributeMismatch2(listener);
            writer.write();
        } catch (XBProcessingException | IOException e) {
            ex = e;
        }

        assertEquals(new XBProcessingException("Must begin with NodeBegin", XBProcessingExceptionType.UNEXPECTED_ORDER), ex);
    }

    /**
     * Tests XBPullWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteCorruptedAttributeAfterEnd() throws Exception {
        Throwable ex = null;

        ByteArrayOutputStream target = new ByteArrayOutputStream();
        try (XBPullWriter writer = new XBPullWriter(target)) {
            XBPrintFilter listener = new XBPrintFilter(new XBConsumerToListener(new XBPullConsumerToConsumer(writer)));
            XBCoreTestSampleData.writeCorruptedAttributeAfterEnd(listener);
            writer.write();
        } catch (XBProcessingException | IOException e) {
            ex = e;
        }

        assertEquals(new XBProcessingException("Missing at least one attribute", XBProcessingExceptionType.UNEXPECTED_ORDER), ex);
    }
}
