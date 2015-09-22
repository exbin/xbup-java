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
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import org.xbup.lib.core.block.XBBlock;
import org.xbup.lib.core.block.XBEditableBlock;

/**
 * XBUP level 0 command writer.
 *
 * This reader expects data not to be changed, so exclusive lock on source data
 * is recommended.
 *
 * @version 0.1.25 2015/09/20
 * @author XBUP Project (http://xbup.org)
 */
public class XBWriter implements XBCommandWriter, Closeable {

    private InputStream source;
    private List<XBWriter.BlockPosition> pathPositions;

    public XBWriter() {
        reset();
    }

    public XBWriter(InputStream source) {
        this.source = source;
        reset();
    }

    @Override
    public void open(InputStream stream) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void close() throws IOException {
        source.close();
    }

    @Override
    public void save(OutputStream stream) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public XBEditableBlock getRootBlock() {
        return new XBWriterBlock(this, new long[0]);
    }

    @Override
    public XBEditableBlock getBlock(long[] blockPath) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void resetXB() throws IOException {
        reset();
    }

    private void reset() {
        pathPositions = new ArrayList<>();
    }

    @Override
    public InputStream getExtendedArea() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public long getExtendedAreaSize() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setRootBlock(XBBlock block) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setExtendedArea(InputStream source) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public long getDocumentSize() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private class BlockPosition {

        public long streamPosition;
        public int blockIndex;
    }
}
