/*
 * Copyright (C) ExBin Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.exbin.xbup.core.ubnumber.ubtypes;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.TestCase;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.ubnumber.type.UBENat32;

/**
 * Test class for UBENat32.
 *
 * @version 0.1.25 2015/07/05
 * @author ExBin Project (http://exbin.org)
 */
public class UBENat32Test extends TestCase {

    public UBENat32Test(String testName) {
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
     * Tests fromStreamUB method of class UBENat32.
     *
     * @throws java.lang.Exception
     */
    public void testFromStreamUB() throws Exception {
        UBENat32 instance = new UBENat32();
        InputStream stream;
        int result;

        stream = new ByteArrayInputStream(new byte[]{0});
        result = instance.fromStreamUB(stream);
        assertEquals(1, result);
        assertEquals(0, instance.getInt());

        stream = new ByteArrayInputStream(new byte[]{-0x80, 0});
        result = instance.fromStreamUB(stream);
        assertEquals(2, result);
        assertEquals(127, instance.getInt());
    }

    /**
     * Tests toStreamUB and fromStreamUB methods of class UBENat32.
     */
    public void testReadWrite() {
        ByteArrayOutputStream oStream = new ByteArrayOutputStream();
        try {
            assertEquals(1, new UBENat32(0x7E).toStreamUB(oStream));
            assertEquals(2, new UBENat32(0x7F).toStreamUB(oStream));
            assertEquals(2, new UBENat32(0x407E).toStreamUB(oStream));
            assertEquals(3, new UBENat32(0x407F).toStreamUB(oStream));
            assertEquals(3, new UBENat32(0x20407E).toStreamUB(oStream));
            assertEquals(4, new UBENat32(0x20407F).toStreamUB(oStream));
            assertEquals(4, new UBENat32(0x1020407E).toStreamUB(oStream));
            assertEquals(5, new UBENat32(0x1020407F).toStreamUB(oStream));
            UBENat32 infinity = new UBENat32();
            infinity.setInfinity();
            assertEquals(infinity.toStreamUB(oStream), 1);
            oStream.flush();
            oStream.close();
        } catch (IOException ex) {
            Logger.getLogger(UBENat32Test.class.getName()).log(Level.SEVERE, null, ex);
        }

        ByteArrayInputStream iStream;
        try {
            iStream = new ByteArrayInputStream(oStream.toByteArray());
            UBENat32 value = new UBENat32();
            assertEquals(1, value.fromStreamUB(iStream));
            assertEquals(0x7E, value.getInt());
            assertEquals(2, value.fromStreamUB(iStream));
            assertEquals(0x7F, value.getInt());
            assertEquals(2, value.fromStreamUB(iStream));
            assertEquals(0x407E, value.getInt());
            assertEquals(3, value.fromStreamUB(iStream));
            assertEquals(0x407F, value.getInt());
            assertEquals(3, value.fromStreamUB(iStream));
            assertEquals(0x20407E, value.getInt());
            assertEquals(4, value.fromStreamUB(iStream));
            assertEquals(0x20407F, value.getInt());
            assertEquals(4, value.fromStreamUB(iStream));
            assertEquals(0x1020407E, value.getInt());
            assertEquals(5, value.fromStreamUB(iStream));
            assertEquals(0x1020407F, value.getInt());
            value.fromStreamUB(iStream);
            assertTrue(value.isInfinity());
            iStream.close();
        } catch (IOException | XBProcessingException ex) {
            Logger.getLogger(UBENat32Test.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Tests getInt method of class UBENat32.
     */
    public void testGetInt() {
        UBENat32 instance = new UBENat32(Integer.MAX_VALUE);
        int expResult = Integer.MAX_VALUE;
        int result = instance.getInt();
        assertEquals(expResult, result);
    }
}
