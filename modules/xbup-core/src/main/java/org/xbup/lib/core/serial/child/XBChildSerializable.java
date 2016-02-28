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
package org.xbup.lib.core.serial.child;

import java.io.IOException;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.serial.XBSerializable;

/**
 * Interface is providing serialization methods for serialization into XBUP
 * level 0 protocol using basic child serialization.
 *
 * @version 0.1.24 2014/08/23
 * @author ExBin Project (http://exbin.org)
 */
public interface XBChildSerializable extends XBSerializable {

    /**
     * Performs serialization from XBUP protocol.
     *
     * @param serializationHandler serialization resource
     * @throws XBProcessingException if proccesing problem encountered
     * @throws IOException if input/output problem encountered
     */
    public void serializeFromXB(XBChildInputSerialHandler serializationHandler) throws XBProcessingException, IOException;

    /**
     * Performs serialization to XBUP protocol.
     *
     * @param serializationHandler serialization resource
     * @throws XBProcessingException if proccesing problem encountered
     * @throws IOException if input/output problem encountered
     */
    public void serializeToXB(XBChildOutputSerialHandler serializationHandler) throws XBProcessingException, IOException;
}
