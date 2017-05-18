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
import org.exbin.xbup.core.block.XBBlockTerminationMode;

/**
 * XBUP protocol level 0 begin token.
 *
 * Class marks beggining of block. Terminated flag carry information about
 * method for block termination type in bitstream.
 *
 * @version 0.2.1 2017/05/18
 * @author ExBin Project (http://exbin.org)
 */
public class XBBeginToken extends XBToken {

    private static XBBeginToken sizeSpecifiedBeginToken = null;
    private static XBBeginToken terminatedByZeroBeginToken = null;

    @Nonnull
    private final XBBlockTerminationMode terminationMode;

    public XBBeginToken(@Nonnull XBBlockTerminationMode terminationMode) {
        this.terminationMode = terminationMode;
    }

    @Nonnull
    public XBBlockTerminationMode getTerminationMode() {
        return terminationMode;
    }

    @Override
    @Nonnull
    public XBTokenType getTokenType() {
        return XBTokenType.BEGIN;
    }

    @Nonnull
    public static XBBeginToken getInstance(@Nonnull XBBlockTerminationMode terminationMode) {
        switch (terminationMode) {
            case SIZE_SPECIFIED:
                return getSizeSpecifiedInstance();
            case TERMINATED_BY_ZERO:
                return getTerminatedByZeroInstance();
            default:
                throw new IllegalStateException("Unexpected termination mode");
        }
    }

    @Nonnull
    public static XBBeginToken getSizeSpecifiedInstance() {
        if (sizeSpecifiedBeginToken == null) {
            sizeSpecifiedBeginToken = new XBBeginToken(XBBlockTerminationMode.SIZE_SPECIFIED);
        }

        return sizeSpecifiedBeginToken;
    }

    @Nonnull
    public static XBBeginToken getTerminatedByZeroInstance() {
        if (terminatedByZeroBeginToken == null) {
            terminatedByZeroBeginToken = new XBBeginToken(XBBlockTerminationMode.TERMINATED_BY_ZERO);
        }

        return terminatedByZeroBeginToken;
    }
}
