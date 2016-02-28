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
 * Basic plain implementation of XBBlock interface.
 *
 * @version 0.2.0 2015/09/19
 * @author ExBin Project (http://exbin.org)
 */
public class XBDefaultBlock implements XBBlock {

    private XBBlock parent;
    private final XBBlockDataMode dataMode;
    private final XBBlockTerminationMode terminationMode;
    private final XBAttribute[] attributes;
    private final XBBlock[] children;
    private final XBBlockData data;

    /**
     * Creates new instance of XBDefaultBlock as an empty data block.
     */
    public XBDefaultBlock() {
        this(null, XBBlockTerminationMode.SIZE_SPECIFIED, new XBData());
    }

    /**
     * Creates new instance of XBDefaultBlock as a data block with given values.
     *
     * @param parent parent node
     * @param terminationMode termination mode
     * @param data block data
     */
    public XBDefaultBlock(XBBlock parent, XBBlockTerminationMode terminationMode, XBBlockData data) {
        dataMode = XBBlockDataMode.DATA_BLOCK;
        this.parent = parent;
        this.terminationMode = terminationMode;
        this.attributes = null;
        this.children = null;
        this.data = data == null ? new XBData() : data;
    }

    /**
     * Creates new instance of XBDefaultBlock as a data block with given values
     * and no parent block.
     *
     * @param terminationMode termination mode
     * @param data block data
     */
    public XBDefaultBlock(XBBlockTerminationMode terminationMode, XBBlockData data) {
        this(null, terminationMode, data);
    }

    /**
     * Creates new instance of XBDefaultBlock as a node block with given values.
     *
     * @param parent parent node
     * @param terminationMode termination mode
     * @param attributes attributes
     * @param children children blocks
     */
    public XBDefaultBlock(XBBlock parent, XBBlockTerminationMode terminationMode, XBAttribute[] attributes, XBBlock[] children) {
        dataMode = XBBlockDataMode.NODE_BLOCK;
        this.parent = parent;
        this.terminationMode = terminationMode == null ? XBBlockTerminationMode.SIZE_SPECIFIED : terminationMode;
        this.attributes = attributes == null ? new XBAttribute[0] : attributes;
        this.children = children == null ? new XBBlock[0] : children;
        data = null;
        if (children != null) {
            attachChildren(children);
        }
    }

    /**
     * Creates new instance of XBDefaultBlock as a node block with given values
     * and no parent block.
     *
     * @param terminationMode termination mode
     * @param attributes attributes
     * @param children children blocks
     */
    public XBDefaultBlock(XBBlockTerminationMode terminationMode, XBAttribute[] attributes, XBBlock[] children) {
        this(null, terminationMode, attributes, children);
    }

    private void attachChildren(XBBlock[] children) {
        for (XBBlock child : children) {
            if (child instanceof XBDefaultBlock) {
                ((XBDefaultBlock) child).setParent(this);
            } else if (child instanceof XBEditableBlock) {
                ((XBEditableBlock) child).setParent(this);
            }
        }
    }

    @Override
    public XBBlock getParent() {
        return parent;
    }

    /**
     * Allows to set parent block, which is not cosidered as a part of the block
     * value and allows to move this block in tree.
     *
     * @param parent parent block
     */
    public void setParent(XBBlock parent) {
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
    public XBBlock[] getChildren() {
        return children;
    }

    @Override
    public XBBlock getChildAt(int childIndex) {
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

    public long getDataSize() {
        return data == null ? 0 : data.getDataSize();
    }

    /**
     * Gets block position in depth-first scan of the tree.
     *
     * Returns -1 for null block or if tree structure is corrupted.
     *
     * @param block target block
     * @return position index
     */
    public static int getBlockIndex(XBBlock block) {
        if (block == null) {
            return -1;
        }

        if (block.getParent() != null) {
            int result = getBlockIndex(block.getParent()) + 1;
            int childIndex = 0;
            XBBlock child;
            do {
                child = block.getParent().getChildAt(childIndex);
                if (block.equals(child)) {
                    return result + childIndex;
                }
                childIndex++;
            } while (child != null);

            return result;
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
    public static int getChildIndexOf(XBBlock parent, XBBlock block) {
        if (block == null || parent == null) {
            return -1;
        }

        int childIndex = 0;
        XBBlock child;
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
