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
package org.xbup.lib.core.type;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import org.xbup.lib.core.block.XBTEmptyBlock;
import org.xbup.lib.core.block.declaration.XBDeclBlockType;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.serial.param.XBPInputSerialHandler;
import org.xbup.lib.core.serial.param.XBPOutputSerialHandler;
import org.xbup.lib.core.serial.param.XBPSerializable;

/**
 * Encapsulation class for date &amp; time.
 *
 * @version 0.1.25 2015/03/13
 * @author ExBin Project (http://exbin.org)
 */
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
