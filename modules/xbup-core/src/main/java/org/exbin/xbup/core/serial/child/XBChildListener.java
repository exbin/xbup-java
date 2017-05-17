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
package org.exbin.xbup.core.serial.child;

import java.io.IOException;
import java.io.InputStream;
import javax.annotation.Nonnull;
import org.exbin.xbup.core.block.XBBlockTerminationMode;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.serial.XBSerializable;
import org.exbin.xbup.core.ubnumber.UBNatural;

/**
 * XBUP level 0 child serialization listener interface.
 *
 * @version 0.2.1 2017/05/17
 * @author ExBin Project (http://exbin.org)
 */
public interface XBChildListener {

    /**
     * Puts beggining of block.
     *
     * @param terminationMode flag
     * @throws XBProcessingException if processing error
     * @throws java.io.IOException if input/output error
     */
    void begin(@Nonnull XBBlockTerminationMode terminationMode) throws XBProcessingException, IOException;

    /**
     * Puts block's attribute.
     *
     * @param attribute attribute value
     * @throws XBProcessingException if processing error
     * @throws java.io.IOException if input/output error
     */
    void addAttribute(@Nonnull UBNatural attribute) throws XBProcessingException, IOException;

    /**
     * Puts block's child.
     *
     * @param child serializable block
     * @throws XBProcessingException if processing error
     * @throws java.io.IOException if input/output error
     */
    void addChild(@Nonnull XBSerializable child) throws XBProcessingException, IOException;

    /**
     * Puts block's data.
     *
     * @param data data stream
     * @throws XBProcessingException if processing error
     * @throws java.io.IOException if input/output error
     */
    void addData(@Nonnull InputStream data) throws XBProcessingException, IOException;

    /**
     * Puts end of block.
     *
     * @throws XBProcessingException if processing error
     * @throws java.io.IOException if input/output error
     */
    void end() throws XBProcessingException, IOException;
}
