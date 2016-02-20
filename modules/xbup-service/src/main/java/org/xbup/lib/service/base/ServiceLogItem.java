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
package org.xbup.lib.service.base;

import java.util.Date;
import org.xbup.lib.core.catalog.base.XBCBase;

/**
 * Interface for service log item entity.
 *
 * @version 0.2.0 2016/02/20
 * @author XBUP Project (http://xbup.org)
 */
public interface ServiceLogItem extends XBCBase {

    /**
     * Time of log entity creation.
     *
     * @return time
     */
    public Date getCreated();

    /**
     * Gets log request data.
     *
     * @return data
     */
    public byte[] getRequestData();

    /**
     * Gets log response data.
     *
     * @return data
     */
    public byte[] getResponseData();
}
