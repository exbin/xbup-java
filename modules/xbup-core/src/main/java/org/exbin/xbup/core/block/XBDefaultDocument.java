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
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.auxiliary.paged_data.BinaryData;
import org.exbin.xbup.core.type.XBData;

/**
 * Basic plain implementation of XBDocument interface.
 *
 * @version 0.2.1 2020/04/16
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XBDefaultDocument implements XBDocument {

    @Nonnull
    private final XBBlock rootBlock;
    @Nullable
    private final BinaryData tailData;

    public XBDefaultDocument(XBBlock rootBlock) {
        this(rootBlock, (BinaryData) null);
    }

    public XBDefaultDocument(XBBlock rootBlock, @Nullable BinaryData tailData) {
        this.rootBlock = rootBlock;
        this.tailData = tailData;
    }

    public XBDefaultDocument(XBBlock rootBlock, InputStream tailDataStream) throws IOException {
        this.rootBlock = rootBlock;
        XBData data = new XBData();
        data.loadFromStream(tailDataStream);
        this.tailData = data;
    }

    @Nonnull
    @Override
    public Optional<XBBlock> getRootBlock() {
        return Optional.of(rootBlock);
    }

    @Nonnull
    @Override
    public Optional<InputStream> getTailData() {
        return tailData == null ? Optional.empty() : Optional.of(tailData.getDataInputStream());
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
