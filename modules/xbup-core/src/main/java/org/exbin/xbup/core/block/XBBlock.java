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
package org.exbin.xbup.core.block;

import java.io.InputStream;
import org.exbin.xbup.core.parser.token.XBAttribute;

/**
 * Interface for read access to XBUP level 0 block.
 *
 * @version 0.2.0 2015/09/19
 * @author ExBin Project (http://exbin.org)
 */
public interface XBBlock {

    /**
     * Gets parent block or null if block has no parent.
     *
     * @return Parent block.
     */
    public XBBlock getParent();

    /**
     * Returns mode whether this block is data block.
     *
     * @return data mode
     */
    public XBBlockDataMode getDataMode();

    /**
     * Returns whether this block is using sequence of children ended with
     * termination block.
     *
     * @return termination mode
     */
    public XBBlockTerminationMode getTerminationMode();

    /**
     * Gets array of attributes in order of appearance.
     *
     * @return array of attributes
     */
    public XBAttribute[] getAttributes();

    /**
     * Gets attribute of given index / order.
     *
     * @param attributeIndex index/order of attribute
     * @return attribute value
     */
    public XBAttribute getAttributeAt(int attributeIndex);

    /**
     * Gets count of attributes.
     *
     * @return count of attributes
     */
    public int getAttributesCount();

    /**
     * Gets array of all children blocks.
     *
     * @return list of child blocks
     */
    public XBBlock[] getChildren();

    /**
     * Gets child block of given index.
     *
     * @param childIndex index of child block
     * @return child block
     */
    public XBBlock getChildAt(int childIndex);

    /**
     * Gets count of children.
     *
     * @return count of children
     */
    public int getChildrenCount();

    /**
     * Gets block data.
     *
     * @return block data or null
     */
    public InputStream getData();

    /**
     * Gets block data.
     *
     * @return block data or null
     */
    public XBBlockData getBlockData();
}
