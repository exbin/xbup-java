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
package org.xbup.lib.core.parser.token.event.convert;

import java.io.IOException;
import java.io.InputStream;
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.block.XBBlockType;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.basic.XBTListener;
import org.xbup.lib.core.parser.basic.XBTSListener;
import org.xbup.lib.core.parser.token.XBAttribute;
import org.xbup.lib.core.parser.token.XBTAttributeToken;
import org.xbup.lib.core.parser.token.XBTBeginToken;
import org.xbup.lib.core.parser.token.XBTDataToken;
import org.xbup.lib.core.parser.token.XBTEndToken;
import org.xbup.lib.core.parser.token.XBTSBeginToken;
import org.xbup.lib.core.parser.token.XBTTypeToken;
import org.xbup.lib.core.parser.token.event.XBTEventListener;
import org.xbup.lib.core.ubnumber.UBNatural;

/**
 * Event listener to listener convertor for XBUP protocol level 1.
 *
 * @version 0.1.24 2014/11/27
 * @author ExBin Project (http://exbin.org)
 */
public class XBTEventListenerToListener implements XBTListener, XBTSListener {

    private final XBTEventListener eventListener;

    public XBTEventListenerToListener(XBTEventListener eventListener) {
        this.eventListener = eventListener;
    }

    @Override
    public void attribXBT(XBAttribute attribute) throws XBProcessingException, IOException {
        eventListener.putXBTToken(new XBTAttributeToken(attribute));
    }

    @Override
    public void beginXBT(XBBlockTerminationMode terminationMode) throws XBProcessingException, IOException {
        eventListener.putXBTToken(new XBTBeginToken(terminationMode));
    }

    @Override
    public void beginXBT(XBBlockTerminationMode terminationMode, UBNatural blockSize) throws XBProcessingException, IOException {
        eventListener.putXBTToken(new XBTSBeginToken(terminationMode, blockSize));
    }

    @Override
    public void typeXBT(XBBlockType type) throws XBProcessingException, IOException {
        eventListener.putXBTToken(new XBTTypeToken(type));
    }

    @Override
    public void dataXBT(InputStream data) throws XBProcessingException, IOException {
        eventListener.putXBTToken(new XBTDataToken(data));
    }

    @Override
    public void endXBT() throws XBProcessingException, IOException {
        eventListener.putXBTToken(new XBTEndToken());
    }

    public boolean isClosed() {
        return false;
    }
}
