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
package org.xbup.lib.xb.parser.tree;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.tree.TreeNode;
import org.xbup.lib.xb.block.XBBlock;
import org.xbup.lib.xb.block.XBBlockDataMode;
import org.xbup.lib.xb.block.XBEditableBlock;
import org.xbup.lib.xb.parser.XBParseException;
import org.xbup.lib.xb.parser.XBProcessingException;
import org.xbup.lib.xb.parser.XBProcessingExceptionType;
import org.xbup.lib.xb.parser.basic.XBListener;
import org.xbup.lib.xb.parser.basic.XBProvider;
import org.xbup.lib.xb.block.XBBlockTerminationMode;
import org.xbup.lib.xb.ubnumber.UBNatural;
import org.xbup.lib.xb.ubnumber.UBStreamable;
import org.xbup.lib.xb.ubnumber.type.UBENat32;
import org.xbup.lib.xb.ubnumber.type.UBNat32;
import org.xbup.lib.xb.util.CopyStreamUtils;

/**
 * Basic object model parser XBUP level 0 document block / tree node.
 *
 * @version 0.1 wr23.0 2014/02/20
 * @author XBUP Project (http://xbup.org)
 */
public class XBTreeNode implements XBEditableBlock, TreeNode, UBStreamable {

    private XBTreeNode parent;

    private XBBlockTerminationMode terminationMode;
    private XBBlockDataMode dataMode;

    private List<UBNatural> attributes;
    private List<XBBlock> children;
    private byte[] data;

    /**
     * Creates a new instance of XBTreeNode.
     */
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
        terminationMode = XBBlockTerminationMode.SIZE_SPECIFIED;
        dataMode = XBBlockDataMode.NODE_BLOCK;

        children = new ArrayList<>();
        attributes = new ArrayList<>();
        data = null;
    }

    /**
     * Get count of all subnodes.
     *
     * @return count of child nodes and all their child nodes
     */
    public int getSubNodesCount() {
        if (children != null) {
            int result = children.size();
            if (result > 0) {
                for (Iterator<XBBlock> it = children.iterator(); it.hasNext();) {
                    result += ((XBTreeNode) it.next()).getSubNodesCount();
                }
            }

            return result;
        }
        return 0;
    }

    /**
     * Get index in depth-first order.
     *
     * @return node index
     */
    public int getNodeIndex() {
        if (parent != null) {
            int result = parent.getNodeIndex() + 1;
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

    public int getNodeIndexAfter() {
        if (dataMode == XBBlockDataMode.NODE_BLOCK) {
            return getNodeIndex() + getSubNodesCount() + 1;
        } else {
            return getNodeIndex() + 1;
        }
    }

    /**
     * Find node of given index (recursive, no cache).
     *
     * @param index depth-first order index
     * @return node if found
     */
    public XBTreeNode findNodeByIndex(long index) {
        if (index == 0) {
            return this;
        }

        index--;
        if (getChildCount() > 0) {
            List<Iterator<XBBlock>> iterators = new ArrayList<>();
            iterators.add(children.iterator());
            int level = 0;
            XBTreeNode node = (XBTreeNode) iterators.get(level).next();
            while (node != null) {
                if (index == 0) {
                    return node;
                }

                if (node.getChildCount() > 0) {
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

                node = (XBTreeNode) iterators.get(level).next();
                index--;
            }
        }

        return null;
    }

    @Override
    public int toStreamUB(OutputStream stream) throws IOException {
        if (getDataMode() == XBBlockDataMode.DATA_BLOCK) {
            UBENat32 dataPartSize = new UBENat32(getDataSize());
            UBNat32 attributePartSize = new UBNat32(dataPartSize.getSizeUB());
            int size = attributePartSize.toStreamUB(stream);
            size += dataPartSize.toStreamUB(stream);
            if (data != null) {
                stream.write(data);
            }

            size += getDataSize();
            return size;
        } else {
            UBENat32 childrenSize = new UBENat32();
            if (terminationMode == XBBlockTerminationMode.SIZE_SPECIFIED) {
                childrenSize = new UBENat32(childrenSizeUB());
            } else {
                childrenSize.setInfinity();
            }

            UBNat32 attrSize = new UBNat32(childrenSize.getSizeUB() + attrSizeUB());
            int size = attrSize.toStreamUB(stream);
            size += childrenSize.toStreamUB(stream);
            if (getAttributes() != null) {
                Iterator<UBNatural> iter = getAttributes().iterator();
                while (iter.hasNext()) {
                    size += iter.next().toStreamUB(stream);
                }
            }

            if (getChildren() != null) {
                size += childrenToStreamUB(stream);
            }

            if (terminationMode == XBBlockTerminationMode.ZERO_TERMINATED) {
                size += (new UBNat32()).toStreamUB(stream);
            }

            return size;
        }
    }

    public int childrenToStreamUB(OutputStream stream) throws IOException {
        if (getChildren() != null) {
            Iterator<XBBlock> iter = getChildren().iterator();
            int size = 0;
            while (iter.hasNext()) {
                size += ((XBTreeNode) iter.next()).toStreamUB(stream);
            }

            return size;
        } else {
            return 0;
        }
    }

    @Override
    public int fromStreamUB(InputStream stream) throws IOException, XBProcessingException {
        return fromStreamUB(stream, false);
    }

    /**
     * Load this node from data stream.
     *
     * @param stream data stream
     * @param terminable specify if terminator can be read
     * @return size of read bytes
     * @throws java.io.IOException
     */
    public int fromStreamUB(InputStream stream, boolean terminable) throws IOException, XBProcessingException {
        UBNat32 attrSize = new UBNat32();
        int size = attrSize.fromStreamUB(stream);
        if (attrSize.getLong() == 0) {
            if (terminable) {
                return 1; // attrSize.getSizeUB();
            } else {
                throw new XBParseException("Unexpected terminator", XBProcessingExceptionType.UNEXPECTED_TERMINATOR);
            } // TODO
        } else {
            UBENat32 dataSize = new UBENat32();
            size += dataSize.fromStreamUB(stream);
            terminationMode = dataSize.isInfinity() ? XBBlockTerminationMode.ZERO_TERMINATED : XBBlockTerminationMode.SIZE_SPECIFIED;
            if (attrSize.getInt() == dataSize.getSizeUB()) {
                // Data Block
                dataMode = XBBlockDataMode.DATA_BLOCK;
                ByteArrayOutputStream dataStream = new ByteArrayOutputStream(dataSize.getInt());
                CopyStreamUtils.copyFixedSizeInputStreamToOutputStream(stream, dataStream, dataSize.getInt());
                size += dataSize.getInt();
                data = dataStream.toByteArray();
            } else if (attrSize.getInt() < dataSize.getSizeUB()) {
                throw new XBParseException("Attribute overreached");
            } else {
                // Node Block
                setData(null);
                attrSize.setValue(attrSize.getInt() - dataSize.getSizeUB());
                ArrayList<UBNatural> attribs = new ArrayList<>();
                int itemSize = 0;
                do {
                    UBNat32 attr = new UBNat32();
                    int childSize = attr.fromStreamUB(stream);
                    itemSize += childSize;
                    attribs.add(attr);
                    size += childSize;
                    if (itemSize > attrSize.getInt()) {
                        throw new XBParseException("Attribute overreached");
                    }
                } while (itemSize < attrSize.getInt());

                setAttributes(attribs);
                if (!dataSize.isInfinity() && dataSize.getInt() > 0) {
                    size += childrenFromStreamUB(stream, dataSize.getInt());
                }
            }
        }

        return size;
    }

    /**
     * Load children from data stream.
     *
     * @param stream
     * @param maxSize Maximum size of bytes to consume. Otherwise Exception.
     * Zero means terminationMode block.
     * @return size of bytes read
     * @throws java.io.IOException
     */
    public int childrenFromStreamUB(InputStream stream, int maxSize) throws IOException, XBProcessingException {
        if (getChildren() == null) {
            setChildren(new ArrayList<XBBlock>());
        }

        int childSize;
        int size = 0;
        do {
            XBTreeNode child = newNodeInstance(this);
            childSize = child.fromStreamUB(stream, maxSize == 0);
            getChildren().add(child);
            size += childSize;
            if (size > maxSize) {
                throw new XBParseException("Block overreached", XBProcessingExceptionType.BLOCK_OVERFLOW);
            }
        } while (((maxSize == 0) && (childSize != 1)) || ((maxSize > 0) && (size < maxSize)));

        return size;
    }

    @Override
    public int getSizeUB() {
        if (getDataMode() == XBBlockDataMode.DATA_BLOCK) {
            UBENat32 dataSize = new UBENat32(getDataSize());
            UBNat32 attrSize = new UBNat32(dataSize.getSizeUB());
            int size = attrSize.getSizeUB();
            size += dataSize.getSizeUB();
            size += getDataSize();
            return size;
        } else {
            int size = childrenSizeUB();
            UBENat32 childrenSize = new UBENat32();
            if (terminationMode == XBBlockTerminationMode.SIZE_SPECIFIED) {
                childrenSize.setValue(childrenSizeUB());
            } else {
                childrenSize.setInfinity();
            }

            UBNat32 attrSize = new UBNat32(childrenSize.getSizeUB() + attrSizeUB());
            size += attrSize.getSizeUB();
            size += childrenSize.getSizeUB();
            if (getAttributes() != null) {
                Iterator<UBNatural> iter = getAttributes().iterator();
                while (iter.hasNext()) {
                    size += iter.next().getSizeUB();
                }
            }

            if (terminationMode == XBBlockTerminationMode.ZERO_TERMINATED) {
                size++;
            } // size += (new UBNat32()).getSizeUB();

            return size;
        }
    }

    @Override
    public XBTreeNode getChildAt(int childIndex) {
        return (XBTreeNode) getChildren().get(childIndex);
    }

    @Override
    public int getChildCount() {
        return getChildren().size();
    }

    /**
     * Return index of child.
     *
     * @param node node to search match for
     * @return index of node if node found
     */
    @Override
    public int getIndex(TreeNode node) {
        return children.indexOf(node);
    }

    @Override
    public boolean getAllowsChildren() {
        return dataMode != XBBlockDataMode.NODE_BLOCK;
    }

    @Override
    public boolean isLeaf() {
        return dataMode == XBBlockDataMode.DATA_BLOCK || getChildCount() == 0;
    }

    @Override
    public Enumeration children() {
        return Collections.enumeration(getChildren());
    }

    void clear() {
        children.clear();
        attributes.clear();

        if (data != null) {
            data = null;
        }
    }

    public int childrenSizeUB() {
        if (getChildren() != null) {
            Iterator<XBBlock> iter = getChildren().iterator();
            int size = 0;
            while (iter.hasNext()) {
                size += ((XBTreeNode) iter.next()).getSizeUB();
            }

            return size;
        } else {
            return 0;
        }
    }

    public int attrSizeUB() {
        if (getAttributes() != null) {
            Iterator<UBNatural> iter = getAttributes().iterator();
            int size = 0;
            while (iter.hasNext()) {
                size += iter.next().getSizeUB();
            }

            return size;
        } else {
            return 0;
        }
    }

    @Override
    public String toString() {
        return "Item";
    }

    @Override
    public int getAttributesCount() {
        return attributes == null ? 0 : attributes.size();
    }

    @Override
    public UBNatural getAttribute(int index) {
        return attributes.get(index);
    }

    @Override
    public void setData(InputStream source) {
        if (source == null) {
            data = null;
        } else if (source instanceof ByteArrayInputStream) {
            // data.reset();
            data = new byte[((ByteArrayInputStream) source).available()];
            if (data.length > 0) {
                try {
                    source.read(data, 0, data.length);
                } catch (IOException ex) {
                    Logger.getLogger(XBTreeNode.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
            // data.reset();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            try {
                CopyStreamUtils.copyInputStreamToOutputStream(source, stream);
            } catch (IOException ex) {
                Logger.getLogger(XBTreeNode.class.getName()).log(Level.SEVERE, null, ex);
            }

            data = stream.toByteArray();
        }
    }

    @Override
    public InputStream getData() {
        if (data == null) {
            return null;
        }

        return new ByteArrayInputStream(data);
    }

    public byte[] getDataArray() {
        return data;
    }

    @Override
    public int getDataSize() {
        if (data == null) {
            return 0;
        }

        return data.length;
    }

    public XBTreeNode cloneNode(boolean recursive) {
        throw new UnsupportedOperationException();
//        XBTreeNode node = new XBTreeNode();
// TODO        node.data = (ArrayList<Byte>) data.clone();
// TODO        node.attributes = (ArrayList<UBNumber>) attributes.clone();
// TODO        if (recursive) node.children = (ArrayList<XBTreeNode>) children.clone();
//        return node;
    }

    public XBProvider convertToXBR(boolean recursive) {
        return new XBTreeWriter(this, recursive);
    }

    public XBListener convertFromXBR(boolean recursive) {
        return new XBTreeReader(this, recursive);
    }

    public XBProvider convertToXB() {
        return convertToXBR(true);
    }

    public XBListener convertFromXB() {
        return convertFromXBR(true);
    }

    @Override
    public void setAttributesCount(int count) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setChildCount(int count) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setChildAt(XBBlock block, int childIndex) {
        children.set(childIndex, block);
    }

    /**
     * TODO: This is stub, because I'm to lazy to think about proper solution
     *
     * @param parent
     * @return new
     */
    public XBTreeNode newNodeInstance(XBTreeNode parent) {
        return new XBTreeNode(parent);
    }

    @Override
    public long getBlockSize() {
        return getSizeUB();
    }

    @Override
    public void setAttribute(UBNatural attribute, int index) {
        attributes.set(index, attribute);
    }

    @Override
    public XBTreeNode getParent() {
        return parent;
    }

    @Override
    public void setParent(XBBlock parent) {
        this.parent = (XBTreeNode) parent;
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
    public List<UBNatural> getAttributes() {
        return attributes;
    }

    @Override
    public void setAttributes(List<UBNatural> attributes) {
        if (attributes == null) {
            throw new NullPointerException();
        }

        this.attributes = attributes;
    }

    @Override
    public List<XBBlock> getChildren() {
        return children;
    }

    @Override
    public void setChildren(List<XBBlock> children) {
        if (children == null) {
            throw new NullPointerException();
        }

        this.children = children;
    }
}
