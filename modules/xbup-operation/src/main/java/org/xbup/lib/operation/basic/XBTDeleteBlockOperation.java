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
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xbup.lib.core.block.XBTBlock;
import org.xbup.lib.core.block.XBTDefaultBlock;
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
 * Operation for deleting child block.
 *
 * @version 0.2.0 2015/09/19
 * @author XBUP Project (http://xbup.org)
 */
public class XBTDeleteBlockOperation extends XBTDocOperation {

    public XBTDeleteBlockOperation(XBTBlock block) {
        long position = XBTDefaultBlock.getBlockIndex(block);
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
        try {
            reader.read(serial);
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBTDeleteBlockOperation.class.getName()).log(Level.SEVERE, null, ex);
            throw new IllegalStateException("Unable to process data");
        }

        XBTAddBlockOperation undoOperation = null;

        if (withUndo) {
            int childIndex = 0;
            Long parentPosition = null;
            XBTEditableBlock deletedNode;
            if (serial.position < 1) {
                deletedNode = (XBTEditableBlock) document.getRootBlock();
            } else {
                deletedNode = (XBTEditableBlock) document.findBlockByIndex(serial.position);
                XBTEditableBlock parentNode = (XBTEditableBlock) deletedNode.getParent();
                parentPosition = (long) XBTDefaultBlock.getBlockIndex(parentNode);
                childIndex = Arrays.asList(parentNode.getChildren()).indexOf(deletedNode);
            }
            undoOperation = new XBTAddBlockOperation(parentPosition, childIndex, deletedNode);
            undoOperation.setDocument(document);
        }

        if (serial.position < 1) {
            document.clear();
        } else {
            XBTEditableBlock node = (XBTEditableBlock) document.findBlockByIndex(serial.position);
            XBTEditableBlock parentNode = (XBTEditableBlock) node.getParent();
            int childIndex = Arrays.asList(parentNode.getChildren()).indexOf(node);
            parentNode.removeChild(childIndex);
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
