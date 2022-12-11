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
package org.exbin.xbup.parser_tree;

import junit.framework.TestCase;
import org.exbin.xbup.core.block.XBDocument;
import org.exbin.xbup.core.parser.basic.convert.XBPrintFilter;
import org.exbin.xbup.core.parser.data.XBCoreTestSampleData;
import org.exbin.xbup.core.test.XBTestUtils;
import org.junit.Test;

/**
 * Test class for XBTreeReader.
 *
 * @author ExBin Project (https://exbin.org)
 */
public class XBTreeReaderTest extends TestCase {

    public XBTreeReaderTest(String testName) {
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
     * Tests XBTreeReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleEmpty() throws Exception {
        XBDocument document = new XBTreeDocument(new XBTreeNode());
        XBTreeReader instance = new XBTreeReader(document);
        XBCoreTestSampleData.writeSampleEmpty(new XBPrintFilter(instance));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleEmptyTree();
        XBTestUtils.assertEqualsXBDocuments(expectedDocument, document);
    }

    /**
     * Tests XBTreeReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleBlock() throws Exception {
        XBDocument document = new XBTreeDocument(new XBTreeNode());
        XBTreeReader instance = new XBTreeReader(document);
        XBCoreTestSampleData.writeSampleBlock(new XBPrintFilter(instance));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleBlockTree();
        XBTestUtils.assertEqualsXBDocuments(expectedDocument, document);
    }

    /**
     * Tests XBTreeReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleBlockWithTail() throws Exception {
        XBDocument document = new XBTreeDocument(new XBTreeNode());
        XBTreeReader instance = new XBTreeReader(document);
        XBCoreTestSampleData.writeSampleBlockWithTail(new XBPrintFilter(instance));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleBlockWithTailTree();
        XBTestUtils.assertEqualsXBDocuments(expectedDocument, document);
    }

    /**
     * Tests XBTreeReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleBlockTerminated() throws Exception {
        XBDocument document = new XBTreeDocument(new XBTreeNode());
        XBTreeReader instance = new XBTreeReader(document);
        XBCoreTestSampleData.writeSampleBlockTerminated(new XBPrintFilter(instance));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleBlockTerminatedTree();
        XBTestUtils.assertEqualsXBDocuments(expectedDocument, document);
    }

    /**
     * Tests XBTreeReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleBlockTerminatedWithTail() throws Exception {
        XBDocument document = new XBTreeDocument(new XBTreeNode());
        XBTreeReader instance = new XBTreeReader(document);
        XBCoreTestSampleData.writeSampleBlockTerminatedWithTail(new XBPrintFilter(instance));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleBlockTerminatedWithTailTree();
        XBTestUtils.assertEqualsXBDocuments(expectedDocument, document);
    }

    /**
     * Tests XBTreeReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleData() throws Exception {
        XBDocument document = new XBTreeDocument(new XBTreeNode());
        XBTreeReader instance = new XBTreeReader(document);
        XBCoreTestSampleData.writeSampleData(new XBPrintFilter(instance));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleDataTree();
        XBTestUtils.assertEqualsXBDocuments(expectedDocument, document);
    }

    /**
     * Tests XBTreeReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleDataWithTail() throws Exception {
        XBDocument document = new XBTreeDocument(new XBTreeNode());
        XBTreeReader instance = new XBTreeReader(document);
        XBCoreTestSampleData.writeSampleDataWithTail(new XBPrintFilter(instance));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleDataWithTailTree();
        XBTestUtils.assertEqualsXBDocuments(expectedDocument, document);
    }

    /**
     * Tests XBTreeReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleDataTerminated() throws Exception {
        XBDocument document = new XBTreeDocument(new XBTreeNode());
        XBTreeReader instance = new XBTreeReader(document);
        XBCoreTestSampleData.writeSampleDataTerminated(new XBPrintFilter(instance));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleDataTerminatedTree();
        XBTestUtils.assertEqualsXBDocuments(expectedDocument, document);
    }

    /**
     * Tests XBTreeReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleDataTerminatedWithTail() throws Exception {
        XBDocument document = new XBTreeDocument(new XBTreeNode());
        XBTreeReader instance = new XBTreeReader(document);
        XBCoreTestSampleData.writeSampleDataTerminatedWithTail(new XBPrintFilter(instance));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleDataTerminatedWithTailTree();
        XBTestUtils.assertEqualsXBDocuments(expectedDocument, document);
    }

    /**
     * Tests XBTreeReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleBlockData() throws Exception {
        XBDocument document = new XBTreeDocument(new XBTreeNode());
        XBTreeReader instance = new XBTreeReader(document);
        XBCoreTestSampleData.writeSampleBlockData(new XBPrintFilter(instance));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleBlockDataTree();
        XBTestUtils.assertEqualsXBDocuments(expectedDocument, document);
    }

    /**
     * Tests XBTreeReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleBlockDataWithTail() throws Exception {
        XBDocument document = new XBTreeDocument(new XBTreeNode());
        XBTreeReader instance = new XBTreeReader(document);
        XBCoreTestSampleData.writeSampleBlockDataWithTail(new XBPrintFilter(instance));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleBlockDataWithTailTree();
        XBTestUtils.assertEqualsXBDocuments(expectedDocument, document);
    }

    /**
     * Tests XBTreeReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleBlockDataTerminated() throws Exception {
        XBDocument document = new XBTreeDocument(new XBTreeNode());
        XBTreeReader instance = new XBTreeReader(document);
        XBCoreTestSampleData.writeSampleBlockDataTerminated(new XBPrintFilter(instance));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleBlockDataTerminatedTree();
        XBTestUtils.assertEqualsXBDocuments(expectedDocument, document);
    }

    /**
     * Tests XBTreeReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleBlockDataTerminatedWithTail() throws Exception {
        XBDocument document = new XBTreeDocument(new XBTreeNode());
        XBTreeReader instance = new XBTreeReader(document);
        XBCoreTestSampleData.writeSampleBlockDataTerminatedWithTail(new XBPrintFilter(instance));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleBlockDataTerminatedWithTailTree();
        XBTestUtils.assertEqualsXBDocuments(expectedDocument, document);
    }

    /**
     * Tests XBTreeReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleBlockDataHybrid() throws Exception {
        XBDocument document = new XBTreeDocument(new XBTreeNode());
        XBTreeReader instance = new XBTreeReader(document);
        XBCoreTestSampleData.writeSampleBlockDataHybrid(new XBPrintFilter(instance));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleBlockDataHybridTree();
        XBTestUtils.assertEqualsXBDocuments(expectedDocument, document);
    }

    /**
     * Tests XBTreeReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleBlockDataHybrid2() throws Exception {
        XBDocument document = new XBTreeDocument(new XBTreeNode());
        XBTreeReader instance = new XBTreeReader(document);
        XBCoreTestSampleData.writeSampleBlockDataHybrid2(new XBPrintFilter(instance));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleBlockDataHybrid2Tree();
        XBTestUtils.assertEqualsXBDocuments(expectedDocument, document);
    }

    /**
     * Tests XBTreeReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleTwoBlocks() throws Exception {
        XBDocument document = new XBTreeDocument(new XBTreeNode());
        XBTreeReader instance = new XBTreeReader(document);
        XBCoreTestSampleData.writeSampleTwoBlocks(new XBPrintFilter(instance));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleTwoBlocksTree();
        XBTestUtils.assertEqualsXBDocuments(expectedDocument, document);
    }

    /**
     * Tests XBTreeReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleTwoBlocksWithTail() throws Exception {
        XBDocument document = new XBTreeDocument(new XBTreeNode());
        XBTreeReader instance = new XBTreeReader(document);
        XBCoreTestSampleData.writeSampleTwoBlocksWithTail(new XBPrintFilter(instance));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleTwoBlocksWithTailTree();
        XBTestUtils.assertEqualsXBDocuments(expectedDocument, document);
    }

    /**
     * Tests XBTreeReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleTwoBlocksTerminated() throws Exception {
        XBDocument document = new XBTreeDocument(new XBTreeNode());
        XBTreeReader instance = new XBTreeReader(document);
        XBCoreTestSampleData.writeSampleTwoBlocksTerminated(new XBPrintFilter(instance));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleTwoBlocksTerminatedTree();
        XBTestUtils.assertEqualsXBDocuments(expectedDocument, document);
    }

    /**
     * Tests XBTreeReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleTwoBlocksTerminatedWithTail() throws Exception {
        XBDocument document = new XBTreeDocument(new XBTreeNode());
        XBTreeReader instance = new XBTreeReader(document);
        XBCoreTestSampleData.writeSampleTwoBlocksTerminatedWithTail(new XBPrintFilter(instance));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleTwoBlocksTerminatedWithTailTree();
        XBTestUtils.assertEqualsXBDocuments(expectedDocument, document);
    }

    /**
     * Tests XBTreeReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleTwoBlocksHybrid() throws Exception {
        XBDocument document = new XBTreeDocument(new XBTreeNode());
        XBTreeReader instance = new XBTreeReader(document);
        XBCoreTestSampleData.writeSampleTwoBlocksHybrid(new XBPrintFilter(instance));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleTwoBlocksHybridTree();
        XBTestUtils.assertEqualsXBDocuments(expectedDocument, document);
    }

    /**
     * Tests XBTreeReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleTwoBlocksHybrid2() throws Exception {
        XBDocument document = new XBTreeDocument(new XBTreeNode());
        XBTreeReader instance = new XBTreeReader(document);
        XBCoreTestSampleData.writeSampleTwoBlocksHybrid2(new XBPrintFilter(instance));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleTwoBlocksHybrid2Tree();
        XBTestUtils.assertEqualsXBDocuments(expectedDocument, document);
    }

    /**
     * Tests XBTreeReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleSixBlocks() throws Exception {
        XBDocument document = new XBTreeDocument(new XBTreeNode());
        XBTreeReader instance = new XBTreeReader(document);
        XBCoreTestSampleData.writeSampleSixBlocks(new XBPrintFilter(instance));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleSixBlocksTree();
        XBTestUtils.assertEqualsXBDocuments(expectedDocument, document);
    }
}
