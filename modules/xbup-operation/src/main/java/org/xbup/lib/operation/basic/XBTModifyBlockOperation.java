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
import org.xbup.lib.core.block.XBTBlock;
import org.xbup.lib.core.block.XBTEditableBlock;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.basic.convert.XBTListenerToConsumer;
import org.xbup.lib.core.parser.basic.convert.XBTProviderToProducer;
import org.xbup.lib.core.parser.token.event.convert.XBListenerToEventListener;
import org.xbup.lib.core.parser.token.event.convert.XBTEventListenerToListener;
import org.xbup.lib.core.parser.token.event.convert.XBTToXBEventUnwrapper;
import org.xbup.lib.core.parser.token.pull.convert.XBProviderToPullProvider;
import org.xbup.lib.core.parser.token.pull.convert.XBTPullProviderToProvider;
import org.xbup.lib.core.parser.token.pull.convert.XBTToXBPullWrapper;
import org.xbup.lib.core.serial.XBPSerialReader;
import org.xbup.lib.core.serial.XBPSerialWriter;
import org.xbup.lib.core.serial.basic.XBTBasicInputSerialHandler;
import org.xbup.lib.core.serial.basic.XBTBasicOutputSerialHandler;
import org.xbup.lib.core.serial.basic.XBTBasicSerializable;
import org.xbup.lib.core.serial.param.XBPSequenceSerialHandler;
import org.xbup.lib.core.serial.param.XBPSequenceSerializable;
import org.xbup.lib.core.serial.param.XBSerializationMode;
import org.xbup.lib.operation.Operation;
import org.xbup.lib.operation.XBTDocOperation;
import org.xbup.lib.parser_tree.XBTBlockToXBBlock;
import org.xbup.lib.parser_tree.XBTTreeNode;
import org.xbup.lib.parser_tree.XBTreeReader;
import org.xbup.lib.parser_tree.XBTreeWriter;

/**
 * Command for adding child block.
 *
 * @version 0.1.25 2015/06/24
 * @author XBUP Project (http://xbup.org)
 */
public class XBTModifyBlockOperation extends XBTDocOperation {

    public XBTModifyBlockOperation(long position, XBTEditableBlock newNode) {
        OutputStream dataOutputStream = getData().getDataOutputStream();
        XBPSerialWriter writer = new XBPSerialWriter(dataOutputStream);
        Serializator serializator = new Serializator(position, newNode);
        writer.write(serializator);
    }

    @Override
    public XBBasicOperationType getBasicType() {
        return XBBasicOperationType.MODIFY_NODE;
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

        XBTEditableBlock node = (XBTEditableBlock) document.findBlockByIndex(serial.position);
        XBTEditableBlock parentNode = (XBTEditableBlock) node.getParent();
        if (parentNode == null) {
            document.setRootBlock(serial.newNode);
        } else {
            // Find child index of node
            int childPosition = 0;
            do {
                XBTBlock child = parentNode.getChildAt(childPosition);
                if (child == node) {
                    break;
                }

                childPosition++;
            } while (childPosition < parentNode.getChildrenCount());

            if (childPosition == parentNode.getChildrenCount()) {
                throw new IllegalStateException("Unexpected missing child block");
            }

            parentNode.setChildAt(serial.newNode, childPosition);
            serial.newNode.setParent(parentNode);
        }

        if (withUndo) {
            XBTModifyBlockOperation undoOperation;
            undoOperation = new XBTModifyBlockOperation(serial.position, node);
            undoOperation.setDocument(document);
            return undoOperation;
        }

        return null;
    }

    private class Serializator implements XBPSequenceSerializable {

        private long position;
        private XBTEditableBlock newNode;

        private Serializator() {
        }

        public Serializator(long position, XBTEditableBlock newNode) {
            this.position = position;
            this.newNode = newNode;
        }

        @Override
        public void serializeXB(XBPSequenceSerialHandler serializationHandler) throws XBProcessingException, IOException {
            serializationHandler.begin();
            serializationHandler.matchType();
            if (serializationHandler.getSerializationMode() == XBSerializationMode.PULL) {
                position = serializationHandler.pullLongAttribute();
                newNode = new XBTTreeNode();
                serializationHandler.consist(new XBTBasicSerializable() {
                    @Override
                    public void serializeFromXB(XBTBasicInputSerialHandler serializationHandler) throws XBProcessingException, IOException {
                        XBTreeReader serialReader = new XBTreeReader(new XBTBlockToXBBlock(newNode));
                        serializationHandler.process(new XBTListenerToConsumer(new XBTEventListenerToListener(new XBTToXBEventUnwrapper(new XBListenerToEventListener(serialReader)))));
                    }

                    @Override
                    public void serializeToXB(XBTBasicOutputSerialHandler serializationHandler) throws XBProcessingException, IOException {
                    }
                });
            } else {
                serializationHandler.putAttribute(position);
                serializationHandler.consist(new XBTBasicSerializable() {
                    @Override
                    public void serializeFromXB(XBTBasicInputSerialHandler serializationHandler) throws XBProcessingException, IOException {
                    }

                    @Override
                    public void serializeToXB(XBTBasicOutputSerialHandler serializationHandler) throws XBProcessingException, IOException {
                        XBTreeWriter serialWriter = new XBTreeWriter(new XBTBlockToXBBlock(newNode));
                        serializationHandler.process(new XBTProviderToProducer(new XBTPullProviderToProvider(new XBTToXBPullWrapper(new XBProviderToPullProvider(serialWriter)))));
                    }
                });
            }
            serializationHandler.end();
        }
    }
}
