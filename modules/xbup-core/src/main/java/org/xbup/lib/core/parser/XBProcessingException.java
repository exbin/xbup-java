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
 * Exception class for XBUP protocol processing.
 *
 * @version 0.1.23 2014/02/01
 * @author XBUP Project (http://xbup.org)
 */
public class XBProcessingException extends RuntimeException {

    private final int errorNo;
    private final int position;

    public XBProcessingException() {
        errorNo = 0;
        position = 0;
    }

    public XBProcessingException(String message) {
        super(message);
        errorNo = 0;
        position = 0;
    }

    public XBProcessingException(String message, int errorNo) {
        super(message);
        this.errorNo = errorNo;
        position = 0;
    }

    public XBProcessingException(String message, XBProcessingExceptionType type) {
        super(message);
        this.errorNo = type.ordinal();
        position = 0;
    }

    public XBProcessingException(String message, int errorNo, int position) {
        super(message);
        this.errorNo = errorNo;
        this.position = position;
    }

    public XBProcessingException(String message, XBProcessingExceptionType type, int position) {
        super(message);
        this.errorNo = type.ordinal();
        this.position = position;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof XBProcessingException) {
            return (getMessage().equals(((XBProcessingException) obj).getMessage()) && (getErrorNo() == ((XBProcessingException) obj).getErrorNo()));
        }

        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + this.getErrorNo();
        hash = 47 * hash + this.getPosition();
        return hash;
    }

    public int getErrorNo() {
        return errorNo;
    }

    public int getPosition() {
        return position;
    }
}
