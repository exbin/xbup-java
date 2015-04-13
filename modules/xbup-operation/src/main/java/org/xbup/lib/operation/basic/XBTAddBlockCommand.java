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
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.operation.XBTDocCommand;

/**
 * Command for adding child block.
 *
 * @version 0.1.25 2015/04/13
 * @author XBUP Project (http://xbup.org)
 */
public class XBTAddBlockCommand extends XBTDocCommand {

    private String caption;
    private XBTEditableBlock newNode;
    private final long position;

    public XBTAddBlockCommand(XBTEditableBlock node, XBTEditableBlock newNode) {
        caption = "Added item";
        if (node == null) {
            position = -1;
        } else {
            position = node.getBlockIndex();
        }
        this.newNode = newNode;
    }

    @Override
    public XBBasicCommandType getOpType() {
        return XBBasicCommandType.NODE_ADD;
    }

    @Override
    public String getCaption() {
        return caption;
    }

    @Override
    public void redo() throws XBProcessingException {
        if (position == -1) {
            if (document.getRootBlock() == null) {
                document.setRootBlock(newNode);
// TODO:                newNode.setContext(document.getRootContext());
// TODO:                document.processSpec();
            }
        } else {
            XBTEditableBlock node = (XBTEditableBlock) document.findBlockByIndex(position);
            List<XBTBlock> children = node.getChildren();
            if (children == null) {
                children = new ArrayList<>();
                node.setChildren(children);
            }
            children.add(newNode);
            newNode.setParent(node);
// TODO:            newNode.setContext(node.getContext());
// TODO:            document.processSpec();
        }
    }

    @Override
    public void undo() {
        if (position == -1) {
            newNode = (XBTEditableBlock) document.getRootBlock();
            document.clear();
        } else {
            XBTEditableBlock node = (XBTEditableBlock) document.findBlockByIndex(position);
            List<XBTBlock> children = node.getChildren();
            newNode = (XBTEditableBlock) children.get(children.size() - 1);
            children.remove(children.size() - 1);
        }
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    @Override
    public boolean canUndo() {
        return true;
    }
}
