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
package org.exbin.xbup.core.parser.token.pull;

import org.exbin.xbup.core.parser.token.pull.XBPullReader;
import java.io.IOException;
import java.io.InputStream;
import junit.framework.TestCase;
import org.junit.Test;
import org.exbin.xbup.core.parser.XBParseException;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.parser.XBProcessingExceptionType;
import org.exbin.xbup.core.parser.basic.XBProducer;
import org.exbin.xbup.core.parser.basic.convert.XBConsumerToListener;
import org.exbin.xbup.core.parser.basic.convert.XBProviderToProducer;
import org.exbin.xbup.core.parser.data.XBCoreTestSampleData;
import org.exbin.xbup.core.parser.token.pull.convert.XBPullProviderToProvider;
import org.exbin.xbup.core.test.XBTestUtils.BufferAssertXBFilter;

/**
 * Test class for XBPullReader.
 *
 * @version 0.1.25 2015/08/10
 * @author ExBin Project (http://exbin.org)
 */
public class XBPullReaderTest extends TestCase {

    public XBPullReaderTest(String testName) {
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
     * Tests XBPullReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleEmpty() throws Exception {
        try (InputStream stream = XBPullReaderTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_EMPTY); XBPullReader instance = new XBPullReader(stream)) {
            BufferAssertXBFilter assertListener;
            XBConsumerToListener buffer = new XBConsumerToListener(null);
            XBCoreTestSampleData.writeSampleEmpty(buffer);
            assertListener = new BufferAssertXBFilter(buffer.getTokens());

            XBProducer producer = new XBProviderToProducer(new XBPullProviderToProvider(instance));
            producer.attachXBListener(assertListener);
            assertTrue(assertListener.isFinished());
        }
    }

    /**
     * Tests XBPullReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleBlock() throws Exception {
        try (InputStream stream = XBPullReaderTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_BLOCK); XBPullReader instance = new XBPullReader(stream)) {
            BufferAssertXBFilter assertListener;
            XBConsumerToListener buffer = new XBConsumerToListener(null);
            XBCoreTestSampleData.writeSampleBlock(buffer);
            assertListener = new BufferAssertXBFilter(buffer.getTokens());

            XBProducer producer = new XBProviderToProducer(new XBPullProviderToProvider(instance));
            producer.attachXBListener(assertListener);
            assertTrue(assertListener.isFinished());
        }
    }

    /**
     * Tests XBPullReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleBlockExtended() throws Exception {
        try (InputStream stream = XBPullReaderTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_BLOCK_EXTENDED); XBPullReader instance = new XBPullReader(stream)) {
            BufferAssertXBFilter assertListener;
            XBConsumerToListener buffer = new XBConsumerToListener(null);
            XBCoreTestSampleData.writeSampleBlockExtended(buffer);
            assertListener = new BufferAssertXBFilter(buffer.getTokens());

            XBProducer producer = new XBProviderToProducer(new XBPullProviderToProvider(instance));
            producer.attachXBListener(assertListener);
            assertTrue(assertListener.isFinished());
        }
    }

    /**
     * Tests XBPullReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleBlockTerminated() throws Exception {
        try (InputStream stream = XBPullReaderTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_BLOCK_TERMINATED); XBPullReader instance = new XBPullReader(stream)) {
            BufferAssertXBFilter assertListener;
            XBConsumerToListener buffer = new XBConsumerToListener(null);
            XBCoreTestSampleData.writeSampleBlockTerminated(buffer);
            assertListener = new BufferAssertXBFilter(buffer.getTokens());

            XBProducer producer = new XBProviderToProducer(new XBPullProviderToProvider(instance));
            producer.attachXBListener(assertListener);
            assertTrue(assertListener.isFinished());
        }
    }

    /**
     * Tests XBPullReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleBlockTerminatedExtended() throws Exception {
        try (InputStream stream = XBPullReaderTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_BLOCK_TERMINATED_EXTENDED); XBPullReader instance = new XBPullReader(stream)) {
            BufferAssertXBFilter assertListener;
            XBConsumerToListener buffer = new XBConsumerToListener(null);
            XBCoreTestSampleData.writeSampleBlockTerminatedExtended(buffer);
            assertListener = new BufferAssertXBFilter(buffer.getTokens());

            XBProducer producer = new XBProviderToProducer(new XBPullProviderToProvider(instance));
            producer.attachXBListener(assertListener);
            assertTrue(assertListener.isFinished());
        }
    }

    /**
     * Tests XBPullReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleData() throws Exception {
        try (InputStream stream = XBPullReaderTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_DATA); XBPullReader instance = new XBPullReader(stream)) {
            BufferAssertXBFilter assertListener;
            XBConsumerToListener buffer = new XBConsumerToListener(null);
            XBCoreTestSampleData.writeSampleData(buffer);
            assertListener = new BufferAssertXBFilter(buffer.getTokens());

            XBProducer producer = new XBProviderToProducer(new XBPullProviderToProvider(instance));
            producer.attachXBListener(assertListener);
            assertTrue(assertListener.isFinished());
        }
    }

    /**
     * Tests XBPullReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleDataExtended() throws Exception {
        try (InputStream stream = XBPullReaderTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_DATA_EXTENDED); XBPullReader instance = new XBPullReader(stream)) {
            BufferAssertXBFilter assertListener;
            XBConsumerToListener buffer = new XBConsumerToListener(null);
            XBCoreTestSampleData.writeSampleDataExtended(buffer);
            assertListener = new BufferAssertXBFilter(buffer.getTokens());

            XBProducer producer = new XBProviderToProducer(new XBPullProviderToProvider(instance));
            producer.attachXBListener(assertListener);
            assertTrue(assertListener.isFinished());
        }
    }

    /**
     * Tests XBPullReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleDataTerminated() throws Exception {
        try (InputStream stream = XBPullReaderTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_DATA_TERMINATED); XBPullReader instance = new XBPullReader(stream)) {
            BufferAssertXBFilter assertListener;
            XBConsumerToListener buffer = new XBConsumerToListener(null);
            XBCoreTestSampleData.writeSampleDataTerminated(buffer);
            assertListener = new BufferAssertXBFilter(buffer.getTokens());

            XBProducer producer = new XBProviderToProducer(new XBPullProviderToProvider(instance));
            producer.attachXBListener(assertListener);
            assertTrue(assertListener.isFinished());
        }
    }

    /**
     * Tests XBPullReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleDataTerminatedExtended() throws Exception {
        try (InputStream stream = XBPullReaderTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_DATA_TERMINATED_EXTENDED); XBPullReader instance = new XBPullReader(stream)) {
            BufferAssertXBFilter assertListener;
            XBConsumerToListener buffer = new XBConsumerToListener(null);
            XBCoreTestSampleData.writeSampleDataTerminatedExtended(buffer);
            assertListener = new BufferAssertXBFilter(buffer.getTokens());

            XBProducer producer = new XBProviderToProducer(new XBPullProviderToProvider(instance));
            producer.attachXBListener(assertListener);
            assertTrue(assertListener.isFinished());
        }
    }

    /**
     * Tests XBPullReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleBlockData() throws Exception {
        try (InputStream stream = XBPullReaderTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_BLOCK_DATA); XBPullReader instance = new XBPullReader(stream)) {
            BufferAssertXBFilter assertListener;
            XBConsumerToListener buffer = new XBConsumerToListener(null);
            XBCoreTestSampleData.writeSampleBlockData(buffer);
            assertListener = new BufferAssertXBFilter(buffer.getTokens());

            XBProducer producer = new XBProviderToProducer(new XBPullProviderToProvider(instance));
            producer.attachXBListener(assertListener);
            assertTrue(assertListener.isFinished());
        }
    }

    /**
     * Tests XBPullReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleBlockDataExtended() throws Exception {
        try (InputStream stream = XBPullReaderTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_BLOCK_DATA_EXTENDED); XBPullReader instance = new XBPullReader(stream)) {
            BufferAssertXBFilter assertListener;
            XBConsumerToListener buffer = new XBConsumerToListener(null);
            XBCoreTestSampleData.writeSampleBlockDataExtended(buffer);
            assertListener = new BufferAssertXBFilter(buffer.getTokens());

            XBProducer producer = new XBProviderToProducer(new XBPullProviderToProvider(instance));
            producer.attachXBListener(assertListener);
            assertTrue(assertListener.isFinished());
        }
    }

    /**
     * Tests XBPullReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleBlockDataTerminated() throws Exception {
        try (InputStream stream = XBPullReaderTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_BLOCK_DATA_TERMINATED); XBPullReader instance = new XBPullReader(stream)) {
            BufferAssertXBFilter assertListener;
            XBConsumerToListener buffer = new XBConsumerToListener(null);
            XBCoreTestSampleData.writeSampleBlockDataTerminated(buffer);
            assertListener = new BufferAssertXBFilter(buffer.getTokens());

            XBProducer producer = new XBProviderToProducer(new XBPullProviderToProvider(instance));
            producer.attachXBListener(assertListener);
            assertTrue(assertListener.isFinished());
        }
    }

    /**
     * Tests XBPullReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleBlockDataTerminatedExtended() throws Exception {
        try (InputStream stream = XBPullReaderTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_BLOCK_DATA_TERMINATED_EXTENDED); XBPullReader instance = new XBPullReader(stream)) {
            BufferAssertXBFilter assertListener;
            XBConsumerToListener buffer = new XBConsumerToListener(null);
            XBCoreTestSampleData.writeSampleBlockDataTerminatedExtended(buffer);
            assertListener = new BufferAssertXBFilter(buffer.getTokens());

            XBProducer producer = new XBProviderToProducer(new XBPullProviderToProvider(instance));
            producer.attachXBListener(assertListener);
            assertTrue(assertListener.isFinished());
        }
    }

    /**
     * Tests XBPullReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleBlockDataHybrid() throws Exception {
        try (InputStream stream = XBPullReaderTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_BLOCK_DATA_HYBRID); XBPullReader instance = new XBPullReader(stream)) {
            BufferAssertXBFilter assertListener;
            XBConsumerToListener buffer = new XBConsumerToListener(null);
            XBCoreTestSampleData.writeSampleBlockDataHybrid(buffer);
            assertListener = new BufferAssertXBFilter(buffer.getTokens());

            XBProducer producer = new XBProviderToProducer(new XBPullProviderToProvider(instance));
            producer.attachXBListener(assertListener);
            assertTrue(assertListener.isFinished());
        }
    }

    /**
     * Tests XBPullReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleBlockDataHybrid2() throws Exception {
        try (InputStream stream = XBPullReaderTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_BLOCK_DATA_HYBRID2); XBPullReader instance = new XBPullReader(stream)) {
            BufferAssertXBFilter assertListener;
            XBConsumerToListener buffer = new XBConsumerToListener(null);
            XBCoreTestSampleData.writeSampleBlockDataHybrid2(buffer);
            assertListener = new BufferAssertXBFilter(buffer.getTokens());

            XBProducer producer = new XBProviderToProducer(new XBPullProviderToProvider(instance));
            producer.attachXBListener(assertListener);
            assertTrue(assertListener.isFinished());
        }
    }

    /**
     * Tests XBPullReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleTwoBlocks() throws Exception {
        try (InputStream stream = XBPullReaderTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_TWO_BLOCKS); XBPullReader instance = new XBPullReader(stream)) {
            BufferAssertXBFilter assertListener;
            XBConsumerToListener buffer = new XBConsumerToListener(null);
            XBCoreTestSampleData.writeSampleTwoBlocks(buffer);
            assertListener = new BufferAssertXBFilter(buffer.getTokens());

            XBProducer producer = new XBProviderToProducer(new XBPullProviderToProvider(instance));
            producer.attachXBListener(assertListener);
            assertTrue(assertListener.isFinished());
        }
    }

    /**
     * Tests XBPullReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleTwoBlocksExtended() throws Exception {
        try (InputStream stream = XBPullReaderTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_TWO_BLOCKS_EXTENDED); XBPullReader instance = new XBPullReader(stream)) {
            BufferAssertXBFilter assertListener;
            XBConsumerToListener buffer = new XBConsumerToListener(null);
            XBCoreTestSampleData.writeSampleTwoBlocksExtended(buffer);
            assertListener = new BufferAssertXBFilter(buffer.getTokens());

            XBProducer producer = new XBProviderToProducer(new XBPullProviderToProvider(instance));
            producer.attachXBListener(assertListener);
            assertTrue(assertListener.isFinished());
        }
    }

    /**
     * Tests XBPullReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleTwoBlocksTerminated() throws Exception {
        try (InputStream stream = XBPullReaderTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_TWO_BLOCKS_TERMINATED); XBPullReader instance = new XBPullReader(stream)) {
            BufferAssertXBFilter assertListener;
            XBConsumerToListener buffer = new XBConsumerToListener(null);
            XBCoreTestSampleData.writeSampleTwoBlocksTerminated(buffer);
            assertListener = new BufferAssertXBFilter(buffer.getTokens());

            XBProducer producer = new XBProviderToProducer(new XBPullProviderToProvider(instance));
            producer.attachXBListener(assertListener);
            assertTrue(assertListener.isFinished());
        }
    }

    /**
     * Tests XBPullReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleTwoBlocksTerminatedExtended() throws Exception {
        try (InputStream stream = XBPullReaderTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_TWO_BLOCKS_TERMINATED_EXTENDED); XBPullReader instance = new XBPullReader(stream)) {
            BufferAssertXBFilter assertListener;
            XBConsumerToListener buffer = new XBConsumerToListener(null);
            XBCoreTestSampleData.writeSampleTwoBlocksTerminatedExtended(buffer);
            assertListener = new BufferAssertXBFilter(buffer.getTokens());

            XBProducer producer = new XBProviderToProducer(new XBPullProviderToProvider(instance));
            producer.attachXBListener(assertListener);
            assertTrue(assertListener.isFinished());
        }
    }

    /**
     * Tests XBPullReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleTwoBlocksHybrid() throws Exception {
        try (InputStream stream = XBPullReaderTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_TWO_BLOCKS_HYBRID); XBPullReader instance = new XBPullReader(stream)) {
            BufferAssertXBFilter assertListener;
            XBConsumerToListener buffer = new XBConsumerToListener(null);
            XBCoreTestSampleData.writeSampleTwoBlocksHybrid(buffer);
            assertListener = new BufferAssertXBFilter(buffer.getTokens());

            XBProducer producer = new XBProviderToProducer(new XBPullProviderToProvider(instance));
            producer.attachXBListener(assertListener);
            assertTrue(assertListener.isFinished());
        }
    }

    /**
     * Tests XBPullReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleTwoBlocksHybrid2() throws Exception {
        try (InputStream stream = XBPullReaderTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_TWO_BLOCKS_HYBRID2); XBPullReader instance = new XBPullReader(stream)) {
            BufferAssertXBFilter assertListener;
            XBConsumerToListener buffer = new XBConsumerToListener(null);
            XBCoreTestSampleData.writeSampleTwoBlocksHybrid2(buffer);
            assertListener = new BufferAssertXBFilter(buffer.getTokens());

            XBProducer producer = new XBProviderToProducer(new XBPullProviderToProvider(instance));
            producer.attachXBListener(assertListener);
            assertTrue(assertListener.isFinished());
        }
    }

    /**
     * Tests XBPullReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleSixBlocks() throws Exception {
        try (InputStream stream = XBPullReaderTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_SIX_BLOCKS); XBPullReader instance = new XBPullReader(stream)) {
            BufferAssertXBFilter assertListener;
            XBConsumerToListener buffer = new XBConsumerToListener(null);
            XBCoreTestSampleData.writeSampleSixBlocks(buffer);
            assertListener = new BufferAssertXBFilter(buffer.getTokens());

            XBProducer producer = new XBProviderToProducer(new XBPullProviderToProvider(instance));
            producer.attachXBListener(assertListener);
            assertTrue(assertListener.isFinished());
        }
    }

    /**
     * Tests XBPullReader class reading corrupted empty file.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadCorruptedEmptyFile() throws Exception {
        Throwable ex = null;

        try (InputStream stream = XBPullReaderTest.class.getResourceAsStream(XBCoreTestSampleData.CORRUPTED_EMPTY)) {
            XBPullReader instance = new XBPullReader(stream);
            try {
                instance.pullXBToken();
            } catch (XBProcessingException | IOException e) {
                ex = e;
            }

            assertEquals(new XBProcessingException("End of data reached", XBProcessingExceptionType.UNEXPECTED_END_OF_STREAM), ex);
        }
    }

    /**
     * Tests XBPullReader class reading corrupted single byte.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadCorruptedSingleByte() throws Exception {
        Throwable ex = null;
        try (InputStream stream = XBPullReaderTest.class.getResourceAsStream(XBCoreTestSampleData.CORRUPTED_SINGLE_BYTE)) {
            XBPullReader instance = new XBPullReader(stream);
            try {
                instance.pullXBToken();
            } catch (XBProcessingException | IOException e) {
                ex = e;
            }

            assertEquals(new XBProcessingException("End of data reached", XBProcessingExceptionType.UNEXPECTED_END_OF_STREAM), ex);
        }
    }

    /**
     * Tests XBPullReader class reading corrupted wrong header.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadCorruptedWrongHeader() throws Exception {
        Throwable ex = null;

        try (InputStream stream = XBPullReaderTest.class.getResourceAsStream(XBCoreTestSampleData.CORRUPTED_WRONG_HEADER)) {
            XBPullReader instance = new XBPullReader(stream);
            try {
                instance.pullXBToken();
            } catch (XBProcessingException | IOException e) {
                ex = e;
            }

            assertEquals(new XBParseException("Unsupported header: 0xfe0059420002", XBProcessingExceptionType.CORRUPTED_HEADER), ex);
        }
    }

    /**
     * Tests XBPullReader class reading corrupted file.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadCorruptedIncompleteBlock() throws Exception {
        Throwable ex = null;

        try (InputStream stream = XBPullReaderTest.class.getResourceAsStream(XBCoreTestSampleData.CORRUPTED_INCOMPLETE_BLOCK)) {
            XBPullReader instance = new XBPullReader(stream);
            try {
                while (!instance.isFinishedXB()) {
                    instance.pullXBToken();
                }
            } catch (XBProcessingException | IOException e) {
                ex = e;
            }

            assertEquals(new XBParseException("End of data reached", XBProcessingExceptionType.UNEXPECTED_END_OF_STREAM), ex);
        }
    }

    /**
     * Tests XBPullReader class reading corrupted file.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadCorruptedIncompleteBlock2() throws Exception {
        Throwable ex = null;

        try (InputStream stream = XBPullReaderTest.class.getResourceAsStream(XBCoreTestSampleData.CORRUPTED_INCOMPLETE_BLOCK2)) {
            XBPullReader instance = new XBPullReader(stream);
            try {
                while (!instance.isFinishedXB()) {
                    instance.pullXBToken();
                }
            } catch (XBProcessingException | IOException e) {
                ex = e;
            }

            assertEquals(new XBParseException("End of data reached", XBProcessingExceptionType.UNEXPECTED_END_OF_STREAM), ex);
        }
    }

    /**
     * Tests XBPullReader class reading corrupted file.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadCorruptedIncompleteBlockTerminated() throws Exception {
        Throwable ex = null;

        try (InputStream stream = XBPullReaderTest.class.getResourceAsStream(XBCoreTestSampleData.CORRUPTED_INCOMPLETE_BLOCK_TERMINATED)) {
            XBPullReader instance = new XBPullReader(stream);
            try {
                while (!instance.isFinishedXB()) {
                    instance.pullXBToken();
                }
            } catch (XBProcessingException | IOException e) {
                ex = e;
            }

            assertEquals(new XBParseException("End of data reached", XBProcessingExceptionType.UNEXPECTED_END_OF_STREAM), ex);
        }
    }

    /**
     * Tests XBPullReader class reading corrupted file.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadCorruptedIncompleteData() throws Exception {
        Throwable ex = null;

        try (InputStream stream = XBPullReaderTest.class.getResourceAsStream(XBCoreTestSampleData.CORRUPTED_INCOMPLETE_DATA)) {
            XBPullReader instance = new XBPullReader(stream);
            try {
                while (!instance.isFinishedXB()) {
                    instance.pullXBToken();
                }
            } catch (XBProcessingException | IOException e) {
                ex = e;
            }

            assertEquals(new XBParseException("Unexpected end of stream", XBProcessingExceptionType.UNEXPECTED_END_OF_STREAM), ex);
        }
    }

    /**
     * Tests XBPullReader class reading corrupted file.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadCorruptedIncompleteDataTerminated() throws Exception {
        Throwable ex = null;

        try (InputStream stream = XBPullReaderTest.class.getResourceAsStream(XBCoreTestSampleData.CORRUPTED_INCOMPLETE_DATA_TERMINATED)) {
            XBPullReader instance = new XBPullReader(stream);
            try {
                while (!instance.isFinishedXB()) {
                    instance.pullXBToken();
                }
            } catch (XBProcessingException | IOException e) {
                ex = e;
            }

            assertEquals(new XBParseException("Missing data block terminator", XBProcessingExceptionType.UNEXPECTED_END_OF_STREAM), ex);
        }
    }

    /**
     * Tests XBPullReader class reading corrupted file.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadCorruptedUnexpectedTerminator() throws Exception {
        Throwable ex = null;

        try (InputStream stream = XBPullReaderTest.class.getResourceAsStream(XBCoreTestSampleData.CORRUPTED_UNEXPECTED_TERMINATOR)) {
            XBPullReader instance = new XBPullReader(stream);
            try {
                while (!instance.isFinishedXB()) {
                    instance.pullXBToken();
                }
            } catch (XBProcessingException | IOException e) {
                ex = e;
            }

            assertEquals(new XBParseException("Unexpected terminator", XBProcessingExceptionType.UNEXPECTED_TERMINATOR), ex);
        }
    }

    /**
     * Tests XBPullReader class reading corrupted file.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadCorruptedUnexpectedTerminator2() throws Exception {
        Throwable ex = null;

        try (InputStream stream = XBPullReaderTest.class.getResourceAsStream(XBCoreTestSampleData.CORRUPTED_UNEXPECTED_TERMINATOR2)) {
            XBPullReader instance = new XBPullReader(stream);
            try {
                while (!instance.isFinishedXB()) {
                    instance.pullXBToken();
                }
            } catch (XBProcessingException | IOException e) {
                ex = e;
            }

            assertEquals(new XBParseException("Unexpected terminator", XBProcessingExceptionType.UNEXPECTED_TERMINATOR), ex);
        }
    }

    /**
     * Tests XBPullReader class reading corrupted file.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadCorruptedChildOverflow() throws Exception {
        Throwable ex = null;

        try (InputStream stream = XBPullReaderTest.class.getResourceAsStream(XBCoreTestSampleData.CORRUPTED_CHILD_OVERFLOW)) {
            XBPullReader instance = new XBPullReader(stream);
            try {
                while (!instance.isFinishedXB()) {
                    instance.pullXBToken();
                }
            } catch (XBProcessingException | IOException e) {
                ex = e;
            }

            assertEquals(new XBParseException("Block overflow", XBProcessingExceptionType.BLOCK_OVERFLOW), ex);
        }
    }

    /**
     * Tests XBPullReader class reading corrupted file.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadCorruptedAttributeOverflow() throws Exception {
        Throwable ex = null;

        try (InputStream stream = XBPullReaderTest.class.getResourceAsStream(XBCoreTestSampleData.CORRUPTED_ATTRIBUTE_OVERFLOW)) {
            XBPullReader instance = new XBPullReader(stream);
            try {
                while (!instance.isFinishedXB()) {
                    instance.pullXBToken();
                }
            } catch (XBProcessingException | IOException e) {
                ex = e;
            }

            assertEquals(new XBParseException("Attribute overflow", XBProcessingExceptionType.ATTRIBUTE_OVERFLOW), ex);
        }
    }

    /**
     * Tests XBPullReader class reading corrupted file.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadCorruptedDataOverflow() throws Exception {
        Throwable ex = null;

        try (InputStream stream = XBPullReaderTest.class.getResourceAsStream(XBCoreTestSampleData.CORRUPTED_DATA_OVERFLOW)) {
            XBPullReader instance = new XBPullReader(stream);
            try {
                while (!instance.isFinishedXB()) {
                    instance.pullXBToken();
                }
            } catch (XBProcessingException | IOException e) {
                ex = e;
            }

            assertEquals(new XBParseException("Block overflow", XBProcessingExceptionType.BLOCK_OVERFLOW), ex);
        }
    }
}