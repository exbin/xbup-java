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
package org.exbin.xbup.core.ubnumber;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * This interface provides methods for object conversion from and to stream in
 * XBUP protocol stack in its native form.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
public interface UBStreamable extends Serializable {

    /**
     * Writes current value in UB Encoding to standard byte stream.
     *
     * @param stream Stream to write to
     * @return count of bytes written to stream
     * @throws IOException if stream throws it
     */
    int toStreamUB(OutputStream stream) throws IOException;

    /**
     * Reads value in UB Encoding from standard byte stream.
     *
     * @param stream Stream to read from
     * @return count of bytes read from stream
     * @throws IOException if stream throws it
     */
    int fromStreamUB(InputStream stream) throws IOException;

    /**
     * Returns size of data which would be saved in byte stream.
     *
     * @return count of bytes which would be saved
     */
    int getSizeUB();
}
