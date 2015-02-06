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
package org.xbup.lib.core.parser.token.event;

import java.io.IOException;
import java.util.EventListener;
import org.xbup.lib.core.parser.XBProcessingException;
import org.xbup.lib.core.parser.token.XBTToken;

/**
 * Interface for receiving XBUP protocol level 1 events.
 *
 * @version 0.1.23 2013/11/22
 * @author XBUP Project (http://xbup.org)
 */
public interface XBTEventListener extends EventListener {

    /**
     * Puts next token.
     *
     * @param token
     * @throws XBProcessingException
     * @throws IOException
     */
    public void putXBTToken(XBTToken token) throws XBProcessingException, IOException;

}
