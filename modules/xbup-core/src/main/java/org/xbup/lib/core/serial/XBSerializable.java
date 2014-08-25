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
package org.xbup.lib.core.serial;

/**
 * This is parent interface for interfaces providing serialization methods for
 * serialization from and into XBUP protocol.
 *
 * Child interfaces should implement at either two methods with arguments using
 * child interfaces of given interfaces or single method performing both
 * operations:
 *
 * Method to perform serialization from XBUP protocol:
 *
 * public void serializeFromXB(XBInputSerialHandler serializationHandler) throws
 * XBProcessingException, IOException;
 *
 * Method to perform serialization to XBUP protocol:
 *
 * public void serializeToXB(XBOutputSerialHandler serializationHandler) throws
 * XBProcessingException, IOException;
 *
 * Method to peform dual-way serialization to XBUP protocol:
 *
 * public void serializeXB(XBSerialHandler serializationHandler) throws
 * XBProcessingException, IOException;
 *
 * @version 0.1 wr24.0 2014/08/24
 * @author XBUP Project (http://xbup.org)
 */
public interface XBSerializable {
}
