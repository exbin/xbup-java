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
import org.exbin.xbup.core.parser.token.XBTToken;
import org.exbin.xbup.core.parser.token.XBTTypeToken;
import org.exbin.xbup.core.ubnumber.UBNatural;

/**
 * XBUP level 1 listener to token convertor and static method for reverse
 * operation.
 *
 * @version 0.2.1 2017/06/05
 * @author ExBin Project (http://exbin.org)
 */
public class XBTListenerToToken implements XBTListener, XBTSListener {

    @Nullable
    private XBTToken token;

    public XBTListenerToToken() {
        token = null;
    }

    @Nullable
    public XBTToken getToken() {
        return token;
    }

    public void setToken(@Nullable XBTToken token) {
        this.token = token;
    }

    @Override
    public void beginXBT(@Nonnull XBBlockTerminationMode terminationMode) throws XBProcessingException, IOException {
        token = XBTBeginToken.create(terminationMode);
    }

    @Override
    public void beginXBT(@Nonnull XBBlockTerminationMode terminationMode, @Nullable UBNatural blockSize) throws XBProcessingException, IOException {
        token = XBTSBeginToken.create(terminationMode, blockSize);
    }

    @Override
    public void typeXBT(@Nonnull XBBlockType blockType) throws XBProcessingException, IOException {
        token = XBTTypeToken.create(blockType);
    }

    @Override
    public void attribXBT(@Nonnull XBAttribute value) throws XBProcessingException, IOException {
        token = XBTAttributeToken.create(value);
    }

    @Override
    public void dataXBT(@Nonnull InputStream data) throws XBProcessingException, IOException {
        token = XBTDataToken.create(data);
    }

    @Override
    public void endXBT() throws XBProcessingException, IOException {
        token = XBTEndToken.create();
    }

    /**
     * Passes given token to given listener.
     *
     * @param token given token
     * @param listener target listener
     * @throws XBProcessingException if processing error
     * @throws java.io.IOException if input/output error
     */
    public static void tokenToListener(@Nonnull XBTToken token, @Nonnull XBTListener listener) throws XBProcessingException, IOException {
        switch (token.getTokenType()) {
            case BEGIN: {
                if ((token instanceof XBTSBeginToken) && (listener instanceof XBTSListener)) {
                    ((XBTSListener) listener).beginXBT(((XBTSBeginToken) token).getTerminationMode(), ((XBTSBeginToken) token).getBlockSize());
                } else {
                    listener.beginXBT(((XBTBeginToken) token).getTerminationMode());
                }

                break;
            }

            case TYPE: {
                listener.typeXBT(((XBTTypeToken) token).getBlockType());
                break;
            }

            case ATTRIBUTE: {
                listener.attribXBT(((XBTAttributeToken) token).getAttribute());
                break;
            }

            case DATA: {
                listener.dataXBT(((XBTDataToken) token).getData());
                break;
            }

            case END: {
                listener.endXBT();
                break;
            }
        }
    }
}
