/*
 * Copyright (C) ExBin Project
 *
 * This application or library is free software: you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * This application or library is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along this application.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.xbup.lib.framework.gui.service.catalog.panel;

import java.util.HashMap;
import java.util.Map;

/**
 * Enumeration of specification definition's operation types.
 *
 * @version 0.2.0 2016/02/01
 * @author ExBin Project (http://exbin.org)
 */
public enum CatalogDefOperationType {

    CONSIST(0, "Consist"),
    JOIN(1, "Join"),
    ANY(2, "Any"),
    ATTRIBUTE(3, "Attribute"),
    CONSIST_LIST(4, "Consist[]"),
    JOIN_LIST(5, "Join[]"),
    ANY_LIST(6, "Any[]"),
    ATTRIBUTE_LIST(7, "Attribute[]");

    final int rowIndex;
    private final String caption;
    private static final Map<Integer, CatalogDefOperationType> map = new HashMap<>();

    static {
        for (CatalogDefOperationType operationType : CatalogDefOperationType.values()) {
            map.put(operationType.rowIndex, operationType);
        }
    }

    CatalogDefOperationType(int rowIndex, String caption) {
        this.rowIndex = rowIndex;
        this.caption = caption;
    }

    public static CatalogDefOperationType valueOf(int rowIndex) {
        return map.get(rowIndex);
    }

    public static String[] getAsArray() {
        return getAsArray(map.size());
    }

    public static String[] getAsArray(int rowsCount) {
        String[] rows = new String[rowsCount];
        for (int rowIndex = 0; rowIndex < rows.length; rowIndex++) {
            rows[rowIndex] = valueOf(rowIndex).caption;
        }
        return rows;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public String getCaption() {
        return caption;
    }
}
