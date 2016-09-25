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
import org.exbin.utils.binary_data.BinaryData;

/**
 * Basic plain implementation of XBTDocument interface.
 *
 * @version 0.2.0 2016/05/29
 * @author ExBin Project (http://exbin.org)
 */
public class XBTDefaultDocument implements XBTDocument {

    private final XBTBlock rootBlock;
    private final BinaryData tailData;

    public XBTDefaultDocument(XBTBlock rootBlock) {
        this(rootBlock, null);
    }

    public XBTDefaultDocument(XBTBlock rootBlock, BinaryData tailData) {
        this.rootBlock = rootBlock;
        this.tailData = tailData;
    }

    @Override
    public XBTBlock getRootBlock() {
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
}
