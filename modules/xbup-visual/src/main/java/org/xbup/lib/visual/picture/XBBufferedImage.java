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
package org.xbup.lib.visual.picture;

import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;
import java.io.IOException;
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.block.declaration.XBDeclBlockType;
import org.xbup.lib.core.block.declaration.local.XBLBlockDecl;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.serial.param.XBPInputSerialHandler;
import org.xbup.lib.core.serial.param.XBPOutputSerialHandler;
import org.xbup.lib.core.serial.param.XBPSerializable;
import org.xbup.lib.core.ubnumber.UBNatural;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * BufferedImage serialization wrapper.
 *
 * @version 0.1.25 2015/02/03
 * @author XBUP Project (http://xbup.org)
 */
public class XBBufferedImage implements XBPSerializable {

    private BufferedImage image;

    public static long[] XBUP_BLOCKREV_CATALOGPATH = {1, 4, 0, 0, 2, 0};
    public static long[] XBUP_FORMATREV_CATALOGPATH = {1, 4, 0, 1, 0};

    public XBBufferedImage() {
        image = null;
    }

    public XBBufferedImage(BufferedImage image) {
        this.image = image;
    }

    public XBBufferedImage(int width, int height, int imageType, IndexColorModel cm) {
        image = new BufferedImage(width, height, imageType, cm);
    }

    public XBBufferedImage(int width, int height, int imageType) {
        image = new BufferedImage(width, height, imageType);
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    @Override
    public void serializeFromXB(XBPInputSerialHandler serial) throws XBProcessingException, IOException {
        serial.pullBegin();
        serial.matchType(new XBDeclBlockType(XBUP_BLOCKREV_CATALOGPATH));
        UBNatural width = serial.pullAttribute().convertToNatural();
        UBNatural height = serial.pullAttribute().convertToNatural();
        BufferedImage result = new BufferedImage(width.getInt(), height.getInt(), BufferedImage.TYPE_INT_RGB);
        serial.pullConsist(XBWritableRaster.getXBWritableRasterSerializator(result.getRaster()));
        setImage(result);
        serial.pullEnd();
    }

    @Override
    public void serializeToXB(XBPOutputSerialHandler serial) throws XBProcessingException, IOException {
        serial.putBegin(XBBlockTerminationMode.SIZE_SPECIFIED);
        serial.putType(new XBDeclBlockType(XBUP_BLOCKREV_CATALOGPATH));
        WritableRaster raster = image.getRaster();
        serial.putAttribute(new UBNat32(raster.getWidth()));
        serial.putAttribute(new UBNat32(raster.getHeight()));
        serial.putConsist(XBWritableRaster.getXBWritableRasterSerializator(raster));
        serial.putEnd();
    }
}
