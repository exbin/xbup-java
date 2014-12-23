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

import java.util.ArrayList;
import org.xbup.lib.core.block.XBTBlock;
import org.xbup.lib.core.block.definition.XBBlockParam;
import org.xbup.lib.core.catalog.XBACatalog;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * Extracting specified parameters from XBUP level 1 blocks.
 *
 * TODO
 *
 * @version 0.1.24 2014/12/20
 * @author XBUP Project (http://xbup.org)
 */
public class XBTreeParamExtractor {

    private final XBTBlock source;
    private final long targetParam;

    private XBBlockParam paramType;
    private long attributeStart = 0;
    private long attributeCount = 0;
    private long blockStart = 0;
    private long blockCount = 0;

    private long currentParam = 0;

    public XBTreeParamExtractor(XBTBlock source, XBACatalog catalog, long targetParamValue) {
        this.targetParam = targetParamValue;
        this.source = source;
    }

    public XBTTreeNode getOutput() {
        XBTTreeNode output = new XBTTreeNode();
        output.setBlockDecl(paramType.getBlockDecl());
        output.setChildren(new ArrayList<XBTBlock>());
        if (paramType.getParamType().isJoin()) {
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

    public XBBlockParam getParamType() {
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
