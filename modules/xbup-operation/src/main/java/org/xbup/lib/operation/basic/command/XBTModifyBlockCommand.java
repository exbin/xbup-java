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

import org.xbup.lib.core.block.XBTEditableDocument;
import org.xbup.lib.operation.XBTOpDocCommand;
import org.xbup.lib.parser_tree.XBTTreeNode;
import org.xbup.lib.operation.basic.XBBasicCommandType;
import org.xbup.lib.operation.basic.XBTModifyBlockOperation;

/**
 * Command for modifying block.
 *
 * @version 0.1.25 2015/06/24
 * @author XBUP Project (http://xbup.org)
 */
public class XBTModifyBlockCommand extends XBTOpDocCommand {

    public XBTModifyBlockCommand(XBTEditableDocument document, XBTTreeNode node, XBTTreeNode newNode) {
        super(document);
        long position = node.getBlockIndex();
        setOperation(new XBTModifyBlockOperation(document, position, newNode));
    }

    @Override
    public XBBasicCommandType getBasicType() {
        return XBBasicCommandType.NODE_MODIFIED;
    }
}
