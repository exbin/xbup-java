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
package org.exbin.xbup.core.parser.token.event.convert;

import java.io.IOException;
import javax.annotation.Nonnull;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.parser.XBProcessingExceptionType;
import org.exbin.xbup.core.parser.token.XBAttributeToken;
import org.exbin.xbup.core.parser.token.XBBeginToken;
import org.exbin.xbup.core.parser.token.XBDataToken;
import org.exbin.xbup.core.parser.token.XBToken;
import org.exbin.xbup.core.parser.token.event.XBEventFilter;
import org.exbin.xbup.core.parser.token.event.XBEventListener;

/**
 * XBUP level 1 event filter printing out informations about tokens.
 *
 * @version 0.2.1 2017/06/05
 * @author ExBin Project (http://exbin.org)
 */
public class XBPrintEventFilter implements XBEventFilter {

    @Nonnull
    private XBEventListener eventListener;
    @Nonnull
    private String prefix = "";

    public XBPrintEventFilter(@Nonnull String prefix, @Nonnull XBEventListener eventListener) {
        this(eventListener);
        this.prefix = prefix;
    }

    public XBPrintEventFilter(XBEventListener eventListener) {
        this.eventListener = eventListener;
    }

    @Override
    public void putXBToken(@Nonnull XBToken token) throws XBProcessingException, IOException {
        switch (token.getTokenType()) {
            case BEGIN: {
                System.out.println(prefix + "> Begin (" + ((XBBeginToken) token).getTerminationMode().toString() + "):");
                break;
            }
            case ATTRIBUTE: {
                System.out.println(prefix + "  Attribute: " + ((XBAttributeToken) token).getAttribute().getNaturalLong());
                break;
            }
            case DATA: {
                System.out.println(prefix + "  Data:" + ((XBDataToken) token).getData().available());
                break;
            }
            case END: {
                System.out.println(prefix + "< End.");
                break;
            }
            default:
                throw new XBProcessingException("Unexpected token type", XBProcessingExceptionType.UNKNOWN);
        }

        if (eventListener != null) {
            eventListener.putXBToken(token);
        }
    }

    @Override
    public void attachXBEventListener(@Nonnull XBEventListener eventListener) {
        this.eventListener = eventListener;
    }
}
