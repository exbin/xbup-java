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
package org.xbup.lib.parser_tree;

import junit.framework.TestCase;
import org.junit.Test;
import org.xbup.lib.core.block.XBDocument;
import org.xbup.lib.core.block.XBTDocument;
import org.xbup.lib.core.parser.basic.convert.XBPrintFilter;
import org.xbup.lib.core.parser.basic.convert.XBToXBTConvertor;
import org.xbup.lib.core.parser.data.XBCoreTestSampleData;
import org.xbup.lib.core.test.XBTestUtils;

/**
 * Test class for XBTTreeReader.
 *
 * @version 0.1.25 2015/08/13
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
    public void testReadSampleBlockExtended() throws Exception {
        XBTDocument document = new XBTTreeDocument(new XBTTreeNode());
        XBTTreeReader instance = new XBTTreeReader(document);
        XBCoreTestSampleData.writeSampleBlockExtended(new XBPrintFilter(new XBToXBTConvertor(instance)));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleBlockExtendedTree();
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
    public void testReadSampleBlockTerminatedExtended() throws Exception {
        XBTDocument document = new XBTTreeDocument(new XBTTreeNode());
        XBTTreeReader instance = new XBTTreeReader(document);
        XBCoreTestSampleData.writeSampleBlockTerminatedExtended(new XBPrintFilter(new XBToXBTConvertor(instance)));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleBlockTerminatedExtendedTree();
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
    public void testReadSampleDataExtended() throws Exception {
        XBTDocument document = new XBTTreeDocument(new XBTTreeNode());
        XBTTreeReader instance = new XBTTreeReader(document);
        XBCoreTestSampleData.writeSampleDataExtended(new XBPrintFilter(new XBToXBTConvertor(instance)));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleDataExtendedTree();
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
    public void testReadSampleDataTerminatedExtended() throws Exception {
        XBTDocument document = new XBTTreeDocument(new XBTTreeNode());
        XBTTreeReader instance = new XBTTreeReader(document);
        XBCoreTestSampleData.writeSampleDataTerminatedExtended(new XBPrintFilter(new XBToXBTConvertor(instance)));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleDataTerminatedExtendedTree();
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
    public void testReadSampleBlockDataExtended() throws Exception {
        XBTDocument document = new XBTTreeDocument(new XBTTreeNode());
        XBTTreeReader instance = new XBTTreeReader(document);
        XBCoreTestSampleData.writeSampleBlockDataExtended(new XBPrintFilter(new XBToXBTConvertor(instance)));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleBlockDataExtendedTree();
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
    public void testReadSampleBlockDataTerminatedExtended() throws Exception {
        XBTDocument document = new XBTTreeDocument(new XBTTreeNode());
        XBTTreeReader instance = new XBTTreeReader(document);
        XBCoreTestSampleData.writeSampleBlockDataTerminatedExtended(new XBPrintFilter(new XBToXBTConvertor(instance)));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleBlockDataTerminatedExtendedTree();
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
    public void testReadSampleTwoBlocksExtended() throws Exception {
        XBTDocument document = new XBTTreeDocument(new XBTTreeNode());
        XBTTreeReader instance = new XBTTreeReader(document);
        XBCoreTestSampleData.writeSampleTwoBlocksExtended(new XBPrintFilter(new XBToXBTConvertor(instance)));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleTwoBlocksExtendedTree();
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
    public void testReadSampleTwoBlocksTerminatedExtended() throws Exception {
        XBTDocument document = new XBTTreeDocument(new XBTTreeNode());
        XBTTreeReader instance = new XBTTreeReader(document);
        XBCoreTestSampleData.writeSampleTwoBlocksTerminatedExtended(new XBPrintFilter(new XBToXBTConvertor(instance)));

        XBDocument expectedDocument = XBCoreTestSampleData.getSampleTwoBlocksTerminatedExtendedTree();
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
