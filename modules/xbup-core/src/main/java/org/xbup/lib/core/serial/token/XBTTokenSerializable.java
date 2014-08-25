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
package org.xbup.lib.core.serial.token;

import java.io.IOException;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.serial.XBSerializable;

/**
 * Interface is providing serialization methods for serialization into XBUP
 * level 1 protocol.
 *
 * @version 0.1 wr24.0 2014/08/23
 * @author XBUP Project (http://xbup.org)
 */
public interface XBTTokenSerializable extends XBSerializable {

    /**
     * Method to perform serialization from XBUP protocol.
     *
     * @param serializationHandler serialization resource
     * @throws XBProcessingException if proccesing problem encountered
     * @throws java.io.IOException if input/output problem encountered
     */
    public void serializeFromXB(XBTTokenInputSerialHandler serializationHandler) throws XBProcessingException, IOException;

    /**
     * Method to perform serialization to XBUP protocol.
     *
     * @param serializationHandler serialization resource
     * @throws XBProcessingException if proccesing problem encountered
     * @throws java.io.IOException if input/output problem encountered
     */
    public void serializeToXB(XBTTokenOutputSerialHandler serializationHandler) throws XBProcessingException, IOException;
}
