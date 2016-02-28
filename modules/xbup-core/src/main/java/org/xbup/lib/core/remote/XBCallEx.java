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
package org.xbup.lib.core.remote;

import java.io.IOException;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.stream.XBInput;
import org.xbup.lib.core.stream.XBOutput;

/**
 * XBUP RPC procedure call interface.
 *
 * @version 0.1.25 2015/02/22
 * @author ExBin Project (http://exbin.org)
 */
public interface XBCallEx {

    /**
     * Invocates procedure.
     *
     * @param parameters procedure parameters data
     * @return result type
     * @throws IOException if input/output error
     */
    public XBProcedureResultType call(XBInput parameters) throws XBProcessingException, IOException;

    /**
     * Executes if call was invoked properly.
     *
     * @param result handler for procedure result data
     * @throws IOException if input/output error
     */
    public void result(XBOutput result) throws XBProcessingException, IOException;

    /**
     * Invocates if procedure call failed with exception.
     *
     * @param data exception data
     * @throws IOException if input/output error
     */
    public void exception(XBOutput data) throws XBProcessingException, IOException;
}
