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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import junit.framework.TestCase;
import org.exbin.xbup.core.block.XBBlock;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.parser.token.XBAttribute;
import org.exbin.xbup.core.ubnumber.type.UBNat32;
import org.junit.Test;

/**
 * Test class for XBTreeDocument.
 *
 * @author ExBin Project (https://exbin.org)
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
