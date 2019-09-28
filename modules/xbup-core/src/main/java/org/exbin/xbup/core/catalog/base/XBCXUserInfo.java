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
package org.exbin.xbup.core.catalog.base;

import java.util.Date;

/**
 * Interface for catalog user information entity.
 *
 * @version 0.1.22 2012/09/09
 * @author ExBin Project (http://exbin.org)
 */
public interface XBCXUserInfo extends XBCBase {

    /**
     * Gets related user.
     *
     * @return the user
     */
    XBCXUser getUser();

    /**
     * Gets date of creation.
     *
     * @return the created
     */
    Date getCreated();

    /**
     * Gets date of current login.
     *
     * @return the currLogin
     */
    Date getCurrLogin();

    /**
     * Gets date of last login.
     *
     * @return the lastLogin
     */
    Date getLastLogin();

    /**
     * Gets date of last change.
     *
     * @return the updated
     */
    Date getUpdated();
}
