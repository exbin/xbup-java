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
package org.exbin.xbup.client.catalog.remote.manager;

import javax.annotation.ParametersAreNonnullByDefault;
import org.exbin.xbup.client.catalog.XBRCatalog;
import org.exbin.xbup.core.catalog.base.XBCItemLimi;
import org.exbin.xbup.core.catalog.base.manager.XBCLimiManager;

/**
 * Remote manager class for XBRLimit catalog items.
 *
 * @version 0.2.1 2020/08/26
 * @author ExBin Project (http://exbin.org)
 */
@ParametersAreNonnullByDefault
public class XBRLimiManager extends XBRDefaultManager<XBCItemLimi> implements XBCLimiManager {

    public XBRLimiManager(XBRCatalog catalog) {
        super(catalog);
    }
}
