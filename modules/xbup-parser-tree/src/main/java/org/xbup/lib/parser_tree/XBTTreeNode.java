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
package org.xbup.lib.parser_tree;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.tree.TreeNode;
import org.xbup.lib.core.block.XBBasicBlockType;
import org.xbup.lib.core.block.XBBlockData;
import org.xbup.lib.core.block.XBBlockDataMode;
import org.xbup.lib.core.block.XBBlockType;
import org.xbup.lib.core.block.XBTBlock;
import org.xbup.lib.core.block.XBTEditableBlock;
import org.xbup.lib.core.block.declaration.XBBlockDecl;
import org.xbup.lib.core.block.declaration.XBContext;
import org.xbup.lib.core.catalog.XBCatalog;
import org.xbup.lib.core.parser.XBParseException;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.XBProcessingExceptionType;
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.block.XBDBlockType;
import org.xbup.lib.core.block.XBFBlockType;
import org.xbup.lib.core.block.XBFixedBlockType;
import org.xbup.lib.core.block.declaration.XBDeclBlockType;
import org.xbup.lib.core.parser.basic.wrapper.FixedDataInputStreamWrapper;
import org.xbup.lib.core.parser.basic.wrapper.TerminatedDataInputStreamWrapper;
import org.xbup.lib.core.parser.basic.wrapper.TerminatedDataOutputStreamWrapper;
import org.xbup.lib.core.parser.token.XBAttribute;
import org.xbup.lib.core.parser.token.convert.XBTokenBuffer;
import org.xbup.lib.core.parser.token.pull.convert.XBTProviderToPullProvider;
import org.xbup.lib.core.stream.FinishableStream;
import org.xbup.lib.core.type.XBData;
import org.xbup.lib.core.ubnumber.UBStreamable;
import org.xbup.lib.core.ubnumber.type.UBENat32;
import org.xbup.lib.core.ubnumber.type.UBNat32;
import org.xbup.lib.core.util.StreamUtils;

/**
 * Basic object model parser XBUP level 1 document block / tree node.
 *
 * @version 0.2.0 2015/09/19
 * @author ExBin Project (http://exbin.org)
 */
public class XBTTreeNode implements TreeNode, XBTEditableBlock, UBStreamable {

    private XBTTreeNode parent;

    private XBBlockTerminationMode terminationMode = XBBlockTerminationMode.SIZE_SPECIFIED;
    private XBBlockDataMode dataMode = XBBlockDataMode.NODE_BLOCK;

    private XBFixedBlockType blockType;
    private final List<XBAttribute> attributes;
    private final List<XBTBlock> children;
    private XBData data;

    private XBContext context;
    private XBBlockDecl blockDecl;
    private boolean singleAttributeType = true;

    public XBTTreeNode() {
        this(null);
    }

    /**
     * Creates a new instance of XBTTreeNode.
     *
     * This is not copy constructor.
     *
     * @param parent set it as parent node
     */
    public XBTTreeNode(XBTTreeNode parent) {
        this.parent = parent;

        children = new ArrayList<>();
        blockType = new XBFixedBlockType();
        attributes = new ArrayList<>();
        data = null;

        context = null;
        blockDecl = null;
    }

    /**
     * Returns index of child.
     *
     * @param node node to search match for
     * @return index of node if node found
     */
    @Override
    public int getIndex(TreeNode node) {
        return children.indexOf(node);
    }

    /**
     * Gets count of all subnodes.
     *
     * @return count of child nodes and all their child nodes
     */
    public int getSubNodesCount() {
        int result = children.size();
        if (result > 0) {
            for (Iterator<XBTBlock> it = children.iterator(); it.hasNext();) {
                result += ((XBTTreeNode) it.next()).getSubNodesCount();
            }
        }

        return result;
    }

    /**
     * Gets block position in tree in depth-first scan.
     *
     * @return position index
     */
    public int getBlockIndex() {
        if (parent != null) {
            int result = parent.getBlockIndex() + 1;
            Iterator<XBTBlock> it = parent.children.iterator();
            XBTTreeNode node = (XBTTreeNode) it.next();
            while (node != this) {
                result += node.getSubNodesCount() + 1;
                if (!it.hasNext()) {
                    return -1;// TODO: throw new Exception("Tree structure corrupted or internal error");
                }

                node = (XBTTreeNode) it.next();
            }

            return result;
        } else {
            return 0;
        }
    }

    public int getNodeIndexAfter() {
        if (dataMode == XBBlockDataMode.NODE_BLOCK) {
            return getBlockIndex() + getSubNodesCount() + 1;
        } else {
            return getBlockIndex() + 1;
        }
    }

    /**
     * Finds node of given index (recursive, no cache).
     *
     * @param index depth-first order index
     * @return node if found
     */
    public XBTTreeNode findNodeByIndex(long index) {
        if (index == 0) {
            return this;
        }

        index--;
        if (getChildrenCount() > 0) {
            List<Iterator<XBTBlock>> iterators = new ArrayList<>();
            iterators.add(children.iterator());
            int level = 0;
            XBTTreeNode node = (XBTTreeNode) iterators.get(level).next();
            while (node != null) {
                if (index == 0) {
                    return node;
                }

                if (node.getChildrenCount() > 0) {
                    iterators.add(node.children.iterator());
                    level++;
                }

                while (!iterators.get(level).hasNext()) {
                    if (level == 0) {
                        return null;
                    }

                    iterators.remove(level);
                    level--;
                }

                node = (XBTTreeNode) iterators.get(level).next();
                index--;
            }
        }
        return null;
    }

    @Override
    public int fromStreamUB(InputStream stream) throws IOException, XBProcessingException {
        return fromStreamUB(stream, false);
    }

    /**
     * Loads this node from data stream.
     *
     * @param stream data stream
     * @param terminable specify if terminator can be read
     * @return size of read bytes
     * @throws java.io.IOException
     */
    public int fromStreamUB(InputStream stream, boolean terminable) throws IOException, XBProcessingException {
        clear();
        UBNat32 attributePartSize = new UBNat32();
        int size = attributePartSize.fromStreamUB(stream);
        if (attributePartSize.getLong() == 0) {
            if (terminable) {
                return UBENat32.INFINITY_SIZE_UB;
            } else {
                throw new XBParseException("Unexpected terminator", XBProcessingExceptionType.UNEXPECTED_TERMINATOR);
            }
        } else {
            UBENat32 dataPartSize = new UBENat32();
            size += dataPartSize.fromStreamUB(stream);
            terminationMode = dataPartSize.isInfinity() ? XBBlockTerminationMode.TERMINATED_BY_ZERO : XBBlockTerminationMode.SIZE_SPECIFIED;
            Integer dataPartSizeValue = dataPartSize.isInfinity() ? null : dataPartSize.getInt();
            if (attributePartSize.getInt() == dataPartSize.getSizeUB()) {
                // Data Block
                dataMode = XBBlockDataMode.DATA_BLOCK;
                data = new XBData();
                // Process data block
                FinishableStream dataWrapper = (dataPartSizeValue == null)
                        ? new TerminatedDataInputStreamWrapper(stream)
                        : new FixedDataInputStreamWrapper(stream, dataPartSizeValue);
                data.loadFromStream((InputStream) dataWrapper);
                size += data.getDataSize() + (dataPartSizeValue == null ? 2 : 0);
            } else if (attributePartSize.getInt() < dataPartSize.getSizeUB()) {
                throw new XBParseException("Attribute overreached", XBProcessingExceptionType.ATTRIBUTE_OVERFLOW);
            } else {
                // Node Block
                dataMode = XBBlockDataMode.NODE_BLOCK;
                attributePartSize.setValue(attributePartSize.getInt() - dataPartSize.getSizeUB());

                // Load block type
                int itemSize = 0;
                UBNat32 groupId = new UBNat32();
                int groupIdSize = groupId.fromStreamUB(stream);
                itemSize += groupIdSize;
                size += groupIdSize;
                if (itemSize > attributePartSize.getInt()) {
                    throw new XBParseException("Attribute overreached", XBProcessingExceptionType.ATTRIBUTE_OVERFLOW);
                }

                UBNat32 blockId = new UBNat32();
                if (itemSize < attributePartSize.getInt()) {
                    int blockIdSize = blockId.fromStreamUB(stream);
                    itemSize += blockIdSize;
                    size += blockIdSize;
                    if (itemSize > attributePartSize.getInt()) {
                        throw new XBParseException("Attribute overreached", XBProcessingExceptionType.ATTRIBUTE_OVERFLOW);
                    }

                    setFixedBlockType(new XBFixedBlockType(groupId, blockId));
                } else {
                    setFixedBlockType(new XBFixedBlockType(groupId, blockId));
                    singleAttributeType = true;
                }

                // Load attributes
                if (attributePartSize.getInt() > itemSize) {
                    do {
                        UBNat32 attribute = new UBNat32();
                        int attributeSize = attribute.fromStreamUB(stream);
                        itemSize += attributeSize;
                        attributes.add(attribute);
                        size += attributeSize;
                        if (itemSize > attributePartSize.getInt()) {
                            throw new XBParseException("Attribute overreached", XBProcessingExceptionType.ATTRIBUTE_OVERFLOW);
                        }
                    } while (itemSize < attributePartSize.getInt());
                }

                if (dataPartSize.isInfinity() || dataPartSize.getInt() > 0) {
                    size += childrenFromStreamUB(stream, dataPartSizeValue);
                }
            }
        }

        return size;
    }

    @Override
    public int toStreamUB(OutputStream stream) throws IOException {
        if (dataMode == XBBlockDataMode.NODE_BLOCK) {
            UBENat32 dataPartSize;
            if (terminationMode == XBBlockTerminationMode.SIZE_SPECIFIED) {
                dataPartSize = new UBENat32(childrenSizeUB());
            } else {
                dataPartSize = new UBENat32();
                dataPartSize.setInfinity();
            }

            UBNat32 attributePartSize = new UBNat32(dataPartSize.getSizeUB() + attributesSizeUB() + typeSizeUB());
            int size = attributePartSize.toStreamUB(stream);
            size += dataPartSize.toStreamUB(stream);

            // Write block type
            size += blockType.getGroupID().toStreamUB(stream);
            if (!singleAttributeType) {
                size += blockType.getBlockID().toStreamUB(stream);
            }

            // Write attributes
            if (getAttributesCount() > 0) {
                Iterator<XBAttribute> iter = attributes.iterator();
                while (iter.hasNext()) {
                    size += iter.next().toStreamUB(stream);
                }
            }
            size += childrenToStreamUB(stream);
            if (terminationMode == XBBlockTerminationMode.TERMINATED_BY_ZERO) {
                stream.write(0);
                size++;
            }

            return size;
        } else {
            int size = 0;
            if (terminationMode == XBBlockTerminationMode.SIZE_SPECIFIED) {
                UBENat32 dataPartSize = new UBENat32(getDataSize());
                UBNat32 attributePartSize = new UBNat32(dataPartSize.getSizeUB());
                size += attributePartSize.toStreamUB(stream);
                size += dataPartSize.toStreamUB(stream);
                data.saveToStream(stream);
                size += getDataSize();
            } else {
                UBNat32 attributePartSize = new UBNat32(UBENat32.INFINITY_SIZE_UB);
                size += attributePartSize.toStreamUB(stream);
                UBENat32 dataPartSize = new UBENat32();
                dataPartSize.setInfinity();
                size += dataPartSize.toStreamUB(stream);

                OutputStream streamWrapper = new TerminatedDataOutputStreamWrapper(stream);
                StreamUtils.copyInputStreamToOutputStream(data.getDataInputStream(), streamWrapper);
                int dataSize = (int) ((FinishableStream) streamWrapper).finish();
                size += dataSize;
            }
            return size;
        }
    }

    public int childrenToStreamUB(OutputStream stream) throws IOException {
        Iterator<XBTBlock> iter = children.iterator();
        int size = 0;
        while (iter.hasNext()) {
            size += ((XBTTreeNode) iter.next()).toStreamUB(stream);
        }
        return size;
    }

    @Override
    public int getSizeUB() {
        if (dataMode == XBBlockDataMode.NODE_BLOCK) {
            int size = childrenSizeUB();
            UBENat32 dataPartSize = new UBENat32();
            if (terminationMode == XBBlockTerminationMode.SIZE_SPECIFIED) {
                dataPartSize.setValue(size);
            } else {
                dataPartSize.setInfinity();
            }

            UBNat32 attrPartSize = new UBNat32(dataPartSize.getSizeUB() + attributesSizeUB() + typeSizeUB());
            size += attrPartSize.getSizeUB();
            size += attrPartSize.getInt();

            if (terminationMode == XBBlockTerminationMode.TERMINATED_BY_ZERO) {
                size++;
            }

            return size;
        } else {
            int size = 0;
            UBENat32 dataPartSize;
            if (terminationMode == XBBlockTerminationMode.SIZE_SPECIFIED) {
                dataPartSize = new UBENat32(getDataSize());
                UBNat32 attrPartSize = new UBNat32(dataPartSize.getSizeUB());
                size += attrPartSize.getSizeUB();
                size += dataPartSize.getSizeUB();
                size += getDataSize();
            } else {
                try {
                    size += 2 + XBTokenBuffer.computeAdjustedSize(getData());
                } catch (IOException ex) {
                    Logger.getLogger(XBTreeNode.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            return size;
        }
    }

    public int childrenSizeUB() {
        Iterator<XBTBlock> iter = children.iterator();
        int size = 0;
        while (iter.hasNext()) {
            size += ((XBTTreeNode) iter.next()).getSizeUB();
        }

        return size;
    }

    public int attributesSizeUB() {
        if (getAttributesCount() > 0) {
            int size = 0;
            for (XBAttribute attribute : attributes) {
                size += attribute.getSizeUB();
            }

            return size;
        } else {
            return 0;
        }
    }

    @Override
    public void clear() {
        children.clear();
        if (data != null) {
            data = null;
        }
        attributes.clear();

        blockDecl = null;
    }

    @Override
    public InputStream getData() {
        if (data == null) {
            return null;
        }

        return data.getDataInputStream();
    }

    @Override
    public XBBlockData getBlockData() {
        return data;
    }

    public long getDataSize() {
        if (data == null) {
            return 0;
        }

        return data.getDataSize();
    }

    @Override
    public void setData(InputStream source) {
        if (source == null) {
            data = null;
        } else {
            data = new XBData();
            data.loadFromStream(source);
        }
    }

    @Override
    public void setData(XBBlockData newData) {
        data = new XBData();
        data.setData(newData);
    }

    /**
     * Creates deep copy of this node.
     *
     * @return tree node
     */
    public XBTTreeNode cloneNode() {
        return cloneNode(true);
    }

    /**
     * Creates copy of this node
     *
     * @param recursive if true, perform deep copy or else shallow copy.
     * @return tree node
     */
    public XBTTreeNode cloneNode(boolean recursive) {
        XBTTreeNode node = new XBTTreeNode();
        node.setBlockDecl(blockDecl);
        node.setFixedBlockType(blockType);
        node.setSingleAttributeType(singleAttributeType);
        node.setDataMode(dataMode);

        if (data != null) {
            node.data = data.copy();
        }

        for (XBAttribute attribute : attributes) {
            node.addAttribute(new UBNat32(attribute.getNaturalLong()));
        }

        List<XBTBlock> cloneChildren = new ArrayList<>();
        for (XBTBlock block : children) {
            if (recursive) {
                XBTTreeNode childNode = ((XBTTreeNode) block).cloneNode(true);
                childNode.setParent(node);
                cloneChildren.add(childNode);
            } else {
                cloneChildren.add(block);
            }
        }

        node.setChildren(cloneChildren.toArray(new XBTBlock[0]));
        return node;
    }

    /**
     * This method instantiates new child node.
     *
     * @param childIndex child index
     * @return new
     */
    @Override
    public XBTTreeNode createNewChild(int childIndex) {
        XBTTreeNode childNode = new XBTTreeNode(this);
        setChildAt(childNode, childIndex);
        return childNode;
    }

    public long getBlockSize() {
        return getSizeUB();
    }

    public void clearAttributes() {
        attributes.clear();
    }

    public void addAttribute(XBAttribute attribute) {
        if (attributes.isEmpty() && singleAttributeType) {
            singleAttributeType = false;
        }

        attributes.add(attribute);
    }

    @Override
    public void removeAttribute(int index) {
        attributes.remove(index);
    }

    private int typeSizeUB() {
        return singleAttributeType ? blockType.getGroupID().getSizeUB() : blockType.getSizeUB();
    }

    @Override
    public XBTTreeNode getParent() {
        return parent;
    }

    @Override
    public void setParent(XBTBlock parent) {
        this.parent = (XBTTreeNode) parent;
    }

    @Override
    public XBBlockTerminationMode getTerminationMode() {
        return terminationMode;
    }

    @Override
    public void setTerminationMode(XBBlockTerminationMode terminationMode) {
        if (terminationMode == null) {
            throw new NullPointerException();
        }

        this.terminationMode = terminationMode;
    }

    @Override
    public XBBlockDataMode getDataMode() {
        return dataMode;
    }

    @Override
    public void setDataMode(XBBlockDataMode dataMode) {
        if (dataMode == null) {
            throw new NullPointerException();
        }

        if (dataMode != this.dataMode) {
            switch (dataMode) {
                case DATA_BLOCK: {
                    data = new XBData();
                    clearAttributes();
                    break;
                }
                case NODE_BLOCK: {
                    data = null;
                    clearAttributes();
                    break;
                }
            }
        }

        this.dataMode = dataMode;
    }

    @Override
    public XBBlockType getBlockType() {
        return blockDecl != null ? new XBDeclBlockType(blockDecl) : getFixedBlockType();
    }

    @Override
    public void setBlockType(XBBlockType blockType) {
        if (blockType instanceof XBFBlockType) {
            blockDecl = null;
            if (context != null) {
                XBDeclBlockType declBlockType = context.getDeclBlockType((XBFBlockType) blockType);
                if (declBlockType != null) {
                    blockDecl = declBlockType.getBlockDecl();
                }
            }
            setFixedBlockType(((XBFixedBlockType) blockType));
        } else if (blockType instanceof XBDBlockType) {
            blockDecl = ((XBDBlockType) blockType).getBlockDecl();
            XBFixedBlockType fixedBlockType = context != null ? context.getFixedBlockType((XBDBlockType) blockType) : null;
            setFixedBlockType(fixedBlockType != null ? fixedBlockType : new XBFixedBlockType());
        } else {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    public XBFixedBlockType getFixedBlockType() {
        return blockType;
    }

    public void setFixedBlockType(XBFixedBlockType blockType) {
        if (blockType == null) {
            throw new NullPointerException();
        }

        if (!blockType.getBlockID().isZero()) {
            singleAttributeType = false;
        }
        this.blockType = blockType;
    }

    @Override
    public XBAttribute getAttributeAt(int attributeIndex) {
        return attributeIndex < attributes.size() ? attributes.get(attributeIndex) : null;
    }

    @Override
    public void setAttributeAt(XBAttribute attribute, int attributeIndex) {
        singleAttributeType = false;
        while (attributes.size() <= attributeIndex) {
            attributes.add(new UBNat32());
        }

        attributes.set(attributeIndex, attribute);
    }

    public int getAttributeValue(int attributeIndex) {
        return attributeIndex < attributes.size() ? attributes.get(attributeIndex).getNaturalInt() : 0;
    }

    @Override
    public int getAttributesCount() {
        return attributes.size();
    }

    @Override
    public void setAttributesCount(int count) {
        singleAttributeType = false;
        if (attributes.size() < count) {
            for (int i = attributes.size(); i < count; i++) {
                attributes.add(new UBNat32());
            }
        } else if (attributes.size() > count) {
            for (int i = attributes.size() - 1; i >= count; i--) {
                attributes.remove(i);
            }
        }
    }

    @Override
    public XBAttribute[] getAttributes() {
        return attributes.toArray(new XBAttribute[0]);
    }

    @Override
    public void setAttributes(XBAttribute[] attributes) {
        this.attributes.clear();
        this.attributes.addAll(Arrays.asList(attributes));
        singleAttributeType = false;
    }

    @Override
    public XBTTreeNode getChildAt(int childIndex) {
        return childIndex < children.size() ? (XBTTreeNode) children.get(childIndex) : null;
    }

    @Override
    public void setChildAt(XBTBlock block, int childIndex) {
        while (children.size() <= childIndex) {
            children.add(new XBTTreeNode(this));
        }

        children.set(childIndex, block);
    }

    public void addChild(XBTTreeNode child) {
        children.add(child);
    }

    @Override
    public void removeChild(int index) {
        children.remove(index);
    }

    @Override
    public int getChildrenCount() {
        return children.size();
    }

    @Override
    public int getChildCount() {
        return getChildrenCount();
    }

    @Override
    public void setChildrenCount(int count) {
        if (children.size() < count) {
            for (int i = children.size(); i < count; i++) {
                children.add(new XBTTreeNode(this));
            }
        } else if (children.size() > count) {
            for (int i = children.size() - 1; i >= count; i--) {
                children.remove(i);
            }
        }
    }

    @Override
    public XBTBlock[] getChildren() {
        return children.toArray(new XBTBlock[0]);
    }

    @Override
    public void setChildren(XBTBlock[] children) {
        this.children.clear();
        this.children.addAll(Arrays.asList(children));
    }

    @Override
    public boolean getAllowsChildren() {
        return dataMode != XBBlockDataMode.NODE_BLOCK;
    }

    @Override
    public boolean isLeaf() {
        return dataMode == XBBlockDataMode.DATA_BLOCK;
    }

    @Override
    public Enumeration children() {
        return Collections.enumeration(children);
    }

    public void setContext(XBContext context) {
        this.context = context;
    }

    public XBContext getContext() {
        return context;
    }

    public XBBlockDecl getBlockDecl() {
        return blockDecl;
    }

    public void setBlockDecl(XBBlockDecl blockDecl) {
        this.blockDecl = blockDecl;
    }

    public boolean getSingleAttributeType() {
        return singleAttributeType;
    }

    public void setSingleAttributeType(boolean singleAttributeType) {
        this.singleAttributeType = singleAttributeType;
    }

    /**
     * Performs tree depth specification processing.
     *
     * @param catalog instance of catalog
     * @param parentContext parent context
     */
    public void processSpec(XBCatalog catalog, XBContext parentContext) {
        context = parentContext;
        if (dataMode == XBBlockDataMode.NODE_BLOCK) {
            if (context != null) {
                XBDeclBlockType declBlockType = context.getDeclBlockType(getFixedBlockType());
                blockDecl = declBlockType == null ? null : declBlockType.getBlockDecl();
            } else {
                blockDecl = null;
            }

            if (getFixedBlockType().getAsBasicType() == XBBasicBlockType.DECLARATION && catalog != null) {
                // Process declaration block
                XBContext childContext = catalog.processDeclaration(parentContext, new XBTProviderToPullProvider(new XBTTreeWriter(new XBTBlockFixedSource(this))));
                long blockPos = 0;
                for (Iterator it = children.iterator(); it.hasNext();) {
                    blockPos++;
                    XBTTreeNode item = ((XBTTreeNode) it.next());
                    item.processSpec(catalog, blockPos == 2 ? childContext : parentContext);
                }

                return;
            }
        } else {
            blockDecl = null;
        }

        for (Iterator it = children.iterator(); it.hasNext();) {
            XBTTreeNode item = ((XBTTreeNode) it.next());
            item.processSpec(catalog, parentContext);
        }
    }

    public XBTTreeNode followPointer(int index) {
        if (getAttributesCount() >= index) {
            int childIndex = ((UBNat32) getAttributeAt(index)).getInt();
            if ((childIndex > 0) && (children.size() >= childIndex)) {
                return (XBTTreeNode) getChildAt(childIndex - 1);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * Reads all children blocks.
     *
     * @param stream Source input stream
     * @param maxSize Maximum size of bytes to consume. Otherwise Exception.
     * @return
     * @throws java.io.IOException
     */
    public int childrenFromStreamUB(InputStream stream, Integer maxSize) throws IOException, XBProcessingException {
        children.clear();
        if (maxSize != null && maxSize == 0) {
            return 0;
        }

        int childSize;
        int size = 0;
        do {
            XBTTreeNode child = new XBTTreeNode();
            child.setParent(this);
            childSize = child.fromStreamUB(stream, maxSize == null);
            size += childSize;
            if (childSize > 1) {
                if (maxSize != null && size > maxSize) {
                    throw new XBParseException("Block overreached", XBProcessingExceptionType.BLOCK_OVERFLOW);
                }

                children.add(child);
            }
        } while ((maxSize == null && childSize != 1) || (maxSize != null && maxSize > 0 && size < maxSize));

        return size;
    }

    private static class XBTBlockFixedSource implements XBTBlock {

        private final XBTTreeNode block;

        public XBTBlockFixedSource(XBTTreeNode block) {
            this.block = block;
        }

        @Override
        public XBTBlock getParent() {
            return block.getParent();
        }

        @Override
        public XBBlockTerminationMode getTerminationMode() {
            return block.getTerminationMode();
        }

        @Override
        public XBBlockDataMode getDataMode() {
            return block.getDataMode();
        }

        @Override
        public XBBlockType getBlockType() {
            return block.getFixedBlockType();
        }

        @Override
        public XBAttribute[] getAttributes() {
            return block.getAttributes();
        }

        @Override
        public XBAttribute getAttributeAt(int attributeIndex) {
            return block.getAttributeAt(attributeIndex);
        }

        @Override
        public int getAttributesCount() {
            return block.getAttributesCount();
        }

        @Override
        public XBTBlock[] getChildren() {
            XBTBlock[] result = new XBTBlock[block.getChildrenCount()];
            int i = 0;
            for (XBTBlock child : block.getChildren()) {
                result[i++] = new XBTBlockFixedSource((XBTTreeNode) child);
            }

            return result;
        }

        @Override
        public XBTBlock getChildAt(int childIndex) {
            return new XBTBlockFixedSource(block.getChildAt(childIndex));
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

        public long getDataSize() {
            return block.getDataSize();
        }

        public int getBlockIndex() {
            return block.getBlockIndex();
        }

        public long getBlockSize() {
            return block.getBlockSize();
        }
    }

    /**
     * Creates XBTTreeNode copy of given block and all of its children.
     *
     * @param block source block
     * @return node
     */
    public static XBTTreeNode createTreeCopy(XBTBlock block) {
        return createTreeCopy(block, null);
    }

    /**
     * Creates XBTTreeNode copy of given block and all of its children.
     *
     * @param block source block
     * @param parent parent node
     * @return node
     */
    public static XBTTreeNode createTreeCopy(XBTBlock block, XBTTreeNode parent) {
        XBTTreeNode node = new XBTTreeNode();
        node.setParent(parent);
        node.setDataMode(block.getDataMode());
        node.setTerminationMode(block.getTerminationMode());
        if (block.getDataMode() == XBBlockDataMode.NODE_BLOCK) {
            node.setBlockType(block.getBlockType());
            if (block.getAttributesCount() > 0) {
                node.setAttributes(block.getAttributes());
            }

            if (block.getChildrenCount() > 0) {
                for (XBTBlock childBlock : block.getChildren()) {
                    XBTTreeNode childNode = createTreeCopy(childBlock, node);
                    node.addChild(childNode);
                }
            }
        } else {
            XBData data = new XBData();
            data.loadFromStream(block.getBlockData().getDataInputStream());
            node.setData(data);
        }

        return node;
    }
}
