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
package org.exbin.xbup.core.parser;

/**
 * Enumeration of XBUP protocol processing exception types.
 *
 * @author ExBin Project (https://exbin.org)
 */
public enum XBProcessingExceptionType {

    /**
     * Unknown problem.
     */
    UNKNOWN,
    /**
     * Corrupted or missing header.
     */
    CORRUPTED_HEADER,
    /**
     * There is terminator when it is not expected.
     */
    UNEXPECTED_TERMINATOR,
    /**
     * Attribute ends beyond end of attribute part.
     */
    ATTRIBUTE_OVERFLOW,
    /**
     * Blocks ends beyond end of its parent block.
     */
    BLOCK_OVERFLOW,
    /**
     * End of stream was reached during processing.
     */
    UNEXPECTED_END_OF_STREAM,
    /**
     * Attempt to read when whole document was already processed.
     */
    READING_AFTER_END,
    /**
     * Attempt to write when whole document was already processed.
     */
    WRITING_AFTER_END,
    /**
     * Item was received in unexpected order.
     */
    UNEXPECTED_ORDER,
    /**
     * Item was received a block of type which is not supported.
     */
    BLOCK_TYPE_MISMATCH,
    /**
     * Currently unsupported feature.
     */
    UNSUPPORTED,
    /**
     * Attempt to perform disallowed operation.
     */
    ILLEGAL_OPERATION
}
