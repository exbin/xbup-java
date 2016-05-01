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
package org.exbin.xbup.core.serial.basic;

import org.exbin.xbup.core.parser.basic.XBListener;
import org.exbin.xbup.core.parser.basic.XBProvider;
import org.exbin.xbup.core.parser.basic.convert.XBProviderToProducer;

/**
 * XBUP level 0 serialization handler using basic parser mapping to listener.
 *
 * @version 0.1.25 2015/02/04
 * @author ExBin Project (http://exbin.org)
 */
public class XBListenerReceivingSerialHandler implements XBBasicOutputReceivingSerialHandler {

    private XBListener listener;

    public XBListenerReceivingSerialHandler() {
    }

    public void attachXBListener(XBListener listener) {
        this.listener = listener;
    }

    @Override
    public void process(XBProvider provider) {
        new XBProviderToProducer(provider).attachXBListener(listener);
    }
}