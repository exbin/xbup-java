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
package org.exbin.xbup.core.parser.token.convert;

import java.io.IOException;
import java.io.InputStream;
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
import org.exbin.xbup.core.parser.token.XBTToken;
import org.exbin.xbup.core.parser.token.XBTTypeToken;
import org.exbin.xbup.core.ubnumber.UBNatural;

/**
 * XBUP level 1 listener to token convertor and static method for reverse
 * operation.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
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
    public void beginXBT(XBBlockTerminationMode terminationMode) throws XBProcessingException, IOException {
        token = XBTBeginToken.create(terminationMode);
    }

    @Override
    public void beginXBT(XBBlockTerminationMode terminationMode, @Nullable UBNatural blockSize) throws XBProcessingException, IOException {
        token = XBTSBeginToken.create(terminationMode, blockSize);
    }

    @Override
    public void typeXBT(XBBlockType blockType) throws XBProcessingException, IOException {
        token = XBTTypeToken.create(blockType);
    }

    @Override
    public void attribXBT(XBAttribute value) throws XBProcessingException, IOException {
        token = XBTAttributeToken.create(value);
    }

    @Override
    public void dataXBT(InputStream data) throws XBProcessingException, IOException {
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
    public static void tokenToListener(XBTToken token, XBTListener listener) throws XBProcessingException, IOException {
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
