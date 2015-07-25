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
import org.xbup.lib.core.parser.basic.convert.XBPrintFilter;
import org.xbup.lib.core.parser.data.XBCoreTestSampleData;
import org.xbup.lib.core.test.XBTestUtils;

/**
 * Test class for XBTreeReader.
 *
 * @version 0.1.25 2015/07/25
 * @author XBUP Project (http://xbup.org)
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
    public void testReadSampleBlockExtended() throws Exception {
        XBDocument document = new XBTreeDocument(new XBTreeNode());
        XBTreeReader instance = new XBTreeReader(document);
        XBCoreTestSampleData.writeSampleBlockExtended(new XBPrintFilter(instance));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleBlockExtendedTree();
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
    public void testReadSampleBlockTerminatedExtended() throws Exception {
        XBDocument document = new XBTreeDocument(new XBTreeNode());
        XBTreeReader instance = new XBTreeReader(document);
        XBCoreTestSampleData.writeSampleBlockTerminatedExtended(new XBPrintFilter(instance));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleBlockTerminatedExtendedTree();
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
    public void testReadSampleDataExtended() throws Exception {
        XBDocument document = new XBTreeDocument(new XBTreeNode());
        XBTreeReader instance = new XBTreeReader(document);
        XBCoreTestSampleData.writeSampleDataExtended(new XBPrintFilter(instance));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleDataExtendedTree();
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
    public void testReadSampleDataTerminatedExtended() throws Exception {
        XBDocument document = new XBTreeDocument(new XBTreeNode());
        XBTreeReader instance = new XBTreeReader(document);
        XBCoreTestSampleData.writeSampleDataTerminatedExtended(new XBPrintFilter(instance));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleDataTerminatedExtendedTree();
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
    public void testReadSampleTwoBlocksExtended() throws Exception {
        XBDocument document = new XBTreeDocument(new XBTreeNode());
        XBTreeReader instance = new XBTreeReader(document);
        XBCoreTestSampleData.writeSampleTwoBlocksExtended(new XBPrintFilter(instance));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleTwoBlocksExtendedTree();
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
