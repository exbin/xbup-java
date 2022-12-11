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
import org.exbin.xbup.core.parser.basic.convert.XBConsumerToListener;
import org.exbin.xbup.core.parser.basic.convert.XBTPrintFilter;
import org.exbin.xbup.core.parser.basic.convert.XBTToXBConvertor;
import org.exbin.xbup.core.parser.data.XBCoreTestSampleData;
import org.exbin.xbup.core.test.XBTestUtils.BufferAssertXBFilter;
import org.junit.Test;

/**
 * Test class for XBTTreeWriter.
 *
 * @author ExBin Project (https://exbin.org)
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
    public void testWriteSampleBlockWithTail() throws Exception {
        XBConsumerToListener buffer = new XBConsumerToListener(null);
        XBCoreTestSampleData.writeSampleBlockWithTail(buffer);
        BufferAssertXBFilter assertListener = new BufferAssertXBFilter(buffer.getTokens());

        XBTTreeWriter treeWriter = new XBTTreeWriter(new XBDocumentToXBTDocument(XBCoreTestSampleData.getSampleBlockWithTailTree()));
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
    public void testWriteSampleBlockTerminatedWithTail() throws Exception {
        XBConsumerToListener buffer = new XBConsumerToListener(null);
        XBCoreTestSampleData.writeSampleBlockTerminatedWithTail(buffer);
        BufferAssertXBFilter assertListener = new BufferAssertXBFilter(buffer.getTokens());

        XBTTreeWriter treeWriter = new XBTTreeWriter(new XBDocumentToXBTDocument(XBCoreTestSampleData.getSampleBlockTerminatedWithTailTree()));
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
    public void testWriteSampleDataWithTail() throws Exception {
        XBConsumerToListener buffer = new XBConsumerToListener(null);
        XBCoreTestSampleData.writeSampleDataWithTail(buffer);
        BufferAssertXBFilter assertListener = new BufferAssertXBFilter(buffer.getTokens());

        XBTTreeWriter treeWriter = new XBTTreeWriter(new XBDocumentToXBTDocument(XBCoreTestSampleData.getSampleDataWithTailTree()));
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
    public void testWriteSampleDataTerminatedWithTail() throws Exception {
        XBConsumerToListener buffer = new XBConsumerToListener(null);
        XBCoreTestSampleData.writeSampleDataTerminatedWithTail(buffer);
        BufferAssertXBFilter assertListener = new BufferAssertXBFilter(buffer.getTokens());

        XBTTreeWriter treeWriter = new XBTTreeWriter(new XBDocumentToXBTDocument(XBCoreTestSampleData.getSampleDataTerminatedWithTailTree()));
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
    public void testWriteSampleBlockDataWithTail() throws Exception {
        XBConsumerToListener buffer = new XBConsumerToListener(null);
        XBCoreTestSampleData.writeSampleBlockDataWithTail(buffer);
        BufferAssertXBFilter assertListener = new BufferAssertXBFilter(buffer.getTokens());

        XBTTreeWriter treeWriter = new XBTTreeWriter(new XBDocumentToXBTDocument(XBCoreTestSampleData.getSampleBlockDataWithTailTree()));
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
    public void testWriteSampleBlockDataTerminatedWithTail() throws Exception {
        XBConsumerToListener buffer = new XBConsumerToListener(null);
        XBCoreTestSampleData.writeSampleBlockDataTerminatedWithTail(buffer);
        BufferAssertXBFilter assertListener = new BufferAssertXBFilter(buffer.getTokens());

        XBTTreeWriter treeWriter = new XBTTreeWriter(new XBDocumentToXBTDocument(XBCoreTestSampleData.getSampleBlockDataTerminatedWithTailTree()));
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
    public void testWriteSampleTwoBlocksWithTail() throws Exception {
        XBConsumerToListener buffer = new XBConsumerToListener(null);
        XBCoreTestSampleData.writeSampleTwoBlocksWithTail(buffer);
        BufferAssertXBFilter assertListener = new BufferAssertXBFilter(buffer.getTokens());

        XBTTreeWriter treeWriter = new XBTTreeWriter(new XBDocumentToXBTDocument(XBCoreTestSampleData.getSampleTwoBlocksWithTailTree()));
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
    public void testWriteSampleTwoBlocksTerminatedWithTail() throws Exception {
        XBConsumerToListener buffer = new XBConsumerToListener(null);
        XBCoreTestSampleData.writeSampleTwoBlocksTerminatedWithTail(buffer);
        BufferAssertXBFilter assertListener = new BufferAssertXBFilter(buffer.getTokens());

        XBTTreeWriter treeWriter = new XBTTreeWriter(new XBDocumentToXBTDocument(XBCoreTestSampleData.getSampleTwoBlocksTerminatedWithTailTree()));
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
