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
package org.xbup.lib.core.serial.transform;

import java.io.IOException;
import org.xbup.lib.core.block.XBBlockType;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.serial.param.XBPListener;

/**
 * TODO: XBUP level 2 child serialization listener interface.
 *
 * @version 0.1.24 2015/01/21
 * @author ExBin Project (http://exbin.org)
 */
public interface XBAListener extends XBPListener {

    /**
     * Puts block type and request it to be converted to given type.
     *
     * @param type block type
     * @param targetType requested target type
     * @throws XBProcessingException
     * @throws IOException
     */
    public void putType(XBBlockType type, XBBlockType targetType) throws XBProcessingException, IOException;
}
