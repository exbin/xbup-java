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
package org.exbin.xbup.parser_tree;

import junit.framework.TestCase;
import org.exbin.xbup.core.block.XBDocument;
import org.exbin.xbup.core.block.XBTDocument;
import org.exbin.xbup.core.parser.basic.convert.XBPrintFilter;
import org.exbin.xbup.core.parser.basic.convert.XBToXBTConvertor;
import org.exbin.xbup.core.parser.data.XBCoreTestSampleData;
import org.exbin.xbup.core.test.XBTestUtils;
import org.junit.Test;

/**
 * Test class for XBTTreeReader.
 *
 * @version 0.2.0 2016/09/25
 * @author ExBin Project (http://exbin.org)
 */
public class XBTTreeReaderTest extends TestCase {

    public XBTTreeReaderTest(String testName) {
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
     * Tests XBTTreeReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleEmpty() throws Exception {
        XBTDocument document = new XBTTreeDocument(new XBTTreeNode());
        XBTTreeReader instance = new XBTTreeReader(document);
        XBCoreTestSampleData.writeSampleEmpty(new XBPrintFilter(new XBToXBTConvertor(instance)));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleEmptyTree();
        XBTestUtils.assertEqualsXBDocuments(expectedDocument, new XBTDocumentToXBDocument(document));
    }

    /**
     * Tests XBTTreeReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleBlock() throws Exception {
        XBTDocument document = new XBTTreeDocument(new XBTTreeNode());
        XBTTreeReader instance = new XBTTreeReader(document);
        XBCoreTestSampleData.writeSampleBlock(new XBPrintFilter(new XBToXBTConvertor(instance)));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleBlockTree();
        XBTestUtils.assertEqualsXBDocuments(expectedDocument, new XBTDocumentToXBDocument(document));
    }

    /**
     * Tests XBTTreeReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleBlockWithTail() throws Exception {
        XBTDocument document = new XBTTreeDocument(new XBTTreeNode());
        XBTTreeReader instance = new XBTTreeReader(document);
        XBCoreTestSampleData.writeSampleBlockWithTail(new XBPrintFilter(new XBToXBTConvertor(instance)));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleBlockWithTailTree();
        XBTestUtils.assertEqualsXBDocuments(expectedDocument, new XBTDocumentToXBDocument(document));
    }

    /**
     * Tests XBTTreeReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleBlockTerminated() throws Exception {
        XBTDocument document = new XBTTreeDocument(new XBTTreeNode());
        XBTTreeReader instance = new XBTTreeReader(document);
        XBCoreTestSampleData.writeSampleBlockTerminated(new XBPrintFilter(new XBToXBTConvertor(instance)));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleBlockTerminatedTree();
        XBTestUtils.assertEqualsXBDocuments(expectedDocument, new XBTDocumentToXBDocument(document));
    }

    /**
     * Tests XBTTreeReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleBlockTerminatedWithTail() throws Exception {
        XBTDocument document = new XBTTreeDocument(new XBTTreeNode());
        XBTTreeReader instance = new XBTTreeReader(document);
        XBCoreTestSampleData.writeSampleBlockTerminatedWithTail(new XBPrintFilter(new XBToXBTConvertor(instance)));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleBlockTerminatedWithTailTree();
        XBTestUtils.assertEqualsXBDocuments(expectedDocument, new XBTDocumentToXBDocument(document));
    }

    /**
     * Tests XBTTreeReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleData() throws Exception {
        XBTDocument document = new XBTTreeDocument(new XBTTreeNode());
        XBTTreeReader instance = new XBTTreeReader(document);
        XBCoreTestSampleData.writeSampleData(new XBPrintFilter(new XBToXBTConvertor(instance)));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleDataTree();
        XBTestUtils.assertEqualsXBDocuments(expectedDocument, new XBTDocumentToXBDocument(document));
    }

    /**
     * Tests XBTTreeReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleDataWithTail() throws Exception {
        XBTDocument document = new XBTTreeDocument(new XBTTreeNode());
        XBTTreeReader instance = new XBTTreeReader(document);
        XBCoreTestSampleData.writeSampleDataWithTail(new XBPrintFilter(new XBToXBTConvertor(instance)));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleDataWithTailTree();
        XBTestUtils.assertEqualsXBDocuments(expectedDocument, new XBTDocumentToXBDocument(document));
    }

    /**
     * Tests XBTTreeReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleDataTerminated() throws Exception {
        XBTDocument document = new XBTTreeDocument(new XBTTreeNode());
        XBTTreeReader instance = new XBTTreeReader(document);
        XBCoreTestSampleData.writeSampleDataTerminated(new XBPrintFilter(new XBToXBTConvertor(instance)));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleDataTerminatedTree();
        XBTestUtils.assertEqualsXBDocuments(expectedDocument, new XBTDocumentToXBDocument(document));
    }

    /**
     * Tests XBTTreeReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleDataTerminatedWithTail() throws Exception {
        XBTDocument document = new XBTTreeDocument(new XBTTreeNode());
        XBTTreeReader instance = new XBTTreeReader(document);
        XBCoreTestSampleData.writeSampleDataTerminatedWithTail(new XBPrintFilter(new XBToXBTConvertor(instance)));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleDataTerminatedWithTailTree();
        XBTestUtils.assertEqualsXBDocuments(expectedDocument, new XBTDocumentToXBDocument(document));
    }

    /**
     * Tests XBTTreeReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleBlockData() throws Exception {
        XBTDocument document = new XBTTreeDocument(new XBTTreeNode());
        XBTTreeReader instance = new XBTTreeReader(document);
        XBCoreTestSampleData.writeSampleBlockData(new XBPrintFilter(new XBToXBTConvertor(instance)));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleBlockDataTree();
        XBTestUtils.assertEqualsXBDocuments(expectedDocument, new XBTDocumentToXBDocument(document));
    }

    /**
     * Tests XBTTreeReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleBlockDataWithTail() throws Exception {
        XBTDocument document = new XBTTreeDocument(new XBTTreeNode());
        XBTTreeReader instance = new XBTTreeReader(document);
        XBCoreTestSampleData.writeSampleBlockDataWithTail(new XBPrintFilter(new XBToXBTConvertor(instance)));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleBlockDataWithTailTree();
        XBTestUtils.assertEqualsXBDocuments(expectedDocument, new XBTDocumentToXBDocument(document));
    }

    /**
     * Tests XBTTreeReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleBlockDataTerminated() throws Exception {
        XBTDocument document = new XBTTreeDocument(new XBTTreeNode());
        XBTTreeReader instance = new XBTTreeReader(document);
        XBCoreTestSampleData.writeSampleBlockDataTerminated(new XBPrintFilter(new XBToXBTConvertor(instance)));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleBlockDataTerminatedTree();
        XBTestUtils.assertEqualsXBDocuments(expectedDocument, new XBTDocumentToXBDocument(document));
    }

    /**
     * Tests XBTTreeReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleBlockDataTerminatedWithTail() throws Exception {
        XBTDocument document = new XBTTreeDocument(new XBTTreeNode());
        XBTTreeReader instance = new XBTTreeReader(document);
        XBCoreTestSampleData.writeSampleBlockDataTerminatedWithTail(new XBPrintFilter(new XBToXBTConvertor(instance)));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleBlockDataTerminatedWithTailTree();
        XBTestUtils.assertEqualsXBDocuments(expectedDocument, new XBTDocumentToXBDocument(document));
    }

    /**
     * Tests XBTTreeReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleBlockDataHybrid() throws Exception {
        XBTDocument document = new XBTTreeDocument(new XBTTreeNode());
        XBTTreeReader instance = new XBTTreeReader(document);
        XBCoreTestSampleData.writeSampleBlockDataHybrid(new XBPrintFilter(new XBToXBTConvertor(instance)));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleBlockDataHybridTree();
        XBTestUtils.assertEqualsXBDocuments(expectedDocument, new XBTDocumentToXBDocument(document));
    }

    /**
     * Tests XBTTreeReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleBlockDataHybrid2() throws Exception {
        XBTDocument document = new XBTTreeDocument(new XBTTreeNode());
        XBTTreeReader instance = new XBTTreeReader(document);
        XBCoreTestSampleData.writeSampleBlockDataHybrid2(new XBPrintFilter(new XBToXBTConvertor(instance)));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleBlockDataHybrid2Tree();
        XBTestUtils.assertEqualsXBDocuments(expectedDocument, new XBTDocumentToXBDocument(document));
    }

    /**
     * Tests XBTTreeReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleTwoBlocks() throws Exception {
        XBTDocument document = new XBTTreeDocument(new XBTTreeNode());
        XBTTreeReader instance = new XBTTreeReader(document);
        XBCoreTestSampleData.writeSampleTwoBlocks(new XBPrintFilter(new XBToXBTConvertor(instance)));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleTwoBlocksTree();
        XBTestUtils.assertEqualsXBDocuments(expectedDocument, new XBTDocumentToXBDocument(document));
    }

    /**
     * Tests XBTTreeReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleTwoBlocksWithTail() throws Exception {
        XBTDocument document = new XBTTreeDocument(new XBTTreeNode());
        XBTTreeReader instance = new XBTTreeReader(document);
        XBCoreTestSampleData.writeSampleTwoBlocksWithTail(new XBPrintFilter(new XBToXBTConvertor(instance)));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleTwoBlocksWithTailTree();
        XBTestUtils.assertEqualsXBDocuments(expectedDocument, new XBTDocumentToXBDocument(document));
    }

    /**
     * Tests XBTTreeReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleTwoBlocksTerminated() throws Exception {
        XBTDocument document = new XBTTreeDocument(new XBTTreeNode());
        XBTTreeReader instance = new XBTTreeReader(document);
        XBCoreTestSampleData.writeSampleTwoBlocksTerminated(new XBPrintFilter(new XBToXBTConvertor(instance)));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleTwoBlocksTerminatedTree();
        XBTestUtils.assertEqualsXBDocuments(expectedDocument, new XBTDocumentToXBDocument(document));
    }

    /**
     * Tests XBTTreeReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleTwoBlocksTerminatedWithTail() throws Exception {
        XBTDocument document = new XBTTreeDocument(new XBTTreeNode());
        XBTTreeReader instance = new XBTTreeReader(document);
        XBCoreTestSampleData.writeSampleTwoBlocksTerminatedWithTail(new XBPrintFilter(new XBToXBTConvertor(instance)));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleTwoBlocksTerminatedWithTailTree();
        XBTestUtils.assertEqualsXBDocuments(expectedDocument, new XBTDocumentToXBDocument(document));
    }

    /**
     * Tests XBTTreeReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleTwoBlocksHybrid() throws Exception {
        XBTDocument document = new XBTTreeDocument(new XBTTreeNode());
        XBTTreeReader instance = new XBTTreeReader(document);
        XBCoreTestSampleData.writeSampleTwoBlocksHybrid(new XBPrintFilter(new XBToXBTConvertor(instance)));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleTwoBlocksHybridTree();
        XBTestUtils.assertEqualsXBDocuments(expectedDocument, new XBTDocumentToXBDocument(document));
    }

    /**
     * Tests XBTTreeReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleTwoBlocksHybrid2() throws Exception {
        XBTDocument document = new XBTTreeDocument(new XBTTreeNode());
        XBTTreeReader instance = new XBTTreeReader(document);
        XBCoreTestSampleData.writeSampleTwoBlocksHybrid2(new XBPrintFilter(new XBToXBTConvertor(instance)));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleTwoBlocksHybrid2Tree();
        XBTestUtils.assertEqualsXBDocuments(expectedDocument, new XBTDocumentToXBDocument(document));
    }

    /**
     * Tests XBTTreeReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleSixBlocks() throws Exception {
        XBTDocument document = new XBTTreeDocument(new XBTTreeNode());
        XBTTreeReader instance = new XBTTreeReader(document);
        XBCoreTestSampleData.writeSampleSixBlocks(new XBPrintFilter(new XBToXBTConvertor(instance)));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleSixBlocksTree();
        XBTestUtils.assertEqualsXBDocuments(expectedDocument, new XBTDocumentToXBDocument(document));
    }
}
