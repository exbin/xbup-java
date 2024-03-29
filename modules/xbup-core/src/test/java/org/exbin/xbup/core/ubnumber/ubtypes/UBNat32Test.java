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
import org.exbin.xbup.core.ubnumber.type.UBNat32;

/**
 * Test class for UBNat32.
 *
 * @author ExBin Project (https://exbin.org)
 */
public class UBNat32Test extends TestCase {

    public UBNat32Test(String testName) {
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
     * Tests fromStreamUB method of class UBNat32.
     *
     * @throws java.lang.Exception
     */
    public void testFromStreamUB() throws Exception {
        UBNat32 instance = new UBNat32();
        InputStream stream;
        int result;

        stream = new ByteArrayInputStream(new byte[]{0});
        result = instance.fromStreamUB(stream);
        assertEquals(1, result);
        assertEquals(0, instance.getInt());

        stream = new ByteArrayInputStream(new byte[]{-0x80, 0});
        result = instance.fromStreamUB(stream);
        assertEquals(2, result);
        assertEquals(128, instance.getInt());
    }

    /**
     * Tests toStreamUB and fromStreamUB methods of class UBNat32.
     */
    public void testReadWrite() {
        ByteArrayOutputStream oStream = new ByteArrayOutputStream();
        try {
            assertEquals(1, new UBNat32(0x7F).toStreamUB(oStream));
            assertEquals(2, new UBNat32(0x80).toStreamUB(oStream));
            assertEquals(2, new UBNat32(0x407F).toStreamUB(oStream));
            assertEquals(3, new UBNat32(0x4080).toStreamUB(oStream));
            assertEquals(3, new UBNat32(0x20407F).toStreamUB(oStream));
            assertEquals(4, new UBNat32(0x204080).toStreamUB(oStream));
            assertEquals(4, new UBNat32(0x1020407F).toStreamUB(oStream));
            assertEquals(5, new UBNat32(0x10204080).toStreamUB(oStream));
            oStream.flush();
            oStream.close();
        } catch (IOException ex) {
            Logger.getLogger(UBNat32Test.class.getName()).log(Level.SEVERE, null, ex);
        }

        ByteArrayInputStream iStream;
        try {
            iStream = new ByteArrayInputStream(oStream.toByteArray());
            UBNat32 value = new UBNat32();
            assertEquals(1, value.fromStreamUB(iStream));
            assertEquals(0x7F, value.getInt());
            assertEquals(2, value.fromStreamUB(iStream));
            assertEquals(0x80, value.getInt());
            assertEquals(2, value.fromStreamUB(iStream));
            assertEquals(0x407F, value.getInt());
            assertEquals(3, value.fromStreamUB(iStream));
            assertEquals(0x4080, value.getInt());
            assertEquals(3, value.fromStreamUB(iStream));
            assertEquals(0x20407F, value.getInt());
            assertEquals(4, value.fromStreamUB(iStream));
            assertEquals(0x204080, value.getInt());
            assertEquals(4, value.fromStreamUB(iStream));
            assertEquals(0x1020407F, value.getInt());
            assertEquals(5, value.fromStreamUB(iStream));
            assertEquals(0x10204080, value.getInt());
            iStream.close();
        } catch (IOException | XBProcessingException ex) {
            Logger.getLogger(UBNat32Test.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Tests getInt method of class UBNat32.
     */
    public void testGetInt() {
        UBNat32 instance = new UBNat32(Integer.MAX_VALUE);
        int expResult = Integer.MAX_VALUE;
        int result = instance.getInt();
        assertEquals(expResult, result);
    }
}
