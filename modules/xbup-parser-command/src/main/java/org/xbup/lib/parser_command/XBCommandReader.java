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
package org.xbup.lib.parser_command;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import org.xbup.lib.core.block.XBBlock;

/**
 * XBUP level 0 command reader interface.
 *
 * @version 0.1.25 2015/09/08
 * @author XBUP Project (http://xbup.org)
 */
public interface XBCommandReader extends Closeable {

    /**
     * Opens input byte-stream.
     *
     * @param stream input stream
     * @throws java.io.IOException
     */
    public void open(InputStream stream) throws IOException;

    /**
     * Closes input stream.
     */
    @Override
    public void close() throws IOException;

    /**
     * Resets parser.
     *
     * @throws IOException
     */
    public void resetParser() throws IOException;

    /**
     * Returns block handler for the root block.
     *
     * @return block handler
     */
    public XBBlock getRootBlock();

    /**
     * Returns block handler for given path in document.
     *
     * @param blockPath path to block in document
     * @return block handler
     */
    public XBBlock getBlock(long[] blockPath);
}
