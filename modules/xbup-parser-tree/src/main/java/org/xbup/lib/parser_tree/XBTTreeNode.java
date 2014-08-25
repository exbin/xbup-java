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
import org.xbup.lib.core.block.XBBasicBlockType;
import org.xbup.lib.core.block.XBBlockDataMode;
import org.xbup.lib.core.block.XBBlockType;
import org.xbup.lib.core.block.XBTBlock;
import org.xbup.lib.core.block.XBTEditableBlock;
import org.xbup.lib.core.block.declaration.XBBlockDecl;
import org.xbup.lib.core.block.declaration.XBContext;
import org.xbup.lib.core.block.declaration.XBContextBlockType;
import org.xbup.lib.core.block.declaration.XBDBlockDecl;
import org.xbup.lib.core.block.declaration.XBDeclBlockType;
import org.xbup.lib.core.catalog.XBACatalog;
import org.xbup.lib.core.catalog.XBCatalog;
import org.xbup.lib.core.parser.XBParseException;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.XBProcessingExceptionType;
import org.xbup.lib.core.parser.basic.XBTListener;
import org.xbup.lib.core.parser.basic.XBTProvider;
import org.xbup.lib.core.parser.param.XBParamPosition;
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.serial.XBSerializable;
import org.xbup.lib.core.ubnumber.UBNatural;
import org.xbup.lib.core.ubnumber.UBStreamable;
import org.xbup.lib.core.ubnumber.type.UBENat32;
import org.xbup.lib.core.ubnumber.type.UBNat32;
import org.xbup.lib.core.util.CopyStreamUtils;

/**
 * Basic object model parser XBUP level 1 document block / tree node.
 *
 * @version 0.1.23 2014/02/20
 * @author XBUP Project (http://xbup.org)
 */
public class XBTTreeNode implements TreeNode, XBTEditableBlock, UBStreamable {

    private XBTTreeNode parent;

    private XBBlockTerminationMode terminationMode;
    private XBBlockDataMode dataMode;

    private XBBlockType blockType;
    private int blockTypeLength;

    private List<UBNatural> attributes;
    private List<XBTBlock> children;
    private byte[] data;

    /**
     * Creates a new instance of XBTTreeNode.
     */
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
        terminationMode = XBBlockTerminationMode.SIZE_SPECIFIED;
        dataMode = XBBlockDataMode.NODE_BLOCK;

        children = new ArrayList<>();
        attributes = new ArrayList<>();
        data = null;

        blockType = null;
        if (parent != null) {
            if (parent.getBlockType() instanceof XBContextBlockType) {
                blockType = new XBContextBlockType(0, 0, ((XBContextBlockType) parent.getBlockType()).getContext());
            }
        }

        blockTypeLength = 0;
    }

    /**
     * Performs tree depth specification processing.
     *
     * @param catalog instance of catalog
     * @param parentContext parent context
     */
    public void processSpec(XBCatalog catalog, XBContext parentContext) {
        if (!(blockType instanceof XBContextBlockType)) {
            return;
        }

        ((XBContextBlockType) blockType).setContext(parentContext);
        if (dataMode == XBBlockDataMode.NODE_BLOCK) {
            if (getAttributesCount() > 1) {
                if ((getAttributeValue(0) == 0) && (getAttributeValue(1) == XBBasicBlockType.DECLARATION.ordinal())) {
                    // Process specification block
                    XBContext childContext = XBContext.processDeclaration(catalog, parentContext, this);
                    long blockPos = 0;
                    if (getChildren() != null) {
                        for (Iterator it = getChildren().iterator(); it.hasNext();) {
                            blockPos++;
                            XBTTreeNode item = ((XBTTreeNode) it.next());
                            item.processSpec(catalog, blockPos == 2 ? childContext : parentContext);
                        }
                    }
                    return;
                }
            }
        }

        if (getChildren() != null) {
            for (Iterator it = getChildren().iterator(); it.hasNext();) {
                XBTTreeNode item = ((XBTTreeNode) it.next());
                item.processSpec(catalog, parentContext);
            }
        }
    }

    public XBTTreeNode followPointer(int index) {
        if (getAttributesCount() >= index) {
            int childIndex = ((UBNat32) getAttribute(index)).getInt();
            if ((childIndex > 0) && (getChildren().size() >= childIndex)) {
                return (XBTTreeNode) getChildAt(childIndex - 1);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public int getAttributeValue(int index) {
        if (getAttributesCount() > index) {
            if (index == 0) {
                return getBlockType().getGroupID().getInt();
            }
            if (index == 1) {
                return getBlockType().getBlockID().getInt();
            }
            return ((UBNat32) getAttributes().get(index - 2)).getInt();
        } else {
            return 0;
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
    public int childrenFromStreamUB(InputStream stream, int maxSize) throws IOException, XBProcessingException {
        if (getChildren() == null) {
            setChildren(new ArrayList<XBTBlock>());
        }
        int childSize;
        int size = 0;
        do {
            XBTTreeNode child = newNodeInstance(this);
            childSize = child.fromStreamUB(stream);
            getChildren().add(child);
            size += childSize;
            if (size > maxSize) {
                throw new XBParseException("Block overreached", XBProcessingExceptionType.BLOCK_OVERFLOW);
            }
        } while (((maxSize == 0) && (childSize != 1)) || ((maxSize > 0) && (size < maxSize)));
        return size;
    }

    @Override
    public XBTTreeNode getChildAt(int childIndex) {
        return (XBTTreeNode) getChildren().get(childIndex);
    }

    @Override
    public int getChildCount() {
        if (getChildren() != null) {
            return getChildren().size();
        } else {
            return 0;
        }
    }

    /**
     * Return index of child.
     *
     * @param node node to search match for
     * @return index of node if node found
     */
    @Override
    public int getIndex(TreeNode node) {
        if (children != null) {
            return children.indexOf(node);
        } else {
            return -1;
        }
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
                for (Iterator<XBTBlock> it = children.iterator(); it.hasNext();) {
                    result += ((XBTTreeNode) it.next()).getSubNodesCount();
                }
            }
            return result;
        }
        return 0;
    }

    /**
     * Get index in depth-first order
     */
    @Override
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
     * Find node of given index (recursive, no cache).
     *
     * @param index depth-first order index
     * @return node if found
     */
    public XBTTreeNode findNodeByIndex(long index) {
        if (index == 0) {
            return this;
        }
        index--;
        if (getChildCount() > 0) {
            List<Iterator<XBTBlock>> iterators = new ArrayList<>();
            iterators.add(children.iterator());
            int level = 0;
            XBTTreeNode node = (XBTTreeNode) iterators.get(level).next();
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
                node = (XBTTreeNode) iterators.get(level).next();
                index--;
            }
        }
        return null;
    }

    @Override
    public int toStreamUB(OutputStream stream) throws IOException {
        if (dataMode == XBBlockDataMode.DATA_BLOCK) {
            UBENat32 dataSize = new UBENat32(getDataSize());
            UBNat32 attrSize = new UBNat32(dataSize.getSizeUB());
            int size = attrSize.toStreamUB(stream);
            size += dataSize.toStreamUB(stream);
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
            UBNat32 attrSize = new UBNat32(childrenSize.getSizeUB() + attrSizeUB() + typeSizeUB());
            int size = attrSize.toStreamUB(stream);
            size += childrenSize.toStreamUB(stream);
            if (blockTypeLength > 0) {
                blockType.getGroupID().toStreamUB(stream);
            }
            if (blockTypeLength > 1) {
                blockType.getBlockID().toStreamUB(stream);
            }
            if (getAttributesCount() > 2) {
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
                throw new XBParseException("Unexpected terminator"); // TODO
            }
        } else {
            UBENat32 dataSize = new UBENat32();
            size += dataSize.fromStreamUB(stream);
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
                if (attribs.size() > 1) {
                    setBlockType(new XBContextBlockType(attribs.get(0).getInt(), attribs.get(1).getInt()));
                    attribs.remove(1);
                    attribs.remove(0);
                } else if (attribs.size() == 1) {
                    setBlockType(new XBContextBlockType(attribs.get(0).getInt(), 0));
                    attribs.remove(0);
                }
                setAttributes(attribs);
                if (dataSize.getInt() > 0) {
                    size += childrenFromStreamUB(stream, dataSize.getInt());
                }
            }
        }
        return size;
    }

    public int childrenToStreamUB(OutputStream stream) throws IOException {
        if (getChildren() != null) {
            Iterator<XBTBlock> iter = getChildren().iterator();
            int size = 0;
            while (iter.hasNext()) {
                size += ((XBTTreeNode) iter.next()).toStreamUB(stream);
            }
            return size;
        } else {
            return 0;
        }
    }

    void clear() {
        if (children != null) {
            children.clear();
        }
        if (data != null) {
            data = null;
        }
        if (attributes != null) {
            attributes.clear();
        }
    }

    @Override
    public int getSizeUB() {
        if (dataMode == XBBlockDataMode.DATA_BLOCK) {
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
            UBNat32 attrSize = new UBNat32(childrenSize.getSizeUB() + attrSizeUB() + typeSizeUB());
            size += attrSize.getSizeUB();
            size += childrenSize.getSizeUB();
            if (blockTypeLength > 0) {
                size += blockType.getGroupID().getSizeUB();
            }
            if (blockTypeLength > 1) {
                size += blockType.getBlockID().getSizeUB();
            }
            if (getAttributesCount() > 2) {
                Iterator<UBNatural> iter = getAttributes().iterator();
                while (iter.hasNext()) {
                    size += iter.next().getSizeUB();
                }
            }
            if (terminationMode == XBBlockTerminationMode.ZERO_TERMINATED) {
                size++; // size += (new UBNat32()).getSizeUB();
            }
            return size;
        }
    }

    public int childrenSizeUB() {
        if (getChildren() != null) {
            Iterator<XBTBlock> iter = getChildren().iterator();
            int size = 0;
            while (iter.hasNext()) {
                size += ((XBTTreeNode) iter.next()).getSizeUB();
            }
            return size;
        } else {
            return 0;
        }
    }

    public int attrSizeUB() {
        if (getAttributesCount() > 2) {
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
        if (attributes.size() > 0) {
            return attributes.size() + 2;
        }
        return blockTypeLength;
    }

    @Override
    public UBNatural getAttribute(int index) {
        if (index == 0) {
            return getBlockType().getGroupID();
        }
        if (index == 1) {
            return getBlockType().getBlockID();
        }
        return attributes.get(index - 2);
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
                    Logger.getLogger(XBTTreeNode.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
            // data.reset();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            try {
                CopyStreamUtils.copyInputStreamToOutputStream(source, stream);
            } catch (IOException ex) {
                Logger.getLogger(XBTTreeNode.class.getName()).log(Level.SEVERE, null, ex);
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

    /**
     * Create deep copy of this node.
     *
     * @return tree node
     */
    public XBTTreeNode cloneNode() {
        return cloneNode(true);
    }

    /**
     * Create copy of this node
     *
     * @param recursive if true, perform deep copy or else shallow copy.
     * @return tree node
     */
    public XBTTreeNode cloneNode(boolean recursive) {
        XBTTreeNode node = new XBTTreeNode();
        node.setBlockType(blockType);
        if (data != null) {
            node.data = data.clone();
        }

        for (UBNatural attribute : attributes) {
            node.addAttribute(new UBNat32(attribute));
        }

        if (children != null) {
            children = new ArrayList<>();
            for (int i = 0; i < children.size(); i++) {
                XBTBlock block = children.get(i);
                if (recursive) {
                    children.add(((XBTTreeNode) block).cloneNode(true));
                } else {
                    children.add(block);
                }
            }
        }
// TODO        node.attributes = (ArrayList<UBNumber>) attributes.clone();
// TODO        if (recursive) node.children = (ArrayList<XBTTreeNode>) children.clone();
        return node;
    }

    public XBTProvider convertToXBTR(boolean recursive) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public XBTListener convertFromXBTR(boolean recursive) {
        return new XBTTreeReader(this, recursive);
    }

    public XBTProvider convertToXBT() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public XBTListener convertFromXBT() {
        return convertFromXBTR(true);
    }

    @Override
    public void setAttributesCount(int count) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setAttribute(UBNatural attribute, int index) {
        if (index == 0) {
            blockType = new XBContextBlockType(attribute.getInt(), getBlockType().getBlockID().getInt(), ((XBContextBlockType) getBlockType()).getContext());
        } else if (index == 1) {
            blockType = new XBContextBlockType(getBlockType().getGroupID().getInt(), attribute.getInt(), ((XBContextBlockType) getBlockType()).getContext());
        } else {
            attributes.set(index - 2, attribute);
        }
    }

    @Override
    public void setChildCount(int count) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setChildAt(XBTBlock block, int childIndex) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * TODO: This is stub, because I'm to lazy to think about proper solution
     *
     * @param parent
     * @return new
     */
    public XBTTreeNode newNodeInstance(XBTTreeNode parent) {
        return new XBTTreeNode(parent);
    }

    @Override
    public long getBlockSize() {
        return getSizeUB();
    }

    /**
     * @param blockType the blockType to set
     */
    public void setBlockType(XBBlockType blockType) {
        this.blockType = blockType;
        blockTypeLength = 2;
    }

    public void clearAttributes() {
        attributes = new ArrayList<>();
        blockTypeLength = 0;
    }

    public void addAttribute(UBNatural attribute) {
        if ((attributes.size() > 0) || (blockTypeLength == 2)) {
            attributes.add(attribute);
        } else {
            setAttribute(attribute, blockTypeLength);
            blockTypeLength++;
        }
    }

    public void removeAttribute(int index) {
        if (index > 1) {
            attributes.remove(index - 2);
        } else {
            if (blockTypeLength <= index) {
                throw new NullPointerException();
            }
            if (attributes.size() > 0) {
                if (index == 0) {
                    blockType = new XBContextBlockType(getBlockType().getBlockID().getInt(), attributes.get(0).getInt(), ((XBContextBlockType) getBlockType()).getContext());
                } else {
                    blockType = new XBContextBlockType(getBlockType().getGroupID().getInt(), attributes.get(0).getInt(), ((XBContextBlockType) getBlockType()).getContext());
                }
                attributes.remove(0);
            } else {
                if ((index == 0) && (blockTypeLength > 1)) {
                    blockType = new XBContextBlockType(getBlockType().getBlockID().getInt(), 0, ((XBContextBlockType) getBlockType()).getContext());
                } else if (index == 1) {
                    blockType = new XBContextBlockType(getBlockType().getGroupID().getInt(), 0, ((XBContextBlockType) getBlockType()).getContext());
                } else {
                    blockType = new XBContextBlockType(0, 0, ((XBContextBlockType) getBlockType()).getContext());
                }
                blockTypeLength--;
            }
        }
    }

    private int typeSizeUB() {
        if (blockTypeLength > 1) {
            return blockType.getGroupID().getSizeUB() + blockType.getBlockID().getSizeUB();
        }

        if (blockTypeLength == 1) {
            return blockType.getGroupID().getSizeUB();
        }

        return 0;
    }

    /**
     * Get specific parameter convertor from current block.
     *
     * @param index index of requested parameter
     * @return requested parameter
     */
    public XBSerializable getParameter(int index) {
//        XBDeclaration decl = context.getDeclaration(); // Or from catalog?
//        XBDeclaration paramDecl = context.getParamDecl(blockType, index);
        if (blockType instanceof XBDeclBlockType) {
            XBBlockDecl blockDecl = ((XBDeclBlockType) blockType).getBlockDecl();
            if (blockDecl instanceof XBDBlockDecl) {
                XBParamPosition paramPos = ((XBDBlockDecl) blockDecl).getParamPosition(new XBSerializable() {
                    // TODO
                }, index);
                return new XBSerializable() {
                    // TODO
                };
            }
        }
        return null;
    }

    public XBTBlock getParam(XBACatalog catalog, long paramId) {
        XBTreeParamExtractor extractor = new XBTreeParamExtractor(this, catalog, paramId);
        return extractor.getOutput();
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

        this.dataMode = dataMode;
    }

    @Override
    public List<XBTBlock> getChildren() {
        return children;
    }

    @Override
    public void setChildren(List<XBTBlock> children) {
        if (children == null) {
            throw new NullPointerException();
        }

        this.children = children;
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

    @Override
    public XBBlockType getBlockType() {
        return blockType;
    }
}
