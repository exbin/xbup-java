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
package org.xbup.lib.core.parser.param;

import java.io.IOException;
import org.xbup.lib.core.parser.XBProcessingException;

/**
 * XBUP protocol level 1 parameter provider.
 *
 * @version 0.1 wr23.0 2013/11/25
 * @author XBUP Project (http://xbup.org)
 */
public interface XBParamProvider {

    /**
     * Produce single param data.
     * 
     * @param listener listener to send data to
     * @throws org.xbup.lib.xb.parser.XBProcessingException
     * @throws java.io.IOException
     */
    public void produceParamXB(XBParamListener listener) throws XBProcessingException, IOException;

    /**
     * Indicate end of data.
     * 
     * @return true if end of data stream reached
     */
    public boolean eofParamXB();
}
