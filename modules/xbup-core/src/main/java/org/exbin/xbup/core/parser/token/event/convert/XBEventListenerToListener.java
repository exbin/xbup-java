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
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.parser.basic.XBListener;
import org.exbin.xbup.core.parser.basic.XBSListener;
import org.exbin.xbup.core.parser.token.XBAttribute;
import org.exbin.xbup.core.parser.token.XBAttributeToken;
import org.exbin.xbup.core.parser.token.XBBeginToken;
import org.exbin.xbup.core.parser.token.XBDataToken;
import org.exbin.xbup.core.parser.token.XBEndToken;
import org.exbin.xbup.core.parser.token.XBSBeginToken;
import org.exbin.xbup.core.parser.token.event.XBEventListener;
import org.exbin.xbup.core.ubnumber.UBNatural;

/**
 * Event listener to listener convertor for XBUP protocol level 0.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XBEventListenerToListener implements XBListener, XBSListener {

    @Nonnull
    private final XBEventListener eventListener;

    public XBEventListenerToListener(XBEventListener eventListener) {
        this.eventListener = eventListener;
    }

    @Override
    public void attribXB(XBAttribute value) throws XBProcessingException, IOException {
        eventListener.putXBToken(XBAttributeToken.create(value));
    }

    @Override
    public void beginXB(XBBlockTerminationMode terminationMode) throws XBProcessingException, IOException {
        eventListener.putXBToken(XBBeginToken.create(terminationMode));
    }

    @Override
    public void beginXB(XBBlockTerminationMode terminationMode, @Nullable UBNatural blockSize) throws XBProcessingException, IOException {
        eventListener.putXBToken(XBSBeginToken.create(terminationMode, blockSize));
    }

    @Override
    public void dataXB(InputStream data) throws XBProcessingException, IOException {
        eventListener.putXBToken(XBDataToken.create(data));
    }

    @Override
    public void endXB() throws XBProcessingException, IOException {
        eventListener.putXBToken(XBEndToken.create());
    }
}
