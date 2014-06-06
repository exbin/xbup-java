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
package org.xbup.lib.xb.parser.token.operation;

import java.io.IOException;
import org.xbup.lib.xb.parser.token.TypedToken;

/**
 * Token operator.
 *
 * @version 0.1 wr16.0 2008/08/14
 * @author XBUP Project (http://xbup.org)
 * @param <T> source token generic type
 * @param <U> target token generic type
 */
public abstract class XBTokenOperator<T extends TypedToken,U extends TypedToken> {

    /** Put single processed event */
    public abstract void putToken(U event) throws IOException;

    /** Get single event from source */
    public abstract T getToken() throws IOException;

    /** Reset processing */
    public abstract void reset();

    /** Get available capacity of input buffer */
    public abstract int availableInput();

    /** Get available capacity of output buffer */
    public abstract int availableOutput();

    /** Set request for minimum cache fill to operate */
    public abstract void request(int count);

    /** Set size of operator's input cache */
    public abstract void setInputCache(int size);

    /** Set size of operator's output cache */
    public abstract void setOutputCache(int size);
}
