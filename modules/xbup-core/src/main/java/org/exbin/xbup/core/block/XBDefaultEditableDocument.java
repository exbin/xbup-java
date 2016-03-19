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

import java.io.InputStream;
import org.exbin.xbup.core.type.XBData;

/**
 * Basic plain implementation of XBEditableDocument interface.
 *
 * @version 0.2.0 2015/10/09
 * @author ExBin Project (http://exbin.org)
 */
public class XBDefaultEditableDocument implements XBEditableDocument {

    private XBBlock rootBlock;
    private XBBlockData extendedArea;

    public XBDefaultEditableDocument(XBBlock rootBlock) {
        this(rootBlock, null);
    }

    public XBDefaultEditableDocument(XBBlock rootBlock, XBBlockData extendedArea) {
        this.rootBlock = rootBlock;
        this.extendedArea = extendedArea;
    }

    @Override
    public XBBlock getRootBlock() {
        return rootBlock;
    }

    @Override
    public InputStream getExtendedArea() {
        return extendedArea.getDataInputStream();
    }

    @Override
    public long getExtendedAreaSize() {
        return extendedArea == null ? 0 : extendedArea.getDataSize();
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
    public void setExtendedArea(InputStream source) {
        XBData data = new XBData();
        data.loadFromStream(source);
        extendedArea = data;
    }

    @Override
    public void clear() {
        rootBlock = null;
        extendedArea = null;
    }
}
