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

import java.util.ArrayList;
import java.util.List;
import org.xbup.lib.core.block.XBTBlock;
import org.xbup.lib.core.block.XBTEditableBlock;
import org.xbup.lib.operation.Operation;
import org.xbup.lib.operation.XBTDocOperation;

/**
 * Command for adding child block.
 *
 * @version 0.1.25 2015/04/30
 * @author XBUP Project (http://xbup.org)
 */
public class XBTAddBlockOperation extends XBTDocOperation {

    private final XBTEditableBlock newNode;
    private final long parentPosition;
    private final int childIndex;

    public XBTAddBlockOperation(long parentPosition, int childIndex, XBTEditableBlock newNode) {
        this.parentPosition = parentPosition;
        this.newNode = newNode;
        this.childIndex = childIndex;
    }

    @Override
    public XBBasicOperationType getBasicType() {
        return XBBasicOperationType.ADD_NODE;
    }

    @Override
    public void execute() throws Exception {
        if (parentPosition < 0) {
            if (document.getRootBlock() == null) {
                document.setRootBlock(newNode);
            }
        } else {
            XBTEditableBlock parentNode = (XBTEditableBlock) document.findBlockByIndex(parentPosition);
            List<XBTBlock> children = parentNode.getChildren();
            if (children == null) {
                children = new ArrayList<>();
                parentNode.setChildren(children);
            }
            children.add(childIndex, newNode);
            newNode.setParent(parentNode);
        }
    }

    @Override
    public Operation executeWithUndo() throws Exception {
        execute();

        XBTDeleteBlockOperation undoOperation;
        if (parentPosition >= 0) {
            XBTEditableBlock parentNode = (XBTEditableBlock) document.findBlockByIndex(parentPosition);
            XBTBlock node = parentNode.getChildAt(childIndex);
            undoOperation = new XBTDeleteBlockOperation(node.getBlockIndex());
        } else {
            undoOperation = new XBTDeleteBlockOperation(0);
        }
        undoOperation.setDocument(document);
        return undoOperation;
    }
}
