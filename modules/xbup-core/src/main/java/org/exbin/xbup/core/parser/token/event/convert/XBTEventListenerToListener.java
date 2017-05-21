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
package org.exbin.xbup.core.parser.token.event.convert;

import java.io.IOException;
import java.io.InputStream;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
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
 * @version 0.2.1 2017/05/21
 * @author ExBin Project (http://exbin.org)
 */
public class XBTEventListenerToListener implements XBTListener, XBTSListener {

    @Nonnull
    private final XBTEventListener eventListener;

    public XBTEventListenerToListener(@Nonnull XBTEventListener eventListener) {
        this.eventListener = eventListener;
    }

    @Override
    public void attribXBT(@Nonnull XBAttribute attribute) throws XBProcessingException, IOException {
        eventListener.putXBTToken(XBTAttributeToken.create(attribute));
    }

    @Override
    public void beginXBT(@Nonnull XBBlockTerminationMode terminationMode) throws XBProcessingException, IOException {
        eventListener.putXBTToken(XBTBeginToken.create(terminationMode));
    }

    @Override
    public void beginXBT(@Nonnull XBBlockTerminationMode terminationMode, @Nullable UBNatural blockSize) throws XBProcessingException, IOException {
        eventListener.putXBTToken(XBTSBeginToken.create(terminationMode, blockSize));
    }

    @Override
    public void typeXBT(@Nonnull XBBlockType type) throws XBProcessingException, IOException {
        eventListener.putXBTToken(XBTTypeToken.create(type));
    }

    @Override
    public void dataXBT(@Nonnull InputStream data) throws XBProcessingException, IOException {
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
