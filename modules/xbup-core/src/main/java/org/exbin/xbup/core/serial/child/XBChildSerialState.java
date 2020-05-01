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
package org.exbin.xbup.core.serial.child;

/**
 * Enumeration of possible child serialization states.
 *
 * @version 0.1.24 2014/08/23
 * @author ExBin Project (http://exbin.org)
 */
public enum XBChildSerialState {

    /**
     * Start of serialization of the given child.
     */
    BLOCK_BEGIN,
    /**
     * Inside or at the end of attribute part.
     */
    ATTRIBUTE_PART,
    /**
     * Type processing.
     */
    TYPE,
    /**
     * After first and before last attribute.
     */
    ATTRIBUTES,
    /**
     * Inside or at the end of data part.
     */
    DATA,
    /**
     * Children data processed.
     */
    CHILDREN,
    /**
     * End of block.
     */
    BLOCK_END,
    /**
     * End of serialization of the given child.
     */
    EOF,
}
