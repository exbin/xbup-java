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
package org.xbup.web.xbcatalogweb.base.manager;

import java.io.Serializable;
import java.util.List;
import org.xbup.lib.xb.catalog.base.manager.XBCManager;
import org.xbup.lib.xbcatalog.entity.XBEXUser;
import org.xbup.web.xbcatalogweb.base.XBCUserRecord;

/**
 * XBUP catalog XBEUserRecord manager interface.
 *
 * @version 0.1 wr23.0 2014/05/23
 * @author XBUP Project (http://xbup.org)
 */
public interface XBCUserRecordManager extends XBCManager<XBCUserRecord>, Serializable {

    List<XBCUserRecord> findAllPaged(int startFrom, int maxResults, String filterCondition, String orderCondition);

    int findAllPagedCount(String filterCondition);

    public void persistUser(XBEXUser user);

}
