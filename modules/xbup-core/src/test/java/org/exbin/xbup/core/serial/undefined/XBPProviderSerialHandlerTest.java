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
package org.exbin.xbup.core.serial.undefined;

import junit.framework.TestCase;
import org.junit.Test;
import org.exbin.xbup.core.parser.data.XBCoreTestSampleTypes;
import org.exbin.xbup.core.parser.token.pull.XBPullReader;
import org.exbin.xbup.core.parser.token.pull.convert.XBToXBTPullConvertor;
import org.exbin.xbup.core.serial.param.XBPProviderSerialHandler;
import org.exbin.xbup.core.type.XBString;
import org.exbin.xbup.core.ubnumber.type.UBInt32;
import org.exbin.xbup.core.ubnumber.type.UBNat32;
import org.exbin.xbup.core.ubnumber.type.UBRea;

/**
 * Test class for XBPProviderSerialHandler.
 *
 * @version 0.2.0 2015/11/26
 * @author ExBin Project (http://exbin.org)
 */
public class XBPProviderSerialHandlerTest extends TestCase {

    public XBPProviderSerialHandlerTest(String testName) {
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
     * Tests XBPProviderSerialHandler class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleUndefinedString() throws Exception {
        XBPullReader pullReader = new XBPullReader(XBPProviderSerialHandlerTest.class.getResourceAsStream(XBCoreTestSampleTypes.SAMPLE_UNDEFINED_STRING));
        XBPProviderSerialHandler serial = new XBPProviderSerialHandler(new XBToXBTPullConvertor(pullReader));
        XBString testValue = new XBString();
        serial.process(testValue);

        XBString matchingValue = XBCoreTestSampleTypes.getSampleTypeUndefinedString();
        assertEquals(matchingValue, testValue);
    }

    /**
     * Tests XBPProviderSerialHandler class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleUndefinedStringTerminated() throws Exception {
        XBPullReader pullReader = new XBPullReader(XBPProviderSerialHandlerTest.class.getResourceAsStream(XBCoreTestSampleTypes.SAMPLE_UNDEFINED_STRING_TERMINATED));
        XBPProviderSerialHandler serial = new XBPProviderSerialHandler(new XBToXBTPullConvertor(pullReader));
        XBString testValue = new XBString();
        serial.process(testValue);

        XBString matchingValue = XBCoreTestSampleTypes.getSampleTypeUndefinedStringTerminated();
        assertEquals(matchingValue, testValue);
    }

    /**
     * Tests XBPProviderSerialHandler class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleUndefinedNatural() throws Exception {
        XBPullReader pullReader = new XBPullReader(XBPProviderSerialHandlerTest.class.getResourceAsStream(XBCoreTestSampleTypes.SAMPLE_UNDEFINED_NATURAL));
        XBPProviderSerialHandler serial = new XBPProviderSerialHandler(new XBToXBTPullConvertor(pullReader));
        UBNat32 testValue = new UBNat32();
        serial.process(testValue);

        UBNat32 matchingValue = XBCoreTestSampleTypes.getSampleTypeUndefinedNatural();
        assertEquals(matchingValue, testValue);
    }

    /**
     * Tests XBPProviderSerialHandler class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleUndefinedNaturalTerminated() throws Exception {
        XBPullReader pullReader = new XBPullReader(XBPProviderSerialHandlerTest.class.getResourceAsStream(XBCoreTestSampleTypes.SAMPLE_UNDEFINED_NATURAL_TERMINATED));
        XBPProviderSerialHandler serial = new XBPProviderSerialHandler(new XBToXBTPullConvertor(pullReader));
        UBNat32 testValue = new UBNat32();
        serial.process(testValue);

        UBNat32 matchingValue = XBCoreTestSampleTypes.getSampleTypeUndefinedNaturalTerminated();
        assertEquals(matchingValue, testValue);
    }

    /**
     * Tests XBPProviderSerialHandler class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleUndefinedInteger() throws Exception {
        XBPullReader pullReader = new XBPullReader(XBPProviderSerialHandlerTest.class.getResourceAsStream(XBCoreTestSampleTypes.SAMPLE_UNDEFINED_INTEGER));
        XBPProviderSerialHandler serial = new XBPProviderSerialHandler(new XBToXBTPullConvertor(pullReader));
        UBInt32 testValue = new UBInt32();
        serial.process(testValue);

        UBInt32 matchingValue = XBCoreTestSampleTypes.getSampleTypeUndefinedInteger();
        assertEquals(matchingValue, testValue);
    }

    /**
     * Tests XBPProviderSerialHandler class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleUndefinedIntegerTerminated() throws Exception {
        XBPullReader pullReader = new XBPullReader(XBPProviderSerialHandlerTest.class.getResourceAsStream(XBCoreTestSampleTypes.SAMPLE_UNDEFINED_INTEGER_TERMINATED));
        XBPProviderSerialHandler serial = new XBPProviderSerialHandler(new XBToXBTPullConvertor(pullReader));
        UBInt32 testValue = new UBInt32();
        serial.process(testValue);

        UBInt32 matchingValue = XBCoreTestSampleTypes.getSampleTypeUndefinedIntegerTerminated();
        assertEquals(matchingValue, testValue);
    }

    /**
     * Tests XBPProviderSerialHandler class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleUndefinedReal() throws Exception {
        XBPullReader pullReader = new XBPullReader(XBPProviderSerialHandlerTest.class.getResourceAsStream(XBCoreTestSampleTypes.SAMPLE_UNDEFINED_REAL));
        XBPProviderSerialHandler serial = new XBPProviderSerialHandler(new XBToXBTPullConvertor(pullReader));
        UBRea testValue = new UBRea();
        serial.process(testValue);

        UBRea matchingValue = XBCoreTestSampleTypes.getSampleTypeUndefinedReal();
        assertEquals(matchingValue, testValue);
    }

    /**
     * Tests XBPProviderSerialHandler class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleUndefinedRealTerminated() throws Exception {
        XBPullReader pullReader = new XBPullReader(XBPProviderSerialHandlerTest.class.getResourceAsStream(XBCoreTestSampleTypes.SAMPLE_UNDEFINED_REAL_TERMINATED));
        XBPProviderSerialHandler serial = new XBPProviderSerialHandler(new XBToXBTPullConvertor(pullReader));
        UBRea testValue = new UBRea();
        serial.process(testValue);

        UBRea matchingValue = XBCoreTestSampleTypes.getSampleTypeUndefinedRealTerminated();
        assertEquals(matchingValue, testValue);
    }
}
