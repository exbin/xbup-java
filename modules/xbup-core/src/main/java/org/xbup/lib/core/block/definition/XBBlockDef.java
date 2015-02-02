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
 * XBUP level 1 block definition interface.
 *
 * @version 0.1.25 2015/02/02
 * @author XBUP Project (http://xbup.org)
 */
public interface XBBlockDef extends XBSerializable {

    /**
     * Gets list of parameters in order of appearance.
     *
     * @return list of parameters
     */
    public List<XBBlockParam> getBlockParams();

    /**
     * Gets count of parameter's declarations.
     *
     * @return count of declarations
     */
    public long getParamCount();

    /**
     * Gets revision definition.
     *
     * @return revision definition
     */
    public XBRevisionDef getRevisionDef();

    /**
     * Returns block parameter of given index.
     *
     * @param paramIndex parameter index
     * @return block parameter
     */
    public XBBlockParam getBlockParam(int paramIndex);
}
