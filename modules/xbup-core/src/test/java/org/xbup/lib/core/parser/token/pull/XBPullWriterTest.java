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
package org.xbup.lib.core.parser.token.pull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import static junit.framework.Assert.assertEquals;
import junit.framework.TestCase;
import org.junit.Test;
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.XBProcessingExceptionType;
import org.xbup.lib.core.parser.basic.convert.XBConsumerToListener;
import org.xbup.lib.core.parser.basic.convert.XBPrintFilter;
import org.xbup.lib.core.parser.data.XBCoreTestSampleData;
import org.xbup.lib.core.parser.token.pull.convert.XBPullConsumerToConsumer;
import static org.xbup.lib.core.test.XBTestUtils.assertEqualsInputStream;

/**
 * Test class for XBPullWriter.
 *
 * @version 0.1.25 2015/07/23
 * @author XBUP Project (http://xbup.org)
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
     * Tests XBPullWriter class writting sample data.
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

            assertEqualsInputStream(new ByteArrayInputStream(target.toByteArray()), stream);
        }
    }

    /**
     * Tests XBPullWriter class writting sample data.
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

            assertEqualsInputStream(new ByteArrayInputStream(target.toByteArray()), stream);
        }
    }

    /**
     * Tests XBPullWriter class writting sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleBlockExtended() throws Exception {
        try (InputStream stream = XBPullWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_BLOCK_EXTENDED)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBPullWriter writer = new XBPullWriter(target)) {
                XBPrintFilter listener = new XBPrintFilter(new XBConsumerToListener(new XBPullConsumerToConsumer(writer)));
                XBCoreTestSampleData.writeSampleBlockExtended(listener);
                writer.write();
            }

            assertEqualsInputStream(new ByteArrayInputStream(target.toByteArray()), stream);
        }
    }

    /**
     * Tests XBPullWriter class writting sample data.
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

            assertEqualsInputStream(new ByteArrayInputStream(target.toByteArray()), stream);
        }
    }

    /**
     * Tests XBPullWriter class writting sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleBlockTerminatedExtended() throws Exception {
        try (InputStream stream = XBPullWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_BLOCK_TERMINATED_EXTENDED)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBPullWriter writer = new XBPullWriter(target)) {
                XBPrintFilter listener = new XBPrintFilter(new XBConsumerToListener(new XBPullConsumerToConsumer(writer)));
                XBCoreTestSampleData.writeSampleBlockTerminatedExtended(listener);
                writer.write();
            }

            assertEqualsInputStream(new ByteArrayInputStream(target.toByteArray()), stream);
        }
    }

    /**
     * Tests XBPullWriter class writting sample data.
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

            assertEqualsInputStream(new ByteArrayInputStream(target.toByteArray()), stream);
        }
    }

    /**
     * Tests XBPullWriter class writting sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleDataExtended() throws Exception {
        try (InputStream stream = XBPullWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_DATA_EXTENDED)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBPullWriter writer = new XBPullWriter(target)) {
                XBPrintFilter listener = new XBPrintFilter(new XBConsumerToListener(new XBPullConsumerToConsumer(writer)));
                XBCoreTestSampleData.writeSampleDataExtended(listener);
                writer.write();
            }

            assertEqualsInputStream(new ByteArrayInputStream(target.toByteArray()), stream);
        }
    }

    /**
     * Tests XBPullWriter class writting sample data.
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

            assertEqualsInputStream(new ByteArrayInputStream(target.toByteArray()), stream);
        }
    }

    /**
     * Tests XBPullWriter class writting sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleDataTerminatedExtended() throws Exception {
        try (InputStream stream = XBPullWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_DATA_TERMINATED_EXTENDED)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBPullWriter writer = new XBPullWriter(target)) {
                XBPrintFilter listener = new XBPrintFilter(new XBConsumerToListener(new XBPullConsumerToConsumer(writer)));
                XBCoreTestSampleData.writeSampleDataTerminatedExtended(listener);
                writer.write();
            }

            assertEqualsInputStream(new ByteArrayInputStream(target.toByteArray()), stream);
        }
    }

    /**
     * Tests XBPullWriter class writting sample data.
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

            assertEqualsInputStream(new ByteArrayInputStream(target.toByteArray()), stream);
        }
    }

    /**
     * Tests XBPullWriter class writting sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleTwoBlocksExtended() throws Exception {
        try (InputStream stream = XBPullWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_TWO_BLOCKS_EXTENDED)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBPullWriter writer = new XBPullWriter(target)) {
                XBPrintFilter listener = new XBPrintFilter(new XBConsumerToListener(new XBPullConsumerToConsumer(writer)));
                XBCoreTestSampleData.writeSampleTwoBlocksExtended(listener);
                writer.write();
            }

            assertEqualsInputStream(new ByteArrayInputStream(target.toByteArray()), stream);
        }
    }

    /**
     * Tests XBPullWriter class writting sample data.
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

            assertEqualsInputStream(new ByteArrayInputStream(target.toByteArray()), stream);
        }
    }

    /**
     * Tests XBPullWriter class writting sample data.
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

            assertEqualsInputStream(new ByteArrayInputStream(target.toByteArray()), stream);
        }
    }

    /**
     * Tests XBPullWriter class writting sample data.
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

            assertEqualsInputStream(new ByteArrayInputStream(target.toByteArray()), stream);
        }
    }

    /**
     * Tests XBPullWriter class writting sample data.
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

            assertEqualsInputStream(new ByteArrayInputStream(target.toByteArray()), stream);
        }
    }

    /**
     * Tests XBPullWriter class writting corrupted data.
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
     * Tests XBPullWriter class writting corrupted data.
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
}
