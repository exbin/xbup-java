/*
 * Copyright (C) ExBin Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
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
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
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
@ParametersAreNonnullByDefault
public class XBEmptyBlock implements XBBlock, XBPSequenceSerializable {

    private static XBEmptyBlock cachedEmptyBlock = null;

    public XBEmptyBlock() {
    }

    @Nonnull
    @Override
    public Optional<XBBlock> getParentBlock() {
        return Optional.empty();
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
