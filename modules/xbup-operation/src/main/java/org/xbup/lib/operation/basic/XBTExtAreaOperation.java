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
import org.xbup.lib.core.block.XBTEditableDocument;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.serial.XBPSerialReader;
import org.xbup.lib.core.serial.XBPSerialWriter;
import org.xbup.lib.core.serial.param.XBPSequenceSerialHandler;
import org.xbup.lib.core.serial.param.XBPSequenceSerializable;
import org.xbup.lib.core.serial.param.XBSerializationMode;
import org.xbup.lib.core.type.XBData;
import org.xbup.lib.operation.Operation;
import org.xbup.lib.operation.XBTDocOperation;

/**
 * Operation for adding child block.
 *
 * @version 0.2.0 2016/02/27
 * @author XBUP Project (http://xbup.org)
 */
public class XBTExtAreaOperation extends XBTDocOperation {

    public XBTExtAreaOperation(XBTEditableDocument document, XBData data) {
        super(document);
        OutputStream dataOutputStream = data.getDataOutputStream();
        XBPSerialWriter writer = new XBPSerialWriter(dataOutputStream);
        Serializator serializator = new Serializator(data);
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
        XBTExtAreaOperation undoOperation = null;
        if (withUndo) {
            XBData oldData = new XBData();
            oldData.loadFromStream(document.getExtendedArea());
            undoOperation = new XBTExtAreaOperation(document, oldData);
        }

        XBData data = new XBData();
        InputStream dataInputStream = getData().getDataInputStream();
        XBPSerialReader reader = new XBPSerialReader(dataInputStream);
        Serializator serial = new Serializator(data);
        try {
            reader.read(serial);
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBTDeleteBlockOperation.class.getName()).log(Level.SEVERE, null, ex);
            throw new IllegalStateException("Unable to process data");
        }
        document.setExtendedArea(data.getDataInputStream());

        return undoOperation;
    }

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
