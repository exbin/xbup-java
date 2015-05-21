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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import static junit.framework.Assert.assertEquals;
import junit.framework.TestCase;
import org.junit.Test;
import org.xbup.lib.core.block.XBBlockDataMode;
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.parser.XBParseException;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.XBProcessingExceptionType;

/**
 * Test class for XBTreeDocument.
 *
 * @version 0.1.23 2014/02/17
 * @author XBUP Project (http://xbup.org)
 */
public class XBTreeDocumentReaderTest extends TestCase {

    public XBTreeDocumentReaderTest(String testName) {
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
    public void testRead() throws Exception {
        InputStream stream = getClass().getResourceAsStream("/org/xbup/lib/parser_tree/resources/test/samples/xhtml-test.xb");
        try {
            XBTreeDocument instance = new XBTreeDocument();
            instance.fromStreamUB(stream);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(XBTreeDocumentReaderTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("File not found");
        } catch (IOException | XBProcessingException ex) {
            Logger.getLogger(XBTreeDocumentReaderTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Processing error: " + ex.getMessage());
        }
    }

    /**
     * Test of open method of the class XBTreeDocument.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleSingleEmptyBlock() throws Exception {
        InputStream stream = getClass().getResourceAsStream("/org/xbup/lib/parser_tree/resources/test/samples/l0_singleemptyblock.xb");
        XBTreeDocument instance = new XBTreeDocument();
        instance.fromStreamUB(stream);

        assertNotNull(instance.getRootBlock());
        assertEquals(XBBlockTerminationMode.SIZE_SPECIFIED, instance.getRootBlock().getTerminationMode());

        assertEquals(0, instance.getRootBlock().getChildrenCount());
        assertEquals(1, instance.getRootBlock().getAttributesCount());
        assertEquals(0, instance.getRootBlock().getAttributeAt(0).getNaturalInt());
    }

    /**
     * Test of open method of the class XBTreeDocument.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleExtended() throws Exception {
        InputStream stream = getClass().getResourceAsStream("/org/xbup/lib/parser_tree/resources/test/samples/l0_extended.xb");
        XBTreeDocument instance = new XBTreeDocument();
        instance.fromStreamUB(stream);

        assertNotNull(instance.getRootBlock());
        assertEquals(XBBlockTerminationMode.SIZE_SPECIFIED, instance.getRootBlock().getTerminationMode());

        assertEquals(0, instance.getRootBlock().getChildrenCount());
        assertEquals(1, instance.getRootBlock().getAttributesCount());
        assertEquals(0, instance.getRootBlock().getAttributeAt(0).getNaturalInt());

        assertEquals(10, instance.getExtendedAreaSize());
    }

    /**
     * Test of open method of the class XBTreeDocument.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleEmpty() throws Exception {
        Throwable ex = null;

        InputStream stream = getClass().getResourceAsStream("/org/xbup/lib/parser_tree/resources/test/samples/corrupted/l0_empty.xb");
        XBTreeDocument instance = new XBTreeDocument();
        try {
            instance.fromStreamUB(stream);
        } catch (XBProcessingException | IOException e) {
            ex = e;
        }

        assertEquals(new XBProcessingException("End of data reached", XBProcessingExceptionType.UNEXPECTED_END_OF_STREAM), ex);
    }

    /**
     * Test of open method of the class XBTreeDocument.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleSingleByte() throws Exception {
        Throwable ex = null;

        InputStream stream = getClass().getResourceAsStream("/org/xbup/lib/parser_tree/resources/test/samples/corrupted/l0_singlebyte.xb");
        XBTreeDocument instance = new XBTreeDocument();
        try {
            instance.fromStreamUB(stream);
        } catch (XBProcessingException | IOException e) {
            ex = e;
        }

        assertEquals(new XBProcessingException("End of data reached", XBProcessingExceptionType.UNEXPECTED_END_OF_STREAM), ex);
    }

    /**
     * Test of open method of the class XBTreeDocument.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleWrongHeader() throws Exception {
        Throwable ex = null;

        InputStream stream = getClass().getResourceAsStream("/org/xbup/lib/parser_tree/resources/test/samples/corrupted/l0_wrongheader.xb");
        XBTreeDocument instance = new XBTreeDocument();
        try {
            instance.fromStreamUB(stream);
        } catch (XBProcessingException | IOException e) {
            ex = e;
        }

        assertEquals(new XBParseException("Unsupported header: 0xfe0059420002", XBProcessingExceptionType.CORRUPTED_HEADER), ex);
    }

    /**
     * Test of open method of the class XBTreeDocument.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleSingleData() throws Exception {
        InputStream stream = getClass().getResourceAsStream("/org/xbup/lib/parser_tree/resources/test/samples/l0_singledata.xb");
        XBTreeDocument instance = new XBTreeDocument();
        instance.fromStreamUB(stream);

        assertNotNull(instance.getRootBlock());
        assertEquals(XBBlockTerminationMode.SIZE_SPECIFIED, instance.getRootBlock().getTerminationMode());

        assertEquals(0, instance.getRootBlock().getChildrenCount());
        assertEquals(0, instance.getRootBlock().getAttributesCount());
        assertEquals(10, instance.getRootBlock().getDataSize());
    }

    /**
     * Test of open method of the class XBTreeDocument.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleTerminated() throws Exception {
        InputStream stream = getClass().getResourceAsStream("/org/xbup/lib/parser_tree/resources/test/samples/l0_terminated.xb");
        XBTreeDocument instance = new XBTreeDocument();
        instance.fromStreamUB(stream);

        assertNotNull(instance.getRootBlock());
        assertEquals(XBBlockTerminationMode.TERMINATED_BY_ZERO, instance.getRootBlock().getTerminationMode());

        assertEquals(0, instance.getRootBlock().getChildrenCount());
        assertEquals(4, instance.getRootBlock().getAttributesCount());
        assertEquals(0, instance.getRootBlock().getAttributeAt(0).getNaturalInt());
        assertEquals(0, instance.getRootBlock().getAttributeAt(1).getNaturalInt());
        assertEquals(1, instance.getRootBlock().getAttributeAt(2).getNaturalInt());
        assertEquals(2, instance.getRootBlock().getAttributeAt(3).getNaturalInt());
    }

    /**
     * Test of open method of the class XBTreeDocument.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleSixBlocks() throws Exception {
        InputStream stream = getClass().getResourceAsStream("/org/xbup/lib/parser_tree/resources/test/samples/l0_sixblocks.xb");
        XBTreeDocument instance = new XBTreeDocument();
        instance.fromStreamUB(stream);

        XBTreeNode rootNode = instance.getRootBlock();
        assertNotNull(rootNode);
        assertEquals(XBBlockTerminationMode.SIZE_SPECIFIED, rootNode.getTerminationMode());

        assertEquals(2, rootNode.getChildrenCount());
        assertEquals(1, rootNode.getAttributesCount());
        assertEquals(0, rootNode.getAttributeAt(0).getNaturalInt());

        XBTreeNode node1 = rootNode.getChildAt(0);
        assertEquals(1, node1.getChildrenCount());
        assertEquals(1, node1.getAttributesCount());
        assertEquals(0, node1.getAttributeAt(0).getNaturalInt());

        XBTreeNode node2 = node1.getChildAt(0);
        assertEquals(1, node2.getChildrenCount());
        assertEquals(1, node2.getAttributesCount());
        assertEquals(0, node2.getAttributeAt(0).getNaturalInt());

        XBTreeNode node3 = node2.getChildAt(0);
        assertEquals(0, node3.getChildrenCount());
        assertEquals(0, node3.getAttributesCount());
        assertEquals(XBBlockDataMode.DATA_BLOCK, node3.getDataMode());

        XBTreeNode node4 = rootNode.getChildAt(1);
        assertEquals(1, node4.getChildrenCount());
        assertEquals(1, node4.getAttributesCount());
        assertEquals(0, node4.getAttributeAt(0).getNaturalInt());

        XBTreeNode node5 = node4.getChildAt(0);
        assertEquals(0, node5.getChildrenCount());
        assertEquals(5, node5.getAttributesCount());
        assertEquals(0, node5.getAttributeAt(0).getNaturalInt());
        assertEquals(0, node5.getAttributeAt(1).getNaturalInt());
        assertEquals(1, node5.getAttributeAt(2).getNaturalInt());
        assertEquals(2, node5.getAttributeAt(3).getNaturalInt());
        assertEquals(3, node5.getAttributeAt(4).getNaturalInt());
    }
}
