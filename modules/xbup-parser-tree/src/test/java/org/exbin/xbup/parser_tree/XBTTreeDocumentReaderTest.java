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
package org.exbin.xbup.parser_tree;

import junit.framework.TestCase;
import org.junit.Test;
import org.exbin.xbup.core.block.XBDocument;
import org.exbin.xbup.core.parser.data.XBCoreTestSampleData;
import org.exbin.xbup.core.test.XBTestUtils;

/**
 * Test class for XBTTreeDocument.
 *
 * @version 0.2.0 2016/09/25
 * @author ExBin Project (http://exbin.org)
 */
public class XBTTreeDocumentReaderTest extends TestCase {

    public XBTTreeDocumentReaderTest(String testName) {
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
     * Tests XBTTreeDocument class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleEmpty() throws Exception {
        XBTTreeDocument instance = new XBTTreeDocument(new XBTTreeNode());
        instance.fromStreamUB(XBTTreeDocumentReaderTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_EMPTY));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleEmptyTree();
        XBTestUtils.assertEqualsXBDocuments(expectedDocument, new XBTDocumentToXBDocument(instance));
    }

    /**
     * Tests XBTTreeDocument class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleBlock() throws Exception {
        XBTTreeDocument instance = new XBTTreeDocument(new XBTTreeNode());
        instance.fromStreamUB(XBTTreeDocumentReaderTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_BLOCK));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleBlockTree();
        XBTestUtils.assertEqualsXBDocuments(expectedDocument, new XBTDocumentToXBDocument(instance));
    }

    /**
     * Tests XBTTreeDocument class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleBlockTailData() throws Exception {
        XBTTreeDocument instance = new XBTTreeDocument(new XBTTreeNode());
        instance.fromStreamUB(XBTTreeDocumentReaderTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_BLOCK_WITH_TAIL));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleBlockWithTailTree();
        XBTestUtils.assertEqualsXBDocuments(expectedDocument, new XBTDocumentToXBDocument(instance));
    }

    /**
     * Tests XBTTreeDocument class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleBlockTerminated() throws Exception {
        XBTTreeDocument instance = new XBTTreeDocument(new XBTTreeNode());
        instance.fromStreamUB(XBTTreeDocumentReaderTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_BLOCK_TERMINATED));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleBlockTerminatedTree();
        XBTestUtils.assertEqualsXBDocuments(expectedDocument, new XBTDocumentToXBDocument(instance));
    }

    /**
     * Tests XBTTreeDocument class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleBlockTerminatedWithTail() throws Exception {
        XBTTreeDocument instance = new XBTTreeDocument(new XBTTreeNode());
        instance.fromStreamUB(XBTTreeDocumentReaderTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_BLOCK_TERMINATED_WITH_TAIL));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleBlockTerminatedWithTailTree();
        XBTestUtils.assertEqualsXBDocuments(expectedDocument, new XBTDocumentToXBDocument(instance));
    }

    /**
     * Tests XBTTreeDocument class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleData() throws Exception {
        XBTTreeDocument instance = new XBTTreeDocument(new XBTTreeNode());
        instance.fromStreamUB(XBTTreeDocumentReaderTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_DATA));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleDataTree();
        XBTestUtils.assertEqualsXBDocuments(expectedDocument, new XBTDocumentToXBDocument(instance));
    }

    /**
     * Tests XBTTreeDocument class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleDataWithTail() throws Exception {
        XBTTreeDocument instance = new XBTTreeDocument(new XBTTreeNode());
        instance.fromStreamUB(XBTTreeDocumentReaderTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_DATA_WITH_TAIL));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleDataWithTailTree();
        XBTestUtils.assertEqualsXBDocuments(expectedDocument, new XBTDocumentToXBDocument(instance));
    }

    /**
     * Tests XBTTreeDocument class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleDataTerminated() throws Exception {
        XBTTreeDocument instance = new XBTTreeDocument(new XBTTreeNode());
        instance.fromStreamUB(XBTTreeDocumentReaderTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_DATA_TERMINATED));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleDataTerminatedTree();
        XBTestUtils.assertEqualsXBDocuments(expectedDocument, new XBTDocumentToXBDocument(instance));
    }

    /**
     * Tests XBTTreeDocument class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleDataTerminatedWithTail() throws Exception {
        XBTTreeDocument instance = new XBTTreeDocument(new XBTTreeNode());
        instance.fromStreamUB(XBTTreeDocumentReaderTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_DATA_TERMINATED_WITH_TAIL));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleDataTerminatedWithTailTree();
        XBTestUtils.assertEqualsXBDocuments(expectedDocument, new XBTDocumentToXBDocument(instance));
    }

    /**
     * Tests XBTTreeDocument class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleBlockData() throws Exception {
        XBTTreeDocument instance = new XBTTreeDocument(new XBTTreeNode());
        instance.fromStreamUB(XBTTreeDocumentReaderTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_BLOCK_DATA));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleBlockDataTree();
        XBTestUtils.assertEqualsXBDocuments(expectedDocument, new XBTDocumentToXBDocument(instance));
    }

    /**
     * Tests XBTTreeDocument class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleBlockDataWithTail() throws Exception {
        XBTTreeDocument instance = new XBTTreeDocument(new XBTTreeNode());
        instance.fromStreamUB(XBTTreeDocumentReaderTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_BLOCK_DATA_WITH_TAIL));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleBlockDataWithTailTree();
        XBTestUtils.assertEqualsXBDocuments(expectedDocument, new XBTDocumentToXBDocument(instance));
    }

    /**
     * Tests XBTTreeDocument class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleBlockDataTerminated() throws Exception {
        XBTTreeDocument instance = new XBTTreeDocument(new XBTTreeNode());
        instance.fromStreamUB(XBTTreeDocumentReaderTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_BLOCK_DATA_TERMINATED));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleBlockDataTerminatedTree();
        XBTestUtils.assertEqualsXBDocuments(expectedDocument, new XBTDocumentToXBDocument(instance));
    }

    /**
     * Tests XBTTreeDocument class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleBlockDataTerminatedWithTail() throws Exception {
        XBTTreeDocument instance = new XBTTreeDocument(new XBTTreeNode());
        instance.fromStreamUB(XBTTreeDocumentReaderTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_BLOCK_DATA_TERMINATED_WITH_TAIL));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleBlockDataTerminatedWithTailTree();
        XBTestUtils.assertEqualsXBDocuments(expectedDocument, new XBTDocumentToXBDocument(instance));
    }

    /**
     * Tests XBTTreeDocument class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleBlockDataHybrid() throws Exception {
        XBTTreeDocument instance = new XBTTreeDocument(new XBTTreeNode());
        instance.fromStreamUB(XBTTreeDocumentReaderTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_BLOCK_DATA_HYBRID));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleBlockDataHybridTree();
        XBTestUtils.assertEqualsXBDocuments(expectedDocument, new XBTDocumentToXBDocument(instance));
    }

    /**
     * Tests XBTTreeDocument class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleBlockDataHybrid2() throws Exception {
        XBTTreeDocument instance = new XBTTreeDocument(new XBTTreeNode());
        instance.fromStreamUB(XBTTreeDocumentReaderTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_BLOCK_DATA_HYBRID2));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleBlockDataHybrid2Tree();
        XBTestUtils.assertEqualsXBDocuments(expectedDocument, new XBTDocumentToXBDocument(instance));
    }

    /**
     * Tests XBTTreeDocument class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleTwoBlocks() throws Exception {
        XBTTreeDocument instance = new XBTTreeDocument(new XBTTreeNode());
        instance.fromStreamUB(XBTTreeDocumentReaderTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_TWO_BLOCKS));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleTwoBlocksTree();
        XBTestUtils.assertEqualsXBDocuments(expectedDocument, new XBTDocumentToXBDocument(instance));
    }

    /**
     * Tests XBTTreeDocument class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleTwoBlocksWithTail() throws Exception {
        XBTTreeDocument instance = new XBTTreeDocument(new XBTTreeNode());
        instance.fromStreamUB(XBTTreeDocumentReaderTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_TWO_BLOCKS_WITH_TAIL));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleTwoBlocksWithTailTree();
        XBTestUtils.assertEqualsXBDocuments(expectedDocument, new XBTDocumentToXBDocument(instance));
    }

    /**
     * Tests XBTTreeDocument class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleTwoBlocksTerminated() throws Exception {
        XBTTreeDocument instance = new XBTTreeDocument(new XBTTreeNode());
        instance.fromStreamUB(XBTTreeDocumentReaderTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_TWO_BLOCKS_TERMINATED));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleTwoBlocksTerminatedTree();
        XBTestUtils.assertEqualsXBDocuments(expectedDocument, new XBTDocumentToXBDocument(instance));
    }

    /**
     * Tests XBTTreeDocument class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleTwoBlocksTerminatedWithTail() throws Exception {
        XBTTreeDocument instance = new XBTTreeDocument(new XBTTreeNode());
        instance.fromStreamUB(XBTTreeDocumentReaderTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_TWO_BLOCKS_TERMINATED_WITH_TAIL));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleTwoBlocksTerminatedWithTailTree();
        XBTestUtils.assertEqualsXBDocuments(expectedDocument, new XBTDocumentToXBDocument(instance));
    }

    /**
     * Tests XBTTreeDocument class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleTwoBlocksHybrid() throws Exception {
        XBTTreeDocument instance = new XBTTreeDocument(new XBTTreeNode());
        instance.fromStreamUB(XBTTreeDocumentReaderTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_TWO_BLOCKS_HYBRID));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleTwoBlocksHybridTree();
        XBTestUtils.assertEqualsXBDocuments(expectedDocument, new XBTDocumentToXBDocument(instance));
    }

    /**
     * Tests XBTTreeDocument class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleTwoBlocksHybrid2() throws Exception {
        XBTTreeDocument instance = new XBTTreeDocument(new XBTTreeNode());
        instance.fromStreamUB(XBTTreeDocumentReaderTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_TWO_BLOCKS_HYBRID2));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleTwoBlocksHybrid2Tree();
        XBTestUtils.assertEqualsXBDocuments(expectedDocument, new XBTDocumentToXBDocument(instance));
    }

    /**
     * Tests XBTTreeDocument class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleSixBlocks() throws Exception {
        XBTTreeDocument instance = new XBTTreeDocument(new XBTTreeNode());
        instance.fromStreamUB(XBTTreeDocumentReaderTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_SIX_BLOCKS));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleSixBlocksTree();
        XBTestUtils.assertEqualsXBDocuments(expectedDocument, new XBTDocumentToXBDocument(instance));
    }
}
