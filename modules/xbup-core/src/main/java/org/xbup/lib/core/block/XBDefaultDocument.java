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
package org.xbup.lib.core.block;

import java.io.InputStream;
import org.xbup.lib.core.type.XBData;

/**
 * Basic plain implementation of XBDocument interface.
 *
 * @version 0.2.0 2015/10/17
 * @author ExBin Project (http://exbin.org)
 */
public class XBDefaultDocument implements XBDocument {

    private final XBBlock rootBlock;
    private final XBBlockData extendedArea;

    public XBDefaultDocument(XBBlock rootBlock) {
        this(rootBlock, (XBBlockData) null);
    }

    public XBDefaultDocument(XBBlock rootBlock, XBBlockData extendedArea) {
        this.rootBlock = rootBlock;
        this.extendedArea = extendedArea;
    }

    public XBDefaultDocument(XBBlock rootBlock, InputStream extendedArea) {
        this.rootBlock = rootBlock;
        XBData data = new XBData();
        data.loadFromStream(extendedArea);
        this.extendedArea = data;
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
}
