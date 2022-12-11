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
package org.exbin.xbup.core.catalog;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Enumeration of plugin UI type.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public enum XBPlugUiType {
    COMPONENT_VIEWER(1, "Component Viewer"),
    COMPONENT_EDITOR(2, "Component Editor"),
    PANEL_VIEWER(3, "Panel Viewer"),
    PANEL_EDITOR(4, "Panel Editor"),
    ROW_EDITOR(5, "Row Editor");

    private final int dbIndex;
    private final String name;

    private XBPlugUiType(int dbIndex, String name) {
        this.dbIndex = dbIndex;
        this.name = name;
    }

    public int getDbIndex() {
        return dbIndex;
    }

    @Nonnull
    public String getName() {
        return name;
    }

    @Nonnull
    public static XBPlugUiType findByDbIndex(int dbIndex) {
        for (XBPlugUiType type : values()) {
            if (type.getDbIndex() == dbIndex) {
                return type;
            }
        }

        throw new IllegalArgumentException("No type match given DB index");
    }
}
