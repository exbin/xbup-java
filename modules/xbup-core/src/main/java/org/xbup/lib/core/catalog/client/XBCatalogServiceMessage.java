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
package org.xbup.lib.core.catalog.client;

import org.xbup.lib.core.parser.basic.XBListener;
import org.xbup.lib.core.stream.XBTokenInputStream;
import org.xbup.lib.core.stream.XBTokenOutputStream;
import org.xbup.lib.core.stream.XBStreamChecker;

/**
 * Catalog service message interface.
 *
 * @version 0.1.17 2009/06/07
 * @author XBUP Project (http://xbup.org)
 */
public interface XBCatalogServiceMessage {

    /**
     * Gets output stream.
     *
     * @return token output stream
     */
    public XBTokenOutputStream getXBOutputStream();

    /**
     * Gets input stream.
     *
     * @return token input stream
     */
    public XBTokenInputStream getXBInputStream();

    /**
     * Gets output listener.
     *
     * @return listener
     */
    public XBListener getXBOutput();

    /**
     * Gets input checker.
     *
     * @return input stream checker
     */
    public XBStreamChecker getXBInput();

    /**
     * Closes message.
     */
    public void close();
}
