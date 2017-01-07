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
package org.exbin.xbup.parser_command;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import org.exbin.xbup.core.block.XBBlock;
import org.exbin.xbup.core.block.XBDocument;

/**
 * XBUP level 0 command reader interface.
 *
 * @version 0.2.0 2015/09/22
 * @author ExBin Project (http://exbin.org)
 */
public interface XBCommandReader extends XBDocument, Closeable {

    /**
     * Opens input byte-stream.
     *
     * @param stream input stream
     * @throws java.io.IOException exception on input/output error
     */
    public void open(InputStream stream) throws IOException;

    /**
     * Closes input stream.
     *
     * @throws java.io.IOException exception on input/output error
     */
    @Override
    public void close() throws IOException;

    /**
     * Resets parser.
     *
     * @throws IOException exception on input/output error
     */
    public void resetXB() throws IOException;

    /**
     * Returns block handler for given path in document.
     *
     * @param blockPath path to block in document
     * @return block handler
     */
    public XBBlock getBlock(long[] blockPath);
}
