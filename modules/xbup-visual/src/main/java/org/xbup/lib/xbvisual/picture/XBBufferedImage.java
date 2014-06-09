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
package org.xbup.lib.xbvisual.picture;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import org.xbup.lib.xb.block.XBBlockType;
import org.xbup.lib.xb.block.declaration.XBDBlockType;
import org.xbup.lib.xb.block.declaration.XBDeclaration;
import org.xbup.lib.xb.catalog.declaration.XBCDeclaration;
import org.xbup.lib.xb.catalog.declaration.XBCPBlockDecl;
import org.xbup.lib.xb.parser.XBProcessingException;
import org.xbup.lib.xb.serial.XBSerialHandler;
import org.xbup.lib.xb.serial.XBSerialMethod;
import org.xbup.lib.xb.serial.XBSerializable;
import org.xbup.lib.xb.serial.XBSerializationType;
import org.xbup.lib.xb.serial.child.XBTChildListener;
import org.xbup.lib.xb.serial.child.XBTChildListenerSerialMethod;
import org.xbup.lib.xb.serial.child.XBTChildProvider;
import org.xbup.lib.xb.serial.child.XBTChildProviderSerialMethod;
import org.xbup.lib.xb.ubnumber.UBNatural;
import org.xbup.lib.xb.ubnumber.type.UBNat32;

/**
 * BufferedImage XBUP level 2 testing serializer.
 *
 * @version 0.1 wr23.0 2014/03/04
 * @author XBUP Project (http://xbup.org)
 */
public class XBBufferedImage implements XBSerializable {

    private BufferedImage image;

    public static long[] xbBlockPath = {0, 4, 0, 0}; // Testing only

    public XBBufferedImage() {
        image = null;
    }

    public XBBufferedImage(BufferedImage image) {
        this.image = image;
    }

    public XBBufferedImage(ColorModel cm, WritableRaster raster, boolean isRasterPremultiplied, Hashtable<?, ?> properties) {
        image = new BufferedImage(cm, raster, isRasterPremultiplied, properties);
    }

    public XBBufferedImage(int width, int height, int imageType, IndexColorModel cm) {
        image = new BufferedImage(width, height, imageType, cm);
    }

    public XBBufferedImage(int width, int height, int imageType) {
        image = new BufferedImage(width, height, imageType);
    }

    public XBDeclaration getXBDeclaration() {
        return new XBCDeclaration(new XBCPBlockDecl(xbBlockPath));
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
    public List<XBSerialMethod> getSerializationMethods(XBSerializationType serialType) {
        return serialType == XBSerializationType.FROM_XB
                ? Arrays.asList(new XBSerialMethod[]{new XBTChildProviderSerialMethod()})
                : Arrays.asList(new XBSerialMethod[]{new XBTChildListenerSerialMethod()});
    }

    @Override
    public void serializeXB(XBSerializationType serialType, int methodIndex, XBSerialHandler serializationHandler) throws XBProcessingException, IOException {
        if (serialType == XBSerializationType.FROM_XB) {
            XBTChildProvider serial = (XBTChildProvider) serializationHandler;
            serial.getType(); //setType(new XBCBlockDecl(xbBlockPath));
            UBNatural width = serial.nextAttribute();
            UBNatural height = serial.nextAttribute();
            BufferedImage result = new BufferedImage(width.getInt(), height.getInt(), BufferedImage.TYPE_INT_RGB);
            serial.nextChild(XBWritableRaster.getXBWritableRasterSerializator(result.getRaster()), 0);
            setImage(result);
            serial.end();
        } else {
            XBTChildListener serial = (XBTChildListener) serializationHandler;
            serial.setType(new XBDBlockType(new XBCPBlockDecl(xbBlockPath)));
            WritableRaster raster = image.getRaster();
            serial.addAttribute(new UBNat32(raster.getWidth()));
            serial.addAttribute(new UBNat32(raster.getHeight()));
            serial.addChild(XBWritableRaster.getXBWritableRasterSerializator(raster), 0);
            serial.end();
        }
    }
}
