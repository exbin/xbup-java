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

import java.util.List;
import org.xbup.lib.core.block.XBBlockDataMode;
import org.xbup.lib.core.block.XBFixedBlockType;
import org.xbup.lib.core.parser.token.XBAttribute;
import org.xbup.lib.operation.XBTDocCommand;
import org.xbup.lib.parser_tree.XBTTreeNode;
import org.xbup.lib.operation.basic.XBBasicCommandType;

/**
 * Command for modifying block attributes.
 *
 * @version 0.1.25 2015/04/13
 * @author XBUP Project (http://xbup.org)
 */
public class XBTModAttrBlockCommand extends XBTDocCommand {

    private String caption;
    private List<XBAttribute> attrList;
    private XBFixedBlockType blockType;
    private boolean singleAttributeType = true;
    private int position;

    public XBTModAttrBlockCommand(XBTTreeNode node, XBTTreeNode newNode) throws Exception {
        caption = "Modified attribute value";
        if (newNode.getDataMode() == XBBlockDataMode.DATA_BLOCK) {
            throw new Exception("Unable to process data node");
        }
        if (node.getDataMode() == XBBlockDataMode.DATA_BLOCK) {
            throw new Exception("Unable to process data node");
        }
        attrList = newNode.getAttributes();
        blockType = newNode.getFixedBlockType();
        singleAttributeType = newNode.getSingleAttributeType();
        position = node.getBlockIndex();
    }

    @Override
    public XBBasicCommandType getBasicType() {
        return XBBasicCommandType.ATTRIBUTE_MODIFIED;
    }

    @Override
    public String getCaption() {
        return caption;
    }

    @Override
    public void redo() {
        XBTTreeNode node = (XBTTreeNode) document.findBlockByIndex(position);
        List<XBAttribute> newList = node.getAttributes();
        node.setAttributes(attrList);
        node.setFixedBlockType(blockType);
        node.setSingleAttributeType(singleAttributeType);
        attrList = newList;
    }

    @Override
    public void undo() throws Exception {
        XBTTreeNode node = (XBTTreeNode) document.findBlockByIndex(position);
        List<XBAttribute> newList = node.getAttributes();
        node.setAttributes(attrList);
        node.setBlockType(blockType);
        attrList = newList;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    @Override
    public boolean canUndo() {
        return true;
    }
}
