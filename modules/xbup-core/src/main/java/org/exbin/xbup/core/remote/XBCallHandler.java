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
package org.exbin.xbup.core.remote;

import java.io.IOException;
import javax.annotation.Nonnull;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.stream.XBInput;
import org.exbin.xbup.core.stream.XBOutput;

/**
 * XBUP RPC procedure call handler interface.
 *
 * @version 0.2.1 2017/05/29
 * @author ExBin Project (http://exbin.org)
 */
public interface XBCallHandler {

    /**
     * Returns XBUP input stream handler for parameters.
     *
     * @return input
     * @throws IOException if input/output error
     */
    @Nonnull
    XBInput getParametersInput() throws XBProcessingException, IOException;

    /**
     * Returns XBUP output stream handler for result.
     *
     * @return output
     * @throws IOException if input/output error
     */
    @Nonnull
    XBOutput getResultOutput() throws XBProcessingException, IOException;

    /**
     * Performs execution of the handler.
     *
     * @throws IOException if input/output error
     */
    void execute() throws XBProcessingException, IOException;
}
