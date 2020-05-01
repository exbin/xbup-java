/*
 * Copyright (C) ExBin Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
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
 * XBUP RPC procedure call interface.
 *
 * @version 0.2.1 2017/05/29
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
public interface XBCallEx {

    /**
     * Invocates procedure.
     *
     * @param parameters procedure parameters data
     * @return result type
     * @throws IOException if input/output error
     */
    @Nonnull
    XBProcedureResultType call(XBInput parameters) throws XBProcessingException, IOException;

    /**
     * Executes if call was invoked properly.
     *
     * @param result handler for procedure result data
     * @throws IOException if input/output error
     */
    void result(XBOutput result) throws XBProcessingException, IOException;

    /**
     * Invocates if procedure call failed with exception.
     *
     * @param data exception data
     * @throws IOException if input/output error
     */
    void exception(XBOutput data) throws XBProcessingException, IOException;
}
