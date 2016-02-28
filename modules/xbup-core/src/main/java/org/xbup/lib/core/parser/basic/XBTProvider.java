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
package org.xbup.lib.core.parser.basic;

import java.io.IOException;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.stream.XBOutput;

/**
 * XBUP protocol level 1 data provider.
 *
 * Execution is receiver side controlled (pull).
 *
 * @version 0.1.25 2015/02/13
 * @author ExBin Project (http://exbin.org)
 */
public interface XBTProvider extends XBOutput  {

    /**
     * Produces single data.
     *
     * @param listener listener to send data to
     * @throws XBProcessingException
     * @throws IOException
     */
    public void produceXBT(XBTListener listener) throws XBProcessingException, IOException;
}
