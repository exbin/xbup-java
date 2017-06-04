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
 * XBUP RPC procedure interface with support for exceptions.
 *
 * @version 0.2.1 2017/05/29
 * @author ExBin Project (http://exbin.org)
 */
public interface XBProcedureEx extends XBExecutable {

    /**
     * Invocates procedure.
     *
     * @param parameters procedure parameters data
     * @param result handler for procedure result data
     * @param exception handler for exception data
     * @return procedure result type
     * @throws IOException if input/output error
     */
    @Nonnull
    XBProcedureResultType execute(@Nonnull XBOutput parameters, @Nonnull XBInput result, @Nonnull XBInput exception) throws XBProcessingException, IOException;
}
