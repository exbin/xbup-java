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
package org.xbup.lib.core.parser.basic;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import static junit.framework.Assert.assertEquals;
import junit.framework.TestCase;
import org.junit.Test;
import org.xbup.lib.core.parser.XBParseException;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.XBProcessingExceptionType;
import org.xbup.lib.core.parser.basic.convert.XBConsumerToListener;
import org.xbup.lib.core.parser.basic.convert.XBPrintFilter;
import org.xbup.lib.core.parser.data.XBCoreTestSampleData;
import org.xbup.lib.core.test.XBTestUtils;
import org.xbup.lib.core.test.XBTestUtils.TokenAssertXBFilter;

/**
 * Test class for XBProviderReader.
 *
 * @version 0.1.25 2015/07/10
 * @author XBUP Project (http://xbup.org)
 */
public class XBProviderReaderTest extends TestCase {

    public XBProviderReaderTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Tests XBProviderReader class with simple read.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testRead() throws Exception {
        try (InputStream source = XBProviderReaderTest.class.getResourceAsStream("/org/xbup/lib/core/resources/test/samples/xhtml-test.xb")) {
            try {
                XBProviderReader reader = new XBProviderReader();
                reader.open(source);
                while (!reader.isFinished()) {
                    reader.produceXB(new XBPrintFilter());
                }

                reader.close();
            } catch (FileNotFoundException ex) {
                fail("File not found");
            } catch (IOException | XBProcessingException ex) {
                fail("Processing error: " + ex.getMessage());
            }
        }
    }

    /**
     * Tests XBProviderReader class reading simple empty block.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleSingleEmptyBlock() throws Exception {
        try (InputStream stream = XBProviderReaderTest.class.getResourceAsStream("/org/xbup/lib/core/resources/test/samples/l0_singleemptyblock.xb")) {
            XBProviderReader instance = new XBProviderReader(stream);

            XBTestUtils.BufferAssertXBFilter assertListener;
            XBConsumerToListener buffer = new XBConsumerToListener(null);
            XBCoreTestSampleData.writeSampleSingleEmptyBlock(buffer);
            assertListener = new XBTestUtils.BufferAssertXBFilter(buffer.getTokens());

            while (!instance.isFinished()) {
                instance.produceXB(assertListener);
            }
            assertListener.isCorrect();

            instance.close();
        }
    }

    /**
     * Tests XBProviderReader class reading sample extended block.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleExtended() throws Exception {
        try (InputStream stream = XBProviderReaderTest.class.getResourceAsStream("/org/xbup/lib/core/resources/test/samples/l0_extended.xb")) {
            XBProviderReader instance = new XBProviderReader(stream);

            XBTestUtils.BufferAssertXBFilter assertListener;
            XBConsumerToListener buffer = new XBConsumerToListener(null);
            XBCoreTestSampleData.writeSampleExtended(buffer);
            assertListener = new XBTestUtils.BufferAssertXBFilter(buffer.getTokens());

            while (!instance.isFinished()) {
                instance.produceXB(assertListener);
            }
            assertListener.isCorrect();

            instance.close();
        }
    }

    /**
     * Tests XBProviderReader class reading no data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleEmpty() throws Exception {
        Throwable ex = null;

        try (InputStream stream = XBProviderReaderTest.class.getResourceAsStream("/org/xbup/lib/core/resources/test/samples/corrupted/l0_empty.xb")) {
            XBProviderReader instance = new XBProviderReader(stream);
            try {
                instance.produceXB(new TokenAssertXBFilter(null));
            } catch (XBProcessingException | IOException e) {
                ex = e;
            }

            assertEquals(new XBProcessingException("End of data reached", XBProcessingExceptionType.UNEXPECTED_END_OF_STREAM), ex);
        }
    }

    /**
     * Tests XBProviderReader class reading single byte.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleSingleByte() throws Exception {
        Throwable ex = null;

        try (InputStream stream = XBProviderReaderTest.class.getResourceAsStream("/org/xbup/lib/core/resources/test/samples/corrupted/l0_singlebyte.xb")) {
            XBProviderReader instance = new XBProviderReader(stream);
            try {
                instance.produceXB(new TokenAssertXBFilter(null));
            } catch (XBProcessingException | IOException e) {
                ex = e;
            }

            assertEquals(new XBProcessingException("End of data reached", XBProcessingExceptionType.UNEXPECTED_END_OF_STREAM), ex);
        }
    }

    /**
     * Tests XBProviderReader class reading wrong header.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleWrongHeader() throws Exception {
        Throwable ex = null;

        try (InputStream stream = XBProviderReaderTest.class.getResourceAsStream("/org/xbup/lib/core/resources/test/samples/corrupted/l0_wrongheader.xb")) {
            XBProviderReader instance = new XBProviderReader(stream);
            try {
                instance.produceXB(new TokenAssertXBFilter(null));
            } catch (XBProcessingException | IOException e) {
                ex = e;
            }

            assertEquals(new XBParseException("Unsupported header: 0xfe0059420002", XBProcessingExceptionType.CORRUPTED_HEADER), ex);
        }
    }

    /**
     * Tests XBProviderReader class reading simple data block.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleSingleData() throws Exception {
        try (InputStream stream = XBProviderReaderTest.class.getResourceAsStream("/org/xbup/lib/core/resources/test/samples/l0_singledata.xb")) {
            XBProviderReader instance = new XBProviderReader(stream);

            XBTestUtils.BufferAssertXBFilter assertListener;
            XBConsumerToListener buffer = new XBConsumerToListener(null);
            XBCoreTestSampleData.writeSampleSingleData(buffer);
            assertListener = new XBTestUtils.BufferAssertXBFilter(buffer.getTokens());

            while (!instance.isFinished()) {
                instance.produceXB(assertListener);
            }
            assertListener.isCorrect();

            instance.close();
        }
    }

    /**
     * Tests XBProviderReader class reading simple terminated block.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleTerminated() throws Exception {
        try (InputStream stream = XBProviderReaderTest.class.getResourceAsStream("/org/xbup/lib/core/resources/test/samples/l0_terminated.xb")) {
            XBProviderReader instance = new XBProviderReader(stream);

            XBTestUtils.BufferAssertXBFilter assertListener;
            XBConsumerToListener buffer = new XBConsumerToListener(null);
            XBCoreTestSampleData.writeSampleTerminated(buffer);
            assertListener = new XBTestUtils.BufferAssertXBFilter(buffer.getTokens());

            while (!instance.isFinished()) {
                instance.produceXB(assertListener);
            }
            assertListener.isCorrect();

            instance.close();
        }
    }

    /**
     * Tests XBProviderReader class writing six blocks.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleSixBlocks() throws Exception {
        try (InputStream stream = XBProviderReaderTest.class.getResourceAsStream("/org/xbup/lib/core/resources/test/samples/l0_sixblocks.xb")) {
            XBProviderReader instance = new XBProviderReader(stream);

            XBTestUtils.BufferAssertXBFilter assertListener;
            XBConsumerToListener buffer = new XBConsumerToListener(null);
            XBCoreTestSampleData.writeSampleSixBlocks(buffer);
            assertListener = new XBTestUtils.BufferAssertXBFilter(buffer.getTokens());

            while (!instance.isFinished()) {
                instance.produceXB(assertListener);
            }
            assertListener.isCorrect();

            instance.close();
        }
    }
}
