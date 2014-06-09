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
package org.xbup.lib.xb.stream.buffer;

import java.io.IOException;
import org.xbup.lib.xb.parser.XBParseException;
import org.xbup.lib.xb.parser.token.XBTToken;
import org.xbup.lib.xb.parser.tree.XBTTreeNode;
import org.xbup.lib.xb.stream.XBTOutputStream;

/**
 * Object model level 1 parser stream receiver.
 *
 * @version 0.1 wr15.0 2007/07/09
 * @author XBUP Project (http://xbup.org)
 */
public class XBTOMOutputStream extends XBTOutputStream {

    private XBTTreeNode treeNode;

    /** Creates a new instance of XBOMOutputStream */
    public XBTOMOutputStream(XBTTreeNode treeNode) {
// TODO:        super(treeNode.fromXBL1Events());
        this.treeNode = treeNode;
    }

    public XBTTreeNode getTreeNode() {
        return treeNode;
    }

    @Override
    public void putXBTToken(XBTToken item) throws IOException, XBParseException {
    }

    @Override
    public void close() throws IOException {
    }

    @Override
    public void flush() throws IOException {
    }
}
