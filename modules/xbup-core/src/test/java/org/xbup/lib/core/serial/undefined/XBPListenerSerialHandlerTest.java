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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import junit.framework.TestCase;
import org.junit.Ignore;
import org.junit.Test;
import org.xbup.lib.core.parser.data.XBCoreTestSampleTypes;
import org.xbup.lib.core.parser.token.event.XBEventWriter;
import org.xbup.lib.core.parser.token.event.convert.XBTToXBEventDropper;
import org.xbup.lib.core.serial.param.XBPListenerSerialHandler;
import static org.xbup.lib.core.test.XBTestUtils.assertEqualsInputStream;
import org.xbup.lib.core.type.XBString;

/**
 * Test class for XBPListenerSerialHandler.
 *
 * @version 0.2.0 2015/11/25
 * @author XBUP Project (http://xbup.org)
 */
public class XBPListenerSerialHandlerTest extends TestCase {

    public XBPListenerSerialHandlerTest(String testName) {
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
     * Tests XBPListenerSerialHandler class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleUndefinedString() throws Exception {
        ByteArrayOutputStream target = new ByteArrayOutputStream();
        XBEventWriter eventReader = new XBEventWriter(target);
        XBPListenerSerialHandler serial = new XBPListenerSerialHandler(new XBTToXBEventDropper(eventReader));
        XBString testString = XBCoreTestSampleTypes.getSampleTypeUndefinedString();
        serial.process(testString);

        InputStream matchingStream = XBPProviderSerialHandlerTest.class.getResourceAsStream(XBCoreTestSampleTypes.SAMPLE_UNDEFINED_STRING);
        assertEqualsInputStream(matchingStream, new ByteArrayInputStream(target.toByteArray()));
    }

    /**
     * Tests XBPListenerSerialHandler class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleUndefinedStringTerminated() throws Exception {
        ByteArrayOutputStream target = new ByteArrayOutputStream();
        XBEventWriter eventReader = new XBEventWriter(target);
        XBPListenerSerialHandler serial = new XBPListenerSerialHandler(new XBTToXBEventDropper(eventReader));
        XBString testString = XBCoreTestSampleTypes.getSampleTypeUndefinedStringTerminated();
        serial.process(testString);

        InputStream matchingStream = XBPProviderSerialHandlerTest.class.getResourceAsStream(XBCoreTestSampleTypes.SAMPLE_UNDEFINED_STRING_TERMINATED);
        // TODO assertEqualsInputStream(matchingStream, new ByteArrayInputStream(target.toByteArray()));
    }
}
