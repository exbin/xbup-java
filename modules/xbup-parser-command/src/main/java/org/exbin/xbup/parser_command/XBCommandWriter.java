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
import java.io.OutputStream;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.core.block.XBEditableDocument;

/**
 * XBUP level 0 command writer interface.
 *
 * @version 0.2.1 2017/05/24
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
public interface XBCommandWriter extends XBEditableDocument, XBCommandReader, Closeable {

    /**
     * Saves data to byte-stream.
     *
     * @param stream output stream
     * @throws java.io.IOException exception on input/output error
     */
    void save(OutputStream stream) throws IOException;

    /**
     * Writes all the changes to the source file.
     */
    void flush();
}
