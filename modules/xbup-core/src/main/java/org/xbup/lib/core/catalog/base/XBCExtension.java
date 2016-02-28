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
package org.xbup.lib.core.catalog.base;

/**
 * Interface for extensions - used for catalog extensions.
 *
 * This is probably obsolote by XManager items.
 *
 * @version 0.1.21 2011/12/29
 * @author ExBin Project (http://exbin.org)
 */
public interface XBCExtension {

    /**
     * Method for initializing given extension.
     *
     * It's executed when extension is loaded.
     */
    public void initializeExtension();

    /**
     * Gets some name for give extension.
     *
     * @return the name string of this extension
     */
    public String getExtensionName();
}
