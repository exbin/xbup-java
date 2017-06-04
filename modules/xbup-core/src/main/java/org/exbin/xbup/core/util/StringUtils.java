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
package org.exbin.xbup.core.util;

import javax.annotation.Nullable;

/**
 * Utilities for stream data manipulations.
 *
 * @version 0.2.1 2017/05/12
 * @author ExBin Project (http://exbin.org)
 */
public abstract class StringUtils {

    /**
     * Compares two strings including nulls.
     *
     * @param string first string
     * @param matchString second string
     * @return true if both null or equals
     */
    public static boolean stringsEquals(@Nullable String string, @Nullable String matchString) {
        if (string == null) {
            return matchString == null;
        }

        return string.equals(matchString);
    }

    /**
     * Compares two strings ignoring case including nulls.
     *
     * @param string first string
     * @param matchString second string
     * @return true if both null or equals ignoring case sensitivity
     */
    public static boolean stringsEqualsIgnoreCase(@Nullable String string, @Nullable String matchString) {
        if (string == null) {
            return matchString == null;
        }

        return string.equalsIgnoreCase(matchString);
    }
}
