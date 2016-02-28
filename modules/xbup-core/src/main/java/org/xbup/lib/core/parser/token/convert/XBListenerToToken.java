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
package org.xbup.lib.core.parser.token.convert;

import java.io.IOException;
import java.io.InputStream;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.basic.XBListener;
import org.xbup.lib.core.parser.token.XBAttributeToken;
import org.xbup.lib.core.parser.token.XBBeginToken;
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.parser.basic.XBSListener;
import org.xbup.lib.core.parser.token.XBAttribute;
import org.xbup.lib.core.parser.token.XBDataToken;
import org.xbup.lib.core.parser.token.XBEndToken;
import org.xbup.lib.core.parser.token.XBSBeginToken;
import org.xbup.lib.core.parser.token.XBToken;
import org.xbup.lib.core.ubnumber.UBNatural;

/**
 * XBUP level 0 listener to single token convertor and static method for reverse
 * operation.
 *
 * @version 0.1.24 2014/11/27
 * @author ExBin Project (http://exbin.org)
 */
public class XBListenerToToken implements XBListener, XBSListener {

    private XBToken token;

    public XBListenerToToken() {
        token = null;
    }

    public XBToken getToken() {
        return token;
    }

    public void setToken(XBToken token) {
        this.token = token;
    }

    @Override
    public void beginXB(XBBlockTerminationMode terminationMode) throws XBProcessingException, IOException {
        token = new XBBeginToken(terminationMode);
    }

    @Override
    public void beginXB(XBBlockTerminationMode terminationMode, UBNatural blockSize) throws XBProcessingException, IOException {
        token = new XBSBeginToken(terminationMode, blockSize);
    }

    @Override
    public void attribXB(XBAttribute value) throws XBProcessingException, IOException {
        token = new XBAttributeToken(value);
    }

    @Override
    public void dataXB(InputStream data) throws XBProcessingException, IOException {
        token = new XBDataToken(data);
    }

    @Override
    public void endXB() throws XBProcessingException, IOException {
        token = new XBEndToken();
    }

    /**
     * Passes given token to given listener.
     *
     * @param token given token
     * @param listener target listener
     * @throws XBProcessingException
     * @throws IOException
     */
    public static void tokenToListener(XBToken token, XBListener listener) throws XBProcessingException, IOException {
        switch (token.getTokenType()) {
            case BEGIN: {
                if ((token instanceof XBSBeginToken) && (listener instanceof XBSListener)) {
                    ((XBSListener) listener).beginXB(((XBSBeginToken) token).getTerminationMode(), ((XBSBeginToken) token).getBlockSize());
                } else {
                    listener.beginXB(((XBBeginToken) token).getTerminationMode());
                }

                break;
            }

            case ATTRIBUTE: {
                listener.attribXB(((XBAttributeToken) token).getAttribute());
                break;
            }

            case DATA: {
                listener.dataXB(((XBDataToken) token).getData());
                break;
            }

            case END: {
                listener.endXB();
                break;
            }
        }
    }
}
