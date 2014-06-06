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
package org.xbup.lib.xb.parser;

/**
 * Exception for XBUP protocol parsing.
 *
 * @version 0.1 wr23.0 2013/11/30
 * @author XBUP Project (http://xbup.org)
 */
public class XBParseException extends XBProcessingException {

    public XBParseException() {
        super();
    }

    public XBParseException(String message) {
        super(message);
    }

    public XBParseException(String message, int errorNo) {
        super(message, errorNo);
    }

    public XBParseException(String message, XBProcessingExceptionType type) {
        super(message, type);
    }

    public XBParseException(String message, int errorNo, int position) {
        super(message, errorNo, position);
    }

    public XBParseException(String message, XBProcessingExceptionType type, int position) {
        super(message, type, position);
    }
}
