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

import org.exbin.xbup.core.parser.basic.XBTListener;
import org.exbin.xbup.core.parser.basic.XBTProducer;
import org.exbin.xbup.core.parser.token.event.XBTEventProducer;

/**
 * Event producer To producer convertor for XBUP protocol level 1.
 *
 * @version 0.1.23 2014/02/06
 * @author ExBin Project (http://exbin.org)
 */
public class XBTEventProducerToProducer implements XBTProducer {

    private final XBTEventProducer eventProducer;

    public XBTEventProducerToProducer(XBTEventProducer eventProducer) {
        this.eventProducer = eventProducer;
    }

    @Override
    public void attachXBTListener(XBTListener eventListener) {
        eventProducer.attachXBTEventListener(new XBTListenerToEventListener(eventListener));
    }
}