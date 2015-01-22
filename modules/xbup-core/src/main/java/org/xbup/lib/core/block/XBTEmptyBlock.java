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
package org.xbup.lib.core.block;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.XBProcessingExceptionType;
import org.xbup.lib.core.parser.token.XBTEmptyDataToken;
import org.xbup.lib.core.serial.param.XBPSequenceSerialHandler;
import org.xbup.lib.core.serial.param.XBPSequenceSerializable;
import org.xbup.lib.core.serial.param.XBSerializationMode;
import org.xbup.lib.core.ubnumber.UBNatural;

/**
 * Interface for read access to XBUP level 1 block.
 *
 * @version 0.1.24 2015/01/18
 * @author XBUP Project (http://xbup.org)
 */
public class XBTEmptyBlock implements XBTBlock, XBPSequenceSerializable {

    private static XBTEmptyBlock cachedEmptyBlock = null;

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
    public List<UBNatural> getAttributes() {
        return null;
    }

    @Override
    public UBNatural getAttribute(int attributeIndex) {
        return null;
    }

    @Override
    public int getAttributesCount() {
        return 0;
    }

    @Override
    public List<XBTBlock> getChildren() {
        return null;
    }

    @Override
    public XBTBlock getChildAt(int childIndex) {
        return null;
    }

    @Override
    public int getChildCount() {
        return 0;
    }

    @Override
    public InputStream getData() {
        return XBTEmptyDataToken.getEmptyToken().getData();
    }

    @Override
    public int getDataSize() {
        return 0;
    }

    @Override
    public int getBlockIndex() {
        return -1;
    }

    @Override
    public long getBlockSize() {
        return 2;
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
