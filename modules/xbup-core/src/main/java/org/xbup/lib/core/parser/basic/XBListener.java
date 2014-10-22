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
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.ubnumber.UBNatural;

/**
 * XBUP protocol level 0 data listener.
 *
 * Execution is sender side controlled (push).
 *
 * @version 0.1.23 2014/01/27
 * @author XBUP Project (http://xbup.org)
 */
public interface XBListener {

    /**
     * Reports block begin.
     *
     * @param terminationMode Specify whether block is terminated
     * @throws XBProcessingException
     * @throws IOException
     */
    public void beginXB(XBBlockTerminationMode terminationMode) throws XBProcessingException, IOException;

    /**
     * Reports block attribute.
     *
     * @param attribute given attribute
     * @throws XBProcessingException if unable to parse attribute value
     * @throws IOException
     */
    public void attribXB(UBNatural attribute) throws XBProcessingException, IOException;

    /**
     * Reports block data.
     *
     * You have to process data before processing next event.
     *
     * @param data processed data
     * @throws XBProcessingException
     * @throws IOException
     */
    public void dataXB(InputStream data) throws XBProcessingException, IOException;

    /**
     * Reports block end.
     *
     * @throws XBProcessingException
     * @throws IOException
     */
    public void endXB() throws XBProcessingException, IOException;
}