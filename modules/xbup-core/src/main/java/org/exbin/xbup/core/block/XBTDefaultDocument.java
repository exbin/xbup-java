/*
 * Copyright (C) ExBin Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.exbin.xbup.core.block;

import java.io.InputStream;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.exbin.auxiliary.binary_data.BinaryData;

/**
 * Basic plain implementation of XBTDocument interface.
 *
 * @author ExBin Project (https://exbin.org)
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
    public Optional<XBTBlock> getRootBlock() {
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
