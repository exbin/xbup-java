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
package org.exbin.xbup.core.type;

import java.io.IOException;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.auxiliary.paged_data.BinaryData;
import org.exbin.auxiliary.paged_data.PagedData;
import org.exbin.xbup.core.block.XBBlockTerminationMode;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.serial.child.XBTChildInputSerialHandler;
import org.exbin.xbup.core.serial.child.XBTChildOutputSerialHandler;
import org.exbin.xbup.core.serial.child.XBTChildSerializable;

/**
 * Encapsulation class for binary blob.
 *
 * Data are stored using paging. Last page might be shorter than page size, but
 * not empty.
 *
 * @version 0.2.0 2016/05/24
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XBData extends PagedData implements XBTChildSerializable {

    public XBData() {
        super();
    }

    public XBData(int pageSize) {
        super(pageSize);
    }

    public void setData(BinaryData newData) {
        clear();
        insert(0, newData);
    }

    @Override
    public void serializeFromXB(XBTChildInputSerialHandler serial) throws XBProcessingException, IOException {
        serial.pullBegin();
        loadFromStream(serial.pullData());
        serial.pullEnd();
    }

    @Override
    public void serializeToXB(XBTChildOutputSerialHandler serial) throws XBProcessingException, IOException {
        serial.putBegin(XBBlockTerminationMode.SIZE_SPECIFIED);
        serial.putData(getDataInputStream());
        serial.putEnd();
    }
}
