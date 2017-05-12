/*
 * Copyright (C) ExBin Project
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
package org.exbin.xbup.core.parser;

import javax.annotation.Nullable;

/**
 * Exception class for XBUP protocol parsing.
 *
 * @version 0.2.1 2017/05/12
 * @author ExBin Project (http://exbin.org)
 */
public class XBParseException extends XBProcessingException {

    private long position = -1;

    public XBParseException() {
        super();
    }

    public XBParseException(@Nullable String message) {
        super(message);
    }

    public XBParseException(@Nullable String message, @Nullable XBProcessingExceptionType errorType) {
        super(message, errorType);
    }

    public XBParseException(@Nullable String message, @Nullable long[] treePath) {
        super(message, treePath);
    }

    public XBParseException(@Nullable String message, @Nullable XBProcessingExceptionType errorType, @Nullable long[] treePath) {
        super(message, errorType, treePath);
    }

    public XBParseException(@Nullable String message, long position) {
        super(message);
        this.position = position;
    }

    public XBParseException(@Nullable String message, @Nullable XBProcessingExceptionType errorType, long position) {
        super(message, errorType);
        this.position = position;
    }

    public XBParseException(@Nullable String message, @Nullable long[] treePath, long position) {
        super(message, treePath);
        this.position = position;
    }

    public XBParseException(@Nullable String message, @Nullable XBProcessingExceptionType errorType, @Nullable long[] treePath, long position) {
        super(message, errorType, treePath);
        this.position = position;
    }

    public long getPosition() {
        return position;
    }
}
