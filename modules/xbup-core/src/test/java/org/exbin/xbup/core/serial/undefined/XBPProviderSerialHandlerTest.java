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
package org.exbin.xbup.core.serial.undefined;

import junit.framework.TestCase;
import org.exbin.xbup.core.parser.data.XBCoreTestSampleTypes;
import org.exbin.xbup.core.parser.token.pull.XBPullReader;
import org.exbin.xbup.core.parser.token.pull.convert.XBToXBTPullConvertor;
import org.exbin.xbup.core.serial.param.XBPProviderSerialHandler;
import org.exbin.xbup.core.type.XBString;
import org.exbin.xbup.core.ubnumber.type.UBInt32;
import org.exbin.xbup.core.ubnumber.type.UBNat32;
import org.exbin.xbup.core.ubnumber.type.UBRea;
import org.junit.Test;

/**
 * Test class for XBPProviderSerialHandler.
 *
 * @author ExBin Project (https://exbin.org)
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
