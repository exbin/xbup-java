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
package org.exbin.xbup.core.parser.token.convert;

import java.io.IOException;
import java.io.InputStream;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
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
import org.exbin.xbup.core.parser.token.XBToken;
import org.exbin.xbup.core.ubnumber.UBNatural;

/**
 * XBUP level 0 listener to single token convertor and static method for reverse
 * operation.
 *
 * @version 0.2.1 2017/06/05
 * @author ExBin Project (http://exbin.org)
 */
public class XBListenerToToken implements XBListener, XBSListener {

    @Nullable
    private XBToken token;

    public XBListenerToToken() {
        token = null;
    }

    @Nullable
    public XBToken getToken() {
        return token;
    }

    public void setToken(@Nullable XBToken token) {
        this.token = token;
    }

    @Override
    public void beginXB(@Nonnull XBBlockTerminationMode terminationMode) throws XBProcessingException, IOException {
        token = XBBeginToken.create(terminationMode);
    }

    @Override
    public void beginXB(@Nonnull XBBlockTerminationMode terminationMode, @Nullable UBNatural blockSize) throws XBProcessingException, IOException {
        token = XBSBeginToken.create(terminationMode, blockSize);
    }

    @Override
    public void attribXB(@Nonnull XBAttribute value) throws XBProcessingException, IOException {
        token = XBAttributeToken.create(value);
    }

    @Override
    public void dataXB(@Nonnull InputStream data) throws XBProcessingException, IOException {
        token = XBDataToken.create(data);
    }

    @Override
    public void endXB() throws XBProcessingException, IOException {
        token = XBEndToken.create();
    }

    /**
     * Passes given token to given listener.
     *
     * @param token given token
     * @param listener target listener
     * @throws XBProcessingException if processing error
     * @throws java.io.IOException if input/output error
     */
    public static void tokenToListener(@Nonnull XBToken token, @Nonnull XBListener listener) throws XBProcessingException, IOException {
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
