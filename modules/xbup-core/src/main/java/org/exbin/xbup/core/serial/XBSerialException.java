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
package org.exbin.xbup.core.serial;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.parser.XBProcessingExceptionType;

/**
 * Exception for XBUP protocol serialization errors.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
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

    @Nullable
    public Object getSerializedObject() {
        return serializedObject;
    }
}
