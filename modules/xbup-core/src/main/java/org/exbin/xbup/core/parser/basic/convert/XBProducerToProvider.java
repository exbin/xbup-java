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
 * @version 0.2.1 2017/05/19
 * @author ExBin Project (http://exbin.org)
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
                tokens.add(new XBAttributeToken(value));
            }

            @Override
            public void dataXB(InputStream data) throws XBProcessingException, IOException {
                tokens.add(new XBDataToken(data));
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
