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
package org.exbin.xbup.client;

import javax.annotation.Nonnull;
import org.exbin.xbup.core.parser.basic.XBListener;
import org.exbin.xbup.core.parser.basic.XBMatchingProvider;
import org.exbin.xbup.core.parser.token.event.XBEventListener;
import org.exbin.xbup.core.parser.token.pull.XBPullProvider;

/**
 * Catalog service message interface.
 *
 * @version 0.2.1 2017/05/25
 * @author ExBin Project (http://exbin.org)
 */
public interface XBCatalogServiceMessage {

    /**
     * Gets output stream.
     *
     * @return token output stream
     */
    @Nonnull
    XBEventListener getXBOutputStream();

    /**
     * Gets input stream.
     *
     * @return token input stream
     */
    @Nonnull
    XBPullProvider getXBInputStream();

    /**
     * Gets output listener.
     *
     * @return listener
     */
    @Nonnull
    XBListener getXBOutput();

    /**
     * Gets input checker.
     *
     * @return input stream checker
     */
    @Nonnull
    XBMatchingProvider getXBInput();

    /**
     * Closes message.
     */
    public void close();
}
