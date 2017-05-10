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
package org.exbin.xbup.core.ubnumber;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import javax.annotation.Nonnull;

/**
 * This interface provides methods for object conversion from and to stream in
 * XBUP protocol stack in its native form.
 *
 * @version 0.2.1 2017/05/20
 * @author ExBin Project (http://exbin.org)
 */
public interface UBStreamable extends Serializable {

    /**
     * Writes current value in UB Encoding to standard byte stream.
     *
     * @param stream Stream to write to
     * @return count of bytes written to stream
     * @throws IOException if stream throws it
     */
    public int toStreamUB(@Nonnull OutputStream stream) throws IOException;

    /**
     * Reads value in UB Encoding from standard byte stream.
     *
     * @param stream Stream to read from
     * @return count of bytes read from stream
     * @throws IOException if stream throws it
     */
    public int fromStreamUB(@Nonnull InputStream stream) throws IOException;

    /**
     * Returns size of data which would be saved in byte stream.
     *
     * @return count of bytes which would be saved
     */
    public int getSizeUB();
}
