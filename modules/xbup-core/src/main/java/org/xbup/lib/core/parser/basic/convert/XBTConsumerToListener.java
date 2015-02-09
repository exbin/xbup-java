/*
 * Copyright (C) XBUP Project
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
package org.xbup.lib.core.parser.basic.convert;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.xbup.lib.core.block.XBBlockType;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.XBProcessingExceptionType;
import org.xbup.lib.core.parser.basic.XBTConsumer;
import org.xbup.lib.core.parser.basic.XBTListener;
import org.xbup.lib.core.parser.basic.XBTProvider;
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.parser.basic.XBTSListener;
import org.xbup.lib.core.parser.token.XBAttribute;
import org.xbup.lib.core.parser.token.XBTAttributeToken;
import org.xbup.lib.core.parser.token.XBTBeginToken;
import org.xbup.lib.core.parser.token.XBTDataToken;
import org.xbup.lib.core.parser.token.XBTEndToken;
import org.xbup.lib.core.parser.token.XBTSBeginToken;
import org.xbup.lib.core.parser.token.XBTToken;
import org.xbup.lib.core.parser.token.XBTTypeToken;
import org.xbup.lib.core.parser.token.convert.XBTListenerToToken;
import org.xbup.lib.core.ubnumber.UBNatural;

/**
 * XBUP level 1 consumer to listener convert.
 *
 * Uses token buffer stored in memory.
 *
 * @version 0.1.25 2015/02/06
 * @author XBUP Project (http://xbup.org)
 */
public class XBTConsumerToListener implements XBTSListener {

    private List<XBTToken> tokens;

    public XBTConsumerToListener(XBTConsumer consumer) {
        tokens = new ArrayList<>();
        consumer.attachXBTProvider(new XBTProvider() {

            @Override
            public void produceXBT(XBTListener listener) throws XBProcessingException, IOException {
                if (tokens.isEmpty()) {
                    XBTToken token = tokens.get(0);
                    XBTListenerToToken.tokenToListener(token, listener);
                } else {
                    throw new XBProcessingException("End of data reached", XBProcessingExceptionType.UNEXPECTED_END_OF_STREAM);
                }
            }
        });
    }

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
}
