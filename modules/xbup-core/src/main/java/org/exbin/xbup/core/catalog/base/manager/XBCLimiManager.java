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
package org.exbin.xbup.core.catalog.base.manager;

import org.exbin.xbup.core.catalog.base.XBCItemLimi;

/**
 * Interface for XBCItemLimi catalog manager.
 *
 * @version 0.1.21 2011/12/29
 * @author ExBin Project (http://exbin.org)
 * @param <T> item limitation entity
 */
public interface XBCLimiManager<T extends XBCItemLimi> extends XBCCatalogManager<T> {
}
