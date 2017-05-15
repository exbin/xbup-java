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
 * XBUP level 1 group definition interface.
 *
 * @version 0.2.1 2017/05/15
 * @author ExBin Project (http://exbin.org)
 */
public interface XBGroupDef extends XBSerializable {

    /**
     * Returns count of group parameters.
     *
     * @return count of group parameters
     */
    long getParamsCount();

    /**
     * Returns group parameter of given index.
     *
     * @param paramIndex parameter index
     * @return group parameter
     */
    @Nullable
    XBGroupParam getGroupParam(int paramIndex);

    /**
     * Gets list of group parameters.
     *
     * @return list of group parameters
     */
    @Nullable
    List<XBGroupParam> getGroupParams();

    /**
     * Gets revision definition.
     *
     * @return revision definition
     */
    @Nullable
    XBRevisionDef getRevisionDef();
}
