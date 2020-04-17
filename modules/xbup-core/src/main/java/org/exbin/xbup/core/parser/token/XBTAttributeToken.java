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
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.core.ubnumber.type.UBNat32;

/**
 * XBUP protocol level 1 attribute token.
 *
 * This class carry single UBNatural value.
 *
 * @version 0.2.1 2017/05/22
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
public abstract class XBTAttributeToken implements XBTToken {

    @Nonnull
    public abstract XBAttribute getAttribute();

    /**
     * Returns true if this is attribute is zero.
     *
     * @return true if attribute is zero
     */
    public abstract boolean isZero();

    @Nonnull
    @Override
    public XBTTokenType getTokenType() {
        return XBTTokenType.ATTRIBUTE;
    }

    @Nonnull
    public static XBTAttributeToken create(XBAttribute attribute) {
        return new XBTAttributeTokenImpl(attribute);
    }

    @ParametersAreNonnullByDefault
    private static class XBTAttributeTokenImpl extends XBTAttributeToken {

        @Nonnull
        private final XBAttribute attribute;

        private XBTAttributeTokenImpl(XBAttribute attribute) {
            this.attribute = attribute;
        }

        @Nonnull
        @Override
        public XBAttribute getAttribute() {
            return attribute;
        }

        @Override
        public boolean isZero() {
            return attribute.isNaturalZero();
        }

        @Nonnull
        @Override
        public XBTTokenType getTokenType() {
            return XBTTokenType.ATTRIBUTE;
        }
    }

    @Nullable
    private static XBTZeroAttributeToken instance = null;

    @Nonnull
    public static XBTAttributeToken createZeroToken() {
        if (instance == null) {
            instance = new XBTZeroAttributeToken();
        }

        return instance;
    }

    @ParametersAreNonnullByDefault
    private static class XBTZeroAttributeToken extends XBTAttributeToken {

        private XBTZeroAttributeToken() {
        }

        @Nonnull
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
