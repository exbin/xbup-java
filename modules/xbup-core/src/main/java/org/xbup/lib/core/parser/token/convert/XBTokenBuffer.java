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
package org.xbup.lib.core.parser.token.convert;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.xbup.lib.core.parser.basic.wrapper.TerminatedDataOutputStreamWrapper;
import org.xbup.lib.core.parser.token.XBAttributeToken;
import org.xbup.lib.core.parser.token.XBBeginToken;
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.parser.token.XBDataToken;
import org.xbup.lib.core.parser.token.XBToken;
import org.xbup.lib.core.parser.token.event.XBEventListener;
import org.xbup.lib.core.ubnumber.type.UBENat32;
import org.xbup.lib.core.ubnumber.type.UBNat32;
import org.xbup.lib.core.util.StreamUtils;

/**
 * Token buffer to store received tokens into memory structure.
 *
 * @version 0.1.25 2015/07/03
 * @author XBUP Project (http://xbup.org)
 */
public class XBTokenBuffer implements XBEventListener {

    private static final int BUFFER_SIZE = 1024;

    private final List<XBToken> tokenList;
    private final List<List<BlockSize>> blockSizes;

    public XBTokenBuffer() {
        tokenList = new ArrayList<>();
        blockSizes = new LinkedList<>();
    }

    /**
     * Writes buffer content into data stream.
     *
     * In the end tokens are disposed.
     *
     * @param stream output stream to write to
     * @throws IOException if writing in stream throws it
     */
    public void write(OutputStream stream) throws IOException {
        precomputeBufferBlockSizes();
        writePrecomputedBlocks(stream);
    }

    /**
     * Precomputes sizes of attribute and data parts for each block into
     * blockSizes list.
     */
    private void precomputeBufferBlockSizes() throws IOException {
        int countingLevel = 0;
        List<BlockSize> bufferLevelSizes = new ArrayList<>();
        List<XBBlockTerminationMode> bufferLevelType = new ArrayList<>();

        int bufferTokenIndex = 0;
        while (bufferTokenIndex < tokenList.size()) {
            XBToken bufferToken = tokenList.get(bufferTokenIndex);
            switch (bufferToken.getTokenType()) {
                case BEGIN: {
                    bufferLevelSizes.add(new BlockSize());
                    bufferLevelType.add(((XBBeginToken) bufferToken).getTerminationMode());
                    countingLevel++;
                    break;
                }

                case ATTRIBUTE: {
                    addBufferLevelAttributePartSize(bufferLevelSizes, ((XBAttributeToken) bufferToken).getAttribute().getSizeUB());
                    break;
                }

                case DATA: {
                    int dataPartSize;
                    InputStream data = ((XBDataToken) bufferToken).getData();
                    ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
                    XBBlockTerminationMode terminationMode = bufferLevelType.get(countingLevel - 1);
                    if (terminationMode == XBBlockTerminationMode.TERMINATED_BY_ZERO) {
                        dataPartSize = copyStreamAndComputeAdjustedSize(data, dataStream);
                    } else {
                        StreamUtils.copyInputStreamToOutputStream(data, dataStream);
                        dataPartSize = dataStream.size();
                    }

                    XBDataToken copiedToken = new XBDataToken(new ByteArrayInputStream(dataStream.toByteArray()));
                    tokenList.set(bufferTokenIndex, copiedToken);
                    addBufferLevelDataPartSize(bufferLevelSizes, dataPartSize);
                    break;
                }

                case END: {
                    countingLevel--;
                    BlockSize blockSize = bufferLevelSizes.remove(countingLevel);
                    while (blockSizes.size() <= countingLevel) {
                        blockSizes.add(new ArrayList<BlockSize>());
                    }

                    blockSizes.get(countingLevel).add(blockSize);
                    if (countingLevel > 0) {
                        UBNat32 attributePartSize = new UBNat32(blockSize.attributePartSize);
                        addBufferLevelDataPartSize(bufferLevelSizes, attributePartSize.getSizeUB());

                        XBBlockTerminationMode terminationMode = bufferLevelType.remove(countingLevel);
                        if (terminationMode == XBBlockTerminationMode.SIZE_SPECIFIED) {
                            UBENat32 dataPartSize = new UBENat32(blockSize.dataPartSize);
                            addBufferLevelDataPartSize(bufferLevelSizes, dataPartSize.getSizeUB());
                        } else {
                            addBufferLevelDataPartSize(bufferLevelSizes, UBENat32.INFINITY_SIZE_UB + 1);
                        }
                    }
                }
            }

            bufferTokenIndex++;
        }
    }

    /**
     * Writes and deletes all buffered tokens using precompute sizes.
     *
     * @param stream output stream
     */
    private void writePrecomputedBlocks(OutputStream stream) throws IOException {
        List<XBBlockTerminationMode> bufferLevelType = new ArrayList<>();
        int level = 0;
        for (XBToken bufferToken : tokenList) {
            switch (bufferToken.getTokenType()) {
                case BEGIN: {
                    BlockSize blockSize = blockSizes.get(level).remove(0);
                    level++;

                    UBENat32 dataPartSize = new UBENat32();
                    bufferLevelType.add(((XBBeginToken) bufferToken).getTerminationMode());
                    if (((XBBeginToken) bufferToken).getTerminationMode() == XBBlockTerminationMode.TERMINATED_BY_ZERO) {
                        dataPartSize.setInfinity();
                    } else {
                        dataPartSize.setValue(blockSize.dataPartSize);
                    }

                    UBNat32 attributePartSize = new UBNat32(blockSize.attributePartSize + dataPartSize.getSizeUB());
                    attributePartSize.toStreamUB(stream);
                    dataPartSize.toStreamUB(stream);
                    break;
                }

                case ATTRIBUTE: {
                    ((XBAttributeToken) bufferToken).getAttribute().toStreamUB(stream);
                    break;
                }

                case DATA: {
                    XBBlockTerminationMode terminationMode = bufferLevelType.get(bufferLevelType.size() - 1);
                    OutputStream streamWrapper;
                    if (terminationMode == XBBlockTerminationMode.TERMINATED_BY_ZERO) {
                        bufferLevelType.set(bufferLevelType.size() - 1, XBBlockTerminationMode.SIZE_SPECIFIED);
                        streamWrapper = new TerminatedDataOutputStreamWrapper(stream);
                    } else {
                        streamWrapper = stream;
                    }

                    StreamUtils.copyInputStreamToOutputStream(((XBDataToken) bufferToken).getData(), streamWrapper);
                    break;
                }

                case END: {
                    XBBlockTerminationMode terminationMode = bufferLevelType.remove(bufferLevelType.size() - 1);
                    if (terminationMode == XBBlockTerminationMode.TERMINATED_BY_ZERO) {
                        stream.write(0);
                    }

                    level--;
                    break;
                }
            }
        }

        tokenList.clear();
        blockSizes.clear();
    }

    /**
     * Stores passed token into token buffer list.
     *
     * @param token passed tokken
     */
    @Override
    public void putXBToken(XBToken token) {
        tokenList.add(token);
    }

    /**
     * Adds value to block sizes accross all depths.
     *
     * @param blockSizes list of block sizes
     * @param attributeSize Value to add to all block sizes
     */
    private void addBufferLevelAttributePartSize(List<BlockSize> blockSizes, int attributeSize) {
        for (int level = 0; level < blockSizes.size(); level++) {
            BlockSize bufferLevelSize = blockSizes.get(level);
            if (level < blockSizes.size() - 1) {
                bufferLevelSize.dataPartSize += attributeSize;
            } else {
                bufferLevelSize.attributePartSize += attributeSize;
            }
        }
    }

    /**
     * Adds value to block sizes accross all depths.
     *
     * @param blockSizes list of block sizes
     * @param dataSize Value to add to all block sizes
     */
    private void addBufferLevelDataPartSize(List<BlockSize> blockSizes, int dataSize) {
        for (BlockSize bufferLevelSize : blockSizes) {
            bufferLevelSize.dataPartSize += dataSize;
        }
    }

    public static int copyStreamAndComputeAdjustedSize(InputStream source, OutputStream target) throws IOException {
        int dataPartSize = 0;
        ZeroCounter zeroCounter = new ZeroCounter();

        byte[] buffer = new byte[BUFFER_SIZE];
        int used = 0;

        while (source.available() > 0) {
            int read = source.read(buffer, used, BUFFER_SIZE - used);
            used += read;
            if (used == BUFFER_SIZE) {
                dataPartSize += computeAdjustedSize(buffer, BUFFER_SIZE, zeroCounter);
                target.write(buffer, 0, BUFFER_SIZE);
                used = 0;
            }
        }

        if (used > 0) {
            dataPartSize += computeAdjustedSize(buffer, used, zeroCounter);
            target.write(buffer, 0, used);
        }

        return dataPartSize + (zeroCounter.zeroCount > 0 ? 3 : 2);
    }

    private static int computeAdjustedSize(byte[] buffer, int size, ZeroCounter zeroCounter) {
        int dataPartSize = 0;
        for (int i = 0; i < size; i++) {
            byte data = buffer[i];
            if (data == 0) {
                if (zeroCounter.zeroCount < 254) {
                    zeroCounter.zeroCount++;
                } else {
                    dataPartSize += 2;
                    zeroCounter.zeroCount = 0;
                }
            } else {
                if (zeroCounter.zeroCount > 0) {
                    dataPartSize += 2;
                    zeroCounter.zeroCount = 0;
                }

                dataPartSize++;
            }
        }

        return dataPartSize;
    }

    private static class ZeroCounter {

        int zeroCount;
    }

    /**
     * Internal structure for block sizes.
     */
    private static class BlockSize {

        int attributePartSize = 0;
        int dataPartSize = 0;
    }
}
