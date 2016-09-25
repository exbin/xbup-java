/*
 * Copyright (C) ExBin Project
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
package org.exbin.xbup.core.block;

import java.io.IOException;
import java.io.InputStream;
import org.exbin.utils.binary_data.BinaryData;
import org.exbin.xbup.core.type.XBData;

/**
 * Basic plain implementation of XBEditableDocument interface.
 *
 * @version 0.2.0 2016/09/25
 * @author ExBin Project (http://exbin.org)
 */
public class XBDefaultEditableDocument implements XBEditableDocument {

    private XBBlock rootBlock;
    private BinaryData tailData;

    public XBDefaultEditableDocument(XBBlock rootBlock) {
        this(rootBlock, null);
    }

    public XBDefaultEditableDocument(XBBlock rootBlock, BinaryData tailData) {
        this.rootBlock = rootBlock;
        this.tailData = tailData;
    }

    @Override
    public XBBlock getRootBlock() {
        return rootBlock;
    }

    @Override
    public InputStream getTailData() {
        return tailData.getDataInputStream();
    }

    @Override
    public long getTailDataSize() {
        return tailData == null ? 0 : tailData.getDataSize();
    }

    @Override
    public long getDocumentSize() {
        return -1;
    }

    @Override
    public void setRootBlock(XBBlock block) {
        rootBlock = block;
    }

    @Override
    public void setTailData(InputStream source) throws IOException {
        XBData data = new XBData();
        data.loadFromStream(source);
        tailData = data;
    }

    @Override
    public void clear() {
        rootBlock = null;
        tailData = null;
    }
}
