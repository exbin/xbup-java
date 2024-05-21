/*
 * Copyright (C) ExBin Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.exbin.xbup.operation.basic;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
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
import org.exbin.xbup.operation.undo.UndoableOperation;
import org.exbin.xbup.parser_tree.XBTBlockToXBBlock;
import org.exbin.xbup.parser_tree.XBTTreeNode;
import org.exbin.xbup.parser_tree.XBTreeReader;
import org.exbin.xbup.parser_tree.XBTreeWriter;

/**
 * Operation for adding child block.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XBTModifyBlockOperation extends XBTDocOperation {

    public XBTModifyBlockOperation(XBTEditableDocument document, long position, XBTEditableBlock newNode) {
        super(document);
        OutputStream dataOutputStream = data.getDataOutputStream();
        XBPSerialWriter writer = new XBPSerialWriter(dataOutputStream);
        Serializator serializator = new Serializator(position, newNode);
        writer.write(serializator);
    }

    @Override
    public XBBasicOperationType getBasicType() {
        return XBBasicOperationType.MODIFY_BLOCK;
    }

    @Override
    public void execute() {
        execute(false);
    }

    @Nonnull
    @Override
    public UndoableOperation executeWithUndo() {
        return (UndoableOperation) execute(true).get();
    }

    @Nonnull
    private Optional<Operation> execute(boolean withUndo) {
        InputStream dataInputStream = getData().getDataInputStream();
        XBPSerialReader reader = new XBPSerialReader(dataInputStream);
        Serializator serial = new Serializator();
        try {
            reader.read(serial);
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBTDeleteBlockOperation.class.getName()).log(Level.SEVERE, null, ex);
            throw new IllegalStateException("Unable to process data");
        }

        XBTEditableBlock node = (XBTEditableBlock) document.findBlockByIndex(serial.position).get();
        Optional<XBTBlock> optParentNode = node.getParentBlock();
        if (!optParentNode.isPresent()) {
            document.setRootBlock(serial.newNode);
        } else {
            XBTEditableBlock parentNode = (XBTEditableBlock) optParentNode.get();
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
            undoOperation = new XBTModifyBlockOperation(document, serial.position, node);
            return Optional.of(undoOperation);
        }

        return Optional.empty();
    }

    @ParametersAreNonnullByDefault
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
                serializationHandler.putAttribute(position);
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
