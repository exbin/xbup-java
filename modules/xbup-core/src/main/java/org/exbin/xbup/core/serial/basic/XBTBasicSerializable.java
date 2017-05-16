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
package org.exbin.xbup.core.serial.basic;

import java.io.IOException;
import javax.annotation.Nonnull;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.serial.XBSerializable;

/**
 * Interface is providing serialization methods for serialization from and into
 * XBUP level 1 protocol using basic parser.
 *
 * @version 0.2.1 2017/05/16
 * @author ExBin Project (http://exbin.org)
 */
public interface XBTBasicSerializable extends XBSerializable {

    /**
     * Performs serialization from XBUP protocol.
     *
     * @param serializationHandler serialization resource
     * @throws XBProcessingException if proccesing problem encountered
     * @throws IOException if input/output problem encountered
     */
    void serializeFromXB(@Nonnull XBTBasicInputSerialHandler serializationHandler) throws XBProcessingException, IOException;

    /**
     * Performs serialization to XBUP protocol.
     *
     * @param serializationHandler serialization resource
     * @throws XBProcessingException if proccesing problem encountered
     * @throws IOException if input/output problem encountered
     */
    void serializeToXB(@Nonnull XBTBasicOutputSerialHandler serializationHandler) throws XBProcessingException, IOException;
}
