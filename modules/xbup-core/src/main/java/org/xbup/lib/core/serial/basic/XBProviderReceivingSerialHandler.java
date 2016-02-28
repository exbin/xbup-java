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
package org.xbup.lib.core.serial.basic;

import org.xbup.lib.core.parser.basic.XBListener;
import org.xbup.lib.core.parser.basic.XBProvider;
import org.xbup.lib.core.parser.basic.convert.XBListenerToConsumer;

/**
 * XBUP level 0 serialization handler using basic parser mapping to provider.
 *
 * @version 0.1.25 2015/02/04
 * @author ExBin Project (http://exbin.org)
 */
public class XBProviderReceivingSerialHandler implements XBBasicInputReceivingSerialHandler {

    private XBProvider provider = null;

    public XBProviderReceivingSerialHandler() {
    }

    public void attachXBProvider(XBProvider provider) {
        this.provider = provider;
    }

    @Override
    public void process(XBListener listener) {
        new XBListenerToConsumer(listener).attachXBProvider(provider);
    }
}
