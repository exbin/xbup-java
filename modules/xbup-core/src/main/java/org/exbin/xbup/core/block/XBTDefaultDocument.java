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
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.exbin.utils.binary_data.BinaryData;

/**
 * Basic plain implementation of XBTDocument interface.
 *
 * @version 0.2.1 2017/05/09
 * @author ExBin Project (http://exbin.org)
 */
public class XBTDefaultDocument implements XBTDocument {

    @Nonnull
    private final XBTBlock rootBlock;
    @Nullable
    private final BinaryData tailData;

    public XBTDefaultDocument(@Nonnull XBTBlock rootBlock) {
        this(rootBlock, null);
    }

    public XBTDefaultDocument(@Nonnull XBTBlock rootBlock, @Nullable BinaryData tailData) {
        this.rootBlock = rootBlock;
        this.tailData = tailData;
    }

    @Nonnull
    @Override
    public XBTBlock getRootBlock() {
        return rootBlock;
    }

    @Nullable
    @Override
    public InputStream getTailData() {
        if (tailData == null) {
            return null;
        }

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
