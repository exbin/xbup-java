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
 * XBUP level 1 editable block interface.
 *
 * @version 0.1.23 2014/02/20
 * @author XBUP Project (http://xbup.org)
 */
public interface XBTEditableBlock extends XBTBlock {

    /**
     * Set parent block.
     *
     * @param parent block
     */
    public void setParent(XBTBlock parent);

    /**
     * Set terminated mode.
     *
     * @param terminationMode terminated mode flag
     */
    public void setTerminationMode(XBBlockTerminationMode terminationMode);

    /**
     * Set data mode.
     *
     * @param dataMode data mode
     */
    public void setDataMode(XBBlockDataMode dataMode);

    /**
     * Set List of attributes in order of appearance.
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
     * @param blocks list of blocks
     */
    public void setChildren(List<XBTBlock> blocks);

    /**
     * Set children of given index.
     *
     * @param block child block
     * @param childIndex child index
     */
    public void setChildAt(XBTBlock block, int childIndex);

    /**
     * Get count of children.
     *
     * @param count count of child blocks
     */
    public void setChildCount(int count);

    /**
     * Set block data.
     *
     * @param data data stream
     */
    public void setData(InputStream data);
}
