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
package org.xbup.lib.core.serial;

import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.XBProcessingExceptionType;

/**
 * Exception for XBUP protocol serialization errors.
 *
 * @version 0.1.24 2014/11/27
 * @author XBUP Project (http://xbup.org)
 */
public class XBSerialException extends XBProcessingException {

    private Object serializedObject = null;

    public XBSerialException() {
        super();
    }

    public XBSerialException(String message) {
        super(message);
    }

    public XBSerialException(String message, XBProcessingExceptionType errorType) {
        super(message, errorType);
    }

    public XBSerialException(String message, long[] treePath) {
        super(message, treePath);
    }

    public XBSerialException(String message, XBProcessingExceptionType errorType, long[] treePath) {
        super(message, errorType, treePath);
    }

    public XBSerialException(Object serializedObject) {
        super();
    }

    public XBSerialException(String message, Object serializedObject) {
        super(message);
        this.serializedObject = serializedObject;
    }

    public XBSerialException(String message, XBProcessingExceptionType errorType, Object serializedObject) {
        super(message, errorType);
        this.serializedObject = serializedObject;
    }

    public XBSerialException(String message, long[] treePath, Object serializedObject) {
        super(message, treePath);
        this.serializedObject = serializedObject;
    }

    public XBSerialException(String message, XBProcessingExceptionType errorType, long[] treePath, Object serializedObject) {
        super(message, errorType, treePath);
        this.serializedObject = serializedObject;
    }

    public Object getSerializedObject() {
        return serializedObject;
    }
}
