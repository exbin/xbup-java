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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.xbup.lib.core.block.XBBlock;
import org.xbup.lib.core.block.XBBlockDataMode;
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.block.XBEditableBlock;
import org.xbup.lib.core.parser.XBParserMode;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.token.XBAttribute;
import org.xbup.lib.core.parser.token.XBToken;
import org.xbup.lib.core.parser.token.pull.XBPullProvider;
import org.xbup.lib.core.stream.SeekableStream;

/**
 * XBUP level 0 command writer.
 *
 * This writer expects source data not to be changed, so exclusive lock is
 * recommended.
 *
 * @version 0.1.25 2015/10/06
 * @author XBUP Project (http://xbup.org)
 */
public class XBWriter implements XBCommandWriter, XBPullProvider, Closeable {

    private InputStream inputStream;
    private OutputStream outputStream;
    private XBReader reader;

    /**
     * Writer keeps links to all blocks related to it.
     */
    private int blockStoreIndex = 1;
    private Map<Integer, XBWriterBlock> blockStore = new HashMap<>();
    // TODO tree structure for search by block path
    private BlockTreeCache blockTreeCache = null;

    public XBWriter() {
        reader = new XBReader();
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
        reader.open(stream);
    }

    @Override
    public void save(OutputStream stream) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void close() throws IOException {
        inputStream.close();
    }

    @Override
    public XBEditableBlock getRootBlock() {
        return getBlock(new long[0]);
    }

    @Override
    public XBEditableBlock getBlock(long[] blockPath) {
        // TODO search for existing first
        // TODO fail if it doesn't exist
        return createNewBlock(blockPath);
    }

    public XBEditableBlock getBlockChild(XBWriterBlock parentBlock, int childIndex) {
        long[] blockPath = parentBlock.getBlockPath();
        long[] childPath = Arrays.copyOf(blockPath, blockPath.length + 1);
        childPath[childPath.length - 1] = childIndex;
        return createNewBlock(childPath);
    }

    private XBWriterBlock createNewBlock(long[] blockPath) {
        blockStoreIndex++;
        XBWriterBlock block = new XBWriterBlock(this, blockPath, blockStoreIndex);
        blockStore.put(blockStoreIndex, block);
        return block;
    }

    @Override
    public void resetXB() throws IOException {
        reset();
    }

    private void reset() {
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

    @Override
    public XBToken pullXBToken() throws XBProcessingException, IOException {
        return reader.pullXBToken();
    }

    public boolean isFinishedXB() {
        return reader.isFinishedXB();
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

    public long[] getCurrentPath() {
        return reader.getCurrentPath();
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

        long streamPosition;
        int blockIndex;
    }

    private class BlockTreeCache {

        XBWriterBlock block;
        Map<Integer, BlockTreeCache> childBlocks;
    }
}
