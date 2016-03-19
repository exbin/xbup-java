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
package org.exbin.xbup.core.parser.token.event;

import java.io.IOException;
import org.exbin.xbup.core.parser.XBProcessingException;
import org.exbin.xbup.core.parser.token.XBTToken;
import org.exbin.xbup.core.stream.XBInput;

/**
 * Interface for receiving XBUP protocol level 1 events.
 *
 * @version 0.1.25 2015/02/13
 * @author ExBin Project (http://exbin.org)
 */
public interface XBTEventListener extends XBInput {

    /**
     * Puts next token.
     *
     * @param token
     * @throws XBProcessingException
     * @throws IOException
     */
    public void putXBTToken(XBTToken token) throws XBProcessingException, IOException;

}
