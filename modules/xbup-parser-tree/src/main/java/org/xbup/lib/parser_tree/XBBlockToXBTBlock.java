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
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * Conversion from level 0 block to level 1 block
 *
 * @version 0.1.25 2015/05/23
 * @author XBUP Project (http://xbup.org)
 */
public class XBBlockToXBTBlock implements XBTEditableBlock {

    private final XBBlock block;

    public XBBlockToXBTBlock(XBBlock block) {
        this.block = block;
    }

    public XBBlock getBlock() {
        return block;
    }

    @Override
    public XBTBlock getParent() {
        if (block.getParent() instanceof XBTBlockToXBBlock) {
            return ((XBTBlockToXBBlock) block.getParent()).getBlock();
        }

        return new XBBlockToXBTBlock(block.getParent());
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
    public XBBlockType getBlockType() {
        return new XBFixedBlockType(block.getAttributesCount() > 0 ? block.getAttributeAt(0).getNaturalLong() : 0, block.getAttributesCount() > 1 ? block.getAttributeAt(1).getNaturalLong() : 0);
    }

    @Override
    public void setBlockType(XBBlockType blockType) {
        if (!(block instanceof XBEditableBlock)) {
            throw new IllegalStateException("Cannot clear data of read only block");
        }

        if (blockType instanceof XBFixedBlockType) {
            if (block.getAttributesCount() < 2) {
                ((XBEditableBlock) block).setAttributesCount(2);
            }
            
            ((XBEditableBlock) block).setAttributeAt(((XBFixedBlockType) blockType).getGroupID(), 0);
            ((XBEditableBlock) block).setAttributeAt(((XBFixedBlockType) blockType).getBlockID(), 1);
        } else {
            throw new IllegalStateException("Setting non-fixed block type");
        }
    }

    @Override
    public XBAttribute[] getAttributes() {
        int attributesCount = block.getAttributesCount();
        if (attributesCount < 3) {
            return new XBAttribute[0];
        }
        XBAttribute[] result = new XBAttribute[attributesCount - 2];
        System.arraycopy(block.getAttributes(), 2, result, 0, attributesCount - 2);

        return result;
    }

    @Override
    public XBAttribute getAttributeAt(int attributeIndex) {
        return block.getAttributeAt(attributeIndex + 2);
    }

    @Override
    public int getAttributesCount() {
        int attributesCount = block.getAttributesCount();
        return attributesCount < 2 ? 0 : attributesCount - 2;
    }

    @Override
    public XBTBlock[] getChildren() {
        XBTBlock[] result = new XBTBlock[getChildrenCount()];
        int i = 0;
        for (XBBlock child : block.getChildren()) {
            result[i++] = new XBBlockToXBTBlock(child);
        }
        return result;
    }

    @Override
    public XBTBlock getChildAt(int childIndex) {
        return new XBBlockToXBTBlock(block.getChildAt(childIndex));
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
    public void setParent(XBTBlock parent) {
        if (!(block instanceof XBEditableBlock)) {
            throw new IllegalStateException("Cannot set parent of read only block");
        }

        if (parent instanceof XBBlockToXBTBlock) {
            ((XBEditableBlock) block).setParent(((XBBlockToXBTBlock) parent).block);
        } else {
            ((XBEditableBlock) block).setParent(new XBTBlockToXBBlock(parent));
        }
    }

    @Override
    public void setTerminationMode(XBBlockTerminationMode terminationMode) {
        if (!(block instanceof XBEditableBlock)) {
            throw new IllegalStateException("Cannot set termination mode of read only block");
        }

        ((XBEditableBlock) block).setTerminationMode(terminationMode);
    }

    @Override
    public void setDataMode(XBBlockDataMode dataMode) {
        if (!(block instanceof XBEditableBlock)) {
            throw new IllegalStateException("Cannot set data mode of read only block");
        }

        ((XBEditableBlock) block).setDataMode(dataMode);
    }

    @Override
    public void setAttributes(XBAttribute[] attributes) {
        if (!(block instanceof XBEditableBlock)) {
            throw new IllegalStateException("Cannot set attribute of read only block");
        }

        XBAttribute[] result = new XBAttribute[attributes.length + 2];
        result[0] = new UBNat32(block.getAttributesCount() > 0 ? block.getAttributeAt(0).getNaturalLong() : 0);
        result[1] = new UBNat32(block.getAttributesCount() > 1 ? block.getAttributeAt(1).getNaturalLong() : 0);
        System.arraycopy(block.getAttributes(), 0, result, 2, attributes.length - 2);
        ((XBTEditableBlock) block).setAttributes(result);
    }

    @Override
    public void setAttributeAt(XBAttribute attribute, int attributeIndex) {
        if (!(block instanceof XBEditableBlock)) {
            throw new IllegalStateException("Cannot set attribute of read only block");
        }

        ((XBEditableBlock) block).setAttributeAt(attribute, attributeIndex + 2);
    }

    @Override
    public void setAttributesCount(int count) {
        if (!(block instanceof XBEditableBlock)) {
            throw new IllegalStateException("Cannot set attribute count of read only block");
        }

        if (count > 1) {
            ((XBEditableBlock) block).setAttributesCount(count - 2);
        } else {
            ((XBEditableBlock) block).setAttributesCount(0);
            // Ignore block type
        }
    }

    @Override
    public void removeAttribute(int attributeIndex) {
        if (!(block instanceof XBEditableBlock)) {
            throw new IllegalStateException("Cannot remove attribute count of read only block");
        }

        if (attributeIndex > 2) {
            ((XBEditableBlock) block).removeAttribute(attributeIndex - 2);
        } else {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    @Override
    public void setChildren(XBTBlock[] blocks) {
        if (!(block instanceof XBEditableBlock)) {
            throw new IllegalStateException("Cannot set child of read only block");
        }

        XBBlock[] convertedBlocks = new XBBlock[blocks.length];
        for (int i = 0; i < blocks.length; i++) {
            XBTBlock child = blocks[i];
            if (child instanceof XBTBlockToXBBlock) {

            } else {
                convertedBlocks[i] = new XBTBlockToXBBlock(child);
            }
        }

        ((XBEditableBlock) block).setChildren(convertedBlocks);
    }

    @Override
    public void setChildAt(XBTBlock child, int childIndex) {
        if (!(child instanceof XBEditableBlock)) {
            throw new IllegalStateException("Cannot set attribute of read only block");
        }

        if (child instanceof XBTBlockToXBBlock) {

        } else {
            ((XBEditableBlock) block).setChildAt(new XBTBlockToXBBlock(child), childIndex);
        }
    }

    @Override
    public void setChildrenCount(int count) {
        if (!(block instanceof XBEditableBlock)) {
            throw new IllegalStateException("Cannot set attribute of read only block");
        }

        ((XBEditableBlock) block).setChildrenCount(count);
    }

    @Override
    public void removeChild(int childIndex) {
        if (!(block instanceof XBEditableBlock)) {
            throw new IllegalStateException("Cannot remove child of read only block");
        }

        ((XBEditableBlock) block).removeChild(childIndex);
    }

    @Override
    public void setData(InputStream data) {
        if (!(block instanceof XBEditableBlock)) {
            throw new IllegalStateException("Cannot set data of read only block");
        }

        ((XBEditableBlock) block).setData(data);
    }

    @Override
    public void setData(XBBlockData data) {
        if (!(block instanceof XBEditableBlock)) {
            throw new IllegalStateException("Cannot set data of read only block");
        }

        ((XBEditableBlock) block).setData(data);
    }

    @Override
    public void clearData() {
        if (!(block instanceof XBEditableBlock)) {
            throw new IllegalStateException("Cannot clear data of read only block");
        }

        ((XBEditableBlock) block).clearData();
    }

    @Override
    public void clear() {
        if (!(block instanceof XBEditableBlock)) {
            throw new IllegalStateException("Cannot clear data of read only block");
        }

        ((XBEditableBlock) block).clear();
    }

    @Override
    public int getBlockIndex() {
        return block.getBlockIndex();
    }
}
