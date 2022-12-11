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
package org.exbin.xbup.core.parser.token;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * XBUP protocol level 0 data token.
 *
 * Class carry data represented as byte stream available via InputStream class.
 * You have to process data before processing next event.
 *
 * @author ExBin Project (https://exbin.org)
 */
public abstract class XBDataToken implements XBToken {

    /**
     * Returns token data.
     *
     * @return token data
     */
    @Nonnull
    public abstract InputStream getData();

    /**
     * Returns true if this is empty data token.
     *
     * This method is supposed to be called only before data was processed.
     *
     * @return true if data are empty
     */
    public abstract boolean isEmpty();

    @Nonnull
    @Override
    public XBTokenType getTokenType() {
        return XBTokenType.DATA;
    }

    @Nonnull
    public static XBDataToken create(@Nonnull InputStream data) {
        return new XBDataTokenImpl(data);
    }

    @ParametersAreNonnullByDefault
    private static class XBDataTokenImpl extends XBDataToken {

        @Nonnull
        private final InputStream data;

        public XBDataTokenImpl(InputStream data) {
            this.data = data;
        }

        @Nonnull
        @Override
        public XBTokenType getTokenType() {
            return super.getTokenType();
        }

        @Nonnull
        @Override
        public InputStream getData() {
            return data;
        }

        @Override
        public boolean isEmpty() {
            try {
                return data.available() == 0;
            } catch (IOException ex) {
                Logger.getLogger(XBDataToken.class.getName()).log(Level.SEVERE, null, ex);
            }

            return false;
        }
    }

    @Nullable
    private static XBEmptyDataToken instance = null;

    @Nonnull
    public static XBDataToken createEmptyToken() {
        if (instance == null) {
            instance = new XBEmptyDataToken();
        }

        return instance;
    }

    private static class XBEmptyDataToken extends XBDataToken {

        XBEmptyDataToken() {
        }

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Nonnull
        @Override
        public InputStream getData() {
            return new ByteArrayInputStream(new byte[0]);
        }
    }
}
