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
 * XBUP protocol level 1 data token.
 *
 * Class carry data represented as byte stream available via InputStream class.
 * You have to process data before processing next event.
 *
 * @version 0.2.1 2017/05/22
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
public abstract class XBTDataToken implements XBTToken {

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
    public XBTTokenType getTokenType() {
        return XBTTokenType.DATA;
    }

    @Nonnull
    public static XBTDataToken create(InputStream data) {
        return new XBTDataTokenImpl(data);
    }

    @ParametersAreNonnullByDefault
    private static class XBTDataTokenImpl extends XBTDataToken {

        @Nonnull
        private final InputStream data;

        private XBTDataTokenImpl(InputStream data) {
            this.data = data;
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
    private static XBTEmptyDataToken instance = null;

    @Nonnull
    public static XBTDataToken createEmptyToken() {
        if (instance == null) {
            instance = new XBTEmptyDataToken();
        }

        return instance;
    }

    @ParametersAreNonnullByDefault
    private static class XBTEmptyDataToken extends XBTDataToken {

        private XBTEmptyDataToken() {
        }

        @Nonnull
        @Override
        public InputStream getData() {
            return new ByteArrayInputStream(new byte[0]);
        }

        @Override
        public boolean isEmpty() {
            return true;
        }
    }
}
