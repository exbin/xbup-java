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
package org.exbin.xbup.core.block.declaration;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.core.block.XBDBlockType;
import org.exbin.xbup.core.block.XBFBlockType;
import org.exbin.xbup.core.block.XBFixedBlockType;

/**
 * Interface for context related type conversions.
 *
 * @version 0.1.25 2015/02/04
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
public interface XBTypeConvertor {

    /**
     * Returns group representation for given group ID.
     *
     * @param groupId group identification
     * @return group representation
     */
    @Nullable
    XBGroup getGroupForId(int groupId);

    /**
     * Converts fixed block type to declared type.
     *
     * @param fixedType fixed block type
     * @return declared block type of null if no match found
     */
    @Nullable
    XBDeclBlockType getDeclBlockType(XBFBlockType fixedType);

    /**
     * Converts declared type to fixed block type.
     *
     * @param declType declared block type
     * @return fixed block type or null if no match found
     */
    @Nullable
    XBFixedBlockType getFixedBlockType(XBDBlockType declType);

    /**
     * Converts declared type to fixed block type.
     *
     * @param blockDecl block declaration
     * @param groupIdLimit maximum group ID to limit group search
     * @return fixed block type or null if no match found
     */
    @Nullable
    XBFixedBlockType getFixedBlockType(XBBlockDecl blockDecl, int groupIdLimit);
}
