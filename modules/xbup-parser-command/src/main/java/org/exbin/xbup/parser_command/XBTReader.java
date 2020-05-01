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
package org.exbin.xbup.parser_command;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.core.block.XBBlock;
import org.exbin.xbup.core.block.XBTBlock;
import org.exbin.xbup.core.parser.XBParserMode;
import org.exbin.xbup.parser_tree.XBBlockToXBTBlock;

/**
 * XBUP level 1 command reader using pull reader.
 *
 * This reader expects data not to be changed, so exclusive lock on source data
 * is recommended.
 *
 * @version 0.2.1 2017/05/24
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XBTReader implements XBTCommandReader, Closeable {

    @Nonnull
    private final XBReader reader;

    public XBTReader() throws IOException {
        reader = new XBReader();
    }

    public XBTReader(InputStream stream) throws IOException {
        reader = new XBReader(stream);
    }

    public XBTReader(InputStream stream, XBParserMode parserMode) throws IOException {
        reader = new XBReader(stream, parserMode);
    }

    @Override
    public void open(@Nonnull InputStream stream) throws IOException {
        reader.open(stream);
    }

    @Override
    public void close() throws IOException {
        reader.close();
    }

    @Override
    public void resetParser() throws IOException {
        reader.resetXB();
    }

    @Nonnull
    @Override
    public Optional<XBTBlock> getRootBlock() {
        Optional<XBBlock> rootBlock = reader.getRootBlock();
        return rootBlock.isPresent() ? Optional.of(new XBBlockToXBTBlock(rootBlock.get())) : Optional.empty();
    }

    @Nonnull
    @Override
    public Optional<XBTBlock> getBlock(long[] blockPath) {
        Optional<XBBlock> block = reader.getBlock(blockPath);
        return block.isPresent() ? Optional.of(new XBBlockToXBTBlock(block.get())) : Optional.empty();
    }

    @Nonnull
    @Override
    public Optional<InputStream> getTailData() {
        return reader.getTailData();
    }

    @Override
    public long getTailDataSize() {
        return reader.getTailDataSize();
    }

    @Override
    public long getDocumentSize() {
        return reader.getDocumentSize();
    }
}
