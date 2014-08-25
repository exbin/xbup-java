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
import org.xbup.lib.core.block.XBBlockType;
import org.xbup.lib.core.parser.XBParseException;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.basic.XBTListener;
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.parser.token.XBTAttributeToken;
import org.xbup.lib.core.parser.token.XBTBeginToken;
import org.xbup.lib.core.parser.token.XBTDataToken;
import org.xbup.lib.core.parser.token.XBTToken;
import org.xbup.lib.core.parser.token.XBTTokenType;
import org.xbup.lib.core.parser.token.XBTTypeToken;
import org.xbup.lib.core.ubnumber.UBNatural;

/**
 * XBUP level 1 stream checker.
 *
 * @version 0.1.21 2011/08/21
 * @author XBUP Project (http://xbup.org)
 */
public class XBTStreamChecker implements XBTListener {

    private final XBTInputTokenStream stream;

    public XBTStreamChecker(XBTInputTokenStream stream) {
        this.stream = stream;
    }

    public XBBlockTerminationMode beginXBT() throws XBProcessingException, IOException {
        XBTToken token = getStream().pullXBTToken();
        if (token.getTokenType() == XBTTokenType.BEGIN) {
            return ((XBTBeginToken)token).getTerminationMode();
        }
        throw new XBParseException("Unexpected event order");
    }

    public XBBlockType typeXBT() throws XBProcessingException, IOException {
        XBTToken token = getStream().pullXBTToken();
        if (token.getTokenType() == XBTTokenType.TYPE) {
            return ((XBTTypeToken)token).getBlockType();
        }
        throw new XBParseException("Unexpected event order");
    }

    public UBNatural attribXBT() throws XBProcessingException, IOException {
        XBTToken token = getStream().pullXBTToken();
        if (token.getTokenType() == XBTTokenType.ATTRIBUTE) {
            return ((XBTAttributeToken)token).getAttribute();
        }
        throw new XBParseException("Unexpected event order");
    }

    public InputStream dataXBT() throws XBProcessingException, IOException {
        XBTToken token = getStream().pullXBTToken();
        if (token.getTokenType() == XBTTokenType.DATA) {
            return ((XBTDataToken)token).getData();
        }
        throw new XBParseException("Unexpected event order");
    }

    @Override
    public void endXBT() throws XBProcessingException, IOException {
        XBTToken token = getStream().pullXBTToken();
        if (token.getTokenType() == XBTTokenType.END) {
            return;
        }
        throw new XBParseException("Unexpected event order");
    }

    @Override
    public void beginXBT(XBBlockTerminationMode terminationMode) throws XBProcessingException, IOException {
        XBTToken token = getStream().pullXBTToken();
        if (token.getTokenType() != XBTTokenType.BEGIN || terminationMode != ((XBTBeginToken)token).getTerminationMode()) {
            throw new XBParseException("Unexpected event order");
        }
    }

    @Override
    public void typeXBT(XBBlockType type) throws XBProcessingException, IOException {
        XBTToken token = getStream().pullXBTToken();
        if (token.getTokenType() != XBTTokenType.TYPE || !((XBTTypeToken)token).getBlockType().equals(type)) {
            throw new XBParseException("Unexpected event order");
        }
    }

    @Override
    public void attribXBT(UBNatural value) throws XBProcessingException, IOException {
        XBTToken token = getStream().pullXBTToken();
        if (token.getTokenType() != XBTTokenType.ATTRIBUTE || ((XBTAttributeToken)token).getAttribute().getLong() != value.getLong()) {
            throw new XBParseException("Unexpected event order");
        }
    }

    @Override
    public void dataXBT(InputStream data) throws XBProcessingException, IOException {
        XBTToken token = getStream().pullXBTToken();
        if (token.getTokenType() != XBTTokenType.DATA || data != ((XBTDataToken)token).getData()) {
            throw new XBParseException("Unexpected event order");
        }
    }

    public boolean isClosed() {
        return getStream() == null;
    }

    /**
     * @return source stream
     */
    public XBTInputTokenStream getStream() {
        return stream;
    }
}
