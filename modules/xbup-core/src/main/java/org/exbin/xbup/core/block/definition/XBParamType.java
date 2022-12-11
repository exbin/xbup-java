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
package org.exbin.xbup.core.block.definition;

/**
 * XBUP level 1 parameter type enumeration.
 *
 * @author ExBin Project (https://exbin.org)
 */
public enum XBParamType {

    /**
     * Consist parameter.
     *
     * Appends given block as subblock or any block if unspecified.
     */
    CONSIST,
    /**
     * Join parameter.
     *
     * Appends attributes and subblocks of given block or single attribute if
     * unspecified.
     */
    JOIN,
    /**
     * List consist parameter.
     *
     * Appends list of subblocks of given block type or any block. List size is
     * included first as UBENatural attribute (including infinity).
     */
    LIST_CONSIST,
    /**
     * List join parameter.
     *
     * Appends list of attributes and subblocks of given block type or plain
     * attributes. List size is included first as UBNatural attribute.
     */
    LIST_JOIN;

    public boolean isList() {
        return this == LIST_CONSIST || this == LIST_JOIN;
    }

    public boolean isConsist() {
        return this == CONSIST || this == LIST_CONSIST;
    }

    public boolean isJoin() {
        return this == JOIN || this == LIST_JOIN;
    }
}
