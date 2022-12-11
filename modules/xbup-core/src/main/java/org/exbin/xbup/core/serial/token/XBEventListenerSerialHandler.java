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
package org.exbin.xbup.core.serial.token;

import java.io.IOException;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.parser.XBProcessingExceptionType;
import org.exbin.xbup.core.parser.token.XBToken;
import org.exbin.xbup.core.parser.token.event.XBEventListener;

/**
 * XBUP level 0 serialization token handler using token parser mapping to
 * listener.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XBEventListenerSerialHandler implements XBEventListener, XBTokenOutputSerialHandler {

    @Nullable
    private XBEventListener listener;

    public XBEventListenerSerialHandler() {
    }

    @Override
    public void attachXBEventListener(XBEventListener listener) {
        this.listener = listener;
    }

    @Override
    public void putXBToken(XBToken token) throws XBProcessingException, IOException {
        if (listener == null) {
            throw new XBProcessingException("Receiving tokens before initialization", XBProcessingExceptionType.UNEXPECTED_ORDER);
        }

        listener.putXBToken(token);
    }
}
