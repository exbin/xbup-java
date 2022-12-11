/*
 * Copyright (C) ExBin Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.exbin.xbup.core.serial;

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
 * Method to perform dual-way serialization to XBUP protocol:
 *
 * public void serializeXB(XBSerialHandler serializationHandler) throws
 * XBProcessingException, IOException;
 *
 * @author ExBin Project (https://exbin.org)
 */
public interface XBSerializable {
}
