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
import org.exbin.xbup.core.parser.basic.convert.XBConsumerToListener;
import org.exbin.xbup.core.parser.basic.convert.XBPrintFilter;
import org.exbin.xbup.core.parser.data.XBCoreTestSampleData;
import org.exbin.xbup.core.test.XBTestUtils.BufferAssertXBFilter;

/**
 * Test class for XBTreeWriter.
 *
 * @version 0.2.0 2016/09/25
 * @author ExBin Project (http://exbin.org)
 */
public class XBTreeWriterTest extends TestCase {

    public XBTreeWriterTest(String testName) {
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
     * Tests XBTreeWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleEmpty() throws Exception {
        XBConsumerToListener buffer = new XBConsumerToListener(null);
        XBCoreTestSampleData.writeSampleEmpty(buffer);
        BufferAssertXBFilter assertListener = new BufferAssertXBFilter(buffer.getTokens());

        XBTreeWriter treeWriter = new XBTreeWriter(XBCoreTestSampleData.getSampleEmptyTree());
        treeWriter.attachXBListener(new XBPrintFilter(assertListener));
    }

    /**
     * Tests XBTreeWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleBlock() throws Exception {
        XBConsumerToListener buffer = new XBConsumerToListener(null);
        XBCoreTestSampleData.writeSampleBlock(buffer);
        BufferAssertXBFilter assertListener = new BufferAssertXBFilter(buffer.getTokens());

        XBTreeWriter treeWriter = new XBTreeWriter(XBCoreTestSampleData.getSampleBlockTree());
        treeWriter.attachXBListener(new XBPrintFilter(assertListener));
    }

    /**
     * Tests XBTreeWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleBlockWithTail() throws Exception {
        XBConsumerToListener buffer = new XBConsumerToListener(null);
        XBCoreTestSampleData.writeSampleBlockWithTail(buffer);
        BufferAssertXBFilter assertListener = new BufferAssertXBFilter(buffer.getTokens());

        XBTreeWriter treeWriter = new XBTreeWriter(XBCoreTestSampleData.getSampleBlockWithTailTree());
        treeWriter.attachXBListener(new XBPrintFilter(assertListener));
    }

    /**
     * Tests XBTreeWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleBlockTerminated() throws Exception {
        XBConsumerToListener buffer = new XBConsumerToListener(null);
        XBCoreTestSampleData.writeSampleBlockTerminated(buffer);
        BufferAssertXBFilter assertListener = new BufferAssertXBFilter(buffer.getTokens());

        XBTreeWriter treeWriter = new XBTreeWriter(XBCoreTestSampleData.getSampleBlockTerminatedTree());
        treeWriter.attachXBListener(new XBPrintFilter(assertListener));
    }

    /**
     * Tests XBTreeWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleBlockTerminatedWithTail() throws Exception {
        XBConsumerToListener buffer = new XBConsumerToListener(null);
        XBCoreTestSampleData.writeSampleBlockTerminatedWithTail(buffer);
        BufferAssertXBFilter assertListener = new BufferAssertXBFilter(buffer.getTokens());

        XBTreeWriter treeWriter = new XBTreeWriter(XBCoreTestSampleData.getSampleBlockTerminatedWithTailTree());
        treeWriter.attachXBListener(new XBPrintFilter(assertListener));
    }

    /**
     * Tests XBTreeWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleData() throws Exception {
        XBConsumerToListener buffer = new XBConsumerToListener(null);
        XBCoreTestSampleData.writeSampleData(buffer);
        BufferAssertXBFilter assertListener = new BufferAssertXBFilter(buffer.getTokens());

        XBTreeWriter treeWriter = new XBTreeWriter(XBCoreTestSampleData.getSampleDataTree());
        treeWriter.attachXBListener(new XBPrintFilter(assertListener));
    }

    /**
     * Tests XBTreeWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleDataWithTail() throws Exception {
        XBConsumerToListener buffer = new XBConsumerToListener(null);
        XBCoreTestSampleData.writeSampleDataWithTail(buffer);
        BufferAssertXBFilter assertListener = new BufferAssertXBFilter(buffer.getTokens());

        XBTreeWriter treeWriter = new XBTreeWriter(XBCoreTestSampleData.getSampleDataWithTailTree());
        treeWriter.attachXBListener(new XBPrintFilter(assertListener));
    }

    /**
     * Tests XBTreeWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleDataTerminated() throws Exception {
        XBConsumerToListener buffer = new XBConsumerToListener(null);
        XBCoreTestSampleData.writeSampleDataTerminated(buffer);
        BufferAssertXBFilter assertListener = new BufferAssertXBFilter(buffer.getTokens());

        XBTreeWriter treeWriter = new XBTreeWriter(XBCoreTestSampleData.getSampleDataTerminatedTree());
        treeWriter.attachXBListener(new XBPrintFilter(assertListener));
    }

    /**
     * Tests XBTreeWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleDataTerminatedWithTail() throws Exception {
        XBConsumerToListener buffer = new XBConsumerToListener(null);
        XBCoreTestSampleData.writeSampleDataTerminatedWithTail(buffer);
        BufferAssertXBFilter assertListener = new BufferAssertXBFilter(buffer.getTokens());

        XBTreeWriter treeWriter = new XBTreeWriter(XBCoreTestSampleData.getSampleDataTerminatedWithTailTree());
        treeWriter.attachXBListener(new XBPrintFilter(assertListener));
    }

    /**
     * Tests XBTreeWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleBlockData() throws Exception {
        XBConsumerToListener buffer = new XBConsumerToListener(null);
        XBCoreTestSampleData.writeSampleBlockData(buffer);
        BufferAssertXBFilter assertListener = new BufferAssertXBFilter(buffer.getTokens());

        XBTreeWriter treeWriter = new XBTreeWriter(XBCoreTestSampleData.getSampleBlockDataTree());
        treeWriter.attachXBListener(new XBPrintFilter(assertListener));
    }

    /**
     * Tests XBTreeWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleBlockDataWithTail() throws Exception {
        XBConsumerToListener buffer = new XBConsumerToListener(null);
        XBCoreTestSampleData.writeSampleBlockDataWithTail(buffer);
        BufferAssertXBFilter assertListener = new BufferAssertXBFilter(buffer.getTokens());

        XBTreeWriter treeWriter = new XBTreeWriter(XBCoreTestSampleData.getSampleBlockDataWithTailTree());
        treeWriter.attachXBListener(new XBPrintFilter(assertListener));
    }

    /**
     * Tests XBTreeWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleBlockDataTerminated() throws Exception {
        XBConsumerToListener buffer = new XBConsumerToListener(null);
        XBCoreTestSampleData.writeSampleBlockDataTerminated(buffer);
        BufferAssertXBFilter assertListener = new BufferAssertXBFilter(buffer.getTokens());

        XBTreeWriter treeWriter = new XBTreeWriter(XBCoreTestSampleData.getSampleBlockDataTerminatedTree());
        treeWriter.attachXBListener(new XBPrintFilter(assertListener));
    }

    /**
     * Tests XBTreeWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleBlockDataTerminatedWithTail() throws Exception {
        XBConsumerToListener buffer = new XBConsumerToListener(null);
        XBCoreTestSampleData.writeSampleBlockDataTerminatedWithTail(buffer);
        BufferAssertXBFilter assertListener = new BufferAssertXBFilter(buffer.getTokens());

        XBTreeWriter treeWriter = new XBTreeWriter(XBCoreTestSampleData.getSampleBlockDataTerminatedWithTailTree());
        treeWriter.attachXBListener(new XBPrintFilter(assertListener));
    }

    /**
     * Tests XBTreeWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleBlockDataHybrid() throws Exception {
        XBConsumerToListener buffer = new XBConsumerToListener(null);
        XBCoreTestSampleData.writeSampleBlockDataHybrid(buffer);
        BufferAssertXBFilter assertListener = new BufferAssertXBFilter(buffer.getTokens());

        XBTreeWriter treeWriter = new XBTreeWriter(XBCoreTestSampleData.getSampleBlockDataHybridTree());
        treeWriter.attachXBListener(new XBPrintFilter(assertListener));
    }

    /**
     * Tests XBTreeWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleBlockDataHybrid2() throws Exception {
        XBConsumerToListener buffer = new XBConsumerToListener(null);
        XBCoreTestSampleData.writeSampleBlockDataHybrid2(buffer);
        BufferAssertXBFilter assertListener = new BufferAssertXBFilter(buffer.getTokens());

        XBTreeWriter treeWriter = new XBTreeWriter(XBCoreTestSampleData.getSampleBlockDataHybrid2Tree());
        treeWriter.attachXBListener(new XBPrintFilter(assertListener));
    }

    /**
     * Tests XBTreeWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleTwoBlocks() throws Exception {
        XBConsumerToListener buffer = new XBConsumerToListener(null);
        XBCoreTestSampleData.writeSampleTwoBlocks(buffer);
        BufferAssertXBFilter assertListener = new BufferAssertXBFilter(buffer.getTokens());

        XBTreeWriter treeWriter = new XBTreeWriter(XBCoreTestSampleData.getSampleTwoBlocksTree());
        treeWriter.attachXBListener(new XBPrintFilter(assertListener));
    }

    /**
     * Tests XBTreeWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleTwoBlocksWithTail() throws Exception {
        XBConsumerToListener buffer = new XBConsumerToListener(null);
        XBCoreTestSampleData.writeSampleTwoBlocksWithTail(buffer);
        BufferAssertXBFilter assertListener = new BufferAssertXBFilter(buffer.getTokens());

        XBTreeWriter treeWriter = new XBTreeWriter(XBCoreTestSampleData.getSampleTwoBlocksWithTailTree());
        treeWriter.attachXBListener(new XBPrintFilter(assertListener));
    }

    /**
     * Tests XBTreeWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleTwoBlocksTerminated() throws Exception {
        XBConsumerToListener buffer = new XBConsumerToListener(null);
        XBCoreTestSampleData.writeSampleTwoBlocksTerminated(buffer);
        BufferAssertXBFilter assertListener = new BufferAssertXBFilter(buffer.getTokens());

        XBTreeWriter treeWriter = new XBTreeWriter(XBCoreTestSampleData.getSampleTwoBlocksTerminatedTree());
        treeWriter.attachXBListener(new XBPrintFilter(assertListener));
    }

    /**
     * Tests XBTreeWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleTwoBlocksTerminatedWithTail() throws Exception {
        XBConsumerToListener buffer = new XBConsumerToListener(null);
        XBCoreTestSampleData.writeSampleTwoBlocksTerminatedWithTail(buffer);
        BufferAssertXBFilter assertListener = new BufferAssertXBFilter(buffer.getTokens());

        XBTreeWriter treeWriter = new XBTreeWriter(XBCoreTestSampleData.getSampleTwoBlocksTerminatedWithTailTree());
        treeWriter.attachXBListener(new XBPrintFilter(assertListener));
    }

    /**
     * Tests XBTreeWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleTwoBlocksHybrid() throws Exception {
        XBConsumerToListener buffer = new XBConsumerToListener(null);
        XBCoreTestSampleData.writeSampleTwoBlocksHybrid(buffer);
        BufferAssertXBFilter assertListener = new BufferAssertXBFilter(buffer.getTokens());

        XBTreeWriter treeWriter = new XBTreeWriter(XBCoreTestSampleData.getSampleTwoBlocksHybridTree());
        treeWriter.attachXBListener(new XBPrintFilter(assertListener));
    }

    /**
     * Tests XBTreeWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleTwoBlocksHybrid2() throws Exception {
        XBConsumerToListener buffer = new XBConsumerToListener(null);
        XBCoreTestSampleData.writeSampleTwoBlocksHybrid2(buffer);
        BufferAssertXBFilter assertListener = new BufferAssertXBFilter(buffer.getTokens());

        XBTreeWriter treeWriter = new XBTreeWriter(XBCoreTestSampleData.getSampleTwoBlocksHybrid2Tree());
        treeWriter.attachXBListener(new XBPrintFilter(assertListener));
    }

    /**
     * Tests XBTreeWriter class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleSixBlocks() throws Exception {
        XBConsumerToListener buffer = new XBConsumerToListener(null);
        XBCoreTestSampleData.writeSampleSixBlocks(buffer);
        BufferAssertXBFilter assertListener = new BufferAssertXBFilter(buffer.getTokens());

        XBTreeWriter treeWriter = new XBTreeWriter(XBCoreTestSampleData.getSampleSixBlocksTree());
        treeWriter.attachXBListener(new XBPrintFilter(assertListener));
    }
}
