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
package org.xbup.lib.core.parser;

/**
 * Exception class for XBUP protocol parsing.
 *
 * @version 0.1.24 2014/11/27
 * @author ExBin Project (http://exbin.org)
 */
public class XBParseException extends XBProcessingException {

    private long position = -1;

    public XBParseException() {
        super();
    }

    public XBParseException(String message) {
        super(message);
    }

    public XBParseException(String message, XBProcessingExceptionType errorType) {
        super(message, errorType);
    }

    public XBParseException(String message, long[] treePath) {
        super(message, treePath);
    }

    public XBParseException(String message, XBProcessingExceptionType errorType, long[] treePath) {
        super(message, errorType, treePath);
    }

    public XBParseException(String message, long position) {
        super(message);
        this.position = position;
    }

    public XBParseException(String message, XBProcessingExceptionType errorType, long position) {
        super(message, errorType);
        this.position = position;
    }

    public XBParseException(String message, long[] treePath, long position) {
        super(message, treePath);
        this.position = position;
    }

    public XBParseException(String message, XBProcessingExceptionType errorType, long[] treePath, long position) {
        super(message, errorType, treePath);
        this.position = position;
    }

    public long getPosition() {
        return position;
    }
}
