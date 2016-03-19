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

/**
 * XBUP protocol level 1 empty data token.
 *
 * @version 0.1.24 2015/01/05
 * @author ExBin Project (http://exbin.org)
 */
public class XBTEmptyDataToken extends XBTDataToken {

    private static XBTEmptyDataToken cachedEmptyToken = null;

    public XBTEmptyDataToken() {
        super(new ByteArrayInputStream(new byte[0]));
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    public static XBTEmptyDataToken getEmptyToken() {
        if (cachedEmptyToken == null) {
            cachedEmptyToken = new XBTEmptyDataToken();
        }

        return cachedEmptyToken;
    }
}
