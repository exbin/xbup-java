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
package org.exbin.xbup.core.parser;

/**
 * Enumeration of possible parsing states.
 *
 * @version 0.1.24 2014/09/30
 * @author ExBin Project (http://exbin.org)
 */
public enum XBParserState {

    /**
     * Start of file (before Head).
     */
    START,
    /**
     * Start of block or block expected.
     */
    BLOCK_BEGIN,
    /**
     * Inside or at the end of block type.
     */
    BLOCK_TYPE,
    /**
     * Inside or at the end of attribute part.
     */
    ATTRIBUTE_PART,
    /**
     * Inside or at the end of data part.
     */
    DATA_PART,
    /**
     * Inside or at the end of children part.
     */
    CHILDREN_PART,
    /**
     * Inside tail data.
     */
    TAIL_DATA,
    /**
     * End of block or end of multiple blocks.
     */
    BLOCK_END,
    /**
     * End of parsing.
     */
    EOF
}
