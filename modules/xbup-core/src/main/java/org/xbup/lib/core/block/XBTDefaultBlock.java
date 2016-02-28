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
package org.xbup.lib.core.block;

import java.io.InputStream;
import org.xbup.lib.core.parser.token.XBAttribute;
import org.xbup.lib.core.type.XBData;

/**
 * Basic plain implementation of XBTBlock interface.
 *
 * @version 0.2.0 2015/09/19
 * @author ExBin Project (http://exbin.org)
 */
public class XBTDefaultBlock implements XBTBlock {

    private XBTBlock parent;
    private final XBBlockDataMode dataMode;
    private final XBBlockTerminationMode terminationMode;
    private final XBBlockType blockType;
    private final XBAttribute[] attributes;
    private final XBTBlock[] children;
    private final XBBlockData data;

    /**
     * Creates new instance of XBDefaultBlock as an empty data block.
     */
    public XBTDefaultBlock() {
        this(null, XBBlockTerminationMode.SIZE_SPECIFIED, new XBData());
    }

    /**
     * Creates new instance of XBTDefaultBlock as a data block with given
     * values.
     *
     * @param parent parent node
     * @param terminationMode termination mode
     * @param data block data
     */
    public XBTDefaultBlock(XBTBlock parent, XBBlockTerminationMode terminationMode, XBBlockData data) {
        dataMode = XBBlockDataMode.DATA_BLOCK;
        this.parent = parent;
        this.terminationMode = terminationMode;
        this.blockType = null;
        this.attributes = null;
        this.children = null;
        this.data = data == null ? new XBData() : data;
    }

    /**
     * Creates new instance of XBTDefaultBlock as a data block with given values
     * and no parent block.
     *
     * @param terminationMode termination mode
     * @param data block data
     */
    public XBTDefaultBlock(XBBlockTerminationMode terminationMode, XBBlockData data) {
        this(null, terminationMode, data);
    }

    /**
     * Creates new instance of XBTDefaultBlock as a node block with given
     * values.
     *
     * @param parent parent node
     * @param terminationMode termination mode
     * @param blockType block type
     * @param attributes attributes
     * @param children children blocks
     */
    public XBTDefaultBlock(XBTBlock parent, XBBlockTerminationMode terminationMode, XBBlockType blockType, XBAttribute[] attributes, XBTBlock[] children) {
        dataMode = XBBlockDataMode.NODE_BLOCK;
        this.parent = parent;
        this.terminationMode = terminationMode == null ? XBBlockTerminationMode.SIZE_SPECIFIED : terminationMode;
        this.blockType = blockType == null ? XBFixedBlockType.UNKNOWN_BLOCK_TYPE : blockType;
        this.attributes = attributes == null ? new XBAttribute[0] : attributes;
        this.children = children == null ? new XBTBlock[0] : children;
        data = null;
        if (children != null) {
            attachChildren(children);
        }
    }

    /**
     * Creates new instance of XBTDefaultBlock as a node block with given values
     * and no parent block.
     *
     * @param terminationMode termination mode
     * @param blockType block type
     * @param attributes attributes
     * @param children children blocks
     */
    public XBTDefaultBlock(XBBlockTerminationMode terminationMode, XBBlockType blockType, XBAttribute[] attributes, XBTBlock[] children) {
        this(null, terminationMode, blockType, attributes, children);
    }

    private void attachChildren(XBTBlock[] children) {
        for (XBTBlock child : children) {
            if (child instanceof XBTDefaultBlock) {
                ((XBTDefaultBlock) child).setParent(this);
            } else if (child instanceof XBEditableBlock) {
                ((XBTEditableBlock) child).setParent(this);
            }
        }
    }

    @Override
    public XBTBlock getParent() {
        return parent;
    }

    /**
     * Allows to set parent block, which is not cosidered as a part of the block
     * value and allows to move this block in tree.
     *
     * @param parent parent block
     */
    public void setParent(XBTBlock parent) {
        this.parent = parent;
    }

    @Override
    public XBBlockDataMode getDataMode() {
        return dataMode;
    }

    @Override
    public XBBlockTerminationMode getTerminationMode() {
        return terminationMode;
    }

    @Override
    public XBBlockType getBlockType() {
        return blockType;
    }

    @Override
    public XBAttribute[] getAttributes() {
        return attributes;
    }

    @Override
    public XBAttribute getAttributeAt(int attributeIndex) {
        return attributes[attributeIndex];
    }

    @Override
    public int getAttributesCount() {
        return attributes.length;
    }

    @Override
    public XBTBlock[] getChildren() {
        return children;
    }

    @Override
    public XBTBlock getChildAt(int childIndex) {
        return children[childIndex];
    }

    @Override
    public int getChildrenCount() {
        return children.length;
    }

    @Override
    public InputStream getData() {
        return data.getDataInputStream();
    }

    @Override
    public XBBlockData getBlockData() {
        return data;
    }

    /**
     * Gets block position in depth-first scan of the tree.
     *
     * Returns -1 for null block or if tree structure is corrupted.
     *
     * @param block target block
     * @return position index
     */
    public static int getBlockIndex(XBTBlock block) {
        if (block == null) {
            return -1;
        }

        if (block.getParent() != null) {
            int result = getBlockIndex(block.getParent()) + 1;
            int childIndex = 0;
            XBTBlock child;
            do {
                child = block.getParent().getChildAt(childIndex);
                if (block.equals(child)) {
                    return result + childIndex;
                }
                childIndex++;
            } while (child != null);

            return -1;
        } else {
            return 0;
        }
    }

    /**
     * Gets block position in parents direct child list.
     *
     * @param parent parent block
     * @param block target block
     * @return position index
     */
    public static int getChildIndexOf(XBTBlock parent, XBTBlock block) {
        if (block == null || parent == null) {
            return -1;
        }

        int childIndex = 0;
        XBTBlock child;
        do {
            child = block.getParent().getChildAt(childIndex);
            if (block.equals(child)) {
                return childIndex;
            }
            childIndex++;
        } while (child != null);

        return -1;
    }
}
