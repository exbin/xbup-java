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
package org.xbup.lib.parser_tree;

import junit.framework.TestCase;
import org.junit.Test;
import org.xbup.lib.core.block.XBDocument;
import org.xbup.lib.core.parser.data.XBCoreTestSampleData;
import org.xbup.lib.core.test.XBTestUtils;

/**
 * Test class for XBTTreeDocument.
 *
 * @version 0.1.25 2015/08/12
 * @author XBUP Project (http://xbup.org)
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
    public void testReadSampleBlockExtended() throws Exception {
        XBTTreeDocument instance = new XBTTreeDocument(new XBTTreeNode());
        instance.fromStreamUB(XBTTreeDocumentReaderTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_BLOCK_EXTENDED));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleBlockExtendedTree();
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
    public void testReadSampleBlockTerminatedExtended() throws Exception {
        XBTTreeDocument instance = new XBTTreeDocument(new XBTTreeNode());
        instance.fromStreamUB(XBTTreeDocumentReaderTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_BLOCK_TERMINATED_EXTENDED));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleBlockTerminatedExtendedTree();
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
    public void testReadSampleDataExtended() throws Exception {
        XBTTreeDocument instance = new XBTTreeDocument(new XBTTreeNode());
        instance.fromStreamUB(XBTTreeDocumentReaderTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_DATA_EXTENDED));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleDataExtendedTree();
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
    public void testReadSampleDataTerminatedExtended() throws Exception {
        XBTTreeDocument instance = new XBTTreeDocument(new XBTTreeNode());
        instance.fromStreamUB(XBTTreeDocumentReaderTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_DATA_TERMINATED_EXTENDED));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleDataTerminatedExtendedTree();
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
    public void testReadSampleBlockDataExtended() throws Exception {
        XBTTreeDocument instance = new XBTTreeDocument(new XBTTreeNode());
        instance.fromStreamUB(XBTTreeDocumentReaderTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_BLOCK_DATA_EXTENDED));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleBlockDataExtendedTree();
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
    public void testReadSampleBlockDataTerminatedExtended() throws Exception {
        XBTTreeDocument instance = new XBTTreeDocument(new XBTTreeNode());
        instance.fromStreamUB(XBTTreeDocumentReaderTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_BLOCK_DATA_TERMINATED_EXTENDED));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleBlockDataTerminatedExtendedTree();
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
    public void testReadSampleTwoBlocksExtended() throws Exception {
        XBTTreeDocument instance = new XBTTreeDocument(new XBTTreeNode());
        instance.fromStreamUB(XBTTreeDocumentReaderTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_TWO_BLOCKS_EXTENDED));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleTwoBlocksExtendedTree();
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
    public void testReadSampleTwoBlocksTerminatedExtended() throws Exception {
        XBTTreeDocument instance = new XBTTreeDocument(new XBTTreeNode());
        instance.fromStreamUB(XBTTreeDocumentReaderTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_TWO_BLOCKS_TERMINATED_EXTENDED));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleTwoBlocksTerminatedExtendedTree();
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
