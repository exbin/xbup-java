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
package org.xbup.lib.core.stream;

import java.io.IOException;
import java.io.InputStream;
import org.xbup.lib.core.parser.XBParseException;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.basic.XBListener;
import org.xbup.lib.core.parser.token.XBAttributeToken;
import org.xbup.lib.core.parser.token.XBBeginToken;
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.parser.token.XBDataToken;
import org.xbup.lib.core.parser.token.XBToken;
import org.xbup.lib.core.parser.token.XBTokenType;
import org.xbup.lib.core.ubnumber.UBNatural;

/**
 * XBUP level 0 stream checker.
 *
 * @version 0.1.21 2011/08/21
 * @author XBUP Project (http://xbup.org)
 */
public class XBStreamChecker implements XBListener {

    private XBTokenInputStream stream;

    public XBStreamChecker(XBTokenInputStream stream) {
        this.stream = stream;
    }

    public XBBlockTerminationMode beginXB() throws XBProcessingException, IOException {
        XBToken token = stream.pullXBToken();
        if (token.getTokenType() == XBTokenType.BEGIN) {
            return ((XBBeginToken)token).getTerminationMode();
        }
        throw new XBParseException("Unexpected event order");
    }

    public UBNatural attribXB() throws XBProcessingException, IOException {
        XBToken token = stream.pullXBToken();
        if (token.getTokenType() == XBTokenType.ATTRIBUTE) {
            return ((XBAttributeToken)token).getAttribute();
        }
        throw new XBParseException("Unexpected event order");
    }

    public InputStream dataXB() throws XBProcessingException, IOException {
        XBToken token = stream.pullXBToken();
        if (token.getTokenType() == XBTokenType.DATA) {
            return ((XBDataToken)token).getData();
        }
        throw new XBParseException("Unexpected event order");
    }

    @Override
    public void beginXB(XBBlockTerminationMode terminationMode) throws XBProcessingException, IOException {
        XBToken token = stream.pullXBToken();
        if (token.getTokenType() != XBTokenType.BEGIN || terminationMode != ((XBBeginToken)token).getTerminationMode()) {
            throw new XBParseException("Unexpected event order");
        }
    }

    @Override
    public void attribXB(UBNatural value) throws XBProcessingException, IOException {
        XBToken token = stream.pullXBToken();
        if (token.getTokenType() != XBTokenType.ATTRIBUTE || ((XBAttributeToken)token).getAttribute().getLong() != value.getLong()) {
            throw new XBParseException("Unexpected event order");
        }
    }

    @Override
    public void dataXB(InputStream data) throws XBProcessingException, IOException {
        XBToken token = stream.pullXBToken();
        if (token.getTokenType() != XBTokenType.DATA || data != ((XBDataToken)token).getData()) {
            throw new XBParseException("Unexpected event order");
        }
    }

    public boolean isClosed() {
        return stream == null;
    }

    @Override
    public void endXB() throws XBProcessingException, IOException {
        XBToken token = stream.pullXBToken();
        if (token.getTokenType() != XBTokenType.END) {
            throw new XBParseException("Unexpected event order");
        }
    }
}
