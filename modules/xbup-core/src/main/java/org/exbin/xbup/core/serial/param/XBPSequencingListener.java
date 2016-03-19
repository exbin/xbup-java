/*
 * Copyright (C) ExBin Project
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
package org.exbin.xbup.core.serial.param;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.exbin.xbup.core.block.XBBlockTerminationMode;
import org.exbin.xbup.core.block.XBBlockType;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.parser.XBProcessingExceptionType;
import org.exbin.xbup.core.parser.token.XBAttribute;
import org.exbin.xbup.core.parser.token.XBTAttributeToken;
import org.exbin.xbup.core.parser.token.XBTBeginToken;
import org.exbin.xbup.core.parser.token.XBTDataToken;
import org.exbin.xbup.core.parser.token.XBTEndToken;
import org.exbin.xbup.core.parser.token.XBTToken;
import org.exbin.xbup.core.parser.token.XBTTypeToken;
import org.exbin.xbup.core.serial.XBSerializable;
import org.exbin.xbup.core.serial.sequence.XBSerialSequenceItem;
import org.exbin.xbup.core.serial.sequence.XBSerialSequenceOp;
import org.exbin.xbup.core.ubnumber.type.UBNat32;

/**
 * Level 2 event listener for performing block building using sequence
 * operations.
 *
 * @version 0.1.24 2015/01/26
 * @author ExBin Project (http://exbin.org)
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
    public void putAttribute(XBAttribute attribute) throws XBProcessingException, IOException {
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
                    break;
                }
                case END: {
                    validate();
                    depth--;
                    break;
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

    @Override
    public void putAppend(XBSerializable serial) throws XBProcessingException, IOException {
        throw new IllegalStateException("Append is not allowed on sequencing");
    }

    public List<XBSerialSequenceItem> getSerialSequence() {
        return serialSequence;
    }

    public XBSerializable getSequenceSerial() {
        return new XBPSerialSequenceWrapper(serialSequence);
    }

    public int getDepth() {
        return depth;
    }

    private static class XBPSerialSequenceWrapper implements XBPSerializable {

        private final List<XBSerialSequenceItem> serialSequence;

        public XBPSerialSequenceWrapper(List<XBSerialSequenceItem> serialSequence) {
            this.serialSequence = serialSequence;
        }

        @Override
        public void serializeFromXB(XBPInputSerialHandler serializationHandler) throws XBProcessingException, IOException {
            throw new IllegalStateException();
        }

        @Override
        public void serializeToXB(XBPOutputSerialHandler serializationHandler) throws XBProcessingException, IOException {
            for (XBSerialSequenceItem serialItem : serialSequence) {
                serializationHandler.putItem(serialItem);
            }
        }
    }
}
