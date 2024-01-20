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
package org.exbin.xbup.core.parser.token.event;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import junit.framework.TestCase;
import org.exbin.xbup.core.block.XBBlockTerminationMode;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.parser.XBProcessingExceptionType;
import org.exbin.xbup.core.parser.data.XBCoreTestSampleData;
import org.exbin.xbup.core.parser.token.XBBeginToken;
import org.exbin.xbup.core.parser.token.event.convert.XBEventListenerToListener;
import org.exbin.xbup.core.parser.token.event.convert.XBPrintEventFilter;
import static org.exbin.xbup.core.test.XBTestUtils.assertEqualsInputStream;
import org.junit.Test;

/**
 * Test class for XBEventWriter.
 *
 * @author ExBin Project (https://exbin.org)
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
     * Tests XBEventWriter class writting sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleEmpty() throws Exception {
        try (InputStream stream = XBEventWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_EMPTY)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBEventWriter writer = new XBEventWriter(target)) {
                XBPrintEventFilter listener = new XBPrintEventFilter(writer);

                writer.open(target);
                XBCoreTestSampleData.writeSampleEmpty(new XBEventListenerToListener(listener));
                writer.closeXB();
            }

            assertEqualsInputStream(new ByteArrayInputStream(target.toByteArray()), stream);
        }
    }

    /**
     * Tests XBEventWriter class writting sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleBlock() throws Exception {
        try (InputStream stream = XBEventWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_BLOCK)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBEventWriter writer = new XBEventWriter(target)) {
                XBPrintEventFilter listener = new XBPrintEventFilter(writer);

                writer.open(target);
                XBCoreTestSampleData.writeSampleBlock(new XBEventListenerToListener(listener));
                writer.closeXB();
            }

            assertEqualsInputStream(new ByteArrayInputStream(target.toByteArray()), stream);
        }
    }

    /**
     * Tests XBEventWriter class writting sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleBlockWithTail() throws Exception {
        try (InputStream stream = XBEventWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_BLOCK_WITH_TAIL)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBEventWriter writer = new XBEventWriter(target)) {
                XBPrintEventFilter listener = new XBPrintEventFilter(writer);

                writer.open(target);
                XBCoreTestSampleData.writeSampleBlockWithTail(new XBEventListenerToListener(listener));
                writer.closeXB();
            }

            assertEqualsInputStream(new ByteArrayInputStream(target.toByteArray()), stream);
        }
    }

    /**
     * Tests XBEventWriter class writting sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleBlockTerminated() throws Exception {
        try (InputStream stream = XBEventWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_BLOCK_TERMINATED)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBEventWriter writer = new XBEventWriter(target)) {
                XBPrintEventFilter listener = new XBPrintEventFilter(writer);

                writer.open(target);
                XBCoreTestSampleData.writeSampleBlockTerminated(new XBEventListenerToListener(listener));
                writer.closeXB();
            }

            assertEqualsInputStream(new ByteArrayInputStream(target.toByteArray()), stream);
        }
    }

    /**
     * Tests XBEventWriter class writting sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleBlockTerminatedWithTail() throws Exception {
        try (InputStream stream = XBEventWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_BLOCK_TERMINATED_WITH_TAIL)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBEventWriter writer = new XBEventWriter(target)) {
                XBPrintEventFilter listener = new XBPrintEventFilter(writer);

                writer.open(target);
                XBCoreTestSampleData.writeSampleBlockTerminatedWithTail(new XBEventListenerToListener(listener));
                writer.closeXB();
            }

            assertEqualsInputStream(new ByteArrayInputStream(target.toByteArray()), stream);
        }
    }

    /**
     * Tests XBEventWriter class writting sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleData() throws Exception {
        try (InputStream stream = XBEventWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_DATA)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBEventWriter writer = new XBEventWriter(target)) {
                XBPrintEventFilter listener = new XBPrintEventFilter(writer);

                writer.open(target);
                XBCoreTestSampleData.writeSampleData(new XBEventListenerToListener(listener));
                writer.closeXB();
            }

            assertEqualsInputStream(new ByteArrayInputStream(target.toByteArray()), stream);
        }
    }

    /**
     * Tests XBEventWriter class writting sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleDataWithTail() throws Exception {
        try (InputStream stream = XBEventWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_DATA_WITH_TAIL)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBEventWriter writer = new XBEventWriter(target)) {
                XBPrintEventFilter listener = new XBPrintEventFilter(writer);

                writer.open(target);
                XBCoreTestSampleData.writeSampleDataWithTail(new XBEventListenerToListener(listener));
                writer.closeXB();
            }

            assertEqualsInputStream(new ByteArrayInputStream(target.toByteArray()), stream);
        }
    }

    /**
     * Tests XBEventWriter class writting sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleDataTerminated() throws Exception {
        try (InputStream stream = XBEventWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_DATA_TERMINATED)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBEventWriter writer = new XBEventWriter(target)) {
                XBPrintEventFilter listener = new XBPrintEventFilter(writer);

                writer.open(target);
                XBCoreTestSampleData.writeSampleDataTerminated(new XBEventListenerToListener(listener));
                writer.closeXB();
            }

            assertEqualsInputStream(new ByteArrayInputStream(target.toByteArray()), stream);
        }
    }

    /**
     * Tests XBEventWriter class writting sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleDataTerminatedWithTail() throws Exception {
        try (InputStream stream = XBEventWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_DATA_TERMINATED_WITH_TAIL)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBEventWriter writer = new XBEventWriter(target)) {
                XBPrintEventFilter listener = new XBPrintEventFilter(writer);

                writer.open(target);
                XBCoreTestSampleData.writeSampleDataTerminatedWithTail(new XBEventListenerToListener(listener));
                writer.closeXB();
            }

            assertEqualsInputStream(new ByteArrayInputStream(target.toByteArray()), stream);
        }
    }

    /**
     * Tests XBEventWriter class writting sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleBlockData() throws Exception {
        try (InputStream stream = XBEventWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_BLOCK_DATA)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBEventWriter writer = new XBEventWriter(target)) {
                XBPrintEventFilter listener = new XBPrintEventFilter(writer);

                writer.open(target);
                XBCoreTestSampleData.writeSampleBlockData(new XBEventListenerToListener(listener));
                writer.closeXB();
            }

            assertEqualsInputStream(new ByteArrayInputStream(target.toByteArray()), stream);
        }
    }

    /**
     * Tests XBEventWriter class writting sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleBlockDataWithTail() throws Exception {
        try (InputStream stream = XBEventWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_BLOCK_DATA_WITH_TAIL)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBEventWriter writer = new XBEventWriter(target)) {
                XBPrintEventFilter listener = new XBPrintEventFilter(writer);

                writer.open(target);
                XBCoreTestSampleData.writeSampleBlockDataWithTail(new XBEventListenerToListener(listener));
                writer.closeXB();
            }

            assertEqualsInputStream(new ByteArrayInputStream(target.toByteArray()), stream);
        }
    }

    /**
     * Tests XBEventWriter class writting sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleBlockDataTerminated() throws Exception {
        try (InputStream stream = XBEventWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_BLOCK_DATA_TERMINATED)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBEventWriter writer = new XBEventWriter(target)) {
                XBPrintEventFilter listener = new XBPrintEventFilter(writer);

                writer.open(target);
                XBCoreTestSampleData.writeSampleBlockDataTerminated(new XBEventListenerToListener(listener));
                writer.closeXB();
            }

            assertEqualsInputStream(new ByteArrayInputStream(target.toByteArray()), stream);
        }
    }

    /**
     * Tests XBEventWriter class writting sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleBlockDataTerminatedWithTail() throws Exception {
        try (InputStream stream = XBEventWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_BLOCK_DATA_TERMINATED_WITH_TAIL)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBEventWriter writer = new XBEventWriter(target)) {
                XBPrintEventFilter listener = new XBPrintEventFilter(writer);

                writer.open(target);
                XBCoreTestSampleData.writeSampleBlockDataTerminatedWithTail(new XBEventListenerToListener(listener));
                writer.closeXB();
            }

            assertEqualsInputStream(new ByteArrayInputStream(target.toByteArray()), stream);
        }
    }

    /**
     * Tests XBEventWriter class writting sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleBlockDataHybrid() throws Exception {
        try (InputStream stream = XBEventWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_BLOCK_DATA_HYBRID)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBEventWriter writer = new XBEventWriter(target)) {
                XBPrintEventFilter listener = new XBPrintEventFilter(writer);

                writer.open(target);
                XBCoreTestSampleData.writeSampleBlockDataHybrid(new XBEventListenerToListener(listener));
                writer.closeXB();
            }

            assertEqualsInputStream(new ByteArrayInputStream(target.toByteArray()), stream);
        }
    }

    /**
     * Tests XBEventWriter class writting sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleBlockDataHybrid2() throws Exception {
        try (InputStream stream = XBEventWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_BLOCK_DATA_HYBRID2)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBEventWriter writer = new XBEventWriter(target)) {
                XBPrintEventFilter listener = new XBPrintEventFilter(writer);

                writer.open(target);
                XBCoreTestSampleData.writeSampleBlockDataHybrid2(new XBEventListenerToListener(listener));
                writer.closeXB();
            }

            assertEqualsInputStream(new ByteArrayInputStream(target.toByteArray()), stream);
        }
    }

    /**
     * Tests XBEventWriter class writting sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleTwoBlocks() throws Exception {
        try (InputStream stream = XBEventWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_TWO_BLOCKS)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBEventWriter writer = new XBEventWriter(target)) {
                XBPrintEventFilter listener = new XBPrintEventFilter(writer);

                writer.open(target);
                XBCoreTestSampleData.writeSampleTwoBlocks(new XBEventListenerToListener(listener));
                writer.closeXB();
            }

            assertEqualsInputStream(new ByteArrayInputStream(target.toByteArray()), stream);
        }
    }

    /**
     * Tests XBEventWriter class writting sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleTwoBlocksWithTail() throws Exception {
        try (InputStream stream = XBEventWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_TWO_BLOCKS_WITH_TAIL)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBEventWriter writer = new XBEventWriter(target)) {
                XBPrintEventFilter listener = new XBPrintEventFilter(writer);

                writer.open(target);
                XBCoreTestSampleData.writeSampleTwoBlocksWithTail(new XBEventListenerToListener(listener));
                writer.closeXB();
            }

            assertEqualsInputStream(new ByteArrayInputStream(target.toByteArray()), stream);
        }
    }

    /**
     * Tests XBEventWriter class writting sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleTwoBlocksTerminated() throws Exception {
        try (InputStream stream = XBEventWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_TWO_BLOCKS_TERMINATED)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBEventWriter writer = new XBEventWriter(target)) {
                XBPrintEventFilter listener = new XBPrintEventFilter(writer);

                writer.open(target);
                XBCoreTestSampleData.writeSampleTwoBlocksTerminated(new XBEventListenerToListener(listener));
                writer.closeXB();
            }

            assertEqualsInputStream(new ByteArrayInputStream(target.toByteArray()), stream);
        }
    }

    /**
     * Tests XBEventWriter class writting sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleTwoBlocksTerminatedWithTail() throws Exception {
        try (InputStream stream = XBEventWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_TWO_BLOCKS_TERMINATED_WITH_TAIL)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBEventWriter writer = new XBEventWriter(target)) {
                XBPrintEventFilter listener = new XBPrintEventFilter(writer);

                writer.open(target);
                XBCoreTestSampleData.writeSampleTwoBlocksTerminatedWithTail(new XBEventListenerToListener(listener));
                writer.closeXB();
            }

            assertEqualsInputStream(new ByteArrayInputStream(target.toByteArray()), stream);
        }
    }

    /**
     * Tests XBEventWriter class writting sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleTwoBlocksHybrid() throws Exception {
        try (InputStream stream = XBEventWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_TWO_BLOCKS_HYBRID)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBEventWriter writer = new XBEventWriter(target)) {
                XBPrintEventFilter listener = new XBPrintEventFilter(writer);

                writer.open(target);
                XBCoreTestSampleData.writeSampleTwoBlocksHybrid(new XBEventListenerToListener(listener));
                writer.closeXB();
            }

            assertEqualsInputStream(new ByteArrayInputStream(target.toByteArray()), stream);
        }
    }

    /**
     * Tests XBEventWriter class writting sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleTwoBlocksHybrid2() throws Exception {
        try (InputStream stream = XBEventWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_TWO_BLOCKS_HYBRID2)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBEventWriter writer = new XBEventWriter(target)) {
                XBPrintEventFilter listener = new XBPrintEventFilter(writer);

                writer.open(target);
                XBCoreTestSampleData.writeSampleTwoBlocksHybrid2(new XBEventListenerToListener(listener));
                writer.closeXB();
            }

            assertEqualsInputStream(new ByteArrayInputStream(target.toByteArray()), stream);
        }
    }

    /**
     * Tests XBEventWriter class writting sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleSixBlocks() throws Exception {
        try (InputStream stream = XBEventWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_SIX_BLOCKS)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBEventWriter writer = new XBEventWriter(target)) {
                XBPrintEventFilter listener = new XBPrintEventFilter(writer);

                writer.open(target);
                XBCoreTestSampleData.writeSampleSixBlocks(new XBEventListenerToListener(listener));
                writer.closeXB();
            }

            assertEqualsInputStream(new ByteArrayInputStream(target.toByteArray()), stream);
        }
    }

    /**
     * Tests XBEventWriter class writting corrupted data.
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
     * Tests XBEventWriter class writting corrupted data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteUnfinishedBlock() throws Exception {
        Throwable ex = null;

        ByteArrayOutputStream target = new ByteArrayOutputStream();
        try {
            try (XBEventWriter writer = new XBEventWriter(target)) {
                XBPrintEventFilter listener = new XBPrintEventFilter(writer);

                listener.putXBToken(XBBeginToken.create(XBBlockTerminationMode.SIZE_SPECIFIED));
                writer.closeXB();
            }
        } catch (XBProcessingException | IOException e) {
            ex = e;
        }

        assertEquals(new XBProcessingException("Unexpected end of stream", XBProcessingExceptionType.UNEXPECTED_END_OF_STREAM), ex);
    }

    /**
     * Tests XBEventWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteCorruptedEndWithoutData() throws Exception {
        Throwable ex = null;

        ByteArrayOutputStream target = new ByteArrayOutputStream();
        try (XBEventWriter writer = new XBEventWriter(target)) {
            XBPrintEventFilter listener = new XBPrintEventFilter(writer);
            XBCoreTestSampleData.writeCorruptedEndWithoutData(new XBEventListenerToListener(listener));
        } catch (XBProcessingException | IOException e) {
            ex = e;
        }

        assertEquals(new XBProcessingException("Unexpected end token", XBProcessingExceptionType.UNEXPECTED_ORDER), ex);
    }

    /**
     * Tests XBEventWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteCorruptedDataAttributeMismatch() throws Exception {
        Throwable ex = null;

        ByteArrayOutputStream target = new ByteArrayOutputStream();
        try (XBEventWriter writer = new XBEventWriter(target)) {
            XBPrintEventFilter listener = new XBPrintEventFilter(writer);
            XBCoreTestSampleData.writeCorruptedDataAttributeMismatch(new XBEventListenerToListener(listener));
        } catch (XBProcessingException | IOException e) {
            ex = e;
        }

        assertEquals(new XBProcessingException("Unexpected attribute", XBProcessingExceptionType.UNEXPECTED_ORDER), ex);
    }

    /**
     * Tests XBEventWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteCorruptedDataAttributeMismatch2() throws Exception {
        Throwable ex = null;

        ByteArrayOutputStream target = new ByteArrayOutputStream();
        try (XBEventWriter writer = new XBEventWriter(target)) {
            XBPrintEventFilter listener = new XBPrintEventFilter(writer);
            XBCoreTestSampleData.writeCorruptedDataAttributeMismatch2(new XBEventListenerToListener(listener));
        } catch (XBProcessingException | IOException e) {
            ex = e;
        }

        assertEquals(new XBProcessingException("Unexpected data token", XBProcessingExceptionType.UNEXPECTED_ORDER), ex);
    }

    /**
     * Tests XBEventWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteCorruptedAttributeAfterEnd() throws Exception {
        Throwable ex = null;

        ByteArrayOutputStream target = new ByteArrayOutputStream();
        try (XBEventWriter writer = new XBEventWriter(target)) {
            XBPrintEventFilter listener = new XBPrintEventFilter(writer);
            XBCoreTestSampleData.writeCorruptedAttributeAfterEnd(new XBEventListenerToListener(listener));
        } catch (XBProcessingException | IOException e) {
            ex = e;
        }

        assertEquals(new XBProcessingException("Unexpected attribute", XBProcessingExceptionType.UNEXPECTED_ORDER), ex);
    }
}
