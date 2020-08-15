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
package org.exbin.xbup.core.parser;

import javax.annotation.Nullable;

/**
 * Exception class for XBUP protocol parsing.
 *
 * @version 0.2.1 2020/08/11
 * @author ExBin Project (http://exbin.org)
 */
public class XBParsingException extends XBProcessingException {

    private long position = -1;

    public XBParsingException() {
        super();
    }

    public XBParsingException(@Nullable String message) {
        super(message);
    }

    public XBParsingException(@Nullable String message, @Nullable XBProcessingExceptionType errorType) {
        super(message, errorType);
    }

    public XBParsingException(@Nullable String message, @Nullable long[] treePath) {
        super(message, treePath);
    }

    public XBParsingException(@Nullable String message, @Nullable XBProcessingExceptionType errorType, @Nullable long[] treePath) {
        super(message, errorType, treePath);
    }

    public XBParsingException(@Nullable String message, long position) {
        super(message);
        this.position = position;
    }

    public XBParsingException(@Nullable String message, @Nullable XBProcessingExceptionType errorType, long position) {
        super(message, errorType);
        this.position = position;
    }

    public XBParsingException(@Nullable String message, @Nullable long[] treePath, long position) {
        super(message, treePath);
        this.position = position;
    }

    public XBParsingException(@Nullable String message, @Nullable XBProcessingExceptionType errorType, @Nullable long[] treePath, long position) {
        super(message, errorType, treePath);
        this.position = position;
    }

    public long getPosition() {
        return position;
    }
}
