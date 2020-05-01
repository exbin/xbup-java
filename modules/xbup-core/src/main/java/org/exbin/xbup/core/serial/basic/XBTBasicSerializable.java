/*
 * Copyright (C) ExBin Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.exbin.xbup.core.serial.basic;

import java.io.IOException;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.serial.XBSerializable;

/**
 * Interface is providing serialization methods for serialization from and into
 * XBUP level 1 protocol using basic parser.
 *
 * @version 0.2.1 2017/05/16
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
public interface XBTBasicSerializable extends XBSerializable {

    /**
     * Performs serialization from XBUP protocol.
     *
     * @param serializationHandler serialization resource
     * @throws XBProcessingException if proccesing problem encountered
     * @throws IOException if input/output problem encountered
     */
    void serializeFromXB(XBTBasicInputSerialHandler serializationHandler) throws XBProcessingException, IOException;

    /**
     * Performs serialization to XBUP protocol.
     *
     * @param serializationHandler serialization resource
     * @throws XBProcessingException if proccesing problem encountered
     * @throws IOException if input/output problem encountered
     */
    void serializeToXB(XBTBasicOutputSerialHandler serializationHandler) throws XBProcessingException, IOException;
}
