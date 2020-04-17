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
import org.exbin.xbup.core.block.XBBlockTerminationMode;
import org.exbin.xbup.core.ubnumber.UBNatural;

/**
 * XBUP protocol level 1 size precomputed begin token.
 *
 * Class marks beggining of block. Terminated flag carry information about
 * method for block termination type in bitstream.
 *
 * Additional blockSize represents precomputed size of block if available.
 *
 * @version 0.2.1 2017/05/22
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XBTSBeginToken extends XBTBeginToken {

    @Nullable
    private UBNatural blockSize;
    @Nonnull
    private XBBlockTerminationMode terminationMode;

    private XBTSBeginToken(XBBlockTerminationMode terminationMode, @Nullable UBNatural blockSize) {
        this.terminationMode = terminationMode;
        this.blockSize = blockSize;
    }

    @Nonnull
    @Override
    public XBTTokenType getTokenType() {
        return XBTTokenType.BEGIN;
    }

    @Nonnull
    @Override
    public XBBlockTerminationMode getTerminationMode() {
        return terminationMode;
    }

    public void setTerminationMode(XBBlockTerminationMode terminationMode) {
        this.terminationMode = terminationMode;
    }

    @Nullable
    public UBNatural getBlockSize() {
        return blockSize;
    }

    public void setBlockSize(@Nullable UBNatural blockSize) {
        this.blockSize = blockSize;
    }

    @Nonnull
    public static XBTSBeginToken create(XBBlockTerminationMode terminationMode, @Nullable UBNatural blockSize) {
        return new XBTSBeginToken(terminationMode, blockSize);
    }
}
