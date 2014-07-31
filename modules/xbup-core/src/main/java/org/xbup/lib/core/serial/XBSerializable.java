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
package org.xbup.lib.core.serial;

import java.io.IOException;
import java.util.List;
import org.xbup.lib.core.parser.XBProcessingException;

/**
 * Interface is providing serialization methods for serialization into XBUP
 * protocol.
 *
 * @version 0.1 wr23.0 2014/03/03
 * @author XBUP Project (http://xbup.org)
 */
public interface XBSerializable {

    /**
     * Serialization methods for serialization from and to XBUP level 0
     * protocol.
     *
     * @param serialType serialization type
     * @return list of serialization methods
     */
    public List<XBSerialMethod> getSerializationMethods(XBSerializationType serialType);

    /**
     * Perform serialization using particular method.
     *
     * @param serialType serialization type
     * @param methodIndex index of method
     * @param serializationHandler serialization resource
     * @throws XBProcessingException
     * @throws java.io.IOException
     */
    public void serializeXB(XBSerializationType serialType, int methodIndex, XBSerialHandler serializationHandler) throws XBProcessingException, IOException;
}
