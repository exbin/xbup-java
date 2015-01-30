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
import org.xbup.lib.core.catalog.XBCatalog;
import org.xbup.lib.core.parser.XBParseException;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.XBProcessingExceptionType;
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.block.XBFixedBlockType;
import org.xbup.lib.core.block.declaration.XBDeclBlockType;
import org.xbup.lib.core.parser.token.pull.convert.XBTProviderToPullProvider;
import org.xbup.lib.core.ubnumber.UBNatural;
import org.xbup.lib.core.ubnumber.UBStreamable;
import org.xbup.lib.core.ubnumber.type.UBENat32;
import org.xbup.lib.core.ubnumber.type.UBNat32;
import org.xbup.lib.core.util.CopyStreamUtils;

/**
 * Basic object model parser XBUP level 1 document block / tree node.
 *
 * @version 0.1.24 2015/01/30
 * @author XBUP Project (http://xbup.org)
 */
public class XBTTreeNode implements TreeNode, XBTEditableBlock, UBStreamable {

    private XBTTreeNode parent;

    private XBBlockTerminationMode terminationMode = XBBlockTerminationMode.SIZE_SPECIFIED;
    private XBBlockDataMode dataMode = XBBlockDataMode.NODE_BLOCK;

    private XBFixedBlockType blockType;
    private List<UBNatural> attributes;
    private List<XBTBlock> children;
    private byte[] data;

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
        if (children != null) {
            return children.indexOf(node);
        } else {
            return -1;
        }
    }

    /**
     * Gets count of all subnodes.
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
     * Gets block position in tree in depth-first scan.
     *
     * @return position index
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
        UBNat32 attrPartSize = new UBNat32();
        int size = attrPartSize.fromStreamUB(stream);
        if (attrPartSize.getLong() == 0) {
            if (terminable) {
                return 1; // attrSize.getSizeUB();
            } else {
                // TODO
                throw new XBParseException("Unexpected terminator", XBProcessingExceptionType.UNEXPECTED_TERMINATOR);
            }
        } else {
            UBENat32 dataPartSize = new UBENat32();
            size += dataPartSize.fromStreamUB(stream);
            terminationMode = dataPartSize.isInfinity() ? XBBlockTerminationMode.TERMINATED_BY_ZERO : XBBlockTerminationMode.SIZE_SPECIFIED;
            if (attrPartSize.getInt() == dataPartSize.getSizeUB()) {
                // Data Block
                dataMode = XBBlockDataMode.DATA_BLOCK;
                ByteArrayOutputStream dataStream = new ByteArrayOutputStream(dataPartSize.getInt());
                CopyStreamUtils.copyFixedSizeInputStreamToOutputStream(stream, dataStream, dataPartSize.getInt());
                size += dataPartSize.getInt();
                data = dataStream.toByteArray();
            } else if (attrPartSize.getInt() < dataPartSize.getSizeUB()) {
                throw new XBParseException("Attribute overreached", XBProcessingExceptionType.ATTRIBUTE_OVERFLOW);
            } else {
                // Node Block
                dataMode = XBBlockDataMode.NODE_BLOCK;
                attrPartSize.setValue(attrPartSize.getInt() - dataPartSize.getSizeUB());

                // Load block type
                int itemSize = 0;
                UBNat32 groupId = new UBNat32();
                int groupIdSize = groupId.fromStreamUB(stream);
                itemSize += groupIdSize;
                size += groupIdSize;
                if (itemSize > attrPartSize.getInt()) {
                    throw new XBParseException("Attribute overreached", XBProcessingExceptionType.ATTRIBUTE_OVERFLOW);
                }

                UBNat32 blockId = new UBNat32();
                if (itemSize < attrPartSize.getInt()) {
                    int blockIdSize = blockId.fromStreamUB(stream);
                    itemSize += blockIdSize;
                    size += blockIdSize;
                    if (itemSize > attrPartSize.getInt()) {
                        throw new XBParseException("Attribute overreached", XBProcessingExceptionType.ATTRIBUTE_OVERFLOW);
                    }

                    setFixedBlockType(new XBFixedBlockType(groupId, blockId));
                } else {
                    setFixedBlockType(new XBFixedBlockType(groupId, blockId));
                    singleAttributeType = true;
                }

                // Load attributes
                ArrayList<UBNatural> attribs = new ArrayList<>();
                if (attrPartSize.getInt() > itemSize) {
                    do {
                        UBNat32 attribute = new UBNat32();
                        int attributeSize = attribute.fromStreamUB(stream);
                        itemSize += attributeSize;
                        attribs.add(attribute);
                        size += attributeSize;
                        if (itemSize > attrPartSize.getInt()) {
                            throw new XBParseException("Attribute overreached", XBProcessingExceptionType.ATTRIBUTE_OVERFLOW);
                        }
                    } while (itemSize < attrPartSize.getInt());
                    attributes = attribs;
                }

                if (dataPartSize.isInfinity() || dataPartSize.getInt() > 0) {
                    size += childrenFromStreamUB(stream, dataPartSize.isInfinity() ? 0 : dataPartSize.getInt());
                }
            }
        }

        return size;
    }

    @Override
    public int toStreamUB(OutputStream stream) throws IOException {
        if (dataMode == XBBlockDataMode.DATA_BLOCK) {
            UBENat32 dataPartSize = new UBENat32(getDataSize());
            UBNat32 attrPartSize = new UBNat32(dataPartSize.getSizeUB());
            int size = attrPartSize.toStreamUB(stream);
            size += dataPartSize.toStreamUB(stream);
            if (data != null) {
                stream.write(data);
            }
            size += getDataSize();
            return size;
        } else {
            UBENat32 dataPartSize = new UBENat32();
            if (terminationMode == XBBlockTerminationMode.SIZE_SPECIFIED) {
                dataPartSize = new UBENat32(childrenSizeUB());
            } else {
                dataPartSize.setInfinity();
            }
            UBNat32 attrPartSize = new UBNat32(dataPartSize.getSizeUB() + attributeSizeUB() + typeSizeUB());
            int size = attrPartSize.toStreamUB(stream);
            size += dataPartSize.toStreamUB(stream);

            // Write block type
            size += blockType.getGroupID().toStreamUB(stream);
            if (!singleAttributeType) {
                size += blockType.getBlockID().toStreamUB(stream);
            }

            // Write attributes
            if (getAttributesCount() > 0) {
                Iterator<UBNatural> iter = attributes.iterator();
                while (iter.hasNext()) {
                    size += iter.next().toStreamUB(stream);
                }
            }
            if (getChildren() != null) {
                size += childrenToStreamUB(stream);
            }
            if (terminationMode == XBBlockTerminationMode.TERMINATED_BY_ZERO) {
                size += (new UBNat32()).toStreamUB(stream);
            }

            return size;
        }
    }

    public int childrenToStreamUB(OutputStream stream) throws IOException {
        if (children != null) {
            Iterator<XBTBlock> iter = children.iterator();
            int size = 0;
            while (iter.hasNext()) {
                size += ((XBTTreeNode) iter.next()).toStreamUB(stream);
            }
            return size;
        } else {
            return 0;
        }
    }

    @Override
    public int getSizeUB() {
        if (dataMode == XBBlockDataMode.DATA_BLOCK) {
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

            UBNat32 attrPartSize = new UBNat32(dataPartSize.getSizeUB() + attributeSizeUB() + typeSizeUB());
            size += attrPartSize.getSizeUB();
            size += attrPartSize.getInt();

            if (terminationMode == XBBlockTerminationMode.TERMINATED_BY_ZERO) {
                size++;
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

    public int attributeSizeUB() {
        if (getAttributesCount() > 0) {
            int size = 0;
            for (UBNatural attribute : attributes) {
                size += attribute.getSizeUB();
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

        blockDecl = null;
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
            node.data = data.clone();
        }

        for (UBNatural attribute : attributes) {
            node.addAttribute(new UBNat32(attribute));
        }

        if (children != null) {
            List<XBTBlock> cloneChildren = new ArrayList<>();
            for (XBTBlock block : children) {
                if (recursive) {
                    cloneChildren.add(((XBTTreeNode) block).cloneNode(true));
                } else {
                    cloneChildren.add(block);
                }
            }

            node.setChildren(cloneChildren);
        }

        return node;
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

    public void clearAttributes() {
        attributes = new ArrayList<>();
    }

    public void addAttribute(UBNatural attribute) {
        if (attributes.isEmpty() && singleAttributeType) {
            singleAttributeType = false;
        }

        attributes.add(attribute);
    }

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
                    data = new byte[0];
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

    public void setBlockType(XBBlockType blockType) {
        if (blockType instanceof XBFixedBlockType) {
            blockDecl = context != null ? context.getBlockDecl((XBFixedBlockType) blockType) : null;
            setFixedBlockType(((XBFixedBlockType) blockType));
        } else if (blockType instanceof XBDeclBlockType) {
            blockDecl = ((XBDeclBlockType) blockType).getBlockDecl();
            XBFixedBlockType fixedBlockType = context != null ? context.getFixedBlockType(blockDecl) : null;
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

        singleAttributeType = false;
        this.blockType = blockType;
    }

    @Override
    public UBNatural getAttribute(int attributeIndex) {
        return attributeIndex < attributes.size() ? attributes.get(attributeIndex) : null;
    }

    @Override
    public void setAttribute(UBNatural attribute, int attributeIndex) {
        singleAttributeType = false;
        while (attributes.size() <= attributeIndex) {
            attributes.add(new UBNat32());
        }

        attributes.set(attributeIndex, attribute);
    }

    public int getAttributeValue(int attributeIndex) {
        return attributeIndex < attributes.size() ? attributes.get(attributeIndex).getInt() : 0;
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
    public List<UBNatural> getAttributes() {
        return new ArrayList<>(attributes);
    }

    @Override
    public void setAttributes(List<UBNatural> attributes) {
        if (attributes == null) {
            throw new NullPointerException();
        }

        this.attributes.clear();
        this.attributes.addAll(attributes);
        singleAttributeType = false;
    }

    @Override
    public XBTTreeNode getChildAt(int childIndex) {
        return (XBTTreeNode) getChildren().get(childIndex);
    }

    @Override
    public void setChildAt(XBTBlock block, int childIndex) {
        while (attributes.size() <= childIndex) {
            children.add(new XBTTreeNode(this));
        }

        children.set(childIndex, block);
    }

    @Override
    public int getChildCount() {
        if (getChildren() != null) {
            return getChildren().size();
        } else {
            return 0;
        }
    }

    @Override
    public void setChildCount(int count) {
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
    public List<XBTBlock> getChildren() {
        return children;
    }

    @Override
    public void setChildren(List<XBTBlock> children) {
        if (children == null) {
            throw new NullPointerException();
        }

        this.children.clear();
        this.children.addAll(children);
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
        return Collections.enumeration(getChildren());
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
                blockDecl = context.getBlockDecl(getFixedBlockType());
            } else {
                blockDecl = null;
            }

            if (getFixedBlockType().getAsBasicType() == XBBasicBlockType.DECLARATION && catalog != null) {
                // Process declaration block
                XBContext childContext = catalog.processDeclaration(parentContext, new XBTProviderToPullProvider(new XBTTreeWriter(new XBTBlockFixedSource(this))));
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
        } else {
            blockDecl = null;
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
            childSize = child.fromStreamUB(stream, maxSize == 0);
            size += childSize;
            if (childSize > 1) {
                getChildren().add(child);
                if (maxSize > 0 && size > maxSize) {
                    throw new XBParseException("Block overreached", XBProcessingExceptionType.BLOCK_OVERFLOW);
                }
            }
        } while (((maxSize == 0) && (childSize != 1)) || ((maxSize > 0) && (size < maxSize)));

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
        public List<UBNatural> getAttributes() {
            return block.getAttributes();
        }

        @Override
        public UBNatural getAttribute(int attributeIndex) {
            return block.getAttribute(attributeIndex);
        }

        @Override
        public int getAttributesCount() {
            return block.getAttributesCount();
        }

        @Override
        public List<XBTBlock> getChildren() {
            List<XBTBlock> result = new ArrayList<>();
            for (XBTBlock child : block.getChildren()) {
                result.add(new XBTBlockFixedSource((XBTTreeNode) child));
            }

            return result;
        }

        @Override
        public XBTBlock getChildAt(int childIndex) {
            return new XBTBlockFixedSource(block.getChildAt(childIndex));
        }

        @Override
        public int getChildCount() {
            return block.getChildCount();
        }

        @Override
        public InputStream getData() {
            return block.getData();
        }

        @Override
        public int getDataSize() {
            return block.getDataSize();
        }

        @Override
        public int getBlockIndex() {
            return block.getBlockIndex();
        }

        @Override
        public long getBlockSize() {
            return block.getBlockSize();
        }
    }
}
