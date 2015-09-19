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
 * Interface for editable XBUP level 1 block.
 *
 * @version 0.2.0 2015/09/19
 * @author XBUP Project (http://xbup.org)
 */
public interface XBTEditableBlock extends XBTBlock {

    /**
     * Sets parent block.
     *
     * @param parent block
     */
    public void setParent(XBTBlock parent);

    /**
     * Sets terminated mode.
     *
     * @param terminationMode terminated mode flag
     */
    public void setTerminationMode(XBBlockTerminationMode terminationMode);

    /**
     * Sets data mode.
     *
     * @param dataMode data mode
     */
    public void setDataMode(XBBlockDataMode dataMode);

    /**
     * Sets array of attributes in order of appearance.
     *
     * @param attributes array of attributes
     */
    public void setAttributes(XBAttribute[] attributes);

    /**
     * Sets attribute of given index.
     *
     * If index is greater than current count of attributes, new zero attributes
     * will be filled.
     *
     * @param attribute attribute value
     * @param attributeIndex attribute index
     */
    public void setAttributeAt(XBAttribute attribute, int attributeIndex);

    /**
     * Sets count of attributes.
     *
     * List of attributes will be trimmed/extended with zero values.
     *
     * @param count count of attributes
     */
    public void setAttributesCount(int count);

    /**
     * Removes attribute on given position.
     *
     * @param attributeIndex
     */
    public void removeAttribute(int attributeIndex);

    /**
     * Sets block type.
     *
     * @param blockType block type
     */
    public void setBlockType(XBBlockType blockType);

    /**
     * Sets array of all children.
     *
     * @param blocks array of blocks
     */
    public void setChildren(XBTBlock[] blocks);

    /**
     * This method instantiates new child node.
     *
     * @param childIndex child index
     * @return new instance of block
     */
    public XBTBlock createNewChild(int childIndex);

    /**
     * Sets children of given index.
     *
     * If index is greater than current count of children, new empty blocks will
     * be filled.
     *
     * @param block child block
     * @param childIndex child index
     */
    public void setChildAt(XBTBlock block, int childIndex);

    /**
     * Gets count of children.
     *
     * List of children will be trimmed/extended with empty blocks.
     *
     * @param count count of child blocks
     */
    public void setChildrenCount(int count);

    /**
     * Removes child on given position.
     *
     * @param childIndex
     */
    public void removeChild(int childIndex);

    /**
     * Sets block data.
     *
     * @param data data stream
     */
    public void setData(InputStream data);

    /**
     * Sets block data.
     *
     * @param data data stream
     */
    public void setData(XBBlockData data);

    /**
     * Clears all data, attributes and child blocks.
     */
    public void clear();
}
