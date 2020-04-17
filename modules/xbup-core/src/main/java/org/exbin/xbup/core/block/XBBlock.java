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
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.exbin.auxiliary.paged_data.BinaryData;
import org.exbin.xbup.core.parser.token.XBAttribute;

/**
 * Interface for read access to XBUP level 0 block.
 *
 * @version 0.2.1 2020/04/17
 * @author ExBin Project (http://exbin.org)
 */
public interface XBBlock {

    /**
     * Gets parent block or empty if block has no parent.
     *
     * @return Parent block.
     */
    @Nonnull
    Optional<XBBlock> getParentBlock();

    /**
     * Returns mode whether this block is data block.
     *
     * @return data mode
     */
    @Nonnull
    XBBlockDataMode getDataMode();

    /**
     * Returns whether this block is using sequence of children ended with
     * termination block.
     *
     * @return termination mode
     */
    @Nonnull
    XBBlockTerminationMode getTerminationMode();

    /**
     * Gets array of attributes in order of appearance.
     *
     * @return array of attributes
     */
    @Nullable
    XBAttribute[] getAttributes();

    /**
     * Gets attribute of given index / order.
     *
     * @param attributeIndex index/order of attribute
     * @return attribute value
     */
    @Nullable
    XBAttribute getAttributeAt(int attributeIndex);

    /**
     * Gets count of attributes.
     *
     * @return count of attributes
     */
    int getAttributesCount();

    /**
     * Gets array of all children blocks.
     *
     * @return list of child blocks
     */
    @Nullable
    XBBlock[] getChildren();

    /**
     * Gets child block of given index.
     *
     * @param childIndex index of child block
     * @return child block
     */
    @Nullable
    XBBlock getChildAt(int childIndex);

    /**
     * Gets count of children.
     *
     * @return count of children
     */
    int getChildrenCount();

    /**
     * Gets block data.
     *
     * @return block data or throws invalid operation on non-data block
     */
    @Nonnull
    InputStream getData();

    /**
     * Gets block data.
     *
     * @return block data or throws invalid operation on non-data block
     */
    @Nonnull
    BinaryData getBlockData();
}
