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

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xbup.lib.core.block.XBTBlock;
import org.xbup.lib.core.block.param.XBParamDecl;
import org.xbup.lib.core.catalog.XBACatalog;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.param.XBParamConvertor;
import org.xbup.lib.core.parser.param.XBParamListener;
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * Extracting specified parameters from XBUP level 1 blocks.
 *
 * @version 0.1.23 2014/02/20
 * @author XBUP Project (http://xbup.org)
 */
public class XBTreeParamExtractor implements XBParamListener {

    private final XBTBlock source;
    private final long targetParam;

    private XBParamDecl paramType;
    private long attributeStart = 0;
    private long attributeCount = 0;
    private long blockStart = 0;
    private long blockCount = 0;

    private long currentParam = 0;

    public XBTreeParamExtractor(XBTBlock source, XBACatalog catalog, long targetParam) {
        this.targetParam = targetParam;
        this.source = source;
        XBParamConvertor convertor = new XBParamConvertor(this, catalog);
        try {
            convertor.beginXBT(XBBlockTerminationMode.SIZE_SPECIFIED);
            convertor.typeXBT(source.getBlockType());
            int attribute = 0;
            while (currentParam <= targetParam) {
                if (attribute < source.getAttributesCount()) {
                    convertor.attribXBT(source.getAttribute(attribute));
                } else {
                    convertor.attribXBT(new UBNat32());
                }
                attribute++;
                if (currentParam < targetParam) {
                    attributeStart++;
                } else {
                    if (currentParam == targetParam) {
                        attributeCount++;
                    }
                }
            }
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBTreeParamExtractor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void beginXBParam(XBParamDecl type) throws XBProcessingException, IOException {
        if (currentParam == targetParam) {
            paramType = type;
        }
    }

    @Override
    public void blockXBParam() throws XBProcessingException, IOException {
        if (currentParam < targetParam) {
            blockStart++;
        } else {
            if (currentParam == targetParam) {
                blockCount++;
            }
        }
    }

    @Override
    public void listXBParam() throws XBProcessingException, IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void endXBParam() throws XBProcessingException, IOException {
        currentParam++;
    }

    public XBTTreeNode getOutput() {
        XBTTreeNode output = new XBTTreeNode();
        output.setBlockDecl(paramType.getBlockDecl());
        output.setChildren(new ArrayList<XBTBlock>());
        if (paramType.isJoinFlag()) {
            long attribute = attributeStart;
            if (attribute < source.getAttributesCount()) {
                output.addAttribute(new UBNat32(source.getAttribute((int) attribute)));
                attribute++;
            }
        }

        long block = blockStart;
        while (block < blockStart + blockCount) {
            XBTTreeNode node;
            if (block < source.getChildCount()) {
                node = ((XBTTreeNode) source.getChildAt((int) block)).cloneNode(true);
            } else {
                node = new XBTTreeNode();
            }
            node.setParent(output);
            output.getChildren().add(node);
            block++;
        }

        return output;
    }

    public XBParamDecl getParamType() {
        return paramType;
    }

    public long getAttributeStart() {
        return attributeStart;
    }

    public long getAttributeCount() {
        return attributeCount;
    }

    public long getBlockStart() {
        return blockStart;
    }

    public long getBlockCount() {
        return blockCount;
    }
}