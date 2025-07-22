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
package org.exbin.xbup.core.parser.basic.convert;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.parser.basic.XBTConsumer;
import org.exbin.xbup.core.parser.basic.XBTListener;
import org.exbin.xbup.core.parser.basic.XBTProvider;

/**
 * XBUP level 1 listener to consumer convertor.
 *
 * @author ExBin Project (https://exbin.org)
 */
public class XBTListenerToConsumer implements XBTConsumer {

    private XBTListener listener = null;

    public XBTListenerToConsumer(XBTListener listener) {
        this.listener = listener;
    }

    @Override
    public void attachXBTProvider(XBTProvider provider) {
        XBTCountingFilter countingListener = new XBTCountingFilter(listener);

        do {
            try {
                provider.produceXBT(countingListener);
            } catch (XBProcessingException | IOException ex) {
                Logger.getLogger(XBTListenerToConsumer.class.getName()).log(Level.SEVERE, null, ex);
            }
        } while (!countingListener.isFinished());
    }
}
