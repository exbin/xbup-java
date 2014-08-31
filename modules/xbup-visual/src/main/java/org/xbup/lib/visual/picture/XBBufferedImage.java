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
import java.util.List;
import org.xbup.lib.core.block.XBBlockType;
import org.xbup.lib.core.block.declaration.local.XBDBlockType;
import org.xbup.lib.core.block.declaration.catalog.XBPBlockDecl;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.serial.child.XBTChildInputSerialHandler;
import org.xbup.lib.core.serial.child.XBTChildOutputSerialHandler;
import org.xbup.lib.core.serial.child.XBTChildSerializable;
import org.xbup.lib.core.ubnumber.UBNatural;
import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * BufferedImage XBUP level 2 testing serializer.
 *
 * @version 0.1.24 2014/08/24
 * @author XBUP Project (http://xbup.org)
 */
public class XBBufferedImage implements XBTChildSerializable {

    private BufferedImage image;

    public static long[] XB_BLOCK_PATH = {0, 4, 0, 0}; // Testing only

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

    public List<XBBlockType> getXBTransTypes() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void serializeFromXB(XBTChildInputSerialHandler serial) throws XBProcessingException, IOException {
        serial.getType(); //setType(new XBCBlockDecl(xbBlockPath));
        UBNatural width = serial.nextAttribute();
        UBNatural height = serial.nextAttribute();
        BufferedImage result = new BufferedImage(width.getInt(), height.getInt(), BufferedImage.TYPE_INT_RGB);
        serial.nextChild(XBWritableRaster.getXBWritableRasterSerializator(result.getRaster()));
        setImage(result);
        serial.end();
    }

    @Override
    public void serializeToXB(XBTChildOutputSerialHandler serial) throws XBProcessingException, IOException {
        serial.setType(new XBDBlockType(new XBPBlockDecl(XB_BLOCK_PATH)));
        WritableRaster raster = image.getRaster();
        serial.addAttribute(new UBNat32(raster.getWidth()));
        serial.addAttribute(new UBNat32(raster.getHeight()));
        serial.addChild(XBWritableRaster.getXBWritableRasterSerializator(raster));
        serial.end();
    }
}
