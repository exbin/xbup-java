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
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.block.XBBlockTerminationMode;
import org.xbup.lib.core.ubnumber.UBNatural;

/**
 * XBUP protocol level 1 size precomputed data listener.
 * Execution is sender side controlled (push).
 *
 * @version 0.1.23 2014/02/01
 * @author XBUP Project (http://xbup.org)
 */
public interface XBTSListener extends XBTListener {

    /**
     * Reports block begin with precomputed size.
     *
     * @param terminationMode Specify whether block is terminated
     * @param blockSize Precomputed size of block or null if no precomputed size
     * is available
     * @throws XBProcessingException
     * @throws java.io.IOException
     */
    public void beginXBT(XBBlockTerminationMode terminationMode, UBNatural blockSize) throws XBProcessingException, IOException;
}
