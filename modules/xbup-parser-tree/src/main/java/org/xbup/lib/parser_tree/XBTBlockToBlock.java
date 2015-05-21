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
 * Basic object model parser XBUP level 1 document block / tree node.
 *
 * @version 0.1.25 2015/05/25
 * @author XBUP Project (http://xbup.org)
 */
public class XBTBlockToBlock implements XBEditableBlock {

    private final XBTBlock block;

    public XBTBlockToBlock(XBTBlock block) {
        this.block = block;
    }

    @Override
    public XBBlock getParent() {
        return new XBTBlockToBlock(block.getParent());
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
        int attributesCount = block.getAttributesCount();
        XBAttribute[] result = new XBAttribute[attributesCount + 2];
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
            }

            throw new UnsupportedOperationException("Not supported yet.");
        }

        return block.getAttributeAt(attributeIndex - 2);
    }

    @Override
    public int getAttributesCount() {
        int attributesCount = block.getAttributesCount();
        if (attributesCount > 0) {
            return attributesCount + 2;
        }

        XBBlockType blockType = block.getBlockType();
        if (blockType != null) {
            // TODO
            return 2;
        }

        return 0;
    }

    @Override
    public XBBlock[] getChildren() {
        XBBlock[] result = new XBBlock[getChildrenCount()];
        int i = 0;
        for (XBTBlock child : block.getChildren()) {
            result[i++] = new XBTBlockToBlock(child);
        }
        return result;
    }

    @Override
    public XBBlock getChildAt(int childIndex) {
        return new XBTBlockToBlock(block.getChildAt(childIndex));
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
        if (parent instanceof XBTBlockToBlock) {

        }

        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setTerminationMode(XBBlockTerminationMode terminationMode) {
        if (block instanceof XBTEditableBlock) {
            ((XBTEditableBlock) block).setTerminationMode(terminationMode);
        }

        throw new IllegalStateException();
    }

    @Override
    public void setDataMode(XBBlockDataMode dataMode) {
        if (block instanceof XBTEditableBlock) {
            ((XBTEditableBlock) block).setDataMode(dataMode);
        }

        throw new IllegalStateException();
    }

    @Override
    public void setAttributes(XBAttribute[] attributes) {
        if (!(block instanceof XBTEditableBlock)) {
            throw new IllegalStateException("Cannot set attribute of read only block");
        }

        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setAttributeAt(XBAttribute attribute, int attributeIndex) {
        if (!(block instanceof XBTEditableBlock)) {
            throw new IllegalStateException("Cannot set attribute of read only block");
        }

        if (attributeIndex < 2) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        ((XBTEditableBlock) block).setAttributeAt(attribute, attributeIndex - 2);
    }

    @Override
    public void setAttributesCount(int count) {
        if (!(block instanceof XBTEditableBlock)) {
            throw new IllegalStateException("Cannot set attribute of read only block");
        }

        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setChildren(XBBlock[] blocks) {
        if (!(block instanceof XBTEditableBlock)) {
            throw new IllegalStateException("Cannot set attribute of read only block");
        }

        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setChildAt(XBBlock block, int childIndex) {
        if (!(block instanceof XBTEditableBlock)) {
            throw new IllegalStateException("Cannot set attribute of read only block");
        }

        // childrenConvertor.set(childIndex, block);
    }

    @Override
    public void setChildrenCount(int count) {
        if (!(block instanceof XBTEditableBlock)) {
            throw new IllegalStateException("Cannot set attribute of read only block");
        }

        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setData(InputStream data) {
        if (!(block instanceof XBTEditableBlock)) {
            throw new IllegalStateException("Cannot set attribute of read only block");
        }

        ((XBTEditableBlock) block).setData(data);
    }

    @Override
    public void setData(XBBlockData data) {
        if (!(block instanceof XBTEditableBlock)) {
            throw new IllegalStateException("Cannot set attribute of read only block");
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
}
