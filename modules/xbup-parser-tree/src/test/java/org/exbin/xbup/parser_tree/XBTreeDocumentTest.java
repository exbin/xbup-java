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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import junit.framework.TestCase;
import org.junit.Test;
import org.exbin.xbup.core.block.XBBlock;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.parser.token.XBAttribute;
import org.exbin.xbup.core.ubnumber.type.UBNat32;

/**
 * Test class for XBTreeDocument.
 *
 * @version 0.2.0 2016/09/25
 * @author ExBin Project (http://exbin.org)
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
     * Tests open method of the XBTreeDocument class.
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
            XBAttribute[] attrs = {new UBNat32(10), new UBNat32(20), new UBNat32(30)};
            node.setAttributes(attrs);
            XBBlock[] children = {new XBTreeNode(node)};
            node.setChildren(children);
            doc.setRootBlock(node);
            doc.toStreamUB(data);
            data.flush();
        } catch (IOException ex) {
            fail("Exception: " + ex.getMessage());
        }

        ByteArrayInputStream data2;
        data2 = new ByteArrayInputStream(data.toByteArray());
        XBTreeDocument xbdoc = new XBTreeDocument();
        try {
            xbdoc.fromStreamUB(data2);
        } catch (XBProcessingException | IOException ex) {
            fail("Exception: " + ex.getMessage());
        }
    }

}
