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
package org.xbup.lib.core.catalog.base;

import java.util.Date;

/**
 * Interface for catalog user information entity.
 *
 * @version 0.1.22 2012/09/09
 * @author XBUP Project (http://xbup.org)
 */
public interface XBCXUserInfo extends XBCBase {

    /**
     * Get related user.
     *
     * @return the user
     */
    public XBCXUser getUser();

    /**
     * Get date of creation.
     *
     * @return the created
     */
    public Date getCreated();

    /**
     * Get date of current login.
     *
     * @return the currLogin
     */
    public Date getCurrLogin();

    /**
     * Get date of last login.
     *
     * @return the lastLogin
     */
    public Date getLastLogin();

    /**
     * Get date of last change.
     *
     * @return the updated
     */
    public Date getUpdated();
}
