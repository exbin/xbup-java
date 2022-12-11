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
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.core.block.XBTEditableDocument;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.serial.XBPSerialReader;
import org.exbin.xbup.core.serial.XBPSerialWriter;
import org.exbin.xbup.core.serial.param.XBPSequenceSerialHandler;
import org.exbin.xbup.core.serial.param.XBPSequenceSerializable;
import org.exbin.xbup.core.serial.param.XBSerializationMode;
import org.exbin.xbup.core.type.XBData;
import org.exbin.xbup.operation.Operation;
import org.exbin.xbup.operation.XBTDocOperation;

/**
 * Operation for adding child block.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XBTTailDataOperation extends XBTDocOperation {

    public XBTTailDataOperation(XBTEditableDocument document, XBData data) {
        super(document);
        OutputStream dataOutputStream = data.getDataOutputStream();
        XBPSerialWriter writer = new XBPSerialWriter(dataOutputStream);
        Serializator serializator = new Serializator(data);
        writer.write(serializator);
    }

    @Nonnull
    @Override
    public XBBasicOperationType getBasicType() {
        return XBBasicOperationType.MODIFY_BLOCK;
    }

    @Override
    public void execute() throws Exception {
        execute(false);
    }

    @Nonnull
    @Override
    public Optional<Operation> executeWithUndo() throws Exception {
        return execute(true);
    }

    @Nonnull
    private Optional<Operation> execute(boolean withUndo) {
        XBTTailDataOperation undoOperation = null;
        try {
            if (withUndo) {
                XBData oldData = new XBData();
                Optional<InputStream> tailDataStream = document.getTailData();
                if (tailDataStream.isPresent()) {
                    oldData.loadFromStream(tailDataStream.get());
                }
                undoOperation = new XBTTailDataOperation(document, oldData);
            }

            XBData tailData = new XBData();
            InputStream dataInputStream = getData().getDataInputStream();
            XBPSerialReader reader = new XBPSerialReader(dataInputStream);
            Serializator serial = new Serializator(tailData);
            reader.read(serial);
            document.setTailData(tailData.getDataInputStream());
        } catch (XBProcessingException | IOException ex) {
            throw new IllegalStateException("Unable to process data", ex);
        }

        return Optional.ofNullable(undoOperation);
    }

    @ParametersAreNonnullByDefault
    private class Serializator implements XBPSequenceSerializable {

        private final XBData data;

        private Serializator(XBData data) {
            this.data = data;
        }

        @Override
        public void serializeXB(XBPSequenceSerialHandler serializationHandler) throws XBProcessingException, IOException {
            serializationHandler.begin();
            serializationHandler.matchType();
            serializationHandler.consist(new XBPSequenceSerializable() {
                @Override
                public void serializeXB(XBPSequenceSerialHandler serializationHandler) throws XBProcessingException, IOException {
                    serializationHandler.begin();
                    if (serializationHandler.getSerializationMode() == XBSerializationMode.PULL) {
                        if (serializationHandler.pullIfEmptyData()) {
                            data.clear();
                        } else {
                            data.loadFromStream(serializationHandler.pullData());
                        }
                    } else {
                        serializationHandler.putData(data.getDataInputStream());
                    }
                    serializationHandler.end();
                }
            });
            serializationHandler.end();
        }
    }
}
