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
package org.xbup.lib.parser_command;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.xbup.lib.core.block.XBBlock;
import org.xbup.lib.core.block.XBBlockDataMode;
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.parser.token.XBAttribute;
import org.xbup.lib.core.parser.token.pull.XBPullReader;

/**
 * XBUP level 0 command reader using pull reader.
 *
 * This reader expects data not to be changed, so exclusive lock on source data
 * is recommended.
 *
 * @version 0.1.25 2015/09/09
 * @author XBUP Project (http://xbup.org)
 */
public class XBReader implements XBCommandReader, Closeable {

    private InputStream source;
    private XBPullReader pullReader = null;
    private final List<XBReader.BlockPosition> pathPositions = new ArrayList<>();

    public XBReader() throws IOException {
        reset();
    }

    public XBReader(InputStream source) throws IOException {
        this.source = source;
        reset();
    }

    @Override
    public void open(InputStream stream) throws IOException {
        source = stream;
        reset();
    }

    @Override
    public void close() throws IOException {
        source.close();
    }

    @Override
    public void resetParser() throws IOException {
        reset();
    }

    private void reset() throws IOException {
        pathPositions.clear();
        if (pullReader == null) {
            pullReader = new XBPullReader(source);
        } else {
            pullReader.resetXB();
        }
    }

    @Override
    public XBBlock getRootBlock() {
        return getBlock(new long[0]);
    }

    @Override
    public XBBlock getBlock(long[] blockPath) {
        return new XBReaderBlock(this, blockPath);
    }

    /**
     * Returns currently selected block.
     *
     * @return block handler
     */
    public XBBlock getBlock() {
        return getBlock(getCurrentPath());
    }

    public XBBlockDataMode getBlockDataMode() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public XBBlockTerminationMode getBlockTerminationMode() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public XBAttribute getBlockAttribute(int attributeIndex) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private long[] getCurrentPath() {
        long[] currentPath = new long[pathPositions.size()];
        for (int i = 0; i < currentPath.length; i++) {
            currentPath[i] = pathPositions.get(i).blockIndex;
        }
        return currentPath;
    }

    private int getCurrentLevel() {
        return pathPositions.size() - 1;
    }

    private class BlockPosition {

        public long streamPosition;
        public int blockIndex;
    }
}
