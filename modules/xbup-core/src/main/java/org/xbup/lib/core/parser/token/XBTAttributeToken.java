/*
 * Copyright (C) XBUP Project
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

import org.xbup.lib.core.ubnumber.UBNatural;

/**
 * XBUP protocol level 1 attribute token.
 *
 * This class carry single UBNatural value.
 * 
 * @version 0.1 wr23.0 2013/11/01
 * @author XBUP Project (http://xbup.org)
 */
public class XBTAttributeToken extends XBTToken {

    private final UBNatural attribute;

    public XBTAttributeToken(UBNatural attribute) {
        this.attribute = attribute;
    }

    public UBNatural getAttribute() {
        return attribute;
    }

    @Override
    public XBTTokenType getTokenType() {
        return XBTTokenType.ATTRIBUTE;
    }
}
