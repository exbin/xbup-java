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
package org.xbup.lib.core.block.definition;

import java.util.List;
import org.xbup.lib.core.serial.XBSerializable;

/**
 * XBUP level 1 format definition interface.
 *
 * @version 0.1.25 2015/02/20
 * @author XBUP Project (http://xbup.org)
 */
public interface XBFormatDef extends XBSerializable {

    /**
     * Returns count of parameters.
     *
     * @return count of parameters
     */
    public long getParamsCount();

    /**
     * Gets list of format parameters.
     *
     * @return list of format parameters
     */
    public List<XBFormatParam> getFormatParams();

    /**
     * Returns revision definition.
     *
     * @return revision definition
     */
    public XBRevisionDef getRevisionDef();

    /**
     * Returns format parameter of given index.
     *
     * @param paramIndex parameter index
     * @return format parameter
     */
    public XBFormatParam getFormatParam(int paramIndex);
}
