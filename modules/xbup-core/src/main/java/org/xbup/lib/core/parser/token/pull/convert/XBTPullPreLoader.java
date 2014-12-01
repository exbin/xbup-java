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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.token.XBTToken;
import org.xbup.lib.core.parser.token.XBTTokenType;
import org.xbup.lib.core.parser.token.pull.XBTPullFilter;
import org.xbup.lib.core.parser.token.pull.XBTPullProvider;

/**
 * Level 1 filter providing making accesible next token.
 *
 * This filter should be usable for level 2 expanding conversions.
 *
 * @version 0.1.24 2014/12/01
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
                returnToken = prefixBuffer.get(0);
                prefixBuffer.remove(0);
                return returnToken;
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
}
