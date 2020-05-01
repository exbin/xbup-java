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
package org.exbin.xbup.core.stream;

import java.io.IOException;
import org.exbin.xbup.core.parser.XBProcessingException;

/**
 * Interface for stream with skipable tokens.
 *
 * @version 0.2.1 2017/05/17
 * @author ExBin Project (http://exbin.org)
 */
public interface XBSkipableStream {

    /**
     * Skips given count of tokens in stream.
     *
     * @param tokenCount count of tokens
     * @throws java.io.IOException if input/output error
     */
    void skipXB(long tokenCount) throws XBProcessingException, IOException;

    /**
     * Skips child blocks.
     *
     * @param childBlocksCount count of child blocks to skip
     * @throws java.io.IOException if input/output error
     */
    void skipChildXB(long childBlocksCount) throws XBProcessingException, IOException;

    /**
     * Skips child blocks.
     *
     * @param childBlocksCount count of child blocks to skip
     * @throws java.io.IOException if input/output error
     */
    void skipAttributesXB(long childBlocksCount) throws XBProcessingException, IOException;
}
