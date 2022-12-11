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

import javax.annotation.Nullable;

/**
 * Interface for block type.
 *
 * Block type can be represented in either as particular type from current
 * context or specific block type defined in catalog or local definition.
 *
 * @author ExBin Project (https://exbin.org)
 */
public interface XBBlockType {

    /**
     * Returns this type as basic type if it is matching or null.
     *
     * @return basic block type or null
     */
    @Nullable
    XBBasicBlockType getAsBasicType();
}
