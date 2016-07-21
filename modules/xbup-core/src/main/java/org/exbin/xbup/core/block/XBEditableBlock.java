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
import org.exbin.utils.binary_data.BinaryData;
import org.exbin.xbup.core.parser.token.XBAttribute;

/**
 * Interface for editable XBUP level 0 block.
 *
 * @version 0.2.0 2015/09/19
 * @author ExBin Project (http://exbin.org)
 */
public interface XBEditableBlock extends XBBlock {

    /**
     * Sets parent block.
     *
     * @param parent parent block
     */
    public void setParent(XBBlock parent);

    /**
     * Sets terminated mode.
     *
     * @param terminationMode terminated mode flag
     */
    public void setTerminationMode(XBBlockTerminationMode terminationMode);

    /**
     * Sets data block mode.
     *
     * @param dataMode data block mode
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
     * @param attributeIndex attribute index
     */
    public void removeAttribute(int attributeIndex);

    /**
     * Sets array of all children.
     *
     * @param blocks array of children blocks
     */
    public void setChildren(XBBlock[] blocks);

    /**
     * Sets children of given index.
     *
     * If index is greater than current count of children, new empty blocks will
     * be filled.
     *
     * @param block child block
     * @param childIndex index of child block
     */
    public void setChildAt(XBBlock block, int childIndex);

    /**
     * Gets count of children blocks.
     *
     * List of children will be trimmed/extended with empty blocks.
     *
     * @param count count of children
     */
    public void setChildrenCount(int count);

    /**
     * This method instantiates new child node.
     *
     * @param childIndex child index
     * @return new instance of block
     */
    public XBBlock createNewChild(int childIndex);

    /**
     * Removes child on given position.
     *
     * @param childIndex child index
     */
    public void removeChild(int childIndex);

    /**
     * Sets block data.
     *
     * @param data data stream
     * @throws java.io.IOException exception on input/output error
     */
    public void setData(InputStream data) throws IOException;

    /**
     * Sets block data.
     *
     * @param data data stream
     */
    public void setData(BinaryData data);

    /**
     * Clears all data, attributes and child blocks.
     */
    public void clear();
}
