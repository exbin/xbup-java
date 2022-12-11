/*
 * Copyright (C) ExBin Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.exbin.xbup.core.serial.basic;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.core.parser.basic.XBTListener;
import org.exbin.xbup.core.parser.basic.XBTProvider;
import org.exbin.xbup.core.parser.basic.convert.XBTProviderToProducer;

/**
 * XBUP level 1 serialization handler using basic parser mapping to listener.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XBTListenerReceivingSerialHandler implements XBTBasicOutputReceivingSerialHandler {

    @Nullable
    private XBTListener listener;

    public XBTListenerReceivingSerialHandler() {
    }

    public void attachXBTConsumer(XBTListener listener) {
        this.listener = listener;
    }

    @Override
    public void process(XBTProvider provider) {
        if (listener == null) {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        new XBTProviderToProducer(provider).attachXBTListener(listener);
    }
}
