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
package org.exbin.xbup.core.block.definition;

import java.util.List;
import javax.annotation.Nullable;
import org.exbin.xbup.core.serial.XBSerializable;

/**
 * XBUP level 1 block definition interface.
 *
 * @version 0.2.1 2017/05/15
 * @author ExBin Project (http://exbin.org)
 */
public interface XBBlockDef extends XBSerializable {

    /**
     * Gets count of parameter's declarations.
     *
     * @return count of declarations
     */
    long getParamsCount();

    /**
     * Returns block parameter of given index.
     *
     * @param paramIndex parameter index
     * @return block parameter
     */
    @Nullable
    XBBlockParam getBlockParam(int paramIndex);

    /**
     * Gets list of parameters in order of appearance.
     *
     * @return list of parameters
     */
    @Nullable
    List<XBBlockParam> getBlockParams();

    /**
     * Gets revision definition.
     *
     * @return revision definition
     */
    @Nullable
    XBRevisionDef getRevisionDef();
}
