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
package org.exbin.xbup.core.block;

import java.io.IOException;
import java.io.InputStream;
import org.exbin.utils.binary_data.BinaryData;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.parser.XBProcessingExceptionType;
import org.exbin.xbup.core.parser.token.XBAttribute;
import org.exbin.xbup.core.parser.token.XBTDataToken;
import org.exbin.xbup.core.serial.param.XBPSequenceSerialHandler;
import org.exbin.xbup.core.serial.param.XBPSequenceSerializable;
import org.exbin.xbup.core.serial.param.XBSerializationMode;
import org.exbin.xbup.core.type.XBData;
import org.exbin.xbup.core.ubnumber.UBNatural;

/**
 * Read-only empty level 1 block.
 *
 * @version 0.2.0 2015/09/19
 * @author ExBin Project (http://exbin.org)
 */
public class XBTEmptyBlock implements XBTBlock, XBPSequenceSerializable {

    private static XBTEmptyBlock cachedEmptyBlock = null;

    public XBTEmptyBlock() {
    }

    @Override
    public XBTBlock getParent() {
        return null;
    }

    @Override
    public XBBlockTerminationMode getTerminationMode() {
        return XBBlockTerminationMode.SIZE_SPECIFIED;
    }

    @Override
    public XBBlockDataMode getDataMode() {
        return XBBlockDataMode.DATA_BLOCK;
    }

    @Override
    public XBBlockType getBlockType() {
        return null;
    }

    @Override
    public XBAttribute[] getAttributes() {
        return new XBAttribute[0];
    }

    @Override
    public UBNatural getAttributeAt(int attributeIndex) {
        return null;
    }

    @Override
    public int getAttributesCount() {
        return 0;
    }

    @Override
    public XBTBlock[] getChildren() {
        return new XBTBlock[0];
    }

    @Override
    public XBTBlock getChildAt(int childIndex) {
        return null;
    }

    @Override
    public int getChildrenCount() {
        return 0;
    }

    @Override
    public InputStream getData() {
        return XBTDataToken.createEmptyToken().getData();
    }

    @Override
    public BinaryData getBlockData() {
        return new XBData();
    }

    public static XBTEmptyBlock getEmptyBlock() {
        if (cachedEmptyBlock == null) {
            cachedEmptyBlock = new XBTEmptyBlock();
        }

        return cachedEmptyBlock;
    }

    @Override
    public void serializeXB(XBPSequenceSerialHandler serializationHandler) throws XBProcessingException, IOException {
        serializationHandler.begin();
        if (serializationHandler.getSerializationMode() == XBSerializationMode.PULL) {
            InputStream pullData = serializationHandler.pullData();
            if (pullData.available() > 0) {
                throw new XBProcessingException("Data not empty, when empty data expected", XBProcessingExceptionType.UNEXPECTED_ORDER);
            }
        } else {
            serializationHandler.putData(getData());
        }
        serializationHandler.end();
    }
}
