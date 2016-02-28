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
package org.xbup.lib.core.parser.token;

import org.xbup.lib.core.ubnumber.type.UBNat32;

/**
 * XBUP protocol level 1 zero attribute token.
 *
 * This class carry single UBNatural with zero value.
 *
 * @version 0.1.24 2015/01/05
 * @author ExBin Project (http://exbin.org)
 */
public class XBTZeroAttributeToken extends XBTAttributeToken {

    private static XBTZeroAttributeToken cachedZeroToken = null;

    public XBTZeroAttributeToken() {
        super(new UBNat32());
    }

    @Override
    public boolean isZero() {
        return super.isZero();
    }

    public static XBTZeroAttributeToken getZeroToken() {
        if (cachedZeroToken == null) {
            cachedZeroToken = new XBTZeroAttributeToken();
        }

        return cachedZeroToken;
    }
}
