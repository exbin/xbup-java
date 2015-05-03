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
package org.xbup.lib.operation.basic;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.xbup.lib.core.block.XBTEditableBlock;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.serial.XBPSerialReader;
import org.xbup.lib.core.serial.XBPSerialWriter;
import org.xbup.lib.core.serial.param.XBPSequenceSerialHandler;
import org.xbup.lib.core.serial.param.XBPSequenceSerializable;
import org.xbup.lib.core.serial.param.XBSerializationMode;
import org.xbup.lib.operation.Operation;
import org.xbup.lib.operation.XBTDocOperation;

/**
 * Command for deleting child block.
 *
 * @version 0.1.25 2015/05/03
 * @author XBUP Project (http://xbup.org)
 */
public class XBTDeleteBlockOperation extends XBTDocOperation {

    public XBTDeleteBlockOperation(long position) {
        OutputStream dataOutputStream = getData().getDataOutputStream();
        XBPSerialWriter writer = new XBPSerialWriter(dataOutputStream);
        Serializator serializator = new Serializator(position);
        writer.write(serializator);
    }

    @Override
    public XBBasicOperationType getBasicType() {
        return XBBasicOperationType.ADD_NODE;
    }

    @Override
    public void execute() throws Exception {
        execute(false);
    }

    @Override
    public Operation executeWithUndo() throws Exception {
        return execute(true);
    }

    private Operation execute(boolean withUndo) {
        InputStream dataInputStream = getData().getDataInputStream();
        XBPSerialReader reader = new XBPSerialReader(dataInputStream);
        Serializator serial = new Serializator();
        reader.read(serial);

        XBTAddBlockOperation undoOperation = null;

        if (withUndo) {
            int childIndex = 0;
            long parentPosition = -1;
            XBTEditableBlock deletedNode;
            if (serial.position < 1) {
                deletedNode = (XBTEditableBlock) document.getRootBlock();
            } else {
                deletedNode = (XBTEditableBlock) document.findBlockByIndex(serial.position);
                XBTEditableBlock parentNode = (XBTEditableBlock) deletedNode.getParent();
                parentPosition = parentNode.getBlockIndex();
                childIndex = parentNode.getChildren().indexOf(deletedNode);
            }
            undoOperation = new XBTAddBlockOperation(parentPosition, childIndex, deletedNode);
            undoOperation.setDocument(document);
        }

        if (serial.position < 1) {
            document.clear();
        } else {
            XBTEditableBlock node = (XBTEditableBlock) document.findBlockByIndex(serial.position);
            XBTEditableBlock parentNode = (XBTEditableBlock) node.getParent();
            parentNode.getChildren().remove(node);
        }

        return undoOperation;
    }

    private class Serializator implements XBPSequenceSerializable {

        private long position;

        private Serializator() {
        }

        public Serializator(long position) {
            this.position = position;
        }

        @Override
        public void serializeXB(XBPSequenceSerialHandler serializationHandler) throws XBProcessingException, IOException {
            serializationHandler.begin();
            serializationHandler.matchType();
            if (serializationHandler.getSerializationMode() == XBSerializationMode.PULL) {
                position = serializationHandler.pullLongAttribute();
            } else {
                serializationHandler.putAttribute(position);
            }
            serializationHandler.end();
        }
    }
}
