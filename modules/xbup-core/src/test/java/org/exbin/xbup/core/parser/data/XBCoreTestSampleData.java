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
package org.exbin.xbup.core.parser.data;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import junit.framework.Assert;
import org.exbin.xbup.core.block.XBBlock;
import org.exbin.xbup.core.block.XBBlockTerminationMode;
import org.exbin.xbup.core.block.XBDefaultBlock;
import org.exbin.xbup.core.block.XBDefaultDocument;
import org.exbin.xbup.core.block.XBDocument;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.parser.basic.XBListener;
import org.exbin.xbup.core.parser.token.XBAttribute;
import org.exbin.xbup.core.type.XBData;
import org.exbin.xbup.core.ubnumber.type.UBNat32;

/**
 * Sample data and methods for testing purposes.
 *
 * @version 0.2.0 2016/09/25
 * @author ExBin Project (http://exbin.org)
 */
public class XBCoreTestSampleData {

    public final static byte[] NUMBERS_SEQUENCE = {48, 49, 50, 51, 52, 53, 54, 55, 56, 57};
    public final static byte[] NUMBERS_SEQUENCE_REVERSED = {57, 56, 55, 54, 53, 52, 51, 50, 49, 48};

    // Valid files
    public final static String SAMPLE_FILES_PATH = "/org/exbin/xbup/core/resources/test/samples/";
    public final static String SAMPLE_EMPTY = SAMPLE_FILES_PATH + "empty.xb";
    public final static String SAMPLE_BLOCK = SAMPLE_FILES_PATH + "block.xb";
    public final static String SAMPLE_BLOCK_WITH_TAIL = SAMPLE_FILES_PATH + "block_with_tail.xb";
    public final static String SAMPLE_BLOCK_TERMINATED = SAMPLE_FILES_PATH + "block_terminated.xb";
    public final static String SAMPLE_BLOCK_TERMINATED_WITH_TAIL = SAMPLE_FILES_PATH + "block_terminated_with_tail.xb";
    public final static String SAMPLE_DATA = SAMPLE_FILES_PATH + "data.xb";
    public final static String SAMPLE_DATA_WITH_TAIL = SAMPLE_FILES_PATH + "data_with_tail.xb";
    public final static String SAMPLE_DATA_TERMINATED = SAMPLE_FILES_PATH + "data_terminated.xb";
    public final static String SAMPLE_DATA_TERMINATED_WITH_TAIL = SAMPLE_FILES_PATH + "data_terminated_with_tail.xb";
    public final static String SAMPLE_BLOCK_DATA = SAMPLE_FILES_PATH + "block_data.xb";
    public final static String SAMPLE_BLOCK_DATA_WITH_TAIL = SAMPLE_FILES_PATH + "block_data_with_tail.xb";
    public final static String SAMPLE_BLOCK_DATA_TERMINATED = SAMPLE_FILES_PATH + "block_data_terminated.xb";
    public final static String SAMPLE_BLOCK_DATA_TERMINATED_WITH_TAIL = SAMPLE_FILES_PATH + "block_data_terminated_with_tail.xb";
    public final static String SAMPLE_BLOCK_DATA_HYBRID = SAMPLE_FILES_PATH + "block_data_hybrid.xb";
    public final static String SAMPLE_BLOCK_DATA_HYBRID2 = SAMPLE_FILES_PATH + "block_data_hybrid2.xb";
    public final static String SAMPLE_TWO_BLOCKS = SAMPLE_FILES_PATH + "two_blocks.xb";
    public final static String SAMPLE_TWO_BLOCKS_WITH_TAIL = SAMPLE_FILES_PATH + "two_blocks_with_tail.xb";
    public final static String SAMPLE_TWO_BLOCKS_TERMINATED = SAMPLE_FILES_PATH + "two_blocks_terminated.xb";
    public final static String SAMPLE_TWO_BLOCKS_TERMINATED_WITH_TAIL = SAMPLE_FILES_PATH + "two_blocks_terminated_with_tail.xb";
    public final static String SAMPLE_TWO_BLOCKS_HYBRID = SAMPLE_FILES_PATH + "two_blocks_hybrid.xb";
    public final static String SAMPLE_TWO_BLOCKS_HYBRID2 = SAMPLE_FILES_PATH + "two_blocks_hybrid2.xb";
    public final static String SAMPLE_SIX_BLOCKS = SAMPLE_FILES_PATH + "six_blocks.xb";

    // Invalid files
    public final static String CORRUPTED_FILES_PATH = SAMPLE_FILES_PATH + "corrupted/";
    public final static String CORRUPTED_EMPTY = CORRUPTED_FILES_PATH + "empty_file.xb";
    public final static String CORRUPTED_SINGLE_BYTE = CORRUPTED_FILES_PATH + "single_byte.xb";
    public final static String CORRUPTED_WRONG_HEADER = CORRUPTED_FILES_PATH + "wrong_header.xb";
    public final static String CORRUPTED_INCOMPLETE_BLOCK = CORRUPTED_FILES_PATH + "incomplete_block1.xb";
    public final static String CORRUPTED_INCOMPLETE_BLOCK2 = CORRUPTED_FILES_PATH + "incomplete_block2.xb";
    public final static String CORRUPTED_INCOMPLETE_BLOCK_TERMINATED = CORRUPTED_FILES_PATH + "incomplete_block_terminated.xb";
    public final static String CORRUPTED_INCOMPLETE_DATA = CORRUPTED_FILES_PATH + "incomplete_data.xb";
    public final static String CORRUPTED_INCOMPLETE_DATA_TERMINATED = CORRUPTED_FILES_PATH + "incomplete_data_terminated.xb";
    public final static String CORRUPTED_UNEXPECTED_TERMINATOR = CORRUPTED_FILES_PATH + "unexpected_terminator1.xb";
    public final static String CORRUPTED_UNEXPECTED_TERMINATOR2 = CORRUPTED_FILES_PATH + "unexpected_terminator2.xb";
    public final static String CORRUPTED_ATTRIBUTE_OVERFLOW = CORRUPTED_FILES_PATH + "attribute_overflow.xb";
    public final static String CORRUPTED_CHILD_OVERFLOW = CORRUPTED_FILES_PATH + "child_overflow.xb";
    public final static String CORRUPTED_DATA_OVERFLOW = CORRUPTED_FILES_PATH + "data_overflow.xb";

    /**
     * Writes tokens matching file "block.xb".
     *
     * @param listener token listener
     * @throws java.io.IOException if input/output error
     */
    public static void writeSampleEmpty(XBListener listener) throws XBProcessingException, IOException {
        listener.beginXB(XBBlockTerminationMode.SIZE_SPECIFIED);
        listener.dataXB(new ByteArrayInputStream(new byte[0]));
        listener.endXB();
    }

    /**
     * Writes tokens matching file "block.xb".
     *
     * @param listener token listener
     * @throws java.io.IOException if input/output error
     */
    public static void writeSampleBlock(XBListener listener) throws XBProcessingException, IOException {
        listener.beginXB(XBBlockTerminationMode.SIZE_SPECIFIED);
        listener.attribXB(new UBNat32(1));
        listener.endXB();
    }

    /**
     * Writes tokens matching file "block_with_tail.xb".
     *
     * @param listener token listener
     * @throws java.io.IOException if input/output error
     */
    public static void writeSampleBlockWithTail(XBListener listener) throws XBProcessingException, IOException {
        listener.beginXB(XBBlockTerminationMode.SIZE_SPECIFIED);
        listener.attribXB(new UBNat32(1));
        ByteArrayInputStream extDataStream = new ByteArrayInputStream(NUMBERS_SEQUENCE_REVERSED);
        listener.dataXB(extDataStream);
        listener.endXB();
    }

    /**
     * Writes tokens matching file "block_terminated.xb".
     *
     * @param listener token listener
     * @throws java.io.IOException if input/output error
     */
    public static void writeSampleBlockTerminated(XBListener listener) throws XBProcessingException, IOException {
        listener.beginXB(XBBlockTerminationMode.TERMINATED_BY_ZERO);
        listener.attribXB(new UBNat32(1));
        listener.endXB();
    }

    /**
     * Writes tokens matching file "block_terminated_with_tail.xb".
     *
     * @param listener token listener
     * @throws java.io.IOException if input/output error
     */
    public static void writeSampleBlockTerminatedWithTail(XBListener listener) throws XBProcessingException, IOException {
        listener.beginXB(XBBlockTerminationMode.TERMINATED_BY_ZERO);
        listener.attribXB(new UBNat32(1));
        ByteArrayInputStream extDataStream = new ByteArrayInputStream(NUMBERS_SEQUENCE_REVERSED);
        listener.dataXB(extDataStream);
        listener.endXB();
    }

    /**
     * Writes tokens matching file "data.xb".
     *
     * @param listener token listener
     * @throws java.io.IOException if input/output error
     */
    public static void writeSampleData(XBListener listener) throws XBProcessingException, IOException {
        listener.beginXB(XBBlockTerminationMode.SIZE_SPECIFIED);
        ByteArrayInputStream dataStream = new ByteArrayInputStream(NUMBERS_SEQUENCE);
        listener.dataXB(dataStream);
        listener.endXB();
    }

    /**
     * Writes tokens matching file "data_with_tail.xb".
     *
     * @param listener token listener
     * @throws java.io.IOException if input/output error
     */
    public static void writeSampleDataWithTail(XBListener listener) throws XBProcessingException, IOException {
        listener.beginXB(XBBlockTerminationMode.SIZE_SPECIFIED);
        ByteArrayInputStream dataStream = new ByteArrayInputStream(NUMBERS_SEQUENCE);
        listener.dataXB(dataStream);
        ByteArrayInputStream extDataStream = new ByteArrayInputStream(NUMBERS_SEQUENCE_REVERSED);
        listener.dataXB(extDataStream);
        listener.endXB();
    }

    /**
     * Writes tokens matching file "data_terminated.xb".
     *
     * @param listener token listener
     * @throws java.io.IOException if input/output error
     */
    public static void writeSampleDataTerminated(XBListener listener) throws XBProcessingException, IOException {
        listener.beginXB(XBBlockTerminationMode.TERMINATED_BY_ZERO);
        ByteArrayInputStream dataStream = new ByteArrayInputStream(NUMBERS_SEQUENCE);
        listener.dataXB(dataStream);
        listener.endXB();
    }

    /**
     * Writes tokens matching file "data_terminated_with_tail.xb".
     *
     * @param listener token listener
     * @throws java.io.IOException if input/output error
     */
    public static void writeSampleDataTerminatedWithTail(XBListener listener) throws XBProcessingException, IOException {
        listener.beginXB(XBBlockTerminationMode.TERMINATED_BY_ZERO);
        ByteArrayInputStream dataStream = new ByteArrayInputStream(NUMBERS_SEQUENCE);
        listener.dataXB(dataStream);
        ByteArrayInputStream extDataStream = new ByteArrayInputStream(NUMBERS_SEQUENCE_REVERSED);
        listener.dataXB(extDataStream);
        listener.endXB();
    }

    /**
     * Writes tokens matching file "block_data.xb".
     *
     * @param listener token listener
     * @throws java.io.IOException if input/output error
     */
    public static void writeSampleBlockData(XBListener listener) throws XBProcessingException, IOException {
        listener.beginXB(XBBlockTerminationMode.SIZE_SPECIFIED);
        listener.attribXB(new UBNat32(1));
        listener.beginXB(XBBlockTerminationMode.SIZE_SPECIFIED);
        ByteArrayInputStream dataStream = new ByteArrayInputStream(NUMBERS_SEQUENCE);
        listener.dataXB(dataStream);
        listener.endXB();
        listener.endXB();
    }

    /**
     * Writes tokens matching file "block_data_with_tail.xb".
     *
     * @param listener token listener
     * @throws java.io.IOException if input/output error
     */
    public static void writeSampleBlockDataWithTail(XBListener listener) throws XBProcessingException, IOException {
        listener.beginXB(XBBlockTerminationMode.SIZE_SPECIFIED);
        listener.attribXB(new UBNat32(1));
        listener.beginXB(XBBlockTerminationMode.SIZE_SPECIFIED);
        ByteArrayInputStream dataStream = new ByteArrayInputStream(NUMBERS_SEQUENCE);
        listener.dataXB(dataStream);
        listener.endXB();
        ByteArrayInputStream extDataStream = new ByteArrayInputStream(NUMBERS_SEQUENCE_REVERSED);
        listener.dataXB(extDataStream);
        listener.endXB();
    }

    /**
     * Writes tokens matching file "block_data_terminated.xb".
     *
     * @param listener token listener
     * @throws java.io.IOException if input/output error
     */
    public static void writeSampleBlockDataTerminated(XBListener listener) throws XBProcessingException, IOException {
        listener.beginXB(XBBlockTerminationMode.TERMINATED_BY_ZERO);
        listener.attribXB(new UBNat32(1));
        listener.beginXB(XBBlockTerminationMode.TERMINATED_BY_ZERO);
        ByteArrayInputStream dataStream = new ByteArrayInputStream(NUMBERS_SEQUENCE);
        listener.dataXB(dataStream);
        listener.endXB();
        listener.endXB();
    }

    /**
     * Writes tokens matching file "block_data_terminated_with_tail.xb".
     *
     * @param listener token listener
     * @throws java.io.IOException if input/output error
     */
    public static void writeSampleBlockDataTerminatedWithTail(XBListener listener) throws XBProcessingException, IOException {
        listener.beginXB(XBBlockTerminationMode.TERMINATED_BY_ZERO);
        listener.attribXB(new UBNat32(1));
        listener.beginXB(XBBlockTerminationMode.TERMINATED_BY_ZERO);
        ByteArrayInputStream dataStream = new ByteArrayInputStream(NUMBERS_SEQUENCE);
        listener.dataXB(dataStream);
        listener.endXB();
        ByteArrayInputStream extDataStream = new ByteArrayInputStream(NUMBERS_SEQUENCE_REVERSED);
        listener.dataXB(extDataStream);
        listener.endXB();
    }

    /**
     * Writes tokens matching file "block_data_hybrid.xb".
     *
     * @param listener token listener
     * @throws java.io.IOException if input/output error
     */
    public static void writeSampleBlockDataHybrid(XBListener listener) throws XBProcessingException, IOException {
        listener.beginXB(XBBlockTerminationMode.TERMINATED_BY_ZERO);
        listener.attribXB(new UBNat32(1));
        listener.beginXB(XBBlockTerminationMode.SIZE_SPECIFIED);
        ByteArrayInputStream dataStream = new ByteArrayInputStream(NUMBERS_SEQUENCE);
        listener.dataXB(dataStream);
        listener.endXB();
        listener.endXB();
    }

    /**
     * Writes tokens matching file "block_data_hybrid2.xb".
     *
     * @param listener token listener
     * @throws java.io.IOException if input/output error
     */
    public static void writeSampleBlockDataHybrid2(XBListener listener) throws XBProcessingException, IOException {
        listener.beginXB(XBBlockTerminationMode.SIZE_SPECIFIED);
        listener.attribXB(new UBNat32(1));
        listener.beginXB(XBBlockTerminationMode.TERMINATED_BY_ZERO);
        ByteArrayInputStream dataStream = new ByteArrayInputStream(NUMBERS_SEQUENCE);
        listener.dataXB(dataStream);
        listener.endXB();
        listener.endXB();
    }

    /**
     * Writes tokens matching file "two_blocks.xb".
     *
     * @param listener token listener
     * @throws java.io.IOException if input/output error
     */
    public static void writeSampleTwoBlocks(XBListener listener) throws XBProcessingException, IOException {
        listener.beginXB(XBBlockTerminationMode.SIZE_SPECIFIED);
        listener.attribXB(new UBNat32(1));
        listener.beginXB(XBBlockTerminationMode.SIZE_SPECIFIED);
        listener.attribXB(new UBNat32(2));
        listener.endXB();
        listener.endXB();
    }

    /**
     * Writes tokens matching file "two_blocks_with_tail.xb".
     *
     * @param listener token listener
     * @throws java.io.IOException if input/output error
     */
    public static void writeSampleTwoBlocksWithTail(XBListener listener) throws XBProcessingException, IOException {
        listener.beginXB(XBBlockTerminationMode.SIZE_SPECIFIED);
        listener.attribXB(new UBNat32(1));
        listener.beginXB(XBBlockTerminationMode.SIZE_SPECIFIED);
        listener.attribXB(new UBNat32(2));
        listener.endXB();
        ByteArrayInputStream extDataStream = new ByteArrayInputStream(NUMBERS_SEQUENCE_REVERSED);
        listener.dataXB(extDataStream);
        listener.endXB();
    }

    /**
     * Writes tokens matching file "two_blocks_terminated.xb".
     *
     * @param listener token listener
     * @throws java.io.IOException if input/output error
     */
    public static void writeSampleTwoBlocksTerminated(XBListener listener) throws XBProcessingException, IOException {
        listener.beginXB(XBBlockTerminationMode.TERMINATED_BY_ZERO);
        listener.attribXB(new UBNat32(1));
        listener.beginXB(XBBlockTerminationMode.TERMINATED_BY_ZERO);
        listener.attribXB(new UBNat32(2));
        listener.endXB();
        listener.endXB();
    }

    /**
     * Writes tokens matching file "two_blocks_terminated_with_tail.xb".
     *
     * @param listener token listener
     * @throws java.io.IOException if input/output error
     */
    public static void writeSampleTwoBlocksTerminatedWithTail(XBListener listener) throws XBProcessingException, IOException {
        listener.beginXB(XBBlockTerminationMode.TERMINATED_BY_ZERO);
        listener.attribXB(new UBNat32(1));
        listener.beginXB(XBBlockTerminationMode.TERMINATED_BY_ZERO);
        listener.attribXB(new UBNat32(2));
        listener.endXB();
        ByteArrayInputStream extDataStream = new ByteArrayInputStream(NUMBERS_SEQUENCE_REVERSED);
        listener.dataXB(extDataStream);
        listener.endXB();
    }

    /**
     * Writes tokens matching file "two_blocks_hybrid.xb".
     *
     * @param listener token listener
     * @throws java.io.IOException if input/output error
     */
    public static void writeSampleTwoBlocksHybrid(XBListener listener) throws XBProcessingException, IOException {
        listener.beginXB(XBBlockTerminationMode.TERMINATED_BY_ZERO);
        listener.attribXB(new UBNat32(1));
        listener.beginXB(XBBlockTerminationMode.SIZE_SPECIFIED);
        listener.attribXB(new UBNat32(2));
        listener.endXB();
        listener.endXB();
    }

    /**
     * Writes tokens matching file "two_blocks_hybrid2.xb".
     *
     * @param listener token listener
     * @throws java.io.IOException if input/output error
     */
    public static void writeSampleTwoBlocksHybrid2(XBListener listener) throws XBProcessingException, IOException {
        listener.beginXB(XBBlockTerminationMode.SIZE_SPECIFIED);
        listener.attribXB(new UBNat32(1));
        listener.beginXB(XBBlockTerminationMode.TERMINATED_BY_ZERO);
        listener.attribXB(new UBNat32(2));
        listener.endXB();
        listener.endXB();
    }

    /**
     * Writes tokens matching file "six_blocks.xb".
     *
     * @param listener token listener
     * @throws java.io.IOException if input/output error
     */
    public static void writeSampleSixBlocks(XBListener listener) throws XBProcessingException, IOException {
        listener.beginXB(XBBlockTerminationMode.SIZE_SPECIFIED);
        listener.attribXB(new UBNat32(0));
        listener.beginXB(XBBlockTerminationMode.SIZE_SPECIFIED);
        listener.attribXB(new UBNat32(0));
        listener.beginXB(XBBlockTerminationMode.SIZE_SPECIFIED);
        listener.attribXB(new UBNat32(0));
        listener.beginXB(XBBlockTerminationMode.SIZE_SPECIFIED);
        ByteArrayInputStream dataStream = new ByteArrayInputStream(NUMBERS_SEQUENCE);
        listener.dataXB(dataStream);
        listener.endXB();
        listener.endXB();
        listener.endXB();
        listener.beginXB(XBBlockTerminationMode.SIZE_SPECIFIED);
        listener.attribXB(new UBNat32(0));
        listener.beginXB(XBBlockTerminationMode.SIZE_SPECIFIED);
        listener.attribXB(new UBNat32(0));
        listener.attribXB(new UBNat32(0));
        listener.attribXB(new UBNat32(1));
        listener.attribXB(new UBNat32(2));
        listener.attribXB(new UBNat32(3));
        listener.endXB();
        listener.endXB();
        listener.endXB();
    }

    /**
     * Writes invalid sequence of tokens.
     *
     * @param listener token listener
     * @throws java.io.IOException if input/output error
     */
    public static void writeCorruptedEndWithoutData(XBListener listener) throws XBProcessingException, IOException {
        listener.beginXB(XBBlockTerminationMode.SIZE_SPECIFIED);
        listener.endXB();
    }

    /**
     * Writes invalid sequence of tokens.
     *
     * @param listener token listener
     * @throws java.io.IOException if input/output error
     */
    public static void writeCorruptedDataAttributeMismatch(XBListener listener) throws XBProcessingException, IOException {
        listener.beginXB(XBBlockTerminationMode.SIZE_SPECIFIED);
        listener.dataXB(new XBData().getDataInputStream());
        listener.attribXB(new UBNat32(0));
        listener.endXB();
    }

    /**
     * Writes invalid sequence of tokens.
     *
     * @param listener token listener
     * @throws java.io.IOException if input/output error
     */
    public static void writeCorruptedDataAttributeMismatch2(XBListener listener) throws XBProcessingException, IOException {
        listener.beginXB(XBBlockTerminationMode.SIZE_SPECIFIED);
        listener.attribXB(new UBNat32(0));
        listener.beginXB(XBBlockTerminationMode.SIZE_SPECIFIED);
        listener.attribXB(new UBNat32(0));
        listener.dataXB(new XBData().getDataInputStream());
        listener.endXB();
        listener.endXB();
    }

    /**
     * Writes invalid sequence of tokens.
     *
     * @param listener token listener
     * @throws java.io.IOException if input/output error
     */
    public static void writeCorruptedAttributeAfterEnd(XBListener listener) throws XBProcessingException, IOException {
        listener.beginXB(XBBlockTerminationMode.SIZE_SPECIFIED);
        listener.beginXB(XBBlockTerminationMode.SIZE_SPECIFIED);
        listener.attribXB(new UBNat32(0));
        listener.endXB();
        listener.attribXB(new UBNat32(0));
        listener.endXB();
    }

    /**
     * Returns block structure matching file "block.xb".
     *
     * @return tree structure
     */
    public static XBDocument getSampleEmptyTree() {
        return new XBDefaultDocument(new XBDefaultBlock(XBBlockTerminationMode.SIZE_SPECIFIED, new XBData()));
    }

    /**
     * Returns block structure matching file "block.xb".
     *
     * @return tree structure
     */
    public static XBDocument getSampleBlockTree() {
        return new XBDefaultDocument(new XBDefaultBlock(XBBlockTerminationMode.SIZE_SPECIFIED, new XBAttribute[]{new UBNat32(1)}, new XBBlock[]{}));
    }

    /**
     * Returns block structure matching file "block_with_tail.xb".
     *
     * @return tree structure
     */
    public static XBDocument getSampleBlockWithTailTree() {
        XBData tailData = new XBData();
        try {
            tailData.loadFromStream(new ByteArrayInputStream(NUMBERS_SEQUENCE_REVERSED));
        } catch (IOException ex) {
            Assert.fail();
        }
        return new XBDefaultDocument(
                new XBDefaultBlock(XBBlockTerminationMode.SIZE_SPECIFIED, new XBAttribute[]{new UBNat32(1)}, new XBBlock[]{}),
                tailData);
    }

    /**
     * Returns block structure matching file "block_terminated.xb".
     *
     * @return tree structure
     */
    public static XBDocument getSampleBlockTerminatedTree() {
        return new XBDefaultDocument(new XBDefaultBlock(XBBlockTerminationMode.TERMINATED_BY_ZERO, new XBAttribute[]{new UBNat32(1)}, new XBBlock[]{}));
    }

    /**
     * Returns block structure matching file "block_terminated_with_tail.xb".
     *
     * @return tree structure
     */
    public static XBDocument getSampleBlockTerminatedWithTailTree() {
        XBData tailData = new XBData();
        try {
            tailData.loadFromStream(new ByteArrayInputStream(NUMBERS_SEQUENCE_REVERSED));
        } catch (IOException ex) {
            Assert.fail();
        }
        return new XBDefaultDocument(
                new XBDefaultBlock(XBBlockTerminationMode.TERMINATED_BY_ZERO, new XBAttribute[]{new UBNat32(1)}, new XBBlock[]{}),
                tailData);
    }

    /**
     * Returns block structure matching file "data.xb".
     *
     * @return tree structure
     */
    public static XBDocument getSampleDataTree() {
        XBData data = new XBData();
        try {
            data.loadFromStream(new ByteArrayInputStream(NUMBERS_SEQUENCE));
        } catch (IOException ex) {
            Assert.fail();
        }
        return new XBDefaultDocument(new XBDefaultBlock(XBBlockTerminationMode.SIZE_SPECIFIED, data));
    }

    /**
     * Returns block structure matching file "data_with_tail.xb".
     *
     * @return tree structure
     */
    public static XBDocument getSampleDataWithTailTree() {
        XBData tailData = new XBData();
        try {
            tailData.loadFromStream(new ByteArrayInputStream(NUMBERS_SEQUENCE_REVERSED));
        } catch (IOException ex) {
            Assert.fail();
        }
        XBData data = new XBData();
        try {
            data.loadFromStream(new ByteArrayInputStream(NUMBERS_SEQUENCE));
        } catch (IOException ex) {
            Assert.fail();
        }
        return new XBDefaultDocument(new XBDefaultBlock(XBBlockTerminationMode.SIZE_SPECIFIED, data), tailData);
    }

    /**
     * Returns block structure matching file "data_terminated.xb".
     *
     * @return tree structure
     */
    public static XBDocument getSampleDataTerminatedTree() {
        XBData data = new XBData();
        try {
            data.loadFromStream(new ByteArrayInputStream(NUMBERS_SEQUENCE));
        } catch (IOException ex) {
            Assert.fail();
        }
        return new XBDefaultDocument(new XBDefaultBlock(XBBlockTerminationMode.TERMINATED_BY_ZERO, data));
    }

    /**
     * Returns block structure matching file "data_terminated.xb".
     *
     * @return tree structure
     */
    public static XBDocument getSampleDataTerminatedWithTailTree() {
        XBData tailData = new XBData();
        try {
            tailData.loadFromStream(new ByteArrayInputStream(NUMBERS_SEQUENCE_REVERSED));
        } catch (IOException ex) {
            Assert.fail();
        }
        XBData data = new XBData();
        try {
            data.loadFromStream(new ByteArrayInputStream(NUMBERS_SEQUENCE));
        } catch (IOException ex) {
            Assert.fail();
        }
        return new XBDefaultDocument(new XBDefaultBlock(XBBlockTerminationMode.TERMINATED_BY_ZERO, data), tailData);
    }

    /**
     * Returns block structure matching file "block_data.xb".
     *
     * @return tree structure
     */
    public static XBDocument getSampleBlockDataTree() {
        XBData data = new XBData();
        try {
            data.loadFromStream(new ByteArrayInputStream(NUMBERS_SEQUENCE));
        } catch (IOException ex) {
            Assert.fail();
        }
        return new XBDefaultDocument(new XBDefaultBlock(XBBlockTerminationMode.SIZE_SPECIFIED, new XBAttribute[]{new UBNat32(1)}, new XBBlock[]{
            new XBDefaultBlock(XBBlockTerminationMode.SIZE_SPECIFIED, data)
        }));
    }

    /**
     * Returns block structure matching file "block_data_with_tail.xb".
     *
     * @return tree structure
     */
    public static XBDocument getSampleBlockDataWithTailTree() {
        XBData data = new XBData();
        try {
            data.loadFromStream(new ByteArrayInputStream(NUMBERS_SEQUENCE));
        } catch (IOException ex) {
            Assert.fail();
        }
        XBData tailData = new XBData();
        try {
            tailData.loadFromStream(new ByteArrayInputStream(NUMBERS_SEQUENCE_REVERSED));
        } catch (IOException ex) {
            Assert.fail();
        }
        return new XBDefaultDocument(
                new XBDefaultBlock(XBBlockTerminationMode.SIZE_SPECIFIED, new XBAttribute[]{new UBNat32(1)}, new XBBlock[]{
            new XBDefaultBlock(XBBlockTerminationMode.SIZE_SPECIFIED, data)
        }),
                tailData);
    }

    /**
     * Returns block structure matching file "block_data_terminated.xb".
     *
     * @return tree structure
     */
    public static XBDocument getSampleBlockDataTerminatedTree() {
        XBData data = new XBData();
        try {
            data.loadFromStream(new ByteArrayInputStream(NUMBERS_SEQUENCE));
        } catch (IOException ex) {
            Assert.fail();
        }
        return new XBDefaultDocument(new XBDefaultBlock(XBBlockTerminationMode.TERMINATED_BY_ZERO, new XBAttribute[]{new UBNat32(1)}, new XBBlock[]{
            new XBDefaultBlock(XBBlockTerminationMode.TERMINATED_BY_ZERO, data)
        }));
    }

    /**
     * Returns block structure matching file
     * "block_data_terminated_with_tail.xb".
     *
     * @return tree structure
     */
    public static XBDocument getSampleBlockDataTerminatedWithTailTree() {
        XBData data = new XBData();
        try {
            data.loadFromStream(new ByteArrayInputStream(NUMBERS_SEQUENCE));
        } catch (IOException ex) {
            Assert.fail();
        }
        XBData tailData = new XBData();
        try {
            tailData.loadFromStream(new ByteArrayInputStream(NUMBERS_SEQUENCE_REVERSED));
        } catch (IOException ex) {
            Assert.fail();
        }
        return new XBDefaultDocument(
                new XBDefaultBlock(XBBlockTerminationMode.TERMINATED_BY_ZERO, new XBAttribute[]{new UBNat32(1)}, new XBBlock[]{
            new XBDefaultBlock(XBBlockTerminationMode.TERMINATED_BY_ZERO, data)
        }),
                tailData);
    }

    /**
     * Returns block structure matching file "block_data_hybrid.xb".
     *
     * @return tree structure
     */
    public static XBDocument getSampleBlockDataHybridTree() {
        XBData data = new XBData();
        try {
            data.loadFromStream(new ByteArrayInputStream(NUMBERS_SEQUENCE));
        } catch (IOException ex) {
            Assert.fail();
        }
        return new XBDefaultDocument(new XBDefaultBlock(XBBlockTerminationMode.TERMINATED_BY_ZERO, new XBAttribute[]{new UBNat32(1)}, new XBBlock[]{
            new XBDefaultBlock(XBBlockTerminationMode.SIZE_SPECIFIED, data)
        }));
    }

    /**
     * Returns block structure matching file "block_data_hybrid2.xb".
     *
     * @return tree structure
     */
    public static XBDocument getSampleBlockDataHybrid2Tree() {
        XBData data = new XBData();
        try {
            data.loadFromStream(new ByteArrayInputStream(NUMBERS_SEQUENCE));
        } catch (IOException ex) {
            Assert.fail();
        }
        return new XBDefaultDocument(new XBDefaultBlock(XBBlockTerminationMode.SIZE_SPECIFIED, new XBAttribute[]{new UBNat32(1)}, new XBBlock[]{
            new XBDefaultBlock(XBBlockTerminationMode.TERMINATED_BY_ZERO, data)
        }));
    }

    /**
     * Returns block structure matching file "two_blocks.xb".
     *
     * @return tree structure
     */
    public static XBDocument getSampleTwoBlocksTree() {
        return new XBDefaultDocument(new XBDefaultBlock(XBBlockTerminationMode.SIZE_SPECIFIED, new XBAttribute[]{new UBNat32(1)}, new XBBlock[]{
            new XBDefaultBlock(XBBlockTerminationMode.SIZE_SPECIFIED, new XBAttribute[]{new UBNat32(2)}, new XBBlock[]{})
        }));
    }

    /**
     * Returns block structure matching file "two_blocks_with_tail.xb".
     *
     * @return tree structure
     */
    public static XBDocument getSampleTwoBlocksWithTailTree() {
        XBData tailData = new XBData();
        try {
            tailData.loadFromStream(new ByteArrayInputStream(NUMBERS_SEQUENCE_REVERSED));
        } catch (IOException ex) {
            Assert.fail();
        }
        return new XBDefaultDocument(new XBDefaultBlock(XBBlockTerminationMode.SIZE_SPECIFIED, new XBAttribute[]{new UBNat32(1)}, new XBBlock[]{
            new XBDefaultBlock(XBBlockTerminationMode.SIZE_SPECIFIED, new XBAttribute[]{new UBNat32(2)}, new XBBlock[]{})
        }), tailData);
    }

    /**
     * Returns block structure matching file "two_blocks_terminated.xb".
     *
     * @return tree structure
     */
    public static XBDocument getSampleTwoBlocksTerminatedTree() {
        return new XBDefaultDocument(new XBDefaultBlock(XBBlockTerminationMode.TERMINATED_BY_ZERO, new XBAttribute[]{new UBNat32(1)}, new XBBlock[]{
            new XBDefaultBlock(XBBlockTerminationMode.TERMINATED_BY_ZERO, new XBAttribute[]{new UBNat32(2)}, new XBBlock[]{})
        }));
    }

    /**
     * Returns block structure matching file
     * "two_blocks_terminated_with_tail.xb".
     *
     * @return tree structure
     */
    public static XBDocument getSampleTwoBlocksTerminatedWithTailTree() {
        XBData tailData = new XBData();
        try {
            tailData.loadFromStream(new ByteArrayInputStream(NUMBERS_SEQUENCE_REVERSED));
        } catch (IOException ex) {
            Assert.fail();
        }
        return new XBDefaultDocument(new XBDefaultBlock(XBBlockTerminationMode.TERMINATED_BY_ZERO, new XBAttribute[]{new UBNat32(1)}, new XBBlock[]{
            new XBDefaultBlock(XBBlockTerminationMode.TERMINATED_BY_ZERO, new XBAttribute[]{new UBNat32(2)}, new XBBlock[]{})
        }), tailData);
    }

    /**
     * Returns block structure matching file "two_blocks_hybrid.xb".
     *
     * @return tree structure
     */
    public static XBDocument getSampleTwoBlocksHybridTree() {
        return new XBDefaultDocument(new XBDefaultBlock(XBBlockTerminationMode.TERMINATED_BY_ZERO, new XBAttribute[]{new UBNat32(1)}, new XBBlock[]{
            new XBDefaultBlock(XBBlockTerminationMode.SIZE_SPECIFIED, new XBAttribute[]{new UBNat32(2)}, new XBBlock[]{})
        }));
    }

    /**
     * Returns block structure matching file "two_blocks_hybrid2.xb".
     *
     * @return tree structure
     */
    public static XBDocument getSampleTwoBlocksHybrid2Tree() {
        return new XBDefaultDocument(new XBDefaultBlock(XBBlockTerminationMode.SIZE_SPECIFIED, new XBAttribute[]{new UBNat32(1)}, new XBBlock[]{
            new XBDefaultBlock(XBBlockTerminationMode.TERMINATED_BY_ZERO, new XBAttribute[]{new UBNat32(2)}, new XBBlock[]{})
        }));
    }

    /**
     * Returns block structure matching file "six_blocks.xb".
     *
     * @return tree structure
     */
    public static XBDocument getSampleSixBlocksTree() {
        XBData data = new XBData();
        try {
            data.loadFromStream(new ByteArrayInputStream(NUMBERS_SEQUENCE));
        } catch (IOException ex) {
            Assert.fail();
        }
        return new XBDefaultDocument(new XBDefaultBlock(XBBlockTerminationMode.SIZE_SPECIFIED, new XBAttribute[]{new UBNat32(0)}, new XBBlock[]{
            new XBDefaultBlock(XBBlockTerminationMode.SIZE_SPECIFIED, new XBAttribute[]{new UBNat32(0)}, new XBBlock[]{
                new XBDefaultBlock(XBBlockTerminationMode.SIZE_SPECIFIED, new XBAttribute[]{new UBNat32(0)}, new XBBlock[]{
                    new XBDefaultBlock(XBBlockTerminationMode.SIZE_SPECIFIED, data)
                })
            }),
            new XBDefaultBlock(XBBlockTerminationMode.SIZE_SPECIFIED, new XBAttribute[]{new UBNat32(0)}, new XBBlock[]{
                new XBDefaultBlock(XBBlockTerminationMode.SIZE_SPECIFIED, new XBAttribute[]{new UBNat32(0), new UBNat32(0), new UBNat32(1), new UBNat32(2), new UBNat32(3)}, new XBBlock[]{})
            })
        }));
    }
}
