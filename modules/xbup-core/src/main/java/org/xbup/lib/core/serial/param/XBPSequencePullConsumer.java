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
package org.xbup.lib.core.serial.param;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.token.XBTToken;
import org.xbup.lib.core.parser.token.XBTTokenType;
import org.xbup.lib.core.parser.token.pull.XBTPullConsumer;
import org.xbup.lib.core.parser.token.pull.XBTPullProvider;
import org.xbup.lib.core.parser.token.pull.convert.XBTPullPreLoader;
import org.xbup.lib.core.serial.XBPReadSerialHandler;
import org.xbup.lib.core.serial.XBSerializable;

/**
 * Level 2 pull consumer performing block building using sequence operations.
 *
 * @version 0.1.24 2015/01/22
 * @author XBUP Project (http://xbup.org)
 */
public class XBPSequencePullConsumer implements XBTPullConsumer {

    private final XBPReadSerialHandler serialHandler;
    private XBTPullPreLoader pullProvider;
    private XBPSequencingListener sequencingListener;

    private final List<XBSerializable> childSequence = new ArrayList<>();

    public XBPSequencePullConsumer(XBPReadSerialHandler serialHandler) {
        this.serialHandler = serialHandler;
    }

    @Override
    public void attachXBTPullProvider(XBTPullProvider pullProvider) {
        if (pullProvider instanceof XBTPullPreLoader) {
            this.pullProvider = (XBTPullPreLoader) pullProvider;
        } else {
            this.pullProvider = new XBTPullPreLoader(pullProvider);
        }
    }

    public XBTToken pullToken(XBTTokenType tokenType) throws XBProcessingException, IOException {
        // TODO or add separate method for attributes?
        throw new UnsupportedOperationException("Not supported yet.");
//        if (sequencingListener != null) {
//            sequencingListener.putToken(token);
//            if (sequencingListener.isFinished()) {
//                childSequence.add(sequencingListener.getSequenceSerial());
//                sequencingListener = null;
//            }
//        }

        // TODO
    }

    public void pullChild(XBSerializable child) throws XBProcessingException, IOException {
        // TODO include it in list until all attributes are processed
        throw new UnsupportedOperationException("Not supported yet.");
        /* if (sequencingListener != null) {
         sequencingListener.putToken(token);
         if (sequencingListener.isFinished()) {
         childSequence.add(sequencingListener.getSequenceSerial());
         sequencingListener = null;
         }
         } */

    }
}
