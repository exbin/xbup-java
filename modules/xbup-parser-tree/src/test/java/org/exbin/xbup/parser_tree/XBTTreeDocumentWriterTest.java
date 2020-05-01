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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import junit.framework.TestCase;
import org.exbin.xbup.core.block.XBDocument;
import org.exbin.xbup.core.parser.data.XBCoreTestSampleData;
import org.exbin.xbup.core.test.XBTestUtils;
import org.junit.Test;

/**
 * Test class for XBTTreeDocument.
 *
 * @version 0.2.0 2016/09/25
 * @author ExBin Project (http://exbin.org)
 */
public class XBTTreeDocumentWriterTest extends TestCase {

    public XBTTreeDocumentWriterTest(String testName) {
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
     * Tests XBTTreeDocument class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleEmpty() throws Exception {
        try (InputStream stream = XBTTreeDocumentWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_EMPTY)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            XBTTreeDocument document = new XBTTreeDocument();
            XBDocument sampleDocument = XBCoreTestSampleData.getSampleEmptyTree();
            document.setRoot(XBTTreeNode.createTreeCopy(new XBBlockToXBTBlock(sampleDocument.getRootBlock().get())));
            if (sampleDocument.getTailDataSize() > 0) {
                document.setTailData(sampleDocument.getTailData().orElse(null));
            }

            document.toStreamUB(target);
            XBTestUtils.assertEqualsInputStream(stream, new ByteArrayInputStream(target.toByteArray()));
        }
    }

    /**
     * Tests XBTTreeDocument class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleBlock() throws Exception {
        try (InputStream stream = XBTTreeDocumentWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_BLOCK)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            XBTTreeDocument document = new XBTTreeDocument();
            XBDocument sampleDocument = XBCoreTestSampleData.getSampleBlockTree();
            document.setRoot(XBTTreeNode.createTreeCopy(new XBBlockToXBTBlock(sampleDocument.getRootBlock().get())));
            if (sampleDocument.getTailDataSize() > 0) {
                document.setTailData(sampleDocument.getTailData().orElse(null));
            }

            document.toStreamUB(target);
            XBTestUtils.assertEqualsInputStream(stream, new ByteArrayInputStream(target.toByteArray()));
        }
    }

    /**
     * Tests XBTTreeDocument class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleBlockWithTail() throws Exception {
        try (InputStream stream = XBTTreeDocumentWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_BLOCK_WITH_TAIL)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            XBTTreeDocument document = new XBTTreeDocument();
            XBDocument sampleDocument = XBCoreTestSampleData.getSampleBlockWithTailTree();
            document.setRoot(XBTTreeNode.createTreeCopy(new XBBlockToXBTBlock(sampleDocument.getRootBlock().get())));
            if (sampleDocument.getTailDataSize() > 0) {
                document.setTailData(sampleDocument.getTailData().orElse(null));
            }

            document.toStreamUB(target);
            XBTestUtils.assertEqualsInputStream(stream, new ByteArrayInputStream(target.toByteArray()));
        }
    }

    /**
     * Tests XBTTreeDocument class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleBlockTerminated() throws Exception {
        try (InputStream stream = XBTTreeDocumentWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_BLOCK_TERMINATED)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            XBTTreeDocument document = new XBTTreeDocument();
            XBDocument sampleDocument = XBCoreTestSampleData.getSampleBlockTerminatedTree();
            document.setRoot(XBTTreeNode.createTreeCopy(new XBBlockToXBTBlock(sampleDocument.getRootBlock().get())));
            if (sampleDocument.getTailDataSize() > 0) {
                document.setTailData(sampleDocument.getTailData().orElse(null));
            }

            document.toStreamUB(target);
            XBTestUtils.assertEqualsInputStream(stream, new ByteArrayInputStream(target.toByteArray()));
        }
    }

    /**
     * Tests XBTTreeDocument class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleBlockTerminatedWithTail() throws Exception {
        try (InputStream stream = XBTTreeDocumentWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_BLOCK_TERMINATED_WITH_TAIL)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            XBTTreeDocument document = new XBTTreeDocument();
            XBDocument sampleDocument = XBCoreTestSampleData.getSampleBlockTerminatedWithTailTree();
            document.setRoot(XBTTreeNode.createTreeCopy(new XBBlockToXBTBlock(sampleDocument.getRootBlock().get())));
            if (sampleDocument.getTailDataSize() > 0) {
                document.setTailData(sampleDocument.getTailData().orElse(null));
            }

            document.toStreamUB(target);
            XBTestUtils.assertEqualsInputStream(stream, new ByteArrayInputStream(target.toByteArray()));
        }
    }

    /**
     * Tests XBTTreeDocument class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleData() throws Exception {
        try (InputStream stream = XBTTreeDocumentWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_DATA)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            XBTTreeDocument document = new XBTTreeDocument();
            XBDocument sampleDocument = XBCoreTestSampleData.getSampleDataTree();
            document.setRoot(XBTTreeNode.createTreeCopy(new XBBlockToXBTBlock(sampleDocument.getRootBlock().get())));
            if (sampleDocument.getTailDataSize() > 0) {
                document.setTailData(sampleDocument.getTailData().orElse(null));
            }

            document.toStreamUB(target);
            XBTestUtils.assertEqualsInputStream(stream, new ByteArrayInputStream(target.toByteArray()));
        }
    }

    /**
     * Tests XBTTreeDocument class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleDataWithTail() throws Exception {
        try (InputStream stream = XBTTreeDocumentWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_DATA_WITH_TAIL)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            XBTTreeDocument document = new XBTTreeDocument();
            XBDocument sampleDocument = XBCoreTestSampleData.getSampleDataWithTailTree();
            document.setRoot(XBTTreeNode.createTreeCopy(new XBBlockToXBTBlock(sampleDocument.getRootBlock().get())));
            if (sampleDocument.getTailDataSize() > 0) {
                document.setTailData(sampleDocument.getTailData().orElse(null));
            }

            document.toStreamUB(target);
            XBTestUtils.assertEqualsInputStream(stream, new ByteArrayInputStream(target.toByteArray()));
        }
    }

    /**
     * Tests XBTTreeDocument class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleDataTerminated() throws Exception {
        try (InputStream stream = XBTTreeDocumentWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_DATA_TERMINATED)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            XBTTreeDocument document = new XBTTreeDocument();
            XBDocument sampleDocument = XBCoreTestSampleData.getSampleDataTerminatedTree();
            document.setRoot(XBTTreeNode.createTreeCopy(new XBBlockToXBTBlock(sampleDocument.getRootBlock().get())));
            if (sampleDocument.getTailDataSize() > 0) {
                document.setTailData(sampleDocument.getTailData().orElse(null));
            }

            document.toStreamUB(target);
            XBTestUtils.assertEqualsInputStream(stream, new ByteArrayInputStream(target.toByteArray()));
        }
    }

    /**
     * Tests XBTTreeDocument class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleDataTerminatedWithTail() throws Exception {
        try (InputStream stream = XBTTreeDocumentWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_DATA_TERMINATED_WITH_TAIL)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            XBTTreeDocument document = new XBTTreeDocument();
            XBDocument sampleDocument = XBCoreTestSampleData.getSampleDataTerminatedWithTailTree();
            document.setRoot(XBTTreeNode.createTreeCopy(new XBBlockToXBTBlock(sampleDocument.getRootBlock().get())));
            if (sampleDocument.getTailDataSize() > 0) {
                document.setTailData(sampleDocument.getTailData().orElse(null));
            }

            document.toStreamUB(target);
            XBTestUtils.assertEqualsInputStream(stream, new ByteArrayInputStream(target.toByteArray()));
        }
    }

    /**
     * Tests XBTTreeDocument class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleBlockData() throws Exception {
        try (InputStream stream = XBTTreeDocumentWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_BLOCK_DATA)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            XBTTreeDocument document = new XBTTreeDocument();
            XBDocument sampleDocument = XBCoreTestSampleData.getSampleBlockDataTree();
            document.setRoot(XBTTreeNode.createTreeCopy(new XBBlockToXBTBlock(sampleDocument.getRootBlock().get())));
            if (sampleDocument.getTailDataSize() > 0) {
                document.setTailData(sampleDocument.getTailData().orElse(null));
            }

            document.toStreamUB(target);
            XBTestUtils.assertEqualsInputStream(stream, new ByteArrayInputStream(target.toByteArray()));
        }
    }

    /**
     * Tests XBTTreeDocument class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleBlockDataWithTail() throws Exception {
        try (InputStream stream = XBTTreeDocumentWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_BLOCK_DATA_WITH_TAIL)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            XBTTreeDocument document = new XBTTreeDocument();
            XBDocument sampleDocument = XBCoreTestSampleData.getSampleBlockDataWithTailTree();
            document.setRoot(XBTTreeNode.createTreeCopy(new XBBlockToXBTBlock(sampleDocument.getRootBlock().get())));
            if (sampleDocument.getTailDataSize() > 0) {
                document.setTailData(sampleDocument.getTailData().orElse(null));
            }

            document.toStreamUB(target);
            XBTestUtils.assertEqualsInputStream(stream, new ByteArrayInputStream(target.toByteArray()));
        }
    }

    /**
     * Tests XBTTreeDocument class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleBlockDataTerminated() throws Exception {
        try (InputStream stream = XBTTreeDocumentWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_BLOCK_DATA_TERMINATED)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            XBTTreeDocument document = new XBTTreeDocument();
            XBDocument sampleDocument = XBCoreTestSampleData.getSampleBlockDataTerminatedTree();
            document.setRoot(XBTTreeNode.createTreeCopy(new XBBlockToXBTBlock(sampleDocument.getRootBlock().get())));
            if (sampleDocument.getTailDataSize() > 0) {
                document.setTailData(sampleDocument.getTailData().orElse(null));
            }

            document.toStreamUB(target);
            XBTestUtils.assertEqualsInputStream(stream, new ByteArrayInputStream(target.toByteArray()));
        }
    }

    /**
     * Tests XBTTreeDocument class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleBlockDataTerminatedWithTail() throws Exception {
        try (InputStream stream = XBTTreeDocumentWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_BLOCK_DATA_TERMINATED_WITH_TAIL)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            XBTTreeDocument document = new XBTTreeDocument();
            XBDocument sampleDocument = XBCoreTestSampleData.getSampleBlockDataTerminatedWithTailTree();
            document.setRoot(XBTTreeNode.createTreeCopy(new XBBlockToXBTBlock(sampleDocument.getRootBlock().get())));
            if (sampleDocument.getTailDataSize() > 0) {
                document.setTailData(sampleDocument.getTailData().orElse(null));
            }

            document.toStreamUB(target);
            XBTestUtils.assertEqualsInputStream(stream, new ByteArrayInputStream(target.toByteArray()));
        }
    }

    /**
     * Tests XBTTreeDocument class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleBlockDataHybrid() throws Exception {
        try (InputStream stream = XBTTreeDocumentWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_BLOCK_DATA_HYBRID)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            XBTTreeDocument document = new XBTTreeDocument();
            XBDocument sampleDocument = XBCoreTestSampleData.getSampleBlockDataHybridTree();
            document.setRoot(XBTTreeNode.createTreeCopy(new XBBlockToXBTBlock(sampleDocument.getRootBlock().get())));
            if (sampleDocument.getTailDataSize() > 0) {
                document.setTailData(sampleDocument.getTailData().orElse(null));
            }

            document.toStreamUB(target);
            XBTestUtils.assertEqualsInputStream(stream, new ByteArrayInputStream(target.toByteArray()));
        }
    }

    /**
     * Tests XBTTreeDocument class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleBlockDataHybrid2() throws Exception {
        try (InputStream stream = XBTTreeDocumentWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_BLOCK_DATA_HYBRID2)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            XBTTreeDocument document = new XBTTreeDocument();
            XBDocument sampleDocument = XBCoreTestSampleData.getSampleBlockDataHybrid2Tree();
            document.setRoot(XBTTreeNode.createTreeCopy(new XBBlockToXBTBlock(sampleDocument.getRootBlock().get())));
            if (sampleDocument.getTailDataSize() > 0) {
                document.setTailData(sampleDocument.getTailData().orElse(null));
            }

            document.toStreamUB(target);
            XBTestUtils.assertEqualsInputStream(stream, new ByteArrayInputStream(target.toByteArray()));
        }
    }

    /**
     * Tests XBTTreeDocument class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleTwoBlocks() throws Exception {
        try (InputStream stream = XBTTreeDocumentWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_TWO_BLOCKS)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            XBTTreeDocument document = new XBTTreeDocument();
            XBDocument sampleDocument = XBCoreTestSampleData.getSampleTwoBlocksTree();
            document.setRoot(XBTTreeNode.createTreeCopy(new XBBlockToXBTBlock(sampleDocument.getRootBlock().get())));
            if (sampleDocument.getTailDataSize() > 0) {
                document.setTailData(sampleDocument.getTailData().orElse(null));
            }

            document.toStreamUB(target);
            XBTestUtils.assertEqualsInputStream(stream, new ByteArrayInputStream(target.toByteArray()));
        }
    }

    /**
     * Tests XBTTreeDocument class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleTwoBlocksWithTail() throws Exception {
        try (InputStream stream = XBTTreeDocumentWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_TWO_BLOCKS_WITH_TAIL)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            XBTTreeDocument document = new XBTTreeDocument();
            XBDocument sampleDocument = XBCoreTestSampleData.getSampleTwoBlocksWithTailTree();
            document.setRoot(XBTTreeNode.createTreeCopy(new XBBlockToXBTBlock(sampleDocument.getRootBlock().get())));
            if (sampleDocument.getTailDataSize() > 0) {
                document.setTailData(sampleDocument.getTailData().orElse(null));
            }

            document.toStreamUB(target);
            XBTestUtils.assertEqualsInputStream(stream, new ByteArrayInputStream(target.toByteArray()));
        }
    }

    /**
     * Tests XBTTreeDocument class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleTwoBlocksTerminated() throws Exception {
        try (InputStream stream = XBTTreeDocumentWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_TWO_BLOCKS_TERMINATED)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            XBTTreeDocument document = new XBTTreeDocument();
            XBDocument sampleDocument = XBCoreTestSampleData.getSampleTwoBlocksTerminatedTree();
            document.setRoot(XBTTreeNode.createTreeCopy(new XBBlockToXBTBlock(sampleDocument.getRootBlock().get())));
            if (sampleDocument.getTailDataSize() > 0) {
                document.setTailData(sampleDocument.getTailData().orElse(null));
            }

            document.toStreamUB(target);
            XBTestUtils.assertEqualsInputStream(stream, new ByteArrayInputStream(target.toByteArray()));
        }
    }

    /**
     * Tests XBTTreeDocument class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleTwoBlocksTerminatedWithTail() throws Exception {
        try (InputStream stream = XBTTreeDocumentWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_TWO_BLOCKS_TERMINATED_WITH_TAIL)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            XBTTreeDocument document = new XBTTreeDocument();
            XBDocument sampleDocument = XBCoreTestSampleData.getSampleTwoBlocksTerminatedWithTailTree();
            document.setRoot(XBTTreeNode.createTreeCopy(new XBBlockToXBTBlock(sampleDocument.getRootBlock().get())));
            if (sampleDocument.getTailDataSize() > 0) {
                document.setTailData(sampleDocument.getTailData().orElse(null));
            }

            document.toStreamUB(target);
            XBTestUtils.assertEqualsInputStream(stream, new ByteArrayInputStream(target.toByteArray()));
        }
    }

    /**
     * Tests XBTTreeDocument class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleTwoBlocksHybrid() throws Exception {
        try (InputStream stream = XBTTreeDocumentWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_TWO_BLOCKS_HYBRID)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            XBTTreeDocument document = new XBTTreeDocument();
            XBDocument sampleDocument = XBCoreTestSampleData.getSampleTwoBlocksHybridTree();
            document.setRoot(XBTTreeNode.createTreeCopy(new XBBlockToXBTBlock(sampleDocument.getRootBlock().get())));
            if (sampleDocument.getTailDataSize() > 0) {
                document.setTailData(sampleDocument.getTailData().orElse(null));
            }

            document.toStreamUB(target);
            XBTestUtils.assertEqualsInputStream(stream, new ByteArrayInputStream(target.toByteArray()));
        }
    }

    /**
     * Tests XBTTreeDocument class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleTwoBlocksHybrid2() throws Exception {
        try (InputStream stream = XBTTreeDocumentWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_TWO_BLOCKS_HYBRID2)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            XBTTreeDocument document = new XBTTreeDocument();
            XBDocument sampleDocument = XBCoreTestSampleData.getSampleTwoBlocksHybrid2Tree();
            document.setRoot(XBTTreeNode.createTreeCopy(new XBBlockToXBTBlock(sampleDocument.getRootBlock().get())));
            if (sampleDocument.getTailDataSize() > 0) {
                document.setTailData(sampleDocument.getTailData().orElse(null));
            }

            document.toStreamUB(target);
            XBTestUtils.assertEqualsInputStream(stream, new ByteArrayInputStream(target.toByteArray()));
        }
    }

    /**
     * Tests XBTTreeDocument class writing sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleSixBlocks() throws Exception {
        try (InputStream stream = XBTTreeDocumentWriterTest.class.getResourceAsStream(XBCoreTestSampleData.SAMPLE_SIX_BLOCKS)) {
            ByteArrayOutputStream target = new ByteArrayOutputStream();
            XBTTreeDocument document = new XBTTreeDocument();
            XBDocument sampleDocument = XBCoreTestSampleData.getSampleSixBlocksTree();
            document.setRoot(XBTTreeNode.createTreeCopy(new XBBlockToXBTBlock(sampleDocument.getRootBlock().get())));
            if (sampleDocument.getTailDataSize() > 0) {
                document.setTailData(sampleDocument.getTailData().orElse(null));
            }

            document.toStreamUB(target);
            XBTestUtils.assertEqualsInputStream(stream, new ByteArrayInputStream(target.toByteArray()));
        }
    }
}
