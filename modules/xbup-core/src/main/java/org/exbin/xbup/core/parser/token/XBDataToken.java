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
package org.exbin.xbup.core.parser.token;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * XBUP protocol level 0 data token.
 *
 * Class carry data represented as byte stream available via InputStream class.
 * You have to process data before processing next event.
 *
 * @version 0.2.1 2017/05/22
 * @author ExBin Project (http://exbin.org)
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

    @Override
    @Nonnull
    public XBTokenType getTokenType() {
        return XBTokenType.DATA;
    }

    @Nonnull
    public static XBDataToken create(@Nonnull InputStream data) {
        return new XBDataTokenImpl(data);
    }

    private static class XBDataTokenImpl extends XBDataToken {

        @Nonnull
        private final InputStream data;

        public XBDataTokenImpl(InputStream data) {
            this.data = data;
        }

        @Override
        public XBTokenType getTokenType() {
            return super.getTokenType();
        }

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

        @Override
        public InputStream getData() {
            return new ByteArrayInputStream(new byte[0]);
        }
    }
}
