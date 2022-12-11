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
package org.exbin.xbup.core.serial.child;

import java.io.IOException;
import java.io.InputStream;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.core.block.XBBlockTerminationMode;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.parser.token.XBAttribute;
import org.exbin.xbup.core.serial.XBSerializable;

/**
 * XBUP level 0 child serialization provider interface.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public interface XBChildProvider {

    /**
     * Gets beggining of block.
     *
     * @return terminated block flag
     * @throws XBProcessingException if not matching
     * @throws IOException if input/output exception occurs
     */
    @Nonnull
    XBBlockTerminationMode begin() throws XBProcessingException, IOException;

    /**
     * Gets block attribute.
     *
     * @return attribute value
     * @throws XBProcessingException if not matching
     * @throws IOException if input/output exception occurs
     */
    @Nonnull
    XBAttribute nextAttribute() throws XBProcessingException, IOException;

    /**
     * Gets block child.
     *
     * @param child serializable block
     * @throws XBProcessingException if not matching
     * @throws IOException if input/output exception occurs
     */
    void nextChild(XBSerializable child) throws XBProcessingException, IOException;

    /**
     * Gets block data.
     *
     * @return block data stream
     * @throws XBProcessingException if not matching
     * @throws IOException if input/output exception occurs
     */
    @Nonnull
    InputStream nextData() throws XBProcessingException, IOException;

    /**
     * Gets end of block.
     *
     * @throws XBProcessingException if not matching
     * @throws IOException if input/output exception occurs
     */
    void end() throws XBProcessingException, IOException;
}
