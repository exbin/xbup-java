/*
 * Copyright (C) ExBin Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.exbin.xbup.core.remote;

import java.io.IOException;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.stream.XBInput;
import org.exbin.xbup.core.stream.XBOutput;

/**
 * XBUP RPC procedure interface with support for exceptions.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
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
    XBProcedureResultType execute(XBOutput parameters, XBInput result, XBInput exception) throws XBProcessingException, IOException;
}
