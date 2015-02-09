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
import org.xbup.lib.core.parser.XBParserMode;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.XBProcessingExceptionType;
import org.xbup.lib.core.parser.basic.XBListener;
import org.xbup.lib.core.parser.token.XBAttributeToken;
import org.xbup.lib.core.parser.token.XBBeginToken;
import org.xbup.lib.core.parser.token.XBDataToken;
import org.xbup.lib.core.parser.token.XBToken;
import org.xbup.lib.core.parser.token.event.XBEventListener;
import org.xbup.lib.core.parser.token.event.XBEventReader;

/**
 * Test class for XBTreeReader.
 *
 * @version 0.1.24 2014/10/07
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
     * Test of open method of the class XBTreeReader.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testRead() throws Exception {
        InputStream stream = getClass().getResourceAsStream("/org/xbup/lib/parser_tree/resources/test/samples/xhtml-test.xb");
        try {
            XBTreeNode instance = new XBTreeNode();
            XBTreeReader treeReader = new XBTreeReader(instance, true);
            XBEventReader reader = new XBEventReader(stream, XBParserMode.SKIP_EXTENDED);
            reader.attachXBEventListener(new DebugListener(treeReader));
            reader.read();
            reader.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(XBTreeReaderTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("File not found");
        } catch (IOException | XBProcessingException ex) {
            Logger.getLogger(XBTreeReaderTest.class.getName()).log(Level.SEVERE, null, ex);
            fail("Processing error: " + ex.getMessage());
        }
    }

    /**
     * Test of open method of the class XBTreeReader.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleSingleEmptyBlock() throws Exception {
        InputStream stream = getClass().getResourceAsStream("/org/xbup/lib/parser_tree/resources/test/samples/l0_singleemptyblock.xb");
        XBTreeNode instance = new XBTreeNode();
        XBTreeReader treeReader = new XBTreeReader(instance, true);
        XBEventReader reader = new XBEventReader(stream, XBParserMode.SKIP_EXTENDED);
        reader.attachXBEventListener(new DebugListener(treeReader));
        reader.read();
        reader.close();

        assertEquals(XBBlockTerminationMode.SIZE_SPECIFIED, instance.getTerminationMode());

        assertEquals(0, instance.getChildCount());
        assertEquals(1, instance.getAttributesCount());
        assertEquals(0, instance.getAttribute(0).getNaturalInt());
    }

    /**
     * Test of open method of the class XBTreeReader.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleExtended() throws Exception {
        InputStream stream = getClass().getResourceAsStream("/org/xbup/lib/parser_tree/resources/test/samples/l0_extended.xb");
        XBTreeNode instance = new XBTreeNode();
        XBTreeReader treeReader = new XBTreeReader(instance, true);
        XBEventReader reader = new XBEventReader(stream, XBParserMode.SKIP_EXTENDED);
        reader.attachXBEventListener(new DebugListener(treeReader));
        reader.read();
        reader.close();

        assertEquals(XBBlockTerminationMode.SIZE_SPECIFIED, instance.getTerminationMode());

        assertEquals(0, instance.getChildCount());
        assertEquals(1, instance.getAttributesCount());
        assertEquals(0, instance.getAttribute(0).getNaturalInt());
    }

    /**
     * Test of open method of the class XBTreeReader.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleEmpty() throws Exception {
        Throwable ex = null;

        InputStream stream = getClass().getResourceAsStream("/org/xbup/lib/parser_tree/resources/test/samples/corrupted/l0_empty.xb");
        XBTreeNode instance = new XBTreeNode();
        XBTreeReader treeReader = new XBTreeReader(instance, true);

        try {
            XBEventReader reader = new XBEventReader(stream, XBParserMode.SKIP_EXTENDED);
            reader.attachXBEventListener(new DebugListener(treeReader));
            reader.read();
            reader.close();
        } catch (XBProcessingException | IOException e) {
            ex = e;
        }

        assertEquals(new XBProcessingException("End of data reached", XBProcessingExceptionType.UNEXPECTED_END_OF_STREAM), ex);
    }

    /**
     * Test of open method of the class XBTreeReader.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleSingleByte() throws Exception {
        Throwable ex = null;

        InputStream stream = getClass().getResourceAsStream("/org/xbup/lib/parser_tree/resources/test/samples/corrupted/l0_singlebyte.xb");
        XBTreeNode instance = new XBTreeNode();
        XBTreeReader treeReader = new XBTreeReader(instance, true);

        try {
            XBEventReader reader = new XBEventReader(stream, XBParserMode.SKIP_EXTENDED);
            reader.attachXBEventListener(new DebugListener(treeReader));
            reader.read();
            reader.close();
        } catch (XBProcessingException | IOException e) {
            ex = e;
        }

        assertEquals(new XBProcessingException("End of data reached", XBProcessingExceptionType.UNEXPECTED_END_OF_STREAM), ex);
    }

    /**
     * Test of open method of the class XBTreeReader.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleWrongHeader() throws Exception {
        Throwable ex = null;

        InputStream stream = getClass().getResourceAsStream("/org/xbup/lib/parser_tree/resources/test/samples/corrupted/l0_wrongheader.xb");
        XBTreeNode instance = new XBTreeNode();
        XBTreeReader treeReader = new XBTreeReader(instance, true);

        try {
            XBEventReader reader = new XBEventReader(stream, XBParserMode.SKIP_EXTENDED);
            reader.attachXBEventListener(new DebugListener(treeReader));
            reader.read();
            reader.close();
        } catch (XBProcessingException | IOException e) {
            ex = e;
        }

        assertEquals(new XBParseException("Unsupported header: 0xfe0059420002", XBProcessingExceptionType.CORRUPTED_HEADER), ex);
    }

    /**
     * Test of open method of the class XBTreeReader.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleSingleData() throws Exception {
        InputStream stream = getClass().getResourceAsStream("/org/xbup/lib/parser_tree/resources/test/samples/l0_singledata.xb");
        XBTreeNode instance = new XBTreeNode();
        XBTreeReader treeReader = new XBTreeReader(instance, true);
        XBEventReader reader = new XBEventReader(stream, XBParserMode.SKIP_EXTENDED);
        reader.attachXBEventListener(new DebugListener(treeReader));
        reader.read();
        reader.close();

        assertEquals(XBBlockTerminationMode.SIZE_SPECIFIED, instance.getTerminationMode());

        assertEquals(0, instance.getChildCount());
        assertEquals(0, instance.getAttributesCount());
        assertEquals(10, instance.getDataSize());
    }

    /**
     * Test of open method of the class XBTreeReader.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleTerminated() throws Exception {
        InputStream stream = getClass().getResourceAsStream("/org/xbup/lib/parser_tree/resources/test/samples/l0_terminated.xb");
        XBTreeNode instance = new XBTreeNode();
        XBTreeReader treeReader = new XBTreeReader(instance, true);
        XBEventReader reader = new XBEventReader(stream, XBParserMode.SKIP_EXTENDED);
        reader.attachXBEventListener(new DebugListener(treeReader));
        reader.read();
        reader.close();

        assertEquals(XBBlockTerminationMode.TERMINATED_BY_ZERO, instance.getTerminationMode());

        assertEquals(0, instance.getChildCount());
        assertEquals(4, instance.getAttributesCount());
        assertEquals(0, instance.getAttribute(0).getNaturalInt());
        assertEquals(0, instance.getAttribute(1).getNaturalInt());
        assertEquals(1, instance.getAttribute(2).getNaturalInt());
        assertEquals(2, instance.getAttribute(3).getNaturalInt());
    }

    /**
     * Test of open method of the class XBTreeReader.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleSixBlocks() throws Exception {
        InputStream stream = getClass().getResourceAsStream("/org/xbup/lib/parser_tree/resources/test/samples/l0_sixblocks.xb");
        XBTreeNode instance = new XBTreeNode();
        XBTreeReader treeReader = new XBTreeReader(instance, true);
        XBEventReader reader = new XBEventReader(stream, XBParserMode.SKIP_EXTENDED);
        reader.attachXBEventListener(new DebugListener(treeReader));
        reader.read();
        reader.close();

        XBTreeNode rootNode = instance;
        assertNotNull(rootNode);
        assertEquals(XBBlockTerminationMode.SIZE_SPECIFIED, rootNode.getTerminationMode());

        assertEquals(2, rootNode.getChildCount());
        assertEquals(1, rootNode.getAttributesCount());
        assertEquals(0, rootNode.getAttribute(0).getNaturalInt());

        XBTreeNode node1 = rootNode.getChildAt(0);
        assertEquals(1, node1.getChildCount());
        assertEquals(1, node1.getAttributesCount());
        assertEquals(0, node1.getAttribute(0).getNaturalInt());

        XBTreeNode node2 = node1.getChildAt(0);
        assertEquals(1, node2.getChildCount());
        assertEquals(1, node2.getAttributesCount());
        assertEquals(0, node2.getAttribute(0).getNaturalInt());

        XBTreeNode node3 = node2.getChildAt(0);
        assertEquals(0, node3.getChildCount());
        assertEquals(0, node3.getAttributesCount());
        assertEquals(XBBlockDataMode.DATA_BLOCK, node3.getDataMode());

        XBTreeNode node4 = rootNode.getChildAt(1);
        assertEquals(1, node4.getChildCount());
        assertEquals(1, node4.getAttributesCount());
        assertEquals(0, node4.getAttribute(0).getNaturalInt());

        XBTreeNode node5 = node4.getChildAt(0);
        assertEquals(0, node5.getChildCount());
        assertEquals(5, node5.getAttributesCount());
        assertEquals(0, node5.getAttribute(0).getNaturalInt());
        assertEquals(0, node5.getAttribute(1).getNaturalInt());
        assertEquals(1, node5.getAttribute(2).getNaturalInt());
        assertEquals(2, node5.getAttribute(3).getNaturalInt());
        assertEquals(3, node5.getAttribute(4).getNaturalInt());
    }

    private class DebugListener implements XBEventListener {

        private final XBListener listener;

        public DebugListener(XBListener listener) {
            this.listener = listener;
        }

        @Override
        public void putXBToken(XBToken token) throws XBProcessingException, IOException {
            switch (token.getTokenType()) {
                case BEGIN: {
                    System.out.println("> Begin (" + ((XBBeginToken) token).getTerminationMode().toString() + "):");
                    listener.beginXB(((XBBeginToken) token).getTerminationMode());
                    break;
                }
                case ATTRIBUTE: {
                    System.out.println("  Attribute: " + ((XBAttributeToken) token).getAttribute().getNaturalLong());
                    listener.attribXB(((XBAttributeToken) token).getAttribute());
                    break;
                }
                case DATA: {
                    System.out.println("  Data:" + ((XBDataToken) token).getData().available());
                    listener.dataXB(((XBDataToken) token).getData());
                    break;
                }
                case END: {
                    System.out.println("< End.");
                    listener.endXB();
                    break;
                }
            }
        }
    }
}
