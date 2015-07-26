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
 * Basic plain implementation of XBBlock interface.
 *
 * @version 0.1.25 2015/07/26
 * @author XBUP Project (http://xbup.org)
 */
public class XBDefaultBlock implements XBBlock {

    private XBBlock parent;
    private final XBBlockDataMode dataMode;
    private final XBBlockTerminationMode terminationMode;
    private final XBAttribute[] attributes;
    private final XBBlock[] children;
    private final XBBlockData data;

    public XBDefaultBlock(XBBlockTerminationMode terminationMode, XBAttribute[] attributes, XBBlock[] children) {
        this(null, terminationMode, attributes, children);
    }

    public XBDefaultBlock(XBBlock parent, XBBlockTerminationMode terminationMode, XBAttribute[] attributes, XBBlock[] children) {
        dataMode = XBBlockDataMode.NODE_BLOCK;
        this.terminationMode = terminationMode;
        this.attributes = attributes;
        this.children = children;
        if (children != null) {
            for (XBBlock child : children) {
                if (child instanceof XBDefaultBlock) {
                    ((XBDefaultBlock) child).setParent(this);
                } else if (child instanceof XBEditableBlock) {
                    ((XBEditableBlock) child).setParent(this);
                }
            }
        }
        this.parent = parent;
        data = null;
    }

    public XBDefaultBlock(XBBlockTerminationMode terminationMode, XBBlockData data) {
        this(null, terminationMode, data);
    }

    public XBDefaultBlock(XBBlock parent, XBBlockTerminationMode terminationMode, XBBlockData data) {
        this.dataMode = XBBlockDataMode.DATA_BLOCK;
        this.terminationMode = terminationMode;
        this.attributes = null;
        this.children = null;
        this.parent = parent;
        this.data = data;
    }

    @Override
    public XBBlock getParent() {
        return parent;
    }

    public void setParent(XBBlock parent) {
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
    public XBBlock[] getChildren() {
        return children;
    }

    @Override
    public XBBlock getChildAt(int childIndex) {
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
