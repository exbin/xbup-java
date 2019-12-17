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
package org.exbin.xbup.core.block;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.exbin.auxiliary.paged_data.BinaryData;
import org.exbin.xbup.core.parser.token.XBAttribute;
import org.exbin.xbup.core.type.XBData;

/**
 * Basic plain implementation of XBEditableBlock interface.
 *
 * @version 0.2.1 2017/05/13
 * @author ExBin Project (http://exbin.org)
 */
public class XBDefaultEditableBlock implements XBEditableBlock {

    @Nullable
    private XBBlock parent;
    @Nonnull
    private XBBlockDataMode dataMode;
    @Nonnull
    private XBBlockTerminationMode terminationMode;
    @Nullable
    private List<XBAttribute> attributes;
    @Nullable
    private List<XBBlock> children;
    @Nullable
    private BinaryData data;

    /**
     * Creates new instance of XBDefaultBlock as an empty data block.
     */
    public XBDefaultEditableBlock() {
        this(null, XBBlockTerminationMode.SIZE_SPECIFIED, null);
    }

    /**
     * Creates new instance of XBDefaultBlock as a data block with given values.
     *
     * @param parent parent node
     * @param terminationMode termination mode
     * @param data block data
     */
    public XBDefaultEditableBlock(@Nullable XBBlock parent, @Nonnull XBBlockTerminationMode terminationMode, @Nullable BinaryData data) {
        dataMode = XBBlockDataMode.DATA_BLOCK;
        this.parent = parent;
        this.terminationMode = terminationMode;
        this.data = data;
    }

    /**
     * Creates new instance of XBDefaultBlock as a data block with given values
     * and no parent block.
     *
     * @param terminationMode termination mode
     * @param data block data
     */
    public XBDefaultEditableBlock(@Nullable XBBlockTerminationMode terminationMode, @Nullable BinaryData data) {
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
    public XBDefaultEditableBlock(@Nullable XBBlock parent, @Nullable XBBlockTerminationMode terminationMode, @Nullable XBAttribute[] attributes, @Nullable XBBlock[] children) {
        dataMode = XBBlockDataMode.NODE_BLOCK;
        this.parent = parent;
        this.terminationMode = terminationMode == null ? XBBlockTerminationMode.SIZE_SPECIFIED : terminationMode;

        if (attributes != null) {
            List<XBAttribute> blockAttributes = new ArrayList<>();
            blockAttributes.addAll(Arrays.asList(attributes));
            this.attributes = blockAttributes;
        }

        if (children != null) {
            List<XBBlock> blockChildren = new ArrayList<>();
            blockChildren.addAll(Arrays.asList(children));
            this.children = blockChildren;
        }

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
    public XBDefaultEditableBlock(@Nonnull XBBlockTerminationMode terminationMode, @Nullable XBAttribute[] attributes, @Nullable XBBlock[] children) {
        this(null, terminationMode, attributes, children);
    }

    private void attachChildren(@Nonnull XBBlock[] children) {
        for (XBBlock child : children) {
            if (child instanceof XBDefaultEditableBlock) {
                ((XBDefaultEditableBlock) child).setParent(this);
            } else if (child instanceof XBEditableBlock) {
                ((XBEditableBlock) child).setParent(this);
            }
        }
    }

    @Nullable
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
    @Override
    public void setParent(@Nullable XBBlock parent) {
        this.parent = parent;
    }

    @Nonnull
    @Override
    public XBBlockDataMode getDataMode() {
        return dataMode;
    }

    @Nonnull
    @Override
    public XBBlockTerminationMode getTerminationMode() {
        return terminationMode;
    }

    @Override
    @Nullable
    public XBAttribute[] getAttributes() {
        return attributes.toArray(new XBAttribute[0]);
    }

    @Override
    @Nullable
    public XBAttribute getAttributeAt(int attributeIndex) {
        return attributeIndex < attributes.size() ? attributes.get(attributeIndex) : null;
    }

    @Override
    public int getAttributesCount() {
        return attributes.size();
    }

    @Override
    @Nullable
    public XBBlock[] getChildren() {
        return children == null ? null : children.toArray(new XBBlock[0]);
    }

    @Override
    @Nullable
    public XBBlock getChildAt(int childIndex) {
        return childIndex < children.size() ? children.get(childIndex) : null;
    }

    @Override
    public int getChildrenCount() {
        return children != null ? children.size() : 0;
    }

    @Override
    @Nullable
    public InputStream getData() {
        return data == null ? null : data.getDataInputStream();
    }

    @Override
    public BinaryData getBlockData() {
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
    public static int getBlockIndex(@Nullable XBBlock block) {
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
    public static int getChildIndexOf(@Nullable XBBlock parent, @Nullable XBBlock block) {
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

    @Override
    public void setTerminationMode(@Nonnull XBBlockTerminationMode terminationMode) {
        this.terminationMode = terminationMode;
    }

    @Override
    public void setDataMode(@Nullable XBBlockDataMode dataMode) {
        this.dataMode = dataMode;
    }

    @Override
    public void setAttributes(@Nullable XBAttribute[] attributes) {
        if (this.attributes == null) {
            this.attributes = new ArrayList<>();
        } else {
            this.attributes.clear();
        }

        if (attributes != null) {
            this.attributes.addAll(Arrays.asList(attributes));
        }
    }

    @Override
    public void setAttributeAt(XBAttribute attribute, int attributeIndex) {
        if (attributeIndex >= attributes.size()) {
            setAttributesCount(attributeIndex + 1);
        }
        attributes.set(attributeIndex, attribute);
    }

    @Override
    public void setAttributesCount(int count) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void removeAttribute(int attributeIndex) {
        if (attributeIndex < attributes.size()) {
            attributes.remove(attributeIndex);
        }
    }

    @Override
    public void setChildren(XBBlock[] blocks) {
        List<XBBlock> resultChildren = this.children;
        if (resultChildren == null) {
            resultChildren = new ArrayList<>();
        } else {
            resultChildren.clear();
        }
        resultChildren.addAll(Arrays.asList(blocks));
        this.children = resultChildren;
    }

    @Override
    public void setChildAt(XBBlock block, int childIndex) {
        if (childIndex >= children.size()) {
            setChildrenCount(childIndex + 1);
        }
        children.set(childIndex, block);
    }

    @Override
    public void setChildrenCount(int count) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public XBBlock createNewChild(int childIndex) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void removeChild(int childIndex) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setData(InputStream data) throws IOException {
        this.data = new XBData();
        ((XBData) this.data).loadFromStream(data);
    }

    @Override
    public void setData(BinaryData data) {
        this.data = data;
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
