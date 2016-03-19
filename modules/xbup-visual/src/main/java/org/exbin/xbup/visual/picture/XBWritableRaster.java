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
package org.exbin.xbup.visual.picture;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.DataBuffer;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.io.InputStream;
import org.exbin.xbup.core.block.XBBlockTerminationMode;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.serial.XBSerializable;
import org.exbin.xbup.core.serial.param.XBPInputSerialHandler;
import org.exbin.xbup.core.serial.param.XBPListener;
import org.exbin.xbup.core.serial.param.XBPOutputSerialHandler;
import org.exbin.xbup.core.serial.param.XBPProvider;
import org.exbin.xbup.core.serial.param.XBPSerializable;

/**
 * Bitmap Picture Raster (Testing only).
 *
 * @version 0.1.24 2015/01/24
 * @author ExBin Project (http://exbin.org)
 */
public class XBWritableRaster extends WritableRaster implements XBPSerializable {

    public static long[] XBUP_BLOCKREV_CATALOGPATH = {0, 4, 0, 0, 0};

    public XBWritableRaster(SampleModel sampleModel, DataBuffer dataBuffer, Rectangle aRegion, Point sampleModelTranslate, WritableRaster parent) {
        super(sampleModel, dataBuffer, aRegion, sampleModelTranslate, parent);
    }

    public XBWritableRaster(SampleModel sampleModel, DataBuffer dataBuffer, Point origin) {
        super(sampleModel, dataBuffer, origin);
    }

    public XBWritableRaster(SampleModel sampleModel, Point origin) {
        super(sampleModel, origin);
    }

    public static void serializeXBWritableRasterToXBT(final WritableRaster source, XBPListener serial) throws XBProcessingException, IOException {
        serial.putBegin(XBBlockTerminationMode.SIZE_SPECIFIED);
        serial.putData(new InputStream() {

            private int posX, posY, val;

            void InputStream(WritableRaster source) {
                posX = 0;
                posY = 0;
                val = 0;
            }

            @Override
            public int read() throws IOException {
                if (posY < 0) {
                    return posY;
                }
                int result = source.getSample(posX, posY, val);
//                        if ((val & 1) == 1) result = result >> 8;
                if (val == 2) {
                    val = 0;
                    if (posX == source.getWidth() - 1) {
                        posX = 0;
                        if (posY == source.getHeight() - 1) {
                            posY = -1;
                        } else {
                            posY++;
                        }
                    } else {
                        posX++;
                    }
                } else {
                    val++;
                }
                return result;
            }

            @Override
            public int available() throws IOException {
                if (posY < 0) {
                    return 0;
                }
                int size = (source.getHeight() - posY - 1) * source.getWidth() * 3
                        + (source.getWidth() - posX) * 3 - val;
                return size;
            }
        });
        serial.putEnd();
    }

    public static void serializeXBWritableRasterFromXBT(WritableRaster source, XBPProvider serial) throws XBProcessingException, IOException {
        serial.pullBegin();
        int posX, posY, val;
        posX = 0;
        posY = 0;
        val = 0;

        InputStream stream = serial.pullData();
        while (true) {
            int input = stream.read();
            source.setSample(posX, posY, val, input);
//                        if ((val & 1) == 1) result = result >> 8;
            if (val == 2) {
                val = 0;
                if (posX == source.getWidth() - 1) {
                    posX = 0;
                    if (posY == source.getHeight() - 1) {
                        break;
                    } else {
                        posY++;
                    }
                } else {
                    posX++;
                }
            } else {
                val++;
            }
        }
        serial.pullEnd();
    }

    public static XBSerializable getXBWritableRasterSerializator(WritableRaster source) {
        return new XBTSerializator(source);
    }

    @Override
    public void serializeFromXB(XBPInputSerialHandler serializationHandler) throws XBProcessingException, IOException {
        serializeXBWritableRasterFromXBT(this, serializationHandler);
    }

    @Override
    public void serializeToXB(XBPOutputSerialHandler serializationHandler) throws XBProcessingException, IOException {
        serializeXBWritableRasterToXBT(this, serializationHandler);
    }

    private static class XBTSerializator implements XBPSerializable {

        private final WritableRaster source;

        public XBTSerializator(WritableRaster source) {
            this.source = source;
        }

        @Override
        public void serializeFromXB(XBPInputSerialHandler serializationHandler) throws XBProcessingException, IOException {
            serializeXBWritableRasterFromXBT(source, serializationHandler);
        }

        @Override
        public void serializeToXB(XBPOutputSerialHandler serializationHandler) throws XBProcessingException, IOException {
            serializeXBWritableRasterToXBT(source, serializationHandler);
        }
    }
}
