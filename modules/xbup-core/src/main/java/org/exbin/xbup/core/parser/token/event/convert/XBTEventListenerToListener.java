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
package org.exbin.xbup.core.parser.token.event.convert;

import java.io.IOException;
import java.io.InputStream;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.core.block.XBBlockTerminationMode;
import org.exbin.xbup.core.block.XBBlockType;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.parser.basic.XBTListener;
import org.exbin.xbup.core.parser.basic.XBTSListener;
import org.exbin.xbup.core.parser.token.XBAttribute;
import org.exbin.xbup.core.parser.token.XBTAttributeToken;
import org.exbin.xbup.core.parser.token.XBTBeginToken;
import org.exbin.xbup.core.parser.token.XBTDataToken;
import org.exbin.xbup.core.parser.token.XBTEndToken;
import org.exbin.xbup.core.parser.token.XBTSBeginToken;
import org.exbin.xbup.core.parser.token.XBTTypeToken;
import org.exbin.xbup.core.parser.token.event.XBTEventListener;
import org.exbin.xbup.core.ubnumber.UBNatural;

/**
 * Event listener to listener convertor for XBUP protocol level 1.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XBTEventListenerToListener implements XBTListener, XBTSListener {

    @Nonnull
    private final XBTEventListener eventListener;

    public XBTEventListenerToListener(XBTEventListener eventListener) {
        this.eventListener = eventListener;
    }

    @Override
    public void attribXBT(XBAttribute attribute) throws XBProcessingException, IOException {
        eventListener.putXBTToken(XBTAttributeToken.create(attribute));
    }

    @Override
    public void beginXBT(XBBlockTerminationMode terminationMode) throws XBProcessingException, IOException {
        eventListener.putXBTToken(XBTBeginToken.create(terminationMode));
    }

    @Override
    public void beginXBT(XBBlockTerminationMode terminationMode, @Nullable UBNatural blockSize) throws XBProcessingException, IOException {
        eventListener.putXBTToken(XBTSBeginToken.create(terminationMode, blockSize));
    }

    @Override
    public void typeXBT(XBBlockType type) throws XBProcessingException, IOException {
        eventListener.putXBTToken(XBTTypeToken.create(type));
    }

    @Override
    public void dataXBT(InputStream data) throws XBProcessingException, IOException {
        eventListener.putXBTToken(XBTDataToken.create(data));
    }

    @Override
    public void endXBT() throws XBProcessingException, IOException {
        eventListener.putXBTToken(XBTEndToken.create());
    }

    public boolean isClosed() {
        return false;
    }
}
