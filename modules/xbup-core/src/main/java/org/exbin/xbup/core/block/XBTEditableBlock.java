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

import java.io.IOException;
import java.io.InputStream;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.auxiliary.paged_data.BinaryData;
import org.exbin.xbup.core.parser.token.XBAttribute;

/**
 * Interface for editable XBUP level 1 block.
 *
 * @version 0.2.1 2017/05/13
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
public interface XBTEditableBlock extends XBTBlock {

    /**
     * Sets parent block.
     *
     * @param parent block
     */
    void setParent(@Nullable XBTBlock parent);

    /**
     * Sets terminated mode.
     *
     * @param terminationMode terminated mode flag
     */
    void setTerminationMode(XBBlockTerminationMode terminationMode);

    /**
     * Sets data mode.
     *
     * @param dataMode data mode
     */
    void setDataMode(XBBlockDataMode dataMode);

    /**
     * Sets array of attributes in order of appearance.
     *
     * @param attributes array of attributes
     */
    void setAttributes(@Nullable XBAttribute[] attributes);

    /**
     * Sets attribute of given index.
     *
     * If index is greater than current count of attributes, new zero attributes
     * will be filled.
     *
     * @param attribute attribute value
     * @param attributeIndex attribute index
     */
    void setAttributeAt(XBAttribute attribute, int attributeIndex);

    /**
     * Sets count of attributes.
     *
     * List of attributes will be trimmed/extended with zero values.
     *
     * @param count count of attributes
     */
    void setAttributesCount(int count);

    /**
     * Removes attribute on given position.
     *
     * @param attributeIndex attribute index
     */
    void removeAttribute(int attributeIndex);

    /**
     * Sets block type.
     *
     * @param blockType block type
     */
    void setBlockType(XBBlockType blockType);

    /**
     * Sets array of all children.
     *
     * @param blocks array of blocks
     */
    void setChildren(@Nullable XBTBlock[] blocks);

    /**
     * This method instantiates new child node.
     *
     * @param childIndex child index
     * @return new instance of block
     */
    @Nonnull
    XBTBlock createNewChild(int childIndex);

    /**
     * Sets children of given index.
     *
     * If index is greater than current count of children, new empty blocks will
     * be filled.
     *
     * @param block child block
     * @param childIndex child index
     */
    void setChildAt(XBTBlock block, int childIndex);

    /**
     * Gets count of children.
     *
     * List of children will be trimmed/extended with empty blocks.
     *
     * @param count count of child blocks
     */
    void setChildrenCount(int count);

    /**
     * Removes child on given position.
     *
     * @param childIndex child index
     */
    void removeChild(int childIndex);

    /**
     * Sets block data.
     *
     * @param data data stream
     * @throws java.io.IOException if input/output error
     */
    void setData(@Nullable InputStream data) throws IOException;

    /**
     * Sets block data.
     *
     * @param data data stream
     */
    void setData(@Nullable BinaryData data);

    /**
     * Clears all data, attributes and child blocks.
     */
    void clear();
}
