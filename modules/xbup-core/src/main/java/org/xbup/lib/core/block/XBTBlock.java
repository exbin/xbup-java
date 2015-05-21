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
 * Interface for read access to XBUP level 1 block.
 *
 * @version 0.1.25 2015/05/21
 * @author XBUP Project (http://xbup.org)
 */
public interface XBTBlock {

    /**
     * Gets parent block or null if block has no parent.
     *
     * @return parent block
     */
    public XBTBlock getParent();

    /**
     * Returns whether this block is using sequence of children ended with
     * termination block.
     *
     * @return terminated mode
     */
    public XBBlockTerminationMode getTerminationMode();

    /**
     * Returns whether this block is data block.
     *
     * @return data mode
     */
    public XBBlockDataMode getDataMode();

    /**
     * Gets block type.
     *
     * @return block type
     */
    public XBBlockType getBlockType();

    /**
     * Gets array of attributes in order of appearance.
     *
     * @return array of attributes
     */
    public XBAttribute[] getAttributes();

    /**
     * Gets attribute of given index.
     *
     * Returns zero if attribute index greater than attributes count.
     *
     * @param attributeIndex index of attribute
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
     * Gets array of all children.
     *
     * @return array of child blocks
     */
    public XBTBlock[] getChildren();

    /**
     * Gets children of given index.
     *
     * @param childIndex index of child block
     * @return child block
     */
    public XBTBlock getChildAt(int childIndex);

    /**
     * Gets count of children blocks.
     *
     * @return count of children
     */
    public int getChildrenCount();

    /**
     * Gets block data.
     *
     * @return data stream
     */
    public InputStream getData();

    /**
     * Gets block data.
     *
     * @return block data or null
     */
    public XBBlockData getBlockData();

    /**
     * Gets data size.
     *
     * @return data size in bytes
     */
    public long getDataSize();

    /**
     * Gets block position in tree in depth-first scan.
     *
     * @return position index
     */
    public int getBlockIndex();

    /**
     * Gets block size.
     *
     * @return block size in bytes.
     */
    public long getBlockSize();
}
