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
 * Enumeration of possible parsing modes.
 *
 * @version 0.2.0 2016/09/25
 * @author ExBin Project (http://exbin.org)
 */
public enum XBParserMode {

    /**
     * Parsing includes both header and tail data parts.
     */
    FULL,
    /**
     * Parsing only generates single block - skips both header and tail.
     */
    SINGLE_BLOCK,
    /**
     * Skips header.
     */
    SKIP_HEAD,
    /**
     * Skips tail.
     */
    SKIP_TAIL
}
