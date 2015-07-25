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
package org.xbup.lib.core.parser.data;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.xbup.lib.core.block.XBBlock;
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.block.XBDefaultBlock;
import org.xbup.lib.core.block.XBDefaultDocument;
import org.xbup.lib.core.block.XBDocument;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.basic.XBListener;
import org.xbup.lib.core.parser.token.XBAttribute;
import org.xbup.lib.core.type.XBData;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * Sample data and methods for testing purposes.
 *
 * @version 0.1.25 2015/07/24
 * @author XBUP Project (http://xbup.org)
 */
public class XBCoreTestSampleData {

    public final static byte[] NUMBERS_SEQUENCE = {48, 49, 50, 51, 52, 53, 54, 55, 56, 57};
    public final static byte[] NUMBERS_SEQUENCE_REVERSED = {57, 56, 55, 54, 53, 52, 51, 50, 49, 48};

    // Valid files
    public final static String SAMPLE_FILES_PATH = "/org/xbup/lib/core/resources/test/samples/";
    public final static String SAMPLE_EMPTY = SAMPLE_FILES_PATH + "empty.xb";
    public final static String SAMPLE_BLOCK = SAMPLE_FILES_PATH + "block.xb";
    public final static String SAMPLE_BLOCK_EXTENDED = SAMPLE_FILES_PATH + "block_extended.xb";
    public final static String SAMPLE_BLOCK_TERMINATED = SAMPLE_FILES_PATH + "block_terminated.xb";
    public final static String SAMPLE_BLOCK_TERMINATED_EXTENDED = SAMPLE_FILES_PATH + "block_terminated_extended.xb";
    public final static String SAMPLE_DATA = SAMPLE_FILES_PATH + "data.xb";
    public final static String SAMPLE_DATA_EXTENDED = SAMPLE_FILES_PATH + "data_extended.xb";
    public final static String SAMPLE_DATA_TERMINATED = SAMPLE_FILES_PATH + "data_terminated.xb";
    public final static String SAMPLE_DATA_TERMINATED_EXTENDED = SAMPLE_FILES_PATH + "data_terminated_extended.xb";
    public final static String SAMPLE_TWO_BLOCKS = SAMPLE_FILES_PATH + "two_blocks.xb";
    public final static String SAMPLE_TWO_BLOCKS_TERMINATED = SAMPLE_FILES_PATH + "two_blocks_terminated.xb";
    public final static String SAMPLE_TWO_BLOCKS_EXTENDED = SAMPLE_FILES_PATH + "two_blocks_extended.xb";
    public final static String SAMPLE_TWO_BLOCKS_HYBRID = SAMPLE_FILES_PATH + "two_blocks_hybrid.xb";
    public final static String SAMPLE_TWO_BLOCKS_HYBRID2 = SAMPLE_FILES_PATH + "two_blocks_hybrid2.xb";
    public final static String SAMPLE_SIX_BLOCKS = SAMPLE_FILES_PATH + "six_blocks.xb";

    // Invalid files
    public final static String CORRUPTED_FILES_PATH = SAMPLE_FILES_PATH + "corrupted/";
    public final static String CORRUPTED_EMPTY = CORRUPTED_FILES_PATH + "empty_file.xb";
    public final static String CORRUPTED_SINGLE_BYTE = CORRUPTED_FILES_PATH + "single_byte.xb";
    public final static String CORRUPTED_WRONG_HEADER = CORRUPTED_FILES_PATH + "wrong_header.xb";
    public final static String CORRUPTED_CHILD_OVERFLOW = CORRUPTED_FILES_PATH + "child_overflow.xb";
    public final static String CORRUPTED_MISSING_END = CORRUPTED_FILES_PATH + "missing_end.xb";

    /**
     * Writes tokens matching file "block.xb".
     *
     * @param listener token listener
     * @throws java.io.IOException
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
     * @throws java.io.IOException
     */
    public static void writeSampleBlock(XBListener listener) throws XBProcessingException, IOException {
        listener.beginXB(XBBlockTerminationMode.SIZE_SPECIFIED);
        listener.attribXB(new UBNat32(1));
        listener.endXB();
    }

    /**
     * Writes tokens matching file "block_extended.xb".
     *
     * @param listener token listener
     * @throws java.io.IOException
     */
    public static void writeSampleBlockExtended(XBListener listener) throws XBProcessingException, IOException {
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
     * @throws java.io.IOException
     */
    public static void writeSampleBlockTerminated(XBListener listener) throws XBProcessingException, IOException {
        listener.beginXB(XBBlockTerminationMode.TERMINATED_BY_ZERO);
        listener.attribXB(new UBNat32(1));
        listener.endXB();
    }

    /**
     * Writes tokens matching file "block_terminated.xb".
     *
     * @param listener token listener
     * @throws java.io.IOException
     */
    public static void writeSampleBlockTerminatedExtended(XBListener listener) throws XBProcessingException, IOException {
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
     * @throws java.io.IOException
     */
    public static void writeSampleData(XBListener listener) throws XBProcessingException, IOException {
        listener.beginXB(XBBlockTerminationMode.SIZE_SPECIFIED);
        ByteArrayInputStream dataStream = new ByteArrayInputStream(NUMBERS_SEQUENCE);
        listener.dataXB(dataStream);
        listener.endXB();
    }

    /**
     * Writes tokens matching file "data_extended.xb".
     *
     * @param listener token listener
     * @throws java.io.IOException
     */
    public static void writeSampleDataExtended(XBListener listener) throws XBProcessingException, IOException {
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
     * @throws java.io.IOException
     */
    public static void writeSampleDataTerminated(XBListener listener) throws XBProcessingException, IOException {
        listener.beginXB(XBBlockTerminationMode.TERMINATED_BY_ZERO);
        ByteArrayInputStream dataStream = new ByteArrayInputStream(NUMBERS_SEQUENCE);
        listener.dataXB(dataStream);
        listener.endXB();
    }

    /**
     * Writes tokens matching file "data_terminated.xb".
     *
     * @param listener token listener
     * @throws java.io.IOException
     */
    public static void writeSampleDataTerminatedExtended(XBListener listener) throws XBProcessingException, IOException {
        listener.beginXB(XBBlockTerminationMode.TERMINATED_BY_ZERO);
        ByteArrayInputStream dataStream = new ByteArrayInputStream(NUMBERS_SEQUENCE);
        listener.dataXB(dataStream);
        ByteArrayInputStream extDataStream = new ByteArrayInputStream(NUMBERS_SEQUENCE_REVERSED);
        listener.dataXB(extDataStream);
        listener.endXB();
    }

    /**
     * Writes tokens matching file "two_blocks.xb".
     *
     * @param listener token listener
     * @throws java.io.IOException
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
     * Writes tokens matching file "two_blocks_extended.xb".
     *
     * @param listener token listener
     * @throws java.io.IOException
     */
    public static void writeSampleTwoBlocksExtended(XBListener listener) throws XBProcessingException, IOException {
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
     * @throws java.io.IOException
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
     * Writes tokens matching file "two_blocks_hybrid.xb".
     *
     * @param listener token listener
     * @throws java.io.IOException
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
     * @throws java.io.IOException
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
     * @throws java.io.IOException
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
     * Returns block structure matching file "block_extended.xb".
     *
     * @return tree structure
     */
    public static XBDocument getSampleBlockExtendedTree() {
        XBData extendedArea = new XBData();
        extendedArea.loadFromStream(new ByteArrayInputStream(NUMBERS_SEQUENCE_REVERSED));
        return new XBDefaultDocument(
                new XBDefaultBlock(XBBlockTerminationMode.SIZE_SPECIFIED, new XBAttribute[]{new UBNat32(1)}, new XBBlock[]{}),
                extendedArea);
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
     * Returns block structure matching file "block_terminated.xb".
     *
     * @return tree structure
     */
    public static XBDocument getSampleBlockTerminatedExtendedTree() {
        XBData extendedArea = new XBData();
        extendedArea.loadFromStream(new ByteArrayInputStream(NUMBERS_SEQUENCE_REVERSED));
        return new XBDefaultDocument(
                new XBDefaultBlock(XBBlockTerminationMode.TERMINATED_BY_ZERO, new XBAttribute[]{new UBNat32(1)}, new XBBlock[]{}),
                extendedArea);
    }

    /**
     * Returns block structure matching file "data.xb".
     *
     * @return tree structure
     */
    public static XBDocument getSampleDataTree() {
        XBData data = new XBData();
        data.loadFromStream(new ByteArrayInputStream(NUMBERS_SEQUENCE));
        return new XBDefaultDocument(new XBDefaultBlock(XBBlockTerminationMode.SIZE_SPECIFIED, data));
    }

    /**
     * Returns block structure matching file "data_extended.xb".
     *
     * @return tree structure
     */
    public static XBDocument getSampleDataExtendedTree() {
        XBData extendedArea = new XBData();
        extendedArea.loadFromStream(new ByteArrayInputStream(NUMBERS_SEQUENCE_REVERSED));
        XBData data = new XBData();
        data.loadFromStream(new ByteArrayInputStream(NUMBERS_SEQUENCE));
        return new XBDefaultDocument(new XBDefaultBlock(XBBlockTerminationMode.SIZE_SPECIFIED, data), extendedArea);
    }

    /**
     * Returns block structure matching file "data_terminated.xb".
     *
     * @return tree structure
     */
    public static XBDocument getSampleDataTerminatedTree() {
        XBData data = new XBData();
        data.loadFromStream(new ByteArrayInputStream(NUMBERS_SEQUENCE));
        return new XBDefaultDocument(new XBDefaultBlock(XBBlockTerminationMode.TERMINATED_BY_ZERO, data));
    }

    /**
     * Returns block structure matching file "data_terminated.xb".
     *
     * @return tree structure
     */
    public static XBDocument getSampleDataTerminatedExtendedTree() {
        XBData extendedArea = new XBData();
        extendedArea.loadFromStream(new ByteArrayInputStream(NUMBERS_SEQUENCE_REVERSED));
        XBData data = new XBData();
        data.loadFromStream(new ByteArrayInputStream(NUMBERS_SEQUENCE));
        return new XBDefaultDocument(new XBDefaultBlock(XBBlockTerminationMode.TERMINATED_BY_ZERO, data), extendedArea);
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
     * Returns block structure matching file "two_blocks_extended.xb".
     *
     * @return tree structure
     */
    public static XBDocument getSampleTwoBlocksExtendedTree() {
        XBData extendedArea = new XBData();
        extendedArea.loadFromStream(new ByteArrayInputStream(NUMBERS_SEQUENCE_REVERSED));
        return new XBDefaultDocument(new XBDefaultBlock(XBBlockTerminationMode.SIZE_SPECIFIED, new XBAttribute[]{new UBNat32(1)}, new XBBlock[]{
            new XBDefaultBlock(XBBlockTerminationMode.SIZE_SPECIFIED, new XBAttribute[]{new UBNat32(2)}, new XBBlock[]{})
        }), extendedArea);
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
        data.loadFromStream(new ByteArrayInputStream(NUMBERS_SEQUENCE));
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
