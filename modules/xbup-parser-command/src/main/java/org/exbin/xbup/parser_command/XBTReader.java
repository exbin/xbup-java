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
