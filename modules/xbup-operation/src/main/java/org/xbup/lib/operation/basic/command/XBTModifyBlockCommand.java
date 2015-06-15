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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xbup.lib.operation.XBTOpDocCommand;
import org.xbup.lib.parser_tree.XBTTreeNode;
import org.xbup.lib.operation.basic.XBBasicCommandType;
import org.xbup.lib.operation.undo.XBTLinearUndo;

/**
 * Command for modifying block.
 *
 * @version 0.1.25 2015/04/28
 * @author XBUP Project (http://xbup.org)
 */
public class XBTModifyBlockCommand extends XBTOpDocCommand {

    private final long position;
    private final long index;
    private InputStream data;
    XBTTreeNode newNode;

    public XBTModifyBlockCommand(XBTTreeNode node, XBTTreeNode newNode) {
        this.newNode = newNode;
        if (node.getParent() != null) {
            position = node.getParent().getBlockIndex();
            index = node.getParent().getIndex(node);
        } else {
            position = 0;
            index = -1;
        }
    }

    @Override
    public XBBasicCommandType getBasicType() {
        return XBBasicCommandType.NODE_MODIFIED;
    }

    @Override
    public void redo() {
        XBTTreeNode node;
        XBTTreeNode parent;
        if ((position == 0) && (index == -1)) {
            node = (XBTTreeNode) document.getRootBlock();
            parent = null;
        } else {
            parent = (XBTTreeNode) document.findBlockByIndex(position);
            node = (XBTTreeNode) parent.getChildAt((int) index);
        }

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            node.toStreamUB(stream);
        } catch (IOException ex) {
            Logger.getLogger(XBTLinearUndo.class.getName()).log(Level.SEVERE, null, ex);
        }
        data = new ByteArrayInputStream(stream.toByteArray());
        if ((position == 0) && (index == -1)) {
            document.setRootBlock(null); // TODO: clear() would also delete extended area
        } else {
            throw new UnsupportedOperationException("Not supported yet.");
            // parent.getChildren().remove(node);
        }

        if ((position == 0) && (index == -1)) {
            if (document.getRootBlock() == null) {
                document.setRootBlock(newNode);
// TODO                    document.processSpec();
// TODO                    document.processSpec();
            }
        } else {
            node = (XBTTreeNode) document.findBlockByIndex(position);
            node.setChildAt(newNode, node.getChildrenCount());
            newNode.setParent(node);
            // TODO: update context newNode.setContext(node.getContext());
// TODO                document.processSpec();
        }
    }

    @Override
    public void undo() throws Exception {
        if (position == -1) {
            newNode = (XBTTreeNode) document.getRootBlock();
            document.clear();
        } else {
            XBTTreeNode node = (XBTTreeNode) document.findBlockByIndex(position);
            newNode = (XBTTreeNode) node.getChildAt(node.getChildCount() - 1);
            node.setChildrenCount(node.getChildrenCount() - 1);
        }
        data.reset();
        if ((position == 0) && (index == -1)) {
            XBTTreeNode restoredNode = (XBTTreeNode) document.createNewBlock(null);
            restoredNode.fromStreamUB(data);
            document.setRootBlock(restoredNode);
// TODO                restoredNode.setContext(document.getRootContext());
// TODO                document.processSpec();
        } else {
            XBTTreeNode node = (XBTTreeNode) document.findBlockByIndex(position);
            if (node == null) {
                throw new Exception("Unable to find node referenced in undo");
            }
            XBTTreeNode restoredNode = (XBTTreeNode) node.createNewChild(node.getChildCount());
            restoredNode.fromStreamUB(data);
            node.setChildAt(restoredNode, (int) index);
            // TODO: Update context restoredNode.setContext(node.getContext());
        }
        data = null;
    }
}
