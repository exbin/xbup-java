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
package org.xbup.lib.xb.parser.tree;

import org.xbup.lib.parser_tree.XBTreeDocument;
import org.xbup.lib.parser_tree.XBTreeNode;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import junit.framework.TestCase;
import org.junit.Test;
import org.xbup.lib.core.block.XBBlockDataMode;
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * Test class for XBTreeDocument.
 *
 * @version 0.1.23 2014/02/19
 * @author XBUP Project (http://xbup.org)
 */
public class XBTreeDocumentWriterTest extends TestCase {

    public XBTreeDocumentWriterTest(String testName) {
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
     * Test of open method of the class XBTreeDocument.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWrite() throws Exception {
        ByteArrayOutputStream target = new ByteArrayOutputStream();
        try {
            XBTreeDocument document = new XBTreeDocument();
            XBTreeNode node = new XBTreeNode();
            node.setTerminationMode(XBBlockTerminationMode.SIZE_SPECIFIED);
            node.addAttribute(new UBNat32(1));
            node.addAttribute(new UBNat32(2));

            document.setRootBlock(node);
            document.toStreamUB(target);
        } catch (FileNotFoundException ex) {
            fail("File not found");
        } catch (IOException | XBProcessingException ex) {
            fail("Processing error: " + ex.getMessage());
        }
    }

    /**
     * Test of open method of the class XBTreeDocument.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleSingleEmptyBlock() throws Exception {
        InputStream stream = getClass().getResourceAsStream("/org/xbup/lib/xb/test/resources/samples/l0_singleemptyblock.xb");
        ByteArrayOutputStream target = new ByteArrayOutputStream();
        XBTreeDocument document = new XBTreeDocument();

        XBTreeNode node = new XBTreeNode();
        node.setTerminationMode(XBBlockTerminationMode.SIZE_SPECIFIED);
        node.addAttribute(new UBNat32(0));

        document.setRootBlock(node);
        document.toStreamUB(target);
        assertEqualsInputStream(stream, new ByteArrayInputStream(target.toByteArray()));
    }

    /**
     * Test of open method of the class XBTreeDocument.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleExtended() throws Exception {
        InputStream stream = getClass().getResourceAsStream("/org/xbup/lib/xb/test/resources/samples/l0_extended.xb");
        ByteArrayOutputStream target = new ByteArrayOutputStream();
        XBTreeDocument document = new XBTreeDocument();

        XBTreeNode node = new XBTreeNode();
        node.setTerminationMode(XBBlockTerminationMode.SIZE_SPECIFIED);
        node.addAttribute(new UBNat32(0));
        document.setRootBlock(node);

        byte[] data = {48, 49, 50, 51, 52, 53, 54, 55, 56, 57};
        ByteArrayInputStream dataStream = new ByteArrayInputStream(data);
        document.setExtendedArea(dataStream);

        document.toStreamUB(target);
        assertEqualsInputStream(stream, new ByteArrayInputStream(target.toByteArray()));
    }

    /**
     * Test of open method of the class XBTreeDocument.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleSingleData() throws Exception {
        InputStream stream = getClass().getResourceAsStream("/org/xbup/lib/xb/test/resources/samples/l0_singledata.xb");
        ByteArrayOutputStream target = new ByteArrayOutputStream();
        XBTreeDocument document = new XBTreeDocument();

        XBTreeNode node = new XBTreeNode();
        node.setDataMode(XBBlockDataMode.DATA_BLOCK);
        node.setTerminationMode(XBBlockTerminationMode.SIZE_SPECIFIED);
        byte[] data = {48, 49, 50, 51, 52, 53, 54, 55, 56, 57};
        ByteArrayInputStream dataStream = new ByteArrayInputStream(data);
        node.setData(dataStream);

        document.setRootBlock(node);
        document.toStreamUB(target);

        assertEqualsInputStream(stream, new ByteArrayInputStream(target.toByteArray()));
    }

    /**
     * Test of open method of the class XBTreeDocument.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleTerminated() throws Exception {
        InputStream stream = getClass().getResourceAsStream("/org/xbup/lib/xb/test/resources/samples/l0_terminated.xb");
        ByteArrayOutputStream target = new ByteArrayOutputStream();
        XBTreeDocument document = new XBTreeDocument();

        XBTreeNode node = new XBTreeNode();
        node.setTerminationMode(XBBlockTerminationMode.TERMINATED_BY_ZERO);
        node.addAttribute(new UBNat32(0));
        node.addAttribute(new UBNat32(0));
        node.addAttribute(new UBNat32(1));
        node.addAttribute(new UBNat32(2));

        document.setRootBlock(node);
        document.toStreamUB(target);

        assertEqualsInputStream(stream, new ByteArrayInputStream(target.toByteArray()));
    }

    /**
     * Test of open method of the class XBTreeDocument.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testWriteSampleSixBlocks() throws Exception {
        InputStream stream = getClass().getResourceAsStream("/org/xbup/lib/xb/test/resources/samples/l0_sixblocks.xb");
        ByteArrayOutputStream target = new ByteArrayOutputStream();
        XBTreeDocument document = new XBTreeDocument();

        XBTreeNode rootNode = new XBTreeNode();
        rootNode.setTerminationMode(XBBlockTerminationMode.SIZE_SPECIFIED);
        rootNode.addAttribute(new UBNat32(0));

        XBTreeNode node1 = new XBTreeNode(rootNode);
        rootNode.getChildren().add(node1);
        node1.setTerminationMode(XBBlockTerminationMode.SIZE_SPECIFIED);
        node1.addAttribute(new UBNat32(0));

        XBTreeNode node2 = new XBTreeNode(node1);
        node1.getChildren().add(node2);
        node2.setTerminationMode(XBBlockTerminationMode.SIZE_SPECIFIED);
        node2.addAttribute(new UBNat32(0));

        XBTreeNode node3 = new XBTreeNode(node2);
        node2.getChildren().add(node3);
        node3.setDataMode(XBBlockDataMode.DATA_BLOCK);
        node3.setTerminationMode(XBBlockTerminationMode.SIZE_SPECIFIED);
        byte[] data = {};
        ByteArrayInputStream dataStream = new ByteArrayInputStream(data);
        node3.setData(dataStream);

        XBTreeNode node4 = new XBTreeNode(rootNode);
        rootNode.getChildren().add(node4);
        node4.setTerminationMode(XBBlockTerminationMode.SIZE_SPECIFIED);
        node4.addAttribute(new UBNat32(0));

        XBTreeNode node5 = new XBTreeNode(node4);
        node4.getChildren().add(node5);
        node5.setTerminationMode(XBBlockTerminationMode.SIZE_SPECIFIED);
        node5.addAttribute(new UBNat32(0));
        node5.addAttribute(new UBNat32(0));
        node5.addAttribute(new UBNat32(1));
        node5.addAttribute(new UBNat32(2));
        node5.addAttribute(new UBNat32(3));

        document.setRootBlock(rootNode);
        document.toStreamUB(target);

        assertEqualsInputStream(stream, new ByteArrayInputStream(target.toByteArray()));
    }

    private void assertEqualsInputStream(InputStream expectedStream, InputStream stream) {
        try {
            byte[] dataBlob = new byte[2];
            byte[] dataBlob2 = new byte[2];
            int position = 0;
            while (expectedStream.available() > 0) {
                int readStat = expectedStream.read(dataBlob, 0, 1);
                if (readStat < 0) {
                    fail("Unable to read expected stream on position " + position);
                }
                int readStat2 = stream.read(dataBlob2, 0, 1);
                if (readStat2 < 0) {
                    fail("Unable to read compared stream on position " + position);
                }

                assertEquals(dataBlob[0], dataBlob2[0]);
                position++;
            }

            assertTrue(stream.available() == 0);
        } catch (IOException ex) {
            fail("IOException " + ex.getMessage());
        }
    }
}
