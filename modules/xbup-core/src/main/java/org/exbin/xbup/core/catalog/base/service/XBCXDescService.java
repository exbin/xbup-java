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
package org.exbin.xbup.core.catalog.base.service;

import java.util.List;
import javax.annotation.Nonnull;
import org.exbin.xbup.core.catalog.base.XBCExtension;
import org.exbin.xbup.core.catalog.base.XBCItem;
import org.exbin.xbup.core.catalog.base.XBCXDesc;
import org.exbin.xbup.core.catalog.base.XBCXLanguage;

/**
 * Interface for XBCXDesc items service.
 *
 * @author ExBin Project (https://exbin.org)
 */
public interface XBCXDescService extends XBCService<XBCXDesc>, XBCExtension {

    /**
     * Returns description for given item in default language.
     *
     * @param item item
     * @return description
     */
    XBCXDesc getDefaultItemDesc(XBCItem item);

    /**
     * Returns description for given item and language.
     *
     * @param item item
     * @param language language
     * @return description
     */
    XBCXDesc getItemDesc(XBCItem item, XBCXLanguage language);

    /**
     * Returns list of names for given item.
     *
     * @param item item
     * @return description
     */
    @Nonnull
    List<XBCXDesc> getItemDescs(XBCItem item);

    /**
     * Gets description text for default language and given item.
     *
     * @param item item to get name of
     * @return text or null if name is not set
     */
    String getDefaultText(XBCItem item);
}
