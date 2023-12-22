/*
 * Copyright (C) ExBin Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.exbin.xbup.core.block;

import java.io.IOException;
import java.io.InputStream;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.auxiliary.binary_data.BinaryData;
import org.exbin.xbup.core.parser.token.XBAttribute;

/**
 * Interface for editable XBUP level 0 block.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public interface XBEditableBlock extends XBBlock {

    /**
     * Sets parent block.
     *
     * @param parent parent block
     */
    void setParent(@Nullable XBBlock parent);

    /**
     * Sets terminated mode.
     *
     * @param terminationMode terminated mode flag
     */
    void setTerminationMode(XBBlockTerminationMode terminationMode);

    /**
     * Sets data block mode.
     *
     * @param dataMode data block mode
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
     * Sets array of all children.
     *
     * @param blocks array of children blocks
     */
    void setChildren(@Nullable XBBlock[] blocks);

    /**
     * Sets children of given index.
     *
     * If index is greater than current count of children, new empty blocks will
     * be filled.
     *
     * @param block child block
     * @param childIndex index of child block
     */
    void setChildAt(XBBlock block, int childIndex);

    /**
     * Gets count of children blocks.
     *
     * List of children will be trimmed/extended with empty blocks.
     *
     * @param count count of children
     */
    void setChildrenCount(int count);

    /**
     * This method instantiates new child node.
     *
     * @param childIndex child index
     * @return new instance of block
     */
    XBBlock createNewChild(int childIndex);

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
     * @throws java.io.IOException exception on input/output error
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
