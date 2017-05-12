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
package org.exbin.xbup.core.parser.basic;

import java.io.IOException;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.exbin.xbup.core.block.XBBlockTerminationMode;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.ubnumber.UBNatural;

/**
 * XBUP protocol level 1 size precomputed data listener.
 *
 * Execution is sender side controlled (push).
 *
 * @version 0.2.1 2017/05/12
 * @author ExBin Project (http://exbin.org)
 */
public interface XBTSListener extends XBTListener {

    /**
     * Reports block begin with precomputed size.
     *
     * @param terminationMode Specify whether block is terminated
     * @param blockSize Precomputed size of block or null if no precomputed size
     * is available
     * @throws XBProcessingException if processing error
     * @throws java.io.IOException if input/output error
     */
    void beginXBT(@Nonnull XBBlockTerminationMode terminationMode, @Nullable UBNatural blockSize) throws XBProcessingException, IOException;
}
