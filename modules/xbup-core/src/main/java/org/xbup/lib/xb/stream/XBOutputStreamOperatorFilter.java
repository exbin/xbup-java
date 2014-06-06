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
package org.xbup.lib.xb.stream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xbup.lib.xb.parser.XBProcessingException;
import org.xbup.lib.xb.parser.token.XBToken;
import org.xbup.lib.xb.parser.token.operation.XBOperation;
import org.xbup.lib.xb.parser.token.operation.XBTokenOperator;

/**
 * XBUP level 0 output stream filter using operators.
 *
 * @version 0.1 wr19.0 2010/05/30
 * @author XBUP Project (http://xbup.org)
 */
public class XBOutputStreamOperatorFilter extends XBTokenOutputStreamFilter {

    private MyOperator operator;

    public XBOutputStreamOperatorFilter(XBOperation<XBToken,XBToken> operation) {
        this.operator = new MyOperator(operation);
    }

    @Override
    public void setXBOutputStream(XBTokenOutputStream target) {
        operator.setXBOutputStream(target);
    }

    @Override
    public void flush() throws IOException {
        operator.flush();
    }

    @Override
    public void putXBToken(XBToken token) throws XBProcessingException, IOException {
        operator.putXBToken(token);
    }

    private class MyOperator extends XBTokenOperator<XBToken, XBToken> {

        private int inputRequest;
        private int inputMax;
        private int inputPos;
        private List<XBToken> inputCache;
        private XBOperation<XBToken,XBToken> operation;
        private XBTokenOutputStream target;

        public MyOperator(XBOperation<XBToken,XBToken> operation) {
            this.operation = operation;
            reset();
        }

        @Override
        public void putToken(XBToken token) throws IOException {
            try {
                target.putXBToken(token);
            } catch (XBProcessingException ex) {
                Logger.getLogger(XBOutputStreamOperatorFilter.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public XBToken getToken() throws IOException {
            if (inputCache.size()>0) {
                return inputCache.remove(0);
            } else {
                throw new IOException("No event available in cache");
            }
        }

        @Override
        public void reset() {
            inputMax = 0;
            inputRequest = 0;
            inputCache = new ArrayList<XBToken>();
        }

        @Override
        public int availableInput() {
            return inputCache.size();
        }

        @Override
        public int availableOutput() {
            return 0;
        }

        @Override
        public void request(int count) {
            inputRequest = count;
        }

        @Override
        public void setInputCache(int size) {
            inputMax = size;
        }

        @Override
        public void setOutputCache(int size) {
        }

        public void setXBOutputStream(XBTokenOutputStream target) {
            this.target = target;
        }

        public void putXBToken(XBToken token) {
            if ((inputCache.size() < inputMax) || inputMax == 0) {
                inputCache.add(token);
                if (inputCache.size() > inputRequest) {
                    inputPos = 0;
                    inputRequest = 0;
                    operation.operate(this);
                    if (inputRequest == 0) {
                        if (inputPos < inputCache.size()) {
                            for (int i = 0; i < inputPos; i++) {
                                inputCache.remove(i);
                            }
                        } else {
                            inputCache = new ArrayList<XBToken>();
                        }
                    }
                }
            } // TODO: else throw new Exception("Buffer capacity overflowed");
        }

        private void flush() throws IOException {
            target.flush();
        }

        private void close() throws IOException {
            target.close();
        }
    }

    @Override
    public void close() throws IOException {
        operator.close();
    }
}
