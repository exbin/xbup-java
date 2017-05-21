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
import org.exbin.xbup.core.block.XBBlockTerminationMode;
import org.exbin.xbup.core.ubnumber.UBNatural;

/**
 * XBUP protocol level 0 size precomputed begin token.
 *
 * Class marks beggining of block. Terminated flag carry information about
 * method for block termination type in bitstream.
 *
 * Additional blockSize represents precomputed size of block if available.
 *
 * @version 0.2.1 2017/05/21
 * @author ExBin Project (http://exbin.org)
 */
public class XBSBeginToken extends XBToken {

    @Nonnull
    private final XBBlockTerminationMode terminationMode;
    @Nullable
    private UBNatural blockSize;

    private XBSBeginToken(@Nonnull XBBlockTerminationMode terminationMode, @Nullable UBNatural blockSize) {
        this.terminationMode = terminationMode;
        this.blockSize = blockSize;
    }

    @Override
    public XBTokenType getTokenType() {
        return XBTokenType.BEGIN;
    }

    public XBBlockTerminationMode getTerminationMode() {
        return terminationMode;
    }

    @Nullable
    public UBNatural getBlockSize() {
        return blockSize;
    }

    public void setBlockSize(@Nullable UBNatural blockSize) {
        this.blockSize = blockSize;
    }

    @Nonnull
    public static XBSBeginToken create(@Nonnull XBBlockTerminationMode terminationMode, @Nullable UBNatural blockSize) {
        return new XBSBeginToken(terminationMode, blockSize);
    }
}
