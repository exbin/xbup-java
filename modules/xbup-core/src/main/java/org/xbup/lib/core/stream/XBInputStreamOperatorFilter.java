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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xbup.lib.core.parser.XBParseException;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.token.XBToken;
import org.xbup.lib.core.parser.token.operation.XBOperation;
import org.xbup.lib.core.parser.token.operation.XBTokenOperator;

/**
 * XBUP level 0 input stream filter using operators.
 *
 * @version 0.1.19 2010/05/30
 * @author XBUP Project (http://xbup.org)
 */
public class XBInputStreamOperatorFilter extends XBTokenInputStreamFilter {

    private final MyOperator operator;

    public XBInputStreamOperatorFilter(XBOperation<XBToken,XBToken> operation) {
        this.operator = new MyOperator(operation);
    }

    @Override
    public XBToken pullXBToken() throws XBParseException, IOException {
        return operator.getXB();
    }

    @Override
    public void setXBInputStream(XBTokenInputStream source) {
        operator.setXBInputStream(source);
    }

    @Override
    public void reset() throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean finished() throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void skip(long tokenCount) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void close() throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private class MyOperator extends XBTokenOperator<XBToken, XBToken> {

        private int inputRequest;
        private int outputMax;
        private List<XBToken> outputCache;
        private XBOperation<XBToken,XBToken> operation;
        private XBTokenInputStream source;

        public MyOperator(XBOperation<XBToken,XBToken> operation) {
            this.operation = operation;
            reset();
        }

        @Override
        public void putToken(XBToken token) throws IOException {
            if (outputCache.size() < outputMax) {
                outputCache.add(token);
            } else {
                throw new IOException("No cache available for event");
            }
        }

        @Override
        public XBToken getToken() throws IOException {
            try {
                return source.pullXBToken();
            } catch (XBProcessingException ex) {
                Logger.getLogger(XBInputStreamOperatorFilter.class.getName()).log(Level.SEVERE, null, ex);
            }
            return null;
        }

        @Override
        public void reset() {
            outputMax = 0;
            inputRequest = 0;
            outputCache = new ArrayList<XBToken>();
        }

        @Override
        public int availableInput() {
            return inputRequest;
        }

        @Override
        public int availableOutput() {
            if (outputCache == null) {
                return 0;
            }
            return outputCache.size();
        }

        @Override
        public void request(int count) {
            inputRequest = count;
        }

        @Override
        public void setInputCache(int size) {
        }

        @Override
        public void setOutputCache(int size) {
            outputMax = size;
        }

        public void setXBInputStream(XBTokenInputStream source) {
            this.source = source;
        }

        public XBToken getXB() {
            if (outputCache.isEmpty()) {
                operation.operate(this);
            }
            if (outputCache.size() > 0) {
                return outputCache.remove(0);
            } else {
                return null;
            } // TODO: Or throw exception?
        }
    }
}
