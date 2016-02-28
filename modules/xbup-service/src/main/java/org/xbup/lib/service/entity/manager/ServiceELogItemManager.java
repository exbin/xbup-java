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
package org.xbup.lib.service.entity.manager;

import java.io.Serializable;
import javax.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import org.xbup.lib.catalog.entity.manager.XBEDefaultManager;
import org.xbup.lib.service.base.manager.ServiceLogItemManager;
import org.xbup.lib.service.entity.ServiceELogItem;

/**
 * XBUP catalog item manager.
 *
 * @version 0.2.0 2016/02/20
 * @author ExBin Project (http://exbin.org)
 */
@Repository
public class ServiceELogItemManager extends XBEDefaultManager<ServiceELogItem> implements ServiceLogItemManager<ServiceELogItem>, Serializable {

    public ServiceELogItemManager() {
        super();
    }

    public ServiceELogItemManager(EntityManager entityManager) {
        super(entityManager);
    }
}
