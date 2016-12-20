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
package org.exbin.xbup.service.entity.service;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import org.exbin.xbup.core.catalog.base.service.XBCDefaultItemService;
import org.exbin.xbup.service.base.service.ServiceLogItemService;
import org.exbin.xbup.service.entity.ServiceELogItem;
import org.exbin.xbup.service.entity.manager.ServiceELogItemManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Interface for XBEItem items service.
 *
 * @version 0.2.0 2016/02/20
 * @author ExBin Project (http://exbin.org)
 */
@Service
public class ServiceELogItemService extends XBCDefaultItemService<ServiceELogItem> implements ServiceLogItemService<ServiceELogItem>, Serializable {

    @Autowired
    private ServiceELogItemManager manager;

    public ServiceELogItemService() {
        super();
    }

    public ServiceELogItemService(EntityManager entityManager) {
        super();
        manager = new ServiceELogItemManager(entityManager);
    }

    @PostConstruct
    public void init() {
        itemManager = manager;
    }
}
