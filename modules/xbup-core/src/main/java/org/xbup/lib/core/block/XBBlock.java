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
import java.util.List;
import org.xbup.lib.core.ubnumber.UBNatural;

/**
 * Interface for read access to XBUP level 0 block.
 *
 * @version 0.1.24 2014/10/02
 * @author XBUP Project (http://xbup.org)
 */
public interface XBBlock {

    /**
     * Gets parent block or null if block has no parent.
     *
     * @return Parent block.
     */
    public XBBlock getParent();

    /**
     * Returns whether this block is data block.
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
     * Gets List of attributes in order of appearance.
     *
     * @return list of attributes
     */
    public List<UBNatural> getAttributes();

    /**
     * Gets attribute of given index / order.
     *
     * @param attributeIndex index/order of attribute
     * @return attribute value
     */
    public UBNatural getAttribute(int attributeIndex);

    /**
     * Gets count of attributes.
     *
     * @return count of attributes
     */
    public int getAttributesCount();

    /**
     * Gets list of all children blocks.
     *
     * @return list of child blocks
     */
    public List<XBBlock> getChildren();

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
    public int getChildCount();

    /**
     * Gets block data.
     *
     * @return block data or null
     */
    public InputStream getData();

    /**
     * Gets data size.
     *
     * @return data size in bytes
     */
    public int getDataSize();

    /**
     * Gets block size.
     *
     * @return block size in bytes
     */
    public long getBlockSize();
}