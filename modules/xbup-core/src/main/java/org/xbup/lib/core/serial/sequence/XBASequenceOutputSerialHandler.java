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
package org.xbup.lib.core.serial.sequence;

import org.xbup.lib.core.serial.XBOutputSerialHandler;
import org.xbup.lib.core.serial.token.XBTTokenOutputSerialHandler;

/**
 * Interface for XBUP level 2 serialization output handler using serialization
 * sequence.
 *
 * @version 0.1.24 2014/11/29
 * @author XBUP Project (http://xbup.org)
 */
public interface XBASequenceOutputSerialHandler extends XBOutputSerialHandler, XBASerialSequenceable, XBTTokenOutputSerialHandler {
}
