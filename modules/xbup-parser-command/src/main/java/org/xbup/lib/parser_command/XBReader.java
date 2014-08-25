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

/**
 * XBUP level 0 command reader.
 *
 * This reader expects data not to be changed, so exclusive lock on source data
 * is recommended.
 *
 * @version 0.1.23 2014/04/14
 * @author XBUP Project (http://xbup.org)
 */
public class XBReader implements Closeable {

    private InputStream source;
    private List<XBReader.BlockPosition> pathPositions;

    public XBReader() {
        resetParser();
    }

    public XBReader(InputStream source) {
        this.source = source;
        resetParser();
    }

    /**
     * Open input byte-stream.
     */
    private void openStream(InputStream stream) throws IOException {
        source = stream;
        resetParser();
    }

    private void resetParser() {
        pathPositions = new ArrayList<>();
    }

    public XBBlock getRootBlock() {
        return new XBReaderBlock(this, new long[0]);
    }

    /**
     * Get block handler for given path in document.
     *
     * @param blockPath path to block in document
     * @return block handler
     */
    public XBBlock getBlock(long[] blockPath) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void close() throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private class BlockPosition {

        public long streamPosition;
        public int blockIndex;
    }
}
