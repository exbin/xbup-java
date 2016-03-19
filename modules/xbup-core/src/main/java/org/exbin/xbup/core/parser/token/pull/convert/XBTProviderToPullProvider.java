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
package org.exbin.xbup.core.parser.token.pull.convert;

import java.io.IOException;
import java.io.InputStream;
import org.exbin.xbup.core.block.XBBlockTerminationMode;
import org.exbin.xbup.core.block.XBBlockType;
import org.exbin.xbup.core.parser.XBProcessingException;
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
import org.exbin.xbup.core.parser.token.pull.XBTPullProvider;
import org.exbin.xbup.core.ubnumber.UBNatural;

/**
 * Provider To pull provider convertor for XBUP protocol level 1.
 *
 * @version 0.1.24 2014/11/27
 * @author ExBin Project (http://exbin.org)
 */
public class XBTProviderToPullProvider implements XBTPullProvider {

    private final XBTProvider provider;
    private XBTToken token;

    public XBTProviderToPullProvider(XBTProvider provider) {
        this.provider = provider;
    }

    @Override
    public XBTToken pullXBTToken() throws XBProcessingException, IOException {
        provider.produceXBT(new XBTSListener() {
            @Override
            public void beginXBT(XBBlockTerminationMode terminationMode) throws XBProcessingException, IOException {
                token = new XBTBeginToken(terminationMode);
            }

            @Override
            public void beginXBT(XBBlockTerminationMode terminationMode, UBNatural blockSize) throws XBProcessingException, IOException {
                token = new XBTSBeginToken(terminationMode, blockSize);
            }

            @Override
            public void typeXBT(XBBlockType blockType) throws XBProcessingException, IOException {
                token = new XBTTypeToken(blockType);
            }

            @Override
            public void attribXBT(XBAttribute value) throws XBProcessingException, IOException {
                token = new XBTAttributeToken(value);
            }

            @Override
            public void dataXBT(InputStream data) throws XBProcessingException, IOException {
                token = new XBTDataToken(data);
            }

            @Override
            public void endXBT() throws XBProcessingException, IOException {
                token = new XBTEndToken();
            }
        });

        return token;
    }
}
