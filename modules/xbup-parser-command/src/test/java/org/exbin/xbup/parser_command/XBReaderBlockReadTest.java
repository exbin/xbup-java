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
package org.exbin.xbup.parser_command;

import org.exbin.xbup.parser_command.XBReader;
import junit.framework.TestCase;
import org.junit.Test;
import org.exbin.xbup.core.block.XBDocument;
import org.exbin.xbup.core.parser.data.XBCoreTestSampleData;
import org.exbin.xbup.core.test.XBTestUtils;
import static org.exbin.xbup.parser_command.XBCommandParserTestUtils.getResourceAsSeekableStream;

/**
 * Test class for XBTreeDocument.
 *
 * @version 0.2.0 2015/10/01
 * @author ExBin Project (http://exbin.org)
 */
public class XBReaderBlockReadTest extends TestCase {

    public XBReaderBlockReadTest(String testName) {
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
     * Tests XBReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleEmpty() throws Exception {
        XBReader instance = new XBReader();
        instance.open(getResourceAsSeekableStream(XBCoreTestSampleData.SAMPLE_EMPTY));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleEmptyTree();
        XBTestUtils.assertEqualsXBDocuments(expectedDocument, instance);
    }

    /**
     * Tests XBReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleBlock() throws Exception {
        XBReader instance = new XBReader();
        instance.open(getResourceAsSeekableStream(XBCoreTestSampleData.SAMPLE_BLOCK));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleBlockTree();
        XBTestUtils.assertEqualsXBDocuments(expectedDocument, instance);
    }

    /**
     * Tests XBReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleBlockExtended() throws Exception {
        XBReader instance = new XBReader();
        instance.open(getResourceAsSeekableStream(XBCoreTestSampleData.SAMPLE_BLOCK_EXTENDED));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleBlockExtendedTree();
        XBTestUtils.assertEqualsXBDocuments(expectedDocument, instance);
    }

    /**
     * Tests XBReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleBlockTerminated() throws Exception {
        XBReader instance = new XBReader();
        instance.open(getResourceAsSeekableStream(XBCoreTestSampleData.SAMPLE_BLOCK_TERMINATED));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleBlockTerminatedTree();
        XBTestUtils.assertEqualsXBDocuments(expectedDocument, instance);
    }

    /**
     * Tests XBReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleBlockTerminatedExtended() throws Exception {
        XBReader instance = new XBReader();
        instance.open(getResourceAsSeekableStream(XBCoreTestSampleData.SAMPLE_BLOCK_TERMINATED_EXTENDED));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleBlockTerminatedExtendedTree();
        XBTestUtils.assertEqualsXBDocuments(expectedDocument, instance);
    }

    /**
     * Tests XBReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleData() throws Exception {
        XBReader instance = new XBReader();
        instance.open(getResourceAsSeekableStream(XBCoreTestSampleData.SAMPLE_DATA));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleDataTree();
        XBTestUtils.assertEqualsXBDocuments(expectedDocument, instance);
    }

    /**
     * Tests XBReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleDataExtended() throws Exception {
        XBReader instance = new XBReader();
        instance.open(getResourceAsSeekableStream(XBCoreTestSampleData.SAMPLE_DATA_EXTENDED));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleDataExtendedTree();
        XBTestUtils.assertEqualsXBDocuments(expectedDocument, instance);
    }

    /**
     * Tests XBReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleDataTerminated() throws Exception {
        XBReader instance = new XBReader();
        instance.open(getResourceAsSeekableStream(XBCoreTestSampleData.SAMPLE_DATA_TERMINATED));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleDataTerminatedTree();
        XBTestUtils.assertEqualsXBDocuments(expectedDocument, instance);
    }

    /**
     * Tests XBReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleDataTerminatedExtended() throws Exception {
        XBReader instance = new XBReader();
        instance.open(getResourceAsSeekableStream(XBCoreTestSampleData.SAMPLE_DATA_TERMINATED_EXTENDED));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleDataTerminatedExtendedTree();
        XBTestUtils.assertEqualsXBDocuments(expectedDocument, instance);
    }

    /**
     * Tests XBReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleBlockData() throws Exception {
        XBReader instance = new XBReader();
        instance.open(getResourceAsSeekableStream(XBCoreTestSampleData.SAMPLE_BLOCK_DATA));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleBlockDataTree();
        XBTestUtils.assertEqualsXBDocuments(expectedDocument, instance);
    }

    /**
     * Tests XBReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleBlockDataExtended() throws Exception {
        XBReader instance = new XBReader();
        instance.open(getResourceAsSeekableStream(XBCoreTestSampleData.SAMPLE_BLOCK_DATA_EXTENDED));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleBlockDataExtendedTree();
        XBTestUtils.assertEqualsXBDocuments(expectedDocument, instance);
    }

    /**
     * Tests XBReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleBlockDataTerminated() throws Exception {
        XBReader instance = new XBReader();
        instance.open(getResourceAsSeekableStream(XBCoreTestSampleData.SAMPLE_BLOCK_DATA_TERMINATED));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleBlockDataTerminatedTree();
        XBTestUtils.assertEqualsXBDocuments(expectedDocument, instance);
    }

    /**
     * Tests XBReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleBlockDataTerminatedExtended() throws Exception {
        XBReader instance = new XBReader();
        instance.open(getResourceAsSeekableStream(XBCoreTestSampleData.SAMPLE_BLOCK_DATA_TERMINATED_EXTENDED));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleBlockDataTerminatedExtendedTree();
        XBTestUtils.assertEqualsXBDocuments(expectedDocument, instance);
    }

    /**
     * Tests XBReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleBlockDataHybrid() throws Exception {
        XBReader instance = new XBReader();
        instance.open(getResourceAsSeekableStream(XBCoreTestSampleData.SAMPLE_BLOCK_DATA_HYBRID));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleBlockDataHybridTree();
        XBTestUtils.assertEqualsXBDocuments(expectedDocument, instance);
    }

    /**
     * Tests XBReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleBlockDataHybrid2() throws Exception {
        XBReader instance = new XBReader();
        instance.open(getResourceAsSeekableStream(XBCoreTestSampleData.SAMPLE_BLOCK_DATA_HYBRID2));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleBlockDataHybrid2Tree();
        XBTestUtils.assertEqualsXBDocuments(expectedDocument, instance);
    }

    /**
     * Tests XBReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleTwoBlocks() throws Exception {
        XBReader instance = new XBReader();
        instance.open(getResourceAsSeekableStream(XBCoreTestSampleData.SAMPLE_TWO_BLOCKS));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleTwoBlocksTree();
        XBTestUtils.assertEqualsXBDocuments(expectedDocument, instance);
    }

    /**
     * Tests XBReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleTwoBlocksExtended() throws Exception {
        XBReader instance = new XBReader();
        instance.open(getResourceAsSeekableStream(XBCoreTestSampleData.SAMPLE_TWO_BLOCKS_EXTENDED));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleTwoBlocksExtendedTree();
        XBTestUtils.assertEqualsXBDocuments(expectedDocument, instance);
    }

    /**
     * Tests XBReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleTwoBlocksTerminated() throws Exception {
        XBReader instance = new XBReader();
        instance.open(getResourceAsSeekableStream(XBCoreTestSampleData.SAMPLE_TWO_BLOCKS_TERMINATED));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleTwoBlocksTerminatedTree();
        XBTestUtils.assertEqualsXBDocuments(expectedDocument, instance);
    }

    /**
     * Tests XBReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleTwoBlocksTerminatedExtended() throws Exception {
        XBReader instance = new XBReader();
        instance.open(getResourceAsSeekableStream(XBCoreTestSampleData.SAMPLE_TWO_BLOCKS_TERMINATED_EXTENDED));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleTwoBlocksTerminatedExtendedTree();
        XBTestUtils.assertEqualsXBDocuments(expectedDocument, instance);
    }

    /**
     * Tests XBReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleTwoBlocksHybrid() throws Exception {
        XBReader instance = new XBReader();
        instance.open(getResourceAsSeekableStream(XBCoreTestSampleData.SAMPLE_TWO_BLOCKS_HYBRID));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleTwoBlocksHybridTree();
        XBTestUtils.assertEqualsXBDocuments(expectedDocument, instance);
    }

    /**
     * Tests XBReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleTwoBlocksHybrid2() throws Exception {
        XBReader instance = new XBReader();
        instance.open(getResourceAsSeekableStream(XBCoreTestSampleData.SAMPLE_TWO_BLOCKS_HYBRID2));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleTwoBlocksHybrid2Tree();
        XBTestUtils.assertEqualsXBDocuments(expectedDocument, instance);
    }

    /**
     * Tests XBReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleSixBlocks() throws Exception {
        XBReader instance = new XBReader();
        instance.open(getResourceAsSeekableStream(XBCoreTestSampleData.SAMPLE_SIX_BLOCKS));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleSixBlocksTree();
        XBTestUtils.assertEqualsXBDocuments(expectedDocument, instance);
    }
}