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
 * Exception class for XBUP protocol processing.
 *
 * @version 0.2.1 2017/05/12
 * @author ExBin Project (http://exbin.org)
 */
public class XBProcessingException extends RuntimeException {

    @Nullable
    private XBProcessingExceptionType errorType = XBProcessingExceptionType.UNKNOWN;
    @Nullable
    private long[] treePath = null;

    public XBProcessingException() {
        super();
    }

    public XBProcessingException(@Nullable String message) {
        super(message);
    }

    public XBProcessingException(@Nullable String message, @Nullable XBProcessingExceptionType errorType) {
        this(message);
        this.errorType = errorType;
    }

    public XBProcessingException(@Nullable String message, @Nullable long[] treePath) {
        this(message);
        this.treePath = treePath;
    }

    public XBProcessingException(String message, XBProcessingExceptionType errorType, long[] treePath) {
        this(message, errorType);
        this.treePath = treePath;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof XBProcessingException) {
            return (getMessage().equals(((XBProcessingException) obj).getMessage()) && (errorType == ((XBProcessingException) obj).getErrorType()));
        }

        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + this.getErrorType().ordinal();
        return hash;
    }

    public @Nullable XBProcessingExceptionType getErrorType() {
        return errorType;
    }

    public @Nullable long[] getTreePath() {
        return treePath;
    }
}
