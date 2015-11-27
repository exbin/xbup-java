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
package org.xbup.lib.core.serial.undefined;

import junit.framework.TestCase;
import org.junit.Test;
import org.xbup.lib.core.parser.data.XBCoreTestSampleTypes;
import org.xbup.lib.core.parser.token.pull.XBPullReader;
import org.xbup.lib.core.parser.token.pull.convert.XBToXBTPullConvertor;
import org.xbup.lib.core.serial.param.XBPProviderSerialHandler;
import org.xbup.lib.core.type.XBString;
import org.xbup.lib.core.ubnumber.type.UBInt32;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * Test class for XBPProviderSerialHandler.
 *
 * @version 0.2.0 2015/11/26
 * @author XBUP Project (http://xbup.org)
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
        // TODO assertEquals(matchingValue, testValue);
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
        // TODO assertEquals(matchingValue, testValue);
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
        // TODO assertEquals(matchingValue, testValue);
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
        // TODO assertEquals(matchingValue, testValue);
    }
}
