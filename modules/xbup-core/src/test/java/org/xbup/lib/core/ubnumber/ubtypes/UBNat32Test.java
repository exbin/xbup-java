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
package org.xbup.lib.core.ubnumber.ubtypes;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.TestCase;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * Test class for UBNat32.
 *
 * @version 0.1.25 2015/07/05
 * @author ExBin Project (http://exbin.org)
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
