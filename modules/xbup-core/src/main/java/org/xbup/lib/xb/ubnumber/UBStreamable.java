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
package org.xbup.lib.xb.ubnumber;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import org.xbup.lib.xb.parser.XBProcessingException;

/**
 * This interface provides methods for object conversion from and to stream in
 * XBUP protocol stack in its native form.
 *
 * @version 0.1 wr23.0 2014/01/27
 * @author XBUP Project (http://xbup.org)
 */
public interface UBStreamable extends Serializable {

    /**
     * Method for writting current value in UB Encoding to standard byte stream.
     *
     * @param stream Stream to write to
     * @return Count of bytes written to stream
     * @throws java.io.IOException if stream throws it
     */
    public int toStreamUB(OutputStream stream) throws IOException;

    /**
     * Method for reading value in UB Encoding from standard byte stream.
     *
     * @param stream Stream to read from
     * @return Count of bytes read from stream
     * @throws java.io.IOException if stream throws it
     */
    public int fromStreamUB(InputStream stream) throws IOException, XBProcessingException;

    /**
     * Method for getting size of data which would be saved in byte stream.
     *
     * @return Count of bytes which would be saved
     */
    public int getSizeUB();
}
