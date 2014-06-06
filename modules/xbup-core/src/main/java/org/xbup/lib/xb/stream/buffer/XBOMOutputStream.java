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
import org.xbup.lib.xb.parser.XBProcessingException;
import org.xbup.lib.xb.parser.basic.XBListener;
import org.xbup.lib.xb.parser.token.XBToken;
import org.xbup.lib.xb.parser.token.event.XBEventListener;
import org.xbup.lib.xb.parser.token.event.convert.XBListenerToEventListener;
import org.xbup.lib.xb.parser.tree.XBTreeNode;
import org.xbup.lib.xb.stream.XBTokenOutputStream;

/**
 * Object model parser stream receiver.
 * 
 * TODO: Clear Listener/EventListener connection later
 *
 * @version 0.1 wr16.0 2008/08/02
 * @author XBUP Project (http://xbup.org)
 */
public class XBOMOutputStream extends XBTokenOutputStream {

    private XBTreeNode treeNode;
    private XBListener treeNodeListener;
    private XBEventListener treeNodeEventListener;

    /** Creates a new instance of XBOMOutputStream */
    public XBOMOutputStream(XBTreeNode treeNode) {
        this.treeNode = treeNode;
        this.treeNodeListener = treeNode.convertFromXB();
        this.treeNodeEventListener = new XBListenerToEventListener(treeNodeListener);
    }

    public XBTreeNode getTreeNode() {
        return treeNode;
    }

    @Override
    public void putXBToken(XBToken token) throws XBProcessingException, IOException {
        if (treeNodeEventListener != null) {
            treeNodeEventListener.putXBToken(token);
        }
    }

    @Override
    public void close() throws IOException {
        treeNodeListener = null;
        treeNodeEventListener = null;
    }

    @Override
    public void flush() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
