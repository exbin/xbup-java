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
package org.xbup.lib.core.parser.token.operation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.xbup.lib.core.parser.token.TypedToken;

/**
 * XBUP token operator filter.
 *
 * @version 0.1.23 2014/02/07
 * @author XBUP Project (http://xbup.org)
 * @param <T> source token generic type
 * @param <U> target token generic type
 */
public class XBOperatorFilter<T extends TypedToken, U extends TypedToken> {

    private final MyOperator operator;

    public XBOperatorFilter(XBOperation<T, U> operation) {
        operator = new MyOperator(operation);
    }

    private class MyOperator extends XBTokenOperator<T, U> {

        private int inputMax;
        private int inputRequest;
        private List<T> inputCache;
        private final XBOperation<T, U> operation;
//        private XBL0EventListener listener;

        public MyOperator(XBOperation<T, U> operation) {
            this.operation = operation;
            reset();
        }

        @Override
        public void putToken(U event) throws IOException {
            /*            try {
             listener.performXBEvent(event);
             } catch (XBParseException ex) {
             Logger.getLogger(XBOperatorFilter.class.getName()).log(Level.SEVERE, null, ex);
             } */
        }

        @Override
        public T getToken() throws IOException {
            if (inputCache.size() > 0) {
                return inputCache.remove(0);
            } else {
                throw new IOException("No event available in cache");
            }
        }

        @Override
        public void reset() {
            inputMax = 0;
            inputRequest = 0;
            inputCache = new ArrayList<>();
        }

        @Override
        public void setInputCache(int size) {
            inputMax = size;
        }

        @Override
        public void setOutputCache(int size) {
        }

        @Override
        public int availableInput() {
            if (inputCache == null) {
                return 0;
            }

            return inputCache.size();
        }

        @Override
        public int availableOutput() {
            return 0;
        }

        @Override
        public void request(int count) {
//            if (inputMax>0) if (count > inputMax) throw new Exception("Requested capacity overflow ");
            inputRequest = count;
        }
    }
}
