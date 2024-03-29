/*
 * Copyright (C) ExBin Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.exbin.xbup.service.entity.manager;

import java.io.Serializable;
import javax.annotation.Nonnull;
import javax.persistence.EntityManager;
import org.exbin.xbup.catalog.entity.manager.XBEDefaultManager;
import org.exbin.xbup.service.base.ServiceLogItem;
import org.exbin.xbup.service.base.manager.ServiceLogItemManager;
import org.exbin.xbup.service.entity.ServiceELogItem;
import org.springframework.stereotype.Repository;

/**
 * XBUP catalog item manager.
 *
 * @author ExBin Project (https://exbin.org)
 */
@Repository
public class ServiceELogItemManager extends XBEDefaultManager<ServiceLogItem> implements ServiceLogItemManager, Serializable {

    public ServiceELogItemManager() {
        super();
    }

    public ServiceELogItemManager(EntityManager entityManager) {
        super(entityManager);
    }

    @Nonnull
    @Override
    public Class getEntityClass() {
        return ServiceELogItem.class;
    }
}
