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

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.auxiliary.binary_data.BinaryData;
import org.exbin.xbup.core.type.XBData;

/**
 * Basic plain implementation of XBEditableDocument interface.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XBDefaultEditableDocument implements XBEditableDocument {

    @Nonnull
    private XBBlock rootBlock;
    @Nullable
    private BinaryData tailData;

    public XBDefaultEditableDocument(XBBlock rootBlock) {
        this(rootBlock, null);
    }

    public XBDefaultEditableDocument(XBBlock rootBlock, @Nullable BinaryData tailData) {
        this.rootBlock = rootBlock;
        this.tailData = tailData;
    }

    @Nonnull
    @Override
    public Optional<XBBlock> getRootBlock() {
        return Optional.ofNullable(rootBlock);
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

    @Override
    public void setRootBlock(XBBlock block) {
        rootBlock = block;
    }

    @Override
    public void setTailData(@Nullable InputStream source) throws IOException {
        if (source == null) {
            tailData = null;
        } else {
            XBData data = new XBData();
            data.loadFromStream(source);
            tailData = data;
        }
    }

    @Override
    public void clear() {
        rootBlock = new XBDefaultBlock();
        tailData = null;
    }
}
