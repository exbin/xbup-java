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
import org.xbup.lib.core.parser.XBParseException;
import org.xbup.lib.core.parser.XBParserMode;
import org.xbup.lib.core.parser.XBParserState;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.XBProcessingExceptionType;
import org.xbup.lib.core.parser.basic.XBHead;
import org.xbup.lib.core.parser.basic.wrapper.ExtendedAreaInputStreamWrapper;
import org.xbup.lib.core.parser.basic.wrapper.FixedDataInputStreamWrapper;
import org.xbup.lib.core.parser.basic.wrapper.TerminatedDataInputStreamWrapper;
import org.xbup.lib.core.parser.token.XBAttribute;
import org.xbup.lib.core.parser.token.XBAttributeToken;
import org.xbup.lib.core.parser.token.XBBeginToken;
import org.xbup.lib.core.parser.token.XBDataToken;
import org.xbup.lib.core.parser.token.XBEndToken;
import org.xbup.lib.core.parser.token.XBToken;
import org.xbup.lib.core.stream.FinishableStream;
import org.xbup.lib.core.stream.SeekableStream;
import org.xbup.lib.core.ubnumber.type.UBENat32;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * XBUP level 0 command reader using pull reader.
 *
 * This reader expects data not to be changed, so exclusive lock on source data
 * is recommended.
 *
 * @version 0.1.25 2015/09/12
 * @author XBUP Project (http://xbup.org)
 */
public class XBReader implements XBCommandReader, Closeable {

    private XBParserState parserState;
    private XBParserMode parserMode = XBParserMode.FULL;
    private FinishableStream dataWrapper;
    private final List<XBReader.BlockPosition> pathPositions = new ArrayList<>();
    private final List<Integer> sizeLimits = new ArrayList<>();
    private int attributePartSizeValue;
    private Integer dataPartSizeValue;

    private InputStream source;

    private boolean currentBlockStarted;
    private XBBlockDataMode currentBlockDataMode;
    private XBBlockTerminationMode currentBlockTerminationMode;
    private int attributePosition;
    private Integer attributesCount;

    public XBReader() throws IOException {
        reset();
    }

    public XBReader(InputStream stream) throws IOException {
        openStream(stream);
    }

    public XBReader(InputStream stream, XBParserMode parserMode) throws IOException {
        this.parserMode = parserMode;
        openStream(stream);
    }

    @Override
    public void open(InputStream stream) throws IOException {
        openStream(stream);
    }

    private void openStream(InputStream stream) throws IOException {
        source = stream;
        reset();
    }

    @Override
    public void close() throws IOException {
        if (source != null) {
            source.close();
        }
    }

    @Override
    public void resetParser() throws IOException {
        reset();

        dataWrapper = null;
        parserState = XBParserState.START;
        sizeLimits.clear();
        attributePartSizeValue = 0;
    }

    private void reset() throws IOException {
        pathPositions.clear();
        resetBlock();
    }

    private void resetBlock() {
        currentBlockStarted = false;
        currentBlockDataMode = null;
        currentBlockTerminationMode = null;
        attributePosition = 0;
        attributesCount = null;
    }

    private void startBlock() {
        // TODO seekBlockStart(getCurrentBlockStreamPosition(), 0);
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
        if (!currentBlockStarted) {
            startBlock();
        }

        return currentBlockDataMode;
    }

    public XBBlockTerminationMode getBlockTerminationMode() {
        if (!currentBlockStarted) {
            startBlock();
        }

        return currentBlockTerminationMode;
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

    public int getCurrentLevel() {
        return pathPositions.size() - 1;
    }

    public long getCurrentBlockStreamPosition() {
        return pathPositions.isEmpty() ? 0 : pathPositions.get(pathPositions.size() - 1).streamPosition;
    }

    public void seekBlockStart(long currentBlockStreamPosition, int preserveLevels) throws IOException {
        dataWrapper = null;
        parserState = XBParserState.START;
        sizeLimits.clear();
        attributePartSizeValue = 0;

        if (source instanceof SeekableStream) {
            ((SeekableStream) source).seek(currentBlockStreamPosition);
        }
    }

    /**
     * Shrinks limits accross all depths.
     *
     * @param length Value to shrink all limits off
     * @throws XBParseException If limits are breached
     */
    private static void shrinkStatus(List<Integer> sizeLimits, int value) throws XBParseException {
        for (int depthLevel = 0; depthLevel < sizeLimits.size(); depthLevel++) {
            Integer limit = sizeLimits.get(depthLevel);
            if (limit != null) {
                if (limit < value) {
                    throw new XBParseException("Block overflow", XBProcessingExceptionType.BLOCK_OVERFLOW);
                }

                sizeLimits.set(depthLevel, limit - value);
            }
        }
    }

    /**
     * Returns current level of how deep in tree we are.
     *
     * @return tree level, 0 means root
     */
    protected int getLevel() {
        return sizeLimits.size();
    }

    /**
     * Indicates parsing completeness.
     *
     * @return true if complete
     * @throws IOException
     */
    public boolean isFinishedXB() throws IOException {
        return parserState == XBParserState.EOF;
    }

    public void skipXB(long tokenCount) throws XBProcessingException, IOException {
        for (long i = 0; i < tokenCount; i++) {
            pullXBToken();
        }
    }

    public void skipChildXB(long childBlocksCount) throws XBProcessingException, IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void skipAttributesXB(long childBlocksCount) throws XBProcessingException, IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Main pull reader parser processing method.
     *
     * @return next recognized event item in stream
     * @throws java.io.IOException
     */
    public XBToken pullXBToken() throws XBProcessingException, IOException {
        if (dataWrapper != null) {
            dataWrapper.finish();
            shrinkStatus(sizeLimits, (int) dataWrapper.getLength());
            dataWrapper = null;
        }

        if (parserState == XBParserState.START) {
            if (parserMode != XBParserMode.SINGLE_BLOCK && parserMode != XBParserMode.SKIP_HEAD) {
                XBHead.checkXBUPHead(source);
            }

            parserState = XBParserState.BLOCK_BEGIN;
        }

        switch (parserState) {
            case BLOCK_BEGIN: {
                UBNat32 attrPartSize = new UBNat32();
                int headSize = attrPartSize.fromStreamUB(source);
                shrinkStatus(sizeLimits, headSize);
                if (attrPartSize.getLong() == 0) {
                    // Process terminator
                    if (sizeLimits.isEmpty() || sizeLimits.get(sizeLimits.size() - 1) != null) {
                        throw new XBParseException("Unexpected terminator", XBProcessingExceptionType.UNEXPECTED_TERMINATOR);
                    }

                    sizeLimits.remove(sizeLimits.size() - 1);
                    parserState = XBParserState.BLOCK_END;
                    // no break - Continue to process BLOCK_END
                } else {
                    attributePartSizeValue = attrPartSize.getInt();
                    shrinkStatus(sizeLimits, attributePartSizeValue);

                    UBENat32 dataPartSize = new UBENat32();
                    int dataPartSizeLength = dataPartSize.fromStreamUB(source);
                    dataPartSizeValue = dataPartSize.isInfinity() ? null : dataPartSize.getInt();

                    if (attributePartSizeValue == dataPartSizeLength) {
                        parserState = XBParserState.DATA_PART;
                    } else {
                        attributePartSizeValue -= dataPartSizeLength;
                        if (attributePartSizeValue > 0) {
                            parserState = XBParserState.ATTRIBUTE_PART;
                        } else {
                            if (dataPartSizeValue == null || dataPartSizeValue > 0) {
                                sizeLimits.add(dataPartSizeValue);
                                parserState = XBParserState.BLOCK_BEGIN;
                            } else {
                                parserState = XBParserState.BLOCK_END;
                            }
                        }
                    }

                    return new XBBeginToken(dataPartSizeValue == null ? XBBlockTerminationMode.TERMINATED_BY_ZERO : XBBlockTerminationMode.SIZE_SPECIFIED);
                }
            }

            case EXTENDED_AREA:
            case BLOCK_END: {
                // must be after begin
                if (sizeLimits.isEmpty()) {
                    if (parserState == XBParserState.BLOCK_END) {
                        if (parserMode != XBParserMode.SINGLE_BLOCK && parserMode != XBParserMode.SKIP_EXTENDED) {
                            parserState = XBParserState.EXTENDED_AREA;
                            ExtendedAreaInputStreamWrapper wrapper = new ExtendedAreaInputStreamWrapper(source);
                            if (wrapper.available() > 0) {
                                return new XBDataToken(wrapper);
                            }
                        }
                    }

                    parserState = XBParserState.EOF;
                } else {
                    if (sizeLimits.get(sizeLimits.size() - 1) == null || sizeLimits.get(sizeLimits.size() - 1) > 0) {
                        parserState = XBParserState.BLOCK_BEGIN;
                    } else {
                        sizeLimits.remove(sizeLimits.size() - 1);
                    }
                }

                return new XBEndToken();
            }

            case ATTRIBUTE_PART: {
                UBNat32 attribute = new UBNat32();
                int attributeLength = attribute.fromStreamUB(source);
                if (attributeLength > attributePartSizeValue) {
                    throw new XBParseException("Attribute overflow", XBProcessingExceptionType.ATTRIBUTE_OVERFLOW);
                }

                attributePartSizeValue -= attributeLength;
                if (attributePartSizeValue == 0) {
                    if (dataPartSizeValue == null || dataPartSizeValue > 0) {
                        sizeLimits.add(dataPartSizeValue);
                        parserState = XBParserState.BLOCK_BEGIN;
                    } else {
                        parserState = XBParserState.BLOCK_END;
                    }
                }

                return new XBAttributeToken(attribute);
            }

            case DATA_PART: {
                dataWrapper = (dataPartSizeValue == null)
                        ? new TerminatedDataInputStreamWrapper(source)
                        : new FixedDataInputStreamWrapper(source, dataPartSizeValue);

                parserState = XBParserState.BLOCK_END;
                return new XBDataToken((InputStream) dataWrapper);
            }

            case EOF:
                throw new XBParseException("Reading After End", XBProcessingExceptionType.READING_AFTER_END);

            default:
                throw new XBParseException("Unexpected pull item type", XBProcessingExceptionType.UNKNOWN);
        }
    }

    /**
     * Returns actual parsing mode.
     *
     * @return parse mode
     */
    public XBParserState getParseMode() {
        return parserState;
    }

    /**
     * Record for one level of the document path.
     */
    private class BlockPosition {

        public long streamPosition;
        public int blockIndex;
    }
}
