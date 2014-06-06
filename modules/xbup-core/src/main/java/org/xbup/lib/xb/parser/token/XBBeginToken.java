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
package org.xbup.lib.xb.parser.token;

import org.xbup.lib.xb.block.XBBlockTerminationMode;

/**
 * XBUP protocol level 0 begin token.
 * 
 * Class marks beggining of block. Terminated flag carry information about
 * method for block termination type in bitstream.
 *
 * @version 0.1 wr23.0 2014/02/01
 * @author XBUP Project (http://xbup.org)
 */
public class XBBeginToken extends XBToken {

    private final XBBlockTerminationMode terminationMode;

    public XBBeginToken(XBBlockTerminationMode terminationMode) {
        this.terminationMode = terminationMode;
    }

    public XBBlockTerminationMode getTerminationMode() {
        return terminationMode;
    }

    @Override
    public XBTokenType getTokenType() {
        return XBTokenType.BEGIN;
    }
}
