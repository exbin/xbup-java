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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import javax.swing.tree.TreeNode;
import org.xbup.lib.core.block.XBBlock;
import org.xbup.lib.core.block.XBBlockData;
import org.xbup.lib.core.block.XBBlockDataMode;
import org.xbup.lib.core.block.XBEditableBlock;
import org.xbup.lib.core.parser.XBParseException;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.XBProcessingExceptionType;
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.parser.basic.wrapper.FixedDataInputStreamWrapper;
import org.xbup.lib.core.parser.basic.wrapper.TerminatedDataInputStreamWrapper;
import org.xbup.lib.core.parser.basic.wrapper.TerminatedDataOutputStreamWrapper;
import org.xbup.lib.core.parser.token.XBAttribute;
import org.xbup.lib.core.stream.FinishableStream;
import org.xbup.lib.core.type.XBData;
import org.xbup.lib.core.ubnumber.UBStreamable;
import org.xbup.lib.core.ubnumber.type.UBENat32;
import org.xbup.lib.core.ubnumber.type.UBNat32;
import org.xbup.lib.core.util.StreamUtils;

/**
 * Basic object model parser XBUP level 0 document block / tree node.
 *
 * @version 0.1.25 2015/07/26
 * @author XBUP Project (http://xbup.org)
 */
public class XBTreeNode implements XBEditableBlock, TreeNode, UBStreamable {

    private XBTreeNode parent;

    private XBBlockTerminationMode terminationMode = XBBlockTerminationMode.SIZE_SPECIFIED;
    private XBBlockDataMode dataMode = XBBlockDataMode.NODE_BLOCK;

    private final List<XBAttribute> attributes = new ArrayList<>();
    private final List<XBBlock> children = new ArrayList<>();
    private XBData data = null;

    public XBTreeNode() {
        this(null);
    }

    /**
     * Creates a new instance of XBTreeNode with parent node assigned.
     *
     * This is NOT copy constructor.
     *
     * @param parent Set it as parent node
     */
    public XBTreeNode(XBTreeNode parent) {
        this.parent = parent;
    }

    /**
     * Gets count of all subnodes.
     *
     * @return count of child nodes and all their child nodes
     */
    public int getSubNodesCount() {
        int result = children.size();
        if (result > 0) {
            for (Iterator<XBBlock> it = children.iterator(); it.hasNext();) {
                result += ((XBTreeNode) it.next()).getSubNodesCount();
            }
        }

        return result;
    }

    /**
     * Gets index in depth-first order.
     *
     * @return node index
     */
    @Override
    public int getBlockIndex() {
        if (parent != null) {
            int result = parent.getBlockIndex() + 1;
            Iterator<XBBlock> it = parent.children.iterator();
            XBTreeNode node = (XBTreeNode) it.next();
            while (node != this) {
                result += node.getSubNodesCount() + 1;
                if (!it.hasNext()) {
                    return -1;
                    // TODO: throw new Exception("Tree structure corrupted or internal error");
                }

                node = (XBTreeNode) it.next();
            }
            return result;
        } else {
            return 0;
        }
    }

    public int getblockIndexAfter() {
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
    public XBTreeNode findNodeByIndex(long index) {
        if (index == 0) {
            return this;
        }

        index--;
        if (getChildrenCount() > 0) {
            List<Iterator<XBBlock>> iterators = new ArrayList<>();
            iterators.add(children.iterator());
            int depthLevel = 0;
            XBTreeNode node = (XBTreeNode) iterators.get(depthLevel).next();
            while (node != null) {
                if (index == 0) {
                    return node;
                }

                if (node.getChildrenCount() > 0) {
                    iterators.add(node.children.iterator());
                    depthLevel++;
                }

                while (!iterators.get(depthLevel).hasNext()) {
                    if (depthLevel == 0) {
                        return null;
                    }
                    iterators.remove(depthLevel);
                    depthLevel--;
                }

                node = (XBTreeNode) iterators.get(depthLevel).next();
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
                return 1;
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
                size += data.getDataSize();
            } else if (attributePartSize.getInt() < dataPartSize.getSizeUB()) {
                throw new XBParseException("Attribute overreached", XBProcessingExceptionType.ATTRIBUTE_OVERFLOW);
            } else {
                // Node Block
                dataMode = XBBlockDataMode.NODE_BLOCK;
                attributePartSize.setValue(attributePartSize.getInt() - dataPartSize.getSizeUB());
                int itemSize = 0;
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

                if (dataPartSize.isInfinity() || dataPartSize.getInt() > 0) {
                    size += childrenFromStreamUB(stream, dataPartSizeValue);
                }
            }
        }

        return size;
    }

    /**
     * Loads children from data stream.
     *
     * @param stream input stream
     * @param maxSize Maximum size of bytes to consume. Otherwise Exception.
     * Zero means terminationMode block.
     * @return size of bytes read
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
            XBTreeNode child = new XBTreeNode();
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

    @Override
    public int toStreamUB(OutputStream stream) throws IOException {
        if (getDataMode() == XBBlockDataMode.DATA_BLOCK) {
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
        } else {
            UBENat32 dataPartSize;
            if (terminationMode == XBBlockTerminationMode.SIZE_SPECIFIED) {
                dataPartSize = new UBENat32(childrenSizeUB());
            } else {
                dataPartSize = new UBENat32();
                dataPartSize.setInfinity();
            }

            UBNat32 attrPartSize = new UBNat32(dataPartSize.getSizeUB() + attributesSizeUB());
            int size = attrPartSize.toStreamUB(stream);
            size += dataPartSize.toStreamUB(stream);
            Iterator<XBAttribute> iter = attributes.iterator();
            while (iter.hasNext()) {
                size += iter.next().toStreamUB(stream);
            }

            size += childrenToStreamUB(stream);

            if (terminationMode == XBBlockTerminationMode.TERMINATED_BY_ZERO) {
                size += (new UBNat32()).toStreamUB(stream);
            }

            return size;
        }
    }

    public int childrenToStreamUB(OutputStream stream) throws IOException {
        Iterator<XBBlock> iter = children.iterator();
        int size = 0;
        while (iter.hasNext()) {
            size += ((XBTreeNode) iter.next()).toStreamUB(stream);
        }

        return size;
    }

    @Override
    public int getSizeUB() {
        if (getDataMode() == XBBlockDataMode.DATA_BLOCK) {
            UBENat32 dataPartSize = new UBENat32(getDataSize());
            UBNat32 attrPartSize = new UBNat32(dataPartSize.getSizeUB());
            int size = attrPartSize.getSizeUB();
            size += dataPartSize.getSizeUB();
            size += getDataSize();
            return size;
        } else {
            int size = childrenSizeUB();
            UBENat32 dataPartSize = new UBENat32();
            if (terminationMode == XBBlockTerminationMode.SIZE_SPECIFIED) {
                dataPartSize.setValue(size);
            } else {
                dataPartSize.setInfinity();
            }

            UBNat32 attrPartSize = new UBNat32(dataPartSize.getSizeUB() + attributesSizeUB());
            size += attrPartSize.getSizeUB();
            size += attrPartSize.getInt();

            if (terminationMode == XBBlockTerminationMode.TERMINATED_BY_ZERO) {
                size++;
            }

            return size;
        }
    }

    @Override
    public boolean getAllowsChildren() {
        return dataMode != XBBlockDataMode.NODE_BLOCK;
    }

    @Override
    public boolean isLeaf() {
        return dataMode == XBBlockDataMode.DATA_BLOCK || getChildrenCount() == 0;
    }

    @Override
    public Enumeration children() {
        return Collections.enumeration(children);
    }

    @Override
    public void clear() {
        children.clear();
        attributes.clear();

        if (data != null) {
            data = null;
        }
    }

    public int childrenSizeUB() {
        Iterator<XBBlock> iter = children.iterator();
        int size = 0;
        while (iter.hasNext()) {
            size += ((XBTreeNode) iter.next()).getSizeUB();
        }

        return size;
    }

    public int attributesSizeUB() {
        Iterator<XBAttribute> iter = attributes.iterator();
        int size = 0;
        while (iter.hasNext()) {
            size += iter.next().getSizeUB();
        }

        return size;
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

    @Override
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

    @Override
    public void clearData() {
        data = null;
    }

    public XBTreeNode cloneNode(boolean recursive) {
        XBTreeNode node = new XBTreeNode(parent);
        node.setDataMode(dataMode);
        node.data = data.copy();

        for (XBAttribute attribute : attributes) {
            node.addAttribute(new UBNat32(attribute.getNaturalLong()));
        }

        List<XBBlock> cloneChildren = new ArrayList<>();
        for (XBBlock block : children) {
            if (recursive) {
                cloneChildren.add(((XBTreeNode) block).cloneNode(true));
            } else {
                cloneChildren.add(block);
            }
        }

        node.setChildren(cloneChildren.toArray(new XBBlock[0]));
        return node;
    }

    /**
     * This method instantiates new child node.
     *
     * @param childIndex child index
     * @return new
     */
    @Override
    public XBTreeNode createNewChild(int childIndex) {
        XBTreeNode childNode = new XBTreeNode(this);
        setChildAt(childNode, childIndex);
        return childNode;
    }

    @Override
    public long getBlockSize() {
        return getSizeUB();
    }

    @Override
    public XBTreeNode getParent() {
        return parent;
    }

    @Override
    public void setParent(XBBlock parent) {
        if (parent == null || parent instanceof XBTreeNode) {
            this.parent = (XBTreeNode) parent;
        } else {
            throw new IllegalArgumentException("Only XBTreeNode is allowed as parent for XBTreeNode");
        }
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

        this.dataMode = dataMode;
    }

    @Override
    public XBAttribute getAttributeAt(int index) {
        return attributes.get(index);
    }

    @Override
    public void setAttributeAt(XBAttribute attribute, int attributeIndex) {
        while (attributes.size() <= attributeIndex) {
            attributes.add(new UBNat32());
        }

        attributes.set(attributeIndex, attribute);
    }

    @Override
    public int getAttributesCount() {
        return attributes.size();
    }

    @Override
    public void setAttributesCount(int count) {
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

    public void addAttribute(XBAttribute attribute) {
        attributes.add(attribute);
    }

    @Override
    public void removeAttribute(int index) {
        attributes.remove(index);
    }

    @Override
    public XBAttribute[] getAttributes() {
        return attributes.toArray(new XBAttribute[0]);
    }

    @Override
    public void setAttributes(XBAttribute[] attributes) {
        this.attributes.clear();
        this.attributes.addAll(Arrays.asList(attributes));
    }

    @Override
    public XBTreeNode getChildAt(int childIndex) {
        return (XBTreeNode) children.get(childIndex);
    }

    @Override
    public void setChildAt(XBBlock block, int childIndex) {
        while (children.size() <= childIndex) {
            children.add(new XBTreeNode(this));
        }

        children.set(childIndex, block);
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

    public void addChild(XBTreeNode child) {
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
                children.add(new XBTreeNode(this));
            }
        } else if (children.size() > count) {
            for (int i = children.size() - 1; i >= count; i--) {
                children.remove(i);
            }
        }
    }

    @Override
    public XBBlock[] getChildren() {
        return children.toArray(new XBBlock[0]);
    }

    @Override
    public void setChildren(XBBlock[] children) {
        this.children.clear();
        this.children.addAll(Arrays.asList(children));
    }

    /**
     * Creates XBTreeNode copy of given block and all of its children.
     *
     * @param block source block
     * @return node
     */
    public static XBTreeNode createTreeCopy(XBBlock block) {
        return createTreeCopy(block, null);
    }

    /**
     * Creates XBTreeNode copy of given block and all of its children.
     *
     * @param block source block
     * @param parent parent node
     * @return node
     */
    public static XBTreeNode createTreeCopy(XBBlock block, XBTreeNode parent) {
        XBTreeNode node = new XBTreeNode();
        node.setParent(parent);
        node.setDataMode(block.getDataMode());
        node.setTerminationMode(block.getTerminationMode());
        if (block.getDataMode() == XBBlockDataMode.NODE_BLOCK) {
            if (block.getAttributesCount() > 0) {
                node.setAttributes(block.getAttributes());
            }

            if (block.getChildrenCount() > 0) {
                for (XBBlock childBlock : block.getChildren()) {
                    XBTreeNode childNode = createTreeCopy(childBlock, node);
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
