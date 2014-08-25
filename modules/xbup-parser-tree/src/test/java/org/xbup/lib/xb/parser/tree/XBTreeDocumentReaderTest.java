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
        InputStream stream = getClass().getResourceAsStream("/org/xbup/lib/xb/test/resources/samples/xhtml-test.xb");
        try {
            XBTreeDocument instance = new XBTreeDocument();
            instance.fromStreamUB(stream);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(XBTreeDocumentReaderTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("File not found");
        } catch (IOException | XBProcessingException ex) {
            Logger.getLogger(XBTreeDocumentReaderTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Processing error: "+ex.getMessage());
        }
    }

    /**
     * Test of open method of the class XBTreeDocument.
     * 
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleSingleEmptyBlock() throws Exception {
        InputStream stream = getClass().getResourceAsStream("/org/xbup/lib/xb/test/resources/samples/l0_singleemptyblock.xb");
        XBTreeDocument instance = new XBTreeDocument();
        instance.fromStreamUB(stream);

        assertNotNull(instance.getRootBlock());
        assertEquals(XBBlockTerminationMode.SIZE_SPECIFIED, instance.getRootBlock().getTerminationMode());

        assertEquals(0, instance.getRootBlock().getChildCount());
        assertEquals(1, instance.getRootBlock().getAttributesCount());
        assertEquals(0, instance.getRootBlock().getAttribute(0).getInt());
    }

    /**
     * Test of open method of the class XBTreeDocument.
     * 
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleExtended() throws Exception {
        InputStream stream = getClass().getResourceAsStream("/org/xbup/lib/xb/test/resources/samples/l0_extended.xb");
        XBTreeDocument instance = new XBTreeDocument();
        instance.fromStreamUB(stream);
        
        assertNotNull(instance.getRootBlock());
        assertEquals(XBBlockTerminationMode.SIZE_SPECIFIED, instance.getRootBlock().getTerminationMode());
        
        assertEquals(0, instance.getRootBlock().getChildCount());
        assertEquals(1, instance.getRootBlock().getAttributesCount());
        assertEquals(0, instance.getRootBlock().getAttribute(0).getInt());

        assertEquals(10, instance.getExtendedSize());
    }

    /**
     * Test of open method of the class XBTreeDocument.
     * 
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleEmpty() throws Exception {
        Throwable ex = null;

        InputStream stream = getClass().getResourceAsStream("/org/xbup/lib/xb/test/resources/samples/corrupted/l0_empty.xb");
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

        InputStream stream = getClass().getResourceAsStream("/org/xbup/lib/xb/test/resources/samples/corrupted/l0_singlebyte.xb");
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

        InputStream stream = getClass().getResourceAsStream("/org/xbup/lib/xb/test/resources/samples/corrupted/l0_wrongheader.xb");
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
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleSingleData() throws Exception {
        InputStream stream = getClass().getResourceAsStream("/org/xbup/lib/xb/test/resources/samples/l0_singledata.xb");
        XBTreeDocument instance = new XBTreeDocument();
        instance.fromStreamUB(stream);

        assertNotNull(instance.getRootBlock());
        assertEquals(XBBlockTerminationMode.SIZE_SPECIFIED, instance.getRootBlock().getTerminationMode());

        assertEquals(0, instance.getRootBlock().getChildCount());
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
        InputStream stream = getClass().getResourceAsStream("/org/xbup/lib/xb/test/resources/samples/l0_terminated.xb");
        XBTreeDocument instance = new XBTreeDocument();
        instance.fromStreamUB(stream);

        assertNotNull(instance.getRootBlock());
        assertEquals(XBBlockTerminationMode.ZERO_TERMINATED, instance.getRootBlock().getTerminationMode());

        assertEquals(0, instance.getRootBlock().getChildCount());
        assertEquals(4, instance.getRootBlock().getAttributesCount());
        assertEquals(0, instance.getRootBlock().getAttribute(0).getInt());
        assertEquals(0, instance.getRootBlock().getAttribute(1).getInt());
        assertEquals(1, instance.getRootBlock().getAttribute(2).getInt());
        assertEquals(2, instance.getRootBlock().getAttribute(3).getInt());
    }

    /**
     * Test of open method of the class XBTreeDocument.
     * 
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleSixBlocks() throws Exception {
        InputStream stream = getClass().getResourceAsStream("/org/xbup/lib/xb/test/resources/samples/l0_sixblocks.xb");
        XBTreeDocument instance = new XBTreeDocument();
        instance.fromStreamUB(stream);

        XBTreeNode rootNode = instance.getRootBlock();
        assertNotNull(rootNode);
        assertEquals(XBBlockTerminationMode.SIZE_SPECIFIED, rootNode.getTerminationMode());

        assertEquals(2, rootNode.getChildCount());
        assertEquals(1, rootNode.getAttributesCount());
        assertEquals(0, rootNode.getAttribute(0).getInt());

        XBTreeNode node1 = rootNode.getChildAt(0);
        assertEquals(1, node1.getChildCount());
        assertEquals(1, node1.getAttributesCount());
        assertEquals(0, node1.getAttribute(0).getInt());
        
        XBTreeNode node2 = node1.getChildAt(0);
        assertEquals(1, node2.getChildCount());
        assertEquals(1, node2.getAttributesCount());
        assertEquals(0, node2.getAttribute(0).getInt());

        XBTreeNode node3 = node2.getChildAt(0);
        assertEquals(0, node3.getChildCount());
        assertEquals(0, node3.getAttributesCount());
        assertEquals(XBBlockDataMode.DATA_BLOCK, node3.getDataMode());
                
        XBTreeNode node4 = rootNode.getChildAt(1);
        assertEquals(1, node4.getChildCount());
        assertEquals(1, node4.getAttributesCount());
        assertEquals(0, node4.getAttribute(0).getInt());

        XBTreeNode node5 = node4.getChildAt(0);
        assertEquals(0, node5.getChildCount());
        assertEquals(5, node5.getAttributesCount());
        assertEquals(0, node5.getAttribute(0).getInt());
        assertEquals(0, node5.getAttribute(1).getInt());
        assertEquals(1, node5.getAttribute(2).getInt());
        assertEquals(2, node5.getAttribute(3).getInt());
        assertEquals(3, node5.getAttribute(4).getInt());
    }
}
