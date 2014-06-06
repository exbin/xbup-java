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
package org.xbup.lib.xbcommand.basic;

import java.io.InputStream;
import org.xbup.lib.xb.block.XBBlockDataMode;
import org.xbup.lib.xb.block.XBTEditableDocument;
import org.xbup.lib.xb.parser.tree.XBTTreeNode;
import org.xbup.lib.xbcommand.XBTCommand;

/**
 * Command for modifying block data.
 *
 * @version 0.1 wr20.0 2010/09/15
 * @author XBUP Project (http://xbup.org)
 */
public class XBTModDataBlockCommand implements XBTCommand {

    private String caption;
    private InputStream dataList;
    private int position;

    public XBTModDataBlockCommand(XBTTreeNode node, XBTTreeNode newNode) throws Exception {
        caption = "Modified attribute value";
        if (newNode.getDataMode() == XBBlockDataMode.NODE_BLOCK) {
            throw new Exception("Unable to process attribute node");
        }
        if (node.getDataMode() == XBBlockDataMode.NODE_BLOCK) {
            throw new Exception("Unable to process attribute node");
        }
        dataList = newNode.getData();
        position = node.getBlockIndex();
    }

    @Override
    public XBBasicCommandType getOpType() {
        return XBBasicCommandType.NODE_MOD_DATA;
    }

    @Override
    public String getCaption() {
        return caption;
    }

    @Override
    public void perform(XBTEditableDocument document) {
        XBTTreeNode node = (XBTTreeNode) document.findNodeByIndex(position);
        InputStream newList = node.getData();
        node.setData(dataList);
        dataList = newList;
    }

    @Override
    public void revert(XBTEditableDocument document) throws Exception {
        XBTTreeNode node = (XBTTreeNode) document.findNodeByIndex(position);
        InputStream newList = node.getData();
        node.setData(dataList);
        dataList = newList;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    @Override
    public boolean supportRevert() {
        return true;
    }
}
