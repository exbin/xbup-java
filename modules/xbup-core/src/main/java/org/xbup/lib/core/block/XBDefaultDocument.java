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
package org.xbup.lib.core.block;

import java.io.InputStream;

/**
 * Basic plain implementation of XBBlock interface.
 *
 * @version 0.1.25 2015/07/24
 * @author XBUP Project (http://xbup.org)
 */
public class XBDefaultDocument implements XBDocument {

    private final XBBlock rootBlock;
    private final XBBlockData extendedArea;

    public XBDefaultDocument(XBBlock rootBlock) {
        this(rootBlock, null);
    }

    public XBDefaultDocument(XBBlock rootBlock, XBBlockData extendedArea) {
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
    public long getDocumentSize() {
        return -1;
    }

    @Override
    public long getExtendedAreaSize() {
        return extendedArea == null ? 0 : extendedArea.getDataSize();
    }
}
