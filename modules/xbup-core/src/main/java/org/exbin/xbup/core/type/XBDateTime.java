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
package org.exbin.xbup.core.type;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.core.block.XBTEmptyBlock;
import org.exbin.xbup.core.block.declaration.XBDeclBlockType;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.serial.param.XBPInputSerialHandler;
import org.exbin.xbup.core.serial.param.XBPOutputSerialHandler;
import org.exbin.xbup.core.serial.param.XBPSerializable;

/**
 * Encapsulation class for date &amp; time.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XBDateTime implements XBPSerializable {

    private Date value;
    static long[] XBUP_BLOCKREV_CATALOGPATH = {0, 0, 14, 0};

    public XBDateTime() {
        this.value = new Date();
    }

    public XBDateTime(Date value) {
        this.value = value;
    }

    public Date getValue() {
        return value;
    }

    public void setValue(Date value) {
        this.value = value;
    }

    @Override
    public void serializeFromXB(XBPInputSerialHandler serial) throws XBProcessingException, IOException {
        if (serial.pullIfEmptyBlock()) {
            value = null;
        } else {
            serial.begin();
            serial.matchType(new XBDeclBlockType(XBUP_BLOCKREV_CATALOGPATH));
            int year = serial.pullIntAttribute();
            int month = serial.pullIntAttribute();
            int day = serial.pullIntAttribute();
            int hour = serial.pullIntAttribute();
            int minute = serial.pullIntAttribute();
            int second = serial.pullIntAttribute();

            Calendar calendar = Calendar.getInstance();
            calendar.clear();
            calendar.set(year, month, day, hour, minute, second);
            value = calendar.getTime();
            serial.end();
        }
    }

    @Override
    public void serializeToXB(XBPOutputSerialHandler serial) throws XBProcessingException, IOException {
        if (value == null) {
            serial.append(XBTEmptyBlock.getEmptyBlock());
        } else {
            serial.begin();
            serial.matchType(new XBDeclBlockType(XBUP_BLOCKREV_CATALOGPATH));

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(value);
            serial.putAttribute(calendar.get(Calendar.YEAR));
            serial.putAttribute(calendar.get(Calendar.MONTH));
            serial.putAttribute(calendar.get(Calendar.DAY_OF_MONTH));
            serial.putAttribute(calendar.get(Calendar.HOUR_OF_DAY));
            serial.putAttribute(calendar.get(Calendar.MINUTE));
            serial.putAttribute(calendar.get(Calendar.SECOND));
            serial.end();
        }
    }
}
