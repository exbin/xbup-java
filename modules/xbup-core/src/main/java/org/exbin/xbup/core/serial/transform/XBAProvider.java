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
package org.exbin.xbup.core.serial.transform;

import java.io.IOException;
import java.util.List;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.core.block.XBBlockType;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.serial.param.XBPProvider;

/**
 * TODO: XBUP level 2 child serialization provider interface.
 *
 * @version 0.2.1 2017/06/04
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
public interface XBAProvider extends XBPProvider {

    /**
     * Pulls and matches block type.
     *
     * @param blockType list of block types to match to
     * @return block type
     * @throws XBProcessingException if not matching
     * @throws IOException if input/output exception occurs
     */
    @Nullable
    XBBlockType pullMatchingType(XBBlockType blockType) throws XBProcessingException, IOException;

    /**
     * Pulls and matches one of block types.
     *
     * @param blockTypes list of block types to match to
     * @return block type
     * @throws XBProcessingException if not matching
     * @throws IOException if input/output exception occurs
     */
    @Nullable
    XBBlockType pullMatchingType(@Nullable List<XBBlockType> blockTypes) throws XBProcessingException, IOException;
}
