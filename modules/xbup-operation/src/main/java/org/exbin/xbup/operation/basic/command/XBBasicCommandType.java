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
package org.exbin.xbup.operation.basic.command;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.operation.command.CommandType;

/**
 * Document command type enumeration.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public enum XBBasicCommandType implements CommandType {

    /**
     * Block added.
     */
    BLOCK_ADDED("Block added"),
    /**
     * Block deleted.
     */
    BLOCK_DELETED("Block deleted"),
    /**
     * Block modified.
     */
    BLOCK_MODIFIED("Block modified"),
    /**
     * Block moved.
     */
    BLOCK_MOVED("Block moved");

    private final String name;

    private XBBasicCommandType(String name) {
        this.name = name;
    }

    @Nonnull
    @Override
    public String getName() {
        return name;
    }
}
