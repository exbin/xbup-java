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
package org.xbup.lib.core.parser.basic;

import java.io.IOException;
import java.io.InputStream;
import org.xbup.lib.core.block.XBBlockType;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.parser.token.XBAttribute;
import org.xbup.lib.core.stream.XBInput;

/**
 * XBUP protocol level 1 data listener.
 *
 * Execution is sender side controlled (push).
 *
 * @version 0.1.25 2015/02/09
 * @author XBUP Project (http://xbup.org)
 */
public interface XBTListener extends XBInput {

    /**
     * Reports block begin.
     *
     * @param terminationMode Specify whether block is terminated
     * @throws XBProcessingException if processing error
     * @throws IOException if input/output error
     */
    public void beginXBT(XBBlockTerminationMode terminationMode) throws XBProcessingException, IOException;

    /**
     * Reports type of block.
     *
     * @param blockType Returns type of block
     * @throws XBProcessingException if processing error
     * @throws IOException if input/output error
     */
    public void typeXBT(XBBlockType blockType) throws XBProcessingException, IOException;

    /**
     * Reports block attribute.
     *
     * @param attribute given attribute
     * @throws XBProcessingException if processing error if unable to parse
     * attribute value
     * @throws IOException if input/output error
     */
    public void attribXBT(XBAttribute attribute) throws XBProcessingException, IOException;

    /**
     * Reports block data.
     *
     * You have to process data before processing next event.
     *
     * @param data processed data
     * @throws XBProcessingException if processing error
     * @throws IOException if input/output error
     */
    public void dataXBT(InputStream data) throws XBProcessingException, IOException;

    /**
     * Reports block end.
     *
     * @throws XBProcessingException if processing error
     * @throws IOException if input/output error
     */
    public void endXBT() throws XBProcessingException, IOException;
}
