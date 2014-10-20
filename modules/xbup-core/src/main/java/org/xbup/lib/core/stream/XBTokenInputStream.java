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
package org.xbup.lib.core.stream;

import java.io.Closeable;
import java.io.IOException;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.token.pull.XBPullProvider;

/**
 * XBUP level 0 token input stream abstract class.
 *
 * @version 0.1.23 2014/02/25
 * @author XBUP Project (http://xbup.org)
 */
public abstract class XBTokenInputStream implements Closeable, XBPullProvider {

    /**
     * Reset source - jump to beginning of stream.
     *
     * @throws IOException if not possible to reset
     */
    public abstract void reset() throws IOException;

    /**
     * Returns true if stream ended.
     *
     * @return true if stream ended
     * @throws IOException if unable to determine stream end
     */
    public abstract boolean finished() throws IOException;

    /**
     * Skip given count of tokens in stream.
     *
     * @param tokenCount count of tokens
     * @throws IOException
     */
    public abstract void skip(long tokenCount) throws XBProcessingException, IOException;
}
