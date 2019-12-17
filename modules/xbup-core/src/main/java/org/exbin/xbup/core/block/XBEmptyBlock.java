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
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.exbin.auxiliary.paged_data.BinaryData;
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
 * Read-only empty level 0 block.
 *
 * @version 0.2.1 2017/05/10
 * @author ExBin Project (http://exbin.org)
 */
public class XBEmptyBlock implements XBBlock, XBPSequenceSerializable {

    private static XBEmptyBlock cachedEmptyBlock = null;

    public XBEmptyBlock() {
    }

    @Nullable
    @Override
    public XBBlock getParent() {
        return null;
    }

    @Nonnull
    @Override
    public XBBlockTerminationMode getTerminationMode() {
        return XBBlockTerminationMode.SIZE_SPECIFIED;
    }

    @Nonnull
    @Override
    public XBBlockDataMode getDataMode() {
        return XBBlockDataMode.DATA_BLOCK;
    }

    @Nullable
    @Override
    public XBAttribute[] getAttributes() {
        return new XBAttribute[0];
    }

    @Nullable
    @Override
    public UBNatural getAttributeAt(int attributeIndex) {
        return null;
    }

    @Override
    public int getAttributesCount() {
        return 0;
    }

    @Nullable
    @Override
    public XBBlock[] getChildren() {
        return new XBBlock[0];
    }

    @Nullable
    @Override
    public XBBlock getChildAt(int childIndex) {
        return null;
    }

    @Override
    public int getChildrenCount() {
        return 0;
    }

    @Nullable
    @Override
    public InputStream getData() {
        return XBTDataToken.createEmptyToken().getData();
    }

    @Nullable
    @Override
    public BinaryData getBlockData() {
        return new XBData();
    }

    @Nonnull
    public static XBEmptyBlock getEmptyBlock() {
        if (cachedEmptyBlock == null) {
            cachedEmptyBlock = new XBEmptyBlock();
        }

        return cachedEmptyBlock;
    }

    @Override
    public void serializeXB(@Nonnull XBPSequenceSerialHandler serializationHandler) throws XBProcessingException, IOException {
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
