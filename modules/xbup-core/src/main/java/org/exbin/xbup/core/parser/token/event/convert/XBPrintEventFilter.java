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
