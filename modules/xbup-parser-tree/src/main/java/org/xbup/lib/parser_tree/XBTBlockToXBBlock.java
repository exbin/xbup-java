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

import java.io.InputStream;
import org.xbup.lib.core.block.XBBlock;
import org.xbup.lib.core.block.XBBlockData;
import org.xbup.lib.core.block.XBBlockDataMode;
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.block.XBBlockType;
import org.xbup.lib.core.block.XBEditableBlock;
import org.xbup.lib.core.block.XBFixedBlockType;
import org.xbup.lib.core.block.XBTBlock;
import org.xbup.lib.core.block.XBTEditableBlock;
import org.xbup.lib.core.parser.token.XBAttribute;

/**
 * Conversion from level 1 block to level 0 block.
 *
 * @version 0.1.25 2015/05/23
 * @author XBUP Project (http://xbup.org)
 */
public class XBTBlockToXBBlock implements XBEditableBlock {

    private final XBTBlock block;

    public XBTBlockToXBBlock(XBTBlock block) {
        this.block = block;
    }

    public XBTBlock getBlock() {
        return block;
    }

    @Override
    public XBBlock getParent() {
        if (block.getParent() instanceof XBBlockToXBTBlock) {
            return ((XBBlockToXBTBlock) block.getParent()).getBlock();
        }

        return new XBTBlockToXBBlock(block.getParent());
    }

    @Override
    public XBBlockDataMode getDataMode() {
        return block.getDataMode();
    }

    @Override
    public XBBlockTerminationMode getTerminationMode() {
        return block.getTerminationMode();
    }

    @Override
    public XBAttribute[] getAttributes() {
        int attributesCount = getAttributesCount();
        XBAttribute[] result = new XBAttribute[attributesCount];
        if (attributesCount > 0) {
            result[0] = getAttributeAt(0);
            if (attributesCount > 1) {
                result[1] = getAttributeAt(1);
                if (attributesCount > 2) {
                    System.arraycopy(block.getAttributes(), 0, result, 2, attributesCount - 2);
                }
            }
        }

        return result;
    }

    @Override
    public XBAttribute getAttributeAt(int attributeIndex) {
        if (attributeIndex < 2) {
            if (block instanceof XBTTreeNode) {
                XBFixedBlockType blockType = ((XBTTreeNode) block).getFixedBlockType();
                return attributeIndex == 0 ? blockType.getGroupID() : blockType.getBlockID();
            } else {
                XBBlockType blockType = block.getBlockType();
                if (blockType instanceof XBFixedBlockType) {
                    return attributeIndex == 0 ? ((XBFixedBlockType) blockType).getGroupID() : ((XBFixedBlockType) blockType).getBlockID();
                }
            }

            throw new IllegalStateException("Cannot convert block with no fixed block type");
        }

        return block.getAttributeAt(attributeIndex - 2);
    }

    @Override
    public int getAttributesCount() {
        int attributesCount = block.getAttributesCount();
        if (attributesCount > 0) {
            return attributesCount + 2;
        }

        if (block instanceof XBTTreeNode) {
            XBFixedBlockType blockType = ((XBTTreeNode) block).getFixedBlockType();
            return blockType.getBlockID().isZero() ? 1 : 2;
        } else {
            XBBlockType blockType = block.getBlockType();
            if (blockType instanceof XBFixedBlockType) {
                return ((XBFixedBlockType) blockType).getBlockID().isZero() ? 1 : 2;
            }
        }

        throw new IllegalStateException("Cannot convert block with no fixed block type");
    }

    @Override
    public XBBlock[] getChildren() {
        XBBlock[] result = new XBBlock[getChildrenCount()];
        int i = 0;
        for (XBTBlock child : block.getChildren()) {
            result[i++] = new XBTBlockToXBBlock(child);
        }
        return result;
    }

    @Override
    public XBBlock getChildAt(int childIndex) {
        return new XBTBlockToXBBlock(block.getChildAt(childIndex));
    }

    @Override
    public int getChildrenCount() {
        return block.getChildrenCount();
    }

    @Override
    public InputStream getData() {
        return block.getData();
    }

    @Override
    public XBBlockData getBlockData() {
        return block.getBlockData();
    }

    @Override
    public long getDataSize() {
        return block.getDataSize();
    }

    @Override
    public long getBlockSize() {
        return block.getBlockSize();
    }

    @Override
    public void setParent(XBBlock parent) {
        if (!(block instanceof XBTEditableBlock)) {
            throw new IllegalStateException("Cannot set parent of read only block");
        }

        if (parent instanceof XBTBlockToXBBlock) {
            ((XBTEditableBlock) block).setParent(((XBTBlockToXBBlock) parent).block);
        } else {
            ((XBTEditableBlock) block).setParent(new XBBlockToXBTBlock(parent));
        }
    }

    @Override
    public void setTerminationMode(XBBlockTerminationMode terminationMode) {
        if (!(block instanceof XBTEditableBlock)) {
            throw new IllegalStateException("Cannot set termination mode of read only block");
        }

        ((XBTEditableBlock) block).setTerminationMode(terminationMode);
    }

    @Override
    public void setDataMode(XBBlockDataMode dataMode) {
        if (!(block instanceof XBTEditableBlock)) {
            throw new IllegalStateException("Cannot set data mode of read only block");
        }

        ((XBTEditableBlock) block).setDataMode(dataMode);
    }

    @Override
    public void setAttributes(XBAttribute[] attributes) {
        if (!(block instanceof XBTEditableBlock)) {
            throw new IllegalStateException("Cannot set attribute of read only block");
        }

        if (attributes.length > 2) {
            XBAttribute[] result = new XBAttribute[attributes.length - 2];
            System.arraycopy(block.getAttributes(), 0, result, 0, attributes.length - 2);
            ((XBTEditableBlock) block).setAttributes(result);
        } else {
            setAttributesCount(attributes.length);
        }

        if (attributes.length > 0) {
            setAttributeAt(attributes[0], 0);
        }
        if (attributes.length > 1) {
            setAttributeAt(attributes[1], 1);
        }
    }

    @Override
    public void setAttributeAt(XBAttribute attribute, int attributeIndex) {
        if (!(block instanceof XBTEditableBlock)) {
            throw new IllegalStateException("Cannot set attribute of read only block");
        }

        if (attributeIndex < 2) {
            XBBlockType blockType = block.getBlockType();
            if (blockType instanceof XBFixedBlockType) {
                blockType = attributeIndex == 0 ?
                        new XBFixedBlockType(attribute.getNaturalLong(), ((XBFixedBlockType) blockType).getBlockID().getLong()) :
                        new XBFixedBlockType(((XBFixedBlockType) blockType).getGroupID().getLong(), attribute.getNaturalLong());
                ((XBTEditableBlock) block).setBlockType(blockType);
            } else {
                throw new IllegalStateException("Cannot set block type when it's not fixed");
            }
        }

        ((XBTEditableBlock) block).setAttributeAt(attribute, attributeIndex - 2);
    }

    @Override
    public void setAttributesCount(int count) {
        if (!(block instanceof XBTEditableBlock)) {
            throw new IllegalStateException("Cannot set attribute count of read only block");
        }

        if (count > 1) {
            ((XBTEditableBlock) block).setAttributesCount(count - 2);
        } else {
            ((XBTEditableBlock) block).setAttributesCount(0);
            // Ignore block type
        }
    }

    @Override
    public void removeAttribute(int attributeIndex) {
        if (!(block instanceof XBEditableBlock)) {
            throw new IllegalStateException("Cannot remove attribute count of read only block");
        }

        ((XBEditableBlock) block).removeAttribute(attributeIndex + 2);
    }

    @Override
    public void setChildren(XBBlock[] blocks) {
        if (!(block instanceof XBTEditableBlock)) {
            throw new IllegalStateException("Cannot set child of read only block");
        }

        XBTBlock[] convertedBlocks = new XBTBlock[blocks.length];
        for (int i = 0; i < blocks.length; i++) {
            XBBlock child = blocks[i];
            if (child instanceof XBBlockToXBTBlock) {
                
            } else {
                convertedBlocks[i] = new XBBlockToXBTBlock(child);
            }
        }
        
        ((XBTEditableBlock) block).setChildren(convertedBlocks);
    }

    @Override
    public void setChildAt(XBBlock child, int childIndex) {
        if (!(child instanceof XBTEditableBlock)) {
            throw new IllegalStateException("Cannot set attribute of read only block");
        }

        if (child instanceof XBBlockToXBTBlock) {

        } else {
            ((XBTEditableBlock) block).setChildAt(new XBBlockToXBTBlock(child), childIndex);
        }
    }

    @Override
    public void setChildrenCount(int count) {
        if (!(block instanceof XBTEditableBlock)) {
            throw new IllegalStateException("Cannot set attribute of read only block");
        }

        ((XBTEditableBlock) block).setChildrenCount(count);
    }

    @Override
    public void removeChild(int childIndex) {
        if (!(block instanceof XBTEditableBlock)) {
            throw new IllegalStateException("Cannot remove child of read only block");
        }

        ((XBTEditableBlock) block).removeChild(childIndex);
    }

    @Override
    public void setData(InputStream data) {
        if (!(block instanceof XBTEditableBlock)) {
            throw new IllegalStateException("Cannot set data of read only block");
        }

        ((XBTEditableBlock) block).setData(data);
    }

    @Override
    public void setData(XBBlockData data) {
        if (!(block instanceof XBTEditableBlock)) {
            throw new IllegalStateException("Cannot set data of read only block");
        }

        ((XBTEditableBlock) block).setData(data);
    }

    @Override
    public void clearData() {
        if (!(block instanceof XBTEditableBlock)) {
            throw new IllegalStateException("Cannot clear data of read only block");
        }

        ((XBTEditableBlock) block).clearData();
    }

    @Override
    public void clear() {
        if (!(block instanceof XBTEditableBlock)) {
            throw new IllegalStateException("Cannot clear data of read only block");
        }

        ((XBTEditableBlock) block).clear();
    }

    @Override
    public int getBlockIndex() {
        return block.getBlockIndex();
    }
}
