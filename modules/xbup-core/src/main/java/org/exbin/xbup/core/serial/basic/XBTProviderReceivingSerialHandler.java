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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.exbin.xbup.core.parser.basic.XBTListener;
import org.exbin.xbup.core.parser.basic.XBTProvider;
import org.exbin.xbup.core.parser.basic.convert.XBTListenerToConsumer;

/**
 * XBUP level 1 serialization handler using basic parser mapping to provider.
 *
 * @version 0.2.1 2017/05/16
 * @author ExBin Project (http://exbin.org)
 */
public class XBTProviderReceivingSerialHandler implements XBTBasicInputReceivingSerialHandler {

    @Nullable
    private XBTProvider provider = null;

    public XBTProviderReceivingSerialHandler() {
    }

    public void attachXBTProvider(@Nonnull XBTProvider provider) {
        this.provider = provider;
    }

    @Override
    public void process(@Nonnull XBTListener listener) {
        if (provider == null) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        new XBTListenerToConsumer(listener).attachXBTProvider(provider);
    }
}
