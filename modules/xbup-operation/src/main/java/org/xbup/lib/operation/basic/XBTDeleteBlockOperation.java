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
package org.xbup.lib.operation.basic;

import org.xbup.lib.core.block.XBTEditableBlock;
import org.xbup.lib.operation.Operation;
import org.xbup.lib.operation.XBTDocOperation;

/**
 * Command for deleting child block.
 *
 * @version 0.1.25 2015/04/30
 * @author XBUP Project (http://xbup.org)
 */
public class XBTDeleteBlockOperation extends XBTDocOperation {

    private final long position;

    public XBTDeleteBlockOperation(long position) {
        this.position = position;
    }

    @Override
    public XBBasicOperationType getBasicType() {
        return XBBasicOperationType.ADD_NODE;
    }

    @Override
    public void execute() throws Exception {
        if (position < 1) {
            document.clear();
        } else {
            XBTEditableBlock node = (XBTEditableBlock) document.findBlockByIndex(position);
            XBTEditableBlock parentNode = (XBTEditableBlock) node.getParent();
            parentNode.getChildren().remove(node);
        }
    }

    @Override
    public Operation executeWithUndo() throws Exception {
        int childIndex = 0;
        long parentPosition = -1;
        XBTEditableBlock deletedNode;
        if (position < 1) {
            deletedNode = (XBTEditableBlock) document.getRootBlock();
        } else {
            deletedNode = (XBTEditableBlock) document.findBlockByIndex(position);
            XBTEditableBlock parentNode = (XBTEditableBlock) deletedNode.getParent();
            parentPosition = parentNode.getBlockIndex();
            childIndex = parentNode.getChildren().indexOf(deletedNode);
        }
        XBTAddBlockOperation undoOperation = new XBTAddBlockOperation(parentPosition, childIndex, deletedNode);
        undoOperation.setDocument(document);

        execute();

        return undoOperation;
    }
}
