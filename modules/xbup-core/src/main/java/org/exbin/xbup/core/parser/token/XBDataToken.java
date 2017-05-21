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

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Nonnull;

/**
 * XBUP protocol level 0 data token.
 *
 * Class carry data represented as byte stream available via InputStream class.
 * You have to process data before processing next event.
 *
 * @version 0.2.1 2017/05/21
 * @author ExBin Project (http://exbin.org)
 */
public class XBDataToken extends XBToken {

    @Nonnull
    private final InputStream data;

    private XBDataToken(@Nonnull InputStream data) {
        this.data = data;
    }

    @Nonnull
    public InputStream getData() {
        return data;
    }

    /**
     * Returns true if this is empty data token.
     *
     * This method is supposed to be called only before data was processed.
     *
     * @return true if data are empty
     */
    public boolean isEmpty() {
        try {
            return data.available() == 0;
        } catch (IOException ex) {
            Logger.getLogger(XBDataToken.class.getName()).log(Level.SEVERE, null, ex);
        }

        return false;
    }

    @Override
    @Nonnull
    public XBTokenType getTokenType() {
        return XBTokenType.DATA;
    }

    @Nonnull
    public static XBDataToken create(@Nonnull InputStream data) {
        return new XBDataToken(data);
    }
}
