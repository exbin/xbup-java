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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import junit.framework.TestCase;
import org.junit.Test;
import org.xbup.lib.core.block.XBBlock;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.ubnumber.UBNatural;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * Test class for XBTreeDocument.
 *
 * @version 0.1.23 2013/11/07
 * @author XBUP Project (http://xbup.org)
 */
public class XBTreeDocumentTest extends TestCase {

    public XBTreeDocumentTest(String testName) {
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
     * Test of open method, of class XBTreeDocument.
     * 
     * @throws java.lang.Exception
     */
    @Test
    public void testStreamUB() throws Exception {
        XBTreeDocument doc = new XBTreeDocument();
        ByteArrayOutputStream data;
        data = new ByteArrayOutputStream();
        try {
            XBTreeNode node = new XBTreeNode();
            List<UBNatural> attrs = new ArrayList<>();
            attrs.add(new UBNat32(10));
            attrs.add(new UBNat32(20));
            attrs.add(new UBNat32(30));
            node.setAttributes(attrs);
            List<XBBlock> children = new ArrayList<>();
            XBTreeNode node2 = new XBTreeNode(node);
            children.add(node2);
            node.setChildren(children);
            doc.setRootBlock(node);
            doc.toStreamUB(data);
            data.flush();
        } catch (IOException ex) {
            fail("Exception: "+ex.getMessage());
        }

        ByteArrayInputStream data2;
        data2 = new ByteArrayInputStream(data.toByteArray());
        XBTreeDocument xbdoc = new XBTreeDocument();
        try {
            xbdoc.fromStreamUB(data2);
        } catch (XBProcessingException | IOException ex) {
            fail("Exception: "+ex.getMessage());
        }
    }

}
