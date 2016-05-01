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
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.exbin.xbup.core.block.XBBlock;
import org.exbin.xbup.core.block.XBBlockData;
import org.exbin.xbup.core.block.XBBlockDataMode;
import org.exbin.xbup.core.block.XBBlockTerminationMode;
import org.exbin.xbup.core.block.XBDefaultDocument;
import org.exbin.xbup.core.block.XBEditableBlock;
import org.exbin.xbup.core.parser.XBParserMode;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.parser.token.XBAttribute;
import org.exbin.xbup.core.parser.token.XBToken;
import org.exbin.xbup.core.parser.token.XBTokenType;
import org.exbin.xbup.core.parser.token.convert.XBListenerToToken;
import org.exbin.xbup.core.parser.token.pull.XBPullProvider;
import org.exbin.xbup.core.parser.token.pull.XBPullWriter;
import org.exbin.xbup.core.parser.token.pull.convert.XBProviderToPullProvider;
import org.exbin.xbup.core.stream.SeekableStream;
import org.exbin.xbup.core.type.XBData;
import org.exbin.xbup.parser_tree.XBTreeWriter;

/**
 * XBUP level 0 command writer.
 *
 * This writer expects source data not to be changed, so exclusive lock is
 * recommended.
 *
 * @version 0.1.25 2015/10/19
 * @author ExBin Project (http://exbin.org)
 */
public class XBWriter implements XBCommandWriter, XBPullProvider, Closeable {

    private InputStream inputStream;
    private OutputStream outputStream;
    private XBReader reader;
    private XBBlockData extendedArea = null;
    private XBTreeWriter treeWriter = null;
    private long activeBlockId = 0;

    /**
     * Writer keeps links to all blocks related to it.
     */
    private long blockStoreIndex = 1;
    private Map<Long, XBWriterBlock> blockStore = new HashMap<>();
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
        this.outputStream = stream;
        try (XBPullWriter pullWriter = new XBPullWriter(outputStream)) {
            if (treeWriter == null) {
                XBWriterBlock activeBlock = getActiveBlock();
                if (activeBlock == null) {
                    treeWriter = new XBTreeWriter(new XBDefaultDocument(getRootBlock(), getExtendedArea()));
                } else {
                    treeWriter = new XBTreeWriter(activeBlock);
                }
            }
            XBProviderToPullProvider pullProvider = new XBProviderToPullProvider(treeWriter);
            pullWriter.attachXBPullProvider(pullProvider);
            pullWriter.write();
        }
    }

    @Override
    public void close() throws IOException {
        inputStream.close();
    }

    @Override
    public XBWriterBlock getRootBlock() {
        return getBlock(new long[0]);
    }

    @Override
    public XBWriterBlock getBlock(long[] blockPath) {
        // Search for existing cache record first 
        BlockTreeCache cacheNode = this.blockTreeCache;
        if (cacheNode != null && blockPath.length == 0) {
            return cacheNode.block;
        }

        int depth = 0;
        while (cacheNode != null && depth < blockPath.length) {
            if (cacheNode.childBlocks != null) {
                cacheNode = cacheNode.childBlocks.get((int) blockPath[depth]);
                depth++;
                if (cacheNode != null && depth == blockPath.length && cacheNode.block != null) {
                    return cacheNode.block;
                }
            } else {
                break;
            }
        }

        // TODO fail if it doesn't exist
        // Create new block
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

        BlockTreeCache cacheNode = this.blockTreeCache;
        if (cacheNode == null) {
            cacheNode = new BlockTreeCache();
            blockTreeCache = cacheNode;
        }

        for (int depth = 0; depth < blockPath.length; depth++) {
            if (cacheNode.childBlocks == null) {
                cacheNode.childBlocks = new HashMap<>();
            }

            BlockTreeCache childNode = cacheNode.childBlocks.get((int) blockPath[depth]);
            if (childNode == null) {
                childNode = new BlockTreeCache();
                cacheNode.childBlocks.put((int) blockPath[depth], childNode);
            }
            cacheNode = childNode;
        }

        cacheNode.block = block;
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
        setActiveBlockId(targetBlock);
        if (!targetBlock.isFixedBlock()) {
            reader.seekBlock(targetBlock);
        }
    }

    @Override
    public XBToken pullXBToken() throws XBProcessingException, IOException {
        XBWriterBlock block;
        if (activeBlockId == 0) {
            block = (XBWriterBlock) getRootBlock();
            setActiveBlockId(block);
        } else {
            block = getActiveBlock();
        }

        if (block.isFixedBlock()) {
            if (treeWriter == null) {
                treeWriter = new XBTreeWriter(block);
            }

            XBListenerToToken tokenListener = new XBListenerToToken();
            treeWriter.produceXB(tokenListener, false);
            XBToken token = tokenListener.getToken();
            if (token.getTokenType() == XBTokenType.END) {
                if (block.getDataMode() == XBBlockDataMode.NODE_BLOCK) {
                    XBWriterBlock childBlock = (XBWriterBlock) block.getChildAt(0);
                    if (childBlock == null) {
                        // TODO set next block
                        // TODO use stub tree writer?
                        throw new UnsupportedOperationException("Not supported yet.");
                    } else {
                        setActiveBlockId(childBlock);
                        return pullXBToken();
                    }
                }
            }

            return token;
        } else {
            boolean postSeek = false;
            if (reader.getActiveBlock() != block) {
                seekBlock(block);
                postSeek = true;
            }

            XBToken token = reader.pullXBToken();
            if (token.getTokenType() == XBTokenType.BEGIN && reader.getActiveBlock() != block) {
                if (postSeek) {
                    reader.setActiveBlock(block);
                } else {
                    XBWriterBlock nextBlock = (XBWriterBlock) reader.getActiveBlock();
                    if (nextBlock == null) {
                        nextBlock = getBlock(reader.getCurrentBlockPath());
                        reader.setActiveBlock(nextBlock);
                    }
                    setActiveBlockId(nextBlock);
                }
            } else if (token.getTokenType() == XBTokenType.END) {
                XBWriterBlock nextBlock = (XBWriterBlock) getActiveBlock().getParent();
                reader.setActiveBlock(nextBlock);
                setActiveBlockId(nextBlock);
            }
            return token;
        }
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
        return reader.getCurrentBlockPath();
    }

    @Override
    public InputStream getExtendedArea() {
        XBWriterBlock rootBlock = getRootBlock();
        if (rootBlock != null && rootBlock.isFixedBlock()) {
            return extendedArea == null ? new XBData().getDataInputStream() : extendedArea.getDataInputStream();
        }

        return reader.getExtendedArea();
    }

    @Override
    public long getExtendedAreaSize() {
        XBWriterBlock rootBlock = getRootBlock();
        if (rootBlock != null && rootBlock.isFixedBlock()) {
            return extendedArea == null ? -1 : extendedArea.getDataSize();
        }

        return reader.getExtendedAreaSize();
    }

    @Override
    public long getDocumentSize() {
        return reader.getDocumentSize();
    }

    @Override
    public void setRootBlock(XBBlock block) {
        long[] blockPath = new long[0];
        XBWriterBlock rootBlock = getBlock(blockPath);
        if (rootBlock == null) {
            rootBlock = createNewBlock(blockPath);
        }
        rootBlock.setFixedBlock(block);
    }

    @Override
    public void setExtendedArea(InputStream source) {
        extendedArea = new XBData();
        ((XBData) extendedArea).loadFromStream(source);
    }

    @Override
    public void clear() {
        extendedArea = null;
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private XBWriterBlock getActiveBlock() {
        return activeBlockId == 0 ? null : blockStore.get(activeBlockId);
    }

    private void setActiveBlockId(XBWriterBlock block) {
        activeBlockId = block == null ? 0 : block.getBlockId();
        treeWriter = null;
    }

    public XBWriterBlock createChildBlock(long[] blockPath, int childIndex) {
        long[] childPath = new long[blockPath.length + 1];
        System.arraycopy(blockPath, 0, childPath, 0, blockPath.length);
        childPath[childPath.length - 1] = childIndex;
        return createNewBlock(childPath);
    }

    private class BlockTreeCache {

        XBWriterBlock block;
        Map<Integer, BlockTreeCache> childBlocks;
    }
}