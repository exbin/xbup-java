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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.token.XBTDataToken;
import org.xbup.lib.core.parser.token.XBTToken;
import org.xbup.lib.core.parser.token.XBTTokenType;
import org.xbup.lib.core.parser.token.pull.XBTPullFilter;
import org.xbup.lib.core.parser.token.pull.XBTPullProvider;
import org.xbup.lib.core.util.StreamUtils;

/**
 * Level 1 filter providing making accesible next token.
 *
 * This filter should be usable for level 2 expanding conversions.
 *
 * @version 0.1.24 2014/12/02
 * @author XBUP Project (http://xbup.org)
 */
public class XBTPullPreLoader implements XBTPullFilter {

    private XBTToken nextToken;
    private int depth = 0;
    private XBTPullProvider pullProvider;
    private List<XBTToken> prefixBuffer = null;

    public XBTPullPreLoader(XBTPullProvider pullProvider) {
        attachProvider(pullProvider);
    }

    @Override
    public void attachXBTPullProvider(XBTPullProvider pullProvider) {
        attachProvider(pullProvider);
    }

    private void attachProvider(XBTPullProvider pullProvider) {
        this.pullProvider = pullProvider;
        try {
            nextToken = pullProvider.pullXBTToken();
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBTPullPreLoader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public XBTToken pullXBTToken() throws XBProcessingException, IOException {
        XBTToken returnToken;
        if (prefixBuffer != null) {
            if (!prefixBuffer.isEmpty()) {
                return prefixBuffer.remove(0);
            }

            prefixBuffer = null;
        }
        returnToken = nextToken;
        switch (nextToken.getTokenType()) {
            case BEGIN: {
                depth++;
                break;
            }
            case END: {
                depth--;
                break;
            }
        }

        if (depth > 0) {
            nextToken = pullProvider.pullXBTToken();
            if (nextToken.getTokenType() == XBTTokenType.DATA) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                StreamUtils.copyInputStreamToOutputStream(((XBTDataToken) nextToken).getData(), stream);
                nextToken = new XBTDataToken(new ByteArrayInputStream(stream.toByteArray()));
            }
        }

        return returnToken;
    }

    public XBTToken getNextToken() {
        return nextToken;
    }

    public XBTTokenType getNextTokenType() {
        return nextToken != null ? nextToken.getTokenType() : null;
    }

    public List<XBTToken> getPrefixBuffer() {
        return prefixBuffer;
    }

    public void setPrefixBuffer(List<XBTToken> prefixBuffer) {
        this.prefixBuffer = prefixBuffer;
    }

    // TODO: Make it later so, that more effective skip is used when source
    // supports it

    /**
     * Skips remaining attributes if child is requested.
     *
     * @throws java.io.IOException if input/output error
     */
    public void skipAttributes() throws XBProcessingException, IOException {
        while (getNextTokenType() == XBTTokenType.ATTRIBUTE) {
            pullXBTToken();
        }
    }

    /**
     * Skips children until end token is reached.
     *
     * @throws java.io.IOException if input/output error
     */
    public void skipChildren() throws XBProcessingException, IOException {
        while (getNextTokenType() == XBTTokenType.BEGIN) {
            skipChild();
        }
    }

    /**
     * Skips single child.
     *
     * @throws java.io.IOException if input/output error
     */
    public void skipChild() throws XBProcessingException, IOException {
        int subDepth = 0;
        do {
            XBTToken token = pullXBTToken();
            switch (token.getTokenType()) {
                case BEGIN: {
                    subDepth++;
                    break;
                }
                case END: {
                    subDepth--;
                    break;
                }
            }
        } while (subDepth > 0);
    }
}
