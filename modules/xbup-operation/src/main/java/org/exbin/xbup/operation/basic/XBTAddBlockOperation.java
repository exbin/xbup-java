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
package org.exbin.xbup.operation.basic;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.exbin.xbup.core.block.XBTBlock;
import org.exbin.xbup.core.block.XBTEditableBlock;
import org.exbin.xbup.core.block.XBTEditableDocument;
import org.exbin.xbup.core.parser.XBParserMode;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.parser.token.event.XBEventReader;
import org.exbin.xbup.core.parser.token.event.XBEventWriter;
import org.exbin.xbup.core.parser.token.event.convert.XBEventListenerToListener;
import org.exbin.xbup.core.parser.token.event.convert.XBListenerToEventListener;
import org.exbin.xbup.core.serial.XBPSerialReader;
import org.exbin.xbup.core.serial.XBPSerialWriter;
import org.exbin.xbup.core.serial.param.XBPSequenceSerialHandler;
import org.exbin.xbup.core.serial.param.XBPSequenceSerializable;
import org.exbin.xbup.core.serial.param.XBSerializationMode;
import org.exbin.xbup.core.type.XBData;
import org.exbin.xbup.operation.Operation;
import org.exbin.xbup.operation.XBTDocOperation;
import org.exbin.xbup.parser_tree.XBTBlockToXBBlock;
import org.exbin.xbup.parser_tree.XBTTreeNode;
import org.exbin.xbup.parser_tree.XBTreeReader;
import org.exbin.xbup.parser_tree.XBTreeWriter;

/**
 * Operation for adding child block.
 *
 * @version 0.2.0 2015/09/19
 * @author ExBin Project (http://exbin.org)
 */
public class XBTAddBlockOperation extends XBTDocOperation {

    public XBTAddBlockOperation(XBTEditableDocument document, Long parentPosition, int childIndex, XBTEditableBlock newNode) {
        super(document);
        OutputStream dataOutputStream = data.getDataOutputStream();
        XBPSerialWriter writer = new XBPSerialWriter(dataOutputStream);
        Serializator serializator = new Serializator(parentPosition == null ? 0 : parentPosition + 1, childIndex, newNode);
        writer.write(serializator);
    }

    @Override
    public XBBasicOperationType getBasicType() {
        return XBBasicOperationType.ADD_BLOCK;
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
                undoOperation = new XBTDeleteBlockOperation(document, node);
            } else {
                undoOperation = new XBTDeleteBlockOperation(document, document.getRootBlock());
            }
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
                        XBEventReader reader = new XBEventReader(data.getDataInputStream(), XBParserMode.SKIP_TAIL);
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
                        treeWriter.attachXBListener(new XBEventListenerToListener(writer));

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
