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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * XBUP protocol level 0 end token.
 *
 * @version 0.2.1 2017/05/22
 * @author ExBin Project (http://exbin.org)
 */
public abstract class XBEndToken implements XBToken {

    private XBEndToken() {
    }

    @Override
    @Nonnull
    public XBTokenType getTokenType() {
        return XBTokenType.END;
    }

    @Nullable
    private static XBEndToken instance = null;

    @Nonnull
    public static XBEndToken create() {
        if (instance == null) {
            instance = new XBEndTokenImpl();
        }

        return instance;
    }

    private static class XBEndTokenImpl extends XBEndToken {

        public XBEndTokenImpl() {
        }
    }
}
