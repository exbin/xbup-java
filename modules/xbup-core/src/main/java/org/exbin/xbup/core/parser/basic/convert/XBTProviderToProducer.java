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
package org.exbin.xbup.core.parser.basic.convert;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.parser.basic.XBTListener;
import org.exbin.xbup.core.parser.basic.XBTProducer;
import org.exbin.xbup.core.parser.basic.XBTProvider;

/**
 * XBUP level 1 provider to producer convertor.
 *
 * @version 0.1.23 2013/11/18
 * @author ExBin Project (http://exbin.org)
 */
public class XBTProviderToProducer implements XBTProducer {

    private XBTProvider provider = null;

    public XBTProviderToProducer(XBTProvider provider) {
        this.provider = provider;
    }

    @Override
    public void attachXBTListener(XBTListener listener) {
        XBTCountingFilter countingListener = new XBTCountingFilter(listener);

        do {
            try {
                provider.produceXBT(countingListener);
            } catch (XBProcessingException | IOException ex) {
                Logger.getLogger(XBProviderToProducer.class.getName()).log(Level.SEVERE, null, ex);
            }
        } while (!countingListener.isFinished());
    }
}
