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
package org.exbin.xbup.core.serial.undefined;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import junit.framework.TestCase;
import org.exbin.xbup.core.block.XBBlockTerminationMode;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.parser.data.XBCoreTestSampleTypes;
import org.exbin.xbup.core.parser.token.XBAttributeToken;
import org.exbin.xbup.core.parser.token.XBBeginToken;
import org.exbin.xbup.core.parser.token.XBDataToken;
import org.exbin.xbup.core.parser.token.XBEndToken;
import org.exbin.xbup.core.parser.token.XBTAttributeToken;
import org.exbin.xbup.core.parser.token.XBTBeginToken;
import org.exbin.xbup.core.parser.token.XBTDataToken;
import org.exbin.xbup.core.parser.token.XBTToken;
import org.exbin.xbup.core.parser.token.XBToken;
import org.exbin.xbup.core.parser.token.XBTokenType;
import org.exbin.xbup.core.parser.token.event.XBEventFilter;
import org.exbin.xbup.core.parser.token.event.XBEventListener;
import org.exbin.xbup.core.parser.token.event.XBEventProducer;
import org.exbin.xbup.core.parser.token.event.XBEventWriter;
import org.exbin.xbup.core.parser.token.event.XBTEventListener;
import org.exbin.xbup.core.serial.param.XBPListenerSerialHandler;
import static org.exbin.xbup.core.test.XBTestUtils.assertEqualsInputStream;
import org.exbin.xbup.core.type.XBString;
import org.exbin.xbup.core.ubnumber.type.UBInt32;
import org.exbin.xbup.core.ubnumber.type.UBNat32;
import org.exbin.xbup.core.ubnumber.type.UBRea;
import org.junit.Test;

/**
 * Test class for XBPListenerSerialHandler.
 *
 * @version 0.2.1 2017/05/19
 * @author ExBin Project (http://exbin.org)
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
        XBEventWriter eventWriter = new XBEventWriter(target);
        XBPListenerSerialHandler serial = new XBPListenerSerialHandler(new XBTToXBEventTypeRemover(eventWriter));
        XBString testValue = XBCoreTestSampleTypes.getSampleTypeUndefinedString();
        serial.process(testValue);

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
        XBEventWriter eventWriter = new XBEventWriter(target);
        XBPListenerSerialHandler serial = new XBPListenerSerialHandler(new XBTToXBEventTypeRemover(new XBEventTerminatedFilter(eventWriter)));
        XBString testValue = XBCoreTestSampleTypes.getSampleTypeUndefinedStringTerminated();
        serial.process(testValue);

        InputStream matchingStream = XBPProviderSerialHandlerTest.class.getResourceAsStream(XBCoreTestSampleTypes.SAMPLE_UNDEFINED_STRING_TERMINATED);
        assertEqualsInputStream(matchingStream, new ByteArrayInputStream(target.toByteArray()));
    }

    /**
     * Tests XBPListenerSerialHandler class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleUndefinedNatural() throws Exception {
        ByteArrayOutputStream target = new ByteArrayOutputStream();
        XBEventWriter eventWriter = new XBEventWriter(target);
        XBPListenerSerialHandler serial = new XBPListenerSerialHandler(new XBTToXBEventTypeRemover(eventWriter));
        UBNat32 testValue = XBCoreTestSampleTypes.getSampleTypeUndefinedNatural();
        serial.process(testValue);

        InputStream matchingStream = XBPProviderSerialHandlerTest.class.getResourceAsStream(XBCoreTestSampleTypes.SAMPLE_UNDEFINED_NATURAL);
        assertEqualsInputStream(matchingStream, new ByteArrayInputStream(target.toByteArray()));
    }

    /**
     * Tests XBPListenerSerialHandler class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleUndefinedNaturalTerminated() throws Exception {
        ByteArrayOutputStream target = new ByteArrayOutputStream();
        XBEventWriter eventWriter = new XBEventWriter(target);
        XBPListenerSerialHandler serial = new XBPListenerSerialHandler(new XBTToXBEventTypeRemover(new XBEventTerminatedFilter(eventWriter)));
        UBNat32 testValue = XBCoreTestSampleTypes.getSampleTypeUndefinedNaturalTerminated();
        serial.process(testValue);

        InputStream matchingStream = XBPProviderSerialHandlerTest.class.getResourceAsStream(XBCoreTestSampleTypes.SAMPLE_UNDEFINED_NATURAL_TERMINATED);
        assertEqualsInputStream(matchingStream, new ByteArrayInputStream(target.toByteArray()));
    }

    /**
     * Tests XBPListenerSerialHandler class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleUndefinedInteger() throws Exception {
        ByteArrayOutputStream target = new ByteArrayOutputStream();
        XBEventWriter eventWriter = new XBEventWriter(target);
        XBPListenerSerialHandler serial = new XBPListenerSerialHandler(new XBTToXBEventTypeRemover(eventWriter));
        UBInt32 testValue = XBCoreTestSampleTypes.getSampleTypeUndefinedInteger();
        serial.process(testValue);

        InputStream matchingStream = XBPProviderSerialHandlerTest.class.getResourceAsStream(XBCoreTestSampleTypes.SAMPLE_UNDEFINED_INTEGER);
        assertEqualsInputStream(matchingStream, new ByteArrayInputStream(target.toByteArray()));
    }

    /**
     * Tests XBPListenerSerialHandler class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleUndefinedIntegerTerminated() throws Exception {
        ByteArrayOutputStream target = new ByteArrayOutputStream();
        XBEventWriter eventWriter = new XBEventWriter(target);
        XBPListenerSerialHandler serial = new XBPListenerSerialHandler(new XBTToXBEventTypeRemover(new XBEventTerminatedFilter(eventWriter)));
        UBInt32 testValue = XBCoreTestSampleTypes.getSampleTypeUndefinedIntegerTerminated();
        serial.process(testValue);

        InputStream matchingStream = XBPProviderSerialHandlerTest.class.getResourceAsStream(XBCoreTestSampleTypes.SAMPLE_UNDEFINED_INTEGER_TERMINATED);
        assertEqualsInputStream(matchingStream, new ByteArrayInputStream(target.toByteArray()));
    }

    /**
     * Tests XBPListenerSerialHandler class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleUndefinedReal() throws Exception {
        ByteArrayOutputStream target = new ByteArrayOutputStream();
        XBEventWriter eventWriter = new XBEventWriter(target);
        XBPListenerSerialHandler serial = new XBPListenerSerialHandler(new XBTToXBEventTypeRemover(eventWriter));
        UBRea testValue = XBCoreTestSampleTypes.getSampleTypeUndefinedReal();
        serial.process(testValue);

        InputStream matchingStream = XBPProviderSerialHandlerTest.class.getResourceAsStream(XBCoreTestSampleTypes.SAMPLE_UNDEFINED_REAL);
        assertEqualsInputStream(matchingStream, new ByteArrayInputStream(target.toByteArray()));
    }

    /**
     * Tests XBPListenerSerialHandler class reading sample data.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReadSampleUndefinedRealTerminated() throws Exception {
        ByteArrayOutputStream target = new ByteArrayOutputStream();
        XBEventWriter eventWriter = new XBEventWriter(target);
        XBPListenerSerialHandler serial = new XBPListenerSerialHandler(new XBTToXBEventTypeRemover(new XBEventTerminatedFilter(eventWriter)));
        UBRea testValue = XBCoreTestSampleTypes.getSampleTypeUndefinedRealTerminated();
        serial.process(testValue);

        InputStream matchingStream = XBPProviderSerialHandlerTest.class.getResourceAsStream(XBCoreTestSampleTypes.SAMPLE_UNDEFINED_REAL_TERMINATED);
        assertEqualsInputStream(matchingStream, new ByteArrayInputStream(target.toByteArray()));
    }

    private class XBEventTerminatedFilter implements XBEventFilter {

        private XBEventListener eventListener;

        public XBEventTerminatedFilter(XBEventListener eventListener) {
            this.eventListener = eventListener;
        }

        @Override
        public void putXBToken(XBToken token) throws XBProcessingException, IOException {
            if (token.getTokenType() == XBTokenType.BEGIN) {
                eventListener.putXBToken(XBBeginToken.create(XBBlockTerminationMode.TERMINATED_BY_ZERO));
            } else {
                eventListener.putXBToken(token);
            }
        }

        @Override
        public void attachXBEventListener(XBEventListener eventListener) {
            this.eventListener = eventListener;
        }
    }

    private class XBTToXBEventTypeRemover implements XBTEventListener, XBEventProducer {

        private XBEventListener eventListener;
        private boolean blockIdSent = false;

        public XBTToXBEventTypeRemover(XBEventListener eventListener) {
            this.eventListener = eventListener;
        }

        @Override
        public void putXBTToken(XBTToken token) throws XBProcessingException, IOException {
            switch (token.getTokenType()) {
                case BEGIN: {
                    eventListener.putXBToken(XBBeginToken.create(((XBTBeginToken) token).getTerminationMode()));
                    blockIdSent = false;
                    break;
                }
                case TYPE: {
                    eventListener.putXBToken(XBAttributeToken.create(new UBNat32()));
                    break;
                }
                case ATTRIBUTE: {
                    if (!blockIdSent) {
                        eventListener.putXBToken(XBAttributeToken.create(new UBNat32()));
                        blockIdSent = true;
                    }
                    eventListener.putXBToken(XBAttributeToken.create(((XBTAttributeToken) token).getAttribute()));
                    break;
                }
                case DATA: {
                    eventListener.putXBToken(XBDataToken.create(((XBTDataToken) token).getData()));
                    break;
                }
                case END: {
                    eventListener.putXBToken(XBEndToken.create());
                    break;
                }
            }
        }

        @Override
        public void attachXBEventListener(XBEventListener eventListener) {
            this.eventListener = eventListener;
        }
    }
}
