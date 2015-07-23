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
import java.io.IOException;
import java.io.InputStream;
import static junit.framework.Assert.assertEquals;
import junit.framework.TestCase;
import org.junit.Test;
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.XBProcessingExceptionType;
import org.xbup.lib.core.parser.data.XBCoreTestSampleData;
import org.xbup.lib.core.parser.token.XBBeginToken;
import org.xbup.lib.core.parser.token.event.convert.XBEventListenerToListener;
import org.xbup.lib.core.parser.token.event.convert.XBPrintEventFilter;
import static org.xbup.lib.core.test.XBTestUtils.assertEqualsInputStream;

/**
 * Test class for XBEventWriter.
 *
 * @version 0.1.25 2015/07/23
 * @author XBUP Project (http://xbup.org)
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
    public void testWriteSampleBlockExtended() throws Exception {
        try (InputStream stream = XBEventWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_BLOCK_EXTENDED)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBEventWriter writer = new XBEventWriter(target)) {
                XBPrintEventFilter listener = new XBPrintEventFilter(writer);

                writer.open(target);
                XBCoreTestSampleData.writeSampleBlockExtended(new XBEventListenerToListener(listener));
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
    public void testWriteSampleBlockTerminatedExtended() throws Exception {
        try (InputStream stream = XBEventWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_BLOCK_TERMINATED_EXTENDED)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBEventWriter writer = new XBEventWriter(target)) {
                XBPrintEventFilter listener = new XBPrintEventFilter(writer);

                writer.open(target);
                XBCoreTestSampleData.writeSampleBlockTerminatedExtended(new XBEventListenerToListener(listener));
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
    public void testWriteSampleDataExtended() throws Exception {
        try (InputStream stream = XBEventWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_DATA_EXTENDED)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBEventWriter writer = new XBEventWriter(target)) {
                XBPrintEventFilter listener = new XBPrintEventFilter(writer);

                writer.open(target);
                XBCoreTestSampleData.writeSampleDataExtended(new XBEventListenerToListener(listener));
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
    public void testWriteSampleDataTerminatedExtended() throws Exception {
        try (InputStream stream = XBEventWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_DATA_TERMINATED_EXTENDED)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBEventWriter writer = new XBEventWriter(target)) {
                XBPrintEventFilter listener = new XBPrintEventFilter(writer);

                writer.open(target);
                XBCoreTestSampleData.writeSampleDataTerminatedExtended(new XBEventListenerToListener(listener));
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
    public void testWriteSampleTwoBlocksExtended() throws Exception {
        try (InputStream stream = XBEventWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_TWO_BLOCKS_EXTENDED)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            try (XBEventWriter writer = new XBEventWriter(target)) {
                XBPrintEventFilter listener = new XBPrintEventFilter(writer);

                writer.open(target);
                XBCoreTestSampleData.writeSampleTwoBlocksExtended(new XBEventListenerToListener(listener));
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

                listener.putXBToken(new XBBeginToken(XBBlockTerminationMode.SIZE_SPECIFIED));
                writer.closeXB();
            }
        } catch (XBProcessingException | IOException e) {
            ex = e;
        }

        assertEquals(new XBProcessingException("Unexpected end of stream", XBProcessingExceptionType.UNEXPECTED_END_OF_STREAM), ex);
    }
}
