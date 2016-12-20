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

import java.io.IOException;
import java.io.InputStream;
import static junit.framework.Assert.assertEquals;
import junit.framework.TestCase;
import org.exbin.xbup.core.parser.XBParseException;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.parser.XBProcessingExceptionType;
import org.exbin.xbup.core.parser.basic.convert.XBConsumerToListener;
import org.exbin.xbup.core.parser.basic.convert.XBPrintFilter;
import org.exbin.xbup.core.parser.data.XBCoreTestSampleData;
import org.exbin.xbup.core.test.XBTestUtils;
import org.exbin.xbup.core.test.XBTestUtils.TokenAssertXBFilter;
import org.junit.Test;

/**
 * Test class for XBProviderReader.
 *
 * @version 0.2.0 2016/09/25
 * @author ExBin Project (http://exbin.org)
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
     * Tests XBProviderReader class reading simple empty block.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleEmpty() throws Exception {
        try (InputStream stream = XBProviderReaderTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_EMPTY)) {
            XBProviderReader instance = new XBProviderReader(stream);

            XBTestUtils.BufferAssertXBFilter assertListener;
            XBConsumerToListener buffer = new XBConsumerToListener(null);
            XBCoreTestSampleData.writeSampleEmpty(buffer);
            assertListener = new XBTestUtils.BufferAssertXBFilter(buffer.getTokens());

            while (!instance.isFinished()) {
                instance.produceXB(assertListener);
            }
            assertTrue(assertListener.isFinished());

            instance.close();
        }
    }

    /**
     * Tests XBProviderReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleBlock() throws Exception {
        try (InputStream stream = XBProviderReaderTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_BLOCK)) {
            XBProviderReader instance = new XBProviderReader(stream);

            XBTestUtils.BufferAssertXBFilter assertListener;
            XBConsumerToListener buffer = new XBConsumerToListener(null);
            XBCoreTestSampleData.writeSampleBlock(buffer);
            assertListener = new XBTestUtils.BufferAssertXBFilter(buffer.getTokens());

            while (!instance.isFinished()) {
                instance.produceXB(assertListener);
            }
            assertTrue(assertListener.isFinished());

            instance.close();
        }
    }

    /**
     * Tests XBProviderReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleBlockWithTail() throws Exception {
        try (InputStream stream = XBProviderReaderTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_BLOCK_WITH_TAIL)) {
            XBProviderReader instance = new XBProviderReader(stream);

            XBTestUtils.BufferAssertXBFilter assertListener;
            XBConsumerToListener buffer = new XBConsumerToListener(null);
            XBCoreTestSampleData.writeSampleBlockWithTail(buffer);
            assertListener = new XBTestUtils.BufferAssertXBFilter(buffer.getTokens());

            while (!instance.isFinished()) {
                instance.produceXB(assertListener);
            }
            assertTrue(assertListener.isFinished());

            instance.close();
        }
    }

    /**
     * Tests XBProviderReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleBlockTerminated() throws Exception {
        try (InputStream stream = XBProviderReaderTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_BLOCK_TERMINATED)) {
            XBProviderReader instance = new XBProviderReader(stream);

            XBTestUtils.BufferAssertXBFilter assertListener;
            XBConsumerToListener buffer = new XBConsumerToListener(null);
            XBCoreTestSampleData.writeSampleBlockTerminated(buffer);
            assertListener = new XBTestUtils.BufferAssertXBFilter(buffer.getTokens());

            while (!instance.isFinished()) {
                instance.produceXB(assertListener);
            }
            assertTrue(assertListener.isFinished());

            instance.close();
        }
    }

    /**
     * Tests XBProviderReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleBlockTerminatedWithTail() throws Exception {
        try (InputStream stream = XBProviderReaderTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_BLOCK_TERMINATED_WITH_TAIL)) {
            XBProviderReader instance = new XBProviderReader(stream);

            XBTestUtils.BufferAssertXBFilter assertListener;
            XBConsumerToListener buffer = new XBConsumerToListener(null);
            XBCoreTestSampleData.writeSampleBlockTerminatedWithTail(buffer);
            assertListener = new XBTestUtils.BufferAssertXBFilter(buffer.getTokens());

            while (!instance.isFinished()) {
                instance.produceXB(assertListener);
            }
            assertTrue(assertListener.isFinished());

            instance.close();
        }
    }

    /**
     * Tests XBProviderReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleData() throws Exception {
        try (InputStream stream = XBProviderReaderTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_DATA)) {
            XBProviderReader instance = new XBProviderReader(stream);

            XBTestUtils.BufferAssertXBFilter assertListener;
            XBConsumerToListener buffer = new XBConsumerToListener(null);
            XBCoreTestSampleData.writeSampleData(buffer);
            assertListener = new XBTestUtils.BufferAssertXBFilter(buffer.getTokens());

            while (!instance.isFinished()) {
                instance.produceXB(assertListener);
            }
            assertTrue(assertListener.isFinished());

            instance.close();
        }
    }

    /**
     * Tests XBProviderReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleDataWithTail() throws Exception {
        try (InputStream stream = XBProviderReaderTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_DATA_WITH_TAIL)) {
            XBProviderReader instance = new XBProviderReader(stream);

            XBTestUtils.BufferAssertXBFilter assertListener;
            XBConsumerToListener buffer = new XBConsumerToListener(null);
            XBCoreTestSampleData.writeSampleDataWithTail(buffer);
            assertListener = new XBTestUtils.BufferAssertXBFilter(buffer.getTokens());

            while (!instance.isFinished()) {
                instance.produceXB(assertListener);
            }
            assertTrue(assertListener.isFinished());

            instance.close();
        }
    }

    /**
     * Tests XBProviderReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleDataTerminated() throws Exception {
        try (InputStream stream = XBProviderReaderTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_DATA_TERMINATED)) {
            XBProviderReader instance = new XBProviderReader(stream);

            XBTestUtils.BufferAssertXBFilter assertListener;
            XBConsumerToListener buffer = new XBConsumerToListener(null);
            XBCoreTestSampleData.writeSampleDataTerminated(buffer);
            assertListener = new XBTestUtils.BufferAssertXBFilter(buffer.getTokens());

            while (!instance.isFinished()) {
                instance.produceXB(assertListener);
            }
            assertTrue(assertListener.isFinished());

            instance.close();
        }
    }

    /**
     * Tests XBProviderReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleDataTerminatedWithTail() throws Exception {
        try (InputStream stream = XBProviderReaderTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_DATA_TERMINATED_WITH_TAIL)) {
            XBProviderReader instance = new XBProviderReader(stream);

            XBTestUtils.BufferAssertXBFilter assertListener;
            XBConsumerToListener buffer = new XBConsumerToListener(null);
            XBCoreTestSampleData.writeSampleDataTerminatedWithTail(buffer);
            assertListener = new XBTestUtils.BufferAssertXBFilter(buffer.getTokens());

            while (!instance.isFinished()) {
                instance.produceXB(assertListener);
            }
            assertTrue(assertListener.isFinished());

            instance.close();
        }
    }

    /**
     * Tests XBProviderReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleBlockData() throws Exception {
        try (InputStream stream = XBProviderReaderTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_BLOCK_DATA)) {
            XBProviderReader instance = new XBProviderReader(stream);

            XBTestUtils.BufferAssertXBFilter assertListener;
            XBConsumerToListener buffer = new XBConsumerToListener(null);
            XBCoreTestSampleData.writeSampleBlockData(buffer);
            assertListener = new XBTestUtils.BufferAssertXBFilter(buffer.getTokens());

            while (!instance.isFinished()) {
                instance.produceXB(assertListener);
            }
            assertTrue(assertListener.isFinished());

            instance.close();
        }
    }

    /**
     * Tests XBProviderReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleBlockDataWithTail() throws Exception {
        try (InputStream stream = XBProviderReaderTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_BLOCK_DATA_WITH_TAIL)) {
            XBProviderReader instance = new XBProviderReader(stream);

            XBTestUtils.BufferAssertXBFilter assertListener;
            XBConsumerToListener buffer = new XBConsumerToListener(null);
            XBCoreTestSampleData.writeSampleBlockDataWithTail(buffer);
            assertListener = new XBTestUtils.BufferAssertXBFilter(buffer.getTokens());

            while (!instance.isFinished()) {
                instance.produceXB(assertListener);
            }
            assertTrue(assertListener.isFinished());

            instance.close();
        }
    }

    /**
     * Tests XBProviderReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleBlockDataTerminated() throws Exception {
        try (InputStream stream = XBProviderReaderTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_BLOCK_DATA_TERMINATED)) {
            XBProviderReader instance = new XBProviderReader(stream);

            XBTestUtils.BufferAssertXBFilter assertListener;
            XBConsumerToListener buffer = new XBConsumerToListener(null);
            XBCoreTestSampleData.writeSampleBlockDataTerminated(buffer);
            assertListener = new XBTestUtils.BufferAssertXBFilter(buffer.getTokens());

            while (!instance.isFinished()) {
                instance.produceXB(assertListener);
            }
            assertTrue(assertListener.isFinished());

            instance.close();
        }
    }

    /**
     * Tests XBProviderReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleBlockDataTerminatedWithTail() throws Exception {
        try (InputStream stream = XBProviderReaderTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_BLOCK_DATA_TERMINATED_WITH_TAIL)) {
            XBProviderReader instance = new XBProviderReader(stream);

            XBTestUtils.BufferAssertXBFilter assertListener;
            XBConsumerToListener buffer = new XBConsumerToListener(null);
            XBCoreTestSampleData.writeSampleBlockDataTerminatedWithTail(buffer);
            assertListener = new XBTestUtils.BufferAssertXBFilter(buffer.getTokens());

            while (!instance.isFinished()) {
                instance.produceXB(assertListener);
            }
            assertTrue(assertListener.isFinished());

            instance.close();
        }
    }

    /**
     * Tests XBProviderReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleBlockDataHybrid() throws Exception {
        try (InputStream stream = XBProviderReaderTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_BLOCK_DATA_HYBRID)) {
            XBProviderReader instance = new XBProviderReader(stream);

            XBTestUtils.BufferAssertXBFilter assertListener;
            XBConsumerToListener buffer = new XBConsumerToListener(null);
            XBCoreTestSampleData.writeSampleBlockDataHybrid(buffer);
            assertListener = new XBTestUtils.BufferAssertXBFilter(buffer.getTokens());

            while (!instance.isFinished()) {
                instance.produceXB(assertListener);
            }
            assertTrue(assertListener.isFinished());

            instance.close();
        }
    }

    /**
     * Tests XBProviderReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleBlockDataHybrid2() throws Exception {
        try (InputStream stream = XBProviderReaderTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_BLOCK_DATA_HYBRID2)) {
            XBProviderReader instance = new XBProviderReader(stream);

            XBTestUtils.BufferAssertXBFilter assertListener;
            XBConsumerToListener buffer = new XBConsumerToListener(null);
            XBCoreTestSampleData.writeSampleBlockDataHybrid2(buffer);
            assertListener = new XBTestUtils.BufferAssertXBFilter(buffer.getTokens());

            while (!instance.isFinished()) {
                instance.produceXB(assertListener);
            }
            assertTrue(assertListener.isFinished());

            instance.close();
        }
    }

    /**
     * Tests XBProviderReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleTwoBlocks() throws Exception {
        try (InputStream stream = XBProviderReaderTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_TWO_BLOCKS)) {
            XBProviderReader instance = new XBProviderReader(stream);

            XBTestUtils.BufferAssertXBFilter assertListener;
            XBConsumerToListener buffer = new XBConsumerToListener(null);
            XBCoreTestSampleData.writeSampleTwoBlocks(buffer);
            assertListener = new XBTestUtils.BufferAssertXBFilter(buffer.getTokens());

            while (!instance.isFinished()) {
                instance.produceXB(assertListener);
            }
            assertTrue(assertListener.isFinished());

            instance.close();
        }
    }

    /**
     * Tests XBProviderReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleTwoBlocksWithTail() throws Exception {
        try (InputStream stream = XBProviderReaderTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_TWO_BLOCKS_WITH_TAIL)) {
            XBProviderReader instance = new XBProviderReader(stream);

            XBTestUtils.BufferAssertXBFilter assertListener;
            XBConsumerToListener buffer = new XBConsumerToListener(null);
            XBCoreTestSampleData.writeSampleTwoBlocksWithTail(buffer);
            assertListener = new XBTestUtils.BufferAssertXBFilter(buffer.getTokens());

            while (!instance.isFinished()) {
                instance.produceXB(assertListener);
            }
            assertTrue(assertListener.isFinished());

            instance.close();
        }
    }

    /**
     * Tests XBProviderReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleTwoBlocksTerminated() throws Exception {
        try (InputStream stream = XBProviderReaderTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_TWO_BLOCKS_TERMINATED)) {
            XBProviderReader instance = new XBProviderReader(stream);

            XBTestUtils.BufferAssertXBFilter assertListener;
            XBConsumerToListener buffer = new XBConsumerToListener(null);
            XBCoreTestSampleData.writeSampleTwoBlocksTerminated(buffer);
            assertListener = new XBTestUtils.BufferAssertXBFilter(buffer.getTokens());

            while (!instance.isFinished()) {
                instance.produceXB(assertListener);
            }
            assertTrue(assertListener.isFinished());

            instance.close();
        }
    }

    /**
     * Tests XBProviderReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleTwoBlocksTerminatedWithTail() throws Exception {
        try (InputStream stream = XBProviderReaderTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_TWO_BLOCKS_TERMINATED_WITH_TAIL)) {
            XBProviderReader instance = new XBProviderReader(stream);

            XBTestUtils.BufferAssertXBFilter assertListener;
            XBConsumerToListener buffer = new XBConsumerToListener(null);
            XBCoreTestSampleData.writeSampleTwoBlocksTerminatedWithTail(buffer);
            assertListener = new XBTestUtils.BufferAssertXBFilter(buffer.getTokens());

            while (!instance.isFinished()) {
                instance.produceXB(assertListener);
            }
            assertTrue(assertListener.isFinished());

            instance.close();
        }
    }

    /**
     * Tests XBProviderReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleTwoBlocksHybrid() throws Exception {
        try (InputStream stream = XBProviderReaderTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_TWO_BLOCKS_HYBRID)) {
            XBProviderReader instance = new XBProviderReader(stream);

            XBTestUtils.BufferAssertXBFilter assertListener;
            XBConsumerToListener buffer = new XBConsumerToListener(null);
            XBCoreTestSampleData.writeSampleTwoBlocksHybrid(buffer);
            assertListener = new XBTestUtils.BufferAssertXBFilter(buffer.getTokens());

            while (!instance.isFinished()) {
                instance.produceXB(assertListener);
            }
            assertTrue(assertListener.isFinished());

            instance.close();
        }
    }

    /**
     * Tests XBProviderReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleTwoBlocksHybrid2() throws Exception {
        try (InputStream stream = XBProviderReaderTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_TWO_BLOCKS_HYBRID2)) {
            XBProviderReader instance = new XBProviderReader(stream);

            XBTestUtils.BufferAssertXBFilter assertListener;
            XBConsumerToListener buffer = new XBConsumerToListener(null);
            XBCoreTestSampleData.writeSampleTwoBlocksHybrid2(buffer);
            assertListener = new XBTestUtils.BufferAssertXBFilter(buffer.getTokens());

            while (!instance.isFinished()) {
                instance.produceXB(assertListener);
            }
            assertTrue(assertListener.isFinished());

            instance.close();
        }
    }

    /**
     * Tests XBProviderReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleSixBlocks() throws Exception {
        try (InputStream stream = XBProviderReaderTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_SIX_BLOCKS)) {
            XBProviderReader instance = new XBProviderReader(stream);

            XBTestUtils.BufferAssertXBFilter assertListener;
            XBConsumerToListener buffer = new XBConsumerToListener(null);
            XBCoreTestSampleData.writeSampleSixBlocks(buffer);
            assertListener = new XBTestUtils.BufferAssertXBFilter(buffer.getTokens());

            while (!instance.isFinished()) {
                instance.produceXB(assertListener);
            }
            assertTrue(assertListener.isFinished());

            instance.close();
        }
    }

    /**
     * Tests XBProviderReader class reading empty file.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadCorruptedEmpty() throws Exception {
        Throwable ex = null;

        try (InputStream stream = XBProviderReaderTest.class.getResourceAsStream(XBCoreTestSampleData.CORRUPTED_EMPTY)) {
            XBProviderReader instance = new XBProviderReader(stream);
            try {
                instance.produceXB(new TokenAssertXBFilter(null));
            } catch (XBProcessingException | IOException e) {
                ex = e;
            }

            assertEquals(new XBProcessingException("End of data reached", XBProcessingExceptionType.UNEXPECTED_END_OF_STREAM), ex);
        }
    }

    /**
     * Tests XBProviderReader class reading single byte.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadCorruptedSingleByte() throws Exception {
        Throwable ex = null;

        try (InputStream stream = XBProviderReaderTest.class.getResourceAsStream(XBCoreTestSampleData.CORRUPTED_SINGLE_BYTE)) {
            XBProviderReader instance = new XBProviderReader(stream);
            try {
                instance.produceXB(new TokenAssertXBFilter(null));
            } catch (XBProcessingException | IOException e) {
                ex = e;
            }

            assertEquals(new XBProcessingException("End of data reached", XBProcessingExceptionType.UNEXPECTED_END_OF_STREAM), ex);
        }
    }

    /**
     * Tests XBProviderReader class reading wrong header.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadCorruptedWrongHeader() throws Exception {
        Throwable ex = null;

        try (InputStream stream = XBProviderReaderTest.class.getResourceAsStream(XBCoreTestSampleData.CORRUPTED_WRONG_HEADER)) {
            XBProviderReader instance = new XBProviderReader(stream);
            try {
                instance.produceXB(new TokenAssertXBFilter(null));
            } catch (XBProcessingException | IOException e) {
                ex = e;
            }

            assertEquals(new XBParseException("Unsupported header: 0xfe0059420002", XBProcessingExceptionType.CORRUPTED_HEADER), ex);
        }
    }

    /**
     * Tests XBProviderReader class reading corrupted file.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadCorruptedIncompleteBlock() throws Exception {
        Throwable ex = null;

        try (InputStream stream = XBProviderReaderTest.class.getResourceAsStream(XBCoreTestSampleData.CORRUPTED_INCOMPLETE_BLOCK)) {
            XBProviderReader instance = new XBProviderReader(stream);
            try {
                XBListener listener = new XBPrintFilter();
                while (!instance.isFinished()) {
                    instance.produceXB(listener);
                }
            } catch (XBProcessingException | IOException e) {
                ex = e;
            }

            assertEquals(new XBParseException("End of data reached", XBProcessingExceptionType.UNEXPECTED_END_OF_STREAM), ex);
        }
    }

    /**
     * Tests XBProviderReader class reading corrupted file.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadCorruptedIncompleteBlock2() throws Exception {
        Throwable ex = null;

        try (InputStream stream = XBProviderReaderTest.class.getResourceAsStream(XBCoreTestSampleData.CORRUPTED_INCOMPLETE_BLOCK2)) {
            XBProviderReader instance = new XBProviderReader(stream);
            try {
                XBListener listener = new XBPrintFilter();
                while (!instance.isFinished()) {
                    instance.produceXB(listener);
                }
            } catch (XBProcessingException | IOException e) {
                ex = e;
            }

            assertEquals(new XBParseException("End of data reached", XBProcessingExceptionType.UNEXPECTED_END_OF_STREAM), ex);
        }
    }

    /**
     * Tests XBProviderReader class reading corrupted file.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadCorruptedIncompleteBlockTerminated() throws Exception {
        Throwable ex = null;

        try (InputStream stream = XBProviderReaderTest.class.getResourceAsStream(XBCoreTestSampleData.CORRUPTED_INCOMPLETE_BLOCK_TERMINATED)) {
            XBProviderReader instance = new XBProviderReader(stream);
            try {
                XBListener listener = new XBPrintFilter();
                while (!instance.isFinished()) {
                    instance.produceXB(listener);
                }
            } catch (XBProcessingException | IOException e) {
                ex = e;
            }

            assertEquals(new XBParseException("End of data reached", XBProcessingExceptionType.UNEXPECTED_END_OF_STREAM), ex);
        }
    }

    /**
     * Tests XBProviderReader class reading corrupted file.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadCorruptedIncompleteData() throws Exception {
        Throwable ex = null;

        try (InputStream stream = XBProviderReaderTest.class.getResourceAsStream(XBCoreTestSampleData.CORRUPTED_INCOMPLETE_DATA)) {
            XBProviderReader instance = new XBProviderReader(stream);
            try {
                XBListener listener = new XBPrintFilter();
                while (!instance.isFinished()) {
                    instance.produceXB(listener);
                }
            } catch (XBProcessingException | IOException e) {
                ex = e;
            }

            assertEquals(new XBParseException("Unexpected end of stream", XBProcessingExceptionType.UNEXPECTED_END_OF_STREAM), ex);
        }
    }

    /**
     * Tests XBProviderReader class reading corrupted file.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadCorruptedIncompleteDataTerminated() throws Exception {
        Throwable ex = null;

        try (InputStream stream = XBProviderReaderTest.class.getResourceAsStream(XBCoreTestSampleData.CORRUPTED_INCOMPLETE_DATA_TERMINATED)) {
            XBProviderReader instance = new XBProviderReader(stream);
            try {
                XBListener listener = new XBPrintFilter();
                while (!instance.isFinished()) {
                    instance.produceXB(listener);
                }
            } catch (XBProcessingException | IOException e) {
                ex = e;
            }

            assertEquals(new XBParseException("Missing data block terminator", XBProcessingExceptionType.UNEXPECTED_END_OF_STREAM), ex);
        }
    }

    /**
     * Tests XBProviderReader class reading corrupted file.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadCorruptedUnexpectedTerminator() throws Exception {
        Throwable ex = null;

        try (InputStream stream = XBProviderReaderTest.class.getResourceAsStream(XBCoreTestSampleData.CORRUPTED_UNEXPECTED_TERMINATOR)) {
            XBProviderReader instance = new XBProviderReader(stream);
            try {
                XBListener listener = new XBPrintFilter();
                while (!instance.isFinished()) {
                    instance.produceXB(listener);
                }
            } catch (XBProcessingException | IOException e) {
                ex = e;
            }

            assertEquals(new XBParseException("Unexpected terminator", XBProcessingExceptionType.UNEXPECTED_TERMINATOR), ex);
        }
    }

    /**
     * Tests XBProviderReader class reading corrupted file.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadCorruptedUnexpectedTerminator2() throws Exception {
        Throwable ex = null;

        try (InputStream stream = XBProviderReaderTest.class.getResourceAsStream(XBCoreTestSampleData.CORRUPTED_UNEXPECTED_TERMINATOR2)) {
            XBProviderReader instance = new XBProviderReader(stream);
            try {
                XBListener listener = new XBPrintFilter();
                while (!instance.isFinished()) {
                    instance.produceXB(listener);
                }
            } catch (XBProcessingException | IOException e) {
                ex = e;
            }

            assertEquals(new XBParseException("Unexpected terminator", XBProcessingExceptionType.UNEXPECTED_TERMINATOR), ex);
        }
    }

    /**
     * Tests XBProviderReader class reading corrupted file.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadCorruptedChildOverflow() throws Exception {
        Throwable ex = null;

        try (InputStream stream = XBProviderReaderTest.class.getResourceAsStream(XBCoreTestSampleData.CORRUPTED_CHILD_OVERFLOW)) {
            XBProviderReader instance = new XBProviderReader(stream);
            try {
                XBListener listener = new XBPrintFilter();
                while (!instance.isFinished()) {
                    instance.produceXB(listener);
                }
            } catch (XBProcessingException | IOException e) {
                ex = e;
            }

            assertEquals(new XBParseException("Block overflow", XBProcessingExceptionType.BLOCK_OVERFLOW), ex);
        }
    }

    /**
     * Tests XBProviderReader class reading corrupted file.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadCorruptedAttributeOverflow() throws Exception {
        Throwable ex = null;

        try (InputStream stream = XBProviderReaderTest.class.getResourceAsStream(XBCoreTestSampleData.CORRUPTED_ATTRIBUTE_OVERFLOW)) {
            XBProviderReader instance = new XBProviderReader(stream);
            try {
                XBListener listener = new XBPrintFilter();
                while (!instance.isFinished()) {
                    instance.produceXB(listener);
                }
            } catch (XBProcessingException | IOException e) {
                ex = e;
            }

            assertEquals(new XBParseException("Attribute overflow", XBProcessingExceptionType.ATTRIBUTE_OVERFLOW), ex);
        }
    }

    /**
     * Tests XBProviderReader class reading corrupted file.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadCorruptedDataOverflow() throws Exception {
        Throwable ex = null;

        try (InputStream stream = XBProviderReaderTest.class.getResourceAsStream(XBCoreTestSampleData.CORRUPTED_DATA_OVERFLOW)) {
            XBProviderReader instance = new XBProviderReader(stream);
            try {
                XBListener listener = new XBPrintFilter();
                while (!instance.isFinished()) {
                    instance.produceXB(listener);
                }
            } catch (XBProcessingException | IOException e) {
                ex = e;
            }

            assertEquals(new XBParseException("Block overflow", XBProcessingExceptionType.BLOCK_OVERFLOW), ex);
        }
    }
}
