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
package org.xbup.lib.core.parser.basic.convert;

import java.io.IOException;
import org.xbup.lib.core.block.XBBlockType;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.basic.XBTListener;

/**
 * Default XBUP level 1 filter.
 *
 * This filter doesn't change data which are passing thru and is intended for
 * extending.
 *
 * @version 0.1.25 2015/02/05
 * @author XBUP Project (http://xbup.org)
 */
public class XBTTypeReplacingFilter extends XBTDefaultFilter {
    
    private boolean typeReplaced = false;
    private XBBlockType replacementBlockType = null;

    public XBTTypeReplacingFilter(XBBlockType blockType) {
        this.replacementBlockType = blockType;
    }

    public XBTTypeReplacingFilter(XBBlockType blockType, XBTListener listener) {
        this(blockType);
        attachXBTListener(listener);
    }

    @Override
    public void typeXBT(XBBlockType blockType) throws XBProcessingException, IOException {
        if (!typeReplaced) {
            super.typeXBT(replacementBlockType);
            typeReplaced = true;
        }

        super.typeXBT(blockType);
    }

}
