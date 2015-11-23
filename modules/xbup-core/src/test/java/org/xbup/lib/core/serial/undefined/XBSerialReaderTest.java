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
import org.junit.Ignore;
import org.junit.Test;
import org.xbup.lib.core.parser.data.XBCoreTestSampleTypes;
import org.xbup.lib.core.parser.token.pull.XBPullReader;
import org.xbup.lib.core.parser.token.pull.convert.XBPullProviderToProvider;
import org.xbup.lib.core.parser.token.pull.convert.XBToXBTPullConvertor;
import org.xbup.lib.core.serial.param.XBPListenerSerialHandler;
import org.xbup.lib.core.serial.param.XBPProviderSerialHandler;
import org.xbup.lib.core.type.XBString;

/**
 * Test class for XBEventReader.
 *
 * @version 0.2.0 2015/11/23
 * @author XBUP Project (http://xbup.org)
 */
public class XBSerialReaderTest extends TestCase {

    public XBSerialReaderTest(String testName) {
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
     * Tests XBEventReader class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Ignore
    @Test
    public void testReadSampleString() throws Exception {
        // TODO new XBPullReader(XBSerialReaderTest.class.getResourceAsStream(XBCoreTestSampleTypes.SAMPLE_UNDEFINED_STRING));
        // XBPListenerSerialHandler serialInput = new XBPProviderSerialHandler(new XBToXBTPullConvertor());
        // XBString testString = new XBString();
        // serialInput.process(testString);
    }
}
