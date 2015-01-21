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
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.block.XBBlockType;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.XBProcessingExceptionType;
import org.xbup.lib.core.parser.param.XBPTokenWrapper;
import org.xbup.lib.core.parser.token.XBTAttributeToken;
import org.xbup.lib.core.parser.token.XBTBeginToken;
import org.xbup.lib.core.parser.token.XBTDataToken;
import org.xbup.lib.core.parser.token.XBTEndToken;
import org.xbup.lib.core.parser.token.XBTToken;
import org.xbup.lib.core.parser.token.XBTTypeToken;
import org.xbup.lib.core.serial.XBSerializable;
import org.xbup.lib.core.ubnumber.UBNatural;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * Level 2 event listener for performing block building using sequence
 * operations.
 *
 * @version 0.1.24 2015/01/21
 * @author XBUP Project (http://xbup.org)
 */
public class XBPSequencingListener implements XBPListener {

    private final List<XBSerialSequenceItem> serialSequence = new ArrayList<>();
    private int depth = 0;

    public XBPSequencingListener() {
    }

    /**
     * Returns if processing was finished (or was not started yet).
     *
     * @return true if processing finished or was not started yet.
     */
    public boolean isFinished() {
        return depth == 0;
    }

    private void validate() {
        if (depth == 0) {
            throw new XBProcessingException("Unexpected serialization event when sequencing", XBProcessingExceptionType.WRITING_AFTER_END);
        }
    }

    @Override
    public void putBegin(XBBlockTerminationMode terminationMode) throws XBProcessingException, IOException {
        depth++;
        serialSequence.add(new XBSerialSequenceItem(XBSerialSequenceOp.TOKEN, new XBPTokenWrapper(new XBTBeginToken(terminationMode))));
    }

    @Override
    public void putType(XBBlockType type) throws XBProcessingException, IOException {
        validate();
        serialSequence.add(new XBSerialSequenceItem(XBSerialSequenceOp.TOKEN, new XBPTokenWrapper(new XBTTypeToken(type))));
    }

    @Override
    public void putAttribute(UBNatural attribute) throws XBProcessingException, IOException {
        validate();
        serialSequence.add(new XBSerialSequenceItem(XBSerialSequenceOp.TOKEN, new XBPTokenWrapper(new XBTAttributeToken(attribute))));
    }

    @Override
    public void putAttribute(byte attributeValue) throws XBProcessingException, IOException {
        validate();
        putAttribute(new UBNat32(attributeValue));
    }

    @Override
    public void putAttribute(short attributeValue) throws XBProcessingException, IOException {
        validate();
        putAttribute(new UBNat32(attributeValue));
    }

    @Override
    public void putAttribute(int attributeValue) throws XBProcessingException, IOException {
        validate();
        putAttribute(new UBNat32(attributeValue));
    }

    @Override
    public void putAttribute(long attributeValue) throws XBProcessingException, IOException {
        validate();
        putAttribute(new UBNat32(attributeValue));
    }

    @Override
    public void putData(InputStream data) throws XBProcessingException, IOException {
        validate();
        serialSequence.add(new XBSerialSequenceItem(XBSerialSequenceOp.TOKEN, new XBPTokenWrapper(new XBTDataToken(data))));
    }

    @Override
    public void putEnd() throws XBProcessingException, IOException {
        validate();
        serialSequence.add(new XBSerialSequenceItem(XBSerialSequenceOp.TOKEN, new XBPTokenWrapper(new XBTEndToken())));
        depth--;
    }

    @Override
    public void putToken(XBTToken token) throws XBProcessingException, IOException {
        putItem(new XBSerialSequenceItem(XBSerialSequenceOp.TOKEN, new XBPTokenWrapper(token)));
    }

    @Override
    public void putConsist(XBSerializable serial) throws XBProcessingException, IOException {
        validate();
        serialSequence.add(new XBSerialSequenceItem(XBSerialSequenceOp.CONSIST, serial));
    }

    @Override
    public void putJoin(XBSerializable serial) throws XBProcessingException, IOException {
        validate();
        serialSequence.add(new XBSerialSequenceItem(XBSerialSequenceOp.JOIN, serial));
    }

    @Override
    public void putListConsist(XBSerializable serial) throws XBProcessingException, IOException {
        validate();
        serialSequence.add(new XBSerialSequenceItem(XBSerialSequenceOp.LIST_CONSIST, serial));
    }

    @Override
    public void putListJoin(XBSerializable serial) throws XBProcessingException, IOException {
        validate();
        serialSequence.add(new XBSerialSequenceItem(XBSerialSequenceOp.LIST_JOIN, serial));
    }

    @Override
    public void putItem(XBSerialSequenceItem item) throws XBProcessingException, IOException {
        if (item.getSequenceOp() == XBSerialSequenceOp.TOKEN) {
            switch (((XBPTokenWrapper) item.getItem()).getToken().getTokenType()) {
                case BEGIN: {
                    depth++;
                }
                case END: {
                    validate();
                    depth--;
                }
                default: {
                    validate();
                }
            }

            serialSequence.add(item);
        } else {
            validate();
            serialSequence.add(item);
        }
    }

    public List<XBSerialSequenceItem> getSerialSequence() {
        return serialSequence;
    }

    public XBSerializable getSequenceSerial() {
        return new XBPSerialSequenceWrapper(serialSequence);
    }

    public class XBPSerialSequenceWrapper implements XBPChildSerializable {

        private final List<XBSerialSequenceItem> serialSequence;

        public XBPSerialSequenceWrapper(List<XBSerialSequenceItem> serialSequence) {
            this.serialSequence = serialSequence;
        }

        @Override
        public void serializeFromXB(XBPChildInputSerialHandler serializationHandler) throws XBProcessingException, IOException {
            throw new IllegalStateException();
        }

        @Override
        public void serializeToXB(XBPChildOutputSerialHandler serializationHandler) throws XBProcessingException, IOException {
            for (XBSerialSequenceItem serialItem : serialSequence) {
                serializationHandler.putItem(serialItem);
            }
        }
    }
}