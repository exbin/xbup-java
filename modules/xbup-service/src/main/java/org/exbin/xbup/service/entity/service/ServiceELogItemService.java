/*
 * Copyright (C) ExBin Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.exbin.xbup.service.entity.service;

import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import org.exbin.xbup.core.catalog.base.service.XBCDefaultItemService;
import org.exbin.xbup.service.base.ServiceLogItem;
import org.exbin.xbup.service.base.service.ServiceLogItemService;
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
public class ServiceELogItemService extends XBCDefaultItemService<ServiceLogItem> implements ServiceLogItemService, Serializable {

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
