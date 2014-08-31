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
 * Interface for editable XBUP level 0 block.
 *
 * @version 0.1.24 2014/08/26
 * @author XBUP Project (http://xbup.org)
 */
public interface XBEditableBlock extends XBBlock {

    /**
     * Set parent block.
     *
     * @param parent parent block
     */
    public void setParent(XBBlock parent);

    /**
     * Set terminated mode.
     *
     * @param terminationMode terminated mode flag
     */
    public void setTerminationMode(XBBlockTerminationMode terminationMode);

    /**
     * Set data block mode.
     *
     * @param dataMode data block mode
     */
    public void setDataMode(XBBlockDataMode dataMode);

    /**
     * Set list of attributes in order of appearance.
     *
     * @param attributes list of attributes
     */
    public void setAttributes(List<UBNatural> attributes);

    /**
     * Set attribute of given index.
     *
     * @param attribute attribute value
     * @param index attribute index
     */
    public void setAttribute(UBNatural attribute, int index);

    /**
     * Set count of attributes.
     *
     * @param count count of attributes
     */
    public void setAttributesCount(int count);

    /**
     * Set list of all children.
     *
     * @param blocks list of children blocks
     */
    public void setChildren(List<XBBlock> blocks);

    /**
     * Set children of given index.
     *
     * @param block child block
     * @param childIndex index of child block
     */
    public void setChildAt(XBBlock block, int childIndex);

    /**
     * Get count of children blocks.
     *
     * @param count count of children
     */
    public void setChildCount(int count);

    /**
     * Set block data.
     *
     * @param data data stream
     */
    public void setData(InputStream data);
}
