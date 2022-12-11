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
import java.util.ArrayList;
import java.util.List;
import org.exbin.xbup.core.block.XBBlockTerminationMode;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.parser.XBProcessingExceptionType;
import org.exbin.xbup.core.parser.basic.XBListener;
import org.exbin.xbup.core.parser.basic.XBProducer;
import org.exbin.xbup.core.parser.basic.XBProvider;
import org.exbin.xbup.core.parser.basic.XBSListener;
import org.exbin.xbup.core.parser.token.XBAttribute;
import org.exbin.xbup.core.parser.token.XBAttributeToken;
import org.exbin.xbup.core.parser.token.XBBeginToken;
import org.exbin.xbup.core.parser.token.XBDataToken;
import org.exbin.xbup.core.parser.token.XBEndToken;
import org.exbin.xbup.core.parser.token.XBSBeginToken;
import org.exbin.xbup.core.parser.token.XBToken;
import org.exbin.xbup.core.parser.token.convert.XBListenerToToken;
import org.exbin.xbup.core.ubnumber.UBNatural;

/**
 * XBUP level 0 producer to provider convert.
 *
 * Uses token buffer stored in memory.
 *
 * @author ExBin Project (https://exbin.org)
 */
public class XBProducerToProvider implements XBProvider {

    private List<XBToken> tokens;

    public XBProducerToProvider(XBProducer producer) {
        tokens = new ArrayList<>();
        producer.attachXBListener(new XBSListener() {

            @Override
            public void beginXB(XBBlockTerminationMode terminationMode) throws XBProcessingException, IOException {
                tokens.add(XBBeginToken.create(terminationMode));
            }

            @Override
            public void beginXB(XBBlockTerminationMode terminationMode, UBNatural blockSize) throws XBProcessingException, IOException {
                tokens.add(XBSBeginToken.create(terminationMode, blockSize));
            }

            @Override
            public void attribXB(XBAttribute value) throws XBProcessingException, IOException {
                tokens.add(XBAttributeToken.create(value));
            }

            @Override
            public void dataXB(InputStream data) throws XBProcessingException, IOException {
                tokens.add(XBDataToken.create(data));
            }

            @Override
            public void endXB() throws XBProcessingException, IOException {
                tokens.add(XBEndToken.create());
            }
        });
    }

    @Override
    public void produceXB(XBListener listener) throws XBProcessingException, IOException {
        if (tokens.isEmpty()) {
            XBToken token = tokens.get(0);
            XBListenerToToken.tokenToListener(token, listener);
        } else {
            throw new XBProcessingException("End of data reached", XBProcessingExceptionType.UNEXPECTED_END_OF_STREAM);
        }
    }
}
