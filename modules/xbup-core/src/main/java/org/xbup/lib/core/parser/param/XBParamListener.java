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
import org.xbup.lib.core.block.param.XBParamDecl;
import org.xbup.lib.core.parser.XBProcessingException;

/**
 * XBUP protocol level 1 parameter listener.
 * 
 * Use this listener along-side XBTListener to process sequence of parameters
 * reported during attributes processing (non-recursive).
 *
 * @version 0.1.23 2013/11/25
 * @author XBUP Project (http://xbup.org)
 */
public interface XBParamListener {

    /**
     * Reports parameter begin.
     *
     * @param paramType Type of parameter
     * @throws XBProcessingException
     * @throws IOException
     */
    public void beginXBParam(XBParamDecl paramType) throws XBProcessingException, IOException;

    /**
     * Reports single block.
     *
     * @throws XBProcessingException
     * @throws IOException
     */
    public void blockXBParam() throws XBProcessingException, IOException;

    /**
     * Reports potentially infinite / terminated block sequence.
     *
     * @throws XBProcessingException
     * @throws IOException
     */
    public void listXBParam() throws XBProcessingException, IOException;

    /**
     * Reports parameter end.
     *
     * @throws XBProcessingException
     * @throws IOException
     */
    public void endXBParam() throws XBProcessingException, IOException;
}
