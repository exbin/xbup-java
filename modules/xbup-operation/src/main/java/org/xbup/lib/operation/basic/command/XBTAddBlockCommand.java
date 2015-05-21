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
package org.xbup.lib.operation.basic.command;

import org.xbup.lib.core.block.XBTEditableBlock;
import org.xbup.lib.operation.XBTOpDocCommand;
import org.xbup.lib.operation.basic.XBBasicCommandType;
import org.xbup.lib.operation.basic.XBTAddBlockOperation;

/**
 * Command for adding child block.
 *
 * @version 0.1.25 2015/04/30
 * @author XBUP Project (http://xbup.org)
 */
public class XBTAddBlockCommand extends XBTOpDocCommand {

    public XBTAddBlockCommand(XBTEditableBlock parentNode, XBTEditableBlock newNode) {
        long position;
        int childIndex = 0;
        if (parentNode == null) {
            position = -1;
        } else {
            position = parentNode.getBlockIndex();
            childIndex = parentNode.getChildrenCount();
        }
        setOperation(new XBTAddBlockOperation(position, childIndex, newNode));
    }

    @Override
    public XBBasicCommandType getBasicType() {
        return XBBasicCommandType.NODE_ADDED;
    }
}
