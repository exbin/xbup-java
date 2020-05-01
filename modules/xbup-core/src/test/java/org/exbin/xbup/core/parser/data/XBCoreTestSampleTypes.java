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
package org.exbin.xbup.core.parser.data;

import org.exbin.xbup.core.type.XBString;
import org.exbin.xbup.core.ubnumber.type.UBInt32;
import org.exbin.xbup.core.ubnumber.type.UBNat32;
import org.exbin.xbup.core.ubnumber.type.UBRea;

/**
 * Sample data and methods for testing purposes.
 *
 * @version 0.2.0 2015/11/29
 * @author ExBin Project (http://exbin.org)
 */
public class XBCoreTestSampleTypes {

    public final static String SAMPLE_TYPES_PATH = "/org/exbin/xbup/core/resources/test/samples/types/";

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
