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
import org.exbin.xbup.core.block.XBBlockType;

/**
 * XBUP protocol level 1 block type token.
 *
 * @version 0.2.1 2017/05/22
 * @author ExBin Project (http://exbin.org)
 */
public abstract class XBTTypeToken implements XBTToken {

    @Nonnull
    public abstract XBBlockType getBlockType();

    @Override
    @Nonnull
    public XBTTokenType getTokenType() {
        return XBTTokenType.TYPE;
    }

    @Nonnull
    public static XBTTypeToken create(@Nonnull XBBlockType blockType) {
        return new XBTTypeTokenImpl(blockType);
    }

    private static class XBTTypeTokenImpl extends XBTTypeToken {

        @Nonnull
        private final XBBlockType blockType;

        private XBTTypeTokenImpl(@Nonnull XBBlockType blockType) {
            this.blockType = blockType;
        }

        @Nonnull
        @Override
        public XBBlockType getBlockType() {
            return blockType;
        }
    }
}
