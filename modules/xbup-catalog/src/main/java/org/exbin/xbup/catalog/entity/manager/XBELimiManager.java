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
package org.exbin.xbup.catalog.entity.manager;

import java.io.Serializable;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.catalog.XBECatalog;
import org.exbin.xbup.catalog.entity.XBEItemLimi;
import org.exbin.xbup.core.catalog.base.XBCItemLimi;
import org.exbin.xbup.core.catalog.base.manager.XBCLimiManager;
import org.springframework.stereotype.Repository;

/**
 * XBUP catalog limitation manager.
 *
 * @author ExBin Project (https://exbin.org)
 */
@ParametersAreNonnullByDefault
@Repository
public class XBELimiManager extends XBEDefaultCatalogManager<XBCItemLimi> implements XBCLimiManager, Serializable {

    public XBELimiManager() {
        super();
    }

    public XBELimiManager(XBECatalog catalog) {
        super(catalog);
    }

    @Nonnull
    @Override
    public Class getEntityClass() {
        return XBEItemLimi.class;
    }

}
