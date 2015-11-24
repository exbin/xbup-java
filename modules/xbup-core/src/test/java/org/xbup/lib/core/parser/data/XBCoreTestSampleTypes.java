/*
 * Copyright (C) XBUP Project
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
package org.xbup.lib.core.parser.data;

import org.xbup.lib.core.type.XBString;

/**
 * Sample data and methods for testing purposes.
 *
 * @version 0.2.0 2015/11/24
 * @author XBUP Project (http://xbup.org)
 */
public class XBCoreTestSampleTypes {

    public final static String SAMPLE_TYPES_PATH = "/org/xbup/lib/core/resources/test/samples/types/";

    // Undefined types
    public final static String SAMPLE_UNDEFINED_TYPES_PATH = SAMPLE_TYPES_PATH + "undefined/";
    public final static String SAMPLE_UNDEFINED_STRING = SAMPLE_UNDEFINED_TYPES_PATH + "string.xb";
    public final static String SAMPLE_UNDEFINED_STRING_TERMINATED = SAMPLE_UNDEFINED_TYPES_PATH + "string_terminated.xb";
    
    /**
     * Returns matching object for "string.xb".
     * 
     * @return type object
     */
    public static XBString getSampleTypeUndefinedString() {
        return new XBString("TEST");
    }

    /**
     * Returns matching object for "string_terminated.xb".
     * 
     * @return type object
     */
    public static XBString getSampleTypeUndefinedStringTerminated() {
        return new XBString("TEST");
    }
}
