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
package org.xbup.lib.parser_command;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xbup.lib.core.block.XBBlock;
import org.xbup.lib.core.block.XBBlockData;
import org.xbup.lib.core.block.XBBlockDataMode;
import org.xbup.lib.core.block.XBEditableBlock;
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.block.XBDefaultEditableBlock;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.token.XBAttribute;

/**
 * XBUP level 0 command writer block.
 *
 * @version 0.2.0 2015/10/19
 * @author XBUP Project (http://xbup.org)
 */
public class XBWriterBlock implements XBCommandBlock, XBEditableBlock, Closeable {

    private long blockId = 0;
    private final long[] blockPath;
    private final XBWriter writer;
    private XBDefaultEditableBlock fixedBlock = null;

    /**
     * Creates new instance of the class.
     *
     * @param writer command writer
     * @param blockPath block path
     * @param blockId block identification number
     */
    public XBWriterBlock(XBWriter writer, long[] blockPath, long blockId) {
        this.blockId = blockId;
        this.blockPath = blockPath;
        this.writer = writer;
    }

    public long getBlockId() {
        return blockId;
    }

    public boolean isFixedBlock() {
        return fixedBlock != null;
    }

    @Override
    public XBBlock getParent() {
        if (blockPath.length == 0) {
            return null;
        } else {
            return writer.getBlock(Arrays.copyOf(blockPath, blockPath.length - 1));
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof XBReaderBlock) {
            Arrays.equals(((XBReaderBlock) obj).getBlockPath(), blockPath);
        }

        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Arrays.hashCode(this.blockPath);
        return hash;
    }

    @Override
    public long[] getBlockPath() {
        return blockPath;
    }

    @Override
    public XBBlockDataMode getDataMode() {
        if (fixedBlock != null) {
            return fixedBlock.getDataMode();
        }

        try {
            writer.seekBlock(this);
            return writer.getBlockDataMode();
        } catch (XBProcessingException | IOException ex) {
            return null;
        }
    }

    @Override
    public XBBlockTerminationMode getTerminationMode() {
        if (fixedBlock != null) {
            return fixedBlock.getTerminationMode();
        }

        try {
            writer.seekBlock(this);
            return writer.getBlockTerminationMode();
        } catch (XBProcessingException | IOException ex) {
            return null;
        }
    }

    @Override
    public XBAttribute[] getAttributes() {
        if (fixedBlock != null) {
            return fixedBlock.getAttributes();
        }

        try {
            writer.seekBlock(this);
        } catch (XBProcessingException | IOException ex) {
            return null;
        }

        List<XBAttribute> attributes = new ArrayList<>();
        int attributeIndex = 0;
        XBAttribute blockAttribute;
        do {
            try {
                blockAttribute = writer.getBlockAttribute(attributeIndex);
            } catch (XBProcessingException | IOException ex) {
                blockAttribute = null;
            }
            if (blockAttribute != null) {
                attributes.add(blockAttribute);
                attributeIndex++;
            }
        } while (blockAttribute != null);

        return attributes.toArray(new XBAttribute[0]);
    }

    @Override
    public XBAttribute getAttributeAt(int attributeIndex) {
        if (fixedBlock != null) {
            return fixedBlock.getAttributeAt(attributeIndex);
        }

        try {
            writer.seekBlock(this);
        } catch (XBProcessingException | IOException ex) {
            return null;
        }

        XBAttribute blockAttribute = null;
        try {
            blockAttribute = writer.getBlockAttribute(attributeIndex);
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBReaderBlock.class.getName()).log(Level.SEVERE, null, ex);
        }
        return blockAttribute == null ? null : blockAttribute.convertToNatural();
    }

    @Override
    public int getAttributesCount() {
        if (fixedBlock != null) {
            return fixedBlock.getAttributesCount();
        }

        try {
            writer.seekBlock(this);
            return writer.getBlockAttributesCount();
        } catch (XBProcessingException | IOException ex) {
            return 0;
        }
    }

    @Override
    public XBBlock[] getChildren() {
        if (fixedBlock != null) {
            return fixedBlock.getChildren();
        }

        int childrenCount = getChildrenCount();
        XBBlock[] result = new XBBlock[childrenCount];
        for (int i = 0; i < result.length; i++) {
            long[] childPath = Arrays.copyOf(blockPath, blockPath.length + 1);
            childPath[childPath.length - 1] = i;
            result[i] = writer.getBlock(childPath);
        }
        return result;
    }

    @Override
    public XBBlock getChildAt(int childIndex) {
        if (fixedBlock != null) {
            return fixedBlock.getChildAt(childIndex);
        }

        try {
            if (writer.hasBlockChildAt(childIndex)) {
                return writer.getBlockChild(this, childIndex);
            }
        } catch (XBProcessingException | IOException ex) {
        }

        return null;
    }

    @Override
    public int getChildrenCount() {
        if (fixedBlock != null) {
            return fixedBlock.getChildrenCount();
        }

        try {
            writer.seekBlock(this);
            return writer.getBlockChildrenCount();
        } catch (XBProcessingException | IOException ex) {
            return 0;
        }
    }

    @Override
    public InputStream getData() {
        if (fixedBlock != null) {
            return fixedBlock.getData();
        }

        try {
            writer.seekBlock(this);
            return writer.getBlockData();
        } catch (XBProcessingException | IOException ex) {
            return null;
        }
    }

    @Override
    public void close() throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public XBBlockData getBlockData() {
        if (fixedBlock != null) {
            return fixedBlock.getBlockData();
        }

        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setParent(XBBlock parent) {
        if (fixedBlock != null) {
            fixedBlock.setParent(parent);
        } else {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    @Override
    public void setTerminationMode(XBBlockTerminationMode terminationMode) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setDataMode(XBBlockDataMode dataMode) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setAttributes(XBAttribute[] attributes) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setAttributeAt(XBAttribute attribute, int attributeIndex) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setAttributesCount(int count) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void removeAttribute(int attributeIndex) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setChildren(XBBlock[] blocks) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setChildAt(XBBlock block, int childIndex) {
        throw new UnsupportedOperationException("Not supported yet.");
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
    public void setData(InputStream data) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setData(XBBlockData data) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void setFixedBlock(XBBlock block) {
        if (fixedBlock == null) {
            fixedBlock = new XBDefaultEditableBlock();
        }

        fixedBlock.setTerminationMode(block.getTerminationMode());
        XBBlockDataMode dataMode = block.getDataMode();
        fixedBlock.setDataMode(dataMode);

        if (dataMode == XBBlockDataMode.NODE_BLOCK) {
            fixedBlock.setAttributes(block.getAttributes());
            XBBlock[] children = block.getChildren();
            if (children.length > 0) {
                XBBlock[] modifiedChildren = new XBBlock[children.length];
                for (int i = 0; i < children.length; i++) {
                    XBWriterBlock modifiedChild = writer.createChildBlock(blockPath, i);
                    modifiedChild.setFixedBlock(children[i]);
                    modifiedChildren[i] = modifiedChild;
                }
                fixedBlock.setChildren(modifiedChildren);
            }
        } else {
            fixedBlock.setData(block.getBlockData());
        }
    }
}
