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
package org.exbin.xbup.core.block;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import javax.annotation.Nonnull;
import org.exbin.auxiliary.binary_data.BinaryData;
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
 * @author ExBin Project (https://exbin.org)
 */
public class XBTEmptyBlock implements XBTBlock, XBPSequenceSerializable {

    private static XBTEmptyBlock cachedEmptyBlock = null;

    public XBTEmptyBlock() {
    }

    @Nonnull
    @Override
    public Optional<XBTBlock> getParentBlock() {
        return Optional.empty();
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

    @Nonnull
    @Override
    public InputStream getData() {
        return XBTDataToken.createEmptyToken().getData();
    }

    @Nonnull
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
