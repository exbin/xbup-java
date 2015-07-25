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
package org.xbup.lib.core.block;

import java.io.InputStream;
import org.xbup.lib.core.parser.token.XBAttribute;

/**
 * Basic plain implementation of XBTBlock interface.
 *
 * @version 0.1.25 2015/07/24
 * @author XBUP Project (http://xbup.org)
 */
public class XBTDefaultBlock implements XBTBlock {

    private XBTBlock parent;
    private final XBBlockDataMode dataMode;
    private final XBBlockTerminationMode terminationMode;
    private final XBBlockType blockType;
    private final XBAttribute[] attributes;
    private final XBTBlock[] children;
    private final XBBlockData data;

    public XBTDefaultBlock(XBBlockTerminationMode terminationMode, XBBlockType blockType, XBAttribute[] attributes, XBTBlock[] children) {
        this(null, terminationMode, blockType, attributes, children);
    }

    public XBTDefaultBlock(XBTBlock parent, XBBlockTerminationMode terminationMode, XBBlockType blockType, XBAttribute[] attributes, XBTBlock[] children) {
        dataMode = XBBlockDataMode.NODE_BLOCK;
        this.terminationMode = terminationMode;
        this.blockType = blockType;
        this.attributes = attributes;
        this.children = children;
        if (children != null) {
            for (XBTBlock child : children) {
                if (child instanceof XBTDefaultBlock) {
                    ((XBTDefaultBlock) child).setParent(parent);
                } else if (child instanceof XBTEditableBlock) {
                    ((XBTEditableBlock) child).setParent(parent);
                }
            }
        }
        this.parent = parent;
        data = null;
    }

    public XBTDefaultBlock(XBBlockTerminationMode terminationMode, XBBlockData data) {
        this(null, terminationMode, data);
    }

    public XBTDefaultBlock(XBTBlock parent, XBBlockTerminationMode terminationMode, XBBlockData data) {
        this.dataMode = XBBlockDataMode.DATA_BLOCK;
        this.terminationMode = terminationMode;
        this.blockType = null;
        this.attributes = null;
        this.children = null;
        this.parent = parent;
        this.data = data;
    }

    @Override
    public XBTBlock getParent() {
        return parent;
    }

    public void setParent(XBTBlock parent) {
        this.parent = parent;
    }

    @Override
    public XBBlockDataMode getDataMode() {
        return dataMode;
    }

    @Override
    public XBBlockTerminationMode getTerminationMode() {
        return terminationMode;
    }

    @Override
    public XBBlockType getBlockType() {
        return blockType;
    }

    @Override
    public XBAttribute[] getAttributes() {
        return attributes;
    }

    @Override
    public XBAttribute getAttributeAt(int attributeIndex) {
        return attributes[attributeIndex];
    }

    @Override
    public int getAttributesCount() {
        return attributes.length;
    }

    @Override
    public XBTBlock[] getChildren() {
        return children;
    }

    @Override
    public XBTBlock getChildAt(int childIndex) {
        return children[childIndex];
    }

    @Override
    public int getChildrenCount() {
        return children.length;
    }

    @Override
    public InputStream getData() {
        return data.getDataInputStream();
    }

    @Override
    public XBBlockData getBlockData() {
        return data;
    }

    @Override
    public long getDataSize() {
        return data == null ? 0 : data.getDataSize();
    }

    @Override
    public long getBlockSize() {
        return -1;
    }

    @Override
    public int getBlockIndex() {
        return -1;
    }
}
