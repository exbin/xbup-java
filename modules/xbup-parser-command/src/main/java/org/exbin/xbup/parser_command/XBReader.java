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
import java.util.ArrayList;
import java.util.List;
import org.exbin.xbup.core.block.XBBlock;
import org.exbin.xbup.core.block.XBBlockDataMode;
import org.exbin.xbup.core.block.XBBlockTerminationMode;
import org.exbin.xbup.core.parser.XBParseException;
import org.exbin.xbup.core.parser.XBParserMode;
import org.exbin.xbup.core.parser.XBParserState;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.parser.XBProcessingExceptionType;
import org.exbin.xbup.core.parser.basic.XBHead;
import org.exbin.xbup.core.parser.basic.wrapper.FixedDataInputStreamWrapper;
import org.exbin.xbup.core.parser.basic.wrapper.TailDataInputStreamWrapper;
import org.exbin.xbup.core.parser.basic.wrapper.TerminatedDataInputStreamWrapper;
import org.exbin.xbup.core.parser.token.XBAttribute;
import org.exbin.xbup.core.parser.token.XBAttributeToken;
import org.exbin.xbup.core.parser.token.XBBeginToken;
import org.exbin.xbup.core.parser.token.XBDataToken;
import org.exbin.xbup.core.parser.token.XBEndToken;
import org.exbin.xbup.core.parser.token.XBToken;
import org.exbin.xbup.core.parser.token.XBTokenType;
import org.exbin.xbup.core.parser.token.pull.XBPullProvider;
import org.exbin.xbup.core.stream.FinishableStream;
import org.exbin.xbup.core.stream.SeekableStream;
import org.exbin.xbup.core.ubnumber.type.UBENat32;
import org.exbin.xbup.core.ubnumber.type.UBNat32;

/**
 * XBUP level 0 command reader using pull reader.
 *
 * This reader expects data not to be changed, so exclusive lock on source data
 * is recommended.
 *
 * @version 0.2.1 2017/05/19
 * @author ExBin Project (http://exbin.org)
 */
public class XBReader implements XBCommandReader, XBPullProvider, Closeable {

    private XBParserState parserState;
    private XBParserMode parserMode = XBParserMode.FULL;
    private FinishableStream dataWrapper;
    private final List<XBReader.BlockPosition> pathPositions = new ArrayList<>();

    private InputStream inputStream;
    private long currentSourcePosition;

    private XBBlockDataMode currentBlockDataMode;
    private XBBlockTerminationMode currentBlockTerminationMode;
    private int attributePartSizeValue;
    private Integer dataPartSizeValue;
    private Integer currentAttributeIndex;
    private int currentChildIndex;
    // Cached last block accesing this reader for faster position matching
    private XBCommandBlock activeBlock = null;

    public XBReader() {
        resetParser();
    }

    public XBReader(InputStream stream) {
        openStream(stream);
    }

    public XBReader(InputStream stream, XBParserMode parserMode) {
        this.parserMode = parserMode;
        openStream(stream);
    }

    @Override
    public void open(InputStream stream) throws IOException {
        openStream(stream);
    }

    private void openStream(InputStream inputStream) {
        if (!(inputStream instanceof SeekableStream)) {
            throw new IllegalArgumentException("XBReader only supports seekable streams");
        }

        this.inputStream = inputStream;
        resetParser();
    }

    @Override
    public void close() throws IOException {
        if (inputStream != null) {
            inputStream.close();
        }
    }

    private void resetParser() {
        pathPositions.clear();
        currentSourcePosition = 0;
        attributePartSizeValue = 0;
        dataPartSizeValue = null;
        currentChildIndex = 0;
        parserState = XBParserState.START;
        activeBlock = null;

        resetBlockState();
    }

    @Override
    public void resetXB() throws IOException {
        resetParser();
        if (inputStream != null) {
            ((SeekableStream) inputStream).seek(currentSourcePosition);
        }
    }

    private void resetBlock() throws IOException {
        if (pathPositions.isEmpty()) {
            currentSourcePosition = 0;
            parserState = XBParserState.START;
        } else {
            BlockPosition topPosition = pathPositions.get(pathPositions.size() - 1);
            revertStatus(currentSourcePosition - topPosition.streamPosition);
            currentSourcePosition = topPosition.streamPosition;
            pathPositions.remove(pathPositions.size() - 1);
            currentChildIndex = topPosition.blockIndex;
            parserState = XBParserState.BLOCK_BEGIN;
        }

        resetBlockState();
        if (inputStream != null) {
            ((SeekableStream) inputStream).seek(currentSourcePosition);
        }
    }

    private void resetBlockState() {
        currentBlockDataMode = null;
        currentBlockTerminationMode = null;
        currentAttributeIndex = null;
        dataWrapper = null;
    }

    @Override
    public XBBlock getRootBlock() {
        return getBlock(new long[0]);
    }

    @Override
    public XBBlock getBlock(long[] blockPath) {
        return new XBReaderBlock(this, blockPath);
    }

    public XBCommandBlock getActiveBlock() {
        return activeBlock;
    }

    public void setActiveBlock(XBCommandBlock activeBlock) {
        this.activeBlock = activeBlock;
    }

    @Override
    public InputStream getTailData() {
        // TODO: Stupid algorithmus for initial testing
        try {
            if (parserState == XBParserState.EOF) {
                resetXB();
            }

            while (!isFinishedXB()) {
                XBToken token = pullXBToken();
                if (parserState == XBParserState.TAIL_DATA) {
                    return ((XBDataToken) token).getData();
                }
            }
        } catch (IOException ex) {
        }

        return null;
    }

    @Override
    public long getTailDataSize() {
        try {
            // TODO
            TailDataInputStreamWrapper tailDataWrapper = (TailDataInputStreamWrapper) getTailData();
            return tailDataWrapper == null ? 0 : tailDataWrapper.available();
        } catch (IOException ex) {
        }

        return 0;
    }

    /**
     * Returns currently selected block.
     *
     * @return block handler
     */
    public XBBlock getBlock() {
        return getBlock(getCurrentBlockPath());
    }

    public XBBlockDataMode getBlockDataMode() throws XBProcessingException, IOException {
        if (parserState == XBParserState.BLOCK_BEGIN || parserState == XBParserState.START) {
            resetBlock();
            pullXBToken();
        }

        return currentBlockDataMode;
    }

    public XBBlockTerminationMode getBlockTerminationMode() throws XBProcessingException, IOException {
        if (parserState == XBParserState.BLOCK_BEGIN || parserState == XBParserState.START) {
            resetBlock();
            pullXBToken();
        }

        return currentBlockTerminationMode;
    }

    public XBAttribute getBlockAttribute(int attributeIndex) throws XBProcessingException, IOException {
        if (currentAttributeIndex == null || currentAttributeIndex >= attributeIndex) {
            resetBlock();
            pullXBToken();
        }

        while (currentAttributeIndex != null && currentAttributeIndex < attributeIndex) {
            pullXBToken();
        }

        if (currentAttributeIndex != null && currentAttributeIndex == attributeIndex) {
            return ((XBAttributeToken) pullXBToken()).getAttribute();
        }

        return null;
    }

    public int getBlockAttributesCount() throws XBProcessingException, IOException {
        int attributesCount = 0;
        if (currentAttributeIndex != null) {
            attributesCount = currentAttributeIndex;
        } else {
            resetBlock();
            pullXBToken();
        }

        while (parserState == XBParserState.ATTRIBUTE_PART) {
            attributesCount++;
            pullXBToken();
        }

        return attributesCount;
    }

    public InputStream getBlockData() throws XBProcessingException, IOException {
        if (parserState != XBParserState.BLOCK_BEGIN) {
            resetBlock();
            pullXBToken();
        }

        if (parserState == XBParserState.DATA_PART) {
            return ((XBDataToken) pullXBToken()).getData();
        } else {
            return null;
        }
    }

    public int getBlockChildrenCount() throws XBProcessingException, IOException {
        resetBlock();
        pullXBToken();

        if (currentBlockDataMode == XBBlockDataMode.DATA_BLOCK) {
            return 0;
        }

        // TODO performance: skip blocks
        int subDepth = 1;
        int childCount = 0;
        XBToken token;
        do {
            token = pullXBToken();
            if (token.getTokenType() == XBTokenType.BEGIN) {
                if (subDepth == 1) {
                    childCount++;
                }

                subDepth++;
            } else if (token.getTokenType() == XBTokenType.END) {
                subDepth--;
            }
        } while (subDepth > 0 || token.getTokenType() != XBTokenType.END);

        return childCount;
    }

    boolean hasBlockChildAt(int childIndex) throws XBProcessingException, IOException {
        // TODO performance: skip only blocks up to childIndex
        return childIndex < getBlockChildrenCount();
    }

    public long[] getCurrentBlockPath() {
        long[] currentPath = new long[pathPositions.size() - 1];
        for (int i = 1; i < currentPath.length; i++) {
            currentPath[i - 1] = pathPositions.get(i).blockIndex;
        }
        return currentPath;
    }

    public long getCurrentBlockStreamPosition() {
        return pathPositions.isEmpty() ? 0 : pathPositions.get(pathPositions.size() - 1).streamPosition;
    }

    /**
     * Returns current level of how deep in tree we are.
     *
     * @return tree level, 0 means root
     */
    public int getLevel() {
        return pathPositions.size();
    }

    public void skipXB(long tokenCount) throws XBProcessingException, IOException {
        for (long i = 0; i < tokenCount; i++) {
            pullXBToken();
        }
    }

    public void skipChildXB(long childBlocksCount) throws XBProcessingException, IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void skipAttributesXB(long attributesCount) throws XBProcessingException, IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Pulls next token from the reader.
     *
     * @return next recognized event item in stream
     * @throws IOException exception on input/output error
     */
    @Override
    public XBToken pullXBToken() throws XBProcessingException, IOException {
        if (dataWrapper != null) {
            dataWrapper.finish();
            addProcessedSize((int) dataWrapper.getLength());
            dataWrapper = null;
        }

        if (parserState == XBParserState.START) {
            if (parserMode != XBParserMode.SINGLE_BLOCK && parserMode != XBParserMode.SKIP_HEAD) {
                currentSourcePosition += XBHead.checkXBUPHead(inputStream);
            }

            parserState = XBParserState.BLOCK_BEGIN;
        }

        switch (parserState) {
            case BLOCK_BEGIN: {
                UBNat32 attributePartSize = new UBNat32();
                int headSize = attributePartSize.fromStreamUB(inputStream);
                if (attributePartSize.getLong() == 0) {
                    // Process terminator
                    if (pathPositions.isEmpty() || pathPositions.get(pathPositions.size() - 1).sizeLimit != null) {
                        throw new XBParseException("Unexpected terminator", XBProcessingExceptionType.UNEXPECTED_TERMINATOR);
                    }

                    addProcessedSize(headSize);
                    parserState = XBParserState.BLOCK_END;
                    // no break - Continue to process BLOCK_END
                } else {
                    attributePartSizeValue = attributePartSize.getInt();

                    UBENat32 dataPartSize = new UBENat32();
                    int dataPartSizeLength = dataPartSize.fromStreamUB(inputStream);
                    dataPartSizeValue = dataPartSize.isInfinity() ? null : dataPartSize.getInt();

                    pathDown();
                    addProcessedSize(headSize + dataPartSizeLength);
                    currentBlockTerminationMode = dataPartSize.isInfinity() ? XBBlockTerminationMode.TERMINATED_BY_ZERO : XBBlockTerminationMode.SIZE_SPECIFIED;

                    if (attributePartSizeValue == dataPartSizeLength) {
                        currentBlockDataMode = XBBlockDataMode.DATA_BLOCK;
                        parserState = XBParserState.DATA_PART;
                    } else {
                        currentBlockDataMode = XBBlockDataMode.NODE_BLOCK;
                        attributePartSizeValue -= dataPartSizeLength;
                        currentAttributeIndex = 0;
                        if (attributePartSizeValue > 0) {
                            parserState = XBParserState.ATTRIBUTE_PART;
                        } else if (dataPartSizeValue == null || dataPartSizeValue > 0) {
                            parserState = XBParserState.BLOCK_BEGIN;
                        } else {
                            parserState = XBParserState.BLOCK_END;
                        }
                    }

                    return XBBeginToken.createToken(dataPartSizeValue == null ? XBBlockTerminationMode.TERMINATED_BY_ZERO : XBBlockTerminationMode.SIZE_SPECIFIED);
                }
            }

            case TAIL_DATA:
            case BLOCK_END: {
                // must be after begin
                if (pathPositions.size() == 1) {
                    if (parserState == XBParserState.BLOCK_END) {
                        if (parserMode != XBParserMode.SINGLE_BLOCK && parserMode != XBParserMode.SKIP_TAIL) {
                            parserState = XBParserState.TAIL_DATA;
                            TailDataInputStreamWrapper wrapper = new TailDataInputStreamWrapper(inputStream);
                            if (wrapper.available() > 0) {
                                return new XBDataToken(wrapper);
                            }
                        }
                    }

                    parserState = XBParserState.EOF;
                } else {
                    pathUp();
                    if (pathPositions.get(pathPositions.size() - 1).sizeLimit == null || pathPositions.get(pathPositions.size() - 1).sizeLimit > 0) {
                        parserState = XBParserState.BLOCK_BEGIN;
                    }
                }

                return new XBEndToken();
            }

            case ATTRIBUTE_PART: {
                UBNat32 attribute = new UBNat32();
                int attributeLength = attribute.fromStreamUB(inputStream);
                if (attributeLength > attributePartSizeValue) {
                    throw new XBParseException("Attribute overflow", XBProcessingExceptionType.ATTRIBUTE_OVERFLOW);
                }

                addProcessedSize(attributeLength);
                attributePartSizeValue -= attributeLength;
                if (attributePartSizeValue == 0) {
                    if (dataPartSizeValue == null || dataPartSizeValue > 0) {
                        parserState = XBParserState.BLOCK_BEGIN;
                    } else {
                        parserState = XBParserState.BLOCK_END;
                    }
                    currentAttributeIndex = null;
                } else {
                    currentAttributeIndex++;
                }

                return new XBAttributeToken(attribute);
            }

            case DATA_PART: {
                dataWrapper = (dataPartSizeValue == null)
                        ? new TerminatedDataInputStreamWrapper(inputStream)
                        : new FixedDataInputStreamWrapper(inputStream, dataPartSizeValue);
                parserState = XBParserState.BLOCK_END;
                return new XBDataToken((InputStream) dataWrapper);
            }

            case EOF:
                throw new XBParseException("Reading After End", XBProcessingExceptionType.READING_AFTER_END);

            default:
                throw new XBParseException("Unexpected pull item type", XBProcessingExceptionType.UNKNOWN);
        }
    }

    public boolean isFinishedXB() {
        return parserState == XBParserState.EOF;
    }

    /**
     * Returns actual parsing mode.
     *
     * @return parse mode
     */
    public XBParserState getParseMode() {
        return parserState;
    }

    @Override
    public long getDocumentSize() {
        return ((SeekableStream) inputStream).getStreamSize();
    }

    /**
     * Seeks given reader block.
     *
     * @param targetBlock target reader block
     * @throws IOException exception on input/output error
     */
    public void seekBlock(XBCommandBlock targetBlock) throws XBProcessingException, IOException {
        if (activeBlock != targetBlock) {
            seekBlockPath(targetBlock.getBlockPath());
            activeBlock = targetBlock;
        }
    }

    /**
     * Seeks given block path in stream.
     *
     * @param blockPath target block path
     * @throws IOException exception on input/output error
     */
    public void seekBlockPath(long[] blockPath) throws XBProcessingException, IOException {
        if (blockPath == null) {
            throw new NullPointerException("BlockPath cannot be null.");
        }

        if (blockPath.length == 0) {
            resetXB();
            return;
        }

        // Find maximum match in current block path
        int depthMatch = 0;
        while (blockPath.length > depthMatch && pathPositions.size() > depthMatch + 1) {
            if (pathPositions.get(depthMatch + 1).blockIndex == blockPath[depthMatch]) {
                depthMatch++;
            } else {
                if (pathPositions.size() == depthMatch && pathPositions.get(depthMatch + 1).blockIndex < blockPath[depthMatch]) {
                    depthMatch++;
                }
                break;
            }
        }

        // Seek to the most further block position
        if (depthMatch == 0) {
            resetXB();
        } else {
            // Remove block positions after matched depth, but compute processed size
            for (int i = pathPositions.size() - 1; i >= depthMatch; i--) {
                pathPositions.remove(i);
            }

            resetBlock();
        }

        // TODO performance: null only in some cases?
        activeBlock = null;

        // TODO performance: optimize
        // Traverse tree to the target position
        do {
            XBToken token = pullXBToken();
            if (token.getTokenType() == XBTokenType.BEGIN) {
                if (pathMatch(blockPath)) {
                    return;
                }
            }
        } while (pathPositions.size() >= depthMatch);

        throw new XBProcessingException("Unable to seek requested block path", XBProcessingExceptionType.ILLEGAL_OPERATION);
    }

    private boolean pathMatch(long[] blockPath) {
        if (pathPositions.size() != blockPath.length + 1) {
            return false;
        }

        for (int i = 1; i < pathPositions.size(); i++) {
            if (pathPositions.get(i).blockIndex != blockPath[i - 1]) {
                return false;
            }
        }

        return true;
    }

    /**
     * Adds given value to processedSize.
     *
     * @param value value
     */
    private void addProcessedSize(int value) {
        shrinkStatus(value);
        currentSourcePosition += value;
    }

    /**
     * Shrinks limits accross all depths.
     *
     * @param value Value to shrink all limits off
     * @throws XBParseException If limits are breached
     */
    private void shrinkStatus(int value) throws XBParseException {
        for (int index = 0; index < pathPositions.size() - 1; index++) {
            BlockPosition blockPosition = pathPositions.get(index);
            Integer limit = blockPosition.sizeLimit;
            if (limit != null) {
                if (limit < value) {
                    throw new XBParseException("Block overflow", XBProcessingExceptionType.BLOCK_OVERFLOW);
                }

                blockPosition.sizeLimit = limit - value;
            }
        }
    }

    /**
     * Reverts limits accross levels up to given depth.
     *
     * @param value value
     */
    private void revertStatus(long value) {
        for (BlockPosition blockPosition : pathPositions) {
            Integer limit = blockPosition.sizeLimit;
            if (limit != null) {
                blockPosition.sizeLimit = (int) (limit + value);
            }
        }
    }

    private void pathDown() {
        pathPositions.add(new BlockPosition(currentSourcePosition, currentChildIndex, dataPartSizeValue));
        resetBlockState();
        currentChildIndex = 0;
        activeBlock = null;
    }

    private void pathUp() {
        BlockPosition removedPosition = pathPositions.remove(pathPositions.size() - 1);
        if (pathPositions.size() > 0) {
            currentChildIndex = removedPosition.blockIndex + 1;
        }
        activeBlock = null;
    }

    /**
     * Record for block position.
     */
    private class BlockPosition {

        long streamPosition;
        int blockIndex;
        Integer sizeLimit;

        public BlockPosition(long streamPosition, int blockIndex, Integer sizeLimit) {
            this.streamPosition = streamPosition;
            this.blockIndex = blockIndex;
            this.sizeLimit = sizeLimit;
        }
    }
}
