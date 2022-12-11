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
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import org.exbin.xbup.core.block.XBBlockTerminationMode;
import org.exbin.xbup.core.block.XBBlockType;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.parser.XBProcessingExceptionType;
import org.exbin.xbup.core.parser.basic.XBTConsumer;
import org.exbin.xbup.core.parser.basic.XBTListener;
import org.exbin.xbup.core.parser.basic.XBTProvider;
import org.exbin.xbup.core.parser.basic.XBTSListener;
import org.exbin.xbup.core.parser.token.XBAttribute;
import org.exbin.xbup.core.parser.token.XBTAttributeToken;
import org.exbin.xbup.core.parser.token.XBTBeginToken;
import org.exbin.xbup.core.parser.token.XBTDataToken;
import org.exbin.xbup.core.parser.token.XBTEndToken;
import org.exbin.xbup.core.parser.token.XBTSBeginToken;
import org.exbin.xbup.core.parser.token.XBTToken;
import org.exbin.xbup.core.parser.token.XBTTypeToken;
import org.exbin.xbup.core.parser.token.convert.XBTListenerToToken;
import org.exbin.xbup.core.ubnumber.UBNatural;

/**
 * XBUP level 1 consumer to listener convert.
 *
 * Uses token buffer stored in memory.
 *
 * @author ExBin Project (https://exbin.org)
 */
public class XBTConsumerToListener implements XBTSListener {

    private List<XBTToken> tokens = new LinkedList<>();

    public XBTConsumerToListener(XBTConsumer consumer) {
        consumer.attachXBTProvider(new XBTProvider() {

            @Override
            public void produceXBT(XBTListener listener) throws XBProcessingException, IOException {
                if (!tokens.isEmpty()) {
                    XBTToken token = tokens.remove(0);
                    XBTListenerToToken.tokenToListener(token, listener);
                } else {
                    throw new XBProcessingException("End of data reached", XBProcessingExceptionType.UNEXPECTED_END_OF_STREAM);
                }
            }
        });
    }

    @Override
    public void beginXBT(XBBlockTerminationMode terminationMode) throws XBProcessingException, IOException {
        tokens.add(XBTBeginToken.create(terminationMode));
    }

    @Override
    public void beginXBT(XBBlockTerminationMode terminationMode, UBNatural blockSize) throws XBProcessingException, IOException {
        tokens.add(XBTSBeginToken.create(terminationMode, blockSize));
    }

    @Override
    public void typeXBT(XBBlockType type) throws XBProcessingException, IOException {
        tokens.add(XBTTypeToken.create(type));
    }

    @Override
    public void attribXBT(XBAttribute value) throws XBProcessingException, IOException {
        tokens.add(XBTAttributeToken.create(value));
    }

    @Override
    public void dataXBT(InputStream data) throws XBProcessingException, IOException {
        tokens.add(XBTDataToken.create(data));
    }

    @Override
    public void endXBT() throws XBProcessingException, IOException {
        tokens.add(XBTEndToken.create());
    }
}
