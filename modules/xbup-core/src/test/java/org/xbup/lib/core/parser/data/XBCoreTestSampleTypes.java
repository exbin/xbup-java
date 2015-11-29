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
import org.xbup.lib.core.ubnumber.type.UBInt32;
import org.xbup.lib.core.ubnumber.type.UBNat32;
import org.xbup.lib.core.ubnumber.type.UBRea;

/**
 * Sample data and methods for testing purposes.
 *
 * @version 0.2.0 2015/11/29
 * @author XBUP Project (http://xbup.org)
 */
public class XBCoreTestSampleTypes {

    public final static String SAMPLE_TYPES_PATH = "/org/xbup/lib/core/resources/test/samples/types/";

    // Undefined types
    public final static String SAMPLE_UNDEFINED_TYPES_PATH = SAMPLE_TYPES_PATH + "undefined/";
    public final static String SAMPLE_UNDEFINED_STRING = SAMPLE_UNDEFINED_TYPES_PATH + "string.xb";
    public final static String SAMPLE_UNDEFINED_STRING_TERMINATED = SAMPLE_UNDEFINED_TYPES_PATH + "string_terminated.xb";
    public final static String SAMPLE_UNDEFINED_NATURAL = SAMPLE_UNDEFINED_TYPES_PATH + "natural.xb";
    public final static String SAMPLE_UNDEFINED_NATURAL_TERMINATED = SAMPLE_UNDEFINED_TYPES_PATH + "natural_terminated.xb";
    public final static String SAMPLE_UNDEFINED_INTEGER = SAMPLE_UNDEFINED_TYPES_PATH + "integer.xb";
    public final static String SAMPLE_UNDEFINED_INTEGER_TERMINATED = SAMPLE_UNDEFINED_TYPES_PATH + "integer_terminated.xb";
    public final static String SAMPLE_UNDEFINED_REAL = SAMPLE_UNDEFINED_TYPES_PATH + "real.xb";
    public final static String SAMPLE_UNDEFINED_REAL_TERMINATED = SAMPLE_UNDEFINED_TYPES_PATH + "real_terminated.xb";

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

    /**
     * Returns matching object for "natural.xb".
     *
     * @return type object
     */
    public static UBNat32 getSampleTypeUndefinedNatural() {
        return new UBNat32(10);
    }

    /**
     * Returns matching object for "natural_terminated.xb".
     *
     * @return type object
     */
    public static UBNat32 getSampleTypeUndefinedNaturalTerminated() {
        return new UBNat32(10);
    }

    /**
     * Returns matching object for "integer.xb".
     *
     * @return type object
     */
    public static UBInt32 getSampleTypeUndefinedInteger() {
        return new UBInt32(10);
    }

    /**
     * Returns matching object for "integer_terminated.xb".
     *
     * @return type object
     */
    public static UBInt32 getSampleTypeUndefinedIntegerTerminated() {
        return new UBInt32(10);
    }

    /**
     * Returns matching object for "real.xb".
     *
     * @return type object
     */
    public static UBRea getSampleTypeUndefinedReal() {
        return new UBRea(2.5f);
    }

    /**
     * Returns matching object for "real_terminated.xb".
     *
     * @return type object
     */
    public static UBRea getSampleTypeUndefinedRealTerminated() {
        return new UBRea(2.5f);
    }
}
