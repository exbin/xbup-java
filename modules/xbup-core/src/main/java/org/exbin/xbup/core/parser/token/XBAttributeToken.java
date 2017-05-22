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
import org.exbin.xbup.core.ubnumber.type.UBNat32;

/**
 * XBUP protocol level 0 attribute token.
 *
 * This class carry single UBNatural value.
 *
 * @version 0.2.1 2017/05/22
 * @author ExBin Project (http://exbin.org)
 */
public abstract class XBAttributeToken implements XBToken {

    /**
     * Return attribute value.
     *
     * @return attribute value
     */
    @Nonnull
    public abstract XBAttribute getAttribute();

    /**
     * Returns true if this is attribute is zero.
     *
     * @return true if attribute is zero
     */
    public abstract boolean isZero();

    @Override
    @Nonnull
    public XBTokenType getTokenType() {
        return XBTokenType.ATTRIBUTE;
    }

    @Nonnull
    public static XBAttributeToken create(@Nonnull XBAttribute attribute) {
        return new XBAttributeTokenImpl(attribute);
    }

    private static class XBAttributeTokenImpl extends XBAttributeToken {

        @Nonnull
        private final XBAttribute attribute;

        private XBAttributeTokenImpl(@Nonnull XBAttribute attribute) {
            this.attribute = attribute;
        }

        @Nonnull
        @Override
        public XBAttribute getAttribute() {
            return attribute;
        }

        /**
         * Returns true if this is attribute is zero.
         *
         * @return true if attribute is zero
         */
        @Override
        public boolean isZero() {
            return attribute.isNaturalZero();
        }
    }

    @Nullable
    private static XBZeroAttributeToken cachedZeroToken = null;

    @Nonnull
    public static XBZeroAttributeToken createZeroToken() {
        if (cachedZeroToken == null) {
            cachedZeroToken = new XBZeroAttributeToken();
        }

        return cachedZeroToken;
    }

    private static class XBZeroAttributeToken extends XBAttributeToken {

        XBZeroAttributeToken() {
        }

        @Override
        public XBAttribute getAttribute() {
            return new UBNat32();
        }

        @Override
        public boolean isZero() {
            return true;
        }
    }
}
