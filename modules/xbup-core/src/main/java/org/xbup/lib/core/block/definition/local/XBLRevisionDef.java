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
package org.xbup.lib.core.block.definition.local;

import java.util.ArrayList;
import java.util.List;
import org.xbup.lib.core.block.definition.XBRevisionDef;
import org.xbup.lib.core.block.definition.XBRevisionParam;
import org.xbup.lib.core.serial.XBSerializable;

/**
 * XBUP level 1 local group definition.
 *
 * @version 0.1.24 2015/01/18
 * @author XBUP Project (http://xbup.org)
 */
public class XBLRevisionDef implements XBSerializable, XBRevisionDef {

    private List<XBRevisionParam> revs;

    public XBLRevisionDef() {
        revs = new ArrayList<>();
    }

    @Override
    public List<XBRevisionParam> getRevParams() {
        return revs;
    }

    @Override
    public int getRevisionLimit(long revision) {
        if (revision > revs.size()) {
            revision = revs.size() - 1;
        }

        int limit = 0;
        for (int index = 0; index <= revision; index++) {
           limit += revs.get(index).getLimit();
        }
        
        return limit;
    }
}
