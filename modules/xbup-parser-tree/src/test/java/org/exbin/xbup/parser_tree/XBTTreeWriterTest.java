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

import org.exbin.xbup.parser_tree.XBTTreeWriter;
import org.exbin.xbup.parser_tree.XBDocumentToXBTDocument;
import junit.framework.TestCase;
import org.junit.Test;
import org.exbin.xbup.core.parser.basic.convert.XBConsumerToListener;
import org.exbin.xbup.core.parser.basic.convert.XBTPrintFilter;
import org.exbin.xbup.core.parser.basic.convert.XBTToXBConvertor;
import org.exbin.xbup.core.parser.data.XBCoreTestSampleData;
import org.exbin.xbup.core.test.XBTestUtils.BufferAssertXBFilter;

/**
 * Test class for XBTTreeWriter.
 *
 * @version 0.1.25 2015/08/13
 * @author ExBin Project (http://exbin.org)
 */
public class XBTTreeWriterTest extends TestCase {

    public XBTTreeWriterTest(String testName) {
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
     * Tests XBTTreeWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleEmpty() throws Exception {
        XBConsumerToListener buffer = new XBConsumerToListener(null);
        XBCoreTestSampleData.writeSampleEmpty(buffer);
        BufferAssertXBFilter assertListener = new BufferAssertXBFilter(buffer.getTokens());

        XBTTreeWriter treeWriter = new XBTTreeWriter(new XBDocumentToXBTDocument(XBCoreTestSampleData.getSampleEmptyTree()));
        treeWriter.produceXBT(new XBTPrintFilter(new XBTToXBConvertor(assertListener)));
    }

    /**
     * Tests XBTTreeWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleBlock() throws Exception {
        XBConsumerToListener buffer = new XBConsumerToListener(null);
        XBCoreTestSampleData.writeSampleBlock(buffer);
        BufferAssertXBFilter assertListener = new BufferAssertXBFilter(buffer.getTokens());

        XBTTreeWriter treeWriter = new XBTTreeWriter(new XBDocumentToXBTDocument(XBCoreTestSampleData.getSampleBlockTree()));
        treeWriter.produceXBT(new XBTPrintFilter(new XBTToXBConvertor(assertListener)));
    }

    /**
     * Tests XBTTreeWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleBlockExtended() throws Exception {
        XBConsumerToListener buffer = new XBConsumerToListener(null);
        XBCoreTestSampleData.writeSampleBlockExtended(buffer);
        BufferAssertXBFilter assertListener = new BufferAssertXBFilter(buffer.getTokens());

        XBTTreeWriter treeWriter = new XBTTreeWriter(new XBDocumentToXBTDocument(XBCoreTestSampleData.getSampleBlockExtendedTree()));
        treeWriter.produceXBT(new XBTPrintFilter(new XBTToXBConvertor(assertListener)));
    }

    /**
     * Tests XBTTreeWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleBlockTerminated() throws Exception {
        XBConsumerToListener buffer = new XBConsumerToListener(null);
        XBCoreTestSampleData.writeSampleBlockTerminated(buffer);
        BufferAssertXBFilter assertListener = new BufferAssertXBFilter(buffer.getTokens());

        XBTTreeWriter treeWriter = new XBTTreeWriter(new XBDocumentToXBTDocument(XBCoreTestSampleData.getSampleBlockTerminatedTree()));
        treeWriter.produceXBT(new XBTPrintFilter(new XBTToXBConvertor(assertListener)));
    }

    /**
     * Tests XBTTreeWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleBlockTerminatedExtended() throws Exception {
        XBConsumerToListener buffer = new XBConsumerToListener(null);
        XBCoreTestSampleData.writeSampleBlockTerminatedExtended(buffer);
        BufferAssertXBFilter assertListener = new BufferAssertXBFilter(buffer.getTokens());

        XBTTreeWriter treeWriter = new XBTTreeWriter(new XBDocumentToXBTDocument(XBCoreTestSampleData.getSampleBlockTerminatedExtendedTree()));
        treeWriter.produceXBT(new XBTPrintFilter(new XBTToXBConvertor(assertListener)));
    }

    /**
     * Tests XBTTreeWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleData() throws Exception {
        XBConsumerToListener buffer = new XBConsumerToListener(null);
        XBCoreTestSampleData.writeSampleData(buffer);
        BufferAssertXBFilter assertListener = new BufferAssertXBFilter(buffer.getTokens());

        XBTTreeWriter treeWriter = new XBTTreeWriter(new XBDocumentToXBTDocument(XBCoreTestSampleData.getSampleDataTree()));
        treeWriter.produceXBT(new XBTPrintFilter(new XBTToXBConvertor(assertListener)));
    }

    /**
     * Tests XBTTreeWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleDataExtended() throws Exception {
        XBConsumerToListener buffer = new XBConsumerToListener(null);
        XBCoreTestSampleData.writeSampleDataExtended(buffer);
        BufferAssertXBFilter assertListener = new BufferAssertXBFilter(buffer.getTokens());

        XBTTreeWriter treeWriter = new XBTTreeWriter(new XBDocumentToXBTDocument(XBCoreTestSampleData.getSampleDataExtendedTree()));
        treeWriter.produceXBT(new XBTPrintFilter(new XBTToXBConvertor(assertListener)));
    }

    /**
     * Tests XBTTreeWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleDataTerminated() throws Exception {
        XBConsumerToListener buffer = new XBConsumerToListener(null);
        XBCoreTestSampleData.writeSampleDataTerminated(buffer);
        BufferAssertXBFilter assertListener = new BufferAssertXBFilter(buffer.getTokens());

        XBTTreeWriter treeWriter = new XBTTreeWriter(new XBDocumentToXBTDocument(XBCoreTestSampleData.getSampleDataTerminatedTree()));
        treeWriter.produceXBT(new XBTPrintFilter(new XBTToXBConvertor(assertListener)));
    }

    /**
     * Tests XBTTreeWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleDataTerminatedExtended() throws Exception {
        XBConsumerToListener buffer = new XBConsumerToListener(null);
        XBCoreTestSampleData.writeSampleDataTerminatedExtended(buffer);
        BufferAssertXBFilter assertListener = new BufferAssertXBFilter(buffer.getTokens());

        XBTTreeWriter treeWriter = new XBTTreeWriter(new XBDocumentToXBTDocument(XBCoreTestSampleData.getSampleDataTerminatedExtendedTree()));
        treeWriter.produceXBT(new XBTPrintFilter(new XBTToXBConvertor(assertListener)));
    }

    /**
     * Tests XBTTreeWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleBlockData() throws Exception {
        XBConsumerToListener buffer = new XBConsumerToListener(null);
        XBCoreTestSampleData.writeSampleBlockData(buffer);
        BufferAssertXBFilter assertListener = new BufferAssertXBFilter(buffer.getTokens());

        XBTTreeWriter treeWriter = new XBTTreeWriter(new XBDocumentToXBTDocument(XBCoreTestSampleData.getSampleBlockDataTree()));
        treeWriter.produceXBT(new XBTPrintFilter(new XBTToXBConvertor(assertListener)));
    }

    /**
     * Tests XBTTreeWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleBlockDataExtended() throws Exception {
        XBConsumerToListener buffer = new XBConsumerToListener(null);
        XBCoreTestSampleData.writeSampleBlockDataExtended(buffer);
        BufferAssertXBFilter assertListener = new BufferAssertXBFilter(buffer.getTokens());

        XBTTreeWriter treeWriter = new XBTTreeWriter(new XBDocumentToXBTDocument(XBCoreTestSampleData.getSampleBlockDataExtendedTree()));
        treeWriter.produceXBT(new XBTPrintFilter(new XBTToXBConvertor(assertListener)));
    }

    /**
     * Tests XBTTreeWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleBlockDataTerminated() throws Exception {
        XBConsumerToListener buffer = new XBConsumerToListener(null);
        XBCoreTestSampleData.writeSampleBlockDataTerminated(buffer);
        BufferAssertXBFilter assertListener = new BufferAssertXBFilter(buffer.getTokens());

        XBTTreeWriter treeWriter = new XBTTreeWriter(new XBDocumentToXBTDocument(XBCoreTestSampleData.getSampleBlockDataTerminatedTree()));
        treeWriter.produceXBT(new XBTPrintFilter(new XBTToXBConvertor(assertListener)));
    }

    /**
     * Tests XBTTreeWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleBlockDataTerminatedExtended() throws Exception {
        XBConsumerToListener buffer = new XBConsumerToListener(null);
        XBCoreTestSampleData.writeSampleBlockDataTerminatedExtended(buffer);
        BufferAssertXBFilter assertListener = new BufferAssertXBFilter(buffer.getTokens());

        XBTTreeWriter treeWriter = new XBTTreeWriter(new XBDocumentToXBTDocument(XBCoreTestSampleData.getSampleBlockDataTerminatedExtendedTree()));
        treeWriter.produceXBT(new XBTPrintFilter(new XBTToXBConvertor(assertListener)));
    }

    /**
     * Tests XBTTreeWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleBlockDataHybrid() throws Exception {
        XBConsumerToListener buffer = new XBConsumerToListener(null);
        XBCoreTestSampleData.writeSampleBlockDataHybrid(buffer);
        BufferAssertXBFilter assertListener = new BufferAssertXBFilter(buffer.getTokens());

        XBTTreeWriter treeWriter = new XBTTreeWriter(new XBDocumentToXBTDocument(XBCoreTestSampleData.getSampleBlockDataHybridTree()));
        treeWriter.produceXBT(new XBTPrintFilter(new XBTToXBConvertor(assertListener)));
    }

    /**
     * Tests XBTTreeWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleBlockDataHybrid2() throws Exception {
        XBConsumerToListener buffer = new XBConsumerToListener(null);
        XBCoreTestSampleData.writeSampleBlockDataHybrid2(buffer);
        BufferAssertXBFilter assertListener = new BufferAssertXBFilter(buffer.getTokens());

        XBTTreeWriter treeWriter = new XBTTreeWriter(new XBDocumentToXBTDocument(XBCoreTestSampleData.getSampleBlockDataHybrid2Tree()));
        treeWriter.produceXBT(new XBTPrintFilter(new XBTToXBConvertor(assertListener)));
    }

    /**
     * Tests XBTTreeWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleTwoBlocks() throws Exception {
        XBConsumerToListener buffer = new XBConsumerToListener(null);
        XBCoreTestSampleData.writeSampleTwoBlocks(buffer);
        BufferAssertXBFilter assertListener = new BufferAssertXBFilter(buffer.getTokens());

        XBTTreeWriter treeWriter = new XBTTreeWriter(new XBDocumentToXBTDocument(XBCoreTestSampleData.getSampleTwoBlocksTree()));
        treeWriter.produceXBT(new XBTPrintFilter(new XBTToXBConvertor(assertListener)));
    }

    /**
     * Tests XBTTreeWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleTwoBlocksExtended() throws Exception {
        XBConsumerToListener buffer = new XBConsumerToListener(null);
        XBCoreTestSampleData.writeSampleTwoBlocksExtended(buffer);
        BufferAssertXBFilter assertListener = new BufferAssertXBFilter(buffer.getTokens());

        XBTTreeWriter treeWriter = new XBTTreeWriter(new XBDocumentToXBTDocument(XBCoreTestSampleData.getSampleTwoBlocksExtendedTree()));
        treeWriter.produceXBT(new XBTPrintFilter(new XBTToXBConvertor(assertListener)));
    }

    /**
     * Tests XBTTreeWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleTwoBlocksTerminated() throws Exception {
        XBConsumerToListener buffer = new XBConsumerToListener(null);
        XBCoreTestSampleData.writeSampleTwoBlocksTerminated(buffer);
        BufferAssertXBFilter assertListener = new BufferAssertXBFilter(buffer.getTokens());

        XBTTreeWriter treeWriter = new XBTTreeWriter(new XBDocumentToXBTDocument(XBCoreTestSampleData.getSampleTwoBlocksTerminatedTree()));
        treeWriter.produceXBT(new XBTPrintFilter(new XBTToXBConvertor(assertListener)));
    }

    /**
     * Tests XBTTreeWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleTwoBlocksTerminatedExtended() throws Exception {
        XBConsumerToListener buffer = new XBConsumerToListener(null);
        XBCoreTestSampleData.writeSampleTwoBlocksTerminatedExtended(buffer);
        BufferAssertXBFilter assertListener = new BufferAssertXBFilter(buffer.getTokens());

        XBTTreeWriter treeWriter = new XBTTreeWriter(new XBDocumentToXBTDocument(XBCoreTestSampleData.getSampleTwoBlocksTerminatedExtendedTree()));
        treeWriter.produceXBT(new XBTPrintFilter(new XBTToXBConvertor(assertListener)));
    }

    /**
     * Tests XBTTreeWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleTwoBlocksHybrid() throws Exception {
        XBConsumerToListener buffer = new XBConsumerToListener(null);
        XBCoreTestSampleData.writeSampleTwoBlocksHybrid(buffer);
        BufferAssertXBFilter assertListener = new BufferAssertXBFilter(buffer.getTokens());

        XBTTreeWriter treeWriter = new XBTTreeWriter(new XBDocumentToXBTDocument(XBCoreTestSampleData.getSampleTwoBlocksHybridTree()));
        treeWriter.produceXBT(new XBTPrintFilter(new XBTToXBConvertor(assertListener)));
    }

    /**
     * Tests XBTTreeWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleTwoBlocksHybrid2() throws Exception {
        XBConsumerToListener buffer = new XBConsumerToListener(null);
        XBCoreTestSampleData.writeSampleTwoBlocksHybrid2(buffer);
        BufferAssertXBFilter assertListener = new BufferAssertXBFilter(buffer.getTokens());

        XBTTreeWriter treeWriter = new XBTTreeWriter(new XBDocumentToXBTDocument(XBCoreTestSampleData.getSampleTwoBlocksHybrid2Tree()));
        treeWriter.produceXBT(new XBTPrintFilter(new XBTToXBConvertor(assertListener)));
    }

    /**
     * Tests XBTTreeWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleSixBlocks() throws Exception {
        XBConsumerToListener buffer = new XBConsumerToListener(null);
        XBCoreTestSampleData.writeSampleSixBlocks(buffer);
        BufferAssertXBFilter assertListener = new BufferAssertXBFilter(buffer.getTokens());

        XBTTreeWriter treeWriter = new XBTTreeWriter(new XBDocumentToXBTDocument(XBCoreTestSampleData.getSampleSixBlocksTree()));
        treeWriter.produceXBT(new XBTPrintFilter(new XBTToXBConvertor(assertListener)));
    }
}
