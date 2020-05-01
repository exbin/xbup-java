/*
 * Copyright (C) ExBin Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.exbin.xbup.core.block;

import java.io.InputStream;
import java.util.Objects;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.exbin.auxiliary.paged_data.BinaryData;
import org.exbin.xbup.core.parser.token.XBAttribute;
import org.exbin.xbup.core.type.XBData;

/**
 * Basic plain implementation of XBTBlock interface.
 *
 * @version 0.2.1 2017/05/09
 * @author ExBin Project (http://exbin.org)
 */
public class XBTDefaultBlock implements XBTBlock {

    @Nullable
    private XBTBlock parent;
    @Nonnull
    private final XBBlockDataMode dataMode;
    @Nonnull
    private final XBBlockTerminationMode terminationMode;
    @Nullable
    private final XBBlockType blockType;
    @Nullable
    private final XBAttribute[] attributes;
    @Nullable
    private final XBTBlock[] children;
    @Nullable
    private final BinaryData data;

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
    public XBTDefaultBlock(@Nullable XBTBlock parent, @Nullable XBBlockTerminationMode terminationMode, @Nullable BinaryData data) {
        dataMode = XBBlockDataMode.DATA_BLOCK;
        this.parent = parent;
        this.terminationMode = terminationMode == null ? XBBlockTerminationMode.SIZE_SPECIFIED : terminationMode;
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
    public XBTDefaultBlock(@Nullable XBBlockTerminationMode terminationMode, @Nullable BinaryData data) {
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
    public XBTDefaultBlock(@Nullable XBTBlock parent, @Nullable XBBlockTerminationMode terminationMode, @Nullable XBBlockType blockType, @Nullable XBAttribute[] attributes, @Nullable XBTBlock[] children) {
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
    public XBTDefaultBlock(@Nullable XBBlockTerminationMode terminationMode, @Nullable XBBlockType blockType, @Nullable XBAttribute[] attributes, @Nullable XBTBlock[] children) {
        this(null, terminationMode, blockType, attributes, children);
    }

    private void attachChildren(@Nonnull XBTBlock[] children) {
        for (XBTBlock child : children) {
            if (child instanceof XBTDefaultBlock) {
                ((XBTDefaultBlock) child).setParent(this);
            } else if (child instanceof XBEditableBlock) {
                ((XBTEditableBlock) child).setParent(this);
            }
        }
    }

    @Nonnull
    @Override
    public Optional<XBTBlock> getParentBlock() {
        return Optional.ofNullable(parent);
    }

    /**
     * Allows to set parent block, which is not cosidered as a part of the block
     * value and allows to move this block in tree.
     *
     * @param parent parent block
     */
    public void setParent(@Nullable XBTBlock parent) {
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

    @Nonnull
    @Override
    public XBBlockType getBlockType() {
        return blockType;
    }

    @Nullable
    @Override
    public XBAttribute[] getAttributes() {
        return attributes;
    }

    @Nullable
    @Override
    public XBAttribute getAttributeAt(int attributeIndex) {
        return attributes[attributeIndex];
    }

    @Override
    public int getAttributesCount() {
        return attributes.length;
    }

    @Nullable
    @Override
    public XBTBlock[] getChildren() {
        return children;
    }

    @Nullable
    @Override
    public XBTBlock getChildAt(int childIndex) {
        return children[childIndex];
    }

    @Override
    public int getChildrenCount() {
        return children.length;
    }

    @Nonnull
    @Override
    public InputStream getData() {
        if (dataMode != XBBlockDataMode.DATA_BLOCK) {
            throw new IllegalStateException("Cannot read data of node block");
        }
        return data.getDataInputStream();
    }

    @Nonnull
    @Override
    public BinaryData getBlockData() {
        if (dataMode != XBBlockDataMode.DATA_BLOCK) {
            throw new IllegalStateException("Cannot read data of node block");
        }
        return Objects.requireNonNull(data);
    }

    /**
     * Gets block position in depth-first scan of the tree.
     *
     * Returns -1 for null block or if tree structure is corrupted.
     *
     * @param block target block
     * @return position index
     */
    public static int getBlockIndex(@Nullable XBTBlock block) {
        if (block == null) {
            return -1;
        }

        Optional<XBTBlock> blockParent = block.getParentBlock();
        if (blockParent.isPresent()) {
            int result = getBlockIndex(blockParent.get()) + 1;
            int childIndex = 0;
            XBTBlock child;
            do {
                child = blockParent.get().getChildAt(childIndex);
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
    public static int getChildIndexOf(@Nullable XBTBlock parent, @Nullable XBTBlock block) {
        if (block == null || parent == null) {
            return -1;
        }

        int childIndex = 0;
        XBTBlock blockParent = block.getParentBlock().get();
        XBTBlock child;
        do {
            child = blockParent.getChildAt(childIndex);
            if (block.equals(child)) {
                return childIndex;
            }
            childIndex++;
        } while (child != null);

        return -1;
    }
}
