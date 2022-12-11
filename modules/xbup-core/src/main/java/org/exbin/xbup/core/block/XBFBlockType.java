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
package org.exbin.xbup.core.block;

import javax.annotation.Nonnull;
import org.exbin.xbup.core.ubnumber.UBNatural;

/**
 * Interface for block type represented by group id and block id.
 *
 * @author ExBin Project (https://exbin.org)
 */
public interface XBFBlockType extends XBBlockType {

    /**
     * Gets group ID of this block.
     *
     * @return value of group index
     */
    @Nonnull
    UBNatural getGroupID();

    /**
     * Gets block ID of this block.
     *
     * @return value of block index
     */
    @Nonnull
    UBNatural getBlockID();
}
