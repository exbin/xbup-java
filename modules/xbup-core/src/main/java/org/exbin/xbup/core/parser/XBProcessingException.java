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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Exception class for XBUP protocol processing.
 *
 * @version 0.2.1 2017/05/15
 * @author ExBin Project (http://exbin.org)
 */
public class XBProcessingException extends RuntimeException {

    @Nonnull
    private XBProcessingExceptionType errorType = XBProcessingExceptionType.UNKNOWN;
    @Nullable
    private long[] treePath = null;

    public XBProcessingException() {
        super();
    }

    public XBProcessingException(@Nullable String message) {
        super(message);
    }

    public XBProcessingException(@Nullable String message, @Nonnull XBProcessingExceptionType errorType) {
        this(message);
        this.errorType = errorType;
    }

    public XBProcessingException(@Nullable String message, @Nullable long[] treePath) {
        this(message);
        this.treePath = treePath;
    }

    public XBProcessingException(@Nullable String message, @Nonnull XBProcessingExceptionType errorType, @Nullable long[] treePath) {
        this(message, errorType);
        this.treePath = treePath;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == null) {
            return false;
        }

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

    @Nonnull
    public XBProcessingExceptionType getErrorType() {
        return errorType;
    }

    @Nullable
    public long[] getTreePath() {
        return treePath;
    }
}
