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
package org.xbup.lib.core.parser.token.operation;

import java.io.IOException;
import org.xbup.lib.core.parser.token.TypedToken;

/**
 * Token operator.
 *
 * @version 0.1.16 2008/08/14
 * @author XBUP Project (http://xbup.org)
 * @param <T> source token generic type
 * @param <U> target token generic type
 */
public abstract class XBTokenOperator<T extends TypedToken, U extends TypedToken> {

    /**
     * Puts single token.
     *
     * @param token
     * @throws IOException
     */
    public abstract void putToken(U token) throws IOException;

    /**
     * Gets single token from source.
     *
     * @return token
     * @throws IOException
     */
    public abstract T getToken() throws IOException;

    /**
     * Resets processing.
     */
    public abstract void reset();

    /**
     * Gets available capacity of input buffer.
     *
     * @return available capacity in bytes
     */
    public abstract int availableInput();

    /**
     * Gets available capacity of output buffer.
     *
     * @return available capacity in bytes
     */
    public abstract int availableOutput();

    /**
     * Sets request for minimum cache fill to operate.
     *
     * @param count
     */
    public abstract void request(int count);

    /**
     * Sets size of operator's input cache.
     *
     * @param size size in bytes
     */
    public abstract void setInputCache(int size);

    /**
     * Sets size of operator's output cache.
     *
     * @param size size in bytes
     */
    public abstract void setOutputCache(int size);
}
