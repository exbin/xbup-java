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
package org.exbin.xbup.core.parser.token.pull;

import java.io.IOException;
import javax.annotation.Nonnull;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.parser.token.XBTToken;
import org.exbin.xbup.core.stream.XBOutput;

/**
 * XBUB level 1 pull provider interface.
 *
 * @version 0.2.1 2017/05/15
 * @author ExBin Project (http://exbin.org)
 */
public interface XBTPullProvider extends XBOutput {

    /**
     * Pulls next token.
     *
     * @return next token
     * @throws XBProcessingException if processing error
     * @throws java.io.IOException if input/output error
     */
    @Nonnull
    XBTToken pullXBTToken() throws XBProcessingException, IOException;
}
