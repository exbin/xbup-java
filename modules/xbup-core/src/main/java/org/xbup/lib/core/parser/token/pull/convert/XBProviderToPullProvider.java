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
package org.xbup.lib.core.parser.token.pull.convert;

import java.io.IOException;
import java.io.InputStream;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.basic.XBListener;
import org.xbup.lib.core.parser.basic.XBProvider;
import org.xbup.lib.core.parser.token.XBAttributeToken;
import org.xbup.lib.core.parser.token.XBBeginToken;
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.parser.token.XBDataToken;
import org.xbup.lib.core.parser.token.XBEndToken;
import org.xbup.lib.core.parser.token.XBToken;
import org.xbup.lib.core.parser.token.pull.XBPullProvider;
import org.xbup.lib.core.ubnumber.UBNatural;

/**
 * Provider To pull provider convertor for XBUP protocol level 0.
 *
 * @version 0.1.23 2014/02/06
 * @author XBUP Project (http://xbup.org)
 */
public class XBProviderToPullProvider implements XBPullProvider {

    private final XBProvider provider;
    private XBToken token;

    public XBProviderToPullProvider(XBProvider provider) {
        this.provider = provider;
    }
    
    @Override
    public XBToken pullXBToken() throws XBProcessingException, IOException {
        provider.produceXB(new XBListener() {
            @Override
            public void beginXB(XBBlockTerminationMode terminationMode) throws XBProcessingException, IOException {
                token = new XBBeginToken(terminationMode);
            }

            @Override
            public void attribXB(UBNatural value) throws XBProcessingException, IOException {
                token = new XBAttributeToken(value);
            }

            @Override
            public void dataXB(InputStream data) throws XBProcessingException, IOException {
                token = new XBDataToken(data);
            }

            @Override
            public void endXB() throws XBProcessingException, IOException {
                token = new XBEndToken();
            }
        });

        return token;
    }
}
