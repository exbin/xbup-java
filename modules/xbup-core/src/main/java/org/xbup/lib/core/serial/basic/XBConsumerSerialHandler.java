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
package org.xbup.lib.core.serial.basic;

import org.xbup.lib.core.parser.basic.XBConsumer;
import org.xbup.lib.core.parser.basic.XBProvider;

/**
 * XBUP level 0 serialization handler using basic parser mapping to listener.
 *
 * @version 0.1.24 2014/09/05
 * @author XBUP Project (http://xbup.org)
 */
public class XBConsumerSerialHandler implements XBConsumer, XBBasicInputSerialHandler {

    private XBConsumer consumer;

    public XBConsumerSerialHandler() {
    }

    @Override
    public void attachXBConsumer(XBConsumer consumer) {
        this.consumer = consumer;
    }

    @Override
    public void attachXBProvider(XBProvider provider) {
        consumer.attachXBProvider(provider);
    }
}
