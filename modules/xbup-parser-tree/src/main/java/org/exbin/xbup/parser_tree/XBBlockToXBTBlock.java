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

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import javax.annotation.Nonnull;
import org.exbin.auxiliary.binary_data.BinaryData;
import org.exbin.xbup.core.block.XBBlock;
import org.exbin.xbup.core.block.XBBlockDataMode;
import org.exbin.xbup.core.block.XBBlockTerminationMode;
import org.exbin.xbup.core.block.XBBlockType;
import org.exbin.xbup.core.block.XBEditableBlock;
import org.exbin.xbup.core.block.XBFixedBlockType;
import org.exbin.xbup.core.block.XBTBlock;
import org.exbin.xbup.core.block.XBTEditableBlock;
import org.exbin.xbup.core.parser.token.XBAttribute;
import org.exbin.xbup.core.ubnumber.type.UBNat32;

/**
 * Conversion from level 0 block to level 1 block
 *
 * @author ExBin Project (https://exbin.org)
 */
public class XBBlockToXBTBlock implements XBTEditableBlock {

    private final XBBlock block;

    public XBBlockToXBTBlock(XBBlock block) {
        this.block = block;
    }

    public XBBlock getBlock() {
        return block;
    }

    @Nonnull
    @Override
    public Optional<XBTBlock> getParentBlock() {
        Optional<XBBlock> optParent = block.getParentBlock();
        if (!optParent.isPresent()) {
            return Optional.empty();
        }

        XBBlock parent = optParent.get();
        if (parent instanceof XBTBlockToXBBlock) {
            return Optional.of(((XBTBlockToXBBlock) parent).getBlock());
        }

        return Optional.of(new XBBlockToXBTBlock(parent));
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

    @Nonnull
    @Override
    public InputStream getData() {
        return block.getData();
    }

    @Nonnull
    @Override
    public BinaryData getBlockData() {
        return block.getBlockData();
    }

    @Override
    public void setParent(XBTBlock parent) {
        if (!(block instanceof XBEditableBlock)) {
            throw new IllegalStateException("Cannot set parent of read only block");
        }

        if (parent instanceof XBBlockToXBTBlock) {
            ((XBEditableBlock) block).setParent(((XBBlockToXBTBlock) parent).block);
        } else {
            ((XBEditableBlock) block).setParent(parent == null ? null : new XBTBlockToXBBlock(parent));
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

        if (count >= 2) {
            ((XBEditableBlock) block).setAttributesCount(count - 2);
        } else {
            ((XBEditableBlock) block).setAttributesCount(0);
            // if (block instanceof XBT)
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
    public void setData(InputStream data) throws IOException {
        if (!(block instanceof XBEditableBlock)) {
            throw new IllegalStateException("Cannot set data of read only block");
        }

        ((XBEditableBlock) block).setData(data);
    }

    @Override
    public void setData(BinaryData data) {
        if (!(block instanceof XBEditableBlock)) {
            throw new IllegalStateException("Cannot set data of read only block");
        }

        ((XBEditableBlock) block).setData(data);
    }

    @Override
    public void clear() {
        if (!(block instanceof XBEditableBlock)) {
            throw new IllegalStateException("Cannot clear data of read only block");
        }

        ((XBEditableBlock) block).clear();
    }

    @Override
    public XBTBlock createNewChild(int childIndex) {
        if (!(block instanceof XBEditableBlock)) {
            throw new IllegalStateException("Cannot set attribute of read only block");
        }

        XBBlock childBlock = ((XBEditableBlock) block).createNewChild(childIndex);
        return new XBBlockToXBTBlock(childBlock);
    }
}
