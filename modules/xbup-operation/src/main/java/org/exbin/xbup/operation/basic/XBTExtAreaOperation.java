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
 * @version 0.2.0 2016/05/24
 * @author ExBin Project (http://exbin.org)
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
        return XBBasicOperationType.MODIFY_BLOCK;
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
        try {
            if (withUndo) {
                XBData oldData = new XBData();
                InputStream extendedArea = document.getExtendedArea();
                if (extendedArea != null) {
                    oldData.loadFromStream(extendedArea);
                }
                undoOperation = new XBTExtAreaOperation(document, oldData);
            }

            XBData extendedAreaData = new XBData();
            InputStream dataInputStream = getData().getDataInputStream();
            XBPSerialReader reader = new XBPSerialReader(dataInputStream);
            Serializator serial = new Serializator(extendedAreaData);
            reader.read(serial);
            document.setExtendedArea(extendedAreaData.getDataInputStream());
        } catch (XBProcessingException | IOException ex) {
            Logger.getLogger(XBTDeleteBlockOperation.class.getName()).log(Level.SEVERE, null, ex);
            throw new IllegalStateException("Unable to process data");
        }

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
