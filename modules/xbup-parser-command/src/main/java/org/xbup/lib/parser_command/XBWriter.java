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
import org.xbup.lib.core.block.XBBlockDataMode;
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.block.XBEditableBlock;
import org.xbup.lib.core.parser.XBParserMode;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.token.XBAttribute;
import org.xbup.lib.core.stream.SeekableStream;

/**
 * XBUP level 0 command writer.
 *
 * This writer expects source data not to be changed, so exclusive lock is
 * recommended.
 *
 * @version 0.1.25 2015/10/04
 * @author XBUP Project (http://xbup.org)
 */
public class XBWriter implements XBCommandWriter, Closeable {

    private InputStream inputStream;
    private OutputStream outputStream;
    private XBReader reader;
    private List<XBWriter.BlockPosition> pathPositions;

    public XBWriter() {
        reset();
    }

    public XBWriter(InputStream inputStream, OutputStream outputStream) {
        this(inputStream, outputStream, XBParserMode.FULL);
    }

    public XBWriter(InputStream inputStream, OutputStream outputStream, XBParserMode parserMode) {
        if (!(inputStream instanceof SeekableStream || outputStream instanceof SeekableStream)) {
            throw new IllegalArgumentException("XBWriter only supports seekable streams");
        }

        reader = new XBReader(inputStream, parserMode);
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        reset();
    }

    @Override
    public void open(InputStream stream) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void close() throws IOException {
        inputStream.close();
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

    /**
     * Writes all the changes to the source file.
     */
    @Override
    public void flush() {

    }

    /**
     * Seeks given reader block.
     *
     * @param targetBlock target reader block
     * @throws IOException
     */
    public void seekBlock(XBWriterBlock targetBlock) throws XBProcessingException, IOException {
        reader.seekBlock(targetBlock);
    }

    public XBBlockDataMode getBlockDataMode() throws XBProcessingException, IOException {
        return reader.getBlockDataMode();
    }

    public XBBlockTerminationMode getBlockTerminationMode() throws XBProcessingException, IOException {
        return reader.getBlockTerminationMode();
    }

    public XBAttribute getBlockAttribute(int attributeIndex) throws XBProcessingException, IOException {
        return reader.getBlockAttribute(attributeIndex);
    }

    public int getBlockAttributesCount() throws XBProcessingException, IOException {
        return reader.getBlockAttributesCount();
    }

    public InputStream getBlockData() throws XBProcessingException, IOException {
        return reader.getBlockData();
    }

    public int getBlockChildrenCount() throws XBProcessingException, IOException {
        return reader.getBlockChildrenCount();
    }

    public boolean hasBlockChildAt(int childIndex) throws XBProcessingException, IOException {
        return reader.hasBlockChildAt(childIndex);
    }

    private long[] getCurrentPath() {
        long[] currentPath = new long[pathPositions.size()];
        for (int i = 0; i < currentPath.length; i++) {
            currentPath[i] = pathPositions.get(i).blockIndex;
        }
        return currentPath;
    }

    @Override
    public InputStream getExtendedArea() {
        return reader.getExtendedArea();
    }

    @Override
    public long getExtendedAreaSize() {
        return reader.getExtendedAreaSize();
    }

    @Override
    public long getDocumentSize() {
        return reader.getDocumentSize();
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

    private class BlockPosition {

        public long streamPosition;
        public int blockIndex;
    }
}
