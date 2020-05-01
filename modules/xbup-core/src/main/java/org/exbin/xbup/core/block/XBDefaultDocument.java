/*
 * Copyright (C) ExBin Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
