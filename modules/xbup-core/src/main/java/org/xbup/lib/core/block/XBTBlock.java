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
 * XBUP level 1 block interface.
 *
 * @version 0.1.24 2014/06/08
 * @author XBUP Project (http://xbup.org)
 */
public interface XBTBlock {

    /**
     * Get parent block or null if block has no parent.
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
     * Get block type.
     *
     * @return block type
     */
    public XBBlockType getBlockType();

    /**
     * Get list of attributes in order of appearance.
     *
     * @return list of attributes
     */
    public List<UBNatural> getAttributes();

    /**
     * Gets attribute of given index. Returns zero if attribute index greater
     * than attributes count.
     *
     * @param index index of attribute
     * @return attribute value
     */
    public UBNatural getAttribute(int index);

    /**
     * Get count of attributes.
     *
     * @return count of attributes
     */
    public int getAttributesCount();

    /**
     * Get list of all children.
     *
     * @return list of child blocks
     */
    public List<XBTBlock> getChildren();

    /**
     * Get children of given index.
     *
     * @param childIndex index of child block
     * @return child block
     */
    public XBTBlock getChildAt(int childIndex);

    /**
     * Get count of children blocks.
     *
     * @return count of children
     */
    public int getChildCount();

    /**
     * Get block data.
     *
     * @return data stream
     */
    public InputStream getData();

    /**
     * Get data size.
     *
     * @return data size in bytes
     */
    public int getDataSize();

    /**
     * Get block position in tree in depth-first scan.
     *
     * @return position index
     */
    public int getBlockIndex();

    /**
     * Get block size.
     *
     * @return block size in bytes.
     */
    public long getBlockSize();
}
