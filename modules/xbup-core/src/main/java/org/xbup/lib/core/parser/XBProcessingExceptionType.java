/*
 * Copyright (C) XBUP Project
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
package org.xbup.lib.core.parser;

/**
 * Enumeration of XBUP protocol processing exception types.
 *
 * @version 0.1.23 2014/02/06
 * @author XBUP Project (http://xbup.org)
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
    UNSUPPORTED
}
