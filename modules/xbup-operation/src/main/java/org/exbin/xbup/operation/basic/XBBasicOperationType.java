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
package org.exbin.xbup.operation.basic;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Document operation type enumeration.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public enum XBBasicOperationType {

    /**
     * Block added.
     */
    ADD_BLOCK("Add block"),
    /**
     * Block deleted.
     */
    DELETE_BLOCK("Delete block"),
    /**
     * Block modified.
     */
    MODIFY_BLOCK("Modify block"),
    /**
     * Block moved.
     */
    MOVE_BLOCK("Move block");

    @Nonnull
    private final String caption;

    private XBBasicOperationType(String caption) {
        this.caption = caption;
    }

    @Nonnull
    public String getCaption() {
        return caption;
    }
}
