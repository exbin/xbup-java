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
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.core.block.XBTBlock;
import org.exbin.xbup.core.block.XBTDocument;

/**
 * XBUP level 1 command reader interface.
 *
 * @version 0.2.1 2017/05/24
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
public interface XBTCommandReader extends XBTDocument, Closeable {

    /**
     * Opens input byte-stream.
     *
     * @param stream input stream
     * @throws java.io.IOException exception on input/output error
     */
    void open(InputStream stream) throws IOException;

    /**
     * Closes input stream.
     *
     * @throws IOException exception on input/output error
     */
    @Override
    void close() throws IOException;

    /**
     * Resets parser.
     *
     * @throws IOException exception on input/output error
     */
    void resetParser() throws IOException;

    /**
     * Returns block handler for given path in document.
     *
     * @param blockPath path to block in document
     * @return block handler
     */
    @Nonnull
    Optional<XBTBlock> getBlock(long[] blockPath);
}
