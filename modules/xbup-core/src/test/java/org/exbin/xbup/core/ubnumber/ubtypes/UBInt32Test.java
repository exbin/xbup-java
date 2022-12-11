/*
 * Copyright (C) ExBin Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
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
import org.exbin.xbup.core.ubnumber.type.UBInt32;

/**
 * Test class for UBInt32.
 *
 * @author ExBin Project (https://exbin.org)
 */
public class UBInt32Test extends TestCase {

    public UBInt32Test(String testName) {
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
     * Tests fromStreamUB method of class UBInt32.
     *
     * @throws java.lang.Exception
     */
    public void testFromStreamUB() throws Exception {
        UBInt32 instance = new UBInt32();
        InputStream stream;
        int result;

        stream = new ByteArrayInputStream(new byte[]{0});
        result = instance.fromStreamUB(stream);
        assertEquals(1, result);
        assertEquals(0, instance.getInt());

        stream = new ByteArrayInputStream(new byte[]{-0x80, 0});
        result = instance.fromStreamUB(stream);
        assertEquals(2, result);
        assertEquals(64, instance.getInt());
    }

    /**
     * Tests toStreamUB and fromStreamUB method of class UBInt32.
     */
    public void testReadWrite() {
        ByteArrayOutputStream oStream = new ByteArrayOutputStream();
        try {
            assertEquals(1, new UBInt32(0x3F).toStreamUB(oStream));
            assertEquals(2, new UBInt32(0x40).toStreamUB(oStream));
            assertEquals(2, new UBInt32(0x203F).toStreamUB(oStream));
            assertEquals(3, new UBInt32(0x2040).toStreamUB(oStream));
            assertEquals(3, new UBInt32(0x10203F).toStreamUB(oStream));
            assertEquals(4, new UBInt32(0x102040).toStreamUB(oStream));
            assertEquals(4, new UBInt32(0x810203F).toStreamUB(oStream));
            assertEquals(5, new UBInt32(0x8102040).toStreamUB(oStream));

            assertEquals(1, new UBInt32(-0x40).toStreamUB(oStream));
            assertEquals(2, new UBInt32(-0x41).toStreamUB(oStream));
            assertEquals(2, new UBInt32(-0x2040).toStreamUB(oStream));
            assertEquals(3, new UBInt32(-0x2041).toStreamUB(oStream));
            assertEquals(3, new UBInt32(-0x102040).toStreamUB(oStream));
            assertEquals(4, new UBInt32(-0x102041).toStreamUB(oStream));
            assertEquals(4, new UBInt32(-0x8102040).toStreamUB(oStream));
            oStream.flush();
            oStream.close();
        } catch (IOException ex) {
            Logger.getLogger(UBInt32Test.class.getName()).log(Level.SEVERE, null, ex);
        }

        ByteArrayInputStream iStream;
        try {
            iStream = new ByteArrayInputStream(oStream.toByteArray());
            UBInt32 value = new UBInt32();
            assertEquals(1, value.fromStreamUB(iStream));
            assertEquals(0x3F, value.getInt());
            assertEquals(2, value.fromStreamUB(iStream));
            assertEquals(0x40, value.getInt());
            assertEquals(2, value.fromStreamUB(iStream));
            assertEquals(0x203F, value.getInt());
            assertEquals(3, value.fromStreamUB(iStream));
            assertEquals(0x2040, value.getInt());
            assertEquals(3, value.fromStreamUB(iStream));
            assertEquals(0x10203F, value.getInt());
            assertEquals(4, value.fromStreamUB(iStream));
            assertEquals(0x102040, value.getInt());
            assertEquals(4, value.fromStreamUB(iStream));
            assertEquals(0x810203F, value.getInt());
            assertEquals(5, value.fromStreamUB(iStream));
            assertEquals(0x8102040, value.getInt());

            assertEquals(1, value.fromStreamUB(iStream));
            assertEquals(-0x40, value.getInt());
            assertEquals(2, value.fromStreamUB(iStream));
            assertEquals(-0x41, value.getInt());
            assertEquals(2, value.fromStreamUB(iStream));
            assertEquals(-0x2040, value.getInt());
            assertEquals(3, value.fromStreamUB(iStream));
            assertEquals(-0x2041, value.getInt());
            assertEquals(3, value.fromStreamUB(iStream));
            assertEquals(-0x102040, value.getInt());
            assertEquals(4, value.fromStreamUB(iStream));
            assertEquals(-0x102041, value.getInt());
            assertEquals(4, value.fromStreamUB(iStream));
            assertEquals(-0x8102040, value.getInt());
            iStream.close();
        } catch (IOException | XBProcessingException ex) {
            Logger.getLogger(UBInt32Test.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Tests getInt method of class UBInt32.
     */
    public void testGetInt() {
        UBInt32 instance = new UBInt32(Integer.MAX_VALUE);
        int expResult = Integer.MAX_VALUE;
        int result = instance.getInt();
        assertEquals(expResult, result);
    }
}
