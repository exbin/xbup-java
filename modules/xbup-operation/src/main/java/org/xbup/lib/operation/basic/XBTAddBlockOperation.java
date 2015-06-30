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
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xbup.lib.core.block.XBTBlock;
import org.xbup.lib.core.block.XBTEditableBlock;
import org.xbup.lib.core.parser.XBParserMode;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.token.event.XBEventReader;
import org.xbup.lib.core.parser.token.event.XBEventWriter;
import org.xbup.lib.core.parser.token.event.convert.XBEventListenerToListener;
import org.xbup.lib.core.parser.token.event.convert.XBListenerToEventListener;
import org.xbup.lib.core.serial.XBPSerialReader;
import org.xbup.lib.core.serial.XBPSerialWriter;
import org.xbup.lib.core.serial.param.XBPSequenceSerialHandler;
import org.xbup.lib.core.serial.param.XBPSequenceSerializable;
import org.xbup.lib.core.serial.param.XBSerializationMode;
import org.xbup.lib.core.type.XBData;
import org.xbup.lib.operation.Operation;
import org.xbup.lib.operation.XBTDocOperation;
import org.xbup.lib.parser_tree.XBTBlockToXBBlock;
import org.xbup.lib.parser_tree.XBTTreeNode;
import org.xbup.lib.parser_tree.XBTreeReader;
import org.xbup.lib.parser_tree.XBTreeWriter;

/**
 * Operation for adding child block.
 *
 * @version 0.1.25 2015/06/29
 * @author XBUP Project (http://xbup.org)
 */
public class XBTAddBlockOperation extends XBTDocOperation {

    public XBTAddBlockOperation(Long parentPosition, int childIndex, XBTEditableBlock newNode) {
        OutputStream dataOutputStream = getData().getDataOutputStream();
        XBPSerialWriter writer = new XBPSerialWriter(dataOutputStream);
        Serializator serializator = new Serializator(parentPosition == null ? 0 : parentPosition + 1, childIndex, newNode);
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

        if (serial.parentPosition == 0) {
            if (document.getRootBlock() == null) {
                document.setRootBlock(serial.newNode);
            }
        } else {
            XBTEditableBlock parentNode = (XBTEditableBlock) document.findBlockByIndex(serial.parentPosition - 1);
            // If inserted in the middle, shift children blocks (add method to interface for this?)
            if (serial.childIndex < parentNode.getChildrenCount()) {
                parentNode.setChildrenCount(parentNode.getChildrenCount() + 1);
                for (int i = parentNode.getChildrenCount() - 1; i > serial.childIndex; i--) {
                    parentNode.setChildAt(parentNode.getChildAt(i-1), i);
                }
            }
            parentNode.setChildAt(serial.newNode, serial.childIndex);
            serial.newNode.setParent(parentNode);
        }

        if (withUndo) {
            XBTDeleteBlockOperation undoOperation;
            if (serial.parentPosition > 0) {
                XBTEditableBlock parentNode = (XBTEditableBlock) document.findBlockByIndex(serial.parentPosition - 1);
                XBTBlock node = parentNode.getChildAt(serial.childIndex);
                undoOperation = new XBTDeleteBlockOperation(node.getBlockIndex());
            } else {
                undoOperation = new XBTDeleteBlockOperation(0);
            }
            undoOperation.setDocument(document);
            return undoOperation;
        }

        return null;
    }

    private class Serializator implements XBPSequenceSerializable {

        // Position is shifted: 0 mean no parent (for root blocks), others are shifted by 1
        // Should be null supporting natural later?
        private long parentPosition;
        private int childIndex;
        private XBTEditableBlock newNode;

        private Serializator() {
        }

        public Serializator(long parentPosition, int childIndex, XBTEditableBlock newNode) {
            this.parentPosition = parentPosition;
            this.childIndex = childIndex;
            this.newNode = newNode;
        }

        @Override
        public void serializeXB(XBPSequenceSerialHandler serializationHandler) throws XBProcessingException, IOException {
            serializationHandler.begin();
            serializationHandler.matchType();
            if (serializationHandler.getSerializationMode() == XBSerializationMode.PULL) {
                parentPosition = serializationHandler.pullLongAttribute();
                childIndex = serializationHandler.pullIntAttribute();
                newNode = new XBTTreeNode();
                serializationHandler.consist(new XBPSequenceSerializable() {
                    @Override
                    public void serializeXB(XBPSequenceSerialHandler serializationHandler) throws XBProcessingException, IOException {
                        serializationHandler.begin();
                        XBData data = new XBData();
                        data.loadFromStream(serializationHandler.pullData());
                        serializationHandler.end();

                        XBTreeReader treeReader = new XBTreeReader(new XBTBlockToXBBlock(newNode));
                        XBEventReader reader = new XBEventReader(data.getDataInputStream(), XBParserMode.SKIP_EXTENDED);
                        reader.attachXBEventListener(new XBListenerToEventListener(treeReader));
                        reader.read();
                        reader.close();
                    }
                });
            } else {
                serializationHandler.putAttribute(parentPosition);
                serializationHandler.putAttribute(childIndex);
                serializationHandler.consist(new XBPSequenceSerializable() {
                    @Override
                    public void serializeXB(XBPSequenceSerialHandler serializationHandler) throws XBProcessingException, IOException {
                        XBData data = new XBData();
                        XBTreeWriter treeWriter = new XBTreeWriter(new XBTBlockToXBBlock(newNode));
                        XBEventWriter writer = new XBEventWriter(data.getDataOutputStream());
                        treeWriter.generateXB(new XBEventListenerToListener(writer), true);

                        serializationHandler.begin();
                        serializationHandler.putData(data.getDataInputStream());
                        serializationHandler.end();
                    }
                });
            }
            serializationHandler.end();
        }
    }
}
