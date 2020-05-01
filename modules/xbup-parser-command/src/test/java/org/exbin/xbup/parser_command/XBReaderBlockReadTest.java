/*
 * Copyright (C) ExBin Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.exbin.xbup.parser_command;

import junit.framework.TestCase;
import org.exbin.xbup.core.block.XBDocument;
import org.exbin.xbup.core.parser.data.XBCoreTestSampleData;
import org.exbin.xbup.core.test.XBTestUtils;
import org.junit.Test;

/**
 * Test class for XBReader.
 *
 * @version 0.2.0 2017/01/18
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
        instance.open(XBCommandParserTestUtils.getResourceAsSeekableStream(XBCoreTestSampleData.SAMPLE_EMPTY));

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
        instance.open(XBCommandParserTestUtils.getResourceAsSeekableStream(XBCoreTestSampleData.SAMPLE_BLOCK));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleBlockTree();
        XBTestUtils.assertEqualsXBDocuments(expectedDocument, instance);
    }

    /**
     * Tests XBReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleBlockWithTail() throws Exception {
        XBReader instance = new XBReader();
        instance.open(XBCommandParserTestUtils.getResourceAsSeekableStream(XBCoreTestSampleData.SAMPLE_BLOCK_WITH_TAIL));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleBlockWithTailTree();
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
        instance.open(XBCommandParserTestUtils.getResourceAsSeekableStream(XBCoreTestSampleData.SAMPLE_BLOCK_TERMINATED));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleBlockTerminatedTree();
        XBTestUtils.assertEqualsXBDocuments(expectedDocument, instance);
    }

    /**
     * Tests XBReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleBlockTerminatedWithTail() throws Exception {
        XBReader instance = new XBReader();
        instance.open(XBCommandParserTestUtils.getResourceAsSeekableStream(XBCoreTestSampleData.SAMPLE_BLOCK_TERMINATED_WITH_TAIL));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleBlockTerminatedWithTailTree();
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
        instance.open(XBCommandParserTestUtils.getResourceAsSeekableStream(XBCoreTestSampleData.SAMPLE_DATA));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleDataTree();
        XBTestUtils.assertEqualsXBDocuments(expectedDocument, instance);
    }

    /**
     * Tests XBReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleDataWithTail() throws Exception {
        XBReader instance = new XBReader();
        instance.open(XBCommandParserTestUtils.getResourceAsSeekableStream(XBCoreTestSampleData.SAMPLE_DATA_WITH_TAIL));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleDataWithTailTree();
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
        instance.open(XBCommandParserTestUtils.getResourceAsSeekableStream(XBCoreTestSampleData.SAMPLE_DATA_TERMINATED));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleDataTerminatedTree();
        XBTestUtils.assertEqualsXBDocuments(expectedDocument, instance);
    }

    /**
     * Tests XBReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleDataTerminatedWithTail() throws Exception {
        XBReader instance = new XBReader();
        instance.open(XBCommandParserTestUtils.getResourceAsSeekableStream(XBCoreTestSampleData.SAMPLE_DATA_TERMINATED_WITH_TAIL));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleDataTerminatedWithTailTree();
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
        instance.open(XBCommandParserTestUtils.getResourceAsSeekableStream(XBCoreTestSampleData.SAMPLE_BLOCK_DATA));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleBlockDataTree();
        XBTestUtils.assertEqualsXBDocuments(expectedDocument, instance);
    }

    /**
     * Tests XBReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleBlockDataWithTail() throws Exception {
        XBReader instance = new XBReader();
        instance.open(XBCommandParserTestUtils.getResourceAsSeekableStream(XBCoreTestSampleData.SAMPLE_BLOCK_DATA_WITH_TAIL));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleBlockDataWithTailTree();
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
        instance.open(XBCommandParserTestUtils.getResourceAsSeekableStream(XBCoreTestSampleData.SAMPLE_BLOCK_DATA_TERMINATED));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleBlockDataTerminatedTree();
        XBTestUtils.assertEqualsXBDocuments(expectedDocument, instance);
    }

    /**
     * Tests XBReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleBlockDataTerminatedWithTail() throws Exception {
        XBReader instance = new XBReader();
        instance.open(XBCommandParserTestUtils.getResourceAsSeekableStream(XBCoreTestSampleData.SAMPLE_BLOCK_DATA_TERMINATED_WITH_TAIL));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleBlockDataTerminatedWithTailTree();
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
        instance.open(XBCommandParserTestUtils.getResourceAsSeekableStream(XBCoreTestSampleData.SAMPLE_BLOCK_DATA_HYBRID));

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
        instance.open(XBCommandParserTestUtils.getResourceAsSeekableStream(XBCoreTestSampleData.SAMPLE_BLOCK_DATA_HYBRID2));

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
        instance.open(XBCommandParserTestUtils.getResourceAsSeekableStream(XBCoreTestSampleData.SAMPLE_TWO_BLOCKS));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleTwoBlocksTree();
        XBTestUtils.assertEqualsXBDocuments(expectedDocument, instance);
    }

    /**
     * Tests XBReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleTwoBlocksWithTail() throws Exception {
        XBReader instance = new XBReader();
        instance.open(XBCommandParserTestUtils.getResourceAsSeekableStream(XBCoreTestSampleData.SAMPLE_TWO_BLOCKS_WITH_TAIL));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleTwoBlocksWithTailTree();
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
        instance.open(XBCommandParserTestUtils.getResourceAsSeekableStream(XBCoreTestSampleData.SAMPLE_TWO_BLOCKS_TERMINATED));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleTwoBlocksTerminatedTree();
        XBTestUtils.assertEqualsXBDocuments(expectedDocument, instance);
    }

    /**
     * Tests XBReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleTwoBlocksTerminatedWithTail() throws Exception {
        XBReader instance = new XBReader();
        instance.open(XBCommandParserTestUtils.getResourceAsSeekableStream(XBCoreTestSampleData.SAMPLE_TWO_BLOCKS_TERMINATED_WITH_TAIL));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleTwoBlocksTerminatedWithTailTree();
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
        instance.open(XBCommandParserTestUtils.getResourceAsSeekableStream(XBCoreTestSampleData.SAMPLE_TWO_BLOCKS_HYBRID));

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
        instance.open(XBCommandParserTestUtils.getResourceAsSeekableStream(XBCoreTestSampleData.SAMPLE_TWO_BLOCKS_HYBRID2));

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
        instance.open(XBCommandParserTestUtils.getResourceAsSeekableStream(XBCoreTestSampleData.SAMPLE_SIX_BLOCKS));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleSixBlocksTree();
        XBTestUtils.assertEqualsXBDocuments(expectedDocument, instance);
    }
}
