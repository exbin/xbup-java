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
package org.exbin.xbup.core.parser.basic.convert;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.exbin.xbup.core.block.XBBlockTerminationMode;
import org.exbin.xbup.core.block.XBBlockType;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.parser.XBProcessingExceptionType;
import org.exbin.xbup.core.parser.basic.XBTListener;
import org.exbin.xbup.core.parser.basic.XBTProducer;
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
 * XBUP level 1 producer to provider convertor.
 *
 * Uses token buffer stored in memory.
 *
 * @version 0.1.25 2015/02/06
 * @author ExBin Project (http://exbin.org)
 */
public class XBTProducerToProvider implements XBTProvider {

    private List<XBTToken> tokens;

    public XBTProducerToProvider(XBTProducer producer) {
        tokens = new ArrayList<>();
        producer.attachXBTListener(new XBTSListener() {

            @Override
            public void beginXBT(XBBlockTerminationMode terminationMode) throws XBProcessingException, IOException {
                tokens.add(new XBTBeginToken(terminationMode));
            }

            @Override
            public void beginXBT(XBBlockTerminationMode terminationMode, UBNatural blockSize) throws XBProcessingException, IOException {
                tokens.add(new XBTSBeginToken(terminationMode, blockSize));
            }

            @Override
            public void typeXBT(XBBlockType type) throws XBProcessingException, IOException {
                tokens.add(new XBTTypeToken(type));
            }

            @Override
            public void attribXBT(XBAttribute value) throws XBProcessingException, IOException {
                tokens.add(new XBTAttributeToken(value));
            }

            @Override
            public void dataXBT(InputStream data) throws XBProcessingException, IOException {
                tokens.add(new XBTDataToken(data));
            }

            @Override
            public void endXBT() throws XBProcessingException, IOException {
                tokens.add(new XBTEndToken());
            }
        });
    }

    @Override
    public void produceXBT(XBTListener listener) throws XBProcessingException, IOException {
        if (tokens.isEmpty()) {
            XBTToken token = tokens.get(0);
            XBTListenerToToken.tokenToListener(token, listener);
        } else {
            throw new XBProcessingException("End of data reached", XBProcessingExceptionType.UNEXPECTED_END_OF_STREAM);
        }
    }
}
