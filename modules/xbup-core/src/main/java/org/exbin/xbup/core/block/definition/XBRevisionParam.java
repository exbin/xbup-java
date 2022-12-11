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
package org.exbin.xbup.core.block.definition;

import java.io.IOException;
import org.exbin.xbup.core.block.XBBasicBlockType;
import org.exbin.xbup.core.block.XBFixedBlockType;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.serial.param.XBPSequenceSerialHandler;
import org.exbin.xbup.core.serial.param.XBPSequenceSerializable;
import org.exbin.xbup.core.serial.param.XBSerializationMode;

/**
 * XBUP level 1 revision parameter.
 *
 * @author ExBin Project (https://exbin.org)
 */
public class XBRevisionParam implements XBPSequenceSerializable {

    private long paramCount;

    public XBRevisionParam() {
    }

    public XBRevisionParam(long paramCount) {
        this.paramCount = paramCount;
    }

    public long getParamCount() {
        return paramCount;
    }

    public void setParamCount(long paramCount) {
        this.paramCount = paramCount;
    }

    @Override
    public void serializeXB(XBPSequenceSerialHandler serial) throws XBProcessingException, IOException {
        serial.begin();
        serial.matchType(new XBFixedBlockType(XBBasicBlockType.REVISION_PARAMETER));
        if (serial.getSerializationMode() == XBSerializationMode.PULL) {
            paramCount = serial.pullLongAttribute();
        } else {
            serial.putAttribute(paramCount);
        }
        serial.end();
    }
}
