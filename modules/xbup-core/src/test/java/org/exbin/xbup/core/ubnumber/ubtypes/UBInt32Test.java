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
 * @version 0.1.25 2015/07/05
 * @author ExBin Project (http://exbin.org)
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
