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
package org.xbup.lib.xb.parser.basic.convert;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.xbup.lib.xb.parser.XBProcessingException;
import org.xbup.lib.xb.parser.XBProcessingExceptionType;
import org.xbup.lib.xb.parser.basic.XBConsumer;
import org.xbup.lib.xb.parser.basic.XBListener;
import org.xbup.lib.xb.parser.basic.XBProvider;
import org.xbup.lib.xb.parser.token.XBAttributeToken;
import org.xbup.lib.xb.parser.token.XBBeginToken;
import org.xbup.lib.xb.block.XBBlockTerminationMode;
import org.xbup.lib.xb.parser.token.XBDataToken;
import org.xbup.lib.xb.parser.token.XBEndToken;
import org.xbup.lib.xb.parser.token.XBToken;
import org.xbup.lib.xb.ubnumber.UBNatural;

/**
 * XBUP level 0 consumer to listener convertor.
 * 
 * Uses token buffer stored in memory.
 *
 * @version 0.1 wr23.0 2013/11/21
 * @author XBUP Project (http://xbup.org)
 */
public class XBConsumerToListener implements XBListener {

    private List<XBToken> tokens;

    public XBConsumerToListener(XBConsumer consumer) {
        tokens = new ArrayList<>();
        consumer.attachXBProvider(new XBProvider() {

            @Override
            public void produceXB(XBListener listener) throws XBProcessingException, IOException {
                if (tokens.isEmpty()) {
                    XBToken token = tokens.get(0);
                    switch (token.getTokenType()) {
                        case BEGIN: {
                            listener.beginXB(((XBBeginToken) token).getTerminationMode());
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
                } else {
                    throw new XBProcessingException("End of data reached", XBProcessingExceptionType.UNEXPECTED_END_OF_STREAM);
                }
            }
        });
    }

    @Override
    public void beginXB(XBBlockTerminationMode terminationMode) throws XBProcessingException, IOException {
        tokens.add(new XBBeginToken(terminationMode));
    }

    @Override
    public void attribXB(UBNatural value) throws XBProcessingException, IOException {
        tokens.add(new XBAttributeToken(value));
    }

    @Override
    public void dataXB(InputStream data) throws XBProcessingException, IOException {
        tokens.add(new XBDataToken(data));
    }

    @Override
    public void endXB() throws XBProcessingException, IOException {
        tokens.add(new XBEndToken());
    }
}
