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
import org.xbup.lib.core.parser.token.event.XBTEventListener;

/**
 * XBUP level 1 output stream abstract class.
 *
 * @version 0.1.23 2014/02/25
 * @author XBUP Project (http://xbup.org)
 */
public abstract class XBTOutputStream implements Closeable, XBTEventListener {

    /**
     * Flushes this output stream and forces any buffered output bytes to be
     * written out.
     *
     * @throws IOException
     */
    public abstract void flush() throws IOException;
}
