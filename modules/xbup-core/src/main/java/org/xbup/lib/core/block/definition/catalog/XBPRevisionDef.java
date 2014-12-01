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
package org.xbup.lib.core.block.definition.catalog;

import java.util.List;
import org.xbup.lib.core.block.definition.XBRevisionDef;
import org.xbup.lib.core.block.definition.XBRevisionParam;
import org.xbup.lib.core.serial.XBSerializable;

/**
 * XBUP level 1 revision definition.
 *
 * @version 0.1.24 2014/11/30
 * @author XBUP Project (http://xbup.org)
 */
public class XBPRevisionDef implements XBRevisionDef, XBSerializable {

    private long[] catalogPath;
    private SpecType specType;

    public XBPRevisionDef(long[] catalogPath, SpecType specType) {
        this.catalogPath = catalogPath;
        this.specType = specType;
    }

    @Override
    public List<XBRevisionParam> getRevParams() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public enum SpecType {

        FORMAT, GROUP, BLOCK
    }
}
